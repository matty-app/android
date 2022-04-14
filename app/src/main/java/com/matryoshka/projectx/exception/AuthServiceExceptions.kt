package com.matryoshka.projectx.exception

import com.matryoshka.projectx.R

class SendSignInLinkToEmailException(cause: Throwable) :
    ProjectxException(cause, R.string.error_sending_link)

class SignInByEmailLinkException(cause: Throwable) :
    ProjectxException(cause, R.string.error_sign_in_by_link)

class SignUpByEmailLinkException(cause: Throwable) :
    ProjectxException(cause, R.string.error_sign_up_by_link)

class CheckEmailExistsException(cause: Throwable) :
    ProjectxException(cause, R.string.error_check_email_exists)

class UpdateUserException(cause: Throwable) :
    ProjectxException(cause, R.string.error_check_email_exists)