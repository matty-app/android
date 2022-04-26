package com.matryoshka.projectx.ui.common

import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController

typealias NavForResultAction = (NavController, LifecycleOwner) -> Unit

val NAV_FOR_RESULT_DEFAULT_ACTION: NavForResultAction = { _, _ -> }