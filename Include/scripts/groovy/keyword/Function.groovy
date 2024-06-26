package keyword
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
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import org.openqa.selenium.Keys

import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory


class Function {
	//** Open Web Utilities **//
	static openBrowser() {
		WebUI.openBrowser('')
		WebUI.maximizeWindow()
		WebUI.navigateToUrl(GlobalVariable.URL)
	}
	//** Re usable functionality **//
	static libraries() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement Libraries = driver.findElement(By.xpath('//span[normalize-space()=\'Libraries\']'))
		Libraries.click()
	}
	static void environment() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement env = driver.findElement(By.xpath('//select[@name=\'env\']'))
		env.sendKeys(GlobalVariable.env)
		WebUI.comment(GlobalVariable.env)
		env.click()
	}
	static void offset() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement Offset = driver.findElement(By.xpath('//span[normalize-space()=\'Response\']'))
		Offset.click()
	}
	static countryTxt() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement countryTxt = driver.findElement(By.xpath('//input[@id=\'country\']'))
		countryTxt.click()
		return countryTxt
	}
	static currencyTxt() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement currencyTxt = driver.findElement(By.xpath('//input[@id=\'currency\']'))
		currencyTxt.click()
		return currencyTxt
	}
	static validationBookingId() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement validationBookingId = driver.findElement(By.xpath('//input[@id=\'1\']'))
		validationBookingId.click()
		return validationBookingId
	}
	static WebElement request() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement request = driver.findElement(By.xpath('//textarea[@name=\'req\']'))
		request.click()
		return request
	}
	static void Submit() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement submit = driver.findElement(By.xpath('//button[@type=\'submit\']'))
		submit.click()
	}

	static String response() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement response = driver.findElement(By.xpath('//textarea[@class=\'text-dark form-control\']'))
		String responseContent = response.getText()
		return responseContent
	}
	static closeBrowser() {
		WebUI.closeBrowser()
	}
}
