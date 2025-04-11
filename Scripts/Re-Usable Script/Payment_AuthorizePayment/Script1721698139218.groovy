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
import com.kms.katalon.core.util.KeywordUtil

utils.openBrowserAndNavigateToPMT()
WebDriver driver = DriverFactory.getWebDriver()
utils.goToPayments()
WebElement clickAuthorizePayment = driver.findElement(By.xpath('//a[normalize-space()=\'Authorize payment\']')).click()


	final String EXCEL_PATH = "./Data Files/TestData.xlsx"
	final String TAB = "Payment_Authorize"
	
	utils.selectEnvironment(GlobalVariable.ENV)
	
	List<List<Object>> testdata = getdata.fromExcel(EXCEL_PATH,TAB)
	for(int i = 0; i < testdata.size(); i++) { 
		String TestScenarioRequiresCreateBooking = (testdata['ExecuteBookingCreationFlag'])[i]
		
		if (TestScenarioRequiresCreateBooking == 'Yes') {
			WebElement sendRequestTextBox = driver.findElement(By.xpath("//textarea[@name='req']"))
			sendRequestTextBox.clear()
			
			//createBooking to get the bookingId and passengerId
			def getBookingData = CreateAndRetrieveBooking.Data(EXCEL_PATH, TAB, i)
			println(getBookingData["BookingId"])
			println(getBookingData["BookingAccessToken"])
		
			WebUI.delay(5)
			
			//Replace "BookingId" value from bookingId column in excel sheet
			String bookingIdCol = testdata["bookingId"][i]
			String BookingId = getBookingData["BookingId"]
			String replaceBookingId = bookingIdCol.replace("BookingId",BookingId)
			println(replaceBookingId)
			
			//Replace "PassengerId" value from passengerId in excel sheet
			String passengerIdCol = testdata["passengerId"][i]
			String PassengerId = getBookingData["PassengerId"]
			String replacePassengerId = passengerIdCol.replace("PassengerId",PassengerId)
			println(replacePassengerId)
			
			//get testData from excel to pass in Request
			String paymentChannel = testdata["paymentChannel"][i]
			String orderId = testdata["orderId"][i]
			String type = testdata["type"][i]
			String intent = testdata["intent"][i]
			String officeCode = testdata["officeCode"][i]
			String countryCode = testdata["countryCode"][i]
			String currency = testdata["currency"][i]
			String total = testdata["total"][i]
			String paymentType = testdata["paymentType"][i]	
			String cardNumber = testdata["token"][i]
			String expirationMonth = testdata["expirationMonth"][i]
			String expirationYear = testdata["expirationYear"][i]
			String cvv = testdata["cvv"][i]
			String cardholderName = testdata["cardholderName"][i]
			String addressOne = testdata["addressOne"][i]
			String city = testdata["city"][i]
			String state = testdata["state"][i]
			String zipCode = testdata["zipCode"][i]
		
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
		
            String validation = (testdata['Validation'])[i]
				if (response.contains(validation) == true) {
					println validation
					
				} else {
					//Mark Failed status after this step
					KeywordUtil.markFailed("Expected response does not meet" + "Actual: " + response)
					String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
					String f = "./screenshots/Failed_AuthorizePayment" + timestamp + ".png"
					WebUI.takeScreenshot(f.toString())
				}
			
            println('Test Scenario Number: ' + (i + 1)) //for checking what test scenario number the running stops if failure occurs (+ 1 because the for loop index starts with 0)
			
		}
		else {

			String BookingIdToBePassed = (testdata['BookingId'])[i]
			sendRequestTextBox.sendKeys(BookingIdToBePassed)
			println(BookingIdToBePassed)
			utils.clickSendButton()

			WebUI.delay(2)

			String response = utils.getResponse()
			String validation = (testdata['Validation'])[i]
				if (response.contains(validation) == true) {
					println validation
					
				} else {
					//Mark Failed status after this step
					KeywordUtil.markFailed("Expected response does not meet" + "Actual: " + response)
					String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
					String f = "./screenshots/Failed_AuthorizePayment" + timestamp + ".png"
					WebUI.takeScreenshot(f.toString())
				}
			println('Test Scenario Number: ' + (i))
		}
	}



