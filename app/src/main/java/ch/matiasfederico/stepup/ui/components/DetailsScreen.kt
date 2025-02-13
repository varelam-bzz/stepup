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

fun calcBurnedCalories(steps: Int): Float {
    // Helper function to calculate calories burned from steps
    return steps * 0.04f
}

@Composable
fun DetailsScreen(
    userViewModel: UserViewModel, stepCounterViewModel: StepCounterViewModel
) {
    val dailyStepGoal by userViewModel.dailyStepGoal.observeAsState(0) // Observes daily step goal
    val dailySteps by stepCounterViewModel.dailyStepCount.collectAsState()
    val dailyCaloriesBurned = calcBurnedCalories(dailySteps)

    val weeklySteps by stepCounterViewModel.weeklyStepCount.collectAsState(0)
    val weeklyCaloriesBurned = calcBurnedCalories(weeklySteps)

    val monthlySteps by stepCounterViewModel.monthlyStepCount.collectAsState(0)
    val monthlyCaloriesBurned = calcBurnedCalories(monthlySteps)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Daily Summary Card
        SummaryCard(
            title = "Daily Summary",
            steps = dailySteps,
            caloriesBurned = dailyCaloriesBurned,
            progress = dailySteps.toFloat() / dailyStepGoal,
            goal = dailyStepGoal
        )

        // Weekly Summary Card
        SummaryCard(
            title = "Weekly Summary",
            steps = weeklySteps,
            caloriesBurned = weeklyCaloriesBurned,
            progress = weeklySteps.toFloat() / (dailyStepGoal * 7),
            goal = dailyStepGoal * 7
        )

        // Monthly Summary Card
        SummaryCard(
            title = "Monthly Summary",
            steps = monthlySteps,
            caloriesBurned = monthlyCaloriesBurned,
            progress = monthlySteps.toFloat() / (dailyStepGoal * 30),
            goal = dailyStepGoal * 30
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0EEFA))
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
                    text = "$steps / $goal", fontSize = 12.sp
                )
            }

            // Progress Bar
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .padding(vertical = 4.dp),
                color = if (progress < 1f) Color(0xFF206584) else Color(0xFF4BB543),
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
                    text = "%.2f".format(caloriesBurned), fontSize = 12.sp
                )
            }
        }
    }
}
