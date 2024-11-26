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
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import org.openqa.selenium.By as By
import org.openqa.selenium.WebElement as WebElement
import org.openqa.selenium.WebDriver as WebDriver
import com.kms.katalon.core.testobject.RequestObject as RequestObject
import com.kms.katalon.core.testobject.ResponseObject as ResponseObject
import groovy.json.*
import rcclpayment.utils as utils
import rcclpayment.getdata as getdata
import rcclpayment.CreateAndRetrieveBooking as CreateAndRetrieveBooking

utils.openBrowserAndNavigateToPMT()

try {

    String EXCEL_PATH = './Data Files/TestData.xlsx'

    String TAB = 'Payment_Capture'

    List<List> testdata = getdata.fromExcel(EXCEL_PATH, TAB)

    for (int TestScenarioNumber = 0; TestScenarioNumber < testdata.size(); TestScenarioNumber++) {
        println(testdata.size())

		WebUI.callTestCase(findTestCase('Re-Usable Script/Payment_AuthorizePayment'), [:], FailureHandling.STOP_ON_FAILURE)
		
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement clickAuthorizePayment = driver.findElement(By.xpath('//a[normalize-space()=\'Capture payment\']')).click()
		utils.selectEnvironment(GlobalVariable.ENV)
        //createBooking to get the bookingId and passengerId
		
		WebElement sendRequestTextBox = driver.findElement(By.xpath("//textarea[@name='req']"))
		sendRequestTextBox.clear()

        //get testData from excel to pass in Request
        String amount = (testdata['amount'])[TestScenarioNumber]
        
		String request = 
		"""{
         "transactionId": "${GlobalVariable.transactionId}",
          "items": [
              {
                  "bookingId": "${GlobalVariable.BKID}",
                  "amount": 10
              }
          ]
      }"""
		def restRequest = new JsonSlurper().parseText(request)
		def prettyJson = new groovy.json.JsonBuilder(restRequest).toPrettyString()
		println(prettyJson)
		sendRequestTextBox.sendKeys(prettyJson)
		utils.clickSendButton()
		
        WebUI.delay(3)

        String response = utils.getResponse()
        println(response)

        //to cancel the bookingId after it runs
        RequestObject cancelBookingRequest = findTestObject('CancelBooking')
        ResponseObject cancelBookingResponse = WS.sendRequest(cancelBookingRequest)
        def cancelBookingJsonResponse = new JsonSlurper().parseText(cancelBookingResponse.getResponseText())
        println(cancelBookingJsonResponse)
        String validation1 = (testdata['ContainsValidation'])[TestScenarioNumber]
        println(validation1)
        String validation2 = (testdata['NotContainsValidation'])[TestScenarioNumber]
        println(validation2)
        println((testdata['TCNumber'])[TestScenarioNumber])
        
		assert response.contains(validation1)
        assert response.contains(validation2) == false

        println('Test Scenario Number: ' + TestScenarioNumber //for checking what test scenario number the running stops if failure occurs
            )
    }
}
catch (AssertionError e) {
	WebUI.takeScreenshot("./screenshots/Failed_Payment_CapturePayment.png")
	println("Assertion failed: ${e.message}")
	e.printStackTrace()
} 
catch (org.openqa.selenium.NoSuchElementException e) {
    println("Element not found: $e.message")

    e.printStackTrace()
} 
catch (Exception e) {
    println("An unexpected error occurred: $e.message")

    e.printStackTrace()
} 
finally { 
    utils.closeBrowser()
}

