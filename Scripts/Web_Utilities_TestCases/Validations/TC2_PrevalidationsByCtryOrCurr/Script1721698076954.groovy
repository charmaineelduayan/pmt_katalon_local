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
import java.nio.file.Path
import java.nio.file.Files
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration
import rcclpayment.utils
import rcclpayment.getdata
import java.text.SimpleDateFormat
import com.kms.katalon.core.util.KeywordUtil


try {
	utils.openBrowserAndNavigateToPMT()
	WebDriver driver = DriverFactory.getWebDriver()
	
	final String EXCEL_PATH = "./Data Files/TestData.xlsx"
	final String TAB = "Validations_ByCtryOrCurr"

	List<List<Object>> testdata = getdata.fromExcel(EXCEL_PATH,TAB)
	
	utils.goToValidations()
	utils.selectEnvironment(GlobalVariable.ENV)
	
	WebElement clickAuthorizePayment = driver.findElement(By.xpath("//a[normalize-space()='Prevalidations by Ctry/Curr']")).click()
	
	WebElement countryInput = driver.findElement(By.xpath("//input[@id='country']"))
	WebElement currencyInput = driver.findElement(By.xpath("//input[@id='currency']"))
	
	
	for(int i = 0; i < testdata.size(); i++) {
		WebUI.delay(5)
		countryInput.clear()
		currencyInput.clear()
		
		String countryData = testdata["Country"][i]
		String currencyData = testdata["Currency"][i]
		println countryData
		println currencyData
		
		countryInput.sendKeys(countryData)
		currencyInput.sendKeys(currencyData)
		
		utils.clickSendButton()
		
		WebUI.delay(5)
		
		String response = utils.getResponse()
		println response
		
		String validation = testdata["Validation"][i]
		
		if (response.contains(validation) == true) {
			println validation
			
		} else {
			//Mark Failed status after this step
			KeywordUtil.markFailed("Expected response does not meet" + "Actual: " + response)
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
			String f = "./screenshots/Failed_PreValidationsByCtryOrCurr" + timestamp + ".png"
			WebUI.takeScreenshot(f.toString())
			}
			
		println("Test Scenario Number: " + (i + 1))		//for checking what test scenario number the running stops if failure occurs (+ 1 because the for loop index starts with 0
		
		}
	
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




