package com.matryoshka.projectx.ui.userprofile

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.matryoshka.projectx.SavedStateKey.INTERESTS_KEY
import com.matryoshka.projectx.data.image.ImagesRepository
import com.matryoshka.projectx.data.image.LocalImagesRepository
import com.matryoshka.projectx.data.user.User
import com.matryoshka.projectx.data.user.UsersRepository
import com.matryoshka.projectx.navigation.Screen
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.authorizedAction
import com.matryoshka.projectx.utils.collectOnce
import com.matryoshka.projectx.utils.compressImage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

private const val TAG = "UserProfileViewModel"

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val localImagesRepository: LocalImagesRepository,
    private val imagesRepository: ImagesRepository,
    private val userRepository: UsersRepository
) : ViewModel() {

    private lateinit var user: User
    private lateinit var photoUri: Uri

    private var isInitialized = false

    var state by mutableStateOf(
        UserProfileScreenState(
            status = ScreenStatus.LOADING
        )
    )
        private set

    val actions = UserProfileActions(
        onBackClick = { navController -> navController.popBackStack() },
        onRemovePhotoClick = this::deletePhoto,
        takePhoto = this::takePhoto,
        selectPhoto = this::takePhoto,
        onNameChanged = this::onNameChanged,
        onEmailChanged = this::onEmailChanged,
        onAboutMeChanged = this::onAboutMeChanged,
        onInterestsClick = this::onInterestsClick,
        setDisplayPhotoDialog = { isDisplayed -> setDisplayPhotoDialog(isDisplayed) },
        setDisplayNameDialog = { isDisplayed -> setDisplayNameDialog(isDisplayed) },
        setDisplayEmailDialog = { isDisplayed -> setDisplayEmailDialog(isDisplayed) },
        setDisplayAboutMeDialog = { isDisplayed -> setDisplayAboutMeDialog(isDisplayed) }
    )

    fun init(navController: NavController) {
        if (!isInitialized) {
            viewModelScope.launch {
                try {
                    authorizedAction(navController) {
                        user = userRepository.getCurrent()!!
                    }
                    user = userRepository.getCurrent()!!
                    photoUri = localImagesRepository.getAvatarUri(user.id)
                    val photoExists = imagesRepository.getAvatar(
                        user.id,
                        localImagesRepository.getAvatarFile(user.id)
                    ) != null
                    state = state.copy(
                        photoUri = photoUri,
                        photoExists = photoExists,
                        name = user.name,
                        email = user.email ?: "",
                        aboutMe = user.aboutMe ?: "",
                        formState = UserProfileFormState(
                            user.name,
                            user.email,
                            user.aboutMe
                        ),
                        interests = user.interests,
                        status = ScreenStatus.READY
                    )
                } catch (ex: Exception) {
                    //TODO("Set error status")
                }
            }
        }
    }

    private fun setDisplayPhotoDialog(isDisplayed: Boolean) {
        state = state.copy(displayPhotoDialog = isDisplayed)
    }

    private fun setDisplayNameDialog(isDisplayed: Boolean) {
        if (isDisplayed) state.formState.nameField.onChange(state.name)
        state = state.copy(displayNameDialog = isDisplayed)
    }

    private fun setDisplayEmailDialog(isDisplayed: Boolean) {
        if (isDisplayed) state.formState.emailField.onChange(state.email)
        state = state.copy(displayEmailDialog = isDisplayed)
    }

    private fun setDisplayAboutMeDialog(isDisplayed: Boolean) {
        if (isDisplayed) state.formState.aboutMeField.onChange(state.aboutMe)
        state = state.copy(displayAboutMeDialog = isDisplayed)
    }

    private fun deletePhoto() {
        setLoadingStatus()
        viewModelScope.launch {
            try {
                imagesRepository.deleteAvatar(user.id)
                user = user.copy(thumbnail = null)
                userRepository.save(user)
                if (localImagesRepository.deleteAvatar(user.id)) {
                    state = state.copy(photoExists = false)
                }
                state = state.copy(displayPhotoDialog = false, status = ScreenStatus.READY)
            } catch (ex: Exception) {
                //TODO("Set error status")
            }
        }
    }

    private fun takePhoto(context: Context, sourcePhotoUri: Uri? = null) {
        setLoadingStatus()
        viewModelScope.launch {
            try {
                if (sourcePhotoUri != null) {
                    localImagesRepository.saveImage(sourcePhotoUri, photoUri)
                }
                imagesRepository.saveAvatar(user.id, photoUri)
                user = user.copy(thumbnail = compressImage(context, photoUri))
                userRepository.save(user)
                state = state.copy(
                    photoExists = true,
                    displayPhotoDialog = false,
                    status = ScreenStatus.READY
                )
            } catch (ex: Exception) {
                //TODO("Set error status")
            }
        }
    }

    private fun onNameChanged() {
        setLoadingStatus()
        viewModelScope.launch {
            try {
                if (state.formState.nameField.validate()) {
                    state = state.copy(name = state.formState.nameField.value)
                    user = user.copy(name = state.name)
                    userRepository.save(user)
                    setDisplayNameDialog(false)
                }
                setReadyStatus()
            } catch (ex: Exception) {
                //TODO("Set error status")
            }
        }
    }

    private fun onEmailChanged(navController: NavController) {
        setLoadingStatus()
        viewModelScope.launch {
            try {
                if (state.formState.emailField.validate()) {
                    state = state.copy(email = state.formState.emailField.value)
                    //authService.sendSignInLinkToEmail(state.email)
                    //navController.navToMailConfirmScreen(state.email)
                    setDisplayEmailDialog(false)
                }
                setReadyStatus()
            } catch (ex: Exception) {
                //TODO("Set error status")
            }
        }
    }

    private fun onAboutMeChanged() {
        setLoadingStatus()
        viewModelScope.launch {
            try {
                if (state.formState.aboutMeField.validate()) {
                    state = state.copy(aboutMe = state.formState.aboutMeField.value)
                    user = user.copy(aboutMe = state.aboutMe)
                    userRepository.save(user)
                    setDisplayAboutMeDialog(false)
                }
                setReadyStatus()
            } catch (ex: Exception) {
                //TODO("Set error status")
            }
        }
    }

    private fun onInterestsClick(navController: NavController) {
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
        viewModelScope.launch {
            savedStateHandle?.collectOnce(
                key = INTERESTS_KEY,
                initialValue = state.interests
            ) { interests ->
                state = state.copy(interests = interests, status = ScreenStatus.LOADING)
                user = user.copy(interests = interests)
                try {
                    userRepository.save(user)
                    setReadyStatus()
                } catch (ex: Exception) {
                    //TODO("Set error status")
                }
            }
        }
        navController.navigate(Screen.INTERESTS)
    }

    private fun setLoadingStatus() {
        changeStatus(ScreenStatus.LOADING)
    }

    private fun setReadyStatus() {
        changeStatus(ScreenStatus.READY)
    }

    private fun changeStatus(status: ScreenStatus) {
        state = state.copy(status = status)
    }
}