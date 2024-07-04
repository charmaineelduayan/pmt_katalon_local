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

import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import java.io.File
import java.io.FileInputStream

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class getdata {

	static List<List<Object>> fromExcel(String excelFilePath, String table) {
		List<List<Object>> excelData = new ArrayList<>()

		// Create an input stream to read the file
		FileInputStream fileInputStream = new FileInputStream(new File(excelFilePath))

		// Create a workbook instance that refers to .xlsx file
		Workbook workbook = new XSSFWorkbook(fileInputStream)

		// Get the first sheet from the workbook
		Sheet sheet = workbook.getSheet(table)

		// Iterate through each row and cell in the sheet
		for (Row row : sheet) {
			List<Object> rowData = new ArrayList<>()
			for (Cell cell : row) {
				switch (cell.getCellTypeEnum()) {
					case CellType.STRING:
						rowData.add(cell.getStringCellValue())
						break
					case CellType.NUMERIC:
						rowData.add(cell.getNumericCellValue())
						break
					case CellType.BOOLEAN:
						rowData.add(cell.getBooleanCellValue())
						break
					default:
						rowData.add(cell.toString())
						break
				}
			}
			excelData.add(rowData)
		}
		def columnNames = excelData[0]
		def data = excelData[1..-1]

		def processedData = []

		data.each { row ->
			def rowMap = [:]
			row.eachWithIndex { value, index ->
				rowMap[columnNames[index]] = value
			}
			processedData << rowMap
		}
		// Close the workbook and input stream
		workbook.close()
		fileInputStream.close()

		return processedData
	}

	static def fromE2KDB(String queryVariable, String[] columnNames) {
		// Database connection details
		final String URL = 'jdbc:as400://augusta.rccl.com;libraries=DTD_RLE;UserID=userId;Password=<password>;'	// connection string for RLE environment
		final String USER = 'ITINEMR' //change to a valid E2K username
		final String PASSWORD = 'Man1la4' //change to a valid E2K password

		Connection connection = null
		Statement statement = null
		ResultSet resultSet = null

		def data = [:]

		Class.forName("com.ibm.as400.access.AS400JDBCDriver")
		// Establish the connection
		connection = DriverManager.getConnection(URL, USER, PASSWORD)
		println("Database connected!")

		// Create a statement object
		statement = connection.createStatement()

		// Execute a query
		String query = queryVariable
		resultSet = statement.executeQuery(query)
		// Process the result set
		if (resultSet.next()) {
			for(int i = 0; i < columnNames.size();i++) {
				data[columnNames[i]] = resultSet.getString(columnNames[i])
			}
		}
		return data
	}
}