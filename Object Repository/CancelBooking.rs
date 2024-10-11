<?xml version="1.0" encoding="UTF-8"?>
<WebServiceRequestEntity>
   <description></description>
   <name>CancelBooking</name>
   <tag></tag>
   <elementGuidId>d8a3b40e-147c-473a-9dd8-21dd13d3b9de</elementGuidId>
   <selectorMethod>BASIC</selectorMethod>
   <smartLocatorEnabled>false</smartLocatorEnabled>
   <useRalativeImagePath>false</useRalativeImagePath>
   <autoUpdateContent>false</autoUpdateContent>
   <connectionTimeout>-1</connectionTimeout>
   <followRedirects>false</followRedirects>
   <httpBody></httpBody>
   <httpBodyContent>{
  &quot;text&quot;: &quot;{\n  \&quot;header\&quot;: {\n    \&quot;application\&quot;: \&quot;${Application}\&quot;,\n    \&quot;brand\&quot;: \&quot;${Brand}\&quot;,\n    \&quot;domainId\&quot;: \&quot;${DomainId}\&quot;,\n    \&quot;language\&quot;: \&quot;en_US\&quot;\n  },\n  \&quot;countryCode\&quot;: \&quot;USA\&quot;,\n  \&quot;bookingId\&quot;: \&quot;${BookingId}\&quot;,\n  \&quot;bookingAccessToken\&quot;: \&quot;${BookingAccessToken}\&quot;,\n  \&quot;lockBooking\&quot;: \&quot;true\&quot;\n}&quot;,
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
      <webElementGuid>90ca7dea-03a4-4552-9933-c3c11d3fe902</webElementGuid>
   </httpHeaderProperties>
   <httpHeaderProperties>
      <isSelected>true</isSelected>
      <matchCondition>equals</matchCondition>
      <name>AppKey</name>
      <type>Main</type>
      <value>ZJCQR5Gf2v7I5zaz8jtOGGe3sbTXMcJp</value>
      <webElementGuid>c92cb4bb-ab17-45fb-b347-6204b77b9769</webElementGuid>
   </httpHeaderProperties>
   <katalonVersion>9.5.0</katalonVersion>
   <maxResponseSize>-1</maxResponseSize>
   <migratedVersion>5.4.1</migratedVersion>
   <path></path>
   <restRequestMethod>POST</restRequestMethod>
   <restUrl>https://tst1-int.api.rccl.com/esl/booking/tst2/rest/v3/cancelBooking</restUrl>
   <serviceType>RESTful</serviceType>
   <soapBody></soapBody>
   <soapHeader></soapHeader>
   <soapRequestMethod></soapRequestMethod>
   <soapServiceEndpoint></soapServiceEndpoint>
   <soapServiceFunction></soapServiceFunction>
   <socketTimeout>-1</socketTimeout>
   <useServiceInfoFromWsdl>true</useServiceInfoFromWsdl>
   <variables>
      <defaultValue>GlobalVariable.Application</defaultValue>
      <description></description>
      <id>e1195e4a-17b5-4abf-b9e4-9b8dac4a6254</id>
      <masked>false</masked>
      <name>Application</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.Brand</defaultValue>
      <description></description>
      <id>8e708596-5f66-4905-8af1-19be8688e6c3</id>
      <masked>false</masked>
      <name>Brand</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.DomainId</defaultValue>
      <description></description>
      <id>3705d826-a5c6-4df0-ad0f-85f1c16a474b</id>
      <masked>false</masked>
      <name>DomainId</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.BookingId</defaultValue>
      <description></description>
      <id>cf5bb01e-5f2c-4af3-8f45-b415d86b95a1</id>
      <masked>false</masked>
      <name>BookingId</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.BookingAccessToken</defaultValue>
      <description></description>
      <id>fc364cb4-45fb-45a1-8e3a-f8f990a05da1</id>
      <masked>false</masked>
      <name>BookingAccessToken</name>
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
