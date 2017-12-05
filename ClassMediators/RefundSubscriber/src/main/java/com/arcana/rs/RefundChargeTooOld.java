package com.arcana.rs;

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

public class RefundChargeTooOld extends AbstractMediator {
	
Log logger = LogFactory.getLog(RefundChargeTooOld.class);
	
	public boolean mediate(MessageContext msgCtx) {
		
		ResultSet resultstring;
		ResultSet resultstring1;
		Connection conn = null;
		Connection conn1 = null;
		Statement stmt =null;
		Statement stmt1 =null;
		String causeFromDb = null;
		String statusFromDb = null;
		String descriptionFromDb = null; 
		String OriginatorConversationID = (String) msgCtx.getProperty("CorrelationID");
		String status = "INIT";
		Hashtable environment = new Hashtable();
	    environment.put("java.naming.factory.initial", "org.wso2.carbon.tomcat.jndi.CarbonJavaURLContextFactory");
	    Context initContext;
		try {
			initContext = new InitialContext(environment);
			DataSource ds = (DataSource)initContext.lookup("jdbc/ott");
			if (ds != null) {
				conn = ds.getConnection();
				conn1 = ds.getConnection();

			    String findResponseInCacheSQL = "SELECT * FROM `HUB_TRANSACTION_STORE` WHERE `CORRELATION_ID` ='"+OriginatorConversationID+"' AND INTERFACE_NAME ='ReserveFunds' ORDER BY `TIMEIN` DESC LIMIT 1";
			    stmt = conn.createStatement();

			    resultstring =stmt.executeQuery(findResponseInCacheSQL);
			    resultstring.next();
				statusFromDb = resultstring.getString("STATUS");
				if(statusFromDb.contains("OK")){
					  String findResponseInCacheSQL1 = "SELECT * FROM `HUB_TRANSACTION_STORE` WHERE `CORRELATION_ID` ='"+OriginatorConversationID+"' AND INTERFACE_NAME ='ReserveFunds' AND DATE_SUB(CURDATE(),INTERVAL 3 MONTH) < `TIMEIN` ORDER BY `TIMEIN` DESC LIMIT 1";
					    stmt1 = conn1.createStatement();
					    try{
					    resultstring1 =stmt1.executeQuery(findResponseInCacheSQL1);
					    resultstring1.next();
						statusFromDb = resultstring1.getString("STATUS");
						if(statusFromDb.contains("OK")){
					    msgCtx.setProperty("result", "OK");
						}
					    }
					    catch(SQLException e){
					    	msgCtx.setProperty("result", "CTO");
					    }
				}else{
					msgCtx.setProperty("result", "NR");
				}
				stmt.close();
				conn.close();
				}
			
		}	
		catch (NamingException e) {
			e.printStackTrace();
			status="NAMING EXCEPTION";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				try {
					stmt.close();
					conn.close();
				} catch (Exception e) {
					log.debug("[RefundPostpaid] Error in closing database connection!");
					e.printStackTrace();
				}
				
			}
			if (conn1 != null) {
				try {
					stmt1.close();
					conn1.close();
				} catch (Exception e) {
					log.debug("[RefundPostpaid] Error in closing database connection!");
					e.printStackTrace();
				}
				
			}
		}
		return true;
	}

}