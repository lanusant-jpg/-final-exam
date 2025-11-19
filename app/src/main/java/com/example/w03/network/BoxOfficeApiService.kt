package com.example.w03.network

import com.example.w03.data.BoxOfficeResponse
import com.example.w03.data.WeeklyBoxOfficeResponse // 1단계에서 만든 주간용 그릇 import
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit이 사용할 'API 명세' 인터페이스
interface BoxOfficeApiService {

    // 1. [일별] 박스오피스 요청 함수
    @GET("searchDailyBoxOfficeList.json")
    suspend fun getDailyBoxOffice(
        @Query("key") key: String,
        @Query("targetDt") targetDt: String,
        @Query("itemPerPage") itemPerPage: String = "10"
    ): Response<BoxOfficeResponse>

    // 2. [주간] 박스오피스 요청 함수 (여기가 추가된 부분!)
    @GET("searchWeeklyBoxOfficeList.json")
    suspend fun getWeeklyBoxOffice(
        @Query("key") key: String,
        @Query("targetDt") targetDt: String,
        @Query("weekGb") weekGb: String = "0" // 0은 주간(월~일)을 의미
    ): Response<WeeklyBoxOfficeResponse>
}