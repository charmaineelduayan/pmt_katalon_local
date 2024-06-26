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
WebElement Payments = driver.findElement(By.xpath('//h6[normalize-space()=\'Payments\']'))

Payments.click()
Function.offset()

// --- change the environment depending on the requirements ---//
Function.environment()

// --- get the test data from gsheet ---//
Request = WS.sendRequest(findTestObject('TestData/gsheet_Payment'))
paymentChannel = WS.getElementPropertyValue(Request, '[0].paymentChannel')
println('paymentChannel : ' + paymentChannel)
orderId = WS.getElementPropertyValue(Request, '[0].orderId')
println('orderId : ' + orderId)
type = WS.getElementPropertyValue(Request, '[0].type')
println('type : ' + type)
intent = WS.getElementPropertyValue(Request, '[0].intent')
println('intent : ' + intent)
bookingId = WS.getElementPropertyValue(Request, '[0].bookingId')
println('bookingId : ' + bookingId)
passengerId = WS.getElementPropertyValue(Request, '[0].passengerId')
println('passengerId : ' + passengerId)
officeCode = WS.getElementPropertyValue(Request, '[0].officeCode')
println('officeCode : ' + officeCode)
countryCode = WS.getElementPropertyValue(Request, '[0].countryCode')
println('countryCode : ' + countryCode)
currency = WS.getElementPropertyValue(Request, '[0].currency')
println('currency : ' + currency)
total = WS.getElementPropertyValue(Request, '[0].total')
println('total : ' + total)
paymentMethod_type = WS.getElementPropertyValue(Request, '[0].paymentMethod_type')
println('paymentMethod_type : ' + paymentMethod_type)
token = WS.getElementPropertyValue(Request, '[0].token')
println('token : ' + token)
expirationYear = WS.getElementPropertyValue(Request, '[0].expirationYear')
println('expirationYear : ' + expirationYear)
expirationMonth = WS.getElementPropertyValue(Request, '[0].expirationMonth')
println('expirationMonth : ' + expirationMonth)
cvv = WS.getElementPropertyValue(Request, '[0].cvv')
println('cvv : ' + cvv)
cardholderName = WS.getElementPropertyValue(Request, '[0].cardholderName')
println('cardholderName : ' + cardholderName)
addressOne = WS.getElementPropertyValue(Request, '[0].addressOne')
println('addressOne : ' + addressOne)
city = WS.getElementPropertyValue(Request, '[0].city')
println('city : ' + city)
state = WS.getElementPropertyValue(Request, '[0].state')
println('state : ' + state)
zipCode = WS.getElementPropertyValue(Request, '[0].zipCode')
println('zipCode : ' + zipCode)
//--- send the request to text box --- //
String AuthorizePayment  = """
{
    "paymentChannel": "${paymentChannel}",
    "orderId": "${orderId}",
    "type": "${type}",
    "intent": "${intent}",
    "items": [
        {
            "bookingId": "${bookingId}",
            "passengerId": "${passengerId}",
            "officeCode": "${officeCode}",
            "countryCode": "${countryCode}",
            "amount": {
                "currency": "${currency}",
                "total": ${total}
            }
        }
    ],
    "paymentMethod": {
        "type": "${paymentMethod_type}",
        "token": "${token}",
        "expirationYear": "${expirationYear}",
        "expirationMonth": "${expirationMonth}",
        "cvv": "${cvv}",
        "cardholder": "${cardholderName}",
        "billingAddress": {
            "addressOne": "${addressOne}",
            "city": "${city}",
            "state": "${state}",
        }
    }
}
"""


def restResponse = new JsonSlurper().parseText(AuthorizePayment)
def prettyJson = new groovy.json.JsonBuilder(restResponse).toPrettyString()
println(prettyJson)
Function.request().sendKeys(prettyJson)
Function.Submit()

// ---  this is where to get the response and validate the expected response ---//
String response_content = Function.response()

if (response_content.contains('orderId')) {
		} 
		else if (response_content.contains('status')) {
			}
		else if (response_content.contains('transactionId')) {
			}
		else if (response_content.contains('bookingIds')) {
			}
		else if (response_content.contains('authorizationCode')) {
			}
		else if (response_content.contains('controlSequenceNumber')) {
				System.out.println('Passed')
			}
		else {
				System.out.println('failed')
			}
			println(response_content)

Function.closeBrowser()