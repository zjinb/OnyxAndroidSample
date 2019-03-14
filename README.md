# Overview
Sample project of SDKs from Onyx-Intl, including [onyxsdk-base](doc/Onyx-Base-SDK.md), [onyxsdk-scribble](doc/Onyx-Scribble-SDK.md), [onyxsdk-pen](doc/Onyx-Pen-SDK.md)

To use the SDK, please add the following statement to your build.gradle:
```gradle
    compile ('com.onyx.android.sdk:onyxsdk-base:1.4.3.7')
    compile('com.onyx.android.sdk:onyxsdk-pen:1.0.8')
    compile('com.onyx.android.sdk:onyxsdk-data:1.1.0')
    compile('com.onyx.android.sdk:onyxsdk-notedata:1.0.5')
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
Following demos are example of [onyxsdk-pen](doc/Onyx-Pen-SDK.md).

We use TouchHepler api to draw

* [ScribbleTouchHelperDemoActivity](app/sample/src/main/java/com/onyx/android/sample/scribble/ScribbleTouchHelperDemoActivity.java) is an example of TouchHepler.

We have no restrictions on the view,  you can set anything view.For example, SurfaceView , webview.
We will return relative coordinates, According to the view you set.

* [ScribbleSurfaceViewDemoActivity](app/sample/src/main/java/com/onyx/android/sample/scribble/ScribbleTouchHelperDemoActivity.java): example of SurfaceView

* [ScribbleWebViewDemoActivity](app/sample/src/main/java/com/onyx/android/sample/scribble/ScribbleWebViewDemoActivity.java): example of Webview

If the device supports touch, you scribble with your fingers.
* [ScribbleTouchScreenDemoActivity](app/sample/src/main/java/com/onyx/android/sample/scribble/ScribbleTouchScreenDemoActivity.java): example of [Scribble API](doc/Scribble-API.md) from [EpdController](doc/EpdController.md) for IMX6 devices

We alse support move eraser, multiple view scribble and save scribble points

* [ScribbleMoveEraserDemoActivity](app/sample/src/main/java/com/onyx/android/sample/scribble/ScribbleMoveEraserDemoActivity.java)
: example of move eraser

* [ScribbleMultipleScribbleViewActivity](app/sample/src/main/java/com/onyx/android/sample/scribble/ScribbleMultipleScribbleViewActivity.java)
: example of multiple view scribble

* [ScribbleSavePointsDemoActivity](app/sample/src/main/java/com/onyx/android/sample/scribble/ScribbleSavePointsDemoActivity.java)
: example of save scribble points

## 4.Screensaver

* [ScreensaverActivity](app/sample/src/main/java/com/onyx/android/sample/ScreensaverActivity.java)
: example of setting screensaver

## 5.Open Setting

* [OpenSettingActivity](app/sample/src/main/java/com/onyx/android/sample/OpenSettingActivity.java)
: example of open setting

## 6.Other API
* **DictionaryUtils** to query word in dictionary, for more details to see [DictionaryActivity](./app/sample/src/main/java/com/onyx/android/sample/DictionaryActivity.java)
