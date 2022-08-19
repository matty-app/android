package com.matryoshka.projectx.exception

import com.matryoshka.projectx.R

class SaveUserException(cause: Throwable) :
    AppException(cause, R.string.error_save_user_details)