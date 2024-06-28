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
// *** "Function" are reusable functionality created in include >> scripts >> groovy >> keyword >> Function ***//

Function.openBrowser() // *** Navigate to Web UI utilities browser *** //

Function.libraries() //***  Open Libraries dropdown link ***//

// *** Navigating to PAYMENT Page ***//
WebDriver driver = DriverFactory.getWebDriver()
WebElement Payments = driver.findElement(By.xpath('//h6[normalize-space()=\'Payments\']'))
Payments.click()
Function.offset()
WebElement CapturePayment = driver.findElement(By.xpath('//a[normalize-space()=\'Capture payment\']'))
CapturePayment.click()

// *** change the KATALON environment depending on the requirements ***//
Function.environment()

// *** get the test data from gsheet: CARD TAB & BOOKING DETAILS TAB***//
Request = WS.sendRequest(findTestObject('TestData/gsheet_Cards'))
Request1 = WS.sendRequest(findTestObject('TestData/gsheet_BookingDetails'))

//** BOOKING DETAILS TAB
transactionId = WS.getElementPropertyValue(Request, '[0].transactionId')
println('transactionId : ' + transactionId)
bookingId = WS.getElementPropertyValue(Request, '[0].bookingId')
println('bookingId : ' + bookingId)

//** CARD TAB
amount = WS.getElementPropertyValue(Request1, '[0].total')
println('amount : ' + amount)

//*** send the request to text box ***//
String AuthorizePayment  = """
{
   "transactionId": "${transactionId}",
    "items": [
        {
            "bookingId": "${bookingId}",
            "amount": ${amount}
        }
    ]
}
"""

def restRequest = new JsonSlurper().parseText(AuthorizePayment)
def prettyJson = new groovy.json.JsonBuilder(restRequest).toPrettyString()
println(prettyJson)
Function.request().sendKeys(prettyJson)
Function.Submit()

// ***  this is where to get the response and validate the expected response from Web ***//
String response_content = Function.response()

assert response_content.contains('orderId')
println(response_content)
//if (response_content.contains('orderId')) {
//		} 
//		else if (response_content.contains('status')) {
//			}
//		else if (response_content.contains('transactionId')) {
//			}
//		else if (response_content.contains('bookingIds')) {
//			}
//		else if (response_content.contains('authorizationCode')) {
//			}
//		else if (response_content.contains('controlSequenceNumber')) {
//				System.out.println('Passed')
//			}
//		else {
//				System.out.println('failed')
//			}
//			println(response_content)

Function.closeBrowser()