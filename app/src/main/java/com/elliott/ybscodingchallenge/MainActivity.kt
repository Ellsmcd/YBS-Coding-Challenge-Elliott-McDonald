package com.elliott.ybscodingchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliott.ybscodingchallenge.data.searchapi.Photo
import com.elliott.ybscodingchallenge.features.home.ui.HomeScreen
import com.elliott.ybscodingchallenge.features.home.ui.HomeViewModel
import com.elliott.ybscodingchallenge.features.home.ui.imageitem.ImageItem
import com.elliott.ybscodingchallenge.ui.theme.YBSCodingChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YBSCodingChallengeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeAndDetailLayout(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomeAndDetailLayout(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    ) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            HomeScreen(
                state = state,
                onEvent = {
                          viewModel.onEvent(it)
                          },
                onImageClick = {
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it)
                }
            )
        },
        detailPane = {
            val photo = navigator.currentDestination?.content as? Photo
            AnimatedPane {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    if (photo == null) {
                        Text("No picture is loaded")
                    } else {
                        ImageItem(
                            photo = photo,
                            isDetailPage = true)
                    }
                }
            }
        }
    )
}

