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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.matiasfederico.stepup.ui.viewmodels.UserViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    permission: PermissionState,
    steps: Int,
    caloriesBurned: Double,
    userViewModel: UserViewModel
) {
    val username by userViewModel.username.observeAsState("")
    val dailyStepGoal by userViewModel.dailyStepGoal.observeAsState(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (username.isNotEmpty()) "Hello, $username!" else "Hello, User!",
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
                    steps = steps,
                    dailyGoal = dailyStepGoal
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Remaining", fontSize = 20.sp)
                        Text(text = "${dailyStepGoal - steps}", fontSize = 20.sp)
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
                }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF206584))
                ) {
                    Text(text = "Grant permission")
                }
            }

            else -> {
                Button(onClick = {
                    permission.launchPermissionRequest()
                }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF206584))) {
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
            color = Color(0xFF206584),
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
