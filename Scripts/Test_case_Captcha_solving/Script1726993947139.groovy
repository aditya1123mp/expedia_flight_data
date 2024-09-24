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
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.ConditionType as ConditionType

// Initial delay to allow the page to load completely
WebUI.delay(5)

// Check for CAPTCHA presence based on the heading
String captchaHeadingXpath = "//h2[contains(text(), 'Show us your human side')]"
TestObject captchaHeading = new TestObject().addProperty('xpath', ConditionType.EQUALS, captchaHeadingXpath)

println("Checking if CAPTCHA is present...")

boolean captchaAppeared = WebUI.verifyElementPresent(captchaHeading, 10, FailureHandling.OPTIONAL)

if (captchaAppeared) {
    println("CAPTCHA appeared. Waiting for it to be solved manually.")

    // Wait for the iframe to disappear, meaning CAPTCHA is solved
    String captchaIframeXpath = "//iframe[@title='Verification challenge']"
    TestObject captchaIframe = new TestObject().addProperty('xpath', ConditionType.EQUALS, captchaIframeXpath)

    int maxAttempts = 36  // Set a limit to the number of attempts (36 * 5 sec = 1 minute)
    int attempts = 0
    boolean captchaSolved = false

    while (attempts < maxAttempts) {
        if (!WebUI.verifyElementPresent(captchaIframe, 5, FailureHandling.OPTIONAL)) {
            captchaSolved = true
            break
        }
        WebUI.delay(5)  // Continue waiting for CAPTCHA to be solved
        attempts++
        println("Waiting for CAPTCHA to be solved... (Attempt " + attempts + ")")
    }

    if (captchaSolved) {
        println("CAPTCHA solved. Proceeding with the main flow.")
        // Call the dummy test case after CAPTCHA is solved
        WebUI.callTestCase(findTestCase('Test_Case_to_extract_data_and_save_in_txt_file'), [:], FailureHandling.STOP_ON_FAILURE)
    } else {
        KeywordUtil.markFailed("CAPTCHA iframe is still present after waiting for 1 minute.")
    }
} else {
    println("CAPTCHA did not appear. Continuing with the script.")
    // Call the dummy test case if CAPTCHA did not appear
    WebUI.callTestCase(findTestCase('Test_Case_to_extract_data_and_save_in_txt_file'), [:], FailureHandling.STOP_ON_FAILURE)
}
