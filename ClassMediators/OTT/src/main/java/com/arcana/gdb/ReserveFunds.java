package com.arcana.gdb;

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


public class ReserveFunds extends AbstractMediator {
	
Log logger = LogFactory.getLog(ReserveFunds.class);
	
	public boolean mediate(MessageContext msgCtx) {
		
		ResultSet resultstring;
		Connection conn = null;
		String causeFromDb = null;
		String statusFromDb = null;
		Statement stmt =null;
		String descriptionFromDb = null; 
		String OriginatorConversationID = (String) msgCtx.getProperty("CorrelationID");
		String Interface = (String) msgCtx.getProperty("Interface");
		String status = "INIT";
		Hashtable environment = new Hashtable();
	    environment.put("java.naming.factory.initial", "org.wso2.carbon.tomcat.jndi.CarbonJavaURLContextFactory");
	    Context initContext;
		try {
			initContext = new InitialContext(environment);
			DataSource ds = (DataSource)initContext.lookup("jdbc/ott");
			if (ds != null) {
				conn = ds.getConnection();
			    String findResponseInCacheSQL = "SELECT * FROM `HUB_TRANSACTION_STORE` WHERE `CORRELATION_ID` ='"+OriginatorConversationID+"' AND INTERFACE_NAME ='ReserveFunds' AND `CAUSE` <> 'SYSTEM_DOWN' AND `CAUSE` <> 'SYSTEM_BUSY' AND `CAUSE` <> 'INDEFINITE_RESULT' ORDER BY `TIMEOUT` DESC LIMIT 1";
			    stmt = conn.createStatement();
			    resultstring =stmt.executeQuery(findResponseInCacheSQL);
			    resultstring.next();
				statusFromDb = resultstring.getString("STATUS");
				causeFromDb = resultstring.getString("CAUSE");	
				descriptionFromDb = resultstring.getString("DESCRIPTION");
				stmt.close();
				conn.close();
				msgCtx.setProperty("status", statusFromDb);
				msgCtx.setProperty("CAUSE", causeFromDb);
				msgCtx.setProperty("DESCRIPTION", descriptionFromDb);
				}
			
		}	
		catch (NamingException e) {
			e.printStackTrace();
			status="NAMING EXCEPTION";
		} catch (SQLException e) {
			e.printStackTrace();
			if(e.getLocalizedMessage().contains("Illegal operation on empty result set")){
			status="SQL EXCEPTION";
			msgCtx.setProperty("status", "NotFound");
			}
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
		}
		return true;
	}

}
