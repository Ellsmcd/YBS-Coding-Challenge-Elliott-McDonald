package com.elliott.ybscodingchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elliott.ybscodingchallenge.features.detail.ui.DetailScreen
import com.elliott.ybscodingchallenge.features.home.ui.HomeScreen
import com.elliott.ybscodingchallenge.ui.theme.YBSCodingChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YBSCodingChallengeTheme {
                val navController = rememberNavController()
                Scaffold { innerPadding ->
                    NavHost(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        navController = navController,
                        startDestination = "homeScreen"
                    ) {
                        composable(
                            "homeScreen",
                            enterTransition = {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Left, tween(500)
                                )
                            }
                        )  {
                            HomeScreen(
                                {navController.navigate("detailScreen") })
                        }
                        composable(
                            "detailScreen",
                            enterTransition = {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Left, tween(500)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
                                )
                            }
                        ) {
                            DetailScreen()
                        }
                    }
                }
            }
        }
    }
}


object HomeScreenView

object DetailScreeView


