import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import java.text.SimpleDateFormat
import java.util.Date

// Click on the calendar button to open the calendar view
WebUI.click(findTestObject('Object Repository/calendar_button'))

// Get the current system date
def currentDate = new Date()

// Define the desired date format (first 3 alphabets for month)
def dateFormat = new SimpleDateFormat("EEE, d MMM")

// Format the current date to match the desired output
def formattedDate = dateFormat.format(currentDate).trim()

// Print the formatted current date
println("Formatted date: " + formattedDate)

// Get the date displayed on the web page and trim extra spaces
def today_date = WebUI.getText(findTestObject('Object Repository/getting_current_date')).trim()

// Extract the month part from today_date and reduce it to the first 3 letters
def today_date_parts = today_date.split(" ")
today_date_parts[2] = today_date_parts[2].substring(0, 3) // Shorten month to 3 letters

// Rebuild the shortened date
today_date = today_date_parts.join(" ")

// Print the updated today_date after modification
System.out.println("Today’s date from web after modification: " + today_date)

// Check if today_date is not equal to formattedDate
if (!today_date.equals(formattedDate)) {
    // Click on the previous button if dates don't match
    WebUI.click(findTestObject('Object Repository/previous_button'))
    println("Clicked on the previous button as dates do not match.")
} else {
    println("Dates match, no need to click.")
}

// Define the TestObject for the element with the XPath for "Today"
TestObject todayElement = new TestObject()
todayElement.addProperty("xpath", com.kms.katalon.core.testobject.ConditionType.EQUALS, "//div[@aria-label='Today']")

// Check if the "Today" element is present and click it if found
if (WebUI.verifyElementPresent(todayElement, 5, FailureHandling.OPTIONAL)) {
    WebUI.click(todayElement)
    println("Clicked on the 'Today' element.")
} else {
    println("'Today' element is not present.")
}

// Get the updated current date from the web page after interaction
def current_date = WebUI.getText(findTestObject('Object Repository/get_date')).trim()

// Extract the month part from current_date and reduce it to the first 3 letters
def current_date_parts = current_date.split(" ")
current_date_parts[2] = current_date_parts[2].substring(0, 3) // Shorten month to 3 letters

// Rebuild the shortened date
current_date = current_date_parts.join(" ")

// Print the updated current date from the web page
System.out.println("Updated current date from web after modification: " + current_date)

// Check if the updated date is equal to the formatted system date
if (current_date.equals(formattedDate)) {
    println("Date is of today")
} else {
    println("Date is not of today")
}
