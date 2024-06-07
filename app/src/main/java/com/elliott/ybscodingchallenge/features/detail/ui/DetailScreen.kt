package com.elliott.ybscodingchallenge.features.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elliott.ybscodingchallenge.data.searchapi.DescriptionContent
import com.elliott.ybscodingchallenge.data.searchapi.Photo
import com.elliott.ybscodingchallenge.features.home.ui.imageitem.ImageItemComponent
import com.elliott.ybscodingchallenge.ui.theme.YBSCodingChallengeTheme

@Composable
fun DetailScreen() {
    val viewModel: DetailScreenViewModel = hiltViewModel()
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        ImageItemComponent(
            viewModel.state,
            modifier = Modifier.padding(vertical = 24.dp),
            isDetailPage = true,
        )
    }
}

@Preview
@Composable
fun ImageItemPreview() {
    YBSCodingChallengeTheme {
        ImageItemComponent(
            Photo(
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
            ),
            modifier = Modifier.padding(vertical = 24.dp),
            isDetailPage = true,
        )
    }
}