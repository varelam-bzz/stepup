package ch.matiasfederico.stepup.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun UserInputForm() {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var dailyStepGoal by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start // Align items to the start for left alignment
    ) {
        Text("Username", modifier = Modifier.padding(start = 4.dp))

        TextField(value = username,
            onValueChange = { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text("Enter username") })

        Text("Daily Step Goal", modifier = Modifier.padding(start = 4.dp))

        TextField(
            value = dailyStepGoal,
            onValueChange = { dailyStepGoal = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text("Enter daily step goal") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        // Center the TextButton
        TextButton(
            onClick = {
                // Action for the "Calories" link - for now, we can just print a message or navigate to another screen
            },
            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF206584)), // Set text color
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp) // Center the button
        ) {
            Text(
                text = "Want to set a calorie goal?", textDecoration = TextDecoration.Underline
            )
        }

        // Center the Save Button
        Button(
            onClick = {
                // Save preferences or perform any action
            }, modifier = Modifier
                .align(Alignment.CenterHorizontally) // Center the button
                .padding(top = 16.dp)
        ) {
            Text("Save")
        }
    }
}
