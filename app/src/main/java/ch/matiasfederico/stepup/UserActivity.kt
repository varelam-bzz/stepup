package ch.matiasfederico.stepup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ch.matiasfederico.stepup.ui.theme.StepupTheme
import ch.matiasfederico.stepup.ui.components.Header
import ch.matiasfederico.stepup.ui.components.UserInputForm
import ch.matiasfederico.stepup.ui.components.Footer
import ch.matiasfederico.stepup.viewmodels.UserViewModel

class UserActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StepupTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Header()
                    UserInputForm(this, userViewModel)
                    Footer(this)
                }
            }
        }
    }
}
