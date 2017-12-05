package Subscriber;

//import java.sql.CallableStatement; Subscriber.CallingProcedure
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import org.apache.synapse.MessageContext;

import org.apache.synapse.mediators.AbstractMediator;

//import com.sun.xml.internal.bind.CycleRecoverable.Context;

public class CallingProcedure  {

	private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DB_CONNECTION = "jdbc:oracle:thin:wajeeh/Mobilink1@10.50.167.63:1521/SBLDB";
	private static final String DB_USER = "wajeeh";
	private static final String DB_PASSWORD = "Mobilink1";
	private static void callOracleStoredProcOUTParameter() throws SQLException {

		Connection dbConnection = null;
//		PreparedStatement pst=null;
		CallableStatement pst=null;
		ResultSet RS=null;
		String mssidn="03092164443";
		double TOTAL_BILL_l=0;
		Date Due_DT_l=null;
		double unpaid_l=0;
		int current_month_l=0;
		
		
//		CallableStatement callableStatement = null;

		String query = "{call siebel.NIC_5NCHK(?,?)}";
		System.out.println("Mssind in Java Class Query:"+query);
		try {
			dbConnection = getDBConnection();
			System.out.println("connected:"+query);
			pst=dbConnection.prepareCall(query);
			pst.setString(1, "3520145155235");
			pst.registerOutParameter(2, java.sql.Types.VARCHAR);
			

//			 execute getDBUSERByUserId store procedure
			pst.executeUpdate();
			String Result = pst.getString(2);
			

			System.out.println("Result : " + Result);
			
//
//			String Result = callableStatement.getString(11);
//			RS=pst.executeQuery();
//			System.out.println("RS"+RS.);
//			while(RS.next()){
//				TOTAL_BILL_l=RS.getDouble(0);
//			System.out.println("val:"+RS.getString("CNT"));
//				Due_DT_l=RS.getDate("DUE_DT");
//				unpaid_l=RS.getDouble("unpaid");
//				current_month_l=RS.getInt("current_month");
//				System.out.println("1"+TOTAL_BILL_l);
//				System.out.println("2"+Due_DT_l);
//				System.out.println("3"+unpaid_l);
//				System.out.println("4"+current_month_l);
//			}
//			
//			context.setProperty("TOTAL_BILL", TOTAL_BILL_l);
//			context.setProperty("Due_DT", Due_DT_l);
//			context.setProperty("unpaid", unpaid_l);
//			context.setProperty("current_month", current_month_l);
//			
			//			callableStatement = dbConnection.prepareCall(getDBUSERByUserIdSql);
//
//			callableStatement.setString(1, "Google");
//			callableStatement.setString(2, "Play");
//			callableStatement.setString(3, "Online Purchases");
//			callableStatement.setString(4, "123");
//			callableStatement.setString(5, "App Purchase");
//			callableStatement.setString(6, "PKR");
//			callableStatement.setString(7, "03008412618");
//			callableStatement.setInt(8, 47);
//			callableStatement.setInt(9, 5000);
//			callableStatement.setString(10, "1-35408530312");
//			callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);
		

			// execute getDBUSERByUserId store procedure
//			callableStatement.executeUpdate();
//
//			String Result = callableStatement.getString(11);
			

//			System.out.println("Result : " + Result);
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());

		} finally {

			if (pst != null) {
				pst.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

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


	public static void main(String[] argv) {

		try {

			callOracleStoredProcOUTParameter();

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

	}

	
}