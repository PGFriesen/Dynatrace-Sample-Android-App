# Android Sample Application Quick-Start guide
Use this README.md guide to download and set up this sample Native Android Application written in Groovy (Java language)


# Table of Contents
- [Android Sample Application Quick-Start guide](#android-sample-application-quick-start-guide)
- [Table of Contents](#table-of-contents)
  - [<a name="-pre-reqs"></a> Pre-requisites](#-pre-requisites)
  - [<a name="download-and-run-application"></a> Download and Run Application](#-download-and-run-application)
  - [<a name="set-up-for-automatic-instrumentation"></a> Set-up for Automatic Instrumentation](#-set-up-for-automatic-instrumentation)

## <a name="-pre-reqs"></a> Pre-requisites
- Android Studio Installed
- Emulator Created in Android Studio or Physical Device with USB Debugging enabled
  - (Note: Emulators cannot be run inside of virtual machines, so make sure you are running from a phyiscal host)


## <a name="download-and-run-application"></a> Download and Run Application
1. Download the project from bitbucket
   - Option 1: Git Clone<br>
```
cd <location where the project will be saved to>
git clone ssh://git@bitbucket.lab.dynatrace.org/~paul.friesen/sample-mobile-applications.git
```

   - Option 2: Manually download and save
     - Navigate to project URL: https://bitbucket.lab.dynatrace.org/users/paul.friesen/repos/sample-mobile-applications/browse 
     - Under "Source", next to the branch name "main" select the "..." and click download
       - Save it anywhere on your machine
<br>

1. Open Project in Android Studio
   - Select <b>Open Existing Project</b>
   - Navigate to the location where you saved <b>sample-mobile-applications</b>
   - Select <b>Android-Sample</b> and click open
   - If prompted with "Project comes from an unknown source, do you want to trust it?" select <b>trust authors</b>
   - Wait for project to sync
     - In the Project files section on the left side of the IDE, you may notice that it says <b>Project</b> and when the sync is complete and the project is recognized as an Android project, you'll want to select the dropdown and select <b>Android</b>
<br>

3. Build and run the project
   - <b>File > Sync Project with Gradle Files</b>
   - <b>Build > Make Project </b>
   - <b>Run > Run 'App'</b> or click the green triangle play button
     - The application will then build and run on the most recently used emulator
     - (If the emulator is not running, running should start it, but if not, ensure the emulator is created and can be run on its own)


## <a name="set-up-for-automatic-instrumentation"></a> Set-up for Automatic Instrumentation
1. Create a Mobile Application in Dynatrace, and once created, disable the UserOptIn toggle<br>
2. Open the "Instrumentation Wizard" and follow the steps to instrument our project<br>
3. Run a gradle sync to save the changes to the gradle file and build the application - <b>File > Sync Project with Gradle Files</b><br>
4. Select <b>Run > Run 'app'</b>
