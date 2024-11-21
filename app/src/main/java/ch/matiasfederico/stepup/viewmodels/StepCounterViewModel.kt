package ch.matiasfederico.stepup.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.Calendar

class StepCounterViewModel(application: Application) : AndroidViewModel(application),
    SensorEventListener {

    private val sensorManager: SensorManager =
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var stepSensor: Sensor? = null

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("StepupPrefs", Context.MODE_PRIVATE)

    private var initialStepCount: Float? =
        null // Tracks the cumulative step count at app start/reset
    private var hasInitialStepCount: Boolean =
        false // Ensures the initial step count is only set once
    private var lastResetDay: Int = -1 // Tracks the day steps were last reset

    // Flow-based reactive state management
    private val _dailySteps = MutableStateFlow(0) // Daily steps
    private val _monthlySteps = MutableStateFlow(IntArray(30) { 0 }) // Monthly steps as an array
    val monthlySteps: StateFlow<IntArray> get() = _monthlySteps

    val dailyStepCount: StateFlow<Int> = _dailySteps
    val weeklyStepCount: Flow<Int>
        get() = _monthlySteps.map {
            it.take(7).sum()
        } // Sum of last 7 days
    val monthlyStepCount: Flow<Int> get() = _monthlySteps.map { it.sum() } // Sum of last 30 days

    init {
        loadSavedData() // Restore saved step data from SharedPreferences
        checkAndResetIfNeeded() // Reset steps if it's a new day
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    private fun loadSavedData() {
        // Load persistent data for daily steps, monthly steps, and the last reset day
        _dailySteps.value = sharedPreferences.getInt("dailySteps", 0)
        lastResetDay = sharedPreferences.getInt("lastResetDay", -1)

        val savedMonthlySteps = sharedPreferences.getString("monthlySteps", null) ?: return
        val stepsArray = savedMonthlySteps.split(",").mapNotNull { it.toIntOrNull() }.toIntArray()

        if (stepsArray.size == 30) {
            _monthlySteps.update { stepsArray }
        }

        // Restore the initial step count if daily steps are non-zero
        if (_dailySteps.value > 0) {
            initialStepCount = sharedPreferences.getFloat("initialStepCount", 0f).takeIf { it > 0 }
            hasInitialStepCount = initialStepCount != null
        }
    }

    fun saveData() {
        // Persist the current step data to SharedPreferences
        with(sharedPreferences.edit()) {
            putInt("dailySteps", _dailySteps.value)
            putInt("lastResetDay", lastResetDay)
            putString("monthlySteps", _monthlySteps.value.joinToString(","))
            initialStepCount?.let { putFloat("initialStepCount", it) }
            apply()
        }
        Log.d(
            "StepCounterViewModel",
            "Data saved. Monthly Steps: ${_monthlySteps.value.joinToString()}"
        )
    }

    fun startTrackingSteps() {
        // Start listening to the step counter sensor
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopTrackingSteps() {
        // Stop listening to the step counter sensor
        sensorManager.unregisterListener(this)
    }

    fun checkAndResetIfNeeded() {
        // Checks if the current day has changed since the last reset and resets steps accordingly
        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val daysSinceLastReset = (today - lastResetDay + 365) % 365 // Handles year rollovers
        if (daysSinceLastReset > 0) {
            repeat(daysSinceLastReset) { shiftMonthlySteps() } // Shift steps for each day passed
            resetDailySteps()
            lastResetDay = today
            saveData()
        }
    }

    private fun resetDailySteps() {
        // Resets daily step count and updates monthly step array
        initialStepCount = null
        hasInitialStepCount = false
        _dailySteps.update { 0 }
        _monthlySteps.update { stepsArray ->
            stepsArray.copyOf().apply { this[0] = 0 } // Reset today's steps
        }
    }

    private fun shiftMonthlySteps() {
        // Shift monthly steps array to accommodate a new day
        val updatedSteps = _monthlySteps.value.copyOf()
        for (i in updatedSteps.size - 1 downTo 1) {
            updatedSteps[i] = updatedSteps[i - 1]
        }
        updatedSteps[0] = _dailySteps.value
        _monthlySteps.update { updatedSteps }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Handles step updates from the sensor
        event?.let { sensorEvent ->
            if (sensorEvent.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                val currentCumulativeSteps = sensorEvent.values[0]

                if (!hasInitialStepCount || currentCumulativeSteps < initialStepCount!!) {
                    initialStepCount = currentCumulativeSteps
                    hasInitialStepCount = true
                }

                val stepsSinceLastReset = (currentCumulativeSteps - initialStepCount!!).toInt()
                _dailySteps.update { stepsSinceLastReset }
                _monthlySteps.update { stepsArray ->
                    stepsArray.apply { this[0] = stepsSinceLastReset }
                }

                saveData()
            }
        }
    }

    fun forceDailyResetForTesting() {
        // Forces a daily reset for testing purposes
        lastResetDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 1
        checkAndResetIfNeeded()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No specific accuracy handling needed for this app
    }
}
