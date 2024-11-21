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

class StepCounterViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {

    private val sensorManager: SensorManager =
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var stepSensor: Sensor? = null

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("StepupPrefs", Context.MODE_PRIVATE)

    private var initialStepCount: Float? = null
    private var hasInitialStepCount: Boolean = false
    private var lastResetDay: Int = -1

    private val _dailySteps = MutableStateFlow(0)
    private val _monthlySteps = MutableStateFlow(IntArray(30) { 0 })
    val monthlySteps: StateFlow<IntArray> get() = _monthlySteps

    val dailyStepCount: StateFlow<Int> = _dailySteps
    val weeklyStepCount: Flow<Int> get() = _monthlySteps.map { it.take(7).sum() }
    val monthlyStepCount: Flow<Int> get() = _monthlySteps.map { it.sum() }

    init {
        loadSavedData()
        checkAndResetIfNeeded()
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    private fun loadSavedData() {
        _dailySteps.value = sharedPreferences.getInt("dailySteps", 0)
        lastResetDay = sharedPreferences.getInt("lastResetDay", -1)

        val savedMonthlySteps = sharedPreferences.getString("monthlySteps", null)
        if (savedMonthlySteps != null) {
            val stepsArray = savedMonthlySteps.split(",").mapNotNull { it.toIntOrNull() }.toIntArray()
            if (stepsArray.size == 30) {
                _monthlySteps.update { stepsArray }
            }
        }

        if (_dailySteps.value > 0) {
            initialStepCount = sharedPreferences.getFloat("initialStepCount", 0f)
            hasInitialStepCount = true
        }
    }

    private fun saveData() {
        with(sharedPreferences.edit()) {
            putInt("dailySteps", _dailySteps.value)
            putInt("lastResetDay", lastResetDay)
            putString("monthlySteps", _monthlySteps.value.joinToString(","))
            initialStepCount?.let { putFloat("initialStepCount", it) }
            apply()
        }
        Log.d("StepCounterViewModel", "Data saved. Monthly Steps: ${_monthlySteps.value.joinToString()}")
    }

    fun startTrackingSteps() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopTrackingSteps() {
        sensorManager.unregisterListener(this)
        saveData()
    }

    fun checkAndResetIfNeeded() {
        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        Log.d("StepCounterViewModel", "Checking reset: LastResetDay = $lastResetDay, Today = $today")

        if (lastResetDay != today) {
            shiftMonthlySteps()
            resetDailySteps()
            lastResetDay = today
            saveData()
            Log.d("StepCounterViewModel", "Reset complete. Updated Monthly Steps: ${_monthlySteps.value.joinToString()}")
        }
    }

    private fun resetDailySteps() {
        Log.d("StepCounterViewModel", "Resetting daily steps. Previous daily steps: ${_dailySteps.value}")
        initialStepCount = null
        hasInitialStepCount = false
        _dailySteps.update { 0 }

        // Reset today's value in the monthly steps array
        _monthlySteps.update { stepsArray ->
            stepsArray.apply {
                this[0] = 0 // Reset today's step count
            }
        }
        Log.d("StepCounterViewModel", "Today's value in monthly steps array reset to 0.")
    }

    private fun shiftMonthlySteps() {
        val updatedSteps = _monthlySteps.value.copyOf()
        val todaySteps = _dailySteps.value
        Log.d("StepCounterViewModel", "Shifting monthly steps. Today's steps: $todaySteps")

        for (i in updatedSteps.size - 1 downTo 1) {
            updatedSteps[i] = updatedSteps[i - 1]
        }

        updatedSteps[0] = todaySteps
        _monthlySteps.update { updatedSteps }

        Log.d("StepCounterViewModel", "Updated monthly steps array: ${updatedSteps.joinToString()}")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let { sensorEvent ->
            if (sensorEvent.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                val currentCumulativeSteps = sensorEvent.values[0]

                if (!hasInitialStepCount) {
                    initialStepCount = currentCumulativeSteps
                    hasInitialStepCount = true
                    Log.d("StepCounterViewModel", "Initial Step Count set to $initialStepCount")
                }

                if (currentCumulativeSteps < initialStepCount!!) {
                    initialStepCount = currentCumulativeSteps
                    Log.d("StepCounterViewModel", "Sensor reset detected. Initial Step Count updated.")
                }

                val stepsSinceLastReset = (currentCumulativeSteps - initialStepCount!!).toInt()
                _dailySteps.update { stepsSinceLastReset }

                _monthlySteps.update { stepsArray ->
                    stepsArray.also { it[0] = stepsSinceLastReset }
                }

                saveData()
            }
        }
    }

    fun forceDailyResetForTesting() {
        lastResetDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 1
        checkAndResetIfNeeded()
        Log.d("StepCounterViewModel", "Forced reset triggered. Monthly Steps: ${_monthlySteps.value.joinToString()}")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No implementation needed
    }
}
