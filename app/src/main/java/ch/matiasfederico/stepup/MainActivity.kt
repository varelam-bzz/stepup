package ch.matiasfederico.stepup

import android.Manifest
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import ch.matiasfederico.stepup.ui.components.Footer
import ch.matiasfederico.stepup.ui.components.Header
import ch.matiasfederico.stepup.ui.components.HomeScreen
import ch.matiasfederico.stepup.ui.theme.StepupTheme
import ch.matiasfederico.stepup.ui.viewmodels.ViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MainActivity : ComponentActivity(), SensorEventListener {
    private val viewModel: ViewModel by viewModels()
    private val sensorManager: SensorManager by lazy {
        getSystemService(SENSOR_SERVICE) as SensorManager
    }

    // TODO: change all of the bottom initialization maybe
    private var sensor: Sensor? = null
    private val counter = MutableStateFlow(0)
    private lateinit var sharedPreferences: SharedPreferences
    private var initialStepCount: Float? = null

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensor =
            sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) // TODO: maybe initialize differently
        sharedPreferences = getSharedPreferences("StepupPrefs", MODE_PRIVATE)  // TODO: change
        counter.update { sharedPreferences.getInt("totalSteps", 0) } // TODO: change

        setContent {
            StepupTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val permission =
                        rememberPermissionState(permission = Manifest.permission.ACTIVITY_RECOGNITION)
                    val counterState = counter.collectAsState()
                    val steps = counterState.value
                    val calorieBurnRate = 0.04
                    val caloriesBurned = steps * calorieBurnRate

                    Header()
                    HomeScreen(
                        permission = permission,
                        steps = steps,
                        caloriesBurned = caloriesBurned,
                        viewModel = viewModel
                    )
                    Footer(this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)

        sharedPreferences.edit().putInt("totalSteps", counter.value).apply()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let { sensorEvent ->
            if (sensorEvent.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                if (initialStepCount == null) {
                    initialStepCount = sensorEvent.values[0]
                }
                val currentStepCount = sensorEvent.values[0] - (initialStepCount ?: 0f)
                counter.update {
                    (currentStepCount + sharedPreferences.getInt(
                        "totalSteps", 0
                    )).toInt()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not yet implemented
    }
}
