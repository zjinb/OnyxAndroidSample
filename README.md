# Overview
Sample project of SDKs from Onyx-Intl, including [onyxsdk-base](https://github.com/onyx-intl/OnyxAndroidSample/wiki/Onyx-Base-SDK), [onyxsdk-scribble](https://github.com/onyx-intl/OnyxAndroidSample/wiki/Onyx-Scribble-SDK)

To use the SDK, please add the following statement to your build.gradle:

    compile 'com.onyx.android.sdk:onyxsdk-base:1.3.2'
    compile 'com.onyx.android.sdk:onyxsdk-scribble:1.0.4'
    
For onyxsdk-scribble SDK, dbflow library is inside the jitpack, so you have to add the following statement to your project build.gradle:

    maven { url "https://jitpack.io" }
#Demo
The project contains following examples that you should take care of:
* [EnvironmentDemoActivity](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/EnvironmentDemoActivity.java): shows how to use [DeviceEnvironment](https://github.com/onyx-intl/OnyxAndroidSample/wiki/DeviceEnvironment) to access removeable sdcard
* [EpdDemoActivity](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/EpdDemoActivity.java): basic demo of [EPD Screen Update](https://github.com/onyx-intl/OnyxAndroidSample/wiki/EPD-Screen-Update)  with [EpdController](https://github.com/onyx-intl/OnyxAndroidSample/wiki/EpdController)
* [FrontLightDemoActivity](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/FrontLightDemoActivity.java): demo of [FrontLightController](https://github.com/onyx-intl/OnyxAndroidSample/wiki/FrontLightController)
* [ScribbleTouchScreenDemoActivity](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/ScribbleTouchScreenDemoActivity.java): example of [Scribble API](https://github.com/onyx-intl/OnyxAndroidSample/wiki/Scribble-API) from [EpdController](https://github.com/onyx-intl/OnyxAndroidSample/wiki/EpdController) for IMX6 devices
> If the device supports touch, you write with your fingers.
* [ScribbleStylusSurfaceViewDemoActivity ](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/ScribbleStylusSurfaceViewDemoActivity.java): example of [Onyx Scribble SDK](https://github.com/onyx-intl/OnyxAndroidSample/wiki/Onyx-Scribble-SDK) for IMX6 devices with stylus
> You can write on webView with stylus. In fact, you can set anything view.
* [ScribbleStylusWebViewDemoActivity ](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/ScribbleStylusWebViewDemoActivity.java): example of [Onyx Scribble SDK](https://github.com/onyx-intl/OnyxAndroidSample/wiki/Onyx-Scribble-SDK) for IMX6 devices with stylus
> You can write on surfaceView with stylus. In fact, you can set anything view.
* [ScribbleStateDemoActivity ](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/ScribbleStateDemoActivity.java): example of [Scribble API](https://github.com/onyx-intl/OnyxAndroidSample/wiki/Scribble-API) from [EpdController](https://github.com/onyx-intl/OnyxAndroidSample/wiki/EpdController) for IMX6 devices
> If you want to enable or disable pen, you can call the api `EpdController.setScreenHandWritingPenState(view, PEN_START);`
* [ScribbleStylusTouchHelperDemoActivity ](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/ScribbleStylusTouchHelperDemoActivity.java): 
> `TouchHelper` is the latest api that you can write with stylus.
* [FullScreenDemoActivity](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/FullScreenDemoActivity.java): example of full screen switch
> If you want to switch full screen , please call the api ` DeviceUtils.setFullScreenOnResume(this, fullscreen);`.That  supports all devices.




