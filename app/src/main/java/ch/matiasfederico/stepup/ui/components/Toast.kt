package ch.matiasfederico.stepup.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

fun showToast(context: Context, message: String, isSuccess: Boolean) {
    Toast.makeText(
        context,
        if (isSuccess) "✅ $message" else "❌ $message",
        Toast.LENGTH_LONG
    ).show()
}

@Composable
fun ShowToast(
    context: Context,
    message: String?,
    isSuccess: Boolean
) {
    message?.let {
        LaunchedEffect(message) {
            showToast(context, it, isSuccess)
        }
    }
}