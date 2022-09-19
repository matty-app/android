package com.matryoshka.projectx.exception

import com.matryoshka.projectx.R

class EmailExistsException(cause: Throwable) : AppException(cause, R.string.email_exists)

class SendSignInLinkToEmailException(cause: Throwable) :
    AppException(cause, R.string.error_sending_link)

class SignInByEmailLinkException(cause: Throwable) :
    AppException(cause, R.string.error_sign_in_by_link)

class SignUpByEmailLinkException(cause: Throwable) :
    AppException(cause, R.string.error_sign_up_by_link)

class CheckEmailExistsException(cause: Throwable) :
    AppException(cause, R.string.error_check_email_exists)

class UpdateUserException(cause: Throwable) :
    AppException(cause, R.string.error_check_email_exists)