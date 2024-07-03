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
//modules to access the DB
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
// Database connection details
String url = 'jdbc:as400://augusta.rccl.com;libraries=DTD_RLE;UserID=userId;Password=<password>;'	// connection string for RLE environment
String user = 'TEST_USERNAME' //change to a valid E2K username
String password = 'TEST_PASSWORD' //change to a valid E2K password
Connection connection = null
Statement statement = null
ResultSet resultSet = null
try {
	// Load the AS400 JDBC driver
	Class.forName("com.ibm.as400.access.AS400JDBCDriver")
	// Establish the connection
	connection = DriverManager.getConnection(url, user, password)
	println("Database connected!")
	// Create a statement object
	statement = connection.createStatement()
	// Execute a query
	String query = "SELECT BVSDDT, BVCBNO FROM RZBKVD WHERE BVCBNO != 'GTY' AND BVSDDT > Cast(Substr(Replace(Char(CURDATE() + 2 MONTH, ISO), '-', ''), 1) as Dec(8, 0))-19000000 FETCH FIRST ROW ONLY"
	resultSet = statement.executeQuery(query)
	// Process the result set	
	if (resultSet.next()) {
		// Retrieve and print multiple columns
		String column1 = resultSet.getString('BVSDDT')
		String column2 = resultSet.getString('BVCBNO')
		println("Column 1: " + column1)
		println("Column 2: " + column2)
		println(resultSet)
	}
} catch (Exception e) {
	e.printStackTrace()
} finally {
	// Close the resources
	if (resultSet != null) {
		try {
			resultSet.close()
		} catch (Exception e) {
			e.printStackTrace()
		}
	}
	if (statement != null) {
		try {
			statement.close()
		} catch (Exception e) {
			e.printStackTrace()
		}
	}
	if (connection != null) {
		try {
			connection.close()
		} catch (Exception e) {
			e.printStackTrace()
		}
	}
	println("Database connection closed!")
}