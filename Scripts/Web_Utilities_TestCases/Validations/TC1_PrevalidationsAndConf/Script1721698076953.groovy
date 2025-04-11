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

try {
    utils.openBrowserAndNavigateToPMT()
    WebDriver driver = DriverFactory.getWebDriver()
    String EXCEL_PATH = './Data Files/TestData.xlsx'
    String TAB = 'Validations_Prevalidations&Conf'
    utils.goToValidations()
    utils.selectEnvironment(GlobalVariable.ENV)

    List<List> testdata = getdata.fromExcel(EXCEL_PATH, TAB)

    WebElement clickAuthorizePayment = driver.findElement(By.xpath('//a[normalize-space()=\'Prevalidations and Conf\']')).click()

    for (int i = 0; i < testdata.size(); i++) {
        String TestScenarioRequiresCreateBooking = (testdata['ExecuteBookingCreationFlag'])[i]

        if (TestScenarioRequiresCreateBooking == 'Yes') {
            WebElement sendRequestTextBox = driver.findElement(By.xpath('//input[@id=\'1\']'))
            sendRequestTextBox.clear()
            WebUI.delay(5)

            def getBookingData = CreateAndRetrieveBooking.Data(EXCEL_PATH, TAB, i)
            println(getBookingData['BookingId'])
            println(getBookingData['BookingAccessToken'])
            String BookingId = getBookingData['BookingId']
			// for test scenarios that requires booking. (test data is from the bookingId column in excel)
            String BookingIdToBePassedRaw = (testdata['BookingId'])[i] 

			// BookingIdToBePassed which is a string "BookingId" and it is replaced with the bookingId from the create booking)
            String BookingIdToBePassed = BookingIdToBePassedRaw.replace('BookingId', BookingId) 
            sendRequestTextBox.sendKeys(BookingIdToBePassed)
            println(BookingIdToBePassed)
            utils.clickSendButton()
            WebUI.delay(5)
			
			String response = utils.getResponse()
            String validation = (testdata['Validation'])[i]
				if (response.contains(validation) == true) {
					println validation
					
				} else {
					//Mark Failed status after this step
					KeywordUtil.markFailed("Expected response does not meet" + "Actual: " + response)
					String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
					String f = "./screenshots/Failed_PreValidationsAndConf" + timestamp + ".png"
					WebUI.takeScreenshot(f.toString())
				}

            println('Test Scenario Number: ' + (i + 1)) //for checking what test scenario number the running stops if failure occurs (+ 1 because the for loop index starts with 0)
        
        } else {
            WebElement sendRequestTextBox = driver.findElement(By.xpath('//input[@id=\'1\']'))
            sendRequestTextBox.clear()

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
					String f = "./screenshots/Failed_PreValidationsAndConf" + timestamp + ".png"
					WebUI.takeScreenshot(f.toString())
				}
            println('Test Scenario Number: ' + (i))
        }
    
		RequestObject cancelBookingRequest = findTestObject('CancelBooking')
		ResponseObject cancelBookingResponse = WS.sendRequest(cancelBookingRequest)
		def cancelBookingJsonResponse = new JsonSlurper().parseText(cancelBookingResponse.getResponseText())
		println(cancelBookingJsonResponse)
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

