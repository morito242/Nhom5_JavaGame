package menuBarGame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import connection.JDBCConnection;
import jdk.vm.ci.code.site.Mark;

public class score 
{
	int mark;
	public score()
	{
		
	}
	public int getPoint(String username)
	{
		
		PreparedStatement pst = null;
		Connection conn = null;
		try
		{
			conn = JDBCConnection.getConnection();
			
			String sql = "select * from userhistory where name= ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, username);
        
			ResultSet rs = pst.executeQuery();
			
			while(rs.next())
			{
				mark=rs.getInt(3);
			}
			
		}
		catch (Exception e1) {}
		return mark;
		
	}
	public void savePoint(String username, int mark)
	{
		PreparedStatement pst = null;
		Connection conn = null;
		try
		{
			conn = JDBCConnection.getConnection();
			
			String sql = "update userhistory set score = ? where name = ?";
			//insert into login (user, pass) values (?,?)
			//pst.setString(1, username)
			//pst.setString(2, password)+
			pst = conn.prepareStatement(sql);
			pst.setInt(1, mark);
			pst.setString(2, username);
			
			int row = pst.executeUpdate();
		}
		catch (Exception e1) {}
		
	}
		
}
