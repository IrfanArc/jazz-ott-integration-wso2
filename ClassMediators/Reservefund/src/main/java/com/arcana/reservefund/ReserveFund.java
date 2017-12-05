package com.arcana.reservefund;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext; 
import org.apache.synapse.mediators.AbstractMediator;

public class ReserveFund extends AbstractMediator { 
 private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
 private static final String DB_CONNECTION = "jdbc:oracle:thin:geneva@10.50.5.41:1521/PMCLINT";
 private static final String DB_USER = "GENEVA_ADMIN";
 private static final String DB_PASSWORD = "geneva";

 Log log=LogFactory.getLog(ReserveFund.class);
 private void callOracleStoredProcOUTParameter(MessageContext context) throws SQLException {
  

  Connection dbConnection = null;
  CallableStatement callableStatement = null;
  
  
  Connection dbConnection2 = null;
  PreparedStatement pst=null;
  ResultSet RS=null;
  String account_num =null;
  String mssidn=(String)context.getProperty("NORMALIZED_MSISDN_FOR_IN");
  String amountStr=(String)context.getProperty("pkr");
  Integer amount = Integer.parseInt(amountStr);
  String desc=(String)context.getProperty("Description");
  mssidn = "0"+mssidn;


  String query2 = "select ACCOUNT_NUM from custeventsource ces , custproductdetails cpd where CES.CUSTOMER_REF = CPD.CUSTOMER_REF and CES.PRODUCT_SEQ = CPD.PRODUCT_SEQ and CES.END_DTM is null and CPD.END_DAT is null and CES.EVENT_TYPE_ID = 9 and CES.EVENT_SOURCE NOT LIKE '4100%' and CES.EVENT_SOURCE = '"+mssidn+"'";
  try {
   dbConnection2 = getDBConnection();
   
   pst=dbConnection2.prepareStatement(query2);
   RS=pst.executeQuery();
   while(RS.next()){
	   account_num=RS.getString("ACCOUNT_NUM");
   }
  }
  catch (SQLTimeoutException e){
	  context.setProperty("status", "timeout");
	  return;
  }
  catch (SQLException e) {
	   e.printStackTrace();
	   System.out.println(e.getMessage());
	   return;

	  } finally {

	   if (pst != null) {
	    pst.close();
	    dbConnection2.close();
	    
	   }
	  }


  String query = "{call GENEVA_ADMIN.IPGPMCLCONTENTCHARGING.MANAGEOTCCREATION(?,?,?,?,?,?,?,?,?,?,?)}";
  System.out.println("Msisdn in Java Class Query:"+query);
  try {
	  log.debug("[ReserveFunds] Calling the MANAGING OTC CREATIONS SP");
   dbConnection = getDBConnection();
   callableStatement = dbConnection.prepareCall(query);
   
   callableStatement.setString(1, "Google");
	callableStatement.setString(2, "GooglePlayStore");
	callableStatement.setString(3, "Online Purchases");
	callableStatement.setString(4, "123");
	callableStatement.setString(5, desc);
	callableStatement.setString(6, "PKR");
	callableStatement.setString(7, mssidn);
	callableStatement.setInt(8, 47);
	callableStatement.setInt(9, amount);
	callableStatement.setString(10, account_num);
	callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);


	// execute getDBUSERByUserId store procedure
	callableStatement.executeUpdate();

	String result = callableStatement.getString(11);
	log.debug("[ReserveFunds] SP Execution finished, we got: " + result);
	if (result.contains("Transaction Success")){
		context.setProperty("status", "OK");
	}
	else{
		context.setProperty("status", "KO");
		context.setProperty("reason", result);
	}
	
  }   catch (SQLTimeoutException e){
	  context.setProperty("status", "timeout");
  }
  catch (SQLException e) {
	   e.printStackTrace();
	   System.out.println(e.getMessage());

	  } finally {

	   if (callableStatement != null) {
		   callableStatement.close();
	   }
	  }


   if (dbConnection != null) {
    dbConnection.close();
   }

  }
 
 private static Connection getDBConnection() {

  Connection dbConnection = null;

  try {

   Class.forName(DB_DRIVER);

  } catch (ClassNotFoundException e) {

   System.out.println(e.getMessage());

  }

  try {

   dbConnection = DriverManager.getConnection(
    DB_CONNECTION, DB_USER,DB_PASSWORD);
   return dbConnection;

  } catch (SQLException e) {

   System.out.println(e.getMessage());

  }

  return dbConnection;

 }

 @Override
 public boolean mediate(MessageContext context) {
  log.info("Subscriber info classs");
  // TODO Auto-generated method stub
  try {
   callOracleStoredProcOUTParameter(context);
  } catch (SQLException e) {
   // TODO Auto-generated catch block
	  log.debug(e.getMessage());
   e.printStackTrace();
  }
  return true;
 }

}