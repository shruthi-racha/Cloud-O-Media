

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;




class Node extends Thread implements ActionListener
{

	static String int_ip="localhost";
	HashMap<String, List<String>> file_list;
	Queue<String> queue = new LinkedList<String>();
	static Registry reg;
	static RmiInterface ri;
	static int port_no=0;
	Thread rec1;
	Thread rec11;
	JFrame jf;
	JPanel jl;

	JLabel jl1;

	JButton jb,jb6,jb1,jb2,jb3,jb5,jb7,jb8,jb9,iptv;
	DefaultListModel dlm1;
	JList jlist1;
	JScrollPane jsp1,jsp3,jsp2;
	JComboBox jcb,jcb1,jcb2;

	DefaultTableModel dtab;
	JMenuItem mi;
	JTable jtab;
	JMenuBar jm;
	JMenu menu;
	static JTextArea jta;
	JCheckBox jc1,jc2;
	static String nodename;
	public static Connection con=new Database().conn;

	ButtonGroup jg;

	TreeMap<String,Integer> route;

	public static Map<String,Integer> node1=new HashMap<String,Integer>();
	public static Map<String,Integer> neigh=new HashMap<String,Integer>(); 
	HashMap<Integer,String> dataforward=new HashMap<Integer,String>();
	HashMap<Integer,String> tempData=new HashMap<Integer,String>();
	HashMap<Integer,String> tempDataMain=new HashMap<Integer,String>();
	static int port=0;
	String flist="";
	static String node;
	Stack dataSend=new Stack();
	ArrayList nodes=new ArrayList();

	static int requestcount=0;

	Node(String nodename1)
	{




		try
		{   
			nodename=nodename1;
			System.out.println(nodename);
			UIManager.LookAndFeelInfo l[]=UIManager.getInstalledLookAndFeels();
			UIManager.setLookAndFeel(l[1].getClassName());

		}
		catch(Exception e)
		{
			System.out.println("Error with main method in Node1::\n"+e);
		}
		try
		{
			reg=LocateRegistry.getRegistry(int_ip,5000);
			ri=(RmiInterface)reg.lookup("interface");
			String ip=InetAddress.getLocalHost().getHostName();
			String ack=ri.Reg_usr(nodename,ip);
			String[] a1=ack.split("&");
			port_no=Integer.parseInt(a1[1]);

			try
			{
				new RMI(port_no,nodename);

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Interface inactive");
			System.exit(0);
		}
		jf=new JFrame("ROUTER "+nodename);
		jf.setBounds(100,100,580,490);
		jf.setLayout(null);


		jm=new JMenuBar();
		jm.setBounds(0,0,580,20);
		jm.setForeground(Color.black);
		jm.setBackground(new Color(0,191,255));
		jf.add(jm);



		jl=new JPanel();
		jl.setBounds(5,20,580,450);
		jl.setLayout(null);
		jl.setBackground(Color.white);
		jf.add(jl);

		jl1=new JLabel(new ImageIcon("images/User3.png"));
		jl1.setBounds(0,0,100,100);
		jl1.setBorder(new TitledBorder(""));
		jl.add(jl1);


		jb6=new JButton("U P L O A D");
		jb6.setBounds(5,240,235,45);
		jb6.setBackground(Color.black);
		jb6.setForeground(Color.yellow);
		jb6.setFont(new Font("",Font.BOLD,15));
		jb6.addActionListener(this);
		jl.add(jb6);



		jta=new JTextArea();
		jsp2=new JScrollPane(jta);
		jsp2.setBounds(240,0,330,180);
		jta.setBackground(Color.black);
		jta.setForeground(Color.white);
		jsp2.setBorder(new TitledBorder("INFORMATION"));
		jl.add(jsp2);




		jcb=new JComboBox();
		jcb.setBounds(5,100,235,60);
		jcb.setBorder(new TitledBorder("OBJECTS IN CLOUD"));
		//jcb.setBackground(new Color(0,191,255));
		jcb.addActionListener(this);
		jl.add(jcb);


		jcb1=new JComboBox();
		jcb1.setBounds(5,170,235,60);
		jcb1.setBorder(new TitledBorder("FILE LIST"));
		//jcb.setBackground(new Color(0,191,255));
		jcb1.addActionListener(this);
		jl.add(jcb1);

		jcb2=new JComboBox();
		jcb2.setBounds(240,200,235,60);
		jcb2.setBorder(new TitledBorder("TIME"));
		//jcb.setBackground(new Color(0,191,255));
		jcb2.addItem("10");
		jcb2.addItem("30");
		jcb2.addItem("40");
		jcb2.addActionListener(this);
		jl.add(jcb2);


		jb8=new JButton("Log Details");
		jb8.setBounds(240,300,235,40);
		jb8.setBackground(Color.black);
		jb8.setForeground(new Color(0,191,255));
		jl.add(jb8);

		jb8.addActionListener(this);


		
		
		jc1=new JCheckBox("LOW QUALITY VIDEO UPLOAD");
		jc1.setBounds(240,270,200,30);
		jc1.setBorder(new TitledBorder(""));
		jl.add(jc1);

		jb2=new JButton("Req Blist");
		jb2.setBounds(10,340,100,30);
		jb2.setBackground(Color.black);
		jb2.setForeground(new Color(0,191,255));
		jl.add(jb2);

		jb2.addActionListener(this);

		jb5=new JButton("D O W N L O A D");
		jb5.setBounds(5,290,235,45);
		jb6.setBackground(Color.black);
		jb6.setForeground(Color.yellow);
		jb5.setFont(new Font("",Font.BOLD,15));
		jb5.setForeground(new Color(0,191,255));
		jl.add(jb5);
		jb5.addActionListener(this);



		jb3=new JButton("Req Flist");
		jb3.setBounds(130,340,100,30);
		jb3.setBackground(Color.black);
		jb3.setForeground(new Color(0,191,255));
		jl.add(jb3);
		jb3.setEnabled(false);
		jb3.addActionListener(this);

		jb=new JButton("EXIT");
		jb.setBounds(10,380,100,30);
		jb.setBackground(Color.black);
		jb.setForeground(new Color(0,191,255));
		jl.add(jb);
		jb.addActionListener(this);

		jb1=new JButton("CLEAR");
		jb1.setBounds(130,380,100,30);
		jb1.setBackground(Color.black);
		jb1.setForeground(new Color(0,191,255));
		jl.add(jb1);
		jb1.addActionListener(this);
		
		jb9=new JButton("UN subscribe");
		jb9.setBounds(250,380,100,30);
		jb9.setBackground(Color.black);
		jb9.setForeground(new Color(0,191,255));
		jl.add(jb9);
		jb9.addActionListener(this);
		
		
		iptv=new JButton("IPTV");
		iptv.setBounds(250,420,100,30);
		iptv.setBackground(Color.black);
		iptv.setForeground(new Color(0,191,255));
		jl.add(iptv);
		iptv.addActionListener(this);
		
		
		jf.setDefaultCloseOperation(0);

		
		
		jf.setVisible(true);

		//jb2.setEnabled(false);

		new Receiver();
	}

	public static void main(String[] args) 
	{

		new Node("aaa");
	}

	public File OpenFile()
	{
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(null);
		File file=null;
		if(returnVal == JFileChooser.APPROVE_OPTION) 
		{
			file = chooser.getSelectedFile();
			String fname = file.getName();

		}	
		return file;
	}

	public byte[] getfileRead(File file)
	{
		byte[] fileread=null;
		try
		{
			FileInputStream fis=new FileInputStream(file.getAbsolutePath());
			fileread = new byte[fis.available()];
			fis.read(fileread);
			jta.setText(fileread+"");
			fis.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return fileread;
	}





	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==jb)
		{
			try
			{
				ri.usrt_exit(nodename);
				JOptionPane.showMessageDialog(null, "Press ok to Terminate ");
				System.exit(0);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		if(ae.getSource()==jb9)
		{
			try
			{
				PreparedStatement ps=con.prepareStatement("delete from cloud where username=?");
				ps.setString(1,nodename);
				ps.executeUpdate();
				
				PreparedStatement ps1=con.prepareStatement("delete from log_details where username=?");
				ps1.setString(1,nodename);
				ps1.executeUpdate();


				JOptionPane.showMessageDialog(null,nodename+" IS TERMINATING");
				System.exit(0);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if(ae.getSource()==jb2)
		{
			try
			{
				jcb.removeAllItems();
				file_list=ri.req_flist(nodename);
				List<String> l1=new Vector<String>(file_list.keySet());
				for(String trv: l1)
				{
					jcb.addItem(trv);
				}
				jb3.setEnabled(true);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if(ae.getSource()==jb3)
		{
			jcb1.removeAllItems();
			List<String> l1=file_list.get(jcb.getSelectedItem());
			for(String trv : l1)
			{
				jcb1.addItem(trv);
			}
		}
		if(ae.getSource()==jb1)
		{
			jta.setText("");
		}
		if(ae.getSource()==jb5)
		{
			
			
			
			{
				try
				{
					int type=0;
					int size;
					float length;
					PreparedStatement ps=con.prepareStatement("select downloadsize,downloaded,type from cloud where username=?");
					ps.setString(1,nodename);
					ResultSet rs=ps.executeQuery();
					if(rs.next())
					{
						size=rs.getInt(1);
						float dwn=rs.getFloat(2);
						type=rs.getInt(3);

						String f1[]=jcb1.getSelectedItem().toString().split(": :");
						length=Float.parseFloat(f1[1])/(1024*1024);
						if(type==1)
						{
							if((size-dwn)>=length)
							{
								try
								{
									String s="";
									try {

										System.out.println(f1[0]);
										s=ri.download(jcb.getSelectedItem().toString(), f1[0], nodename,jcb2.getSelectedItem().toString());
									} catch (RemoteException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null,"sorry you dont have permission to download extra size");
							}
						}
						if(type==2)
						{
							if((size-dwn)>=length)
							{
								try
								{
									String s="";
									try {


										s=ri.download(jcb.getSelectedItem().toString(), f1[0], nodename,jcb2.getSelectedItem().toString());
									} catch (RemoteException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null,"sorry you dont have permission to download extra size");
							}
						}
						if(type==3)
						{
							if((size-dwn)>=length)
							{
								try
								{
									String s="";
									try {


										s=ri.download(jcb.getSelectedItem().toString(), f1[0], nodename,jcb2.getSelectedItem().toString());
									} catch (RemoteException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null,"sorry you dont have permission to download extra size");
							}
						}
						ps.clearBatch();
						ps=con.prepareStatement("insert into log_details values(?,?,?,?,?,?)");
						ps.setString(1,nodename);
						ps.setString(2,jcb1.getSelectedItem().toString());
						ps.setString(3,jcb.getSelectedItem().toString());
						ps.setString(4,new java.util.Date()+"");
						ps.setString(5,"D");
						ps.setFloat(6,length);
						ps.executeUpdate();
						ps.clearBatch();
						ps=con.prepareStatement("select downloaded from cloud where username=?");
						ps.setString(1,nodename);
						rs=ps.executeQuery();
						if(rs.next())
						{
							PreparedStatement ps1=con.prepareStatement("update cloud set downloaded=? where username=?");
							ps1.setFloat(1,rs.getFloat(1)+length);
							ps1.setString(2,nodename);
							ps1.executeUpdate();
						}


					}

				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
		}
		if(ae.getSource()==jcb)
		{
			//jb2.setEnabled(true);
		}
		if(ae.getSource()==jb8)
		{
			new DataView(nodename);
		}

		if(ae.getSource()==jb6)
		{

			
			float upl = 0;
			
			{
				if(!jc1.isSelected())
				{
					JOptionPane.showMessageDialog(null,"if loop");
					File f=OpenFile();
					byte[] fdata= getfileRead(f);
					String fname=f.getName();
					fname.toLowerCase();

					try
					{
						int type=0;
						int size;
						PreparedStatement ps=con.prepareStatement("select uploadsize,uploaded,type from cloud where username=?");
						ps.setString(1,nodename);
						ResultSet rs=ps.executeQuery();
						if(rs.next())
						{
							size=rs.getInt(1);
							float up=rs.getFloat(2);
							type=rs.getInt(3);
							upl=(fdata.length/(1024*1024));
							JOptionPane.showMessageDialog(null,upl);
							JOptionPane.showMessageDialog(null,"fadat length"+ fdata.length+ " UPLOADIN SIZE " + upl);
							if(type==1)
							{
								//JOptionPane.showMessageDialog(null,(size-up)+"--"+fdata.length);
								if((size-up)>=upl)
								{
									try
									{
										String ack=ri.upload(fname, nodename, fdata);
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
								}
								else
								{
									JOptionPane.showMessageDialog(null,"sorry you dont have permission to upload extra size");
								}
							}
							if(type==2)
							{
								if((size-up)>=upl)
								{
									try
									{
										String ack=ri.upload(fname, nodename, fdata);
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
								}
								else
								{
									JOptionPane.showMessageDialog(null,"sorry you dont have permission to upload extra size");
								}
							}
							if(type==3)
							{
								if((size-up)>=upl)
								{
									try
									{
										String ack=ri.upload(fname, nodename, fdata);
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
								}
								else
								{
									JOptionPane.showMessageDialog(null,"sorry you dont have permission to upload extra size");
								}
							}

						}

					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null,"else loop");
					File f=OpenFile();
					byte[] fdata= getfileRead(f);
					String fname=f.getName();
					fname.toLowerCase();
					try {
						String ack=ri.upload(fname, nodename, fdata,"filesrvlowquality");
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			
		}
		if(ae.getSource()==jcb)
		{
			//jb2.setEnabled(true);
		}
		if(ae.getSource()==iptv)
		{
			String option=JOptionPane.showInputDialog(null,"Enter your option:\n1:PLAYER\n2:CAPTURE");
			 if(option.equals("1"))
			{
				try
				{
					java.lang.Process process1 = Runtime.getRuntime().exec("C:\\\\ip tv\\live player\\bin\\Debug\\zMoviePlayer.exe");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else if(option.equals("2"))
			{
				try
				{
					java.lang.Process process1 = Runtime.getRuntime().exec("C:\\ip tv\\Capture video\\CaptureTest\\bin\\Debug\\CaptureTest.exe");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}


@SuppressWarnings("rawtypes")

class Receiver implements Runnable
{
	Receiver()
	{
		Thread th=new Thread(this);
		th.start();
	}

	public void writeFile()
	{
		try
		{
			JFileChooser jf=new JFileChooser();
			int m=jf.showSaveDialog(null);
			if(m==JFileChooser.APPROVE_OPTION) 
			{
				File f1=jf.getSelectedFile();
				String path=f1.getAbsolutePath();
				FileOutputStream fop=new FileOutputStream(path,true);

				String tempdata="";

				byte b1[]=tempdata.getBytes();
				fop.write(b1);
				fop.close();
			}
		}
		catch(Exception e)
		{

		}
	}
	public void run()
	{
		try
		{


			while(true)
			{


			}


		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
