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

sealed class MovieUiState {
    object Loading : MovieUiState()
    data class Success(val movies: List<MovieItem>) : MovieUiState()
    data class Error(val message: String) : MovieUiState()
}

class MainViewModel : ViewModel() {
    // 본인의 키로 꼭 확인하세요!
    private val KOBIS_API_KEY = "a003c99b85f6c40b8ea2930c135cb240"

    private val _uiState = MutableLiveData<MovieUiState>()
    val uiState: LiveData<MovieUiState> get() = _uiState

    // 어떤 모드인지 UI에 알려주기 위해 (true: 일별, false: 주간)
    private val _isDailyMode = MutableLiveData(true)
    val isDailyMode: LiveData<Boolean> get() = _isDailyMode

    init {
        fetchDailyBoxOffice() // 앱 켜지면 기본은 '일별'
    }

    // 1. 일별 박스오피스 가져오기
    fun fetchDailyBoxOffice() {
        _isDailyMode.value = true
        _uiState.value = MovieUiState.Loading
        viewModelScope.launch {
            try {
                val targetDt = getDateDaysAgo(1) // 어제 날짜
                val response = RetrofitClient.apiService.getDailyBoxOffice(KOBIS_API_KEY, targetDt)

                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = MovieUiState.Success(response.body()!!.boxOfficeResult.dailyBoxOfficeList)
                } else {
                    _uiState.value = MovieUiState.Error("일별 데이터 로드 실패")
                }
            } catch (e: Exception) {
                _uiState.value = MovieUiState.Error("에러: ${e.message}")
            }
        }
    }

    // 2. 주간 박스오피스 가져오기 (NEW!)
    fun fetchWeeklyBoxOffice() {
        _isDailyMode.value = false
        _uiState.value = MovieUiState.Loading
        viewModelScope.launch {
            try {
                // 주간 데이터는 보통 '지난주 일요일'을 기준으로 조회하는 게 안전합니다. (7일 전)
                val targetDt = getDateDaysAgo(7)
                val response = RetrofitClient.apiService.getWeeklyBoxOffice(KOBIS_API_KEY, targetDt)

                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = MovieUiState.Success(response.body()!!.boxOfficeResult.weeklyBoxOfficeList)
                } else {
                    _uiState.value = MovieUiState.Error("주간 데이터 로드 실패")
                }
            } catch (e: Exception) {
                _uiState.value = MovieUiState.Error("에러: ${e.message}")
            }
        }
    }

    // 날짜 계산 도우미 함수 (daysAgo일 전 날짜를 구함)
    private fun getDateDaysAgo(daysAgo: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -daysAgo)
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        return sdf.format(calendar.time)
    }
}