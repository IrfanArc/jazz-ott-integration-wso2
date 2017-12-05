package Subscriber;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class PostPaidResposnse extends AbstractMediator {
	private StringBuilder buffer=new StringBuilder();
	private List<String> list=new ArrayList<>();
//	
	@Override
	public boolean mediate(MessageContext context) {
		list.clear();
		buffer.delete(0, buffer.length());
		System.out.println("Reponse body :"+context.getProperty("TOTAL_BILL"));
		String requestedAttributesXMLStr=context.getProperty("RequestedSubscriberAttributes").toString();
//		String xmlo=context.getProperty("RequestedSubscriberAttributes").toString();
//		String requestedAttributesXMLStr= context.getProperty("RequestedSubscriberAttributes").toString();
//		String requestedAttributesXMLStr = "<urn:GetSubscriberInfoRequest xmlns:urn=\"urn:vimpelcom:carrier-billing-hub:types\"><urn:CorrelationId>4ba5fbc1-21c7-4cd3-b5bf-8ae0cd4462124</urn:CorrelationId><urn:PartnerId>google</urn:PartnerId><urn:IMSI>410018609138154</urn:IMSI><urn:RequestedSubscriberAttributes><!--1 or more repetitions:--><urn:Attribute>limit</urn:Attribute><urn:Attribute>account_type</urn:Attribute><urn:Attribute>services</urn:Attribute><urn:Attribute>currency</urn:Attribute></urn:RequestedSubscriberAttributes></urn:GetSubscriberInfoRequest>";
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
//		
		System.out.println("Li size"+":"+list.size());
		for(int i=0; i<list.size(); i++){
			System.out.println("i"+i+":"+list.get(i));
			if(i==0){
				buffer.append("<ns0:SubscriberInfo xmlns:ns0=\"urn:vimpelcom:carrier-billing-hub:types\">");
			}
			if(list.get(i).trim().equals("limit")){
				String sv=context.getProperty("Post_TransactionID").toString();
				long v=Long.parseLong(sv);
				buffer.append("<ns0:Parameter>"
							+ "<ns0:Key>TransactionLimit</ns0:Key>"
							+ "<ns0:Value>"+(v*1000000) +"</ns0:Value>"
							+ "</ns0:Parameter>");
				System.out.println("limit:"+v);
//				context.setProperty("limit", v);
			}else if(list.get(i).trim().equals("PostpaidTotalPaymentDue")){
				System.out.println("V:"+list.get(i));
				String val=context.getProperty("TOTAL_BILL").toString();
				long v=Long.parseLong(val);
				System.out.println("V:"+v);
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>PostpaidTotalPaymentDue</ns0:Key>"
						+ "<ns0:Value>"+(v*1000000)+"</ns0:Value>"
						+ "</ns0:Parameter>");
//				context.setProperty("PostpaidTotalPaymentDue", v);
				System.out.println(buffer.toString());
			}else if(list.get(i).trim().equals("PostpaidUnbilledPaymentDue")){
				String val=context.getProperty("current_month").toString();
				long v=Long.parseLong(val);
				buffer.append("<ns0:Parameter>"
						+ "   <ns0:Key>PostpaidUnbilledPaymentDue</ns0:Key>"
						+ "   <ns0:Value>"+(v*1000000)+"</ns0:Value>"
						+ "</ns0:Parameter>");	
//				context.setProperty("PostpaidUnbilledPaymentDue", v);
			}else if(list.get(i).trim().equals("PostpaidPaymentDueDate")){
				
				buffer.append("<ns0:Parameter>"
						+ "   <ns0:Key>PostpaidBilledPaymentDue</ns0:Key>"
						+ "   <ns0:Value>"+context.getProperty("Due_DT") +"</ns0:Value>"
						+ "</ns0:Parameter>");	
//				context.setProperty("PostpaidUnbilledPaymentDue", context.getProperty("Due_DT"));
			}
			else if(list.get(i).trim().equals("PostpaidBilledPaymentDue")){
				String val=context.getProperty("unpaid").toString();
				long v=Long.parseLong(val);
				buffer.append("<ns0:Parameter>"
						+ "   <ns0:Key>PostpaidBilledPaymentDue</ns0:Key>"
						+ "   <ns0:Value>"+(v*1000000)+"</ns0:Value>"
						+ "</ns0:Parameter>");
//				context.setProperty("PostpaidBilledPaymentDue", context.getProperty("unpaid"));
			}
			else if(list.get(i).trim().equals("currency")){
				String C="PKR";
				String curr=context.getProperty("currencypr").toString();
				if(!curr.equals("")){
					C=curr;
				}
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>AccountCurrency</ns0:Key>"
						+ "   <ns0:Value>"+C+"</ns0:Value>"
						+ "</ns0:Parameter>");	
//				context.setProperty("PostpaidBilledPaymentDue","PKR");
			}
			else if(list.get(i).trim().equals("address")){
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>Address1</ns0:Key>"
						+ "<ns0:Value></ns0:Value>"
						+ "</ns0:Parameter>");	
			}
			else if(list.get(i).trim().equals("city")){
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>City</ns0:Key>"
						+ "<ns0:Value></ns0:Value>"
						+ "</ns0:Parameter>");	
			}
			else if(list.get(i).trim().equals("state")){
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>State</ns0:Key>"
						+ "<ns0:Value></ns0:Value>"
						+ "</ns0:Parameter>");	
			}
			else if(list.get(i).trim().equals("country")){
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>CountryCode</ns0:Key>"
						+ "<ns0:Value></ns0:Value>"
						+ "</ns0:Parameter>");	
			}
			else if(list.get(i).trim().equals("postal_code")){
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>PostalCode</ns0:Key>"
						+ "<ns0:Value></ns0:Value>"
						+ "</ns0:Parameter>");	
			}
			else if(list.get(i).trim().equals("services")){
				String offid=context.getProperty("OfferID").toString();
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
				buffer.append("<ns0:Parameter>"
								+ "<ns0:Key>PrepaidBalance</ns0:Key>"
								+ "<ns0:Value>PrepaidBalance1</ns0:Value>"
								+ "</ns0:Parameter>");	
			}
			else if(list.get(i).trim().equals("activeServiceList")){
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key>ActiveServiceList</ns0:Key>"
						+ "<ns0:Value>ActiveServiceList1</ns0:Value>"
						+ "</ns0:Parameter>");	
			}
			
			else if(list.get(i).trim().equals("account_type")){
				String offid=context.getProperty("accountType").toString().trim();
				if(offid.equals("postpaid")){
					buffer.append("<ns0:Parameter>"
							+ "<ns0:Key>AccountType</ns0:Key>"
							+ "<ns0:Value>postpaid</ns0:Value>"
							+ "</ns0:Parameter>");
//					context.setProperty("account_type","Prepaid");
				}else if(offid.equals("enterprise")){
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
		}
		buffer.append("</ns0:SubscriberInfo>");
		System.out.println("Repose"+ buffer.toString());
		context.setProperty("ReposneBodyPost", buffer.toString());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return true;
	}

}
