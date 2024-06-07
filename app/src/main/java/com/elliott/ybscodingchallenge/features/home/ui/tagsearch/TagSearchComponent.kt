package com.elliott.ybscodingchallenge.features.home.ui.tagsearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.elliott.ybscodingchallenge.features.home.ui.HomeEvent
import com.elliott.ybscodingchallenge.ui.components.TagComponent

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
                    .testTag("tag$index")
            )
        }
    }
}