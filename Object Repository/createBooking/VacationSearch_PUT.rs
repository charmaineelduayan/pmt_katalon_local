<?xml version="1.0" encoding="UTF-8"?>
<WebServiceRequestEntity>
   <description></description>
   <name>VacationSearch_PUT</name>
   <tag></tag>
   <elementGuidId>ac749659-b710-4ae3-a4cb-3ad9f3aba765</elementGuidId>
   <selectorMethod>BASIC</selectorMethod>
   <smartLocatorEnabled>false</smartLocatorEnabled>
   <useRalativeImagePath>false</useRalativeImagePath>
   <autoUpdateContent>false</autoUpdateContent>
   <connectionTimeout>-1</connectionTimeout>
   <followRedirects>false</followRedirects>
   <httpBody></httpBody>
   <httpBodyContent>{
  &quot;text&quot;: &quot;{\n\t\&quot;header\&quot;: {\n\t\t\&quot;brand\&quot;: \&quot;${Brand}\&quot;,\n\t\t\&quot;application\&quot;: \&quot;celebritycruises.com\&quot;,\n\t\t\&quot;domainId\&quot;: 1,\n\t\t\&quot;language\&quot;: \&quot;en_US\&quot;\n\t},\n\t\&quot;criteria\&quot;: {\n\t\t\&quot;office\&quot;: \&quot;MIA\&quot;,\n\t\t\&quot;country\&quot;: \&quot;USA\&quot;,\n\t\t\&quot;currency\&quot;: \&quot;USD\&quot;,\n\t\t\&quot;channel\&quot;: \&quot;VP\&quot;,\n\t\t\&quot;bookingType\&quot;: \&quot;FIT\&quot;, \n\t\t\n\t\t\&quot;cruiseType\&quot;: {\n\t\t\t\&quot;value\&quot;: [\n\t\t\t\t\&quot;CO\&quot;,\n\t\t\t\t\&quot;CT\&quot;\n\t\t\t]\n\t\t},\n\t\t\&quot;brand\&quot;: {\n\t\t\t\&quot;value\&quot;: [\n\t\t\t\t\&quot;C\&quot;\n\t\t\t]\n\t\t}\n\t},\n\t\&quot;resultsPreference\&quot;: {\n\t\t\&quot;additionalFacets\&quot;: [],\n\t\t\&quot;groupBy\&quot;: \&quot;SAILING\&quot;,\n\t\t\&quot;guestPricing\&quot;: \&quot;AVERAGE\&quot;,\n\t\t\&quot;includeFacets\&quot;: false,\n\t\t\&quot;includeOnlyIfPriceAvailable\&quot;: false,\n\t\t\&quot;includePrices\&quot;: true,\n\t\t\&quot;includeResults\&quot;: true,\n\t\t\&quot;pagination\&quot;: {\n\t\t\t\&quot;count\&quot;: \&quot;3100\&quot;,\n\t\t\t\&quot;offset\&quot;: 0\n\t\t},\n\t\t\&quot;priceLevel\&quot;: null,\n\t\t\&quot;sortBy\&quot;: \&quot;PRICE\&quot;,\n\t\t\&quot;sortOrder\&quot;: \&quot;ASCENDING\&quot;,\n\t\t\&quot;strictSearch\&quot;: true\n\t}\n}&quot;,
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
      <webElementGuid>3c56fb64-1a10-417b-a987-c86f9e510a8d</webElementGuid>
   </httpHeaderProperties>
   <httpHeaderProperties>
      <isSelected>true</isSelected>
      <matchCondition>equals</matchCondition>
      <name>Accept</name>
      <type>Main</type>
      <value>application/json</value>
      <webElementGuid>26332b1c-ffa2-43aa-9f54-75a03435e622</webElementGuid>
   </httpHeaderProperties>
   <katalonVersion>9.5.0</katalonVersion>
   <maxResponseSize>-1</maxResponseSize>
   <migratedVersion>5.4.1</migratedVersion>
   <path></path>
   <restRequestMethod>PUT</restRequestMethod>
   <restUrl>https://tst.vps.rccl-it.cruises/vps-gamma/vacation/v1/cruise-search</restUrl>
   <serviceType>RESTful</serviceType>
   <soapBody></soapBody>
   <soapHeader></soapHeader>
   <soapRequestMethod></soapRequestMethod>
   <soapServiceEndpoint></soapServiceEndpoint>
   <soapServiceFunction></soapServiceFunction>
   <socketTimeout>-1</socketTimeout>
   <useServiceInfoFromWsdl>true</useServiceInfoFromWsdl>
   <variables>
      <defaultValue>''</defaultValue>
      <description></description>
      <id>5c86e4db-983a-45e1-92f3-7e896159052c</id>
      <masked>false</masked>
      <name>Application</name>
   </variables>
   <variables>
      <defaultValue>''</defaultValue>
      <description></description>
      <id>66d6b713-1430-4a7e-a166-a98bcf212fcb</id>
      <masked>false</masked>
      <name>Office</name>
   </variables>
   <variables>
      <defaultValue>GlobalVariable.Brand</defaultValue>
      <description></description>
      <id>eb23c6f1-9da6-40fb-9ca5-372d8d144878</id>
      <masked>false</masked>
      <name>Brand</name>
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
