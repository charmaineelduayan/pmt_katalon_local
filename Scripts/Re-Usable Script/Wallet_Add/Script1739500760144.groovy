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
	
	WebElement clickAdd = driver.findElement(By.xpath('//a[normalize-space()=\'Add\']')).click()
	utils.selectEnvironment(GlobalVariable.ENV)
	WebElement sendRequestTextBox = driver.findElement(By.xpath('//textarea[@name=\'req\']'))

	fetchGuestAccount = WS.sendRequest(findTestObject('GuestAccount'))
	accountId = WS.getElementPropertyValue(fetchGuestAccount,'payload.accountId')
	accessToken = WS.getElementPropertyValue(fetchGuestAccount,'payload.accessToken')
	GlobalVariable.accountId = accountId
	GlobalVariable.accessToken = accessToken
	
	sendRequestTextBox.clear()

        String request =
            """{
                "paymentMethod": {
                    "type": "$GlobalVariable.Wallet_paymentMethod",
                    "cardNumber": "$GlobalVariable.Wallet_cardNumber",
                    "expirationMonth": "$GlobalVariable.Wallet_expirationMonth",
                    "expirationYear": "$GlobalVariable.Wallet_expirationYear",
                    "cardholder": "$GlobalVariable.Wallet_cardholderName",
                    "nickname": "$GlobalVariable.Walelt_nickName",
                    "defaultPaymentMethod": "$GlobalVariable.Wallet_defaultPaymentMethod"
                },
                "accountId": "${accountId}",
                "accessToken": "${accessToken}"
            }"""

        def restRequest = new JsonSlurper().parseText(request)
        def prettyJson = new groovy.json.JsonBuilder(restRequest).toPrettyString()
        println(prettyJson)

        sendRequestTextBox.sendKeys(prettyJson)
        utils.clickSendButton()

        WebUI.delay(3)
        String response = utils.getResponse()
        println response

        if (response.contains("PY-0402")== false) {
				// proceed to update
            }
		else {
			println("Existing card detected. Deleting...")
            WebUI.callTestCase(findTestCase('Re-Usable Script/Wallet_Delete'), [:], FailureHandling.CONTINUE_ON_FAILURE)
            WebUI.refresh()
            WebUI.delay(2)
        }
