package empire.digiprem.mycolowepapp.core.design_system

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import kotlin.math.min

@Composable
fun currentDeviceConfigure(): DeviceConfiguration {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    return DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
}

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    fun isMobileDevice(): Boolean = this == MOBILE_PORTRAIT
    fun isTabletDevice(): Boolean = this == TABLET_PORTRAIT
    fun isCompact(): Boolean = isMobileDevice() || isTabletDevice()

    companion object {
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration {
            return with(windowSizeClass) {
                val smallestSideDp = min(minWidthDp, minHeightDp)
                val isTablet = smallestSideDp >= WIDTH_DP_MEDIUM_LOWER_BOUND
                val isLandscape = minWidthDp > minHeightDp
                when {
                    !isTablet && !isLandscape -> MOBILE_PORTRAIT
                    !isTablet && isLandscape -> MOBILE_LANDSCAPE
                    isTablet && !isLandscape -> TABLET_PORTRAIT
                    isTablet && isLandscape -> TABLET_LANDSCAPE
                    else -> DESKTOP
                }
            }
        }
    }
}
