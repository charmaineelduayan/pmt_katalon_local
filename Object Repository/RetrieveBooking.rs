<?xml version="1.0" encoding="UTF-8"?>
<WebServiceRequestEntity>
   <description></description>
   <name>RetrieveBooking</name>
   <tag></tag>
   <elementGuidId>a5361510-a076-44ed-a295-0ec61223db87</elementGuidId>
   <selectorMethod>BASIC</selectorMethod>
   <smartLocatorEnabled>false</smartLocatorEnabled>
   <useRalativeImagePath>false</useRalativeImagePath>
   <autoUpdateContent>true</autoUpdateContent>
   <connectionTimeout>-1</connectionTimeout>
   <followRedirects>false</followRedirects>
   <httpBody></httpBody>
   <httpBodyContent></httpBodyContent>
   <httpBodyType></httpBodyType>
   <httpHeaderProperties>
      <isSelected>true</isSelected>
      <matchCondition>equals</matchCondition>
      <name>Content-Type</name>
      <type>Main</type>
      <value>application/json</value>
      <webElementGuid>91866184-c736-493c-b5b6-2c4a5aab1876</webElementGuid>
   </httpHeaderProperties>
   <httpHeaderProperties>
      <isSelected>true</isSelected>
      <matchCondition>equals</matchCondition>
      <name>AppKey</name>
      <type>Main</type>
      <value>ZJCQR5Gf2v7I5zaz8jtOGGe3sbTXMcJp</value>
      <webElementGuid>1abbbb0e-2b96-4e59-8d62-e2a322134a8c</webElementGuid>
   </httpHeaderProperties>
   <katalonVersion>9.5.0</katalonVersion>
   <maxResponseSize>-1</maxResponseSize>
   <migratedVersion>5.4.1</migratedVersion>
   <path></path>
   <restRequestMethod>GET</restRequestMethod>
   <restUrl>https://tst1-int.api.rccl.com/esl/booking/tst2/rest/v3/retrieveBooking?header.domainId=${DomainId}&amp;header.language=en_US&amp;header.application=${Application}&amp;header.brand=${Brand}&amp;shipCode=${ShipCode}&amp;sailDate=${SailDate}&amp;countryCode=USA&amp;bookingId=${BookingId}&amp;lastName=${LastName}&amp;lockBooking=TRUE&amp;summaryRequested=FALSE</restUrl>
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
      <id>75fde6fe-bf70-41a8-8a24-f6eadb56175e</id>
      <masked>false</masked>
      <name>Brand</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.Ship</defaultValue>
      <description></description>
      <id>09f73c8e-bf40-4aff-9a5f-7a5e90b2961a</id>
      <masked>false</masked>
      <name>ShipCode</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.SailDate</defaultValue>
      <description></description>
      <id>e8d5ab3d-91fe-405a-a37d-113092b7a158</id>
      <masked>false</masked>
      <name>SailDate</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.BookingId</defaultValue>
      <description></description>
      <id>0edb829f-55cb-4611-b2f4-88c887e1373a</id>
      <masked>false</masked>
      <name>BookingId</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.DomainId</defaultValue>
      <description></description>
      <id>0a9e61c8-2e61-4098-a6ed-88d47d763f6d</id>
      <masked>false</masked>
      <name>DomainId</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.Application</defaultValue>
      <description></description>
      <id>840d5fa2-61e0-4d3a-85ce-4b25f4aa3e46</id>
      <masked>false</masked>
      <name>Application</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.LastName</defaultValue>
      <description></description>
      <id>d7144165-232e-4bc0-ad29-ac7db92cf394</id>
      <masked>false</masked>
      <name>LastName</name>
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
