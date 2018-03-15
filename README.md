# training-mobile-basic-practice-project
Practice basic mobile interactions with elements on Android with Appium and Java, using Maven.

## Prerequisites
* Please download IntelliJ IDEA Community edition:
   https://www.jetbrains.com/idea/download/#section=windows
* Installation of IntelliJ IDEA will be done during the training session, but for reference all install steps can be found here:
   https://www.jetbrains.com/help/idea/install-and-set-up-intellij-idea.html
   
## Project resources
This is a Maven project which uses the standard Maven structure. It uses JUnit as a testing framework.
   * `mobile-maven-basic-practice/pom.xml` - the maven project configuration file, where all Java library dependencies should be added
   * `src/main/java/resources` - the app under test, yamba-debug.apk file
   * `src/test/java` - the Java test class with a simple test method

## Application under test
Yamba (Yet Another Micro Blogging App) was written to support various Android training classes, most specifically Android Bootcamp, and is an evolution of the project from Learning Android book written by Marko Gargenta. You can find it at http://github.com/thenewcircle/yamba. The version of the app used in this tutorial was downloaded from: https://github.com/mailat/android-testing-2016-02-19.

The debug apk can be found here:
* `src/main/java/resources/yamba-debug.apk`

The test user for this app is:
* username = `student`
* password = `password`

## Test scenario
As a test scenario in the Yamba application we will want to:
   1. Start the Yamba application fresh, the application has no user logged in
   2. Tap the `More options` button
   
      ![Alt text](screenshots/MoreOptionsBtn.png?raw=true)
   3. Tap the `Settings` button
   
       ![Alt text](screenshots/SettingsBtn.png?raw=true)
   4. Verify we landed on the Settings screen, meaning the Back button is displayed
   
       ![Alt text](screenshots/BackBtn.png?raw=true)
       
   5. Tap on the User Name field
   
        ![Alt text](screenshots/TapUsername.png?raw=true)     
   6. Retrieve the text from username
   
        ![Alt text](screenshots/EmptyUsername.png?raw=true)
   7. Verify the username text has a specific value. This value should be empty because the user is not logged in.
   8. Type in the value for username = student 
   
        ![Alt text](screenshots/UsernameText.png?raw=true)
   9. Tap on OK button in order to close the User Name dialog
   
        ![Alt text](screenshots/OkButton.png?raw=true)
   10. Verify the OK button is not displayed anymore
   11. Close the application

The automated test scenario implemented below is assuming that the application is not logged in.

## Test class
We will make use of JUnit test framework capabilities and will use 3 types of methods in our test class:

* Test setup method - annotated with `@Before` - a method which is executed before the test itself gets executed
* Test teardown method - annotated with `@After` - a method which is executed after the test itself finishes execution
* Test method - annotated with `@Test` - a method which represents the automated test scenario

1. Create a JUnit setup method which is annotated with `@Before`
    1. First we need to import:
        `import org.junit.Before;`
        
    2. At the beginning of the class we will declare an AndroidDriver object called `androidDriver` which we will use across all setup, teardown and test methods.
        ```
           public class MavenJunitTestClass {
               public AndroidDriver androidDriver; 
               }
        ```
        
    3. At the beginning of the test class we will add the annotation and the method name:
       ```
           @Before
           public void setUp() throws MalformedURLException { 
           }
        ```
        The setUp method will also throw an exception of type `MalformedURLException` because this is the one needed by the Java `URL` object constructor.
        
    4. In this method we will start the android driver session for our application, meaning details of the appium server and the desired capabilities:
    :exclamation: Please be careful to notice that we will set the `noReset` capability to `false` in order for the application to start fresh, not logged in.
        
        ```
            @Before
            public void setUp() throws MalformedURLException {
            System.out.println("Starting the Android Driver session in setUp method");
            URL serverUrl = new URL("http://127.0.0.1:4723/wd/hub/");
    
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(CapabilityType.PLATFORM, "Android");
            capabilities.setCapability(MobileCapabilityType.UDID, "192.168.56.101:5555");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "EmulatorS7");
            capabilities.setCapability(MobileCapabilityType.NO_RESET, "false");
            capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "600");
    
            //this path is Relative to the project directory path: src/main/resources/yamba-debug.apk
            //in order to use this copy paste the yamba-debug.apk in src/main/resources/
            //otherwise, copy paste here the full path from File Explorer to the yamba-debug.apk, including the name of the file itself yamba-debug.apk
            String appPath = System.getProperty("user.dir") +
                    File.separator + "src" + File.separator + "main" +
                    File.separator + "resources" + File.separator + "yamba-debug.apk";
    
            capabilities.setCapability(MobileCapabilityType.APP, appPath);
            capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.example.android.yamba");
            capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.example.android.yamba.MainActivity");
    
            androidDriver = new AndroidDriver(serverUrl, capabilities);
            System.out.println("Android Driver session started successfully in setUp method");
            }
        ```
       
         
2. Create a JUnit teardown method which is annotated with `@After`:
    1. First we need to import:
        `import org.junit.After;`
    2. After the `setUp()` method which we previously added, we will add the annotation and the method name for tearDown:
        ```
           @After
           public void tearDown() { 
           }
        ```
    3. In this method we will close the android driver session:
        ```
           @After
           public void tearDown() {
                System.out.println("Trying to close Android Driver session in tearDown method");
                //Closes the session
                androidDriver.quit();
                System.out.println("Android Driver session closed successfully in tearDown method");
           }
        ```

3. The test method will contain only elements, interactions with elements, waits and assertions which will verify expected results:
    ```
        @Test
        public void testSettings() throws InterruptedException { 
        }
    ```
     :exclamation: The test method will also throw an exception of type `InterruptedException` because this is the one needed by the Java `Thread.sleep()` method.
    
## Test method
The test method needs to implement all the test steps from the above described scenario.

1. Start the Yamba application - this is currently happening in our `setUp()` method, where we start the android session
2. Tap the `More options` button - now we write in the test method:
    1. Identify the `More options` button locator with Appium Inspector
    2. Find the element, click on it and wait for 1.5 seconds:
    ```
        @Test
        public void testSettings() throws InterruptedException {
        System.out.println("Starting the test method execution");
        //click on More Options
        MobileElement moreOptionsElement = (MobileElement) androidDriver.findElementByAccessibilityId("More options");
        moreOptionsElement.click();

        //wait for 1500 milliseconds which is 1.5 seconds
        Thread.sleep(1500);
    ```
    :exclamation: We have added the use of a wait method, `Thread.sleep(1500)`, which creates a pause of 1500 milliseconds = 1.5 seconds in execution, in order to make sure all elements 
    are loaded properly after click.
3. Tap the `Settings` button - continue to write in the test method:
    1. Identify the `Settings` element locator with Appium Inspector
    2. Find the element, click on it and wait for 1.5 seconds:
    ```
        //click on Settings
        String settingsButtonXpath = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[2]/android.widget.RelativeLayout/android.widget.TextView";
        MobileElement settingsElement = (MobileElement) androidDriver.findElementByXPath(settingsButtonXpath);
        settingsElement.click();
        //wait for 1500 milliseconds which is 1.5 seconds
        Thread.sleep(1500);
    ```
    We have added the use of a wait method, `Thread.sleep(1500)`, which creates a pause of 1.5 seconds in execution in execution, in order to make sure all elements 
    are loaded properly after click.
4. Verify we landed on the Settings screen, meaning the `Back` button is displayed:
    1. Identify the `Back` button locator with Appium Inspector
    2. Find the element and verify its displayed attribute is true, with an assertion method:
    ```
        //verify Back button is displayed
        MobileElement backButtonElement = (MobileElement) androidDriver.findElementByAccessibilityId("Navigate up");
        boolean isBackButtonDisplayed = backButtonElement.isDisplayed();
        Assert.assertTrue("The back button is not displayed", isBackButtonDisplayed);
        //wait for 1500 milliseconds which is 1.5 seconds
        Thread.sleep(1500);
    ```
     We have added the use of a wait method, `Thread.sleep(1500)`, which creates a pause of 1.5 seconds in execution, in order to make sure all elements 
     are loaded properly after click.
5. Tap on the User Name field
    1. Identify the `User Name` label element locator with Appium Inspector
        * This will be the `resource-id` = `title` which is the same as for Password label 
        * This means that we can identify a list of elements by using the `id` = `title`, a list of size 2
        * We can  uniquely identify the `User Name` label element by:
            * Retrieving from the list the element from index 0
        * We can  uniquely identify the `Password` label element by:
            * Retrieving from the list the element from index 1 
    2. Find the element list, retrieve the element from index 0:
    ```
        //find & click username label element
        String usernameOrPasswordLabelId = "title";
        //the index of the username element is 0, the index of the password element is 1
        int usernameLabelElementIndex = 0;
        //retrieve the username label element from index usernameLabelElementIndex
        MobileElement usernameLabelElement = (MobileElement) androidDriver.findElementsById(usernameOrPasswordLabelId).get(usernameLabelElementIndex);
    ```
    3. Click on it and wait for 1 second:
    ```
        //click the username label element & wait 1 second
        usernameLabelElement.click();
        Thread.sleep(1000);
    ```
6. Retrieve the text from username:
    1. Identify the username text field element locator with Appium Inspector
    2. Find the element, retrieve the text value from it and save it as a String value:
    ```
        //retrieve the text from the username text
        String usernameOrPasswordTextValueId = "edit";
        MobileElement usernameTextElement = (MobileElement) androidDriver.findElementsById(usernameOrPasswordTextValueId).get(usernameLabelElementIndex);
        //save the text value as actual value in a String
        String actualUsernameTextValue = usernameTextElement.getText();
    ```
7. Verify the username text has a specific value. This value should be empty because the user is not logged in:
    1. The text value retrieved form the username text field is the actual value which we see on the screen
    2. The expected value needs to be compared with the actual value - because we did not perform the login, the expected value is the empty String `""`:
     ```
        //print to the console the actual username text value and verify it is
        System.out.println("The username value from screen is: " + actualUsernameTextValue);
        String expectedValue = "";
        Assert.assertEquals("Username actual text value is not as expected", expectedValue, actualUsernameTextValue);
     ```
8. Type in the value for username = student:
    1. The text element is already identified, we just use it for typing in the text value
    2. Type in the username value and wait 1 second:
    ```
        //type in the username = student password and wait 1 second
        usernameTextElement.sendKeys("student");
        Thread.sleep(1000);
    ```
9. Tap on OK button in order to close the User Name dialog
    1. Identify the `OK` button element locator with Appium Inspector
    2. Find the element, click on it and wait for 1 second:
    ```
        //click on OK button
        String okButtonId = "button1";
        MobileElement okButtonElement = (MobileElement) androidDriver.findElementById(okButtonId);
        okButtonElement.click();
        Thread.sleep(1000);
    ```
10. Verify the OK button is not displayed anymore
    1. The `OK` button element is already identified, we just need to verify its displayed attribute is false, with an assertion method:
    ```
        //verify OK button is not displayed anymore
        boolean isOkButtonDisplayed = okButtonElement.isDisplayed();
        Assert.assertFalse("The OK button is still displayed", isOkButtonDisplayed);
        System.out.println("Finished the test method execution");
    ```
11. Close the application - this is already done the in `tearDown()` method where we close the session.

## Run the test method
Make sure the Appium server at localhost is running, started through Appium desktop app.

Run in IntelliJ IDEA - IntelliJ IDEA already has an automatic way of running the JUnit tests,
therefore it is enough to:
    * Right click on the test method name and select `Run` option

The test will start running, and the app will start on the emulator/device

## Practice, practice :exclamation: :sweat:

1.  Implement the following test steps in the current test method:

    1. Tap on the Password field
    2. Verify password text has a specific value. This can be empty if no password has been filled, or the value of the password which has been used.
    3. Tap on OK button in order to close the Password dialog
    4. Verify the OK button is not displayed anymore

If you managed in a timely manner to implement the above steps, please also do the following:

2. Navigate with Back and try to post a message
    
    1. Tap on the Back button - you should reach the Yamba app home page
    2. Tap on Post button
    3. Type in text which should be posted 
    4. Type OK button