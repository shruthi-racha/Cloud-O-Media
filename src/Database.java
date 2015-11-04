/**
 * @(#)Database.java
 *
 *
 * @author
 * @version 1.00 2011/4/17
 */

import java.io.FileInputStream;
import java.sql.*;

import javax.swing.JOptionPane;

final public class Database {

	public static Connection conn;

	
    public  Database() 
    {
    	try
    	{
    	
    		FileInputStream fip=new FileInputStream("C:\\1.txt");
    		byte[] buffer=new byte[fip.available()];
    		fip.read(buffer);
    		String ip=new String(buffer);
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    	
    		//conn=DriverManager.getConnection("jdbc:oracle:thin:@"+ip.trim()+":1521:XE","system","system");
    		
    		conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","system");
    		System.out.println("Connected to Database...");
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

}


/*
 * 
 * 
create table employeeDetails(username varchar(100),password varchar(100),eid varchar(100),loginTime varchar(100),logout varchar(100),blackMark varchar(100),date1 varchar(100),status varchar(100),ip varchar(100),port int not null);
 */