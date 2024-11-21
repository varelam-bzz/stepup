package ch.matiasfederico.stepup.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.matiasfederico.stepup.viewmodels.StepCounterViewModel
import ch.matiasfederico.stepup.viewmodels.UserViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import kotlin.math.max

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    permission: PermissionState,
    userViewModel: UserViewModel,
    stepCounterViewModel: StepCounterViewModel
) {
    val username by userViewModel.username.observeAsState("User") // Observe username changes
    val dailyStepGoal by userViewModel.dailyStepGoal.observeAsState(0) // Observe daily step goal
    val steps by stepCounterViewModel.dailyStepCount.collectAsState() // Collect daily steps
    val caloriesBurned = steps * 0.04f // Calculate calories burned based on steps

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Greeting message
        Text(
            text = "Hello, $username!", fontSize = 32.sp, fontWeight = FontWeight.Bold
        )
        // Section title
        Text(
            text = "Daily Summary",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            permission.status.isGranted -> {
                // Show progress circular UI if permission is granted
                CircularProgressWithLabel(steps = steps, dailyGoal = dailyStepGoal)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Remaining steps
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Remaining", fontSize = 20.sp)
                        Text(text = "${max(dailyStepGoal - steps, 0)}", fontSize = 20.sp)
                    }
                    // Calories burned
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Calories", fontSize = 20.sp)
                        Text(text = "%.2f".format(caloriesBurned), fontSize = 20.sp)
                    }
                }
            }

            permission.status.shouldShowRationale -> {
                // Show rationale for requesting permissions
                Button(
                    onClick = { permission.launchPermissionRequest() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF206584))
                ) {
                    Text(text = "Grant permission")
                }
            }

            else -> {
                // Prompt to request permissions
                Button(
                    onClick = { permission.launchPermissionRequest() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF206584))
                ) {
                    Text(text = "Request permission")
                }
            }
        }

        // Button to simulate a day reset
        Button(onClick = { stepCounterViewModel.forceDailyResetForTesting() }) {
            Text("Simulate Day Reset")
        }
    }
}

@Composable
fun CircularProgressWithLabel(steps: Int, dailyGoal: Int) {
    val progress = steps.toFloat() / dailyGoal.toFloat() // Calculate progress as a fraction

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .size(250.dp)
            .padding(16.dp)
    ) {
        // Circular progress bar
        CircularProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.size(230.dp),
            color = Color(0xFF206584),
            strokeWidth = 15.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        // Steps label
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$steps", fontSize = 36.sp, fontWeight = FontWeight.Bold
            )
            Text(
                text = "/ $dailyGoal", fontSize = 24.sp, color = Color.Gray
            )
        }
    }
}
