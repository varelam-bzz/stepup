package ch.matiasfederico.stepup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ch.matiasfederico.stepup.ui.theme.CalorieCalculator
import ch.matiasfederico.stepup.ui.theme.Footer
import ch.matiasfederico.stepup.ui.theme.Header
import ch.matiasfederico.stepup.ui.theme.StepupTheme

class CalorieActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StepupTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Header()
                    CalorieCalculator()
                    Footer(this)
                }
            }
        }
    }
}
