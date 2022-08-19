package com.matryoshka.projectx

object SavedStateKey {
    const val LOCATION_KEY = "location"
    const val INTEREST_KEY = "interest"
    const val INTERESTS_KEY = "interests"
    const val EVENT_KEY = "event"
}

object NavArgument {
    const val ARG_EMAIL = "email"
    const val ARG_USER_NAME = "user_name"
    const val ARG_LOCATION = "location"
    const val ARG_INTEREST_ID = "interest_id"
    const val ARG_EVENT_ID = "event_id"
    const val ARG_EVENT = "event"
}

object MattyApiPath {
    const val SEND_REGISTER_CODE_PATH = "registration/code"
    const val REGISTER_PATH = "registration"
    const val SEND_LOGIN_CODE_PATH = "login/code"
    const val LOGIN_PATH = "login"
    const val REFRESH_TOKENS_PATH = "auth/refresh"
    const val GET_CURRENT_USER_PATH = "user/me"
}

object SharedPrefsKey {
    const val PREF_SIGN_IN_EMAIL = "SIGN_IN_EMAIL"
    const val PREF_USER_NAME = "USER_NAME"
    const val PREF_IS_NEW_USER = "IS_NEW_USER"
    const val PREF_ACCESS_TOKEN = "ACCESS_TOKEN"
    const val PREF_REFRESH_TOKEN = "REFRESH_TOKEN"
    const val PREF_USER_CACHE = "USER_CACHE"
}