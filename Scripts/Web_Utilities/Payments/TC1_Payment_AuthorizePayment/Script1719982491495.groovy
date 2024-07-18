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


try {
	utils.openBrowserAndNavigateToPMT()
	WebDriver driver = DriverFactory.getWebDriver()
	utils.goToPayments()
	
	WebElement clickAuthorizePayment = driver.findElement(By.xpath("//a[normalize-space()='Authorize payment']")).click()
	
	final String EXCEL_PATH = "./Data Files/TestData.xlsx"
	final String TAB = "Payment_Authorize"
	
	utils.selectEnvironment(GlobalVariable.ENV)
	
	List<List<Object>> testdata = getdata.fromExcel(EXCEL_PATH,TAB)
	for(int TestScenarioNumber = 0; TestScenarioNumber < testdata.size(); TestScenarioNumber++) { 

		
		//createBooking
		def getBookingData = CreateAndRetrieveBooking.Data(EXCEL_PATH, TAB, TestScenarioNumber)
		println(getBookingData["BookingId"])
		println(getBookingData["BookingAccessToken"])
		
		WebUI.delay(5)
		
		//Get Add Request from Execel and execute
		String bookingIdCol = testdata["bookingId"][TestScenarioNumber]
		String BookingId = getBookingData["BookingId"]
		String replaceBookingId = bookingIdCol.replace("BookingId",BookingId)	//this replaces the word "BookingId" in ADDRequest with the value from getBookingData["BookingId"]
		//sendRequestTextBox.sendKeys(replaceBookingId)
		println(replaceBookingId)
		
		String passengerIdCol = testdata["passengerId"][TestScenarioNumber]
		String PassengerId = getBookingData["PassengerId"]
		String replacePassengerId = passengerIdCol.replace("PassengerId",PassengerId)
		
		println(replacePassengerId)
		
		WebElement sendRequestTextBox = driver.findElement(By.xpath("//textarea[@name='req']"))
		sendRequestTextBox.clear()
		
		//get testData from excel to pass in Request
		String paymentChannel = testdata["paymentChannel"][TestScenarioNumber]
		String orderId = testdata["orderId"][TestScenarioNumber]
		String type = testdata["type"][TestScenarioNumber]
		String intent = testdata["intent"][TestScenarioNumber]
		String officeCode = testdata["officeCode"][TestScenarioNumber]
		String countryCode = testdata["countryCode"][TestScenarioNumber]
		String currency = testdata["currency"][TestScenarioNumber]
		String total = testdata["total"][TestScenarioNumber]
		String paymentType = testdata["paymentType"][TestScenarioNumber]	
		String cardNumber = testdata["token"][TestScenarioNumber]
		CNumber = cardNumber.replaceAll(/\.0$/,'')
		String expirationMonth = testdata["expirationMonth"][TestScenarioNumber]
		xMonth = expirationMonth.replaceAll(/\.0$/,'')
		String expirationYear = testdata["expirationYear"][TestScenarioNumber]
		xYear = expirationYear.replaceAll(/\.0$/,'')
		String cvv = testdata["cvv"][TestScenarioNumber]
		String cardholderName = testdata["cardholderName"][TestScenarioNumber]
		String addressOne = testdata["addressOne"][TestScenarioNumber]
		String city = testdata["city"][TestScenarioNumber]
		String state = testdata["state"][TestScenarioNumber]
		String zipCode = testdata["zipCode"][TestScenarioNumber]
		
		String request =
		"""{
		"paymentChannel": "${paymentChannel}",
		"orderId": "${orderId}",
		"type": "${type}",
		"intent": "${intent}",
		"items": [
			{
            "bookingId": "${replaceBookingId}",
            "passengerId": "${replacePassengerId}",
            "officeCode": "${officeCode}",
            "countryCode": "${countryCode}",
            "amount": {
                "currency": "${currency}",
                "total": ${total}
           	 	}
			}
			],
			    "paymentMethod": {
			        "type": "${paymentType}",
			        "token": "${cardNumber}",
			        "expirationYear": "${expirationYear}",
			        "expirationMonth": "${expirationMonth}",
			        "cvv": "${cvv}",
			        "cardholder": "${cardholderName}",
			        "billingAddress": {
			            "addressOne": "${addressOne}",
			            "city": "${city}",
			            "state": "${state}",
			            "zipCode": "${zipCode}"
			        }
			    }
			}"""
		def restRequest = new JsonSlurper().parseText(request)
		def prettyJson = new groovy.json.JsonBuilder(restRequest).toPrettyString()
		println(prettyJson)
		sendRequestTextBox.sendKeys(prettyJson)
		utils.clickSendButton()
		
		WebUI.delay(3)
		String response = utils.getResponse()
		println response
		
//		RequestObject cancelBookingRequest = findTestObject('CancelBooking')
//		ResponseObject cancelBookingResponse = WS.sendRequest(cancelBookingRequest)
//		def cancelBookingJsonResponse = new JsonSlurper().parseText(cancelBookingResponse.getResponseText())
//		println(cancelBookingJsonResponse)
	
		String validation1 = testdata["ContainsValidation"][TestScenarioNumber]
		println validation1
		String validation2 = testdata["NotContainsValidation"][TestScenarioNumber]
		println validation2
		println(testdata["TCNumber"][TestScenarioNumber])
		assert response.contains(validation1)
		assert response.contains(validation2) == false
	}
	
		
}
catch (AssertionError e) {
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




