package ch.matiasfederico.stepup

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
import ch.matiasfederico.stepup.ui.components.Header
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
        stepCounterLifecycle.onResume(localClassName, stepCounterViewModel)
    }

    override fun onPause() {
        super.onPause()
        stepCounterLifecycle.onPause(localClassName, stepCounterViewModel)
    }
}
