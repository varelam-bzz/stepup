package ch.matiasfederico.stepup.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matiasfederico.myapplication.R

@Composable
fun Header() {
    val image = painterResource(R.drawable.logo) // Load the app logo resource

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 54.dp) // Add padding to position the header
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Space items evenly
            verticalAlignment = Alignment.CenterVertically, // Align items vertically
        ) {
            // App title
            Text(
                text = "StepUp", fontSize = 32.sp, fontWeight = FontWeight.Bold
            )

            // Logo
            Image(
                painter = image,
                contentDescription = null, // No description needed for decorative content
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Fit // Fit the logo inside the available space
            )
        }
    }
}
