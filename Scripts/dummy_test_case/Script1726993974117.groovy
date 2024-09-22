import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import org.apache.commons.lang.RandomStringUtils as RandomStringUtils
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.common.WebUiCommonHelper as WebUiCommonHelper
import org.openqa.selenium.WebElement // Add this import

// Check if the message "Try searching nearby airports or alternative dates" is present
String warningMessageXPath = "//div[text()='Try searching nearby airports or alternative dates']"
TestObject warningMessage = new TestObject().addProperty('xpath', ConditionType.EQUALS, warningMessageXPath)

if (WebUI.verifyElementPresent(warningMessage, 5)) {
    println("Message is present. Proceeding to change the date.")

    // Click on the start date button
    String startDateBtnXPath = "//button[@id='start-date-ONE_WAY-0-btn']"
    TestObject startDateButton = new TestObject().addProperty('xpath', ConditionType.EQUALS, startDateBtnXPath)
    WebUI.click(startDateButton)
    WebUI.delay(1)

    // Select the next available date from the list
    String availableDatesXPath = "//button[@class='uitk-date-picker-day undefined']"
    
    // Find all available date elements (as WebElements)
    List<WebElement> availableDates = WebUiCommonHelper.findWebElements(new TestObject().addProperty('xpath', ConditionType.EQUALS, availableDatesXPath), 5)

    // If dates are available, click on the first available one by creating a new TestObject
    if (availableDates.size() > 0) {
        WebElement firstDateElement = availableDates[0]
        
        // Extract the xpath of the first available date element
        String firstDateXPath = firstDateElement.getAttribute("xpath")
        
        // Create a new TestObject for the first available date using the extracted xpath
        TestObject firstDate = new TestObject().addProperty('xpath', ConditionType.EQUALS, availableDatesXPath)
        
        // Click on the first available date
        WebUI.click(firstDate)
        WebUI.delay(1)

        // Click on the "Done" button to apply the date
        String doneBtnXPath = "//button[@data-stid='apply-date-picker']"
        TestObject doneButton = new TestObject().addProperty('xpath', ConditionType.EQUALS, doneBtnXPath)
        WebUI.click(doneButton)
        WebUI.delay(1)
    } else {
        println("No available dates found.")
    }
} else {
    println("Message not present. Skipping date selection.")
}

// Now, continue with clicking buttons and extracting data

// Maximum number of buttons to click and extract data from
int maxButtons = 20
int clickedButtons = 0
int i = 1 // Start from the first button

while (clickedButtons < maxButtons) {
    // XPath to select the button dynamically
    String buttonXPath = "//ul[@role='tablist']/li[" + i + "]/button[@type='button']"
    TestObject button = new TestObject().addProperty('xpath', ConditionType.EQUALS, buttonXPath)

    // Verify if the button is present (wait for up to 5 seconds)
    if (WebUI.verifyElementPresent(button, 5)) {
        // Click the button
        WebUI.click(button)
        WebUI.delay(1) // Optional delay after clicking

        // XPath for extracting the date and price from the clicked button
        String dateXPath = "//ul[@role='tablist']/li[" + i + "]/button[@type='button']/div[contains(@class, 'date')]"
        String priceXPath = "//ul[@role='tablist']/li[" + i + "]/button[@type='button']/div[contains(@class, 'price')]"
        
        // Extract the date and price text
        TestObject dateElement = new TestObject().addProperty('xpath', ConditionType.EQUALS, dateXPath)
        TestObject priceElement = new TestObject().addProperty('xpath', ConditionType.EQUALS, priceXPath)

        String date = WebUI.getText(dateElement)
        String price = WebUI.getText(priceElement)

        // Log the extracted data
        println("Button " + (clickedButtons + 1) + " - Date: " + date + ", Price: " + price)

        // Increment the count of clicked buttons
        clickedButtons++
    } else {
        println("Button at index " + i + " is not present or visible.")
    }

    // Move to the next button
    i++
}
