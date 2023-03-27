# Android용 FoodLens2 SDK 메뉴얼

Android용 FoodLens2 SDK를 사용하여 FoodLens2 기능을 이용할 수 있습니다. 

## [ReleaseNote 바로가기](ReleaseNote.md)

## 1. 안드로이드 프로젝트 설정

### 1.1 Android 12 지원
Android 12 지원을 위해 Compile SDK Version을 31이상으로 설정해 주세요. 프로젝트에서 app > Gradle Scripts(그래들 스크립트) > build.gradle (Module: app)을 연 후 android{} 섹션에 아래와 같은 문구를 추가해 주세요.

```java
android {
        ....
        compileSdkVersion 31
	....       
    }
```

### 1.2 gradle 설정
#### 1.2.1 minSdkVersion 설정
- minSdkVersion은 21 이상을 사용하시기 바랍니다. 프로젝트에서 app > Gradle Scripts(그래들 스크립트) > build.gradle (Module: app)을 연 후 defaultConfig{} 섹션에 아래와 같은 문구를 추가해 주세요.
```java
   defaultConfig {
        ....
        minSdkVersion 23
	....       
    }
```
#### 1.2.2 gradle dependencies 설정
- 프로젝트에서 app > Gradle Scripts(그래들 스크립트) > build.gradle (Module: app)을 연 후 dependencies{} 섹션에 아래와 같은 문구를 추가해 주세요.
```java
   implementation "com.doinglab.foodlens:FoodLens2:1.0.0"
```

## 2. 리소스(Resources) 및 메니페스트(Manifests) 
Company, AppToken을 세팅 합니다.

### 2.1 AppToken, CompanyToken 설정
발급된 AppToken, CompanyToken을 /app/res/values/strings.xml에 추가 합니다.
```xml
<string name="foodlens_app_token">[AppToken]</string>
<string name="foodlens_company_token">[CompanyToken]</string>
```

* Meta data추가
아래와 같이 메타데이터를 Manifest.xml에 추가해 주세요
```xml
<meta-data android:name="com.doinglab.foodlens.sdk.apptoken" android:value="@string/foodlens_app_token"/> 
<meta-data android:name="com.doinglab.foodlens.sdk.companytoken" android:value="@string/foodlens_company_token"/> 
```

### 2.2 공통
* ProGuard 설정
앱에서 proguard를 통한 난독화를 설정할 경우 아래와 같이 proguard 설정을 설정 파일에 추가해 주세요
```xml
-keep public class com.doinglab.foodlens2.sdk.** {
       *;
}
```

## 3.독립 FoodLens 서버 주소 설정
 - Meta data추가 
   아래와 같이 메타데이터를 Manifest.xml에 추가해 주세요
```xml
//프로토콜과 및 포트를 제외한 순수 도메인 주소 혹은 IP주소 e.g) www.foodlens.com, 123.222.100.10
<meta-data android:name="com.doinglab.foodlens.sdk.serveraddr" android:value="[server_address]"/> 
```  

## 4. SDK 사용법 사용법
FoodLens API는 FoodLens 기능을 이미지 파일기반으로 동작하게 하는 기능입니다.

### 4.1 음식 결과 영양정보 얻기
1. FoodlensService 를 생성합니다.
2. predict 메서드를 호출합니다.
파라미터는 Jpeg image, userId(생략가능), RecognizeResultHandler 입니다.
Jpeg이미지는 카메라 촬영 또는 갤러리 원본 이미지를 전달해 줍니다. userId는 없을 경우 생략 가능합니다.</br>
※ 이미지가 작은경우 인식율이 낮아질 수 있습니다.  
3. 코드 예제
```java
//Create FoodLens Service
private val foodLensService by lazy {
    FoodLens.createFoodLensService(this@MainActivity)
}

//Call prediction method.
//foodLensService.predict(byteData, "input_userid", object : RecognitionResultHandler { //userId가 있는 경우
foodLensService.predict(byteData, object : RecognitionResultHandler { //userId가 없는 경우 
  override fun onSuccess(result: RecognitionResult?) {
    result?.let {
      it.foodInfoList.forEach {
        Log.d("FoodLens", "${it.name}")
      }
    }
  }

  override fun onError(errorReason: BaseError?) {
    Log.d("FoodLens", "errorReason?.getMessage()")
  }
})
```

### 4.2 옵션 변경
#### 4.2.1 이미지 회전 기능 지원 여부
```
//true인 경우 회전이 적용된 좌표값이 반환됩니다.
//Default는 false 입니다
foodLensService.setAutoRotate(false)
```
#### 4.2.2 언어 설정
```
//ConfigLocale.ENGLISH, ConfigLocale.KOREA 두가지 중에 선택할 수 있습니다.
//Default는 영어입니다.
foodLensService.setLanguage(LanguageConfig.EN)
```

## 5. SDK 상세 스펙  
[상세 API 명세](https://doinglab.github.io/foodlens2sdk/android/index.html)  

## 6. SDK 사용 예제 
[Sample 예제](SampleCode/)

## 7. JSON Format
[JSON Format](../JSON%20Format)

[JSON Sample](../JSON%20Sample)

## 8. License
FoodLens is available under the MIT license. See the LICENSE file for more info.
