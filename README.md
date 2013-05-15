openmidaas-android-lib
======================

Running the tests
----------------------
To run the tests, run the following ant command:
ant -f build.xml debug install test

You will need to have a emulator/device running/connected to run 
the tests. 
You can set your custom test runner by modifying the property
test.runner in the ant.properties file. By default, 
the tests are using the the TestSuite test runner under 
org.openmidaas.library.test.models but you can set it to any test
runner you wish. 
The status of the tests will be shown in the console. 

Acknowledgements
=====================
This project uses other open-source libraries such as:
* [AsyncHTTPClient](https://github.com/AsyncHttpClient/async-http-client)
* [Android JUnit Report Test Runner](https://github.com/jsankey/android-junit-report)
* [JUnit](http://junit.sourceforge.net/) 
* [libphonenumber](http://code.google.com/p/libphonenumber/)
