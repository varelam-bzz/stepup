package ch.matiasfederico.stepup

import ch.matiasfederico.stepup.ui.components.Header
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ch.matiasfederico.stepup.ui.components.DetailsScreen
import ch.matiasfederico.stepup.ui.components.Footer
import ch.matiasfederico.stepup.ui.theme.StepupTheme
import ch.matiasfederico.stepup.util.StepCounterLifecycle
import ch.matiasfederico.stepup.viewmodels.StepCounterViewModel
import ch.matiasfederico.stepup.viewmodels.UserViewModel

class DetailsActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private val stepCounterViewModel: StepCounterViewModel by viewModels()
    private val stepCounterLifecycle = StepCounterLifecycle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable edge-to-edge immersive display

        setContent {
            StepupTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Header(this) // Display the app header
                    DetailsScreen(
                        userViewModel = userViewModel, stepCounterViewModel = stepCounterViewModel
                    ) // Main content for detailed step tracking
                    Footer(this, clearPreviousActivity = { this.finish() }) // Footer navigation
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        stepCounterLifecycle.onResume(localClassName, stepCounterViewModel) // Resume step tracking
    }

    override fun onPause() {
        super.onPause()
        stepCounterLifecycle.onPause(localClassName, stepCounterViewModel) // Pause step tracking
    }
}
