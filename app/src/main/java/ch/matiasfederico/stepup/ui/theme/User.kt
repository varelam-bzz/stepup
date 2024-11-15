package ch.matiasfederico.stepup.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun User() {
    var currentScreen by remember { mutableStateOf("user") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header() // Display the header at the top

        Box(modifier = Modifier.weight(1f)) { // Main content area
            when (currentScreen) {
                "home" -> {
                    // Placeholder for Home Screen
                    Text("Home Screen")
                }

                "details" -> {
                    // Placeholder for Details Screen
                    Text("Details Screen")
                }

                "user" -> {
                    // User input form
                    UserInputForm()
                }
            }
        }

        Footer(currentScreen = currentScreen,
            onScreenChange = { screen -> currentScreen = screen }) // Footer navigation
    }
}

@Composable
fun UserInputForm() {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var dailyStepGoal by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Username")

        TextField(value = username,
            onValueChange = { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text("Enter username") })

        Text("Daily Step Goal")

        TextField(
            value = dailyStepGoal,
            onValueChange = { dailyStepGoal = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text("Enter daily step goal") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {
                // Save preferences or perform any action
            }, modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Save")
        }
    }
}