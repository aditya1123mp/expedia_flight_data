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
import org.openqa.selenium.WebElement
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Specify the absolute path to save the file
File file = new File("C:\\Users\\DELL\\OneDrive\\Desktop\\mail\\Flight_Details.txt")
file.createNewFile()

// Base URL structure for the search page
String baseUrl = "https://www.expedia.co.in/Flights-Search?leg1=from%3AMumbai%20%28BOM-Chhatrapati%20Shivaji%20Intl.%29%2Cto%3ABengaluru%2C%20India%20%28BLR-Kempegowda%20Intl.%29%2Cdeparture%3A"
String suffixUrl = "TANYT%2CfromType%3AU%2CtoType%3AA&mode=search&options=carrier%3A%2Ccabinclass%3A%2Cmaxhops%3A1%2Cnopenalty%3AN&passengers=adults%3A1%2Cchildren%3A0%2Cinfantinlap%3AN&trip=oneway"

// Date formatter for 'dd-MM-yyyy'
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

// Get the current date
LocalDate today = LocalDate.now()

// Loop for the next 20 days
for (int i = 0; i < 20; i++) {
	// Add i days to the current date
	LocalDate futureDate = today.plusDays(i)

	// Format the date as 'dd-MM-yyyy'
	String formattedDate = futureDate.format(formatter)

	// Construct the full dynamic URL
	String dynamicUrl = baseUrl + formattedDate + suffixUrl

	// Navigate to the new URL
	WebUI.navigateToUrl(dynamicUrl)
	WebUI.delay(5) // Allow time for the page to load

	// Check if the warning message "Try searching nearby airports or alternative dates" is present
	TestObject noResultsMessage = new TestObject().addProperty('xpath', ConditionType.EQUALS, "//div[text()='Try searching nearby airports or alternative dates']")
	if (WebUI.verifyElementPresent(noResultsMessage, 5, FailureHandling.OPTIONAL)) {
		println("No results found for date: " + formattedDate + ". Trying the next date.")
		continue // Skip to the next iteration of the loop
	}

	// Define the XPath for the offer listings
	TestObject offerListing = new TestObject().addProperty('xpath', ConditionType.EQUALS, "//li[@data-test-id='offer-listing']")

	// Check if flight listings are present for this date
	if (WebUI.verifyElementPresent(offerListing, 5, FailureHandling.OPTIONAL)) {
		println("Offer listing found for date: " + formattedDate)

		// Check if the "Show More" button is present
		TestObject showMoreButton = new TestObject().addProperty('xpath', ConditionType.EQUALS, "//button[@data-test-id='show-more-button']")
		
		if (WebUI.verifyElementPresent(showMoreButton, 5, FailureHandling.OPTIONAL)) {
			// Ensure the button is clickable before clicking
			if (WebUI.waitForElementClickable(showMoreButton, 5)) {
				WebUI.scrollToElement(showMoreButton, 5) // Ensure the button is visible
				WebUI.click(showMoreButton)
				WebUI.delay(3) // Allow time for more listings to load
				println("Show More button clicked.")
			} else {
				println("Show More button is not clickable.")
			}
		} else {
			println("Show More button not found. Proceeding with available listings.")
		}

		// Scroll to the specific element (if required)
		TestObject scrollToElement = new TestObject().addProperty('xpath', ConditionType.EQUALS, "//button[@id='start-date-ONE_WAY-0-btn']")
		if (WebUI.verifyElementPresent(scrollToElement, 5, FailureHandling.OPTIONAL)) {
			WebUI.scrollToElement(scrollToElement, 5)
			println("Scrolled to the element.")
		}

		// Get the elements from the offer list
		List<WebElement> offerElements = WebUiCommonHelper.findWebElements(offerListing, 5)

		// Loop through each offer element and print the data directly
		for (WebElement offer : offerElements) {
			String offerDetails = "Date: " + formattedDate + "\n" +
								  "Flight Information: " + offer.getText() + "\n" +
								  "--------------------------------------------------------\n"
			
			// Write the details to the console and the text file
			println(offerDetails)
			file.append(offerDetails)
		}
	} else {
		println("No listings found for date: " + formattedDate + ". Trying the next date.")
	}
}
