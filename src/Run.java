

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Run {

	
	static private Connection con=new Database().conn;
	public static void main(String[] args) {
	

		try
		{   
		
			UIManager.LookAndFeelInfo l[]=UIManager.getInstalledLookAndFeels();
			UIManager.setLookAndFeel(l[1].getClassName());

		}
		catch(Exception e)
		{
			System.out.println("Error with main method in Node1::\n"+e);
		}
		String option=JOptionPane.showInputDialog(null,"ENTER CHOICE:\n1:MONITOR LOGIN\n2:USER LOGIN").trim();
		if(option.equals("1"))
		{
			
			
			String eid=JOptionPane.showInputDialog(null,"ENTER MONITOR EID");
			if(eid.equals("ADMIN"))
			{
				new Interface();
				
			}
			else
			{
				JOptionPane.showMessageDialog(null, "ERROR AUTHENTICATION");
			}
		}
		else if(option.equals("2"))
		{
			new Login();
			
		}
		else
		{
			System.exit(0);
		}
		

	}

}
