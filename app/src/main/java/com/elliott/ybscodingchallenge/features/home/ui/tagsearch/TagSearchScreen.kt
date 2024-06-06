package com.elliott.ybscodingchallenge.features.home.ui.tagsearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.elliott.ybscodingchallenge.features.home.ui.HomeEvent

@Composable
fun TagSearchScreen(
    searchTerm: String,
    onEvent: (HomeEvent) -> Unit = { },
) {
    TextField(
        value = searchTerm,
        onValueChange = { onEvent(HomeEvent.TextChanged(it)) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).testTag("searchBox"),
        label = { Text("Tags") },
        trailingIcon = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = {
                        onEvent(HomeEvent.SearchTermAdded(searchTerm))
                    },
                    modifier = Modifier.padding(horizontal = 8.dp).testTag("searchButton")
                ) {
                    Text("Add")
                }
            }

        }
    )
}
