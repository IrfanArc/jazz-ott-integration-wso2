package com.arcana.reservefund;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext; 
import org.apache.synapse.mediators.AbstractMediator;

public class ReservePostpaid extends AbstractMediator {

	Log logger = LogFactory.getLog(ReservePostpaid.class);
	
	@Override
	public boolean mediate(MessageContext msgCtx) {
		
		logger.debug("[ReservePostpaid] Init");
		  String mssidn=(String)msgCtx.getProperty("NORMALIZED_MSISDN_FOR_IN");
		  String amountStr=(String)msgCtx.getProperty("pkr");
		  Integer amount = Integer.parseInt(amountStr);
		  String desc=(String)msgCtx.getProperty("Description");
		  mssidn = "0"+mssidn;
		
		boolean billingAccountFound = false;
		String isSuccessful = "FALSE";
		
		String billingAccountQuery = "select ACCOUNT_NUM from custeventsource ces , custproductdetails cpd where CES.CUSTOMER_REF = CPD.CUSTOMER_REF and CES.PRODUCT_SEQ = CPD.PRODUCT_SEQ and CES.END_DTM is null and CPD.END_DAT is null and CES.EVENT_TYPE_ID = 9 and CES.EVENT_SOURCE NOT LIKE '4100%' and CES.EVENT_SOURCE = '"+mssidn+"'";
		
		CallableStatement callableStatement = null;
		
		Hashtable environment = new Hashtable();
	    environment.put("java.naming.factory.initial", "org.wso2.carbon.tomcat.jndi.CarbonJavaURLContextFactory");
	    Context initContext;
	    Connection conn = null;
	    
		try {
			
			amount = Integer.parseInt(amountStr);
			String billingAccountNum = "";
			initContext = new InitialContext(environment);
			DataSource ds = (DataSource)initContext.lookup("jdbc/geneva");
			
			if (ds != null) {
				conn = ds.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet res = stmt.executeQuery(billingAccountQuery);
				
				if (!res.isBeforeFirst()) {    
				    System.out.println("No data");
				    billingAccountFound = false;
				} else {
					res.next();
					billingAccountNum = res.getString("ACCOUNT_NUM");
					billingAccountFound = true;
					logger.info("[ReservePostpaid] BillingAccountNumber: " + billingAccountNum + " for MSISDN: " + mssidn);
				}
				
				stmt.close();
				
				if (billingAccountFound) {
					
					  String query = "{call GENEVA_ADMIN.IPGPMCLCONTENTCHARGING.MANAGEOTCCREATION(?,?,?,?,?,?,?,?,?,?,?)}";
					  System.out.println("Msisdn in Java Class Query:"+query);
					  try {
						  log.debug("[ReservePostpaid] Calling the MANAGING OTC CREATIONS SP");
					    callableStatement = conn.prepareCall(query);
					   
					    callableStatement.setString(1, "Google");
						callableStatement.setString(2, "GooglePlayStore");
						callableStatement.setString(3, "Online Purchases");
						callableStatement.setString(4, "123");
						callableStatement.setString(5, desc);
						callableStatement.setString(6, "PKR");
						callableStatement.setString(7, mssidn);
						callableStatement.setInt(8, 3);
						callableStatement.setInt(9, amount);
						callableStatement.setString(10, billingAccountNum);
						callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);


						// execute getDBUSERByUserId store procedure
						callableStatement.executeUpdate();

						String result = callableStatement.getString(11);
						log.debug("[ReserveFunds] SP Execution finished, we got: " + result);
						if (result.contains("Transaction Success")){
							msgCtx.setProperty("status", "OK");
						}
						else{
							msgCtx.setProperty("status", "KO");
							msgCtx.setProperty("reason", result);
						}
						
					  }   catch (SQLTimeoutException e){
						  msgCtx.setProperty("status", "timeout");
					  }
					  catch (SQLException e) {
						   e.printStackTrace();
						   System.out.println(e.getMessage());

						  } finally {

						   if (callableStatement != null) {
							   callableStatement.close();
							   conn.close();
						   }
						  }
			
				}
			}
		}
		catch (NumberFormatException ne) {
			ne.printStackTrace();
			logger.debug("[ReservePostpaid] + Error= " + ne.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			logger.debug("[ReservePostpaid] + Error= " + e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug("[ReservePostpaid] + Error= " + e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					log.debug("[ReservePostpaid] Error in closing database connection!");
					e.printStackTrace();
				}
				
			}
		}
		return true;
	}

}