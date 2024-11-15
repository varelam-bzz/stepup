package ch.matiasfederico.stepup.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserInputForm() {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var dailyStepGoal by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Username",
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            textAlign = TextAlign.Left
        )
        TextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text("Username") },
            placeholder = { Text("Enter your username") }
        )

        Text(
            text = "Daily Step Goal",
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            textAlign = TextAlign.Left
        )
        TextField(
            value = dailyStepGoal,
            onValueChange = { dailyStepGoal = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text("Daily Step Goal") },
            placeholder = { Text("Enter your daily step goal") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        // Calorie Goal Link
        TextButton(
            onClick = {
                // Action for setting calorie goal
            },
            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF206584)),
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text(
                text = "Want to set a calorie goal?",
                textDecoration = TextDecoration.Underline,
                fontSize = 14.sp
            )
        }

        // Save Button
        Button(
            onClick = {
                // Save user preferences or perform desired action
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF206584))
        ) {
            Text("Save", style = MaterialTheme.typography.bodyLarge, color = Color.White)
        }
    }
}
