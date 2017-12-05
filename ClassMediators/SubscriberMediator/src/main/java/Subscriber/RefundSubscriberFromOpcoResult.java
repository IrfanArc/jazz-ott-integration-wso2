//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.18 at 04:18:49 PM PKT 
//


package Subscriber;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for refundSubscriberFromOpcoResult.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="refundSubscriberFromOpcoResult">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="SUCCESS"/>
 *     &lt;enumeration value="SUBSCRIBER_NOT_FOUND"/>
 *     &lt;enumeration value="INVALID_CORRELATION_ID"/>
 *     &lt;enumeration value="NOT_CHARGED"/>
 *     &lt;enumeration value="ALREADY_RELEASED"/>
 *     &lt;enumeration value="CHARGE_TOO_OLD"/>
 *     &lt;enumeration value="NOT_SUPPORTED"/>
 *     &lt;enumeration value="SYSTEM_DOWN"/>
 *     &lt;enumeration value="SYSTEM_BUSY"/>
 *     &lt;enumeration value="INVALID_DATA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "refundSubscriberFromOpcoResult")
@XmlEnum
public enum RefundSubscriberFromOpcoResult {

    SUCCESS,
    SUBSCRIBER_NOT_FOUND,
    INVALID_CORRELATION_ID,
    NOT_CHARGED,
    ALREADY_RELEASED,
    CHARGE_TOO_OLD,
    NOT_SUPPORTED,
    SYSTEM_DOWN,
    SYSTEM_BUSY,
    INVALID_DATA;

    public String value() {
        return name();
    }

    public static RefundSubscriberFromOpcoResult fromValue(String v) {
        return valueOf(v);
    }

}
