package ch.matiasfederico.stepup.ui.theme

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Footer(onDetailsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            // Handle Home button click
        }) {
            Icon(
                Icons.Filled.Home,
                contentDescription = "Home",
                modifier = Modifier.size(48.dp),
            )
        }

        Button(onClick = onDetailsClick) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Info, contentDescription = "Details")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Details")
            }
        }

        IconButton(onClick = {
            // Handle User Profile button click
        }) {
            Icon(
                Icons.Filled.Person,
                contentDescription = "User Profile",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
