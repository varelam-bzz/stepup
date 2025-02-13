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
import ch.matiasfederico.stepup.ui.components.CalorieCalculator
import ch.matiasfederico.stepup.ui.components.Footer
import ch.matiasfederico.stepup.ui.theme.StepupTheme
import ch.matiasfederico.stepup.viewmodels.UserViewModel

class CalorieActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable immersive mode

        setContent {
            StepupTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Header(this) // Display the app header
                    CalorieCalculator(this, userViewModel) // Display calorie calculator UI
                    Footer(this, clearPreviousActivity = { this.finish() }) // Footer navigation
                }
            }
        }
    }
}
