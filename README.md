# Automation Suite

## Summary

Framework is build using the stack
* [REST-assured](http://rest-assured.io/)
* [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [TestNG](https://testng.org/doc/)
* [Extent Reporting](https://extentreports.com/)
* [Lombok](https://projectlombok.org/)
<br>

## Project Pre-Installation

#### Dependency handling
All Dependencies handled by Maven

#### Required software to run tests from Command Line
* [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Apache Maven 3](http://maven.apache.org/download.cgi)

#### Required software to run tests from IDE
Install *Required software to run tests from IDE* 
* [IntelliJ IDEA](https://www.jetbrains.com/de-de/idea/)
* Add Lombok plugin in IntelliJ IDEA from plugin market place

### Test Project Build

#### Steps
1. Clone the repository.
2. Go to ***takeaway-qa*** folder.
3. Import the project as maven project.

## Running the Tests 

#### Available test suites
- Tests configured to run via file: ***api_tests.xml***

#### Test run configuration

***config.properties***
<br>
It is possible to configure test run via **config.properties**

    api.base.url=api.themoviedb.org
    api.protocol=https
    api.version=4
    api.write.access.token= <Access token with write permissions>

#### Command line way
It is also possible to trigger tests from command line.

## Steps to run tests
1. Go to ***takeaway-qa*** folder.
2. Use the below command to run the TestNG tests
     
        mvn clean test
        
#### Extent report
You can find generated extent report for tests under "/test-output/testReporter.html"  