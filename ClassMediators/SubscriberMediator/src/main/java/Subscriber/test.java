package Subscriber;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.*; 
import org.w3c.dom.*; 
import org.xml.sax.*;

import com.oracle.xmlns.internal.webservices.jaxws_databinding.ObjectFactory; 

public class test { 



public static void main(String ar[]){
	 ArrayList<String> RequestedAttrList=new ArrayList<>();
	
	String xmlo="<RequestedSubscriberAttributes xmlns=\"urn:vimpelcom:carrier-billing-hub:types\">"
			+ "<Attribute>services</Attribute><Attribute>services</Attribute><Attribute>limit</Attribute>"
			+ "<Attribute>city</Attribute>"
			+ "<Attribute>postal_code</Attribute>"
			+ "<Attribute>country</Attribute>"
			+ "<Attribute>activeServiceList</Attribute>"
			+ "<Attribute>five</Attribute>"
			+ "</RequestedSubscriberAttributes>";
	System.out.println("reqAr:"+xmlo);
	if(xmlo.contains("xmlns:p=\"urn:vimpelcom:carrier-billing-hub:types\"")){
		String m=xmlo.replace("xmlns:p=\"urn:vimpelcom:carrier-billing-hub:types\"", "");
		String n=m.replace("p:RequestedSubscriberAttributes ", "p:RequestedSubscriberAttributes");
		String mv=n.replace("p:", "");
		String mm=mv.replace("</RequestedSubscriberAttributes>", "").replace("<RequestedSubscriberAttributes>", "");
		String arm=mm.replace("<Attribute>", "").replace("</Attribute>", ",");
		String ma=arm.substring(0, arm.length()-1);
		String [] liststr=ma.split(",");
		RequestedAttrList.clear();
		for (String str : liststr) {
			RequestedAttrList.add(str);
		}
	}else if(xmlo.contains("xmlns:urn=\"urn:vimpelcom:carrier-billing-hub:types\"")){
		String m=xmlo.replace("xmlns:urn=\"urn:vimpelcom:carrier-billing-hub:types\"", "");
		String n=m.replace("urn:RequestedSubscriberAttributes ", "urn:RequestedSubscriberAttributes");
		String mv=n.replace("urn:", "");
		String mm=mv.replace("</RequestedSubscriberAttributes>", "").replace("<RequestedSubscriberAttributes>", "");
		String arm=mm.replace("<Attribute>", "").replace("</Attribute>", ",");
		String ma=arm.substring(0, arm.length()-1);
		String [] liststr=ma.split(",");
		RequestedAttrList.clear();
		for (String str : liststr) {
			RequestedAttrList.add(str);
		}
	}else{
		String m=xmlo.replace("xmlns=\"urn:vimpelcom:carrier-billing-hub:types\"", "");
		String n=m.replace("RequestedSubscriberAttributes ", "RequestedSubscriberAttributes");
		String mm=n.replace("</RequestedSubscriberAttributes>", "").replace("<RequestedSubscriberAttributes>", "");
		String arm=mm.replace("<Attribute>", "").replace("</Attribute>", ",");
		String ma=arm.substring(0, arm.length()-1);
		String [] liststr=ma.split(",");
		RequestedAttrList.clear();
		for (String str : liststr) {
			RequestedAttrList.add(str);
		}
	}
	
	String IsPostOrPrepaid="Postpaid";
//	for(int i=0; i<RequestedAttrList.size(); i++){
		for(int j=0; j<RequestedAttrList.size(); j++){
			System.out.println("i::"+j);
			if("account_type".equals(RequestedAttrList.get(j))){
				System.out.println("iacctype::"+j);
//				context.setProperty("CurrentPrepaidValue", "account_type");
				IsPostOrPrepaid="Prepaid"; break;
				
			}else if("services".equals(RequestedAttrList.get(j))){
//				context.setProperty("CurrentPrepaidValue", "services");
				System.out.println("iservice::"+j);
				IsPostOrPrepaid="Prepaid";break;
			}else if("PrepaidBalance".equals(RequestedAttrList.get(j))){
//				context.setProperty("CurrentPrepaidValue", "PrepaidBalance");
				System.out.println("iPreBal::"+j);
				IsPostOrPrepaid="Prepaid";break;
			} else if("activeServiceList".equals(RequestedAttrList.get(j))){
//				context.setProperty("CurrentPrepaidValue", "activeServiceList");
				System.out.println("iacList::"+j);
				IsPostOrPrepaid="Prepaid";break;
			}
//		}
			System.out.println("End::"+j);
	}
		
		ArrayList<String> ValidAttributes=new ArrayList<>();
			ValidAttributes.add("account_type");
			ValidAttributes.add("services");
			ValidAttributes.add("PrepaidBalance");
			ValidAttributes.add("activeServiceList");
			ValidAttributes.add("postal_code");
			ValidAttributes.add("state");
			ValidAttributes.add("city");
			ValidAttributes.add("country");
			ValidAttributes.add("currency");
			ValidAttributes.add("state");
			ValidAttributes.add("limit");
			ValidAttributes.add("PostpaidTotalPaymentDue");
			ValidAttributes.add("PostpaidUnbilledPaymentDue");
			ValidAttributes.add("PostpaidBilledPaymentDue");
			ValidAttributes.add("PostpaidPaymentDueDate");
		
			int Notvalid=0;
			
			for(int j=0; j<RequestedAttrList.size(); j++){
				System.out.println("str l1"+""+RequestedAttrList.get(j));
				boolean v=containsSubString(ValidAttributes, RequestedAttrList.get(j));
				if(!v){
					System.out.println(v+": false:"+RequestedAttrList.get(j));
					break;
				}
			}
}
public static boolean containsSubString(ArrayList<String> stringArray, String substring){
	
    for (String string : stringArray){
        if (string.contains(substring)) return true;
    }
    System.out.println("bol sgtr:"+"false");
    return false;
    
}
 
	public static void getXmlDat(){
//	<p:RequestedSubscriberAttributes xmlns:p="urn:vimpelcom:carrier-billing-hub:types"><p:Attribute>PostpaidTotalPaymentDue</p:Attribute><p:Attribute>limit</p:Attribute></p:RequestedSubscriberAttributes>

	String xmlo="<p:RequestedSubscriberAttributes xmlns:p=\"urn:vimpelcom:carrier-billing-hub:types\"><p:Attribute>PostpaidTotalPaymentDue</p:Attribute><p:Attribute>limit</p:Attribute></p:RequestedSubscriberAttributes>";
//	String xmlo="<p:RequestedSubscriberAttributes xmlns:p='urn:vimpelcom:carrier-billing-hub:types'><p:Attribute>postal_code</p:Attribute><p:Attribute>account_service</p:Attribute><p:Attribute>account_type</p:Attribute><p:Attribute>limit</p:Attribute><p:Attribute>currency</p:Attribute></p:RequestedSubscriberAttributes>";
	String m=xmlo.replace("xmlns:p=\"urn:vimpelcom:carrier-billing-hub:types\"", "");
	String n=m.replace("p:RequestedSubscriberAttributes ", "p:RequestedSubscriberAttributes");
//	String nv=m.substring(3, n.length()-3);
	String mv=n.replace("p:", "");
	String mm=mv.replace("</RequestedSubscriberAttributes>", "").replace("<RequestedSubscriberAttributes>", "");
	String arm=mm.replace("<Attribute>", "").replace("</Attribute>", ",");
	String ma=arm.substring(0, arm.length()-1);
	String [] list=ma.split(",");
	for (String str : list) {
	 System.out.println("values :::"+str);	
	}
	System.out.println(ma);
}

}