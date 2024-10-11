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

try {
    utils.openBrowserAndNavigateToPMT()

    WebDriver driver = DriverFactory.getWebDriver()

    String EXCEL_PATH = './Data Files/TestData.xlsx'

    String TAB = 'Validations_Prevalidations&Conf'

    utils.goToValidations()

    utils.selectEnvironment(GlobalVariable.ENV)

    List<List> testdataFromExcel = getdata.fromExcel(EXCEL_PATH, TAB)

    WebElement clickAuthorizePayment = driver.findElement(By.xpath('//a[normalize-space()=\'Prevalidations and Conf\']')).click()

    for (int TestScenarioNumber = 0; TestScenarioNumber < testdataFromExcel.size(); TestScenarioNumber++) {
        String TestScenarioRequiresCreateBooking = (testdataFromExcel['ExecuteBookingCreationFlag'])[TestScenarioNumber]

        if (TestScenarioRequiresCreateBooking == 'Yes') {
            WebElement sendRequestTextBox = driver.findElement(By.xpath('//input[@id=\'1\']'))

            WebElement responseTextBox = driver.findElement(By.xpath('/html[1]/body[1]/div[1]/main[1]/div[1]/section[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/form[1]/div[1]/div[1]/textarea[1]'))

            sendRequestTextBox.clear()

            WebUI.delay(5)

            def getBookingData = CreateAndRetrieveBooking.Data(EXCEL_PATH, TAB, TestScenarioNumber)

            println(getBookingData['BookingId'])

            println(getBookingData['BookingAccessToken'])

            String BookingId = getBookingData['BookingId']

            String BookingIdToBePassedRaw = (testdataFromExcel['BookingId'])[TestScenarioNumber // for test scenarios that requires booking. (it gets its value in BookingId column in
            ]

            String BookingIdToBePassed = BookingIdToBePassedRaw.replace('BookingId', BookingId // which is a string "BookingId" and it is replaced with the bookingId from the create booking)
                )

            sendRequestTextBox.sendKeys(BookingIdToBePassed)

            println(BookingIdToBePassed)

            utils.clickSendButton()

            WebUI.delay(5)

            RequestObject cancelBookingRequest = findTestObject('CancelBooking')

            ResponseObject cancelBookingResponse = WS.sendRequest(cancelBookingRequest)

            def cancelBookingJsonResponse = new JsonSlurper().parseText(cancelBookingResponse.getResponseText())

            println(cancelBookingJsonResponse)

            String response = responseTextBox.getText()

            String validationString = (testdataFromExcel['Validation'])[TestScenarioNumber]

            assert response.contains(validationString)

            println('Test Scenario Number: ' + (TestScenarioNumber + 1 //for checking what test scenario number the running stops if failure occurs (+ 1 because the for loop index starts with 0)
                ))

            WebUI.takeFullPageScreenshotAsCheckpoint('Sample Visual Screenshot')
        } else {
            WebElement sendRequestTextBox = driver.findElement(By.xpath('//input[@id=\'1\']'))

            WebElement responseTextBox = driver.findElement(By.xpath('/html[1]/body[1]/div[1]/main[1]/div[1]/section[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/form[1]/div[1]/div[1]/textarea[1]'))

            sendRequestTextBox.clear()

            String BookingIdToBePassed = (testdataFromExcel['BookingId'])[TestScenarioNumber]

            sendRequestTextBox.sendKeys(BookingIdToBePassed)

            println(BookingIdToBePassed)

            utils.clickSendButton()

            WebUI.delay(2)

            String response = responseTextBox.getText()

            String validationString = (testdataFromExcel['Validation'])[TestScenarioNumber]

            assert response.contains(validationString)

            println('Test Scenario Number: ' + (TestScenarioNumber + 1))

            WebUI.takeFullPageScreenshotAsCheckpoint('Sample Visual Screenshot')
        }
    }
}
catch (AssertionError e) {
	WebUI.takeScreenshot("./screenshots/Failed_PreValidationsAndConf.png")
	println("Assertion failed: ${e.message}")
	e.printStackTrace()
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


