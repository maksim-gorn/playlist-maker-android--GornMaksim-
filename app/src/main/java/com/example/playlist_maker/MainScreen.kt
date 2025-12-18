package com.example.playlist_maker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun MainMenuButton(
    buttontext: Int,
    icon: Int,
    onClick: () -> Unit
) {
    val maintext = TextStyle(
        color = Color.Black,
        fontSize = 21.sp,
        fontWeight = FontWeight.Medium
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(66.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        ),
        shape = RectangleShape
    ) {

        Row(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null
            )

            Text(
                text = stringResource(id = buttontext),
                style = maintext,
                modifier = Modifier.padding(start = 6.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.arrow_forward),
                contentDescription = null
            )
        }
    }
}

@Preview(device = Devices.DEFAULT)
@Composable
fun MainView(navController: NavHostController? = null)
{
    val myBlue = Color(red = 55, green = 114, blue = 231)
    Column (modifier = Modifier
        .background(myBlue)
        .fillMaxSize())
    {
        //Загаловок Playlist maker
        Text("Playlist maker",
            style = TextStyle
                (
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium

            ),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.0575f)
        )
        //белый бокс скругленный сверху
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                Color.White,
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
        ){
            //колонка с разными кнопками и их значками и текстом
            Column(modifier = Modifier.padding(top = 8.dp))
            {
                MainMenuButton(R.string.search, R.drawable.search_icon,
                    onClick = { navController?.navigate("search")})
                MainMenuButton(R.string.playlists, R.drawable.melody_icon,
                    onClick = {print("Нажали Плейлисты")})
                MainMenuButton(R.string.favorite, R.drawable.favorite_icon,
                    onClick = {print("Нажали Избранное")})
                MainMenuButton(R.string.settings, R.drawable.settings_icon,
                    onClick = { navController?.navigate("settings")})
            }
        }

    }
}
