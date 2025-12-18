package com.example.playlist_maker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Preview(
    device = Devices.DEFAULT,
    showSystemUi = true,
    showBackground = true
)
@Composable
fun SearchView(navController: NavHostController? = null){

    val maintext = TextStyle(
        color = Color.Black,
        fontSize = 21.sp,
        fontWeight = FontWeight.Medium
    )
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White))
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp),
            verticalAlignment = Alignment.CenterVertically)
        {
            Image(painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = null,
                modifier = Modifier.padding(16.dp).
                clickable(onClick = {navController?.popBackStack()})
            )
            Text(text = stringResource(R.string.search), style = maintext,
                modifier = Modifier.padding(
                    start = 12.dp, top = 10.dp, bottom = 12.dp)
            )
        }
        SearchTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp).
                padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 2.dp),
            textstyle = TextStyle(
                color = Color.Gray,
                fontSize = 16.sp,
            ))
    }
}


@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    textstyle: TextStyle = TextStyle()
) {
    var textField by rememberSaveable { mutableStateOf("") }
    TextField(
        value = textField,
        textStyle = textstyle,
        onValueChange = {textField = it},
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),

        placeholder = {
            Text(text = stringResource(R.string.search), style = textstyle)
        },
        leadingIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Найти"
                )
            }
        },
        trailingIcon = {
            if (textField.isNotEmpty()) {
                IconButton(onClick = {textField=""}) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Очистить"
                    )
                }
            }
        },
    )
}