package internal

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.main.TestCaseMain


/**
 * This class is generated automatically by Katalon Studio and should not be modified or deleted.
 */
public class GlobalVariable {
     
    /**
     * <p></p>
     */
    public static Object ENV
     
    /**
     * <p></p>
     */
    public static Object Brand
     
    /**
     * <p></p>
     */
    public static Object Ship
     
    /**
     * <p></p>
     */
    public static Object SailDate
     
    /**
     * <p></p>
     */
    public static Object PackageId
     
    /**
     * <p></p>
     */
    public static Object CategoryCode
     
    /**
     * <p></p>
     */
    public static Object CabinNumber
     
    /**
     * <p></p>
     */
    public static Object BookingId
     
    /**
     * <p></p>
     */
    public static Object LastName
     
    /**
     * <p></p>
     */
    public static Object BookingAccessToken
     
    /**
     * <p></p>
     */
    public static Object PassengerId
     
    /**
     * <p></p>
     */
    public static Object Application
     
    /**
     * <p></p>
     */
    public static Object DomainId
     
    /**
     * <p></p>
     */
    public static Object GuestRefNumber1
     
    /**
     * <p></p>
     */
    public static Object GuestRefNumber2
     
    /**
     * <p></p>
     */
    public static Object CreateBookingRequestJSON
     
    /**
     * <p></p>
     */
    public static Object uid
     
    /**
     * <p></p>
     */
    public static Object password
     
    /**
     * <p></p>
     */
    public static Object AppKey_GA
     
    /**
     * <p></p>
     */
    public static Object accountId
     
    /**
     * <p></p>
     */
    public static Object accessToken
     
    /**
     * <p></p>
     */
    public static Object transactionId
     
    /**
     * <p></p>
     */
    public static Object BKID
     
    /**
     * <p></p>
     */
    public static Object AuthorizePaymentResponse
     
    /**
     * <p></p>
     */
    public static Object FirstName
     
    /**
     * <p></p>
     */
    public static Object RetrieveBookingRes
     

    static {
        try {
            def selectedVariables = TestCaseMain.getGlobalVariables("default")
			selectedVariables += TestCaseMain.getGlobalVariables(RunConfiguration.getExecutionProfile())
            selectedVariables += TestCaseMain.getParsedValues(RunConfiguration.getOverridingParameters(), selectedVariables)
    
            ENV = selectedVariables['ENV']
            Brand = selectedVariables['Brand']
            Ship = selectedVariables['Ship']
            SailDate = selectedVariables['SailDate']
            PackageId = selectedVariables['PackageId']
            CategoryCode = selectedVariables['CategoryCode']
            CabinNumber = selectedVariables['CabinNumber']
            BookingId = selectedVariables['BookingId']
            LastName = selectedVariables['LastName']
            BookingAccessToken = selectedVariables['BookingAccessToken']
            PassengerId = selectedVariables['PassengerId']
            Application = selectedVariables['Application']
            DomainId = selectedVariables['DomainId']
            GuestRefNumber1 = selectedVariables['GuestRefNumber1']
            GuestRefNumber2 = selectedVariables['GuestRefNumber2']
            CreateBookingRequestJSON = selectedVariables['CreateBookingRequestJSON']
            uid = selectedVariables['uid']
            password = selectedVariables['password']
            AppKey_GA = selectedVariables['AppKey_GA']
            accountId = selectedVariables['accountId']
            accessToken = selectedVariables['accessToken']
            transactionId = selectedVariables['transactionId']
            BKID = selectedVariables['BKID']
            AuthorizePaymentResponse = selectedVariables['AuthorizePaymentResponse']
            FirstName = selectedVariables['FirstName']
            RetrieveBookingRes = selectedVariables['RetrieveBookingRes']
            
        } catch (Exception e) {
            TestCaseMain.logGlobalVariableError(e)
        }
    }
}
