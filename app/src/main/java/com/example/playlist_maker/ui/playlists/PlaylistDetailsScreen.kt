package com.example.playlist_maker.ui.playlists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.playlist_maker.R
import com.example.playlist_maker.data.Playlist
import com.example.playlist_maker.data.network.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PlaylistDetailsScreen(
    playlistId: Long,
    playlistsViewModel: PlaylistsViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val playlists by playlistsViewModel.playlistsState.collectAsState()
    val playlist = playlists.find { it.id == playlistId }
    var trackToRemove by remember { mutableStateOf<Track?>(null) }
    val coroutineScope = rememberCoroutineScope()

    if (trackToRemove != null) {
        AlertDialog(
            onDismissRequest = { trackToRemove = null },
            title = { Text("Удалить трек из плейлиста?") },
            text = { Text("Вы уверены, что хотите удалить трек «${trackToRemove?.trackName}» из этого плейлиста?") },
            confirmButton = {
                Button(onClick = {
                    val track = trackToRemove
                    trackToRemove = null
                    coroutineScope.launch(Dispatchers.IO) {
                        track?.let { playlistsViewModel.deleteTrackFromPlaylist(it) }
                    }
                }) {
                    Text("Да")
                }
            },
            dismissButton = {
                Button(onClick = { trackToRemove = null }) {
                    Text("Нет")
                }
            }
        )
    }

    if (playlist == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Плейлист не найден")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        // стрелка назад
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // картинка плейлиста по центру
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.addpic),
                        contentDescription = "Обложка плейлиста",
                        modifier = Modifier
                            .size(280.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }

            // название плейлиста
            item {
                Text(
                    text = playlist.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // описание плейлиста
            if (playlist.description.isNotBlank()) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = playlist.description,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // количество треков
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${playlist.tracks.size} · треков",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // разделитель
            item {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // список треков
            items(
                items = playlist.tracks,
                key = { it.id }
            ) { track ->
                PlaylistTrackItem(
                    track = track,
                    onClick = { onTrackClick(track) },
                    onLongClick = { trackToRemove = track }
                )
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlaylistTrackItem(
    track: Track,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // обложка трека
        AsyncImage(
            model = track.image.ifEmpty { null },
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_music),
            error = painterResource(id = R.drawable.ic_music)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // информация о треке
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = track.trackName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${track.artistName} · ${track.trackTime}",
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}