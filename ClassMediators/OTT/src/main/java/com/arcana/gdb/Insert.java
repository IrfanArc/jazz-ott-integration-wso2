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

public class Insert extends AbstractMediator {
	
Log logger = LogFactory.getLog(ReserveFunds.class);
	
	public boolean mediate(MessageContext msgCtx) {
		
		ResultSet resultstring;
		String causeFromDb;
		String statusFromDb = null;
		String OriginatorConversationID = (String) msgCtx.getProperty("CorrelationID");
		String amount = (String) msgCtx.getProperty("amount");
		String status = "INIT";
		Hashtable environment = new Hashtable();
	    environment.put("java.naming.factory.initial", "org.wso2.carbon.tomcat.jndi.CarbonJavaURLContextFactory");
	    Context initContext;
		try {
			initContext = new InitialContext(environment);
			DataSource ds = (DataSource)initContext.lookup("jdbc/ott");
			if (ds != null) {
				Connection conn = ds.getConnection();
			    String findResponseInCacheSQL = "INSERT INTO `HUB_TRANSACTION_STORE` (`CORRELATION_ID`, `STATUS`, `INTERFACE_NAME`,`AMOUNT`) VALUES ('"+OriginatorConversationID+"', 'PENDING', 'ReserveFunds'"+amount+")";
			    Statement stmt = conn.createStatement();
			    resultstring =stmt.executeQuery(findResponseInCacheSQL);
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
		return true;
	}
}
