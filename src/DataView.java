import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;


public class DataView implements ActionListener
{
	JFrame jf;
	JPanel jp;
	static DefaultTableModel dtab;
	JTable jt;
	JButton jb,jb1;
	JScrollPane jsp;
	JLabel jl1;
	//static int rows=(Integer.parseInt(JOptionPane.showInputDialog(null,"Enter no of rows")));
	static HashMap<Integer,String> datamask=new HashMap<Integer,String>();
	static HashMap<Integer,String> datashuffle=new HashMap<Integer,String>();
	static HashMap<Integer,String> datasub=new HashMap<Integer,String>();
	static HashMap<Integer,String> dataencryption=new HashMap<Integer,String>();
	private static Connection con=new Database().conn;
	ArrayList temp=new ArrayList();
	static String method;
     Object row[]={"USERNAME","FILENAME","BUCKET","TIME","U/D","SIZE" };
        public SecretKeySpec skeySpec;	
	DataView(String username)
	{
            
           
		
		
		try
		{
			UIManager.LookAndFeelInfo l[]=UIManager.getInstalledLookAndFeels();
			UIManager.setLookAndFeel(l[1].getClassName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		jf=new JFrame("Log Details");
		jf.setBounds(0,0,700,640);
		jf.setLayout(null);
		
		jp=new JPanel();
		jp.setBounds(5,5,680,620);
		jp.setBackground(Color.white);
		jp.setLayout(null);
		jf.add(jp);
		jp.setBorder(new TitledBorder(""));
		jf.add(jp);
		
		dtab=new DefaultTableModel();		
		jt=new JTable(dtab);
		jt.setFont(new Font(null,Font.BOLD,10));
			

		
		
		for(Object obj:row)
		{
			dtab.addColumn(obj);	
		}
	
	  jsp=new JScrollPane(jt);
	  jsp.setBounds(15,0,650,530);
	  jp.add(jsp);
		
	  
	  jb=new JButton("EXIT");
	  jb.setBounds(15,530,650,30);
	  jb.setBackground(Color.black);
	  jb.setForeground(Color.white);
	  jb.addActionListener(this);
	  jp.add(jb);
	  
	 
	  
		jf.setVisible(true);
		
			
		
		
			try
			{
				PreparedStatement ps=con.prepareStatement("select * from log_details where username=?");
				ps.setString(1, username);
				ResultSet rs=ps.executeQuery();
				String a="";
				while(rs.next())
				{
					a="correct";
					Object row1[]={rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6)};
					dtab.addRow(row1);
				}
				if(a.equals(""))
				{
					JOptionPane.showMessageDialog(null, "No data");
					jf.dispose();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
		
	}
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==jb)
		{
			jf.dispose();
		}
	}
	
}
