//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.18 at 04:18:49 PM PKT 
//


package Subscriber;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for getSubscriberInfoRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getSubscriberInfoRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CorrelationId" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN"/>
 *         &lt;element name="PartnerId" type="{urn:vimpelcom:carrier-billing-hub:types}partnerId"/>
 *         &lt;element name="IMSI" type="{urn:vimpelcom:carrier-billing-hub:types}IMSI"/>
 *         &lt;element name="RequestedSubscriberAttributes" type="{urn:vimpelcom:carrier-billing-hub:types}requestedSubscriberAttributes"/>
 *         &lt;element name="UserLocale" type="{urn:vimpelcom:carrier-billing-hub:types}localeCode" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSubscriberInfoRequest", propOrder = {
    "correlationId",
    "partnerId",
    "imsi",
    "requestedSubscriberAttributes",
    "userLocale"
})
public class GetSubscriberInfoRequest {

    @XmlElement(name = "CorrelationId", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String correlationId;
    @XmlElement(name = "PartnerId", required = true)
    protected String partnerId;
    @XmlElement(name = "IMSI", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String imsi;
    @XmlElement(name = "RequestedSubscriberAttributes", required = true)
    protected RequestedSubscriberAttributes requestedSubscriberAttributes;
    @XmlElement(name = "UserLocale")
    protected String userLocale;

    /**
     * Gets the value of the correlationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * Sets the value of the correlationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorrelationId(String value) {
        this.correlationId = value;
    }

    /**
     * Gets the value of the partnerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartnerId() {
        return partnerId;
    }

    /**
     * Sets the value of the partnerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartnerId(String value) {
        this.partnerId = value;
    }

    /**
     * Gets the value of the imsi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIMSI() {
        return imsi;
    }

    /**
     * Sets the value of the imsi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIMSI(String value) {
        this.imsi = value;
    }

    /**
     * Gets the value of the requestedSubscriberAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link RequestedSubscriberAttributes }
     *     
     */
    public RequestedSubscriberAttributes getRequestedSubscriberAttributes() {
        return requestedSubscriberAttributes;
    }

    /**
     * Sets the value of the requestedSubscriberAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestedSubscriberAttributes }
     *     
     */
    public void setRequestedSubscriberAttributes(RequestedSubscriberAttributes value) {
        this.requestedSubscriberAttributes = value;
    }

    /**
     * Gets the value of the userLocale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserLocale() {
        return userLocale;
    }

    /**
     * Sets the value of the userLocale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserLocale(String value) {
        this.userLocale = value;
    }

}
