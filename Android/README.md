# CaloAI(FoodLens2) SDK Android Manual
## [한국어 메뉴얼](README_KO.md)
## [ReleaseNote](ReleaseNote.md)

If you want to use CaloAI(FoodLens2), please use this SDK. Please check below descriptions. 

## 1. Android project setting

### 1.1 Support Android 12
If you want to use Android 12, please set Compile SDK Version over 31. Open app > Gradle Scripts(Gradle Script) > build.gradle (Module: app) and change compileSdkVerion like below.

```java
android {
        ....
        compileSdkVersion 31
	....       
    }
```

### 1.2 Gradle setting
#### 1.2.1 minSdkVersion setting
- You need to set minSdkVersion over 21. Open app > Gradle Scripts(Gradle Script) > build.gradle (Module: app) and change defaultConfig{} section like below.
```java
   defaultConfig {
        ....
        minSdkVersion 23
	....       
    }
```
#### 1.2.2 Gradle dependencies setting
- Open app > Gradle Scripts(Gradle Script) > build.gradle (Module: app) and add foodlens sdk like below in dependencies{} section.
```java
   implementation "com.doinglab.foodlens:FoodLens2:1.0.0"
```

## 2. Resources and Manifests setting
If you want to use FoodLens2SDK, you need to set CompanyToken, AppToken in Manifests.

### 2.1 AppToken, CompanyToken setting
Please add issued AppToken, CompanyToken on your /app/res/values/strings.xml.
```xml
<string name="foodlens_app_token">[AppToken]</string>
<string name="foodlens_company_token">[CompanyToken]</string>
```

* Add Meta data
After that, please add your token like below in Manifest.xml.
```xml
<meta-data android:name="com.doinglab.foodlens.sdk.apptoken" android:value="@string/foodlens_app_token"/> 
<meta-data android:name="com.doinglab.foodlens.sdk.companytoken" android:value="@string/foodlens_company_token"/> 
```

### 2.2 Common
* ProGuard Setting

If you use code obfuscation based on proguard, please set below setting on proguard property file.
```xml
-keep public class com.doinglab.foodlens2.sdk.** {
       *;
}
```

## 3.Set address of independent CaloAI server
   Please add below informaiotn on your Manifest.xml.
```xml
//Pelase add only domain name or ip address instead of URL e.g) www.foodlens.com, 123.222.100.10
<meta-data android:name="com.doinglab.foodlens.sdk.serveraddr" android:value="[server_address]"/> 
```  

## 4. How to use SDK
CaloAI API is working based on image which contains foods.

### 4.1 Get prediction result
1. Create FoodlensService
2. Call the predict method. 
Parameters are Jpeg image, userId (optional), and RecognizeResultHandler. 
You can use image from Camera or Gallery. 
If there is no userId, it can be omitted.</br>
### NOTE THAT, please use ORIGINAL IMAGE, if resolution of image is low, the recognition result can be impact.

3. Sample Code
```java
//Create Network Service
private val foodLensService by lazy {
    FoodLens.createFoodLensService(this@MainActivity)
}

//Call prediction method.
//foodLensService.predict(byteData, "input_userid", object : RecognitionResultHandler { //If userId exists
foodLensService.predict(byteData, object : RecognitionResultHandler { //If userId does not exist
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

### 4.2 CaloAI Options
#### 4.2.1 Auto image rotation based on EXIF orientation information
```
//You can use image rotation based on EXIF information, if you set true, food coordinate can be rotated based EXIF information.
//Default value is true
foodLensService.setAutoRotate(true)
```
#### 4.2.2 Language setting
```
//There are two options, LanguageConfig.EN, LanguageConfig.KO. Default is English
foodLensService.setLanguage(LanguageConfig.EN)
```

## 5. SDK detail specification
[API Spec](https://doinglab.github.io/foodlens2sdk/android/index.html)  

## 6. SDK Sample
[Sample](SampleCode/)

## 7. JSON Format
[JSON Format](../JSON%20Format)

[JSON Sample](../JSON%20Sample)

## 8. License
CaloAI is available under the MIT license. See the LICENSE file for more info.
