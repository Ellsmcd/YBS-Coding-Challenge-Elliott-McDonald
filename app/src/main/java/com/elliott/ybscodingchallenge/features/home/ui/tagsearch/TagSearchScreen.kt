package com.elliott.ybscodingchallenge.features.home.ui.tagsearch

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.elliott.ybscodingchallenge.features.home.ui.HomeEvent

@Composable
fun TagSearchScreen(
    onEvent: (HomeEvent) -> Unit = { },
) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Tags") },
        trailingIcon = {
            Button(
                onClick = {
                    onEvent(HomeEvent.TagAdded(tag = text))
            },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text("Add")
            }
        }
    )
}