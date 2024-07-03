import rcclpayment.getdata

// Specify the path to your Excel file
String excelFilePath = "./Data Files/Validations.xlsx"

// Call the readExcel method and get the data
List<List<Object>> testdata = getdata.forValidations(excelFilePath,"ByCountryAndCurrency")

println testdata.size()


//
//// Now use the table data for testing
//table.each { row ->
//	println "Running test case with TestCaseId: ${row['TestCaseId']}"
//	// Replace this with your actual test code
//	runTestCase(row)
//}
//
//// Example test function
//def runTestCase(Map<String, Object> testCaseData) {
//	def testCaseId = testCaseData['TestCaseId']
//	def scenarioType = testCaseData['ScenarioType']
//	def tcDescription = testCaseData['TCDescription']
//	def country = testCaseData['Country']
//	def currency = testCaseData['Currency']
//   
//	println "TestCaseId: $testCaseId, ScenarioType: $scenarioType, TCDescription: $tcDescription, Country: $country, Currency: $currency"
//   
//	// Add your test logic here
//	// For example:
//	// assert someFunctionToTest(country, currency) == expectedValue
//}