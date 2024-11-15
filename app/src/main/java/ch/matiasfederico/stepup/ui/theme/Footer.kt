package ch.matiasfederico.stepup.ui.theme

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.matiasfederico.stepup.DetailsActivity
import ch.matiasfederico.stepup.MainActivity
import ch.matiasfederico.stepup.UserActivity

@Composable
fun Footer(
    context: Context,
    steps: Int,
    caloriesBurned: Double,
    dailyGoal: Int
) {
    Row(
        modifier = Modifier
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = {
            // Navigate back to the MainActivity (Home screen)
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }) {
            Icon(
                Icons.Filled.Home,
                contentDescription = "Home",
                modifier = Modifier.size(48.dp)
            )
        }

        IconButton(onClick = {
            // Launch DetailsActivity
            val intent = Intent(context, DetailsActivity::class.java).apply {
                putExtra("steps", steps)
                putExtra("caloriesBurned", caloriesBurned)
                putExtra("dailyGoal", dailyGoal)
            }
            context.startActivity(intent)
        }) {
            Icon(
                Icons.Filled.Info,
                contentDescription = "Details",
                modifier = Modifier.size(48.dp)
            )
        }

        IconButton(onClick = {
            // Launch UserActivity
            val intent = Intent(context, UserActivity::class.java)
            context.startActivity(intent)
        }) {
            Icon(
                Icons.Filled.Person,
                contentDescription = "User",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
