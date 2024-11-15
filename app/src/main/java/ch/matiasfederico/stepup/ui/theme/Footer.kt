package ch.matiasfederico.stepup.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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

@Composable
fun Footer(
    currentScreen: String,
    onScreenChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = {
            onScreenChange("home")
        }) {
            Icon(
                Icons.Filled.Home,
                contentDescription = "Home",
                modifier = Modifier.size(48.dp),
            )
        }

        Box(
            modifier = Modifier.padding(start = 32.dp, end = 32.dp)
        ) {
            Button(onClick = {
                onScreenChange("details")
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
            onScreenChange("user")
        }) {
            Icon(
                Icons.Filled.Person,
                contentDescription = "User Profile",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
