package ch.matiasfederico.stepup.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

// Helper function to show a toast message
fun showToast(context: Context, message: String, isSuccess: Boolean) {
    Toast.makeText(
        context,
        if (isSuccess) "✅ $message" else "❌ $message", // Adds a check or cross emoji based on success
        Toast.LENGTH_LONG
    ).show()
}

// Composable wrapper for the toast
@Composable
fun ShowToast(
    context: Context, message: String?, isSuccess: Boolean
) {
    message?.let {
        LaunchedEffect(message) {
            showToast(context, it, isSuccess)
        }
    }
}
