package ch.matiasfederico.stepup.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import ch.matiasfederico.stepup.viewmodels.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun CalorieCalculator(context: Context, userViewModel: UserViewModel) {
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var calories by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Calorie Goal",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = TextAlign.Left
        )

        // Input field for the user to enter a calorie goal
        TextField(
            value = if (calories > 0) calories.toString() else "",
            onValueChange = {
                calories = it.takeIf { it.isDigitsOnly() && it.isNotEmpty() }?.toIntOrNull() ?: 0
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            placeholder = { Text("Enter your calorie goal") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        // Display required steps based on calorie input
        Text(
            text = "Required Steps: ${calories * 20}", modifier = Modifier.padding(top = 16.dp)
        )

        // Show a toast message if needed
        if (showToast) {
            ShowToast(context, toastMessage, isSuccess)
            LaunchedEffect(Unit) {
                delay(Toast.LENGTH_LONG.toLong())
                showToast = false
            }
        }

        // Button to set and save the step goal
        Button(
            onClick = {
                showToast = true
                userViewModel.saveDailyStepGoal(calories * 20)
                if (userViewModel.savePreferences()) {
                    toastMessage = "Successfully saved daily step goal!"
                    isSuccess = true
                } else {
                    toastMessage = "Failed to save daily step goal."
                    isSuccess = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF206584))
        ) {
            Text("Set the goal", style = MaterialTheme.typography.bodyLarge, color = Color.White)
        }
    }
}
