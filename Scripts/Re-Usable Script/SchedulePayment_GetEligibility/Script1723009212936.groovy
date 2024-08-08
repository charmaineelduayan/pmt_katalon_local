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

WebDriver driver = DriverFactory.getWebDriver()
utils.goToScheduledPayments()
WebElement clickGetEligibility = driver.findElement(By.xpath("//a[normalize-space()='Get Eligibility']")).click()

	final String EXCEL_PATH = "./Data Files/TestData.xlsx"
	final String TAB = "ScheduledPayment_GetEligibility"
	
	utils.selectEnvironment(GlobalVariable.ENV)
	
	List<List<Object>> testdata = getdata.fromExcel(EXCEL_PATH,TAB)
	for(int TestScenarioNumber = 0; TestScenarioNumber < testdata.size(); TestScenarioNumber++) {
		println(testdata.size())
		
			//createBooking to get the bookingId and passengerId
			def getBookingData = CreateAndRetrieveBooking.Data(EXCEL_PATH, TAB, TestScenarioNumber)
			println(getBookingData["BookingId"])
			println(getBookingData["BookingAccessToken"])
			println(getBookingData["PassengerId1"])
			println(getBookingData["PassengerId2"])
			
			WebUI.delay(5)
			
			//Replace "BookingId" value from bookingId column in excel sheet
			String bookingIdCol = testdata["BookingId"][TestScenarioNumber]
			String BookingId = getBookingData["BookingId"]
			String replaceBookingId = bookingIdCol.replace("BookingId",BookingId)
			println(replaceBookingId)
			
			
			WebElement sendRequestTextBox = driver.findElement(By.xpath("//textarea[@name='req']"))
			sendRequestTextBox.clear()
			
			String request = BookingId		// which is a string "BookingId" and it is replaced with the bookingId from the create booking
			sendRequestTextBox.sendKeys(request)
			println(request)
			
			utils.clickSendButton()
			
			WebUI.delay(3)
			
			String response = utils.getResponse()
			println(response)
			
			String validation1 = testdata["ContainsValidation"][TestScenarioNumber]
			println validation1
			String validation2 = testdata["NotContainsValidation"][TestScenarioNumber]
			println validation2
			println(testdata["TCNumber"][TestScenarioNumber])
			assert response.contains(validation1)
			assert response.contains(validation2) == false
			
			println("Test Scenario Number: " + TestScenarioNumber) //for checking what test scenario number the running stops if failure occurs
		
	}
