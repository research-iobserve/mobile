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
5.  Your workspace now should contain several new gradle projects.
    1. This already includes the adjusted iObserve projects.
6.  There will be still some errors so you need follow the next steps.
7.  Create a new run configuration for Gradle and select enter the task "jarJar" and as working directory select the "org.iobserve.mobile.agent" project. Execute this configuration now.
8.  Right-click on "iobserve-analysis" project and select Gradle -> Refresh Gradle Project. All errors should be gone now.

Using
-----

0. Create a Gradle build run configuration for the project "org.iobserve.mobile.agent" and run it, so the Agent gets built and can be inserted into Android applications.
1. You can instrument an Android application using the "InstrumenterTest" class in the "org.iobserve.mobile.instrument" project. Create a run configuration for this class and start it. Now you have to select the application you want to instrument and afterwards the instrumentation begins.
2. The instrumented application can be executed on emulated or on real devices.
3. **IMPORTANT**: Because the instrumented application can't save monitoring data on the device itself, it needs an server which is reachable from the device. To create such a server you can use the 'org.iobserve.mobile.server' project which provides one. You only need to execute the "StartServer" class and the monitoring data will be saved within the server project. (The address of the server is defined in the configuration of the instrumentation within the resources of the "org.iobserve.mobile.instrument" project in config/default.xml)

Using iObserve extension
-----

1. The usage of the extended iObserve version is very intuitive. It doesn't differ much from the usage of the common iObserve execution.
2. You can see how to use iObserve here: [https://github.com/research-iobserve/iobserve-analysis/blob/master/doc/general/UsageGuide_iObserve.txt](https://github.com/research-iobserve/iobserve-analysis/blob/master/doc/general/UsageGuide_iObserve.txt) and you only have to select the path with the recorded monitoring logs within the server project.
    1. It is necessary that you have existing PCM models for your application.
    2. Furthermore you need to have a correspondence model which contains mappings for PCM entities so iObserve can automatically detect which method call belongs to which PCM entity.
3. After running iObserve, you get an updated PCM model which can be further analyzed.

Note: The instrumentation uses a default keystore to resign the application after modifying it. If you use this in production you have to exchange the default keystore with your actually used keystore.