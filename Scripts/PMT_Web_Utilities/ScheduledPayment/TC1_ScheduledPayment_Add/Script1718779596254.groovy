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

// *** Navigating to SCHEDULED PAYMENT Page ***//

WebDriver driver = DriverFactory.getWebDriver()
WebElement ScheduledPayment = driver.findElement(By.xpath('//h6[normalize-space()=\'Scheduled Payments\']'))
ScheduledPayment.click()
Function.offset()

WebElement Add = driver.findElement(By.xpath('//a[normalize-space()=\'Add\']'))
Add.click()

// *** change the KATALON environment depending on the requirements ***//
Function.environment()

// *** get the test data from gsheet: CARD TAB & BOOKING DETAILS TAB ***//
Request = WS.sendRequest(findTestObject('TestData/gsheet_Cards'))
Request1 = WS.sendRequest(findTestObject('TestData/gsheet_BookingDetails'))

//** CARDS
expirationYear = WS.getElementPropertyValue(Request, '[0].expirationYear')
println('expirationYear : ' + expirationYear)
expirationMonth = WS.getElementPropertyValue(Request, '[0].expirationMonth')
println('expirationMonth : ' + expirationMonth)
cardholderName = WS.getElementPropertyValue(Request, '[0].cardholderName')
println('cardholderName : ' + cardholderName)
addressOne = WS.getElementPropertyValue(Request, '[0].addressOne')
println('addressOne : ' + addressOne)
addressTwo = WS.getElementPropertyValue(Request, '[0].addressTwo')
println('addressOne : ' + addressOne)
city = WS.getElementPropertyValue(Request, '[0].city')
println('city : ' + city)
state = WS.getElementPropertyValue(Request, '[0].state')
println('state : ' + state)
zipCode = WS.getElementPropertyValue(Request, '[0].zipCode')
println('zipCode : ' + zipCode)
countryCode = WS.getElementPropertyValue(Request, '[0].countryCode')
println('countryCode : ' + countryCode)
total = WS.getElementPropertyValue(Request, '[0].total')
println('total : ' + total)

// ** BOOKING DETAILS
action = WS.getElementPropertyValue(Request1, '[0].action')
println('action : ' + action)
scheduleDate = WS.getElementPropertyValue(Request1, '[0].scheduleDate')
println('scheduleDate : ' + scheduleDate)
bookingId = WS.getElementPropertyValue(Request1, '[0].bookingId')
println('bookingId : ' + bookingId)
paymentChannel = WS.getElementPropertyValue(Request1, '[0].paymentChannel')
println('paymentChannel : ' + paymentChannel)
currency = WS.getElementPropertyValue(Request1, '[0].currency')
println('currency : ' + currency)
token = WS.getElementPropertyValue(Request1, '[0].token')
println('token : ' + token)

//*** send the request to text box ***//
String AddRequest  = """
{
  "bookingId": "${bookingId}",
  "channel": "${paymentChannel}",
  "currencyCode": "${currency}",
  "payments": [
    {
      "cardInfo": {
        "token": "${token}",
        "expirationMonth": "${expirationMonth}",
        "expirationYear": "${expirationYear}",
        "cardholderName": "${cardholderName}",
        "billingAddress": {
          "lineOne": "${addressOne}",
          "lineTwo": "",
          "city": "${city}",
          "state": "${state}",
          "zipCode": "${zipCode}",
          "countryCode": "${countryCode}"
        }
      },
      "paymentSchedule": [
        {
          "action": "${action}",
          "scheduleDate": "${scheduleDate}",
          "totalAmount": "${total}"
        }
      ]
    }
  ]
}
"""


def restResponse = new JsonSlurper().parseText(AddRequest)
def prettyJson = new groovy.json.JsonBuilder(restResponse).toPrettyString()
println(prettyJson)
Function.request().sendKeys(prettyJson)
Function.Submit()

// ---  this is where to get the response and validate the expected response ---//
String response_content = Function.response()

assert response_content != null
println(response_content)

Function.closeBrowser()