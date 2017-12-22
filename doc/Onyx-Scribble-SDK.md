Scribble SDK for devices with stylus.

Latest version is 1.0.5, can be referenced with following gradle statement:

    compile 'com.onyx.android.sdk:onyxsdk-scribble:1.0.6'

You can see sample code in [ScribbleStylusSurfaceViewDemoActivity](https://github.com/onyx-intl/OnyxAndroidSample/blob/master/app/sample/src/main/java/com/onyx/android/sample/ScribbleStylusSurfaceViewDemoActivity.java) 

For onyxsdk-scribble SDK, dbflow library is inside the jitpack, so you have to add the following statement to your project build.gradle:

   `maven { url "https://jitpack.io" }`

EpdController.enterScribbleMode must be use after surface view showed, so we use as:

  `surfaceView.post(new Runnable() {
               @Override
               public void run() {
                   enterScribbleMode();
               }
           });
  `
