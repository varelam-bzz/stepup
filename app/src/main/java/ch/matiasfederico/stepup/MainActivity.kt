package ch.matiasfederico.stepup

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ch.matiasfederico.stepup.ui.components.Footer
import ch.matiasfederico.stepup.ui.components.Header
import ch.matiasfederico.stepup.ui.components.HomeScreen
import ch.matiasfederico.stepup.ui.theme.StepupTheme
import ch.matiasfederico.stepup.viewmodels.StepCounterViewModel
import ch.matiasfederico.stepup.viewmodels.UserViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {
    private val stepCounterViewModel: StepCounterViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StepupTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val permission =
                        rememberPermissionState(permission = Manifest.permission.ACTIVITY_RECOGNITION)

                    Header()
                    HomeScreen(
                        permission = permission,
                        userViewModel = userViewModel,
                        stepCounterViewModel = stepCounterViewModel
                    )
                    Footer(this, clearPreviousActivity = { this.finish() })
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        stepCounterViewModel.startTrackingSteps()
        stepCounterViewModel.checkAndResetIfNeeded()

        // Log the current state
        Log.d("MainActivity", "App resumed. Current Daily Steps: ${stepCounterViewModel.dailyStepCount.value}")
        Log.d(
            "MainActivity",
            "Monthly Steps: ${stepCounterViewModel.monthlySteps.value.joinToString()}"
        )
    }

    override fun onPause() {
        super.onPause()
        stepCounterViewModel.stopTrackingSteps()

        // Log on pause
        Log.d("MainActivity", "App paused. Tracking stopped.")
    }

}
