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

import rcclpayment.getdata

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import groovy.json.*

class CreateAndRetrieveBooking {

	static def Data(String ExcelPath, String Tab, int TestScenarioNumber) {

		//	this method creates a booking using the ESL createBooking endpoint and returns a map list (bookingId, BookingAcccessToken, LastName, Brand, ShipCode, and SailDate)
		//	It has 3 parameters:
		//		- ExcelPath: directory where the test data for each library is stored
		//		- Tab: which excel table you need to access
		//		- TestScenarioNumber: to iterate through each test scenario in the excel file
		//	the values passed in brand, shipCode, sailDate, categoryCode, and cabinNumber parameters of ESL createBooking is from the result of the SQL query which is located in an excel file in Data Files folder
		//	check the utils groovy script on how the data is gathered from the excel and E2K DB

		def bookingData = [:]
		List<List<Object>> testdata = getdata.fromExcel(ExcelPath,Tab)

		for(int i = TestScenarioNumber; i < testdata.size(); i++) {

			String query = testdata["Query"][i]
			def createBookingRequestJSON = testdata["CreateBookingRequest"][i].toString()
			println(createBookingRequestJSON)
			String[] columns = [
				'SMBRND',
				'SMSHIP',
				'SMSDDT',
				'BHPKID',
				'CBCTCD',
				'CBCBNO']		//this should match the columns returned in query
			def dataFromQuery = getdata.fromE2KDB(query, columns)

			println(dataFromQuery)

			//	set global variables value that will be passed to the createBooking esl endpoint
			GlobalVariable.CreateBookingRequestJSON = createBookingRequestJSON

			GlobalVariable.Brand = dataFromQuery["SMBRND"]
			GlobalVariable.Ship = dataFromQuery["SMSHIP"]
			GlobalVariable.SailDate = dataFromQuery["SMSDDT"]
			GlobalVariable.PackageId = dataFromQuery["BHPKID"]
			GlobalVariable.CategoryCode = dataFromQuery["CBCTCD"]
			GlobalVariable.CabinNumber = dataFromQuery["CBCBNO"]

			//	get create booking request from Object Repository
			RequestObject createBookingRequest = findTestObject('CreateBooking')
			String createBookingrequestBody = createBookingRequest.getBodyContent().getText()
			def createBookingjsonRequest = new JsonSlurper().parseText(createBookingrequestBody);
			println(createBookingjsonRequest)
			//	send createBooking request
			ResponseObject createBookingResponse = WS.sendRequest(createBookingRequest)
			def createBookingJsonResponse = new JsonSlurper().parseText(createBookingResponse.getResponseText())
			println(createBookingJsonResponse)

			//fetch data from createBooking response. this is passed to retrieveBooking request
			String lastName = createBookingjsonRequest.reservations[0].guestDetails[0].guestContact.personalInfo.name.lastName
			String application = createBookingjsonRequest.header.application
			String domainId = createBookingjsonRequest.header.domainId
			String rawBookingId = createBookingJsonResponse.bookingConfirmation.bookingId
			String bookingId = rawBookingId.replaceFirst(/^0/, '')

			//local variables above are passed to global variable so it can be accessed by the objects from Object Repository
			GlobalVariable.LastName = lastName
			GlobalVariable.Application = application
			GlobalVariable.DomainId = domainId
			GlobalVariable.BookingId = bookingId
			println(bookingId)

			WebUI.delay(10)

			//	get retrieve booking request from Object Repository
			RequestObject retrieveBookingRequest = findTestObject('RetrieveBooking')
			//send retrieveBooking request
			ResponseObject retrieveBookingResponse = WS.sendRequest(retrieveBookingRequest)
			def retrieveBookingJsonResponse = new JsonSlurper().parseText(retrieveBookingResponse.getResponseText())
			println(retrieveBookingJsonResponse)

			//fetch data from retrieveBooking response. bookingAccessToken will be passed to cancelBooking request
			String guestRefNumber1 = retrieveBookingJsonResponse.booking.guests.guest[0].guestRefNumber
			String guestRefNumber2 = retrieveBookingJsonResponse.booking.guests.guest[1].guestRefNumber
			String bookingAccessToken = retrieveBookingJsonResponse.bookingAccessToken

			GlobalVariable.GuestRefNumber1 = guestRefNumber1
			GlobalVariable.GuestRefNumber2 = guestRefNumber2
			GlobalVariable.BookingAccessToken = bookingAccessToken

			println(bookingAccessToken)

			bookingData["BookingId"] = bookingId
			bookingData["PassengerId1"] = guestRefNumber1
			bookingData["PassengerId2"] = guestRefNumber2
			bookingData["BookingAccessToken"] = bookingAccessToken
			bookingData["LastName"] = lastName
			bookingData["Brand"] = dataFromQuery["SMBRND"]
			bookingData["ShipCode"] = dataFromQuery["SMSHIP"]
			bookingData["SailDate"] = dataFromQuery["SMSDDT"]

			WebUI.delay(5)

			return bookingData
		}
	}
}