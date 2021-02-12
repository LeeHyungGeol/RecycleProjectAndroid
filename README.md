## 우리의 품애(딥러닝 기반 생활폐기물 인식 앱)

### 프로젝트 소개
|구분|내용|
|------|---|
|한줄 소개|생활폐기물 처리를 손쉽게 도와주는 딥러닝 기반 생활폐기물 인식 서비스|
|진행 기간|2020.03 ~ 2020.06|
|주요 기술| Android, Django, Yolov3(딥러닝), OpenCV, MySQL |
|팀원 구성|4명 (Anroid 개발 및 딥러닝 모델 구현 2명, Server 개발 2명)|
|전담 역할|Android 개발 및 딥러닝 모델 구현|
|수상|교내 창의설계경진대회 장려상 및 인기상 수상|

### 프로젝트 개요

- 우리의 품애(愛)는 내가 버린 폐기물이 누군가의 품으로 돌아갈 수 있다는 의미입니다.
- 캡스톤 과목으로 진행한 딥러닝 기반 생활폐기물 인식 모바일 서비스입니다.
- 폐기물에 따라 복잡하고 어려운 배출방법을 해소하고자 객체인식 기술을 이용하여 품목을 분류하고 이에 따른 배출방법을 소개하고자 하였습니다.
- Yolov3 모델의 경우 총 58가지의 폐기물에 대한 분류가 가능하며 객체인식 모델 정확성 평가의 경우 TOP-1 정확도 88.24%, TOP-5 정확도 90.63%를 보여주었습니다.
- 대형폐기물의 경우 길이에 따라 수수료가 다른데 길이를 자동으로 측정해주고 이에 따른 수수료를 알려준다면 문제점이 해결될것같아 해당 부분을 OpenCV를 이용하여 구현하였습니다.
- 길이 측정 오차는 해당 품목의 길이를 기준으로 ± 10% 정도입니다.


### 프로젝트 사용 기술 및 라이브러리

### ✔ Languauge

- Java, Python

### ✔ Server

- Django

### ✔ Client

- Android

### ✔ 협업

- GitHub

### ✔ Deep-Learning

- Yolov3 (Tensorflow)

### ✔ Data Base

- MySQL

### ✔ Library

- OpenCV

### 주요 기능

- 객체인식 딥러닝 기술을 이용하여 폐기물 품목을 확인할 수 있습니다.
- 음성 또는 텍스트로 폐기물의 배출 요령을 검색할 수 있습니다.
- OpenCV의 마커를 이용하여 대형 폐기물 길이 측정이 가능합니다.
- 대형 폐기물 나눔 커뮤니티를 이용할 수 있습니다.
- 사용자의 지역에 따른 폐기물 배출 관련 정보를 확인할 수 있습니다.
- 객체인식 딥러닝 기술을 이용하여 폐기물의 분리상태에 따라 포인트 제공합니다.

### 나의 역할

- Android 구현
- 딥러닝 모델 구현을 위한 데이터 수집 및 정제
- 객체인식 딥러닝(Yolov3) 모델 구현

### 시연 영상

[https://www.youtube.com/watch?v=Xwgt8fvhNH8](https://www.youtube.com/watch?v=Xwgt8fvhNH8)

### [🛠 실행화면 및 자세한 설명]

[노션 문서](https://www.notion.so/Android-cdbae277436441cea1532097b38a89c4)
