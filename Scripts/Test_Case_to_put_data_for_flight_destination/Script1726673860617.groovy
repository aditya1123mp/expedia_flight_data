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

WebUI.click(findTestObject('Object Repository/select_flight_option'))

WebUI.delay(1)

WebUI.click(findTestObject('Object Repository/oneway_button'))

WebUI.delay(1)

//******************************************************* leaving_from ***************************************//

WebUI.click(findTestObject('Object Repository/leaving_from_text_box'))

WebUI.delay(1)


WebUI.click(findTestObject('Object Repository/leaving_from_part_two'))

WebUI.delay(1)



WebUI.setText(findTestObject('Object Repository/leaving_from_part_two'), GlobalVariable.leaving_from)

WebUI.delay(1)


WebUI.click(findTestObject('Object Repository/selecting_first_option'))

WebUI.delay(1)

//******************************************************* going_to ***************************************//

WebUI.click(findTestObject('Object Repository/going_to_text_box'))

WebUI.delay(1)

WebUI.click(findTestObject('Object Repository/going_to_part_two'))

WebUI.delay(1)

WebUI.setText(findTestObject('Object Repository/going_to_part_two'), GlobalVariable.going_to)

WebUI.delay(1)

WebUI.click(findTestObject('Object Repository/selecting_first_option'))

WebUI.delay(1)

//******************************************************* selecting_date_test_case ***************************************//

WebUI.callTestCase(findTestCase('selecting_date_test_case'), [:], FailureHandling.STOP_ON_FAILURE)

//WebUI.delay(2)

//WebUI.click(findTestObject('Object Repository/search_button'))

