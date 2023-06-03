# MOVE18260 - Google Maps Android API Example

## Memo

1. PedometerService 클래스와 SensorEventListener 구현체 분리 필요
2. LocationService 클래스와 PedometerService 클래스의 데이터 전달 방식 문제

    - 시간에 따라 값을 변경하는 것보다 더 개선된 방법이 필요

3. 향후 늘어날 기능들을 위해 패키기 구조 개선 필요

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