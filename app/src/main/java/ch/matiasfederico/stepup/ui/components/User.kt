package ch.matiasfederico.stepup.ui.components

import android.content.Context
import android.content.Intent
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import ch.matiasfederico.stepup.CalorieActivity
import ch.matiasfederico.stepup.ui.viewmodels.UserViewModel
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun UserInputForm(context: Context, viewModel: UserViewModel) {
    val username by viewModel.username.observeAsState("")
    val dailyStepGoal by viewModel.dailyStepGoal.observeAsState(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Username",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = TextAlign.Left
        )
        TextField(
            value = username,
            onValueChange = {
                viewModel.saveUsername(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            placeholder = { Text("Enter your username") }
        )

        Text(
            text = "Daily Step Goal",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = TextAlign.Left
        )
        TextField(
            value = if (dailyStepGoal > 0) dailyStepGoal.toString() else "",
            onValueChange = {
                val newGoal = it.takeIf { it.isDigitsOnly() && it.isNotEmpty() }?.toIntOrNull() ?: 0
                viewModel.saveDailyStepGoal(newGoal)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            placeholder = { Text("Enter your daily step goal") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        TextButton(
            onClick = {
                val intent = Intent(context, CalorieActivity::class.java)
                context.startActivity(intent)
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

        Button(
            onClick = {
                viewModel.savePreferences()
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
