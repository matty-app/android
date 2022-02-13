package com.matryoshka.projectx.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.matryoshka.projectx.navigation.AppNavHost
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navAdapter: NavAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            ProjectxTheme {
                AppNavHost(navController = navController, navAdapter = navAdapter)
            }
        }
    }
}