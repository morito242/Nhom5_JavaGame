package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCConnection 
{
	public static Connection getConnection()
	{
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/questiongame","tannbvip","anhtannbvip20");
			return con;
		} catch (Exception e) {}
		return null;
	}
}
