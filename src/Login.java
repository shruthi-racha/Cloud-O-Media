import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

//[B@7ab03
public class Login implements ActionListener {

	JFrame jf;
	JPanel jp;
	JLabel jl1,jl2,jl3;
	JTextField jtf;
	JPasswordField jpf;
	JButton jb1,jb2;
	JComboBox jcb;
	static Connection con=new Database().conn;
	Login()
	{
		
		// create table(username varchar(100),password varchar(100),level int not null,uploadsize int not null,downloadsize int not null);
		UIManager.LookAndFeelInfo l[]=UIManager.getInstalledLookAndFeels();
		try {
			UIManager.setLookAndFeel(l[1].getClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




		jf=new JFrame("Login");
		jf.setBounds(150,150,400,300);
		jf.setLayout(null);
		
		jp=new JPanel();
		jp.setBounds(5,5,380,380);
		jp.setBackground(Color.white);
		jp.setLayout(null);
		jf.add(jp);
		
		jl1=new JLabel("USERNAME",JLabel.CENTER);
		jl1.setBounds(30,50,150,50);
		jl1.setBorder(new TitledBorder(""));
		jp.add(jl1);
		
		jtf=new JTextField();
		jtf.setBounds(200,50,150,50);
		jtf.setBorder(new TitledBorder(""));
		jp.add(jtf);
		
		jl2=new JLabel("PASSWORD",JLabel.CENTER);
		jl2.setBounds(30,100,150,50);
		jl2.setBorder(new TitledBorder(""));
		jp.add(jl2);
		
		jpf=new JPasswordField();
		jpf.setBounds(200,100,150,50);
		jpf.setBorder(new TitledBorder(""));
		jp.add(jpf);
		
		jl3=new JLabel("SELECT LEVEL",JLabel.CENTER);
		jl3.setBounds(30,150,150,50);
		jl3.setBorder(new TitledBorder(""));
		jp.add(jl3);
		
		jcb=new JComboBox();
		jcb.setBounds(200,150,150,50);
		jcb.addItem("1");
		jcb.addItem("2");
		jcb.addItem("3");
		jcb.addItem("admin");
		jp.add(jcb);
		
		jb1=new JButton("SIGNIN");
		jb1.setBounds(30,200,150,50);
		jb1.setBackground(Color.black);
		jb1.setForeground(Color.white);
		jp.add(jb1);
		
		jb2=new JButton("SIGNUP");
		jb2.setBounds(200,200,150,50);
		jb2.setBackground(Color.black);
		jb2.setForeground(Color.white);
		jp.add(jb2);
		
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jf.setVisible(true);
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Login();
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
		if(ae.getSource()==jb1)
		{
			try
			{
				String username=jtf.getText();
				String Password=jpf.getText();
				if(username.equals("admin") && Password.equals("admin") && jcb.getSelectedItem().toString().equals("admin"))
				{
					new Interface();
					jf.dispose();
				}
				else if(!username.equals("admin") || !Password.equals("admin") || !jcb.getSelectedItem().toString().equals("admin"))
				{
					
					if(!jcb.getSelectedItem().toString().equals("admin"))
					{
						PreparedStatement ps=con.prepareStatement("select * from cloud where username=? and password=?");
						ps.setString(1,jtf.getText());
						ps.setString(2,jpf.getText());
						ResultSet rs=ps.executeQuery();
						if(rs.next())
						{
							new Node(jtf.getText());
						}
						else
						{
							JOptionPane.showMessageDialog(null, "wrong username and password");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "User can't login as an admin");
					}
					
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Select properly");
				}
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if(ae.getSource()==jb2)
		{
			String username=jtf.getText();
			String Password=jpf.getText();
			
			if(!jcb.getSelectedItem().toString().equals("admin"))
			{
				int type=Integer.parseInt(jcb.getSelectedItem().toString());
				try
				{
					 PreparedStatement ps=con.prepareStatement("select * from cloud where username=?");
					  ps.setString(1,username);
					  ResultSet rs=ps.executeQuery();
					  if(!rs.next())
					  {
					  	ps.clearBatch();
					  	ps=con.prepareStatement("insert into cloud values(?,?,?,?,?,?,?)");
					  	ps.setString(1,username);
					  	ps.setString(2,Password);
					  	ps.setInt(3,type);
					  	if(type==1)
					  	{
					  		ps.setInt(4,5);
					  		ps.setInt(5,5);
					  	}
					  	if(type==2)
					  	{
					  		ps.setInt(4,10);
					  		ps.setInt(5,10);
					  	}
					  	if(type==3)
					  	{
					  		ps.setInt(4,15);
					  		ps.setInt(5,15);
					  	}
					  	ps.setInt(6,0);
					  	ps.setInt(7,0);
					  	int a=ps.executeUpdate();
					  	if(a==1)
					  	{
					  		JOptionPane.showMessageDialog(null,"Registered successfully");
					  	}
					  	else
					  	{
					  		JOptionPane.showMessageDialog(null,"Registered un success");
					  	}
					  	
					  }
					  else
					  {
						  JOptionPane.showMessageDialog(null, "Username already existing ");
					  }
					  
					  
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "User can't login as an admin");
				
			}
				
			
			
		}
		
	}

}
