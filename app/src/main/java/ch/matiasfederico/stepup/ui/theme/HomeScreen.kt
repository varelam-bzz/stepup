package ch.matiasfederico.stepup.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    username: String, permission: PermissionState, steps: Int, caloriesBurned: Double, dayGoal: Int
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello, $username!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Daily Summary",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            permission.status.isGranted -> {
                CircularProgressWithLabel(
                    steps = steps, dailyGoal = dayGoal
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Remaining", fontSize = 20.sp)
                        Text(text = "${dayGoal - steps}", fontSize = 20.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Calories", fontSize = 20.sp)
                        Text(text = "%.2f".format(caloriesBurned), fontSize = 20.sp)
                    }
                }
            }

            permission.status.shouldShowRationale -> {
                Button(onClick = {
                    permission.launchPermissionRequest()
                }) {
                    Text(text = "Grant permission")
                }
            }

            else -> {
                Button(onClick = {
                    permission.launchPermissionRequest()
                }) {
                    Text(text = "Request permission")
                }
            }
        }
    }
}

@Composable
fun CircularProgressWithLabel(steps: Int, dailyGoal: Int) {
    val progress = steps.toFloat() / dailyGoal.toFloat()

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .size(250.dp)
            .padding(16.dp)
    ) {
        CircularProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.size(230.dp),
            color = Color(0xFF304474),
            strokeWidth = 15.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$steps",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "/ $dailyGoal",
                fontSize = 24.sp,
                color = Color.Gray
            )
        }
    }
}
