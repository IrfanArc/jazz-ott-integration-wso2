package Subscriber;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class PrePaidResponse  extends AbstractMediator {
	private StringBuilder buffer=new StringBuilder();
	private List<String> list=new ArrayList<>();
//	

	@Override
	public boolean mediate(MessageContext context) {
		System.out.println("getting values");
		Log logger = LogFactory.getLog(PrePaidResponse.class);
		String requestedAttributesXMLStr=context.getProperty("RequestedSubscriberAttributes").toString();
		log.info("Prepaid Classs");
		list.clear();
		buffer.delete(0, buffer.length());
		
		System.out.println("Prepaid:");
		try {
		JAXBContext jaxbCtx = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
		JAXBElement<GetSubscriberInfoRequest> requestElement = (JAXBElement<GetSubscriberInfoRequest>) unmarshaller.unmarshal(new ByteArrayInputStream(requestedAttributesXMLStr.getBytes()));

		GetSubscriberInfoRequest request = requestElement.getValue();
		
		List<RequestedSubscriberAttribute> attrList = request.getRequestedSubscriberAttributes().getAttribute();
		list.clear();
		for (RequestedSubscriberAttribute attr : attrList) {
			System.out.println(attr.value());
			list.add(attr.value().trim());
		}
//		if(xmlo.contains("xmlns:p=\"urn:vimpelcom:carrier-billing-hub:types\"")){
//			String m=xmlo.replace("xmlns:p=\"urn:vimpelcom:carrier-billing-hub:types\"", "");
//			String n=m.replace("p:RequestedSubscriberAttributes ", "p:RequestedSubscriberAttributes");
//			String mv=n.replace("p:", "");
//			String mm=mv.replace("</RequestedSubscriberAttributes>", "").replace("<RequestedSubscriberAttributes>", "");
//			String arm=mm.replace("<Attribute>", "").replace("</Attribute>", ",");
//			String ma=arm.substring(0, arm.length()-1);
//			String [] liststr=ma.split(",");
//			list.clear();
//			for (String str : liststr) {
//				list.add(str.trim());
//			}
//		}else if(xmlo.contains("xmlns:urn=\"urn:vimpelcom:carrier-billing-hub:types\"")){
//			String m=xmlo.replace("xmlns:urn=\"urn:vimpelcom:carrier-billing-hub:types\"", "");
//			String n=m.replace("urn:RequestedSubscriberAttributes ", "urn:RequestedSubscriberAttributes");
//			String mv=n.replace("urn:", "");
//			String mm=mv.replace("</RequestedSubscriberAttributes>", "").replace("<RequestedSubscriberAttributes>", "");
//			String arm=mm.replace("<Attribute>", "").replace("</Attribute>", ",");
//			String ma=arm.substring(0, arm.length()-1);
//			String [] liststr=ma.split(",");
//			list.clear();
//			for (String str : liststr) {
//				list.add(str.trim());
//			}
//		}else{
//			String m=xmlo.replace("xmlns=\"urn:vimpelcom:carrier-billing-hub:types\"", "");
//			String n=m.replace("RequestedSubscriberAttributes ", "RequestedSubscriberAttributes");
//			String mm=n.replace("</RequestedSubscriberAttributes>", "").replace("<RequestedSubscriberAttributes>", "");
//			String arm=mm.replace("<Attribute>", "").replace("</Attribute>", ",");
//			String ma=arm.substring(0, arm.length()-1);
//			String [] liststr=ma.split(",");
//			list.clear();
//			for (String str : liststr) {
//				list.add(str.trim());
//			}
//		}
//		System.out.println("reqAr:"+xmlo);
//		log.info("attributes"+xmlo);
		
		log.info("Prepaid Classs");
		System.out.println("Pre Li size"+":"+list.size());
		for(int i=0; i<list.size(); i++){
			System.out.println("Pi"+i+":"+list.get(i));
			if(i==0){
				buffer.append("<ns0:SubscriberInfo xmlns:ns0=\"urn:vimpelcom:carrier-billing-hub:types\">");
			}
			if(list.get(i).trim().equals("limit")){
				
				String sv=context.getProperty("Post_TransactionID").toString();
				long v=Long.parseLong(sv);
				logger.debug("limit in classs");
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>TransactionLimit</ns0:Key>"
						+ "<ns0:Value>"+(v*1000000)+"</ns0:Value>"
						+ "</ns0:Parameter>");
//			System.out.println("limit:"+v);
//				context.setProperty("limit",v);
			}
			else if(list.get(i).trim().equals("services")){
				String offid="";
				
				offid=context.getProperty("OfferID").toString();
				if(offid.trim().equals("1380")){
					buffer.append("<ns0:Parameter>"
							+ "<ns0:Key>PartnerServices</ns0:Key>"
							+ "<ns0:Value> </ns0:Value>"
							+ "</ns0:Parameter>");
				}else{
					buffer.append("<ns0:Parameter>"
							+ "<ns0:Key>PartnerServices</ns0:Key>"
							+ "<ns0:Value>google.play</ns0:Value>"
							+ "</ns0:Parameter>");
				}
		
			}
			else if(list.get(i).trim().equals("PrepaidBalance")){
				String sv=context.getProperty("accoountvalue1").toString();
				long v=Long.parseLong(sv);
//				long av=Long.parseLong(context.getProperty("accoountvalue1").toString())*1000000;
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>PrepaidBalance</ns0:Key>"
						+ "<ns0:Value>"+v*1000000+"</ns0:Value>"
						+ "</ns0:Parameter>");
//				context.setProperty("PrepaidBalance",av);
			}
			else if(list.get(i).trim().equals("activeServiceList")){
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>ActiveServiceList</ns0:Key>"
						+ "<ns0:Value>ActiveServiceList1</ns0:Value>"
						+ "</ns0:Parameter>");
				//				context.setProperty("ActiveServiceList","");
			}
			else if(list.get(i).trim().equals("account_type")){
				
				String offid=context.getProperty("accountType").toString().trim();
				if(offid.trim().equals("postpaid")){
					buffer.append("<ns0:Parameter>"
							+ "<ns0:Key>AccountType</ns0:Key>"
							+ "<ns0:Value>postpaid</ns0:Value>"
							+ "</ns0:Parameter>");
//					context.setProperty("account_type","Prepaid");
				}else if(offid.trim().equals("enterprise")){
					buffer.append("<ns0:Parameter>"
							+ "<ns0:Key>AccountType</ns0:Key>"
							+ "<ns0:Value>enterprise</ns0:Value>"
							+ "</ns0:Parameter>");
//					context.setProperty("account_type","Postpaid");
				}else{
					buffer.append("<ns0:Parameter>"
							+ "<ns0:Key>AccountType</ns0:Key>"
							+ "<ns0:Value>prepaid</ns0:Value>"
							+ "</ns0:Parameter>");
//					context.setProperty("account_type","Postpaid");
				}
			}
			else if(list.get(i).trim().equals("currency")){
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>AccountCurrency</ns0:Key>"
						+ "<ns0:Value>"+context.getProperty("currencypr").toString()+"</ns0:Value>"
						+ "</ns0:Parameter>");
//				context.setProperty("currency","PKR");
			}
			else if(list.get(i).trim().equals("address")){
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>Address1</ns0:Key>"
						+ "<ns0:Value> </ns0:Value>"
						+ "</ns0:Parameter>");
//				context.setProperty("address","");
			}
			else if(list.get(i).trim().equals("city")){
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>City</ns0:Key>"
						+ "<ns0:Value> </ns0:Value>"
						+ "</ns0:Parameter>");
//				context.setProperty("city","");
			}else if(list.get(i).trim().equals("country")){
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>CountryCode</ns0:Key>"
						+ "<ns0:Value> </ns0:Value>"
						+ "</ns0:Parameter>");
//				context.setProperty("country","");
			}
			else if(list.get(i).trim().equals("postal_code")){
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>PostalCode</ns0:Key>"
						+ "<ns0:Value> </ns0:Value>"
						+ "</ns0:Parameter>");
				
//				context.setProperty("postal_code","");
			}
			
			else if(list.get(i).trim().equals("state")){
					buffer.append("<ns0:Parameter>"
							+ "<ns0:Key>State</ns0:Key>"
							+ "<ns0:Value> </ns0:Value>"
							+ "</ns0:Parameter>");	
			}
			
		}
		buffer.append("</ns0:SubscriberInfo>");
		System.out.println("Repose Pre"+ buffer.toString().trim());
		context.setProperty("ReposneBodyPrepaid", buffer.toString());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return true;
	}

}
