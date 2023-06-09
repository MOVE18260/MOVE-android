# MOVE18260 - Google Maps Android API Example

## Memo

1. 하루 마다 step 이 갱신되는 기능
2. GPS tracking 구현
2. 홈 화면에 날씨 관련 추가
3. 데이터 바인딩 적용

- `MapFragment.java`에 사용되는 `LocationRequest`는 `deprecated` 됨
    - `LocationRequest().create()` 또한 `deprecated`
    - `LocationRequest.Builder()` 는 버전이 맞지 않음, 결국 `LocationRequest` 채택

### 프래그먼트 생명 주기

- 현재 프래그먼트(A) → 다른 프래그먼트(B)

  A 프래그먼트 시작 → A onAttach() → A onCreate() → A createView() → A onStart() → A onResume() → A 프래그먼트
  실행 → A onPause() → A onStop() → A onDestroyView() → onDestory() → onDetach() → A 프래그먼트 종료 → B
  프래그먼트 시작 → 생략

- 현재 프래그먼트(A) → 홈 OR 화면 OFF → 프로그램 OR 화면 ON

  A 프래그먼트 시작 → A onAttach() → A onCreate() → A createView() → A onStart() → A onResume() → A 프래그먼트
  실행 → A onPause() → A onStop() (분기점) → A onStart() OR A onDestoryView() → 생략

## 문제점

- 이동 거리 너무 크게 계산
- 평균 속도 계산 안됨
- Map -> START 버튼 -> END 버튼 -> 종료 시 오류 발생

## Reference

- init: project
    - [Android용 Maps SDK 빠른 시작](https://developers.google.com/maps/documentation/android-sdk/start?hl=ko)
    - [Google Maps Android API 사용 방법 및 예제](https://webnautes.tistory.com/647)
    - [Android - GitHub에 API Key, Hash 값 숨기기](https://pangseyoung.tistory.com/entry/Android-GitHub%EC%97%90-API-Key-Hash-%EA%B0%92-%EC%88%A8%EA%B8%B0%EA%B8%B0#:~:text=2.-,local.properties,%EA%B0%92%EB%93%B1%20%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%A5%BC%20%EC%84%A0%EC%96%B8%ED%95%B4%EC%A4%8D%EB%8B%88%EB%8B%A4.)
- 안드로이드 걸음수 센서 차이
    - [안드로이드 핸드폰 센서 비교 (TYPE_STEP_DETECTOR 와 TYPE_STEP_COUNTER)](https://copycoding.tistory.com/6)
    - [Android - TYPE_STEP_COUNTER, TYPE_STEP_DETECTOR](https://ddunnimlabs.tistory.com/145)
- 기타
    - [린트 검사로 코드 개선](https://developer.android.com/studio/write/lint?hl=ko)
- DataStore
    - [DataStore](https://developer.android.com/topic/libraries/architecture/datastore?hl=ko#java)
        - ! 코틀린 내부적으로 internal 사용해 자바에서 사용하지 못하고 있다.