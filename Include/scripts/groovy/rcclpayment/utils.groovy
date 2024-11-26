package rcclpayment
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

class utils {

	static def openBrowserAndNavigateToPMT() {
		final String BASE_URL = "https://dev1.payment.rccl.com/payment/utilities"
		WebUI.openBrowser('')
		WebUI.maximizeWindow()
		WebUI.navigateToUrl(BASE_URL)
	}

	static def closeBrowser() {
		WebUI.delay(2)
		WebUI.closeBrowser()
	}

	static def selectEnvironment(String env) {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement SelectEnvironment = driver.findElement(By.xpath("//select[@name='env']"))
		SelectEnvironment.sendKeys(env)
	}

	static def clickSendButton() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement clickSend = driver.findElement(By.xpath("//button[@type='submit']")).click()
	}

	static def goToGiftCards() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement clickLibraries = driver.findElement(By.xpath("//span[normalize-space()='Libraries']")).click()
		WebElement clickEndpoint = driver.findElement(By.xpath("//a[@href='/payment/utilities/gift-cards']")).click()
		WebElement hideDropDownMenu = driver.findElement(By.xpath("//span[normalize-space()='Response']")).click()
	}

	static def goToPayments() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement clickLibraries = driver.findElement(By.xpath("//span[normalize-space()='Libraries']")).click()
		WebElement clickEndpoint = driver.findElement(By.xpath("//a[@href='/payment/utilities/payments']")).click()
		WebElement hideDropDownMenu = driver.findElement(By.xpath("//span[normalize-space()='Response']")).click()
	}

	static def goToScheduledPayments() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement clickLibraries = driver.findElement(By.xpath("//span[normalize-space()='Libraries']")).click()
		WebElement clickEndpoint = driver.findElement(By.xpath("//a[@href='/payment/utilities/scheduled-payments']")).click()
		WebElement hideDropDownMenu = driver.findElement(By.xpath("//span[normalize-space()='Response']")).click()
	}

	static def goToThreeDS() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement clickLibraries = driver.findElement(By.xpath("//span[normalize-space()='Libraries']")).click()
		WebElement clickEndpoint = driver.findElement(By.xpath("//a[@href='/payment/utilities/scheduled-payments']")).click()
		WebElement hideDropDownMenu = driver.findElement(By.xpath("//span[normalize-space()='Response']")).click()
	}

	static def goToCardTokenization() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement clickLibraries = driver.findElement(By.xpath("//span[normalize-space()='Libraries']")).click()
		WebElement clickEndpoint = driver.findElement(By.xpath("//h6[normalize-space()='Card Tokenization']")).click()
		WebElement hideDropDownMenu = driver.findElement(By.xpath("//span[normalize-space()='Response']")).click()
	}

	static def goToWallets() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement clickLibraries = driver.findElement(By.xpath("//span[normalize-space()='Libraries']")).click()
		WebElement clickEndpoint = driver.findElement(By.xpath("//h6[normalize-space()='Wallet']")).click()
		WebElement hideDropDownMenu = driver.findElement(By.xpath("//span[normalize-space()='Response']")).click()
	}

	static def goToValidations() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement clickLibraries = driver.findElement(By.xpath("//span[normalize-space()='Libraries']")).click()
		WebElement clickEndpoint = driver.findElement(By.xpath("//h6[normalize-space()='Validations']")).click()
		WebElement hideDropDownMenu = driver.findElement(By.xpath("//span[normalize-space()='Response']")).click()
	}

	static def sendRequest() {
		WebDriver driver = DriverFactory.getWebDriver()≈
		WebElement request = driver.findElement(By.xpath("//textarea[@name='req']"))
		//request.click()
		//return request
	}

	static def getResponse() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement response = driver.findElement(By.xpath('//textarea[@class=\'text-dark form-control\']'))
		String responseContent = response.getText()
		return responseContent
	} 
}