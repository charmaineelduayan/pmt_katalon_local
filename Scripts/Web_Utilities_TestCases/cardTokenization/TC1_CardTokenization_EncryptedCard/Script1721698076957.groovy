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

try {
	utils.openBrowserAndNavigateToPMT()
	
	final String EXCEL_PATH = "./Data Files/TestData.xlsx"
	final String TAB = "cardTokenization_Encrypt"

	WebDriver driver = DriverFactory.getWebDriver()
	utils.goToCardTokenization()
	utils.selectEnvironment(GlobalVariable.ENV)
	
	WebElement sendRequestTextBox = driver.findElement(By.xpath("//textarea[@name='req']"))
	
	List<List<Object>> testdata = getdata.fromExcel(EXCEL_PATH,TAB)

	for(int i = 0; i < testdata.size(); i++) {
		WebUI.delay(5)
		sendRequestTextBox.clear()
		
		String encryptedCard = testdata["encryptedCard"][i]
		CNumber = encryptedCard.replaceAll(/\.0$/,'')
		
		String request =  "${encryptedCard}"
		println(request)
		sendRequestTextBox.sendKeys(request)
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
	}	
}
catch (AssertionError e) {
	WebUI.takeScreenshot("./screenshots/Failed_CardTokenization_EncryptedCard.png")
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


