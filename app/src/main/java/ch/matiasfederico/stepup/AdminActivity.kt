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
import ch.matiasfederico.stepup.ui.components.AdminScreen
import ch.matiasfederico.stepup.ui.components.Footer
import ch.matiasfederico.stepup.ui.theme.StepupTheme
import ch.matiasfederico.stepup.viewmodels.StepCounterViewModel

class AdminActivity : ComponentActivity() {
    private val stepCounterViewModel: StepCounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable immersive mode

        setContent {
            StepupTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Header(this) // Display the app header
                    AdminScreen(this, stepCounterViewModel)
                    Footer(this, clearPreviousActivity = { this.finish() }) // Footer navigation
                }
            }
        }
    }
}

// Button to simulate a day reset
/**/