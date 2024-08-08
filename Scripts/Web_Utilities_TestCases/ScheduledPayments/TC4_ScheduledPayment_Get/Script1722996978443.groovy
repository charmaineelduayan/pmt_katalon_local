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
	final String TAB = "ScheduledPayment_Get"
	
	utils.goToScheduledPayments()
	utils.selectEnvironment(GlobalVariable.ENV)
	
	//WebElement clickAuthorizePayment = driver.findElement(By.xpath("//a[normalize-space()='Get']")).click()
	
	List<List<Object>> testdataFromExcel = getdata.fromExcel(EXCEL_PATH,TAB)
	
	for(int TestScenarioNumber = 0; TestScenarioNumber < testdataFromExcel.size(); TestScenarioNumber++) {
		
		String TestScenarioRequiresCreateBooking = testdataFromExcel["ExecuteBookingCreationFlag"][TestScenarioNumber]

		if(TestScenarioRequiresCreateBooking == "Yes") {	
			WebElement clickADD = driver.findElement(By.xpath("//a[normalize-space()='Add']")).click() // locate Add Schedule Payment Button
			WebElement sendRequestTextBox = driver.findElement(By.xpath("//textarea[@name='req']")) // declare
			
			sendRequestTextBox.clear()
			
			
			//createBooking
			def getBookingData = CreateAndRetrieveBooking.Data(EXCEL_PATH, TAB, TestScenarioNumber)
			println(getBookingData["BookingId"])
			println(getBookingData["BookingAccessToken"])
			
			WebUI.delay(5)
			
			//Get Add Request from Execel and execute
			String ADDRequestRaw = testdataFromExcel["ADDRequest"][TestScenarioNumber]
			String BookingId = getBookingData["BookingId"]
			String ADDRequest = ADDRequestRaw.replace("BookingId",BookingId)	//this replaces the word "BookingId" in ADDRequest with the value from getBookingData["BookingId"]
			sendRequestTextBox.sendKeys(ADDRequest)
			println(ADDRequest)
			utils.clickSendButton()
			WebUI.delay(5)
			
			WebUI.refresh()
			WebUI.delay(3)
			
			// proceed to Get Schedule Paymentß
			WebElement clickGET = driver.findElement(By.xpath("//a[normalize-space()='Get']")).click()
			WebElement sendRequestTextBox2 = driver.findElement(By.xpath("//textarea[@name='req']"))
			sendRequestTextBox2.clear()
			
			String BookingIdToBePassedRaw = testdataFromExcel["BookingId"][TestScenarioNumber]
			String BookingIdToBePassed = BookingIdToBePassedRaw.replace("BookingId",BookingId)
			sendRequestTextBox2.sendKeys(BookingIdToBePassed) //send to Get TextBox
			println(BookingIdToBePassed)
			utils.clickSendButton()
			
			WebUI.delay(5)
			
			RequestObject cancelBookingRequest = findTestObject('CancelBooking')
			ResponseObject cancelBookingResponse = WS.sendRequest(cancelBookingRequest)
			def cancelBookingJsonResponse = new JsonSlurper().parseText(cancelBookingResponse.getResponseText())
			println(cancelBookingJsonResponse)
			
			WebElement responseTextBox = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/main[1]/div[1]/section[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/form[1]/div[1]/div[1]/textarea[1]"))
			String response = responseTextBox.getText()
				
			String validationString = testdataFromExcel["Validation"][TestScenarioNumber]
			
			assert response.contains(validationString)
			
			println("Test Scenario Number: " + (TestScenarioNumber + 1))		//for checking what test scenario number the running stops if failure occurs (+ 1 because the for loop index starts with 0)
		}
		else {
			
			WebElement clickGET = driver.findElement(By.xpath("//a[normalize-space()='Get']")).click()
			WebElement sendRequestTextBox = driver.findElement(By.xpath("//textarea[@name='req']"))
			WebElement responseTextBox = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/main[1]/div[1]/section[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/form[1]/div[1]/div[1]/textarea[1]"))
			sendRequestTextBox.clear()
			
			String BookingIdToBePassed = testdataFromExcel["BookingId"][TestScenarioNumber]
			sendRequestTextBox.sendKeys(BookingIdToBePassed)
			println(BookingIdToBePassed)
			utils.clickSendButton()
			WebUI.delay(2)
			
			String response = responseTextBox.getText()
				
			String validationString = testdataFromExcel["Validation"][TestScenarioNumber]
			
			assert response.contains(validationString)
			
			println("Test Scenario Number: " + (TestScenarioNumber + 1))		//for checking what test scenario number the running stops if failure occurs (+ 1 because the for loop index starts with 0)
		}

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




