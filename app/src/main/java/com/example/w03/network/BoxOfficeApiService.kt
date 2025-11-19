package com.example.w03.network

import com.example.w03.data.BoxOfficeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit이 사용할 'API 명세' 인터페이스입니다.
interface BoxOfficeApiService {

    // "kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/" 뒤에 붙을 주소
    @GET("searchDailyBoxOfficeList.json")
    suspend fun getDailyBoxOffice(
        // @Query("key")는 요청 URL에 "?key=xxxx" 형식으로 파라미터를 추가합니다.
        @Query("key") key: String,
        @Query("targetDt") targetDt: String,
        @Query("itemPerPage") itemPerPage: String = "10" // 10개만 가져오도록 고정
    ): Response<BoxOfficeResponse> // 3단계에서 만든 '그릇'으로 응답을 받습니다.
}