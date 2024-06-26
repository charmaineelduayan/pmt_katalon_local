import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import org.openqa.selenium.WebDriver as WebDriver
import org.openqa.selenium.By as By
import org.openqa.selenium.WebElement as WebElement
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import keyword.Function as Function
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

// ---  "Function" are reusable functionality created in include >> scripts >> groovy >> keyword >> Function ---//
// ---  Navigate to Web UI utilities browser
Function.openBrowser()

// ---  Open Libraries dropdown link ---//≈
Function.libraries()

WebDriver driver = DriverFactory.getWebDriver()

// ---  Navigating to Gift Cards Page ---//
WebElement Validations = driver.findElement(By.xpath('//h6[normalize-space()=\'Validations\']'))

Validations.click()

Function.offset()
WebElement Validation = driver.findElement(By.xpath('//a[normalize-space()=\'Prevalidations by Ctry/Curr\']'))
Validation.click()

// --- change the environment depending on the requirements ---//
Function.environment()


// --- get the test data from gsheet ---//
Request = WS.sendRequest(findTestObject('TestData/gsheet_Payment'))

// **Test Data is from Payment tab** //
country = WS.getElementPropertyValue(Request, '[0].countryCode')
println('country : ' + country)
currency = WS.getElementPropertyValue(Request, '[0].currency')
println('currency : ' + currency)

String Country  = "${country}"
String Currency  = "${currency}"

Function.countryTxt().sendKeys(Country)
Function.currencyTxt().sendKeys(Currency)

Function.Submit()

// ---  this is where to get the response and validate the expected response ---//
String response_content = Function.response()

if (response_content.contains('cards')) {
		}
		else if (response_content.contains('code')) {
			}
		else if (response_content.contains('description')) {
				System.out.println('Passed')
			}
		else {
				System.out.println('failed')
			}

Function.closeBrowser()
