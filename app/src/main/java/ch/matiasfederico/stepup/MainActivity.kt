package ch.matiasfederico.stepup

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import ch.matiasfederico.stepup.ui.theme.StepupTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MainActivity : ComponentActivity(), SensorEventListener {
    private val sensorManager: SensorManager by lazy {
        getSystemService(SENSOR_SERVICE) as SensorManager
    }

    private var sensor: Sensor? = null
    private val counter = MutableStateFlow(0)

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        setContent {
            StepupTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val permission = rememberPermissionState(permission = android.Manifest.permission.ACTIVITY_RECOGNITION)
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
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let { sensorEvent ->
            if (sensorEvent.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                counter.update { it + 1 }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not yet implemented
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(permission: com.google.accompanist.permissions.PermissionState, counter: MutableStateFlow<Int>) {
    val counterState = counter.collectAsState()
    val steps = counterState.value
    val calorieBurnRate = 0.04
    val caloriesBurned = steps * calorieBurnRate
    var dailyGoal by remember { mutableIntStateOf(6000) }
    var newGoalInput by remember { mutableStateOf("") }
    val username by remember { mutableStateOf("User") }
    var showDetails by remember { mutableStateOf(false) }

    if (showDetails) {
        SummaryScreen(steps, caloriesBurned, dailyGoal) {
            showDetails = false
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Top content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(text = "StepUp", fontSize = 32.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                Text(text = "Hello, $username! Daily Summary", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

                when {
                    permission.status.isGranted -> {
                        Text(text = "Steps: $steps / $dailyGoal", fontSize = 24.sp, modifier = Modifier.padding(bottom = 8.dp))
                        Text(text = "Calories Burned: %.2f".format(caloriesBurned), fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
                        Text(text = "Daily Goal: $dailyGoal steps", fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))
                        TextField(
                            value = newGoalInput,
                            onValueChange = { newGoalInput = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            label = { Text("New Daily Goal") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
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
            // Add Footer component
            Footer(onDetailsClick = { showDetails = true })
        }
    }
}

@Composable
fun SummaryScreen(steps: Int, caloriesBurned: Double, dailyGoal: Int, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "StepUp - Summary",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Daily Summary",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Steps: $steps / $dailyGoal",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Calories Burned: %.2f".format(caloriesBurned),
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Weekly Summary",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Total Steps: ${steps * 7}",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Calories Burned: %.2f".format(caloriesBurned * 7),
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Monthly Summary",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Total Steps: ${steps * 30}",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Calories Burned: %.2f".format(caloriesBurned * 30),
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Back")
        }
    }
}

@Composable
fun Footer(onDetailsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = {
            // Handle Home button click
        }) {
            Icon(Icons.Filled.Home, contentDescription = "Home")
        }

        Button(onClick = onDetailsClick) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Info, contentDescription = "Details")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Details")
            }
        }

        IconButton(onClick = {
            // Handle User Profile button click
        }) {
            Icon(Icons.Filled.Person, contentDescription = "User Profile")
        }
    }
}
