package Subscriber;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class RequestedAttribuesGroup extends AbstractMediator {
	
	private List<String> RequestedAttrList=new ArrayList<>();
	
	@Override
	public boolean mediate(MessageContext context) {
		String requestedAttributesXMLStr= context.getProperty("RequestedSubscriberAttributes").toString();
		ArrayList<String> ValidAttributes=new ArrayList<>();
		ValidAttributes.clear();
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
		ValidAttributes.add("address");
//		ValidAttributes.add("PostpaidTotalPaymentDue");
//		ValidAttributes.add("PostpaidUnbilledPaymentDue");
//		ValidAttributes.add("PostpaidBilledPaymentDue");
//		ValidAttributes.add("PostpaidPaymentDueDate");
		try {
			log.debug("[Starting RequestedAttribuesGroup]");
					JAXBContext jaxbCtx = JAXBContext.newInstance(ObjectFactory.class);
					Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
					JAXBElement<GetSubscriberInfoRequest> requestElement = (JAXBElement<GetSubscriberInfoRequest>) unmarshaller.unmarshal(new ByteArrayInputStream(requestedAttributesXMLStr.getBytes()));
	
					GetSubscriberInfoRequest request = requestElement.getValue();
					
					List<RequestedSubscriberAttribute> attrList = request.getRequestedSubscriberAttributes().getAttribute();
					String valid="true";
					RequestedAttrList.clear();
					for (RequestedSubscriberAttribute attr : attrList) {
						 if (attr == null) {
							 valid = "false";
			                    break;
			                } else if (ValidAttributes.contains(attr.value())) {
			                	valid = "true";
			                } else {
			                	valid = "false";
			                    break;
			                }
					}
					log.debug("[setting RequestedAttrList]");		
					
					
					log.debug("[ending ValidAttributes RequestedAttrList]");
					
					
//					for(int j=0; j<RequestedAttrList.size(); j++){
//						System.out.println("str l1"+""+RequestedAttrList.get(j));
//						boolean v=containsSubString(ValidAttributes, RequestedAttrList.get(j).trim());
//						if(!v){
//							valid="false";
//							System.out.println(v+": false:"+RequestedAttrList.get(j).trim());
//							context.setProperty("IsNOTValidDataName", RequestedAttrList.get(j).trim());
//							break;
//						}
//					}		
//		String xmlo=context.getProperty("RequestedSubscriberAttributes").toString();
//		
//		context.setProperty("AttributesValues", xmlo);
//		System.out.println("reqAr:"+xmlo);
//		if(xmlo.contains("xmlns:p=\"urn:vimpelcom:carrier-billing-hub:types\"")){
//			String m=xmlo.replace("xmlns:p=\"urn:vimpelcom:carrier-billing-hub:types\"", "");
//			String n=m.replace("p:RequestedSubscriberAttributes ", "p:RequestedSubscriberAttributes");
//			String mv=n.replace("p:", "");
//			String mm=mv.replace("</RequestedSubscriberAttributes>", "").replace("<RequestedSubscriberAttributes>", "");
//			String arm=mm.replace("<Attribute>", "").replace("</Attribute>", ",");
//			String ma=arm.substring(0, arm.length()-1);
//			String [] liststr=ma.split(",");
//			RequestedAttrList.clear();
//			for (String str : liststr) {
//				RequestedAttrList.add(str.trim());
//			}
//		}else if(xmlo.contains("xmlns:urn=\"urn:vimpelcom:carrier-billing-hub:types\"")){
//			String m=xmlo.replace("xmlns:urn=\"urn:vimpelcom:carrier-billing-hub:types\"", "");
//			String n=m.replace("urn:RequestedSubscriberAttributes ", "urn:RequestedSubscriberAttributes");
//			String mv=n.replace("urn:", "");
//			String mm=mv.replace("</RequestedSubscriberAttributes>", "").replace("<RequestedSubscriberAttributes>", "");
//			String arm=mm.replace("<Attribute>", "").replace("</Attribute>", ",");
//			String ma=arm.substring(0, arm.length()-1);
//			String [] liststr=ma.split(",");
//			RequestedAttrList.clear();
//			for (String str : liststr) {
//				RequestedAttrList.add(str.trim());
//			}
//		}else{
//			String m=xmlo.replace("xmlns=\"urn:vimpelcom:carrier-billing-hub:types\"", "");
//			String n=m.replace("RequestedSubscriberAttributes ", "RequestedSubscriberAttributes");
//			String mm=n.replace("</RequestedSubscriberAttributes>", "").replace("<RequestedSubscriberAttributes>", "");
//			String arm=mm.replace("<Attribute>", "").replace("</Attribute>", ",");
//			String ma=arm.substring(0, arm.length()-1);
//			String [] liststr=ma.split(",");
//			RequestedAttrList.clear();
//			for (String str : liststr) {
//				RequestedAttrList.add(str.trim());
//			}
//		}
					log.debug("[setting Postpaid RequestedAttrList]");
		String IsPostOrPrepaid="Prepaid";
//		for(int i=0; i<RequestedAttrList.size(); i++){
			for(int j=0; j<RequestedAttrList.size(); j++){
				System.out.println("i::"+j);
				if("account_type".equals(RequestedAttrList.get(j).trim())){
					System.out.println("iacctype::"+j);
					context.setProperty("CurrentPrepaidValue", "account_type");
					IsPostOrPrepaid="Prepaid"; break;
				}else if("services".equals(RequestedAttrList.get(j).trim())){
					context.setProperty("CurrentPrepaidValue", "services");
					System.out.println("iservice::"+j);
					IsPostOrPrepaid="Prepaid";break;
				}else if("PrepaidBalance".equals(RequestedAttrList.get(j).trim())){
					context.setProperty("CurrentPrepaidValue", "PrepaidBalance");
					System.out.println("iPreBal::"+j);
					IsPostOrPrepaid="Prepaid";break;
				} else if("activeServiceList".equals(RequestedAttrList.get(j).trim())){
					context.setProperty("CurrentPrepaidValue", "activeServiceList");
					System.out.println("iacList::"+j);
					IsPostOrPrepaid="Prepaid";break;
				}
//			}
				System.out.println("End::"+j);
		}
			
		System.out.println("IsNOTValidDataAttributes:::"+valid);
		context.setProperty("IsNOTValidDataAttributes", valid);
		System.out.println("IsPostOrPrepaid:::"+IsPostOrPrepaid);
		context.setProperty("RequestedAttPrePostpaid", IsPostOrPrepaid);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return true;
	}
		public static boolean containsSubString(ArrayList<String> stringArray, String substring){
			
		    for (String string : stringArray){
		        if (string.contains(substring.trim())) return true;
		    }
		    	System.out.println("bol sgtr:"+"false");
		    return false;
		    
		}


	}
