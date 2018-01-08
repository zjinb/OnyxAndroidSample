# Overview
Sample project of SDKs from Onyx-Intl, including [onyxsdk-base](doc/Onyx-Base-SDK.md), [onyxsdk-scribble](doc/Onyx-Scribble-SDK.md)

To use the SDK, please add the following statement to your build.gradle:
```gradle
    compile ('com.onyx.android.sdk:onyxsdk-base:1.3.7') 
    compile ('com.onyx.android.sdk:onyxsdk-scribble:1.0.8')
```

    
For onyxsdk-scribble SDK, dbflow library is inside the jitpack, so you have to add the following statement to your project build.gradle:
```gradle
    maven { url "https://jitpack.io" }
```

# Demo
The project contains following examples that you should take care of:

## 1.Screen
* [EpdDemoActivity](app/sample/src/main/java/com/onyx/android/sample/EpdDemoActivity.java): basic demo of [EPD Screen Update](doc/EPD-Screen-Update.md)  with [EpdController](doc/EpdController.md)
* [FrontLightDemoActivity](app/sample/src/main/java/com/onyx/android/sample/FrontLightDemoActivity.java): demo of [FrontLightController](doc/FrontLightController.md). If device support frontLight, you can switch the screen brightness.
* [FullScreenDemoActivity](app/sample/src/main/java/com/onyx/android/sample/FullScreenDemoActivity.java): example of full screen switch. If you want to switch full screen , please call the api ` DeviceUtils.setFullScreenOnResume(this, fullscreen);`.That  supports all devices.

## 2.SDCard
* [EnvironmentDemoActivity](app/sample/src/main/java/com/onyx/android/sample/EnvironmentDemoActivity.java): shows how to use [DeviceEnvironment](doc/DeviceEnvironment.md) to access removeable sdcard. You can call `DeviceEnvironment.getRemovableSDCardDirectory().getAbsolutePath();`

## 3.Scribble
`TouchHelper` is the latest api that you can scribble with stylus. You should call it.For more detailed usage, check out it out [here](doc/Scribble-TouchHelper-API.md)
* [ScribbleStylusTouchHelperDemoActivity ](app/sample/src/main/java/com/onyx/android/sample/ScribbleStylusTouchHelperDemoActivity.java): 

We have no restrictions on the view,  you can set anything view.For example, SurfaceView , webview.
We will return relative coordinates, According to the view you set.
* [ScribbleStylusSurfaceViewDemoActivity ](app/sample/src/main/java/com/onyx/android/sample/ScribbleStylusSurfaceViewDemoActivity.java): example of [Onyx Scribble SDK](doc/Onyx-Scribble-SDK.md) for IMX6 devices with stylus
* [ScribbleStylusWebViewDemoActivity ](app/sample/src/main/java/com/onyx/android/sample/ScribbleStylusWebViewDemoActivity.java): example of [Onyx Scribble SDK](doc/Onyx-Scribble-SDK.md) for IMX6 devices with stylus

If the device supports touch, you scribble with your fingers.
* [ScribbleTouchScreenDemoActivity](app/sample/src/main/java/com/onyx/android/sample/ScribbleTouchScreenDemoActivity.java): example of [Scribble API](doc/Scribble-API.md) from [EpdController](doc/EpdController.md) for IMX6 devices