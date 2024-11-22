package ch.matiasfederico.stepup.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.matiasfederico.stepup.AdminActivity
import com.matiasfederico.myapplication.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Header(context: Context) {
    val image = painterResource(R.drawable.logo) // Load the app logo resource

    // State to track click count
    var clickCounter by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 54.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // App title
            Text(
                text = "StepUp", fontSize = 32.sp, fontWeight = FontWeight.Bold
            )

            // Logo with click detection
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        clickCounter++
                        if (clickCounter == 3) {
                            // Reset the counter and navigate to new activity
                            clickCounter = 0
                            val intent = Intent(context, AdminActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(intent)
                        }
                        // Reset counter after 1 second if not triple-clicked
                        scope.launch {
                            delay(1000)
                            clickCounter = 0
                        }
                    },
                contentScale = ContentScale.Fit
            )
        }
    }
}
