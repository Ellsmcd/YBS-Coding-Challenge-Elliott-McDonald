package com.elliott.ybscodingchallenge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TagComponent(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.then(
        Modifier
            .border(width = 1.dp, Color.Black, shape = tagShape)
            .background(Color.White,shape = tagShape)
            .padding(start = 8.dp, end = 16.dp)
    ),
        ) {
        Text(text = text)
    }
}

private val tagShape = GenericShape {size, _ ->

    //Top left of rectangle
    moveTo(0f, 0f)

    //Move to top of triangle
    lineTo(size.width - 50, 0f)

    //Move to centre point of triangle
    lineTo(size.width, size.height * 0.5f)

    //Move to bottom of triangle
    lineTo(size.width - 50 , size.height)

    //Move to bottom left of rectangle
    lineTo(0f, size.height)

    //Move to centre left of rectangle
    lineTo(10f, size.height/2)


}

val triangleShape = GenericShape { size, _ ->

    // 1) Start at the top center
    moveTo(size.width / 2f, 0f)

    // 2) Draw a line to the bottom right corner
    lineTo(size.width, size.height)

    // 3) Draw a line to the bottom left corner and implicitly close the shape
    lineTo(0f, size.height)
}

@Preview
@Composable
fun TagComponentPreview() {
    TagComponent("Hello")
}
