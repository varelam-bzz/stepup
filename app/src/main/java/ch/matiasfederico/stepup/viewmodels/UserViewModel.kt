package ch.matiasfederico.stepup.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("StepupPrefs", Context.MODE_PRIVATE)

    private val _username = MutableLiveData(sharedPreferences.getString("username", "") ?: "")
    val username: LiveData<String> get() = _username // Observable username

    private val _dailyStepGoal = MutableLiveData(sharedPreferences.getInt("dailyStepGoal", 6000))
    val dailyStepGoal: LiveData<Int> get() = _dailyStepGoal // Observable daily step goal

    fun saveUsername(newUsername: String) {
        // Save username locally and update live data
        viewModelScope.launch {
            _username.value = newUsername
        }
    }

    fun saveDailyStepGoal(newGoal: Int) {
        // Save daily step goal locally and update live data
        viewModelScope.launch {
            _dailyStepGoal.value = newGoal
        }
    }

    fun savePreferences(): Boolean {
        // Persist user data to SharedPreferences
        return try {
            sharedPreferences.edit().apply {
                putString("username", _username.value)
                putInt("dailyStepGoal", _dailyStepGoal.value ?: 0)
                apply()
            }
            true // Successful save
        } catch (e: Exception) {
            e.printStackTrace()
            false // Failed save
        }
    }
}
