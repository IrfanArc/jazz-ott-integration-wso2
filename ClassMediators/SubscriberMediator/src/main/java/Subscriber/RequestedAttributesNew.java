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

public class RequestedAttributesNew extends AbstractMediator {
	private List<String> RequestedAttrList=new ArrayList<>();
	@Override
	public boolean mediate(MessageContext context) {
		String requestedAttributesXMLStr= context.getProperty("RequestedSubscriberAttributes").toString();
		try {
			
					JAXBContext jaxbCtx = JAXBContext.newInstance(ObjectFactory.class);
					Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
					JAXBElement<GetSubscriberInfoRequest> requestElement = (JAXBElement<GetSubscriberInfoRequest>) unmarshaller.unmarshal(new ByteArrayInputStream(requestedAttributesXMLStr.getBytes()));
	
					GetSubscriberInfoRequest request = requestElement.getValue();
					
					List<RequestedSubscriberAttribute> attrList = request.getRequestedSubscriberAttributes().getAttribute();
					RequestedAttrList.clear();
					for (RequestedSubscriberAttribute attr : attrList) {
						System.out.println(attr.value());
						RequestedAttrList.add(attr.value().trim());
					}
					String IsPostOrPrepaid="Postpaid";
//					for(int i=0; i<RequestedAttrList.size(); i++){
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
//						}
							System.out.println("End::"+j);
					}
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
					ValidAttributes.add("PostpaidTotalPaymentDue");
					ValidAttributes.add("PostpaidUnbilledPaymentDue");
					ValidAttributes.add("PostpaidBilledPaymentDue");
					ValidAttributes.add("PostpaidPaymentDueDate");
					
					String valid="true";
					for(int j=0; j<RequestedAttrList.size(); j++){
						System.out.println("str l1"+""+RequestedAttrList.get(j));
						boolean v=containsSubString(ValidAttributes, RequestedAttrList.get(j).trim());
						if(!v){
							valid="false";
							System.out.println(v+": false:"+RequestedAttrList.get(j).trim());
							context.setProperty("IsNOTValidDataName", RequestedAttrList.get(j).trim());
							break;
						}
					}
					
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

