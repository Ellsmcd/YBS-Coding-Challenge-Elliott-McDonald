package com.elliott.ybscodingchallenge.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
            items(state.flickrSearchResults!!.photos!!.photo) { photo ->
                ImageItem(photo = photo)
            }
        }
    }
}

@Composable
fun ImageItem(
    photo: Photo
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp),
        ) {
            AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                .data("https://farm${photo.farm}.staticflickr.com/${photo.server}/buddyicons/${photo.owner}.jpg")
                .build(),
                contentDescription = photo.title,
                placeholder = (if (LocalInspectionMode.current) painterResource(id = R.drawable.coil_preview) else null),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape))
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
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().background(Color(0x4d000000))
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
                title = "Picture of a happy cow!"
            )
        )
    }
}