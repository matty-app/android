package com.matryoshka.projectx.exception

import com.matryoshka.projectx.R

class GetInterestsException(cause: Throwable) :
    ProjectxException(cause, R.string.error_save_user_details)