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
import com.elliott.ybscodingchallenge.features.home.ui.imageitem.ImageItem

@Composable
fun DetailScreen() {
    val viewModel: DetailScreenViewModel = hiltViewModel()
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        ImageItem(
            viewModel.state,
            modifier = Modifier.padding(vertical = 24.dp),
            isDetailPage = true,
        )
    }
}

@Preview
@Composable
fun DetailScreenPreview() {

}