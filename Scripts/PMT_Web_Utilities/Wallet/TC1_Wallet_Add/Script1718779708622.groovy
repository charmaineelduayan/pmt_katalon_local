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

// *** Navigating to WALLET Page ***//
WebDriver driver = DriverFactory.getWebDriver()
WebElement Wallet = driver.findElement(By.xpath('//h6[normalize-space()=\'Wallet\']'))
Wallet.click()
Function.offset()
WebElement Add = driver.findElement(By.xpath('//a[normalize-space()=\'Add\']'))
Add.click()

// *** change the KATALON environment depending on the requirements ***//
Function.environment()

// *** get the test data from gsheet: GUESTACCOUNT & CARDS ***//
Request = WS.sendRequest(findTestObject('TestData/gsheet_Cards'))
fetchRes = WS.sendRequest(findTestObject('TestData/GuestAccount'))

// **CARDS
paymentMethod_type = WS.getElementPropertyValue(Request, '[0].paymentMethod_type').toLowerCase()
println('paymentMethod_type : ' + paymentMethod_type)
cardholderName = WS.getElementPropertyValue(Request, '[0].cardholderName')
println('cardholderName : ' + cardholderName)
cardNumber = WS.getElementPropertyValue(Request, '[0].cardNumber')
println('cardNumber : ' + cardNumber)
expirationYear = WS.getElementPropertyValue(Request, '[0].expirationYear')
println('expirationYear : ' + expirationYear)
expirationMonth = WS.getElementPropertyValue(Request, '[0].expirationMonth')
println('expirationMonth : ' + expirationMonth)
defaultPaymentMethod = WS.getElementPropertyValue(Request, '[0].defaultPaymentMethod')
println('defaultPaymentMethod : ' + defaultPaymentMethod)
// **GuestAccount
accountId = WS.getElementPropertyValue(fetchRes, 'payload.accountId')
println('accountId : ' + accountId)
accessToken = WS.getElementPropertyValue(fetchRes, 'payload.accessToken')
println('accessToken : ' + accessToken)

//*** send the request to text box ***//
String AddRequest  = """
{
    "paymentMethod": {
        "type": "${paymentMethod_type}",
        "cardNumber": "${cardNumber}",
        "expirationMonth": "${expirationMonth}",
        "expirationYear": "${expirationYear}",
        "cardholder": "${cardholderName}",
        "nickname": "${cardholderName}",
        "defaultPaymentMethod": "${defaultPaymentMethod}"
    },
    "accountId": "${accountId}",
    "accessToken": "${accessToken}"
}
"""


def restResponse = new JsonSlurper().parseText(AddRequest)
def prettyJson = new groovy.json.JsonBuilder(restResponse).toPrettyString()
println(prettyJson)
Function.request().sendKeys(prettyJson)
Function.Submit()

// ***  this is where to get the response and validate the expected response from Web ***//
String response_content = Function.response()

assert response_content.contains('paymentMethod')
println(response_content)

Function.closeBrowser()