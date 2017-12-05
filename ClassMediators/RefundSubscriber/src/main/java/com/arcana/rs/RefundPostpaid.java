package com.arcana.rs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class RefundPostpaid extends AbstractMediator {

	Log logger = LogFactory.getLog(RefundPostpaid.class);
	
	@Override
	public boolean mediate(MessageContext msgCtx) {
		
		logger.debug("[RefundPostpaid] Init");
		String msisdn = (String)msgCtx.getProperty("MSISDN");
		msisdn = "0"+msisdn;
		String amountStr = (String)msgCtx.getProperty("AMOUNT_PKR");
		logger.debug("[RefundPostpaid] MSISDN=" + msisdn);
		logger.debug("[RefundPostpaid] Amount=" + amountStr);
		
		int amount = 0;
		
		boolean billingAccountFound = false;
		String isSuccessful = "FALSE";
		
		String billingAccountQuery = "select ACCOUNT_NUM from custeventsource ces , custproductdetails cpd "+ 
									 "where CES.CUSTOMER_REF = CPD.CUSTOMER_REF " +
									  "and CES.PRODUCT_SEQ = CPD.PRODUCT_SEQ " +
									  "and CES.END_DTM is null " + 
									  "and CPD.END_DAT is null " +
									  "and CES.EVENT_TYPE_ID = 9 " + 
									  "and CES.EVENT_SOURCE NOT LIKE '4100%' " +
									  "and CES.EVENT_SOURCE = '" + msisdn + "'";
		
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
					logger.info("[RefundPostpaid] BillingAccountNumber: " + billingAccountNum + " for MSISDN: " + msisdn);
				}
				
				stmt.close();
				
				if (billingAccountFound) {
					
					String adjustmentSPQuery = "{call GENEVA_ADMIN.CUSTOM_POST_ADJUSTMENT_NOTE_IN(?,?,?,?,?,?,?,?,?,?)}";
					
					callableStatement = conn.prepareCall(adjustmentSPQuery);
					
					callableStatement.setString(1, billingAccountNum);
					callableStatement.setString(2, "20");
					callableStatement.setString(3, "GoogleDCB_Reversal");
					callableStatement.setInt(4, amount);
					callableStatement.setString(5, "F");
					callableStatement.setInt(6, 1);
					callableStatement.setString(7, "INTGSBL_DEV");
					callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
					callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
					callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR); //Filled in case status is OK
					
					callableStatement.executeUpdate();
					String status = callableStatement.getString(8);
					String message = callableStatement.getString(9);
					String outSeq = callableStatement.getString(10);
					
					logger.debug("[RefundPostpaid] Adjustment API status: " + status);
					logger.debug("[RefundPostpaid] Adjustment API message: " + message);
					logger.debug("[RefundPostpaid] Adjustment API outSeq:" + outSeq);
					
					if (!(outSeq == null) && !(outSeq.isEmpty())) {
						logger.debug("[RefundPostpaid] Adjustment API Called successfully!");
						isSuccessful = "TRUE";
					}
				}
				conn.close();
			}
			
		}catch (NumberFormatException ne) {
			ne.printStackTrace();
			logger.debug("[RefundPostpaid] + Error= " + ne.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			logger.debug("[RefundPostpaid] + Error= " + e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug("[RefundPostpaid] + Error= " + e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					log.debug("[RefundPostpaid] Error in closing database connection!");
					e.printStackTrace();
				}
				
			}
		}
		logger.debug("[RefundPostpaid] REFUND_STATUS: " + isSuccessful);
	    msgCtx.setProperty("REFUND_STATUS", isSuccessful);
		return true;
	}

}