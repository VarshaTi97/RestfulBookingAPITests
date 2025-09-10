## Booking API Automated Tests ##

This is a maven project repository which contains the test scripts, page objects, and test data for testing of Booking API. It is a `hybrid driven framework` which incorporates data driven approach.
Project is hosted on Github and using Github action to trigger the test in docker containerised environment and proced the results.
Target API DOC URL is: https://restful-booker.herokuapp.com/apidoc/index.html#api-Booking-GetBookings

### Repo organization ###
This repository contains the following directories:

   ```
   |____src/main/java
   | |____constants
   | |____exceptions
   | |____pages
   | |____pojoModels
   | |____utils
   |
   |
   |____src/test/java
   | |____booking
   |  |____integration
   |
   |
   |____resources
   | |____testdata
   | |____testExecutor
   |____dockerfile
   |____pom.xml
   ```

1. **booking** - Contains test automation scripts to implement test steps for various test scenarios under
   booking api.
   Examples:
   ```
   |____testScripts
   | |____BaseTest.java
   | |____DeleteBookinTests.java
   | |____GetBookingAPITests.java
   | |____UpdateBookingTests.java
   ```

2. **pages** - Tests are designed based on the Page Object Model (POM) design pattern. Under this model, for each feature
   has a corresponding `page class` containing `page methods` which perform operations page specific.
   Each web page under ToDo application has a dedicated page object `.java` file available under this directory.
   Examples:
   ```
   |____pages
   | |____BookingAPI.java
   ```

3. **Other directories**
   ```
   | Directory         | Description                                                                                         |
   | :---------------- | :---------------------------------------------------------------------------------------------------|
   | utils             | contains utility methods for test automation like API methods, excel reading, property file reading |
   | pojoModels        | contains pojo classes to be used for serialization/deserialization                                  |
   | constants         | contains data which is contant like filepaths, api endpoints                                        |

   ```

### Getting Started ###

Following are the instructions to set up a development environment to develop, execute and maintain automated
functional tests for Booking API

### Libraries Used in Framework ###

Following are the list of libraries used while building the framework:

   ```
   | Library Name      | Version  | Description                                                                                                 |
   | :---------------- | :------- | :-----------------------------------------------------------------------------------------------------------|
   | java              | 23.0.1   | Programming language                                                                                        |
   | testng            | 7.11.0   | Testing framework which supports test configured by annotations, data-driven testing, parametric tests, etc.|
   | rest assurred     | 5.5.6    | It is a API automation library.                                                                             |
   | allure reports    | 2.29.1   | to generate readable html reports                                                                           |
   | apache poi        | 4.1.0    | Java API To Access Microsoft Format Files, used for reading excel sheet data                                |
   | lombook           | 1.18.38  | To generate pojo and remove boiler plate codes                                                              |
   ```

### Prerequisites ###

1.Install allure in order to see the html report. On Mac
    - `brew install allure`

### Data Source used in framework ###

In this framework I have used Excel sheet and property files as data sources. These 2 testcases reading data from excel sheet.
1. checkBookingBySingleField
2. checkBookingByMultipleFields

### Execution Steps ###

1. As soon as code is pushed or MR is created for the main branch Github actions gets triggered. To view the latest report on main branch you can go to the `Actions` tab at the top
2. From all workflow select the first one as it will already have the run from latest commit.
3. Click on `build-test`
4. Click on `Upload allure-results as artifact` step, in there you will see the bottom `Artifact download URL`
5. This will download the `allure-reports.zip`.


### Execution Report ###

- Extract `allure-reports.zip` folder, then open terminal there and run command `allure serve allure-results`. This will give http url with result.
- Report files can be opened in any web browser.

### Problems###
Issues identified during testing are list under `Problems Identified During testsing` path

### Author ###

- Varsha Tiwari
