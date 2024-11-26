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
import org.openqa.selenium.Keys
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import groovy.json.*
import rcclpayment.utils
import rcclpayment.getdata
import rcclpayment.CreateAndRetrieveBooking

utils.openBrowserAndNavigateToPMT()
try {
	String EXCEL_PATH = './Data Files/TestData.xlsx'
    String TAB = 'ScheduledPayment_Add'

    List<List> testdata = getdata.fromExcel(EXCEL_PATH, TAB)
	
	    for (int TestScenarioNumber = 0; TestScenarioNumber < testdata.size(); TestScenarioNumber++) {
		WebUI.callTestCase(findTestCase('Re-Usable Script/SchedulePayment_ConfirmWithRequest'), [:], FailureHandling.STOP_ON_FAILURE)


        WebDriver driver = DriverFactory.getWebDriver()
        WebElement clickADD = driver.findElement(By.xpath("//a[normalize-space()='Add']")).click()
        utils.selectEnvironment(GlobalVariable.ENV)
        WebElement sendRequestTextBox = driver.findElement(By.xpath('//textarea[@name=\'req\']'))

        sendRequestTextBox.clear()

        //get testData from excel to pass in Request
        String cardNumber = (testdata['cardNumber'])[TestScenarioNumber]
        CNumber = cardNumber.replaceAll('\\.0$', '')
        String expirationMonth = (testdata['expirationMonth'])[TestScenarioNumber]
        xMonth = expirationMonth.replaceAll('\\.0$', '')
        String expirationYear = (testdata['expirationYear'])[TestScenarioNumber]
        xYear = expirationYear.replaceAll('\\.0$', '')
        String cardholderName = (testdata['cardholderName'])[TestScenarioNumber]
        String addressOne = (testdata['addressOne'])[TestScenarioNumber]
        String addressTwo = (testdata['addressTwo'])[TestScenarioNumber]
        String city = (testdata['city'])[TestScenarioNumber]
        String state = (testdata['state'])[TestScenarioNumber]
        String zipCode = (testdata['zipCode'])[TestScenarioNumber]
        String countryCode = (testdata['countryCode'])[TestScenarioNumber]
        String action = (testdata['action'])[TestScenarioNumber]
        String scheduledDate = (testdata['scheduledDate'])[TestScenarioNumber]
        xscheduledDate = scheduledDate.replaceAll('\\.0$', '')
        String totalAmount = (testdata['totalAmount'])[TestScenarioNumber]
        String amount1 = (testdata['amount1'])[TestScenarioNumber]
        String amount2 = (testdata['amount2'])[TestScenarioNumber]
		
	    String request = """{
			    "cardInfo": {
			        "cardNumber": "$cardNumber",
			        "expirationMonth": "$expirationMonth",
			        "expirationYear": "$expirationYear",
			        "cardholderName": "$cardholderName",
			        "billingAddress": {
			            "lineOne": "$addressOne",
			            "lineTwo": "$addressTwo",
			            "city": "$city",
			            "state": "$state",
			            "zipCode": "$zipCode",
			            "countryCode": "$countryCode"
			        }
			    },
			    "scheduledPayments": [
			        {
			            "scheduledDate": "$scheduledDate",
			            "totalAmount": $totalAmount,
			            "passengerAmountAllocation": [
			                {
			                    "passengerId": "$GlobalVariable.GuestRefNumber1",
			                    "amount": $amount1
			                },
			                {
			                    "passengerId": "$GlobalVariable.GuestRefNumber2",
			                    "amount": $amount2
			                }
			            ]
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
		
		String validation1 = testdata["ContainsValidation"][TestScenarioNumber]
		println validation1
		String validation2 = testdata["NotContainsValidation"][TestScenarioNumber]
		println validation2
		println(testdata["TestScenarioNumber"][TestScenarioNumber])
		assert response.contains(validation1) 
		assert response.contains(validation2) == false
   	}	
}
catch (AssertionError e) {
	WebUI.takeScreenshot("./screenshots/Failed_Scheduled_Add.png")
	println("Assertion failed: ${e.message}")
	e.printStackTrace()
}
catch (org.openqa.selenium.NoSuchElementException e) {
	println("Element not found: ${e.message}")
	e.printStackTrace()
}
catch (Exception e) {
	println("An unexpected error occurred: ${e.message}")
	e.printStackTrace()
}
finally {
	utils.closeBrowser()
}



