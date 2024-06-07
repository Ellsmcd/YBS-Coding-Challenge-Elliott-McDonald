package com.elliott.ybscodingchallenge.features.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elliott.ybscodingchallenge.R
import com.elliott.ybscodingchallenge.data.searchapi.DescriptionContent
import com.elliott.ybscodingchallenge.data.searchapi.Photo
import com.elliott.ybscodingchallenge.features.home.ui.imageitem.ImageItemComponent
import com.elliott.ybscodingchallenge.features.home.ui.tagsearch.TagSearchComponent
import com.elliott.ybscodingchallenge.features.home.ui.tagsearch.TagSearchScreen
import com.elliott.ybscodingchallenge.features.home.ui.tagsearch.UserSearchComponent
import com.elliott.ybscodingchallenge.ui.theme.YBSCodingChallengeTheme


@Composable
fun HomeScreen(
    navigateToDetail: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = state.showDetailScreen) {
        if (state.showDetailScreen) {
            viewModel.onEvent(HomeEvent.DetailScreenOpened)
            navigateToDetail()
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            TagSearchScreen(
                searchTerm = state.searchInput,
                onEvent = {
                    viewModel.onEvent(it)
                }
            )
        }
        item {
            UserSearchComponent(
                userId = state.userId,
                onEvent = {
                    viewModel.onEvent(it)
                },
            )
        }
        item {
            TagSearchComponent(
                tags = state.tags,
                strictSearch = state.strictSearch,
                onEvent = {
                    viewModel.onEvent(it)
                })
        }
        when(val flickrSearchResults = state.flickrSearchResults) {
            is FlickrApiState.Loading -> {
                item {
                    LoadingScreen()
                }
            }
            is FlickrApiState.Error -> {
                item {
                    ErrorScreen(
                        onEvent = {
                            viewModel.onEvent(it)
                        }
                    )
                }
            }
            is FlickrApiState.DataAvailable -> {
                flickrSearchResults.response?.stat?.let {
                    if (it == "fail") {
                        item {
                            Text(
                                text = stringResource(id = R.string.no_matching_pictures),
                                modifier = Modifier.padding(horizontal = 16.dp),
                                textAlign = TextAlign.Center)
                        }
                    }
                }
                flickrSearchResults.response?.photos?.let {
                    itemsIndexed(it.photo) { index, photo ->
                        ImageItemComponent(
                            photo = photo,
                            modifier = Modifier.padding(top = if (index == 0) 8.dp else 0.dp, bottom = 24.dp),
                            onEvent = { event ->
                                viewModel.onEvent(event)
                            },
                            index = index
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(
    onEvent: (HomeEvent) -> Unit
) {
    Text(stringResource(id = R.string.something_went_wrong))
    Button(
        onClick = {
            onEvent(HomeEvent.SomethingWentWrong)
        },
        content = {
            Text(text = stringResource(id = R.string.try_again))
        },
        modifier = Modifier.testTag("tryAgain")
    )
}
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.testTag("loadingSpinner")
        )
    }
}

@Preview
@Composable
fun ImageItemPreview() {
    YBSCodingChallengeTheme {
        ImageItemComponent(
            photo = Photo(
                datetaken = "2024-04-21 14:11:00",
                description = DescriptionContent("A happy cow that I met on a walk"),
                farm = 0,
                id = "",
                isfamily = 0,
                isfriend = 0,
                ispublic = 0,
                owner = "",
                secret = "",
                server = "",
                title = "Picture of a happy cow!",
                tags = "Yorkshire",
                views = "100",
            )
        )
    }
}