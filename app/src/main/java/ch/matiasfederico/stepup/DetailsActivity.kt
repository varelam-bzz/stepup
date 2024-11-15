package ch.matiasfederico.stepup

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ch.matiasfederico.stepup.ui.theme.DetailsScreen
import ch.matiasfederico.stepup.ui.theme.Footer
import ch.matiasfederico.stepup.ui.theme.Header
import ch.matiasfederico.stepup.ui.theme.StepupTheme

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences =
            getSharedPreferences("StepUpPrefs", Context.MODE_PRIVATE)

        val steps = sharedPreferences.getInt("steps", 0)
        val caloriesBurned = sharedPreferences.getFloat("caloriesBurned", 0f)
        val dailyGoal = sharedPreferences.getInt("dailyGoal", 0)

        setContent {
            StepupTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Header()
                    DetailsScreen(
                        steps = steps,
                        caloriesBurned = caloriesBurned,
                        dailyGoal = dailyGoal
                    )
                    Footer(this)
                }
            }
        }
    }
}
