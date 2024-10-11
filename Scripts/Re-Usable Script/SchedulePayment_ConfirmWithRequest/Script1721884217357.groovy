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

    String EXCEL_PATH = './Data Files/TestData.xlsx'
    String TAB = 'ScheduledPayment_ConfirmWithReq'

    List<List> testdata = getdata.fromExcel(EXCEL_PATH, TAB)

    for (int TestScenarioNumber = 0; TestScenarioNumber < testdata.size(); TestScenarioNumber++) {
		WebUI.callTestCase(findTestCase('Re-Usable Script/SchedulePayment_GetEligibility'), [:], FailureHandling.STOP_ON_FAILURE)

        WebDriver driver = DriverFactory.getWebDriver()
        WebElement getConfrimWithReq = driver.findElement(By.xpath('//a[normalize-space()=\'Confirm with Request\']')).click()
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
				  "payments": [
				    {
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
				          "action": "$action",
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
				    }
				  ]
				}"""

        def restRequest = new JsonSlurper().parseText(request)
        def prettyJson = new groovy.json.JsonBuilder(restRequest).toPrettyString()
        println(prettyJson)
        sendRequestTextBox.sendKeys(prettyJson)
        utils.clickSendButton()
    }