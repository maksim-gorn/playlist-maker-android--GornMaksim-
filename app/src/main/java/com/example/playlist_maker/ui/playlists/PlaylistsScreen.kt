package com.example.playlist_maker.ui.playlists

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.playlist_maker.R
import com.example.playlist_maker.data.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistListItem(playlist: Playlist, onClick: () -> Unit, onLongClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.size(56.dp),
            painter = painterResource(id = R.drawable.ic_music),
            contentDescription = playlist.name,
            colorFilter = ColorFilter.tint(Color.Gray)
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(playlist.name, fontSize = 20.sp)
            val text = "${playlist.tracks.size} треков"
            Text(text, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun PlaylistsView(
    modifier: Modifier,
    playlistsViewModel: PlaylistsViewModel,
    addNewPlaylist: () -> Unit,
    onPlaylistClick: (Long) -> Unit,
    navController: NavHostController? = null
) {
    val playlists by playlistsViewModel.playlistsState.collectAsState()
    var playlistToRemove by remember { mutableStateOf<Playlist?>(null) }
    val coroutineScope = rememberCoroutineScope()

    if (playlistToRemove != null) {
        AlertDialog(
            onDismissRequest = { playlistToRemove = null },
            title = { Text("Удалить плейлист?") },
            text = {
                Text("Вы уверены, что хотите удалить плейлист «${playlistToRemove?.name}»? Все треки в нём будут отвязаны, но не удалены из медиатеки.")
            },
            confirmButton = {
                Button(onClick = {
                    val playlist = playlistToRemove
                    playlistToRemove = null
                    coroutineScope.launch(Dispatchers.IO) {
                        playlist?.let { playlistsViewModel.deletePlaylistById(it.id) }
                    }
                }) {
                    Text("Да")
                }
            },
            dismissButton = {
                Button(onClick = { playlistToRemove = null }) {
                    Text("Нет")
                }
            }
        )
    }

    //логирование, искал ошибку, может потом уберу
    LaunchedEffect(playlists) {
        Log.d("PlaylistsView", "Playlists updated, count: ${playlists.size}")
        playlists.forEach { playlist ->
            Log.d("PlaylistsView", "Playlist: ${playlist.name}, tracks: ${playlist.tracks.size}")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { navController?.popBackStack() },
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.arrow_back)
                )
                Text(
                    stringResource(R.string.playlists),
                    fontSize = 30.sp,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 8.dp, end = 8.dp),
            ) {
                LazyColumn(modifier = modifier.fillMaxSize()) {
                    items(
                        items = playlists,
                        key = { playlist -> playlist.id }
                    ) { playlist ->
                        PlaylistListItem(
                            playlist = playlist,
                            onClick = { onPlaylistClick(playlist.id) },
                            onLongClick = { playlistToRemove = playlist }
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(32.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                addNewPlaylist()
            },
            containerColor = Color.Gray,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.add_playlist)
            )
        }
    }
}