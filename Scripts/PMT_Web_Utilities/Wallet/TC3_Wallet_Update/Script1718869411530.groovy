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
WebElement Wallet = driver.findElement(By.xpath('//h6[normalize-space()=\'Wallet\']'))

Wallet.click()

Function.offset()
WebElement Replace = driver.findElement(By.xpath('//a[normalize-space()=\'Update\']'))
Replace.click()

// --- change the environment depending on the requirements ---//
Function.environment()

// --- get the test data from gsheet ---//
Request = WS.sendRequest(findTestObject('TestData/gsheet_Payment'))
Request2 = WS.sendRequest(findTestObject('TestData/gsheet_Cards'))
fetchRes = WS.sendRequest(findTestObject('TestData/GuestAccount'))

// **Test Data is from Payment tab** //
paymentMethod_type = WS.getElementPropertyValue(Request, '[0].paymentMethod_type').toLowerCase()
println('paymentMethod_type : ' + paymentMethod_type)
cardholderName = WS.getElementPropertyValue(Request, '[0].cardholderName')
println('cardholderName : ' + cardholderName)

// **Test Data is from Cards tab** //

cardNumber = WS.getElementPropertyValue(Request2, '[0].cardNumber')
println('cardNumber : ' + cardNumber)
expirationYear = WS.getElementPropertyValue(Request2, '[0].expirationYear')
println('expirationYear : ' + expirationYear)
expirationMonth = WS.getElementPropertyValue(Request2, '[0].expirationMonth')
println('expirationMonth : ' + expirationMonth)
defaultPaymentMethod = WS.getElementPropertyValue(Request2, '[0].defaultPaymentMethod')
println('defaultPaymentMethod : ' + defaultPaymentMethod)

// **Test Data is from GuestAccount ** //
accountId = WS.getElementPropertyValue(fetchRes, 'payload.accountId')
println('accountId : ' + accountId)
accessToken = WS.getElementPropertyValue(fetchRes, 'payload.accessToken')
println('accessToken : ' + accessToken)

//--- send the request to text box --- //
String AddRequest  = 
"""{
    "type": "${paymentMethod_type}",
    "cardNumber": "${cardNumber}",
    "cardholder": "${cardholderName}",
    "nickname": "${cardholderName}",
    "expirationMonth": "${expirationMonth}",
    "expirationYear": "${expirationYear}",
    "defaultPaymentMethod": "${defaultPaymentMethod}",
    "accountId": "${accountId}",
    "accessToken": "${accessToken}"
}"""



def restResponse = new JsonSlurper().parseText(AddRequest)
def prettyJson = new groovy.json.JsonBuilder(restResponse).toPrettyString()
println(prettyJson)
Function.request().sendKeys(prettyJson)
Function.Submit()

//// ---  this is where to get the response and validate the expected response ---//
String response_content = Function.response()

assert response_content.contains('paymentMethod')
println(response_content)

Function.closeBrowser()