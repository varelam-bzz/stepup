package ch.matiasfederico.stepup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ch.matiasfederico.stepup.ui.theme.DetailsScreen
import ch.matiasfederico.stepup.ui.theme.StepupTheme

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val steps = intent.getIntExtra("steps", 0)
        val caloriesBurned = intent.getDoubleExtra("caloriesBurned", 0.0)
        val dailyGoal = intent.getIntExtra("dailyGoal", 6000)

        setContent {
            StepupTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DetailsScreen(
                        steps = steps,
                        caloriesBurned = caloriesBurned,
                        dailyGoal = dailyGoal
                    )
                }
            }
        }
    }
}
