package com.example.playlist_maker

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun FloatButtonExample(modifier: Modifier, onClick: () -> Unit) {
    Box (modifier = modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .padding(32.dp)
                .align(Alignment.BottomEnd),
            onClick = { onClick() },
            contentColor = Color.White,
            containerColor = Color.Gray,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "This is FloatingActionButton",

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FloatButtonExamplePreview(){
    FloatButtonExample(modifier = Modifier) {}
}