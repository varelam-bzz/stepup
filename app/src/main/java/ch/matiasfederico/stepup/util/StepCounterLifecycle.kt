package ch.matiasfederico.stepup.util

import android.util.Log
import ch.matiasfederico.stepup.viewmodels.StepCounterViewModel

class StepCounterLifecycle {
    fun onResume(
        localClassName: String, stepCounterViewModel: StepCounterViewModel
    ) {
        stepCounterViewModel.startTrackingSteps()
        stepCounterViewModel.checkAndResetIfNeeded()

        Log.d(localClassName, "App resumed.")
        Log.d(localClassName, "Daily Steps: ${stepCounterViewModel.dailyStepCount.value}")
        Log.d(
            localClassName,
            "Monthly Steps: ${stepCounterViewModel.monthlySteps.value.joinToString()}"
        )
    }

    fun onPause(localClassName: String, stepCounterViewModel: StepCounterViewModel) {
        stepCounterViewModel.stopTrackingSteps()
        stepCounterViewModel.saveData()

        Log.d(localClassName, "App paused. Tracking stopped.")
    }
}
