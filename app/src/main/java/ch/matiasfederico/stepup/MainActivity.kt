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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.matiasfederico.stepup.ui.theme.DetailsScreen
import ch.matiasfederico.stepup.ui.theme.Footer
import ch.matiasfederico.stepup.ui.theme.Header
import ch.matiasfederico.stepup.ui.theme.HomeScreen
import ch.matiasfederico.stepup.ui.theme.StepupTheme
import ch.matiasfederico.stepup.ui.theme.UserInputForm
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MainActivity : ComponentActivity(), SensorEventListener {
    private val sensorManager: SensorManager by lazy {
        getSystemService(SENSOR_SERVICE) as SensorManager
    }

    private var sensor: Sensor? = null
    private val counter = MutableStateFlow(0)
    private lateinit var sharedPreferences: SharedPreferences
    private var initialStepCount: Float? = null

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sharedPreferences = getSharedPreferences("stepup_prefs", MODE_PRIVATE)
        counter.update { sharedPreferences.getInt("total_steps", 0) }

        setContent {
            StepupTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val permission =
                        rememberPermissionState(permission = Manifest.permission.ACTIVITY_RECOGNITION)
                    MainScreen(permission, counter)
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

        sharedPreferences.edit().putInt("total_steps", counter.value).apply()
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
                        "total_steps", 0
                    )).toInt()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not yet implemented
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    permission: PermissionState, counter: MutableStateFlow<Int>
) {
    val counterState = counter.collectAsState()
    val steps = counterState.value
    val calorieBurnRate = 0.04
    val caloriesBurned = steps * calorieBurnRate
    val dailyGoal by remember { mutableIntStateOf(6000) }
    var currentScreen by remember { mutableStateOf("home") }
    val username by remember { mutableStateOf("User") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 16.dp, end = 16.dp, top = 32.dp, bottom = 48.dp
            ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Header()

        Box(modifier = Modifier.weight(1f)) {
            when (currentScreen) {
                "home" -> {
                    HomeScreen(
                        username = username,
                        permission = permission,
                        steps = steps,
                        caloriesBurned = caloriesBurned,
                        dayGoal = dailyGoal
                    )
                }

                "details" -> {
                    DetailsScreen(
                        steps = steps, caloriesBurned = caloriesBurned, dailyGoal = dailyGoal
                    )
                }

                "user" -> {
                    UserInputForm()
                }
            }
        }

        Footer(currentScreen = currentScreen, onScreenChange = { newScreen ->
            currentScreen = newScreen
        })
    }
}
