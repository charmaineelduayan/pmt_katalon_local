<?xml version="1.0" encoding="UTF-8"?>
<WebServiceRequestEntity>
   <description></description>
   <name>CreateBooking</name>
   <tag></tag>
   <elementGuidId>0201f5a0-c75f-4a92-b964-23ac2a9b024d</elementGuidId>
   <selectorMethod>BASIC</selectorMethod>
   <smartLocatorEnabled>false</smartLocatorEnabled>
   <useRalativeImagePath>false</useRalativeImagePath>
   <autoUpdateContent>false</autoUpdateContent>
   <connectionTimeout>-1</connectionTimeout>
   <followRedirects>false</followRedirects>
   <httpBody></httpBody>
   <httpBodyContent>{
  &quot;text&quot;: &quot;${CreateBookingRequestJSON}&quot;,
  &quot;contentType&quot;: &quot;application/json&quot;,
  &quot;charset&quot;: &quot;UTF-8&quot;
}</httpBodyContent>
   <httpBodyType>text</httpBodyType>
   <httpHeaderProperties>
      <isSelected>true</isSelected>
      <matchCondition>equals</matchCondition>
      <name>Content-Type</name>
      <type>Main</type>
      <value>application/json</value>
      <webElementGuid>86c097e2-3594-4b0e-a897-4074a8265a0a</webElementGuid>
   </httpHeaderProperties>
   <httpHeaderProperties>
      <isSelected>true</isSelected>
      <matchCondition>equals</matchCondition>
      <name>AppKey</name>
      <type>Main</type>
      <value>ZJCQR5Gf2v7I5zaz8jtOGGe3sbTXMcJp</value>
      <webElementGuid>97f29ae7-ff70-4929-a4aa-52ce1f485e41</webElementGuid>
   </httpHeaderProperties>
   <katalonVersion>9.5.0</katalonVersion>
   <maxResponseSize>-1</maxResponseSize>
   <migratedVersion>5.4.1</migratedVersion>
   <path></path>
   <restRequestMethod>POST</restRequestMethod>
   <restUrl>https://tst1-int.api.rccl.com/esl/booking/tst2/rest/v3/createBooking</restUrl>
   <serviceType>RESTful</serviceType>
   <soapBody></soapBody>
   <soapHeader></soapHeader>
   <soapRequestMethod></soapRequestMethod>
   <soapServiceEndpoint></soapServiceEndpoint>
   <soapServiceFunction></soapServiceFunction>
   <socketTimeout>-1</socketTimeout>
   <useServiceInfoFromWsdl>true</useServiceInfoFromWsdl>
   <variables>
      <defaultValue>GlobalVariable.Brand</defaultValue>
      <description></description>
      <id>7ed9045c-d66b-481d-ab70-ca48af755989</id>
      <masked>false</masked>
      <name>Brand</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.Ship</defaultValue>
      <description></description>
      <id>a985a9c5-ae96-4742-8e26-4e565979813f</id>
      <masked>false</masked>
      <name>ShipCode</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.SailDate</defaultValue>
      <description></description>
      <id>8eb45f6c-34a1-4078-aaad-59d72d4d4e97</id>
      <masked>false</masked>
      <name>SailDate</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.PackageId</defaultValue>
      <description></description>
      <id>b11ad4ad-50b6-4a82-abd5-99000da3511f</id>
      <masked>false</masked>
      <name>PackageId</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.CategoryCode</defaultValue>
      <description></description>
      <id>cd3807fd-c40d-4991-afdb-7609df96f097</id>
      <masked>false</masked>
      <name>CategoryCode</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.CabinNumber</defaultValue>
      <description></description>
      <id>ba189a30-1553-4b48-a553-a2cdbbc03915</id>
      <masked>false</masked>
      <name>CabinNumber</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.CreateBookingRequestJSON</defaultValue>
      <description></description>
      <id>dcc5e5b7-46aa-4a19-af6d-186c34709901</id>
      <masked>false</masked>
      <name>CreateBookingRequestJSON</name>
   </variables>
   <verificationScript>import static org.assertj.core.api.Assertions.*

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webservice.verification.WSResponseManager

import groovy.json.JsonSlurper
import internal.GlobalVariable as GlobalVariable

RequestObject request = WSResponseManager.getInstance().getCurrentRequest()

ResponseObject response = WSResponseManager.getInstance().getCurrentResponse()</verificationScript>
   <wsdlAddress></wsdlAddress>
</WebServiceRequestEntity>
