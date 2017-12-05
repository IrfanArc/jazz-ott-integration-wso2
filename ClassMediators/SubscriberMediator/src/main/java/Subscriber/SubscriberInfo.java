package Subscriber;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext; 
import org.apache.synapse.mediators.AbstractMediator;

public class SubscriberInfo extends AbstractMediator { 
//	private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
//	private static final String DB_CONNECTION = "jdbc:oracle:thin:***@10.50.5.41:1521/PMCLINT";
//	private static final String DB_USER = "";
//	private static final String DB_PASSWORD = "";

	Log log=LogFactory.getLog(SubscriberInfo.class);
	private static void callOracleStoredProcOUTParameter(MessageContext context) throws SQLException {
		
		Hashtable environment = new Hashtable();
	    environment.put("java.naming.factory.initial", "org.wso2.carbon.tomcat.jndi.CarbonJavaURLContextFactory");
	    Context initContext;

	    Connection dbConnection = null;
		PreparedStatement pst=null;
		ResultSet RS=null;
		
		String mssidn=(String)context.getProperty("COMPTEL_IMSI");
		
		long TOTAL_BILL_l=0;
		Date Due_DT_l=null;
		long unpaid_l=0;
		long current_month_l=0;
		
		LogFactory.getLog(SubscriberInfo.class).info("Subscriber info classs executing query");
		String query = "SELECT  TOTAL_BILL, CURRENT_MONTH,UNPAID,  DUE_DT FROM GENEVA_ADMIN.V_BILLINGINFO WHERE MSISDN ='"+mssidn+"'";
		System.out.println("Mssind in Java Class Query:"+query);
		try {
			initContext = new InitialContext(environment);
			DataSource ds = (DataSource)initContext.lookup("jdbc/geneva");
			if (ds != null) {
				dbConnection = ds.getConnection();
				pst=dbConnection.prepareStatement(query);
				RS=pst.executeQuery();
				while(RS.next()){
					TOTAL_BILL_l=RS.getLong("TOTAL_BILL");
					Due_DT_l=RS.getDate("DUE_DT");
					unpaid_l=RS.getLong("unpaid");
					current_month_l=RS.getInt("current_month");
				System.out.println("1:"+TOTAL_BILL_l+"+ 2:+"+Due_DT_l);	
				}
				context.setProperty("TOTAL_BILL", TOTAL_BILL_l);
				context.setProperty("Due_DT", Due_DT_l);
				context.setProperty("unpaid", unpaid_l);
				context.setProperty("current_month", current_month_l);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
//	private static Connection getDBConnection() {
//
//		Connection dbConnection = null;
//
//		try {
//
//			Class.forName(DB_DRIVER);
//
//		} catch (ClassNotFoundException e) {
//
//			System.out.println(e.getMessage());
//
//		}
//
//		try {
//
//			dbConnection = DriverManager.getConnection(
//				DB_CONNECTION, DB_USER,DB_PASSWORD);
//			return dbConnection;
//
//		} catch (SQLException e) {
//
//			System.out.println(e.getMessage());
//
//		}
//
//		return dbConnection;
//
//	}

	@Override
	public boolean mediate(MessageContext context) {
		log.info("Subscriber info classs");
		// TODO Auto-generated method stub
		try {
			callOracleStoredProcOUTParameter(context);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
