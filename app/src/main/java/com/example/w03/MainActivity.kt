package com.example.w03

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.w03.data.MovieItem
import com.example.w03.ui.theme.W03Theme
import com.example.w03.viewmodel.MainViewModel
import com.example.w03.viewmodel.MovieUiState

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            W03Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MovieScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.observeAsState(MovieUiState.Loading)
    val isDaily by viewModel.isDailyMode.observeAsState(true) // 현재 모드 확인

    Column(modifier = Modifier.fillMaxSize()) {
        // [상단] 타이틀 및 전환 버튼
        TopSection(isDaily = isDaily, viewModel = viewModel)

        // [하단] 리스트 영역
        Box(modifier = Modifier.weight(1f)) {
            when (state) {
                is MovieUiState.Loading -> LoadingScreen()
                is MovieUiState.Success -> MovieListScreen(movies = (state as MovieUiState.Success).movies)
                is MovieUiState.Error -> ErrorScreen(message = (state as MovieUiState.Error).message)
            }
        }
    }
}

// ✨ [추가된 상단 영역] 제목과 버튼 2개
@Composable
fun TopSection(isDaily: Boolean, viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer) // 배경색 살짝 넣기
            .padding(16.dp)
    ) {
        Text(
            text = if (isDaily) "📅 일별 박스오피스" else "🏆 주간 박스오피스",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 버튼 2개를 가로로 배치
        Row(modifier = Modifier.fillMaxWidth()) {
            // [일별] 버튼
            Button(
                onClick = { viewModel.fetchDailyBoxOffice() },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp), // 왼쪽 둥글게
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDaily) MaterialTheme.colorScheme.primary else Color.LightGray
                )
            ) {
                Text("일별 랭킹")
            }

            // [주간] 버튼
            Button(
                onClick = { viewModel.fetchWeeklyBoxOffice() },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp), // 오른쪽 둥글게
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isDaily) MaterialTheme.colorScheme.primary else Color.LightGray
                )
            ) {
                Text("주간 랭킹")
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun MovieListScreen(movies: List<MovieItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(movies) { movie ->
            MovieItemRow(movie = movie)
        }
    }
}

@Composable
fun MovieItemRow(movie: MovieItem) {
    val context = LocalContext.current

    val onDetailClick = {
        val query = movie.movieName
        val url = "https://search.naver.com/search.naver?query=$query"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    val onShareClick = {
        val shareText = "야 이 영화 요즘 뜬대! 🎬\n\n[${movie.rank}위] ${movie.movieName}\n(관객수: ${movie.audienceCount}명)"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "친구에게 공유하기")
        context.startActivity(shareIntent)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // [1] 순위
            val rankColor = when (movie.rank) {
                "1" -> Color(0xFFFFD700)
                "2" -> Color(0xFFC0C0C0)
                "3" -> Color(0xFFCD7F32)
                else -> Color.Gray
            }

            Text(
                text = movie.rank,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic,
                color = rankColor,
                modifier = Modifier.width(40.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // [2] 영화 정보
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.movieName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = movie.openDate, style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFFF9800), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${movie.audienceCount}명", style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
                }
            }

            // [3] 버튼 영역
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onShareClick) {
                    Icon(Icons.Default.Share, contentDescription = "공유", tint = Color.Gray)
                }
                FilledTonalButton(
                    onClick = onDetailClick,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(text = "상세", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(Icons.Default.KeyboardArrowRight, null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    W03Theme {
        val fakeMovies = listOf(
            MovieItem("1", "범죄도시4", "2024-04-24", "1000000"),
            MovieItem("2", "쿵푸팬더4", "2024-04-10", "50000")
        )
        MovieListScreen(movies = fakeMovies)
    }
}