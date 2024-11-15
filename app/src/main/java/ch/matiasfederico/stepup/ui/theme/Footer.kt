package ch.matiasfederico.stepup.ui.theme

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ch.matiasfederico.stepup.DetailsActivity
import ch.matiasfederico.stepup.MainActivity
import ch.matiasfederico.stepup.UserActivity

@Composable
fun Footer(
    context: Context,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, bottom = 46.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(
                    Icons.Filled.Home, contentDescription = "Home", modifier = Modifier.size(48.dp)
                )
            }

            Box(
                modifier = Modifier.padding(start = 32.dp, end = 32.dp)
            ) {
                Button(onClick = {
                    val intent = Intent(context, DetailsActivity::class.java)
                    context.startActivity(intent)
                }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF206584))) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Filled.Info, contentDescription = "Details")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Details")
                    }
                }
            }

            IconButton(onClick = {
                val intent = Intent(context, UserActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(
                    Icons.Filled.Person, contentDescription = "User", modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}
