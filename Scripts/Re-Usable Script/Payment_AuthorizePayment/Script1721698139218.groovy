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
import java.text.SimpleDateFormat


WebDriver driver = DriverFactory.getWebDriver()
utils.goToPayments()
WebElement clickAuthorizePayment = driver.findElement(By.xpath('//a[normalize-space()=\'Authorize payment\']')).click()


	final String EXCEL_PATH = "./Data Files/TestData.xlsx"
	final String TAB = "Payment_Authorize"
	
	utils.selectEnvironment(GlobalVariable.ENV)
	
	List<List<Object>> testdata = getdata.fromExcel(EXCEL_PATH,TAB)
	for(int TestScenarioNumber = 0; TestScenarioNumber < testdata.size(); TestScenarioNumber++) { 
		println(testdata.size())

		//createBooking to get the bookingId and passengerId
		def getBookingData = CreateAndRetrieveBooking.Data(EXCEL_PATH, TAB, TestScenarioNumber)
		println(getBookingData["BookingId"])
		println(getBookingData["BookingAccessToken"])
	
		WebUI.delay(5)
		
		//Replace "BookingId" value from bookingId column in excel sheet
		String bookingIdCol = testdata["bookingId"][TestScenarioNumber]
		String BookingId = getBookingData["BookingId"]
		String replaceBookingId = bookingIdCol.replace("BookingId",BookingId)
		println(replaceBookingId)
		
		//Replace "PassengerId" value from passengerId in excel sheet
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
		String expirationMonth = testdata["expirationMonth"][TestScenarioNumber]
		String expirationYear = testdata["expirationYear"][TestScenarioNumber]
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
		
		def res = new JsonSlurper().parseText(response)
		def resJson = new groovy.json.JsonBuilder(res).toPrettyString()
		println(resJson)
		
		GlobalVariable.AuthorizePaymentResponse = res
		String transactionId = res.transactionId
		println(transactionId)
		String BKID = res.bookingIds[0]
		
		GlobalVariable.transactionId = transactionId
		GlobalVariable.BKID = BKID
		
	
		String validation1 = testdata["ContainsValidation"][TestScenarioNumber]
		println validation1
		String validation2 = testdata["NotContainsValidation"][TestScenarioNumber]
		println validation2
		println(testdata["TCNumber"][TestScenarioNumber])
		assert response.contains(validation1)
		assert response.contains(validation2) == false
		
		if (response.contains(validation1) == false || response.contains(validation2) == true) {
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
			String f = "./screenshots/Failed_Wallet_Add" + timestamp + ".png"
			WebUI.takeScreenshot(f.toString())
			println("Assertion failed")
		}
		
		println("Test Scenario Number: " + TestScenarioNumber) //for checking what test scenario number the running stops if failure occurs
		
		}



