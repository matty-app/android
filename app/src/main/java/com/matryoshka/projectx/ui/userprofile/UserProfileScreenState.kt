package com.matryoshka.projectx.ui.userprofile

import android.net.Uri
import androidx.compose.runtime.Stable
import com.matryoshka.projectx.data.interest.Interest
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.textFieldState
import com.matryoshka.projectx.ui.validator.EmailExistsValidator
import com.matryoshka.projectx.ui.validator.EmailValidator
import com.matryoshka.projectx.ui.validator.NameValidator

data class UserProfileScreenState(
    val photoUri: Uri? = null,
    val photoExists: Boolean = false,
    val name: String = "",
    val email: String = "",
    val aboutMe: String = "",
    val interests: List<Interest> = emptyList(),
    val formState: UserProfileFormStatePreview = UserProfileFormStatePreview(),
    val displayPhotoDialog: Boolean = false,
    val displayNameDialog: Boolean = false,
    val displayEmailDialog: Boolean = false,
    val displayAboutMeDialog: Boolean = false,
    val status: ScreenStatus = ScreenStatus.READY
) {
    val isProgressIndicatorVisible: Boolean
        get() = status == ScreenStatus.LOADING
}

//for preview
@Stable
open class UserProfileFormStatePreview(
    name: String? = null,
    email: String? = null,
    aboutMe: String? = null
) {
    val nameField = textFieldState(
        initialValue = name ?: "",
        validators = listOf(NameValidator())
    )
    open val emailField = textFieldState(
        initialValue = email ?: "",
        validators = listOf(EmailValidator())
    )
    val aboutMeField = textFieldState(initialValue = aboutMe ?: "")
}

@Stable
class UserProfileFormState(
    authService: AuthService,
    name: String? = null,
    email: String? = null,
    aboutMe: String? = null
) : UserProfileFormStatePreview(name, email, aboutMe) {
    override val emailField = textFieldState(
        initialValue = email ?: "",
        validators = listOf(
            EmailValidator(),
            EmailExistsValidator(authService)
        )
    )
}