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
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.webui.common.WebUiCommonHelper as WebUiCommonHelper
import org.openqa.selenium.WebElement
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Base URL structure for the search page
String baseUrl = "https://www.expedia.co.in/Flights-Search?leg1=from%3AMumbai%20%28BOM-Chhatrapati%20Shivaji%20Intl.%29%2Cto%3ABengaluru%2C%20India%20%28BLR-Kempegowda%20Intl.%29%2Cdeparture%3A"
String suffixUrl = "TANYT%2CfromType%3AU%2CtoType%3AA&mode=search&options=carrier%3A%2Ccabinclass%3A%2Cmaxhops%3A1%2Cnopenalty%3AN&passengers=adults%3A1%2Cchildren%3A0%2Cinfantinlap%3AN&trip=oneway"

// Date formatter for 'yyyy-MM-dd'
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

// Get the current date
LocalDate today = LocalDate.now()

// Define the XPath for the warning message
String warningMessageXPath = "//div[text()='Try searching nearby airports or alternative dates']"
TestObject warningMessage = new TestObject().addProperty('xpath', ConditionType.EQUALS, warningMessageXPath)

// Check if the warning message is present
if (WebUI.verifyElementPresent(warningMessage, 5)) {
	println("Warning message detected. Searching for offers on future dates...")

	// Loop to generate and search for URLs with new dates for the next 20 days
	for (int i = 1; i <= 20; i++) { // Start with the next day
		// Add i days to the current date
		LocalDate futureDate = today.plusDays(i)

		// Format the date as 'yyyy-MM-dd'
		String formattedDate = futureDate.format(formatter)

		// Construct the full dynamic URL
		String dynamicUrl = baseUrl + formattedDate + suffixUrl

		// Navigate to the new URL
		WebUI.navigateToUrl(dynamicUrl)
		WebUI.delay(5) // Allow time for the page to load

		// Define the XPath for the offer listings on the new page
		String offerListingXPath = "//li[@data-test-id='offer-listing']"
		TestObject offerListing = new TestObject().addProperty('xpath', ConditionType.EQUALS, offerListingXPath)

		// Check if the offer listing is present
		if (WebUI.verifyElementPresent(offerListing, 5)) {
			println("Offer listing found for date: " + formattedDate)

			// Get the elements from the offer list
			List<WebElement> offerElements = WebUiCommonHelper.findWebElements(offerListing, 5)

			// Loop through each offer element and extract the hidden details (flight info, price, etc.)
			for (WebElement offer : offerElements) {
				String flightInfo = offer.getText()

				// Extract and format the flight details
				formatAndDisplayFlightInfo(flightInfo)
			}
			
		} else {
			println("No offers found for date: " + formattedDate + ". Trying the next date...")
		}
	}
} else {
	println("Warning message not detected. Extracting available listings...")

	// Extract the available listings from the current page
	String offerListingXPath = "//li[@data-test-id='offer-listing']"
	TestObject offerListing = new TestObject().addProperty('xpath', ConditionType.EQUALS, offerListingXPath)

	// Check if the offer listing is present
	if (WebUI.verifyElementPresent(offerListing, 5)) {
		println("Offer listing found on current page.")

		// Get the elements from the offer list
		List<WebElement> offerElements = WebUiCommonHelper.findWebElements(offerListing, 5)

		// Loop through each offer element and extract the hidden details (flight info, price, etc.)
		for (WebElement offer : offerElements) {
			String flightInfo = offer.getText()

			// Extract and format the flight details
			formatAndDisplayFlightInfo(flightInfo)
		}
		
	} else {
		println("No offers found on the current page.")
	}
}

// Method to format and display flight information
void formatAndDisplayFlightInfo(String flightInfo) {
	// Regex patterns to extract relevant details
	def flightNamePattern = /^(.*) flight/
	def departureTimePattern = /departing at (\d{2}:\d{2})/
	def arrivalTimePattern = /arriving at (\d{2}:\d{2})/
	def fromPattern = /from (.*)/
	def toPattern = /-(.*)\n/
	def pricePattern = /₹([\d,]+)/
	def totalTimePattern = /(\d+h \d+m).*total travel time/
	def flightTypePattern = /(Direct|Connecting)/

	// Extract details using regex matchers
	def flightName = (flightInfo =~ flightNamePattern) ? (flightInfo =~ flightNamePattern)[0][1] : "Not Available"
	def departureTime = (flightInfo =~ departureTimePattern) ? (flightInfo =~ departureTimePattern)[0][1] : "Not Available"
	def arrivalTime = (flightInfo =~ arrivalTimePattern) ? (flightInfo =~ arrivalTimePattern)[0][1] : "Not Available"
	def fromCity = (flightInfo =~ fromPattern) ? (flightInfo =~ fromPattern)[0][1].trim() : "Not Available"
	def toCity = (flightInfo =~ toPattern) ? (flightInfo =~ toPattern)[0][1].trim() : "Not Available"
	def price = (flightInfo =~ pricePattern) ? (flightInfo =~ pricePattern)[0][1] : "Not Available"
	def totalTime = (flightInfo =~ totalTimePattern) ? (flightInfo =~ totalTimePattern)[0][1] : "Not Available"
	def flightType = (flightInfo =~ flightTypePattern) ? (flightInfo =~ flightTypePattern)[0][1] : "Not Available"

	// Print the flight information in the required format
	println("Flight Name: " + flightName)
	println("Departing at: " + departureTime)
	println("Arriving at: " + arrivalTime)
	println("From: " + fromCity)
	println("To: " + toCity)
	println("Price: ₹" + price)
	println("Total Time: " + totalTime)
	println("Flight Type: " + flightType)
}
