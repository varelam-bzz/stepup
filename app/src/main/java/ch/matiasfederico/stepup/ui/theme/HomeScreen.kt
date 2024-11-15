package ch.matiasfederico.stepup.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    username: String,
    permission: PermissionState,
    steps: Int,
    caloriesBurned: Double,
    dayGoal: Int,
    input: String
) {
    var newGoalInput = input
    var dailyGoal = dayGoal

    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello, $username! Daily Summary",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            permission.status.isGranted -> {
                Text(
                    text = "Steps: $steps / $dailyGoal",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Calories Burned: %.2f".format(caloriesBurned),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Daily Goal: $dailyGoal steps",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                TextField(
                    value = newGoalInput,
                    onValueChange = { newGoalInput = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("New Daily Goal") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                Button(onClick = {
                    newGoalInput.toIntOrNull()?.let {
                        dailyGoal = it
                    }
                }) {
                    Text(text = "Set New Daily Goal")
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