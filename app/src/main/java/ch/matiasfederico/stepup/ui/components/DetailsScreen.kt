package ch.matiasfederico.stepup.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailsScreen(steps: Int, caloriesBurned: Float, dailyGoal: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SummaryCard(
            title = "Daily Summary",
            steps = steps,
            caloriesBurned = caloriesBurned,
            progress = steps.toFloat() / dailyGoal,
            goal = dailyGoal
        )

        SummaryCard(
            title = "Weekly Summary",
            steps = steps * 7,
            caloriesBurned = caloriesBurned * 7,
            progress = steps.toFloat() / (dailyGoal * 7),
            goal = dailyGoal * 7
        )

        SummaryCard(
            title = "Monthly Summary",
            steps = steps * 30,
            caloriesBurned = caloriesBurned * 30,
            progress = steps.toFloat() / (dailyGoal * 30),
            goal = dailyGoal * 30
        )
    }
}

@Composable
fun SummaryCard(title: String, steps: Int, caloriesBurned: Float, progress: Float, goal: Int) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Steps Row
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (progress < 1) Icons.Filled.Close else Icons.Filled.Check,
                    contentDescription = "Steps",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Steps:", fontSize = 12.sp, modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$steps / $goal",
                    fontSize = 12.sp,
                )
            }

            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .padding(vertical = 4.dp),
                color = Color(
                    0xFF206584
                ),
            )

            // Calories Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Calories Burned:", fontSize = 12.sp, modifier = Modifier.weight(1f)
                )
                Text(
                    text = "%.2f".format(caloriesBurned),
                    fontSize = 12.sp,
                )
            }
        }
    }
}
