import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class MavenJunitTestClass {
    public AndroidDriver androidDriver;

    /**
     * Starts the android session with the given appium server and capabilities
     *
     * @throws MalformedURLException
     */
    @Before
    public void setUp() throws MalformedURLException {
        URL serverUrl = new URL("http://127.0.0.1:4723/wd/hub/");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PLATFORM, "Android");
        capabilities.setCapability(MobileCapabilityType.UDID, "192.168.56.101:5555");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "EmulatorS7");
        capabilities.setCapability(MobileCapabilityType.NO_RESET, "true");
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
    }

    /**
     * Close the session opened with Android driver
     */
    @After
    public void tearDown() {
        //Closes the session
        androidDriver.quit();
    }

    @Test
    public void testSettings() throws InterruptedException {
        //click on More Options
        MobileElement moreOptionsElement = (MobileElement) androidDriver.findElementByAccessibilityId("More options");
        moreOptionsElement.click();
        //wait for 1500 milliseconds which is 1.5 seconds
        Thread.sleep(1500);

        //click on Settings
        String settingsButtonXpath = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[2]/android.widget.RelativeLayout/android.widget.TextView";
        MobileElement settingsElement = (MobileElement) androidDriver.findElementByXPath(settingsButtonXpath);
        settingsElement.click();
        //wait for 1500 milliseconds which is 1.5 seconds
        Thread.sleep(1500);

        //verify Back button is displayed
        MobileElement backButtonElement = (MobileElement) androidDriver.findElementByAccessibilityId("Navigate up");
        boolean isBackButtonDisplayed = backButtonElement.isDisplayed();
        Assert.assertTrue("The back button is not displayed", isBackButtonDisplayed);
        //wait for 1500 milliseconds which is 1.5 seconds
        Thread.sleep(1500);

        //find & click username label element
        String usernameOrPasswordLabelId = "title";
        //the index of the username elemnt is 0, the index of the password element is 1
        int usernameLabelElementIndex = 0;
        //retrieve the username label element from index usernameLabelElementIndex
        MobileElement usernameLabelElement = (MobileElement) androidDriver.findElementsById(usernameOrPasswordLabelId).get(usernameLabelElementIndex);
        //click the username label element & wait 1 second
        usernameLabelElement.click();
        Thread.sleep(1000);

        //retrieve the text from the username text
        String usernameOrPasswordTextValueId = "edit";
        MobileElement usernameTextValueElement = (MobileElement) androidDriver.findElementsById(usernameOrPasswordTextValueId).get(usernameLabelElementIndex);
        //save the text value as actual value in a String
        String actualUsernameTextValue = usernameTextValueElement.getText();
        //print to the console the actual username text value and verify it is
        System.out.println("The username value from screen is: " + actualUsernameTextValue);
        String expectedValue = "";
        Assert.assertEquals("Username actual text value is not as expected", expectedValue, actualUsernameTextValue);

        //click on OK button
        String okButtonId = "button1";
        MobileElement okButtonElement = (MobileElement) androidDriver.findElementById(okButtonId);
        okButtonElement.click();
        Thread.sleep(1000);

        //verify OK button is not displayed anymore
        boolean isOkButtonDisplayed = okButtonElement.isDisplayed();
        Assert.assertFalse("The OK button is still displayed", isOkButtonDisplayed);
    }
}
