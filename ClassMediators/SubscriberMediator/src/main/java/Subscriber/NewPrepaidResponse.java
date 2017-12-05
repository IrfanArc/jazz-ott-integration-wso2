package Subscriber;

import java.util.ArrayList;
import java.util.List;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class NewPrepaidResponse extends AbstractMediator{
	private StringBuilder buffer=new StringBuilder();
	private List<String> list=new ArrayList<>();
//	
	@Override
	public boolean mediate(MessageContext context) {
		String xmlo=context.getProperty("Attributes").toString();
		if(xmlo.contains("xmlns:p=\"urn:vimpelcom:carrier-billing-hub:types\"")){
			String m=xmlo.replace("xmlns:p=\"urn:vimpelcom:carrier-billing-hub:types\"", "");
			String n=m.replace("p:RequestedSubscriberAttributes ", "p:RequestedSubscriberAttributes");
			String mv=n.replace("p:", "");
			String mm=mv.replace("</RequestedSubscriberAttributes>", "").replace("<RequestedSubscriberAttributes>", "");
			String arm=mm.replace("<Attribute>", "").replace("</Attribute>", ",");
			String ma=arm.substring(0, arm.length()-1);
			String [] liststr=ma.split(",");
			list.clear();
			for (String str : liststr) {
				list.add(str.trim());
			}
		}else if(xmlo.contains("xmlns:urn=\"urn:vimpelcom:carrier-billing-hub:types\"")){
			String m=xmlo.replace("xmlns:urn=\"urn:vimpelcom:carrier-billing-hub:types\"", "");
			String n=m.replace("urn:RequestedSubscriberAttributes ", "urn:RequestedSubscriberAttributes");
			String mv=n.replace("urn:", "");
			String mm=mv.replace("</RequestedSubscriberAttributes>", "").replace("<RequestedSubscriberAttributes>", "");
			String arm=mm.replace("<Attribute>", "").replace("</Attribute>", ",");
			String ma=arm.substring(0, arm.length()-1);
			String [] liststr=ma.split(",");
			list.clear();
			for (String str : liststr) {
				list.add(str.trim());
			}
		}else{
			String m=xmlo.replace("xmlns=\"urn:vimpelcom:carrier-billing-hub:types\"", "");
			String n=m.replace("RequestedSubscriberAttributes ", "RequestedSubscriberAttributes");
			String mm=n.replace("</RequestedSubscriberAttributes>", "").replace("<RequestedSubscriberAttributes>", "");
			String arm=mm.replace("<Attribute>", "").replace("</Attribute>", ",");
			String ma=arm.substring(0, arm.length()-1);
			String [] liststr=ma.split(",");
			list.clear();
			for (String str : liststr) {
				list.add(str.trim());
			}
		}
		System.out.println("reqAr:"+xmlo);
		log.info("attributes"+xmlo);
		
		log.info("Prepaid Classs NEw");
		return true;
	}

}
