# Overview
Sample project of SDKs from Onyx-Intl, including [onyxsdk-base](https://github.com/onyx-intl/OnyxAndroidSample/wiki/Onyx-Base-SDK), [onyxsdk-scribble](https://github.com/onyx-intl/OnyxAndroidSample/wiki/Onyx-Scribble-SDK)

To use the SDK, please add the following statement to your build.gradle:

    compile 'com.onyx.android.sdk:onyxsdk-base:1.3.1'
    compile 'com.onyx.android.sdk:onyxsdk-scribble:1.0.2'
    
For onyxsdk-scribble SDK, dbflow library is inside the jitpack, so you have to add the following statement to your project build.gradle:

    maven { url "https://jitpack.io" }

The project contains following examples:
* [EnvironmentDemoActivity](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/EnvironmentDemoActivity.java): shows how to use [DeviceEnvironment](https://github.com/onyx-intl/OnyxAndroidSample/wiki/DeviceEnvironment) to access removeable sdcard
* [EpdDemoActivity](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/EpdDemoActivity.java): basic demo of [EPD Screen Update](https://github.com/onyx-intl/OnyxAndroidSample/wiki/EPD-Screen-Update)  with [EpdController](https://github.com/onyx-intl/OnyxAndroidSample/wiki/EpdController)
* [FrontLightDemoActivity](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/FrontLightDemoActivity.java): demo of [FrontLightController](https://github.com/onyx-intl/OnyxAndroidSample/wiki/FrontLightController)
* [ScribbleStylusDemoActivity](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/ScribbleStylusDemoActivity.java): example of [Onyx Scribble SDK](https://github.com/onyx-intl/OnyxAndroidSample/wiki/Onyx-Scribble-SDK) for IMX6 devices with stylus
* [ScribbleTouchScreenDemoActivity](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/ScribbleTouchScreenDemoActivity.java): example of [Scribble API](https://github.com/onyx-intl/OnyxAndroidSample/wiki/Scribble-API) from [EpdController](https://github.com/onyx-intl/OnyxAndroidSample/wiki/EpdController) for IMX6 devices


