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
	
	final String EXCEL_PATH = "./Data Files/TestData.xlsx"
	final String TAB = "ScheduledPayment_Add"
	
	utils.goToScheduledPayments()
	utils.selectEnvironment(GlobalVariable.ENV)
	
	WebElement clickADD = driver.findElement(By.xpath("//a[normalize-space()='Add']")).click()
	WebElement sendRequestTextBox = driver.findElement(By.xpath("//textarea[@name='req']"))
	
	//List object for storing the data from the excel	
	List<List<Object>> testdata = getdata.fromExcel(EXCEL_PATH,TAB)
	
	for(int TestScenarioNumber = 0; TestScenarioNumber < testdata.size(); TestScenarioNumber++) {		
		sendRequestTextBox.clear()
	
		//create and retrieve booking
		def getBookingData = CreateAndRetrieveBooking.Data(EXCEL_PATH, TAB, TestScenarioNumber)
		println(getBookingData["BookingId"])
		println(getBookingData["BookingAccessToken"])
		
		WebUI.delay(5)
		
		//retrieve the ADD request from excel and pass it to the request input box in web utilities
		String ADDRequestRaw = testdata["ADDRequest"][TestScenarioNumber]
		String BookingId = getBookingData["BookingId"]
		String ADDRequest = ADDRequestRaw.replace("BookingId",BookingId)	//this replaces the word "BookingId" in ADDRequest with the value from getBookingData["BookingId"]
		sendRequestTextBox.sendKeys(ADDRequest)
		println(ADDRequest)
		utils.clickSendButton()
		
		WebUI.delay(5)
		
		//cancel booking
		RequestObject cancelBookingRequest = findTestObject('CancelBooking')
		ResponseObject cancelBookingResponse = WS.sendRequest(cancelBookingRequest)
		def cancelBookingJsonResponse = new JsonSlurper().parseText(cancelBookingResponse.getResponseText())
		println(cancelBookingJsonResponse)
		
		//add assertion (assertion should be after the booking cancellation)
		
		println("Test Scenario Number: " + TestScenarioNumber)		//for checking what test scenario number the running stops if failure occurs	
		
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




