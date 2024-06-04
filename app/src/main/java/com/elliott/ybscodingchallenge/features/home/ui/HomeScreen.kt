package com.elliott.ybscodingchallenge.features.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliott.ybscodingchallenge.R
import com.elliott.ybscodingchallenge.data.searchapi.DescriptionContent
import com.elliott.ybscodingchallenge.data.searchapi.Photo
import com.elliott.ybscodingchallenge.features.home.ui.imageitem.ImageItem
import com.elliott.ybscodingchallenge.features.home.ui.tagsearch.TagSearchScreen
import com.elliott.ybscodingchallenge.ui.components.TagComponent
import com.elliott.ybscodingchallenge.ui.theme.YBSCodingChallengeTheme


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onImageClick: (Photo) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
        if (state.flickrSearchResults == null) {
            item {
                LoadingScreen()
            }
        }
        state.flickrSearchResults?.stat?.let {
            if (it == "fail") {
                item {
                    Text(
                        text = "No pictures found matching search terms. Please try again",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center)
                }
            }
        }
        state.flickrSearchResults?.photos?.let {
            itemsIndexed(it.photo) { index, photo ->
                ImageItem(
                    photo = photo,
                    modifier = Modifier.padding(top = if (index == 0) 8.dp else 0.dp, bottom = 24.dp),
                    onEvent = {
                        viewModel.onEvent(it)
                    },
                    onImageClick = {
                        onImageClick(it)
                    },
                )
            }
        }
    }
}


@Composable
fun TagSearchComponent(
    tags: List<String>,
    strictSearch: Boolean,
    onEvent: (HomeEvent) -> Unit = { },
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        modifier = Modifier
            .padding(bottom = 24.dp)
            .heightIn(max = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)
    ) {
        item {
            Checkbox(
                checked = strictSearch,
                onCheckedChange = {
                    onEvent(HomeEvent.StrictSearchInteracted(it))
                }
            )
        }
        itemsIndexed(tags) { index, tag ->
            TagComponent(
                tag,
                modifier = Modifier
                    .padding(
                        start = if (index == 0) 8.dp else 0.dp,
                    )
                    .clickable {
                        onEvent(HomeEvent.TagRemoved(tag))
                    }
            )
        }
    }
}
@Composable
fun UserSearchComponent(
    userId: String?,
    onEvent: (HomeEvent) -> Unit = { },
) {
    userId?.let {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, bottom = 24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.buddy_icon_placeholder),
                "",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(width = 1.dp, Color.Black, shape = CircleShape)
            )
            TagComponent(
                text = it,
                modifier = Modifier
                    .clickable {
                        onEvent(HomeEvent.UserIdRemoved)
                    }
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun ImageItemPreview() {
    YBSCodingChallengeTheme {
        ImageItem(
            photo = Photo(
                dateTaken = "2024-04-21 14:11:00",
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