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

    private val _username =
        MutableLiveData(sharedPreferences.getString("username", "") ?: "")
    val username: LiveData<String> get() = _username

    private val _dailyStepGoal = MutableLiveData(sharedPreferences.getInt("dailyStepGoal", 0))
    val dailyStepGoal: LiveData<Int> get() = _dailyStepGoal

    fun saveUsername(newUsername: String) {
        viewModelScope.launch {
            _username.value = newUsername
        }
    }

    fun saveDailyStepGoal(newGoal: Int) {
        viewModelScope.launch {
            _dailyStepGoal.value = newGoal
        }
    }

    fun savePreferences(): Boolean {
        try {
            val usernameValue = username.value
            if (usernameValue.isNullOrEmpty()) {
                throw IllegalArgumentException("Username cannot be null or empty")
            } else {
                sharedPreferences.edit().putString("username", usernameValue).apply()
            }

            dailyStepGoal.value?.let {
                if (it <= 0) {
                    throw IllegalArgumentException("Daily Step Goal must be a positive number")
                } else {
                    sharedPreferences.edit().putInt("dailyStepGoal", it).apply()
                }
            } ?: throw IllegalArgumentException("Daily Step Goal cannot be null")
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}
