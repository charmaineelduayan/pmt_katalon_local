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
import java.text.SimpleDateFormat
import rcclpayment.utils as utils
import rcclpayment.getdata as getdata

try {
    utils.openBrowserAndNavigateToPMT()

    String EXCEL_PATH = './Data Files/TestData.xlsx'
    String TAB = 'Wallet_Replace'

    utils.goToWallets()

	fetchGuestAccount = WS.sendRequest(findTestObject('GuestAccount'))
	accountId = WS.getElementPropertyValue(fetchGuestAccount,'payload.accountId')
	accessToken = WS.getElementPropertyValue(fetchGuestAccount,'payload.accessToken')
	GlobalVariable.accountId = accountId
	GlobalVariable.accessToken = accessToken
	
	List<List<Object>> testdata = getdata.fromExcel(EXCEL_PATH,TAB)
	for(int i = 0; i < testdata.size(); i++) {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement clickAuthorizePayment = driver.findElement(By.xpath('//a[normalize-space()=\'Replace\']')).click()
		utils.selectEnvironment(GlobalVariable.ENV)
		WebElement sendRequestTextBox = driver.findElement(By.xpath('//textarea[@name=\'req\']'))
		sendRequestTextBox.clear()
		
		// Accessing Excel values correctly
		String idToBeReplaced = testdata["idToBeReplaced"][i]
		String paymentMethod = testdata["paymentMethod"][i]
		String cardNumber = testdata["cardNumber"][i]
		String expirationMonth = testdata["expirationMonth"][i]
		String expirationYear = testdata["expirationYear"][i]
		String cardholderName = testdata["cardholderName"][i]
		String nickName = testdata["nickName"][i]
		String defaultPaymentMethod = testdata["defaultPaymentMethod"][i]
		
		String request =
			"""{
			    "idToBeReplaced": "${idToBeReplaced}",
			    "paymentMethod": {
			        "type": "${paymentMethod}",
			        "cardNumber": "${cardNumber}",
			        "expirationMonth": "${expirationMonth}",
			        "expirationYear": "${expirationYear}",
			        "cardholder": "${cardholderName}",
			        "nickname": "${nickName}"
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
	
		String validation1 = testdata["ContainsValidation"][i]
		println validation1
		String validation2 = testdata["NotContainsValidation"][i]
		println validation2
		println(testdata["TCNumber"][i])
		assert response.contains(validation1)
		assert response.contains(validation2) == false
		
		if (response.contains(validation1) == false || response.contains(validation2) == true) {
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
			String f = "./screenshots/Failed_Wallet_Replace" + timestamp + ".png"
			WebUI.takeScreenshot(f.toString())
			println("Assertion failed")
		}
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
