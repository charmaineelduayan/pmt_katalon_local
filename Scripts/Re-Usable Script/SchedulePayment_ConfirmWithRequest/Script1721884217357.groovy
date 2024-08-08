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

WebDriver driver = DriverFactory.getWebDriver()
utils.goToScheduledPayments()
WebElement getConfrimWithReq = driver.findElement(By.xpath('//a[normalize-space()=\'Confirm with Request\']')).click()

	final String EXCEL_PATH = "./Data Files/TestData.xlsx"
	final String TAB = "ScheduledPayment_ConfirmWithReq"
	
	utils.selectEnvironment(GlobalVariable.ENV)
	
	List<List<Object>> testdata = getdata.fromExcel(EXCEL_PATH,TAB)
	for(int TestScenarioNumber = 0; TestScenarioNumber < testdata.size(); TestScenarioNumber++) {
		println(testdata.size())

		//createBooking to get the bookingId and passengerId
		def getBookingData = CreateAndRetrieveBooking.Data(EXCEL_PATH, TAB, TestScenarioNumber)
		println(getBookingData["PassengerId1"])
		println(getBookingData["PassengerId2"])
		
		//Replace "BookingId" value from bookingId column in excel sheet
		String bookingIdCol = testdata["bookingId"][TestScenarioNumber]
		String BookingId = getBookingData["BookingId"]
		String replaceBookingId = bookingIdCol.replace("BookingId",BookingId)
		println(replaceBookingId)
		
//		//Replace "PassengerId1" value from passengerId1 in excel sheet
//		String passengerIdCol = testdata["passengerId1"][TestScenarioNumber]
//		String PassengerId1 = getBookingData["passengerId"]
//		String replacePassengerId1 = passengerIdCol.replace("passengerId1",PassengerId1)
//		println(replacePassengerId1)
//		
//		//Replace "PassengerId2" value from passengerId2 in excel sheet
//		String passengerIdCol2 = testdata["passengerId2"][TestScenarioNumber]
//		String PassengerId2 = getBookingData["passengerId"]
//		String replacePassengerId2 = passengerIdCol2.replace("passengerId2",PassengerId2)
//		println(replacePassengerId2)
//		
//		WebElement sendRequestTextBox = driver.findElement(By.xpath("//textarea[@name='req']"))
//		sendRequestTextBox.clear()
//		
//		//get testData from excel to pass in Request
//		String cardNumber = testdata["token"][TestScenarioNumber]
//		CNumber = cardNumber.replaceAll(/\.0$/,'')
//		String expirationMonth = testdata["expirationMonth"][TestScenarioNumber]
//		xMonth = expirationMonth.replaceAll(/\.0$/,'')
//		String expirationYear = testdata["expirationYear"][TestScenarioNumber]
//		xYear = expirationYear.replaceAll(/\.0$/,'')
//		String cardholderName = testdata["cardholderName"][TestScenarioNumber]
//		String addressOne = testdata["addressOne"][TestScenarioNumber]
//		String addressTwo = testdata["addressTwo"][TestScenarioNumber]
//		String city = testdata["city"][TestScenarioNumber]
//		String state = testdata["state"][TestScenarioNumber]
//		String zipCode = testdata["zipCode"][TestScenarioNumber]
//		String countryCode = testdata["countryCode"][TestScenarioNumber]
//		String action = testdata["action"][TestScenarioNumber]
//		String scheduledDate = testdata["scheduledDate"][TestScenarioNumber]
//		xscheduledDate = scheduledDate.replaceAll(/\.0$/,'')
//		String totalAmount = testdata["totalAmount"][TestScenarioNumber]
//		String amount1 = testdata["amount1"][TestScenarioNumber]
//		String amount2 = testdata["amount2"][TestScenarioNumber]
//				
//		String request =
//		"""{	
//		  "payments": [
//		    {
//		      "cardInfo": {
//		        "cardNumber": "${cardNumber}",
//		        "expirationMonth": "${expirationMonth}",
//		        "expirationYear": "${expirationYear}",
//		        "cardholderName": "${cardholderName}",
//		        "billingAddress": {
//		          "lineOne": "${addressOne}",
//		          "lineTwo": "${addressTwo}",
//		          "city": "${city}",
//		          "state": "${state}",
//		          "zipCode": "${zipCode}",
//		          "countryCode": "${countryCode}"
//		        }
//		      },
//		      "scheduledPayments": [
//		        {
//		          "action": "${action}",
//		          "scheduledDate": "${scheduledDate}",
//		          "totalAmount": ${totalAmount},
//		          "passengerAmountAllocation": [
//		            {
//		              "passengerId": "${replacePassengerId1}",
//		              "amount": ${amount1}
//		            },
//		            {
//		              "passengerId": "${replacePassengerId2}",
//		              "amount": ${amount2}
//		            }
//		          ]
//		        }
//		      ]
//		    }
//		  ]
//		}"""
//		def restRequest = new JsonSlurper().parseText(request)
//		def prettyJson = new groovy.json.JsonBuilder(restRequest).toPrettyString()
//		println(prettyJson)
//		sendRequestTextBox.sendKeys(prettyJson)
//		utils.clickSendButton()
//		
//		WebUI.delay(3)
//		String response = utils.getResponse()
//		println response
//		
//		def res = new JsonSlurper().parseText(response)
//		def resJson = new groovy.json.JsonBuilder(res).toPrettyString()
//		println(resJson)
//		
////		GlobalVariable.AuthorizePaymentResponse = res
////		String transactionId = res.transactionId
////		println(transactionId)
////		String BKID = res.bookingIds[0]
////		
////		GlobalVariable.transactionId = transactionId
////		GlobalVariable.BKID = BKID
////		
//	
//		String validation1 = testdata["ContainsValidation"][TestScenarioNumber]
//		println validation1
//		String validation2 = testdata["NotContainsValidation"][TestScenarioNumber]
//		println validation2
//		println(testdata["TCNumber"][TestScenarioNumber])
//		assert response.contains(validation1)
//		assert response.contains(validation2) == false
//		
//		println("Test Scenario Number: " + TestScenarioNumber) //for checking what test scenario number the running stops if failure occurs
//		
		}