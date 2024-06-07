package com.elliott.ybscodingchallenge.features.home.ui.tagsearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.elliott.ybscodingchallenge.R
import com.elliott.ybscodingchallenge.features.home.ui.HomeEvent
import com.elliott.ybscodingchallenge.ui.components.TagComponent

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
                    .testTag("usernameSearchText")
            )
        }
    }
}