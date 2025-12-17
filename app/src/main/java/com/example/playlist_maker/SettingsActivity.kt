package com.example.playlist_maker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlist_maker.ui.theme.Playlist_makerTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SettingsMain()
        }
    }
}

@Preview(showBackground = true, device = Devices.DEFAULT, showSystemUi = true)
@Composable
fun SettingsPreview() {
    Playlist_makerTheme {
        SettingsMain()
    }
}
@Composable
fun SettingsMain()
{
    val context = LocalContext.current

    val email = stringResource(R.string.developer_email)
    val subject = stringResource(R.string.developer_email_subject)
    val body = stringResource(R.string.developer_email_body)

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
                modifier = Modifier.padding(16.dp))
            Text(stringResource(R.string.settings), style = maintext, modifier = Modifier.padding(
                start = 12.dp, top = 10.dp, bottom = 12.dp)
            )
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(24.dp))
        ActionRow(
            textRes = R.string.dark_theme,
            iconRes = R.drawable.switch_off,
            onClick = {}
        )
        ActionRow(
            textRes = R.string.share_app,
            iconRes = R.drawable.share_gray,
            onClick = {}
        )
        ActionRow(
            textRes = R.string.write_to_support,
            iconRes = R.drawable.support_gray,
            onClick = {sendThroughMail(context, email, subject, body)}
        )
        ActionRow(
            textRes = R.string.user_agreement,
            iconRes = R.drawable.arrow_forward_gray,
            onClick = {}
        )

    }
}

@Composable
fun ActionRow(
    textRes: Int,
    iconRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val textStyle = TextStyle(
        fontSize = 16.sp,
        color = Color.Black
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(61.dp)
            .padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 6.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = textRes),
            style = textStyle
        )

        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Gray
        )
    }
}
fun sendThroughMail(
    context: Context,
    email: String,
    subject: String,
    body: String
) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
    context.startActivity(intent)
}
