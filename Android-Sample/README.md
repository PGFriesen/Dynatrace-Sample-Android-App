# Android Sample Application Quick-Start guide
Use this README.md guide to download and set up this sample Native Android Application written in Groovy (Java language)


# Table of Contents
- [Android Sample Application Quick-Start guide](#android-sample-application-quick-start-guide)
- [Table of Contents](#table-of-contents)
  - [<a name="pre-reqs"></a> Pre-requisites](#-pre-requisites)
  - [<a name="download-and-run-application"></a> Download and Run Application](#-download-and-run-application)
  - [<a name="set-up-for-automatic-instrumentation"></a> Set-up for Automatic Instrumentation](#-set-up-for-automatic-instrumentation)

## <a name="pre-reqs"></a> Pre-requisites
- Android Studio Installed
- Emulator Created in Android Studio or Physical Device with USB Debugging enabled


## <a name="download-and-run-application"></a> Download and Run Application
1. Download the project from bitbucket, save it anywhere on your machine (Note: Emulators cannot be run inside of virtual machines, so make sure you are running from a phyiscal host)<br>

<img src="../resources/screenshots/download_project.png" alt="example download" width="200"/>

2. Start Android Studio and select "open" and select the directory containing the gradle files. If you are using windows, you'll see an Android Icon in the project explorer window on the project you want to open<br>

<img src="../resources/screenshots/open_project.png" alt="open project" width="200"/>


3. You may be prompted that the project comes from an unknown source, if so, select "trust authors" and let the project load, when it is done, you should be able to set the Project Structure to "Android"<br>

<img src="../resources/screenshots/ide_overview.png" alt="ide overview" width="1000"/>

Here are some descriptions of different files that are mainly worked with in Android projects<br>
<img src="../resources/screenshots/project_overview.png" alt="android files" width="1000"/>



## <a name="set-up-for-automatic-instrumentation"></a> Set-up for Automatic Instrumentation
1. Create a Mobile Application in Dynatrace, and once created, disable the UserOptIn toggle<br>

<img src="../resources/screenshots/dynatrace_mobile_app.png" alt="new mobile application in dynatrace" width="1000"/>

2. Open the "Instrumentation Wizard" and follow the steps to instrument our project<br>

<img src="../resources/screenshots/instrumentation_wizard.png" alt="instrumentation wizard" width="600"/>

Once complete, it should look like this<br>
<img src="../resources/screenshots/build_gradle.png" alt="build.gradle preview" width="600"/>

3. Run a gradle sync to save the changes to the gradle file and build the application - <b>File > Sync Project with Gradle Files</b><br>
<img src="../resources/screenshots/gradle_sync.png" alt="gradle sync" width="600"/>


