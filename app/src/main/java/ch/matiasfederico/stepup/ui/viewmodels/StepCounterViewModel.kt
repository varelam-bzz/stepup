package ch.matiasfederico.stepup.ui.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StepCounterViewModel(application: Application) : AndroidViewModel(application),
    SensorEventListener {

    private val sensorManager: SensorManager =
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("StepupPrefs", Context.MODE_PRIVATE)

    private var sensor: Sensor? = null
    private var initialStepCount: Float? = null

    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> get() = _steps

    init {
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        initializeStepCount()
    }

    private fun initializeStepCount() {
        val lastSavedCumulativeSteps = sharedPreferences.getFloat("lastSavedCumulativeSteps", 0f)
        val savedSteps = sharedPreferences.getInt("totalSteps", 0)

        // Calculate the steps taken since last saved session
        if (lastSavedCumulativeSteps > 0) {
            initialStepCount = lastSavedCumulativeSteps
            _steps.update { savedSteps }
        }
    }

    fun startTrackingSteps() {
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopTrackingSteps() {
        sensorManager.unregisterListener(this)
        saveStepsToPreferences()
    }

    private fun saveStepsToPreferences() {
        sharedPreferences.edit()
            .putFloat("lastSavedCumulativeSteps", initialStepCount ?: 0f)
            .putInt("totalSteps", _steps.value)
            .apply()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let { sensorEvent ->
            if (sensorEvent.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                val currentCumulativeSteps = sensorEvent.values[0]

                if (initialStepCount == null) {
                    initialStepCount = currentCumulativeSteps
                }

                // Calculate steps since the initial count
                val stepsTaken = (currentCumulativeSteps - (initialStepCount ?: 0f)).toInt()
                _steps.update { stepsTaken }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No implementation needed
    }
}
