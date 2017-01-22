Privacy and Performance Diagnosis for Mobile Applications
=========================================================

This projects is related to a bachelor's thesis and the goal is to provide an approach for monitoring mobile applications and the derivation of privacy and performance problems.

Setup
-----

0.  Be sure that you that you have git installed on your machine.
1.  Download your favorite version of Eclipse and install the Gradle Buildship Plugin (from Eclipse Marketplace)
2.  Clone this repository. (Terminal -> git clone https://github.com/research-iobserve/mobile)
3.  In Eclipse select Import -> Gradle -> Gradle Project
4.  Now select the folder where you cloned the repository and finish the process.
5.  Your workspace now should contain five projects.
6.  There will be still some errors so you need follow the next steps.
7.  Create a new run configuration for Gradle and select enter the task "jarJar" and as working directory select the "thesis.root" project. Execute this configuration now.
8.  Right-click on thesis.root project and select Gradle -> Refresh Gradle Project. All errors should be gone now.

Using
-----

0. Create a Gradle build run configuration for the project "thesis.android.agent" and run it, so the Agent gets built and can be inserted into Android applications.
1. You can instrument an Android application using the "InstrumenterTest" class in the thesis-android-instrumenter project. Create a run configuration for this class and start it. Now you have to select the application you want to instrument and afterwards the instrumentation begins.
2. The instrumented application can be executed on emulated or on real devices.
3. **IMPORTANT**: Because the instrumented application can't save monitoring data on the device itself, it needs an server which is reachable from the device. To create such a server you can use the 'thesis.inspectit.bridge' project which provides one. You only need to execute the "StartServer" class and the monitoring data will be saved within the bridge project. (The address of the server is defined in the configuration of the instrumentation within the resources of the "thesis-android-instrumenter" project in config/default.xml)

Note: The instrumentation uses a default keystore to resign the application after modifying it. If you use this in production you have to exchange the default keystore with your actually used keystore.