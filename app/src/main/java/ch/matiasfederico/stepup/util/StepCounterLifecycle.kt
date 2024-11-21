package ch.matiasfederico.stepup.util

import android.util.Log
import ch.matiasfederico.stepup.viewmodels.StepCounterViewModel

// Helper class to manage step counter lifecycle methods
class StepCounterLifecycle {

    fun onResume(
        localClassName: String, stepCounterViewModel: StepCounterViewModel
    ) {
        // Start tracking steps and check for resets when the activity resumes
        stepCounterViewModel.startTrackingSteps()
        stepCounterViewModel.checkAndResetIfNeeded()

        // Log state for debugging
        Log.d(localClassName, "App resumed.")
        Log.d(localClassName, "Daily Steps: ${stepCounterViewModel.dailyStepCount.value}")
        Log.d(
            localClassName,
            "Monthly Steps: ${stepCounterViewModel.monthlySteps.value.joinToString()}"
        )
    }

    fun onPause(localClassName: String, stepCounterViewModel: StepCounterViewModel) {
        // Stop tracking steps and save data when the activity pauses
        stepCounterViewModel.stopTrackingSteps()
        stepCounterViewModel.saveData()

        // Log state for debugging
        Log.d(localClassName, "App paused. Tracking stopped.")
    }
}
