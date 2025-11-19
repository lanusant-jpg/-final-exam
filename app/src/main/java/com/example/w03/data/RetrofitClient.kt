package com.example.w03.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 앱 전체에서 API 통신을 담당할 '공장' (싱글톤 Object)
object RetrofitClient {

    // KOBIS API의 기본 URL (이 주소 뒤에 인터페이스에서 정의한 @GET 주소가 붙습니다)
    private const val BASE_URL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/"

    // 1. Retrofit '본체' 생성
    private val retrofit: Retrofit by lazy { // lazy: 처음 사용할 때 1번만 초기화
        Retrofit.Builder()
            .baseUrl(BASE_URL) // 1-1. 기본 URL 설정
            .addConverterFactory(GsonConverterFactory.create()) // 1-2. JSON을 Gson으로 자동 변환
            .build()
    }

    // 2. 우리가 만든 'BoxOfficeApiService' 인터페이스를 '실제 객체'로 구현
    val apiService: BoxOfficeApiService by lazy {
        retrofit.create(BoxOfficeApiService::class.java)
    }
}