package cn.model.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class MTDataBaseTool {
	private String driver 	= "com.mysql.jdbc.Driver";
	private String dbName 	= "mydb";
	private String userName = "root";
	private String password = "root";
//	private String url 		= "jdbc:mysql://localhost:3306/" + dbName;
	private String url 		= "jdbc:mysql://localhost:3306/" + dbName;
	public MTDataBaseTool() {
	
	}
	
	public MTDataBaseTool(String driver, String dbName, String password,
			String userName, String url) {
		super();
		this.driver = driver;
		this.dbName = dbName;
		this.password = password;
		this.userName = userName;
		this.url = url;
	}

	//	检测数据的检查状态
	public Connection doCheckDB(){
		Connection conn=null;
		try {
	       Class.forName(driver);
	       conn= DriverManager.getConnection(url, userName, password);
	       System.out.println("ss");
	    } catch (Exception e) {
	       return null;
	    }
		return conn;
	}
	//	数据库的更新操作
	public int doDBUpdate(String sqlLanguage){
		int 		 	  nCount	=	0;
		Connection		  conn		=	doCheckDB();
		PreparedStatement ptmt		=	null;;
		try {
			System.out.println("conn= "+conn);
			System.out.println("sql= "+sqlLanguage);
			ptmt 	= conn.prepareStatement(sqlLanguage);
			nCount 	= ptmt.executeUpdate();
			if(conn!=null){
				conn.close();
			}
			if(ptmt!=null){
				ptmt.close();
			}
		} catch (SQLException e) {
			return 0;
		}
		return nCount;
	}
	//	查询结果;
	public ArrayList<String[]> query(String sql){
		ArrayList<String[]> list=new ArrayList<>();
		Connection conn		=	doCheckDB();
		Statement  stmt		=	null;
		int 	   column	=	0;
		try {
			stmt 		 = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);//创建数据对象
			column		 = rs.getMetaData().getColumnCount();
			while (rs.next()){
				String[] arrays	=	new String[column];
				for(int i=0;i<column;i++){
					int index=i+1;
					arrays[i]=rs.getString(index);
				}
				list.add(arrays);
			}
			if(rs!=null){
				rs.close();				
			}
			if(stmt!=null){				
				stmt.close();
			}
			if(conn!=null){				
				conn.close();
			}
		} catch (SQLException e) { 
			return null;
		}
		return list;
	}

}
