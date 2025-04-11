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
import java.text.SimpleDateFormat
import com.kms.katalon.core.util.KeywordUtil

utils.openBrowserAndNavigateToPMT()

try {

    String EXCEL_PATH = './Data Files/TestData.xlsx'

    String TAB = 'Payment_Capture'

    List<List> testdata = getdata.fromExcel(EXCEL_PATH, TAB)

    for (int i = 0; i < testdata.size(); i++) {
        println(testdata.size())

		WebUI.callTestCase(findTestCase('Re-Usable Script/Payment_AuthorizePayment'), [:], FailureHandling.STOP_ON_FAILURE)
		
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement clickAuthorizePayment = driver.findElement(By.xpath('//a[normalize-space()=\'Capture payment\']')).click()
		
		utils.selectEnvironment(GlobalVariable.ENV)
		
		WebElement sendRequestTextBox = driver.findElement(By.xpath("//textarea[@name='req']"))
		sendRequestTextBox.clear()

        //get testData from excel to pass in Request
        String amount = (testdata['amount'])[i]
        
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
		
		String validation = (testdata['Validation'])[i]
		if (response.contains(validation) == true) {
			println validation
			
			} else {
				//Mark Failed status after this step
				KeywordUtil.markFailed("Expected response does not meet" + "Actual: " + response)
				String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
				String f = "./screenshots/Failed_CapturePayment" + timestamp + ".png"
				WebUI.takeScreenshot(f.toString())
			}
	
			println('Test Scenario Number: ' + (i + 1))

        //to cancel the bookingId after it runs
        RequestObject cancelBookingRequest = findTestObject('CancelBooking')
        ResponseObject cancelBookingResponse = WS.sendRequest(cancelBookingRequest)
        def cancelBookingJsonResponse = new JsonSlurper().parseText(cancelBookingResponse.getResponseText())
 
	}
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

