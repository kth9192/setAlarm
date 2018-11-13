SetAlarm Project
================
<!-- 예제) ![animation gif](https://user-images.githubusercontent.com/7121217/48350868-cc970900-e6cb-11e8-924a-0cc94a0a8587.gif)\{:height="100px" width="100px"} -->

예제)

<img src ="https://user-images.githubusercontent.com/7121217/48350868-cc970900-e6cb-11e8-924a-0cc94a0a8587.gif" width="30%">

###제원

-	언어 : android
-	db : androd Room
-	glide, glide-transformations
-	andorid material
-	gson library

###ViewModel

-	정의 : ui 컨트롤러와 repository를 연결. 데이터에 대한 부분을 추상화 함으로써 객체지향적 프로그래밍을 유도함. 안드로이드는 Lifecycle 에 관한 기능도 포함해 안정적인 프로그래밍 지원.

-	LiveData: 관찰 할 수있는 데이터 홀더 클래스 . 최신 버전의 데이터를 항상 보유 / 캐시. 데이터가 변경되면 옵서버에게 알린다. LiveData는 관측하는 동안 관련 라이프 사이클 상태가 변경되었음을 알고 있기 때문에 이 모든 것을 자동으로 관리한다. UI 구성 요소는 관련 데이터를 관찰하고 관찰을 중지하거나 다시 시작하지 않습니다.

-	repository : 데이터엑세스 오브젝트(DAO) 를 통한 데이터 제공자.

-	구성 : 사용자 - UI - Viewmodel - livedata - repository(room + DAO)

### DB 구성

1.	ClockModel : 한개의 알람세트의 데이터를 담당하는 모델.
2.	AlarmModel : ClockModel의 묶음. 알람세트의 전체뷰를 보여주는 모델.
