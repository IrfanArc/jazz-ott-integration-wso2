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


/**
 * <p>Java class for releaseFundsCallback complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="releaseFundsCallback">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vimpelcom:carrier-billing-hub:types}billingOperationCallback">
 *       &lt;sequence>
 *         &lt;element name="Result" type="{urn:vimpelcom:carrier-billing-hub:types}releaseFundsResult"/>
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "releaseFundsCallback", propOrder = {
    "result",
    "message"
})
public class ReleaseFundsCallback
    extends BillingOperationCallback
{

    @XmlElement(name = "Result", required = true)
    @XmlSchemaType(name = "NMTOKEN")
    protected ReleaseFundsResult result;
    @XmlElement(name = "Message")
    protected String message;

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link ReleaseFundsResult }
     *     
     */
    public ReleaseFundsResult getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReleaseFundsResult }
     *     
     */
    public void setResult(ReleaseFundsResult value) {
        this.result = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

}
