package ch.matiasfederico.stepup.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
    val image = painterResource(R.drawable.logo)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "StepUp",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .offset(8.dp)
                .padding(end = 8.dp),
            contentScale = ContentScale.Fit
        )
    }
}
