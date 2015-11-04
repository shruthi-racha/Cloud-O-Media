import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;



import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.StringUtils;


public class Interface extends Thread implements ActionListener
{
	JFrame jf;
	JLabel jl,jl1,jl2,jl3;
	JPanel jp;
	JButton jb1,jb2,jb3,jb8,RELOAD,RELOADfl;
	static JComboBox jcb1,jcb2,jcb3;
	JTextArea jta;
	JScrollPane jsp2,jsp3;
	JTextField jtf1,jtf2,jtf3;
	static DefaultListModel dlm1;
	JList jlist1;
	static HashMap<String, List<String>> file_list=new HashMap<String, List<String>> ();

	static HashMap<String, String > usr_prt=new HashMap<String, String> ();
	static HashMap<String, Integer > uplod_bkt=new HashMap<String, Integer> ();

	static HashMap<String, Integer > dwn_bkt=new HashMap<String, Integer> ();

	static HashMap<String,HashMap<String, Integer>> file_count=new HashMap<String,HashMap<String, Integer>>();
	static HashMap<String,HashMap<String, List<String>>> usr_count=new HashMap<String,HashMap<String, List<String>>>();

	static String bkt_name="supercali";
	static String bk_n="rvvideo";
	static int seq=0;
	static int port_no=5000; 
	static int counter=0;
	Interface()
	{
		try
		{  

			UIManager.LookAndFeelInfo l[]=UIManager.getInstalledLookAndFeels();
			UIManager.setLookAndFeel(l[1].getClassName());

		}
		catch(Exception e)
		{
			System.out.println("Error with main method in Node1::\n"+e);
		}
		try
		{
			new RMI(port_no,"interface");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		jf=new JFrame();
		jf.setBounds(100,100,500,470);
		jf.setLayout(null);

		jp=new JPanel();
		jp.setBackground(Color.white);
		jp.setBorder(new TitledBorder(""));
		jp.setBounds(5,5,480,450);
		jp.setLayout(null);
		jf.add(jp);


		jcb1=new JComboBox();
		jcb1.setBounds(5,5,200,60);
		jcb1.setBorder(new TitledBorder("OBJECTS"));
		//jcb1.addActionListener(this);
		jp.add(jcb1);



		jcb2=new JComboBox();
		jcb2.setBounds(5,70,200,60);
		jcb2.setBorder(new TitledBorder("FILES"));
		jp.add(jcb2);

		RELOADfl=new JButton("RELOAD FILE LIST");
		RELOADfl.setBorder(new TitledBorder(""));
		RELOADfl.setBounds(5,140,200,40);
		RELOADfl.addActionListener(this);
		RELOADfl.setBackground(Color.black);
		RELOADfl.setForeground(Color.yellow);
		RELOADfl.addActionListener(this);
		jp.add(RELOADfl);


		RELOAD=new JButton("RELOAD");
		RELOAD.setBorder(new TitledBorder(""));
		RELOAD.setBounds(5,180,200,30);
		RELOAD.addActionListener(this);
		RELOAD.setBackground(Color.black);
		RELOAD.setForeground(Color.yellow);
		jp.add(RELOAD);

		
		dlm1=new DefaultListModel();	
		jlist1=new JList(dlm1);
		jlist1.setFont(new Font(null,Font.BOLD+Font.ITALIC,12));
		jlist1.setBackground(Color.white);
		jsp3=new JScrollPane(jlist1);
		jsp3.setBorder(new TitledBorder("---ACTIVE CLIENTS---"));
		jsp3.setFont(new Font("Brush Script MT",Font.BOLD,20));
		jsp3.setForeground(Color.orange);
		jsp3.setBackground(Color.blue);
		jsp3.setBounds(210,5,270,170);
		jp.add(jsp3);

		jta=new JTextArea();
		jsp2=new JScrollPane(jta);
		jsp2.setBounds(210,170,270,200);
		jta.setBackground(Color.black);
		jta.setForeground(Color.white);
		jsp2.setBorder(new TitledBorder("Metadata"));
	//	jp.add(jsp2);

		jb1=new JButton("Delete User");
		jb1.setBounds(5,210,200,45);
		jb1.setBackground(Color.black);
		jb1.setForeground(Color.yellow);
		jb1.setFont(new Font("",Font.BOLD,15));
		jb1.addActionListener(this);
		jp.add(jb1);


		jb3=new JButton("C L E A R");
		jb3.setBounds(5,260,200,45);
		jb3.setBackground(Color.black);
		jb3.setForeground(Color.yellow);
		jb3.setFont(new Font("",Font.BOLD,15));
		jb3.addActionListener(this);
		jp.add(jb3);

		jb2=new JButton("E X I T");
		jb2.setBounds(5,310,200,45);
		jb2.setBackground(Color.black);
		jb2.setForeground(Color.yellow);
		jb2.setFont(new Font("",Font.BOLD,15));
		jb2.addActionListener(this);
		jp.add(jb2);
		
		jb8=new JButton("Log Details");
		jb8.setBounds(5,365,200,45);
		jb8.setBackground(Color.black);
		jb8.setForeground(new Color(0,191,255));
		jp.add(jb8);

		jb8.addActionListener(this);
		
		jcb3=new JComboBox();
		jcb3.setBounds(205,365,200,60);
		jcb3.setBorder(new TitledBorder("USERS"));
		//jp.add(jcb3);
		jcb3.addItem("ALL");
		
		jf.setVisible(true);
		cloud_files();
	}
	public void cloud_files()
	{
		Hashtable<Integer,String> cloud_bkt=new Hashtable<Integer,String>();
		try
		{
			try 
			{
				AmazonS3 s3 = new AmazonS3Client(new PropertiesCredentials(
						S3Sample.class.getResourceAsStream("AwsCredentials.properties")));

				System.out.println("===========================================");
				System.out.println("Getting Started with Amazon S3");
				System.out.println("===========================================\n");

				System.out.println("Listing buckets");
				int i=0;
				for (Bucket bucket : s3.listBuckets()) 
				{
					System.out.println(" - " + bucket.getName());
					cloud_bkt.put(++i, bucket.getName());
				}
				System.out.println(cloud_bkt);
				List<Integer> li=new Vector<Integer>(cloud_bkt.keySet());
				for(Integer i1 : li)
				{
					if(cloud_bkt.get(i1).contains(bkt_name) || cloud_bkt.get(i1).contains(bk_n))
					{
						seq++;
						List<String> l1=new Vector<String>();
						ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(cloud_bkt.get(i1)));
						for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
						{
							l1.add(objectSummary.getKey().toString()+": :" +(int) objectSummary.getSize());
						}
						file_list.put(cloud_bkt.get(i1), l1);
						jcb1.addItem(cloud_bkt.get(i1));
					}
				}
				System.out.println(file_list);
			}
			catch(AmazonServiceException ase) 
			{
				System.out.println("Caught an AmazonServiceException, which means your request made it "
						+ "to Amazon S3, but was rejected with an error response for some reason.");
				System.out.println("Error Message:    " + ase.getMessage());
				System.out.println("HTTP Status Code: " + ase.getStatusCode());
				System.out.println("AWS Error Code:   " + ase.getErrorCode());
				System.out.println("Error Type:       " + ase.getErrorType());
				System.out.println("Request ID:       " + ase.getRequestId());
			} 
			catch (AmazonClientException ace) 
			{
				System.out.println("Caught an AmazonClientException, which means the client encountered "
						+ "a serious internal problem while trying to communicate with S3, "
						+ "such as not being able to access the network.");
				System.out.println("Error Message: " + ace.getMessage());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) 
	{
		
		//new Interface();

	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		
		if(arg0.getSource()==RELOADfl)
		{
			jcb2.removeAllItems();
			List<String> l1=file_list.get(jcb1.getSelectedItem());
			for(String trv : l1)
			{
				jcb2.addItem(trv);
			}
		}
		if(arg0.getSource()==RELOAD)
		{
			jcb1.removeAllItems();
			file_list.clear();
			seq=0;
			cloud_files();
			/*try
			{
				jcb2.removeAllItems();
				List<String> l1=new Vector<String>(file_list.get(jcb1.getSelectedItem()));
				for(String trv: l1)
				{
					jcb2.addItem(trv);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}*/
		}
		if(arg0.getSource()==jb1)
		{
			String usr=JOptionPane.showInputDialog("enter the usr name");
			try
			{
				PreparedStatement ps=Login.con.prepareStatement("delete from cloud where username=?");
				ps.setString(1,usr);
				ps.executeUpdate();
				
				PreparedStatement ps1=Login.con.prepareStatement("delete from log_details where username=?");
				ps1.setString(1,usr);
				ps1.executeUpdate();


				JOptionPane.showMessageDialog(null,usr+" Deleted successfully");
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,usr+" Name mismatch can't delete ");
			}
		}
		if(arg0.getSource()==jb8)
		{
			String a=JOptionPane.showInputDialog("Press 1 for : particular usr files \n 2 : all the users \n 3 : particular class \n 4 bucket \n ");
			
			if(a.equals("1"))
			{
				String usr=JOptionPane.showInputDialog("Enetr the the user name");
				new new1(usr,a);
			}
			else if(a.equals("2"))
			{
				new new1("all",a);
			}
			else if(a.equals("3"))
			{
				String usr=JOptionPane.showInputDialog("nbot working");
				//new new1(usr,a);
				
			}
			else if(a.equals("4"))
			{
				String usr=JOptionPane.showInputDialog("Enetr the bucket name");
				new new1(usr,a);
			}
			else
			{
				JOptionPane.showMessageDialog(null, "choose proper option ");
			}
			
			//new DataView(usr);
		}
		if(arg0.getSource()==jb1)
		{
			try
			{
				String nodename=JOptionPane.showInputDialog("Eneter the usr name to delete");
				if(dlm1.contains(nodename))
				{
					PreparedStatement ps=Login.con.prepareStatement("delete from cloud where username=?");
					ps.setString(1,nodename);
					ps.executeUpdate();
					
					PreparedStatement ps1=Login.con.prepareStatement("delete from log_details where username=?");
					ps1.setString(1,nodename);
					ps1.executeUpdate();
					
					try
					{
						String[] adr=Interface.usr_prt.get(nodename).split("&");
						Registry reg=LocateRegistry.getRegistry(adr[0],Integer.parseInt(adr[1]));
						RmiInterface ri=(RmiInterface)reg.lookup(nodename);
						ri.exit();
						dlm1.removeElement(nodename);
						
					}
					catch(Exception e)
					{
						JOptionPane.showMessageDialog(null, "user inactive");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "user inactive can't delete");
				}


				JOptionPane.showMessageDialog(null,nodename+" IS TERMINATING");
				//System.exit(0);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if(arg0.getSource()==jb2)
		{
			System.exit(0);
		}
		if(arg0.getSource()==jb3)
		{
			AmazonS3 s3 = null;
			try {
				s3 = new AmazonS3Client(new PropertiesCredentials(
						S3Sample.class.getResourceAsStream("AwsCredentials.properties")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



			System.out.println("===========================================");
			System.out.println("Getting Started with Amazon S3");
			System.out.println("===========================================\n");

			System.out.println("Listing buckets");
			for (Bucket bucket : s3.listBuckets()) 
			{
				ObjectListing objects = s3.listObjects(new ListObjectsRequest().withBucketName(bucket.getName()));   
				do {

					for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
						System.out.println(objectSummary.getKey() + "\t" +

			                       
			                        StringUtils.fromDate(objectSummary.getLastModified()));
						
						s3.deleteObject(bucket.getName(),objectSummary.getKey());
					}

				} while (objects.isTruncated());
				s3.deleteBucket(bucket.getName());
			}
			file_list.clear();
			seq=0;
			
		}
	}
}


