package com.matryoshka.projectx.exception

import androidx.annotation.StringRes
import com.matryoshka.projectx.R

sealed class AuthServiceException(message: String?, @StringRes resId: Int) :
    ProjectxException(message, resId)

class SendSignInLinkToEmailException(message: String?) :
    AuthServiceException(message, R.string.error_sending_link)

class SignInByEmailLinkException(message: String?) :
    AuthServiceException(message, R.string.error_sign_in_by_link)

class SignUpByEmailLinkException(message: String?) :
    AuthServiceException(message, R.string.error_sign_up_by_link)

class CheckEmailExistsException(message: String?) :
    AuthServiceException(message, R.string.error_check_email_exists)

class UpdateUserException(message: String?) :
    AuthServiceException(message, R.string.error_check_email_exists)