package ch.matiasfederico.stepup.ui.viewmodels

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

    private val _username =
        MutableLiveData<String>(sharedPreferences.getString("username", "") ?: "")
    val username: LiveData<String> get() = _username

    private val _dailyStepGoal = MutableLiveData<Int>(sharedPreferences.getInt("dailyStepGoal", 0))
    val dailyStepGoal: LiveData<Int> get() = _dailyStepGoal

    fun saveUsername(newUsername: String) {
        viewModelScope.launch {
            _username.value = newUsername
        }
    }

    fun saveDailyStepGoal(newGoal: Int) {
        viewModelScope.launch {
            sharedPreferences.edit().putInt("dailyStepGoal", newGoal).apply()
            _dailyStepGoal.value = newGoal
        }
    }

    fun savePreferences(): Boolean {
        try {
            val usernameValue = username.value
            if (usernameValue != null) {
                sharedPreferences.edit().putString("username", usernameValue).apply()
            } else {
                throw IllegalArgumentException("Username cannot be null")
            }

            dailyStepGoal.value?.let {
                sharedPreferences.edit().putInt("dailyStepGoal", it).apply()
            } ?: throw IllegalArgumentException("Daily Step Goal cannot be null")
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}
