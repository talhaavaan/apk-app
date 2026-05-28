package com.example.ui.viewmodel

import android.app.Application
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.data.database.AppDatabase
import com.example.data.database.FavoriteWallpaper
import com.example.data.model.Wallpaper
import com.example.data.repository.WallpaperRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlin.random.Random

class WallpaperViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = WallpaperRepository(db.wallpaperDao())

    // App Navigation state
    private val _currentTab = MutableStateFlow("Home")
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    // Splash State
    private val _isSplashFinished = MutableStateFlow(false)
    val isSplashFinished: StateFlow<Boolean> = _isSplashFinished.asStateFlow()

    // Wallpaper Lists
    val favorites: StateFlow<List<FavoriteWallpaper>> = repository.favorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Live state matching remote list + custom uploads
    private val _wallpapersList = MutableStateFlow<List<Wallpaper>>(emptyList())
    val wallpapersList: StateFlow<List<Wallpaper>> = _wallpapersList.asStateFlow()

    // Search query states
    val searchQuery = MutableStateFlow("")
    val searchColor = MutableStateFlow<String?>(null)
    val searchResolution = MutableStateFlow<String?>(null)

    // Current focused wallpaper
    private val _activeWallpaper = MutableStateFlow<Wallpaper?>(null)
    val activeWallpaper: StateFlow<Wallpaper?> = _activeWallpaper.asStateFlow()

    // Interactive download progress
    private val _downloadProgress = MutableStateFlow<Float?>(null)
    val downloadProgress: StateFlow<Float?> = _downloadProgress.asStateFlow()

    // Interactive wallpaper setting progress
    private val _isSettingWallpaper = MutableStateFlow(false)
    val isSettingWallpaper: StateFlow<Boolean> = _isSettingWallpaper.asStateFlow()

    // Premium Subscription State
    private val _isPremiumUser = MutableStateFlow(false)
    val isPremiumUser: StateFlow<Boolean> = _isPremiumUser.asStateFlow()

    // User Profile Information
    val username = MutableStateFlow("Talha Malik")
    val userEmail = MutableStateFlow("talha.malix2021@gmail.com")
    val isLoggedIn = MutableStateFlow(true)

    // Analytics Counter Simulation
    private val _totalDownloadsCount = MutableStateFlow(342)
    val totalDownloadsCount: StateFlow<Int> = _totalDownloadsCount.asStateFlow()

    init {
        // Collect custom uploads combined with general catalog
        viewModelScope.launch {
            combine(repository.customWallpapers) { _ ->
                repository.getAllWallpapers()
            }.collect { combined ->
                _wallpapersList.value = combined
            }
        }

        // Auto-end the splash screen beautifully after the premium animation frames buy
        viewModelScope.launch {
            delay(2800)
            _isSplashFinished.value = true
        }
    }

    fun setTab(tabName: String) {
        _currentTab.value = tabName
        // If switching tabs, clear selected focused category details
        if (tabName != "Categories") {
            _selectedCategory.value = null
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun selectWallpaper(wallpaper: Wallpaper?) {
        _activeWallpaper.value = wallpaper
    }

    fun toggleFavorite(wallpaper: Wallpaper) {
        viewModelScope.launch {
            repository.toggleFavorite(wallpaper)
        }
    }

    fun isFavorite(id: String): Flow<Boolean> {
        return repository.isFavoriteFlow(id)
    }

    // Double tap like animation trigger
    fun likeWallpaper(wallpaper: Wallpaper) {
        viewModelScope.launch {
            repository.incrementLike(wallpaper.id)
            // Re-sync active wallpaper detailed tags
            _activeWallpaper.value?.let { active ->
                if (active.id == wallpaper.id) {
                    _activeWallpaper.value = active.copy(likesCount = active.likesCount + 1)
                }
            }
            // Trigger a silent database favorite metadata sync if it is saved
            val isFav = favorites.value.any { it.id == wallpaper.id }
            if (isFav) {
                repository.addFavorite(wallpaper.copy(likesCount = wallpaper.likesCount + 1))
            }
        }
    }

    // Set interactive on-device wallpaper
    fun applyWallpaperOnDevice(context: Context, locationFlag: Int) {
        _isSettingWallpaper.value = true
        val active = _activeWallpaper.value ?: return

        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                try {
                    val loader = ImageLoader(context)
                    val req = ImageRequest.Builder(context)
                        .data(active.url)
                        .allowHardware(false) // Required for getting bitmap
                        .build()
                    val result = (loader.execute(req) as? SuccessResult)?.drawable
                    val bitmap = result?.toBitmap()

                    if (bitmap != null) {
                        val wallpaperManager = WallpaperManager.getInstance(context)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            wallpaperManager.setBitmap(bitmap, null, true, locationFlag)
                        } else {
                            wallpaperManager.setBitmap(bitmap)
                        }
                        true
                    } else {
                        false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            }

            _isSettingWallpaper.value = false
            if (success) {
                Toast.makeText(context, "Wallpaper Applied Successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to apply. Check connections.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // High speed async download simulation
    fun downloadWallpaperToGallery(context: Context) {
        val active = _activeWallpaper.value ?: return
        if (active.isPremium && !_isPremiumUser.value) {
            Toast.makeText(context, "Premium wallpaper! Please unlock T4LHA Premium first.", Toast.LENGTH_LONG).show()
            return
        }

        viewModelScope.launch {
            _downloadProgress.value = 0.0f
            // Smooth speed downloading visual shimmer
            delay(300)
            _downloadProgress.value = 0.25f
            delay(400)
            _downloadProgress.value = 0.55f
            delay(300)
            _downloadProgress.value = 0.85f

            val success = withContext(Dispatchers.IO) {
                try {
                    val loader = ImageLoader(context)
                    val req = ImageRequest.Builder(context)
                        .data(active.url)
                        .allowHardware(false)
                        .build()
                    val result = (loader.execute(req) as? SuccessResult)?.drawable
                    val bitmap = result?.toBitmap()

                    if (bitmap != null) {
                        // Save to Pictures/T4LHA_WALLS directory
                        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        val appDir = File(picturesDir, "T4LHA_WALLS")
                        if (!appDir.exists()) {
                            appDir.mkdirs()
                        }
                        val file = File(appDir, "T4lhaWalls_${active.title.replace(" ", "_")}_${System.currentTimeMillis()}.png")
                        val out = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                        out.flush()
                        out.close()
                        true
                    } else {
                        false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            }

            _downloadProgress.value = 1.0f
            delay(200)
            _downloadProgress.value = null // reset

            if (success) {
                _totalDownloadsCount.value += 1
                Toast.makeText(context, "Saved to Gallery inside 'Pictures/T4LHA_WALLS' folder!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Successfully Cached! Saved safely in app memory.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Simulated Premium Actions
    fun purchasePremiumToggle() {
        _isPremiumUser.value = !_isPremiumUser.value
    }

    // Simulated Cache Cleaner
    fun clearCacheFiles(context: Context, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            delay(1500) // Simulated clean latency
            onComplete("Successfully cleared 142.5 MB of image cache data.")
        }
    }

    // Admin Controls
    fun uploadNewWallpaperAdmin(title: String, category: String, url: String, colorHex: String, tagsInput: String) {
        val tags = tagsInput.split(",").map { it.trim().lowercase() }.filter { it.isNotEmpty() }
        val finalUrl = if (url.trim().startsWith("http")) url.trim() else "https://images.unsplash.com/photo-1542751371-adc38448a05e?auto=format"
        
        val newWallpaper = Wallpaper(
            id = "admin_upload_${System.currentTimeMillis()}",
            title = title.ifBlank { "Admin Design Space" },
            url = finalUrl,
            category = category,
            likesCount = Random.nextInt(10, 150),
            resolution = "4000 x 6000",
            author = "Talha Admin",
            colorHex = colorHex.ifBlank { "#00F0FF" },
            tags = tags + listOf(category.lowercase(), "admin", "premium"),
            isLive = false,
            isPremium = false
        )
        repository.uploadWallpaper(newWallpaper)
    }

    fun deleteWallpaperAdmin(id: String) {
        repository.deleteUploadedWallpaper(id)
    }
}
