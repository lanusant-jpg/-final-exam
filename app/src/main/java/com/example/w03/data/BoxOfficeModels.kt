package com.example.w03.data // 1단계에서 만든 패키지 이름입니다.

import com.google.gson.annotations.SerializedName

/**
 * 이 파일은 KOBIS API 응답을 담기 위한 '그릇' (Data Class)들을 정의합니다.
 */

// 1. 최종 응답 형태 (가장 바깥쪽 껍질)
// API 응답: { "boxOfficeResult": { ... } }
data class BoxOfficeResponse(
    // JSON의 "boxOfficeResult" 키와 변수 이름이 같으므로 자동으로 매핑됩니다.
    val boxOfficeResult: BoxOfficeResult
)

// 2. 박스오피스 결과 객체
// { "boxofficeType": "...", "showRange": "...", "dailyBoxOfficeList": [ ... ] }
data class BoxOfficeResult(
    val boxofficeType: String,
    val showRange: String,

    // JSON 키 "dailyBoxOfficeList"를 코틀린 변수 "dailyBoxOfficeList"에 매핑합니다.
    @SerializedName("dailyBoxOfficeList")
    val dailyBoxOfficeList: List<MovieItem>
)

// 3. 개별 영화 정보 (우리가 실제로 리스트에 보여줄 데이터)
// [ { "rank": "1", "movieNm": "...", "openDt": "...", "audiCnt": "..." }, { ... } ]
data class MovieItem(
    val rank: String, // 순위

    // JSON 키 "movieNm"을 코틀린 변수 "movieName"에 매핑합니다.
    @SerializedName("movieNm")
    val movieName: String, // 영화 제목

    // JSON 키 "openDt"를 코틀린 변수 "openDate"에 매핑합니다.
    @SerializedName("openDt")
    val openDate: String, // 개봉일

    // JSON 키 "audiCnt"를 코틀린 변수 "audienceCount"에 매핑합니다.
    @SerializedName("audiCnt")
    val audienceCount: String // 당일 관객 수
)
// [추가] 주간 박스오피스 응답용 그릇
data class WeeklyBoxOfficeResponse(
    val boxOfficeResult: WeeklyBoxOfficeResult
)

data class WeeklyBoxOfficeResult(
    val boxofficeType: String,
    val showRange: String,

    // 여기가 'weeklyBoxOfficeList'로 바뀝니다.
    @SerializedName("weeklyBoxOfficeList")
    val weeklyBoxOfficeList: List<MovieItem>
)
