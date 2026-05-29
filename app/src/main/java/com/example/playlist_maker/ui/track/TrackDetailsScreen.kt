package com.example.playlist_maker.ui.track

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.playlist_maker.R
import com.example.playlist_maker.data.Playlist
import com.example.playlist_maker.data.network.Track
import com.example.playlist_maker.data.network.TracksRepositoryImpl
import com.example.playlist_maker.domain.TracksRepository
import com.example.playlist_maker.ui.playlists.PlaylistsViewModel

//
//@Composable
//fun TrackDetailsScreen(
//    trackId: Long,
//    onBackClick: () -> Unit,
//    tracksRepository: TracksRepository,
//    playlistsViewModel: PlaylistsViewModel
//) {
//    val viewModel = remember(trackId) {
//        TrackDetailsViewModel(trackId, tracksRepository)
//    }
//
//    val track by viewModel.trackState.collectAsState()
//
//    // загрузка
//    if (track == null) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//        return
//    }
//
//    TrackDetailsContent(
//        track = track!!,
//        onBackClick = onBackClick,
//        onFavoriteClick = { viewModel.toggleFavorite() },
//        onAddToPlaylistClick = {
//            // добавление в плейлист
//        }
//    )
//}
//
@Composable
fun TrackDetailsContent(
    track: Track,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onAddToPlaylistClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        //назад
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .size(32.dp)
                .clickable { onBackClick() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // обложка
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = track.image.ifEmpty { null },
                contentDescription = null,
                modifier = Modifier
                    .size(240.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_music),
                error = painterResource(R.drawable.ic_music)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // название
        Text(
            text = track.trackName,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        // автор
        Text(
            text = track.artistName,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        //кнопки
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(onClick = onAddToPlaylistClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (track.favorite)
                        Icons.Default.Favorite
                    else
                        Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        //длительность
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Длительность")
            Text(track.trackTime)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistBottomSheet(
    playlists: List<Playlist>,
    currentTrack: Track?,
    onDismiss: () -> Unit,
    onPlaylistSelected: (Long) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Добавить в плейлист",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = currentTrack?.trackName ?: "",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Divider(modifier = Modifier.padding(bottom = 16.dp))

            if (playlists.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_music), //надо добавить картинку плейлиста
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Нет доступных плейлистов",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Создайте плейлист чтобы добавить трек",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                ) {
                    items(
                        items = playlists,
                        key = { playlist -> playlist.id }
                    ) { playlist ->
                        PlaylistItem(
                            playlist = playlist,
                            onClick = { onPlaylistSelected(playlist.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistItem(
    playlist: Playlist,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_music), //надо добавить картинку плейлиста
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = playlist.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = playlist.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${playlist.tracks.size} ${getTracksCountText(playlist.tracks.size)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add to playlist",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun getTracksCountText(count: Int): String {
    return when {
        count % 10 == 1 && count % 100 != 11 -> "трек"
        count % 10 in 2..4 && count % 100 !in 12..14 -> "трека"
        else -> "треков"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    trackId: Long,
    onBackClick: () -> Unit,
    tracksRepository: TracksRepository,
    playlistsViewModel: PlaylistsViewModel
) {
    val viewModel = remember(trackId) {
        TrackDetailsViewModel(
            trackId,
            tracksRepository,
            playlistsViewModel.playlistsRepositoryPublic
        )
    }

    val track by viewModel.trackState.collectAsState()
    val playlists by viewModel.playlistsState.collectAsState()
    val showBottomSheet by viewModel.showBottomSheet.collectAsState()

    val context = LocalContext.current

    //бработка сообщений
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    if (track == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    TrackDetailsContent(
        track = track!!,
        onBackClick = onBackClick,
        onFavoriteClick = { viewModel.toggleFavorite() },
        onAddToPlaylistClick = { viewModel.showPlaylistSelector() }
    )

    if (showBottomSheet) {
        PlaylistBottomSheet(
            playlists = playlists,
            currentTrack = track,
            onDismiss = { viewModel.hidePlaylistSelector() },
            onPlaylistSelected = { playlistId ->
                viewModel.addToPlaylist(playlistId)
            }
        )
    }
}