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
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import rcclpayment.utils
import rcclpayment.getdata

final String DATA_PATH = "./Data Files/TestData.xlsx"

List<List<Object>> testdata = getdata.forValidations(DATA_PATH,"GiftCards")

try {
	utils.openBrowserAndNavigateToPMT()
	WebDriver driver = DriverFactory.getWebDriver()
	utils.goToGiftCards()
	utils.selectEnvironment(GlobalVariable.ENV)
	

	for(int i = 0; i < testdata.size(); i++) {
		WebUI.delay(5)
		utils.sendRequest().clear()
		
		String cardNumber = testdata["cardNumber"][i]
		CNumber = cardNumber.replaceAll(/\.0$/,'')
		String expirationMonth = testdata["expirationMonth"][i]
		xMonth = expirationMonth.replaceAll(/\.0$/,'')
		String expirationYear = testdata["expirationYear"][i]
		xYear = expirationYear.replaceAll(/\.0$/,'')
		String securityCode = testdata["securityCode"][i]
		SC = securityCode.replaceAll(/\.0$/,'')
		
		String request = 
		"""{
			"cardNumber": "${cardNumber}",
			"expirationMonth": "${expirationMonth}",
			"expirationYear": "${expirationYear}",
			"securityCode": "${securityCode}"
			}"""
		def restRequest = new JsonSlurper().parseText(request)
		def prettyJson = new groovy.json.JsonBuilder(restRequest).toPrettyString()
		println(prettyJson)
		utils.sendRequest().sendKeys(prettyJson)
		utils.clickSendButton()
		
		
		WebUI.delay(3)
		String response = utils.getResponse()
		println response

		String validation1 = testdata["ContainsValidation"][i]
		println validation1
		String validation2 = testdata["NotContainsValidation"][i]
		println validation2
		
		assert response.contains(validation1) 
		assert response.contains(validation2) == false
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




