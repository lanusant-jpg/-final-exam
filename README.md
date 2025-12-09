# 🎬 Cinema Now (씨네마 나우)
> **KOBIS API 기반 안드로이드 실시간 영화 박스오피스 조회 앱**

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple?style=flat&logo=kotlin)
![Android Studio](https://img.shields.io/badge/Android%20Studio-Koala-green?style=flat&logo=android-studio)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-M3-4285F4?style=flat&logo=jetpack-compose)

## 📖 프로젝트 소개
**Cinema Now**는 영화진흥위원회(KOBIS) 오픈 API를 활용하여 사용자가 가장 빠르고 직관적으로 영화 트렌드를 파악할 수 있도록 돕는 안드로이드 애플리케이션입니다.
복잡한 영화 정보를 단순화하여 **순위 중심의 UI**로 제공하며, 부족한 정보는 **포털 검색 연동**으로, 재미있는 정보는 **지인 공유**로 연결하여 사용자 경험(UX)을 극대화했습니다.

---

## 📱 실행 화면
| 메인 랭킹 (일별/주간) | 상세 정보 (네이버 검색) | 친구에게 공유하기 |
|:---:|:---:|:---:|
| ![메인화면](https://via.placeholder.com/200x400?text=Main+Screen) | ![상세화면](https://via.placeholder.com/200x400?text=Search+Screen) | ![공유화면](https://via.placeholder.com/200x400?text=Share+Screen) |
*(위 공간에 실행 스크린샷이나 GIF를 넣어주세요)*

---

## ✨ 주요 기능

* **📅 일별/주간 랭킹 조회:** 토글 버튼 하나로 어제 날짜 기준 일별 순위와 지난주 주간 순위를 즉시 전환하여 조회할 수 있습니다.
* **🏆 직관적인 순위 시각화:** 1~3위 영화에는 금/은/동 색상을 부여하고, 카드 UI를 적용하여 정보 가독성을 높였습니다.
* **📊 순위 변동 트래킹:** 전일 대비 순위 상승(🔺), 하락(🔻), 신규 진입(🆕) 정보를 아이콘으로 표시하여 차트의 역동성을 시각화했습니다.
* **🔍 포털 검색 연동:** API에서 제공하지 않는 포스터나 줄거리 정보는 '상세' 버튼 클릭 시 네이버 검색 결과로 즉시 연결하여 해결했습니다.
* **📤 SNS 공유 기능:** 안드로이드 Intent를 활용하여 영화 제목, 순위, 관객 수 정보를 카카오톡이나 문자 메시지로 손쉽게 공유할 수 있습니다.

---

## 🛠 사용 기술 (Tech Stack)

### 🔹 Language & Framework
* **Kotlin:** 100% 코틀린 언어 사용
* **Jetpack Compose:** 최신 선언형 UI 툴킷 (Material Design 3 적용)

### 🔹 Architecture & Design Pattern
* **MVVM (Model-View-ViewModel):** UI와 비즈니스 로직을 분리하여 유지보수성 향상
* **LiveData:** 데이터 변경 사항을 UI에 실시간으로 반영

### 🔹 Network & Async
* **Retrofit2:** REST API 통신 구현
* **Gson:** JSON 데이터 파싱
* **Coroutines:** 비동기 네트워크 처리 (메인 스레드 블로킹 방지)

### 🔹 Open API
* **KOBIS (영화진흥위원회):** 일별/주간 박스오피스 데이터 호출

---

## 🚀 개발 과정 (Development Process)

본 프로젝트는 다음과 같은 단계로 개발되었습니다.

### Step 1. 프로젝트 설계 및 UI 구축
* **목표:** Jetpack Compose를 활용한 모던 UI 구현
* **내용:** `LazyColumn`을 사용하여 리스트 뷰를 최적화하고, `Card` 컴포넌트로 개별 영화 아이템을 디자인했습니다. 상단에는 일별/주간 전환을 위한 커스텀 버튼을 배치했습니다.

### Step 2. 데이터 모델링 및 네트워크 설정
* **목표:** Retrofit을 이용한 API 통신 환경 구축
* **내용:** KOBIS API 명세서를 분석하여 `BoxOfficeResponse`, `MovieItem` 데이터 클래스를 설계했습니다. Gson Converter를 연결하여 JSON 데이터를 객체로 자동 변환하도록 설정했습니다.

### Step 3. 비즈니스 로직 구현 (MVVM)
* **목표:** ViewModel을 통한 상태 관리
* **내용:** `MainViewModel`을 생성하여 API 호출 로직을 담당하게 했습니다. `LiveData`를 통해 로딩 상태(Loading), 성공(Success), 실패(Error) 상태를 관리하여 UI가 데이터 변화에 반응하도록 만들었습니다.

### Step 4. 기능 고도화 및 확장
* **목표:** 사용자 편의 기능 추가 (Intent 활용)
* **내용:**
    * **검색:** `Intent.ACTION_VIEW`를 사용하여 웹 브라우저 연동.
    * **공유:** `Intent.ACTION_SEND`를 사용하여 텍스트 공유 시트 호출.
    * **디테일:** 순위 변동폭(rankInten) 계산 로직과 신규 진입(rankOldAndNew) 배지 표시 로직 추가.

---

## 📂 폴더 구조 (Project Structure)
