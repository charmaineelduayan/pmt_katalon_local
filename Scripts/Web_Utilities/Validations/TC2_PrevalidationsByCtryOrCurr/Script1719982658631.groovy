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
import rcclpayment.utils
import rcclpayment.getdata

final String DATA_PATH = "./Data Files/Validations.xlsx"

List<List<Object>> testdata = getdata.forValidations(DATA_PATH,"ByCountryAndCurrency")



try {
	utils.openBrowserAndNavigateToPMT()
	WebDriver driver = DriverFactory.getWebDriver()
	utils.goToValidations()
	
	WebElement clickAuthorizePayment = driver.findElement(By.xpath("//a[normalize-space()='Prevalidations by Ctry/Curr']")).click()
	
	utils.selectEnvironment(GlobalVariable.ENV)
	
	WebElement countryInput = driver.findElement(By.xpath("//input[@id='country']"))
	WebElement currencyInput = driver.findElement(By.xpath("//input[@id='currency']"))
	
	WebElement responseTextBox = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/main[1]/div[1]/section[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/form[1]/div[1]/div[1]/textarea[1]"))
	
	
	for(int i = 0; i < testdata.size(); i++) {
		WebUI.delay(5)
		countryInput.clear()
		currencyInput.clear()
		
		String countryData = testdata["Country"][i]
		String currencyData = testdata["Currency"][i]
		
		countryInput.sendKeys(countryData)
		currencyInput.sendKeys(currencyData)
		
		utils.clickSendButton()
		
		WebUI.delay(5)
		String response = responseTextBox.getText()
		println response
		
		String validation1 = testdata["ContainsValidation"][i]
		String validation2 = testdata["NotContainsValidation"][i]
		
		
		
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




