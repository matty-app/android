package com.matryoshka.projectx.ui.launch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.matryoshka.projectx.data.user.UsersRepository
import com.matryoshka.projectx.exception.UserSignedOutException
import com.matryoshka.projectx.navigation.navToEventsFeedScreen
import com.matryoshka.projectx.navigation.navToSignInScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

private const val TAG = "LaunchViewModel"

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    fun checkUserSignedIn(navController: NavController) {
        val methodName = "checkUserSignedIn"
        viewModelScope.launch {
            try {
                usersRepository.getCurrent() ?: throw UserSignedOutException()
                navController.navToEventsFeedScreen()
            } catch (ex: UserSignedOutException) {
                navController.navToSignInScreen()
            } catch (ex: Exception) {
                Log.d(TAG, "$methodName: ${ex.message}")
            }
        }
    }
}