package ch.matiasfederico.stepup

import ch.matiasfederico.stepup.ui.components.Header
import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ch.matiasfederico.stepup.ui.components.Footer
import ch.matiasfederico.stepup.ui.components.HomeScreen
import ch.matiasfederico.stepup.ui.theme.StepupTheme
import ch.matiasfederico.stepup.util.StepCounterLifecycle
import ch.matiasfederico.stepup.viewmodels.StepCounterViewModel
import ch.matiasfederico.stepup.viewmodels.UserViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {
    private val stepCounterViewModel: StepCounterViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val stepCounterLifecycle = StepCounterLifecycle()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable immersive UI features for modern Android devices

        setContent {
            StepupTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val permission =
                        rememberPermissionState(permission = Manifest.permission.ACTIVITY_RECOGNITION)

                    Header(this) // Displays the app's header with title and logo
                    HomeScreen(
                        permission = permission,
                        userViewModel = userViewModel,
                        stepCounterViewModel = stepCounterViewModel
                    ) // Main screen UI components
                    Footer(
                        this,
                        clearPreviousActivity = { this.finish() }) // Footer with navigation buttons
                }
            }
        }
    }

    // Resume lifecycle: Starts step tracking and resets if needed
    override fun onResume() {
        super.onResume()
        stepCounterLifecycle.onResume(localClassName, stepCounterViewModel)
    }

    // Pause lifecycle: Stops step tracking and saves data
    override fun onPause() {
        super.onPause()
        stepCounterLifecycle.onPause(localClassName, stepCounterViewModel)
    }
}
