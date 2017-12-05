package Subscriber;

import java.util.ArrayList;
import java.util.List;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class TestResponse extends AbstractMediator{
	private StringBuilder buffer=new StringBuilder();
	private List<String> list=new ArrayList<>();
	@Override
	public boolean mediate(MessageContext context) {
		
		String xmlo="<p:RequestedSubscriberAttributes xmlns:p=\"urn:vimpelcom:carrier-billing-hub:types\"><p:Attribute>PostpaidTotalPaymentDue</p:Attribute><p:Attribute>limit</p:Attribute></p:RequestedSubscriberAttributes>";
		String m=xmlo.replace("xmlns:p=\"urn:vimpelcom:carrier-billing-hub:types\"", "");
		String n=m.replace("p:RequestedSubscriberAttributes ", "p:RequestedSubscriberAttributes");
		String mv=n.replace("p:", "");
		String mm=mv.replace("</RequestedSubscriberAttributes>", "").replace("<RequestedSubscriberAttributes>", "");
		String arm=mm.replace("<Attribute>", "").replace("</Attribute>", ",");
		String ma=arm.substring(0, arm.length()-1);
		String [] lisat=ma.split(",");
		for (String str : lisat) {
		 System.out.println("values :::"+str);
		 list.add(str);
		}
		System.out.println(ma);
		for(int i=0; i<list.size(); i++){
			System.out.println("Pi"+i+":"+list.get(i));
			
			if(list.get(i).equals("limit")){
//				long v=Long.parseLong(context.getProperty("TOTAL_BILL").toString())*1000000;
				buffer.append("<ns0:Parameter>\n"
						+ "<ns0:Key>TransactionLimit</ns0:Key>\n"
						+ "<ns0:Value>"+100000 +"</ns0:Value>\n"
						+ "</ns0:Parameter>");
				context.setProperty("limit", 10000);
			}else if(list.get(i).equals("PostpaidPaymentDueDate")){
				buffer.append("<ns0:Parameter>"
						+ "<ns0:Key><PostpaidBilledPaymentDue></ns0:Key>"
						+ "<ns0:Value>2017-10-12</ns0:Value>"
						+ "</ns0:Parameter>");	
				
				context.setProperty("PostpaidPaymentDueDate","2017-10-12");
			}
		}
		System.out.println("Repose Pre"+ buffer.toString());
		context.setProperty("ReposneBodyPrepaid", buffer.toString());
		
		return true;
	}

}
