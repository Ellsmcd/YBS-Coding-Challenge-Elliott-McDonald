package com.elliott.ybscodingchallenge.features.home.ui.tagsearch

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.elliott.ybscodingchallenge.features.home.ui.HomeEvent
import kotlinx.coroutines.launch

@Composable
fun TagSearchScreen(
    searchTerm: String,
    searchUsername: Boolean,
    onEvent: (HomeEvent) -> Unit = { },
) {
    TextField(
        value = searchTerm,
        onValueChange = { onEvent(HomeEvent.TextChanged(it)) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Tags") },
        trailingIcon = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = searchUsername,
                    onCheckedChange = {
                        onEvent(HomeEvent.SearchTypeChanged(it))
                    }
                )

                Button(
                    onClick = {
                        if (searchUsername) {
                            onEvent(HomeEvent.UserIdAdded(searchTerm))
                        } else {
                            onEvent(HomeEvent.TagAdded(tag = searchTerm))
                        }
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("Add")
                }
            }

        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchTypePager(
    state: PagerState,
    onEvent: (HomeEvent) -> Unit = { },
) {
    val coroutineScope = rememberCoroutineScope()
    val options = listOf("Tags", "Username")
    VerticalPager(
        state = state,
        modifier = Modifier
            .height(50.dp)
            .width(100.dp)) { page ->
        Text(
            text = options[page],
            textAlign = TextAlign.Right,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    coroutineScope.launch {
                        state.scrollToPage(if (page == 0) 1 else 0)
                    }
                }
        )
    }
}
