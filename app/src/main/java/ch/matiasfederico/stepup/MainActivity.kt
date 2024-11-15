package ch.matiasfederico.stepup

import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.matiasfederico.stepup.ui.theme.Footer
import ch.matiasfederico.stepup.ui.theme.Header
import ch.matiasfederico.stepup.ui.theme.StepupTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
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
                        rememberPermissionState(permission = android.Manifest.permission.ACTIVITY_RECOGNITION)
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
    permission: com.google.accompanist.permissions.PermissionState,
    counter: MutableStateFlow<Int>
) {
    val counterState = counter.collectAsState()
    val steps = counterState.value
    val calorieBurnRate = 0.04
    val caloriesBurned = steps * calorieBurnRate
    var dailyGoal by remember { mutableIntStateOf(6000) }
    var newGoalInput by remember { mutableStateOf("") }
    var currentScreen by remember { mutableStateOf("home") } // Track current screen
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
        // Top content
        Header()

        // Main content area based on current screen
        Box(modifier = Modifier.weight(1f)) {
            when (currentScreen) {
                "home" -> {
                    // Home screen content
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Hello, $username! Daily Summary",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        when {
                            permission.status.isGranted -> {
                                Text(
                                    text = "Steps: $steps / $dailyGoal",
                                    fontSize = 24.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = "Calories Burned: %.2f".format(caloriesBurned),
                                    fontSize = 24.sp,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                Text(
                                    text = "Daily Goal: $dailyGoal steps",
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                TextField(
                                    value = newGoalInput,
                                    onValueChange = { newGoalInput = it },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    label = { Text("New Daily Goal") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp)
                                )
                                Button(onClick = {
                                    newGoalInput.toIntOrNull()?.let {
                                        dailyGoal = it
                                    }
                                }) {
                                    Text(text = "Set New Daily Goal")
                                }
                            }

                            permission.status.shouldShowRationale -> {
                                Button(onClick = {
                                    permission.launchPermissionRequest()
                                }) {
                                    Text(text = "Grant permission")
                                }
                            }

                            else -> {
                                Button(onClick = {
                                    permission.launchPermissionRequest()
                                }) {
                                    Text(text = "Request permission")
                                }
                            }
                        }
                    }
                }
                "details" -> {
                    // Placeholder for Details Screen
                    Text(
                        text = "Details Screen Content",
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                "user" -> {
                    // Placeholder for User Screen
                    UserInputForm() // Use the existing UserInputForm logic here
                }
            }
        }

        // Footer navigation
        Footer(
            currentScreen = currentScreen,
            onScreenChange = { newScreen ->
                currentScreen = newScreen
            }
        )
    }
}

@Composable
fun UserInputForm() {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var dailyStepGoal by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Username")

        TextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text("Enter username") }
        )

        Text("Daily Step Goal")

        TextField(
            value = dailyStepGoal,
            onValueChange = { dailyStepGoal = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text("Enter daily step goal") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {
                // Save preferences or perform any action
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Save")
        }
    }
}
