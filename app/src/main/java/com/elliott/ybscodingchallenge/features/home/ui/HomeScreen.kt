package com.elliott.ybscodingchallenge.features.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.elliott.ybscodingchallenge.R
import com.elliott.ybscodingchallenge.data.searchapi.Photo
import com.elliott.ybscodingchallenge.features.home.ui.tagsearch.TagSearchScreen
import com.elliott.ybscodingchallenge.ui.components.TagComponent
import com.elliott.ybscodingchallenge.ui.theme.Typography
import com.elliott.ybscodingchallenge.ui.theme.YBSCodingChallengeTheme


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.flickrSearchResults == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                TagSearchScreen(
                    searchTerm = state.searchInput,
                    searchUsername = state.searchUserId,
                    onEvent = {
                        viewModel.onEvent(it)
                    }
                )
            }
            item {
                state.userId?.let {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.buddy_icon_placeholder),
                            "",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .border(width = 1.dp, Color.Black, shape = CircleShape))
                        TagComponent(
                            text = it,
                            modifier = Modifier
                                .clickable {
                                    viewModel.onEvent(HomeEvent.UserIdRemoved)
                                }
                        )
                    }
                }
            }
            item {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(1),
                    modifier = Modifier
                        .heightIn(max = 24.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)
                ) {
                    val tags = state.tags
                    item {
                        Checkbox(
                            checked = state.strictSearch,
                            onCheckedChange = {
                                viewModel.onEvent(HomeEvent.StrictSearchInteracted(it))
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
                                    viewModel.onEvent(HomeEvent.TagRemoved(tag))
                                }
                        )
                    }
                }
            }
            if (state.flickrSearchResults!!.stat == "fail") {
                item {
                    Text(text = "Oh no is empty")
                }
            } else {
                itemsIndexed(state.flickrSearchResults!!.photos!!.photo) { index, photo ->
                    ImageItem(
                        photo = photo,
                        modifier = Modifier.padding(top = if (index == 0) 8.dp else 0.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ImageItem(
    photo: Photo,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://farm${photo.farm}.staticflickr.com/${photo.server}/buddyicons/${photo.owner}.jpg")
                    .build(),
                contentDescription = photo.title,
                placeholder = (
                        if (LocalInspectionMode.current)
                            painterResource(id = R.drawable.coil_preview)
                        else painterResource(id = R.drawable.buddy_icon_placeholder)),
                error = painterResource(id = R.drawable.buddy_icon_placeholder),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(width = 1.dp, Color.Black, shape = CircleShape)
            )
            Text(
                text = photo.owner,
                style = Typography.bodySmall,
            )
        }
        Text(
            text = photo.title,
            style = Typography.titleLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        LazyHorizontalGrid(
            rows = GridCells.Fixed(1),
            modifier = Modifier.heightIn(max = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val tags = photo.tags.split(" ").toList()
            itemsIndexed(tags) { index, tag ->
                TagComponent(
                    tag,
                    modifier = Modifier
                        .padding(
                            start = if (index == 0) 8.dp else 0.dp,
                        )
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x4d000000))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_b.jpg")
                    .build(),
                contentDescription = photo.title,
                contentScale = ContentScale.Fit,
                placeholder = (if (LocalInspectionMode.current) painterResource(id = R.drawable.coil_preview) else null),
            )
        }
    }
}

@Preview()
@Composable
fun ImageItemPreview() {
    YBSCodingChallengeTheme {
        ImageItem(
            photo = Photo(
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
            )
        )
    }
}