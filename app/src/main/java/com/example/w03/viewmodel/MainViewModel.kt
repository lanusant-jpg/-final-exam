package com.example.w03.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.w03.data.MovieItem
import com.example.w03.network.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * 1. UI가 가질 수 있는 3가지 상태 (로딩중, 성공, 실패)
 * Sealed Class를 사용하면 UI에서 when(상태) { ... } 로 처리하기 편리합니다.
 */
sealed class MovieUiState {
    object Loading : MovieUiState() // 로딩중
    data class Success(val movies: List<MovieItem>) : MovieUiState() // 성공 (영화 목록을 가짐)
    data class Error(val message: String) : MovieUiState() // 실패 (에러 메시지를 가짐)
}

/**
 * 2. 메인 화면의 '두뇌' 역할을 하는 ViewModel
 */
class MainViewModel : ViewModel() {
    private val KOBIS_API_KEY = "a003c99b85f6c40b8ea2930c135cb240" // "YOUR_API_KEY" 대신 실제 키를 입력

    // 3. UI 상태를 관리하는 LiveData
    //  - _uiState: ViewModel 내부에서만 값을 변경 (Mutable)
    //  -  uiState: UI(Activity)에서는 이 값을 읽기만 함 (Read-only)
    private val _uiState = MutableLiveData<MovieUiState>()
    val uiState: LiveData<MovieUiState> get() = _uiState

    // 4. ViewModel이 생성될 때(init) 딱 한 번 API를 호출합니다.
    init {
        fetchDailyBoxOffice()
    }

    // 5. API를 호출하는 메인 함수
    private fun fetchDailyBoxOffice() {
        // (1) API 호출 직전, UI 상태를 '로딩중'으로 변경
        _uiState.value = MovieUiState.Loading

        // (2) 코루틴(viewModelScope)을 사용해 비동기로 API 호출
        viewModelScope.launch {
            try {
                // (3) 어제 날짜를 계산 (박스오피스는 당일 데이터가 집계 안 될 수 있으므로)
                val targetDt = getYesterdayDate() // 예: "20251111"

                // (4) 4단계에서 만든 RetrofitClient로 API 호출
                val response = RetrofitClient.apiService.getDailyBoxOffice(
                    key = KOBIS_API_KEY,
                    targetDt = targetDt
                    // itemPerPage는 4단계 인터페이스에서 "10"으로 고정해둠
                )

                // (5) API 호출 성공 시
                if (response.isSuccessful && response.body() != null) {
                    val movies = response.body()!!.boxOfficeResult.dailyBoxOfficeList
                    // UI 상태를 '성공'으로 바꾸고 영화 목록(movies)을 전달
                    _uiState.value = MovieUiState.Success(movies)
                } else {
                    // (6) API 호출은 했으나 서버가 에러를 보낸 경우 (예: 키가 틀림)
                    _uiState.value = MovieUiState.Error("데이터를 가져오는데 실패했습니다: ${response.message()}")
                }
            } catch (e: Exception) {
                // (7) 네트워크 연결 실패 등 예외(Exception) 발생 시
                _uiState.value = MovieUiState.Error("오류가 발생했습니다: ${e.message}")
            }
        }
    }

    // (8) 어제 날짜를 "YYYYMMDD" 형식으로 반환하는 도우미 함수
    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1) // 오늘 날짜에서 하루를 뺌
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        return sdf.format(calendar.time)
    }
}