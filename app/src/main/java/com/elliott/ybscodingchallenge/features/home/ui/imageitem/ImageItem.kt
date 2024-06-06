package com.elliott.ybscodingchallenge.features.home.ui.imageitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.elliott.ybscodingchallenge.R
import com.elliott.ybscodingchallenge.data.searchapi.Photo
import com.elliott.ybscodingchallenge.features.home.ui.HomeEvent
import com.elliott.ybscodingchallenge.ui.components.TagComponent
import com.elliott.ybscodingchallenge.ui.theme.Typography

@Composable
fun ImageItem(
    photo: Photo,
    modifier: Modifier = Modifier,
    onEvent: (HomeEvent) -> Unit = { },
    isDetailPage: Boolean = false,
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
            modifier = Modifier
                .padding(horizontal = 8.dp),
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
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                    )
                    .size(48.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = photo.owner,
                    style = Typography.bodySmall,
                    modifier = if (!isDetailPage) {
                        Modifier.clickable {
                            onEvent(HomeEvent.SearchTermAdded(photo.owner))
                        }
                    } else {
                        Modifier
                    }
                )
                if (isDetailPage) {
                    Text(
                        text = photo.datetaken,
                        style = Typography.bodySmall,
                    )
                }
            }
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
                    modifier = if (!isDetailPage) {
                        Modifier
                            .padding(
                                start = if (index == 0) 8.dp else 0.dp,
                            )
                            .clickable {
                                onEvent(HomeEvent.SearchTermAdded(tag))
                            }
                    } else {
                        Modifier
                            .padding(
                                start = if (index == 0) 8.dp else 0.dp,
                            )
                    }
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        if (photo.url_h.isNullOrBlank()) {
                        "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_b.jpg"
                    } else {
                        photo.url_h
                        }
                    )
                    .build(),
                loading = {
                    Box(
                        modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(Color.White),
                        Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                contentDescription = photo.title,
                modifier = if (!isDetailPage) {
                    Modifier
                        .clickable {
                            onEvent(HomeEvent.PhotoTapped(photo))
                        }
                } else {
                    Modifier
                },
                contentScale = ContentScale.Fit,
            )
            if (isDetailPage) {
                Text(
                    text = photo.views + " views",
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    textAlign = TextAlign.Left)
                Text(
                    text = photo.description._content,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    textAlign = TextAlign.Left)
            }
        }
    }
}