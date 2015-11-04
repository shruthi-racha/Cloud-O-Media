import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


@SuppressWarnings({ "serial", "unused" })
public final class RMI extends UnicastRemoteObject implements Runnable,RmiInterface
{
	Thread trmi;
	Registry reg;	
	int port;
	String nodeNam;
	static double timeRequired,timeReqDownload,timeReqDown;
	int fileLength;
	private Connection con=new Database().conn;
	public RMI(int regPort,String nodeName)throws RemoteException
	{			
		port=regPort;
		nodeNam=nodeName;
		System.out.println(port);
		trmi=new Thread(this,nodeName);
		trmi.start();
	}
	public void usrt_exit(String usrname)throws RemoteException
	{
		if(Interface.dlm1.contains(usrname))
		{
			Interface.dlm1.removeElement(usrname);
			
			//Interface.jcb3.addItem(usrname);
		}
	}
	public String Reg_usr(String usrname , String ip) throws RemoteException
	{
		try
		{
			if(!Interface.dlm1.contains(usrname))
			{
				Interface.dlm1.addElement(usrname);
				
				//Interface.jcb3.addItem(usrname);
			}
			Interface.port_no=Interface.port_no+1;
			if(Interface.usr_prt.containsKey(usrname))
			{
				Interface.usr_prt.remove(usrname);
				Interface.usr_prt.put(usrname, ip+"&"+Interface.port_no);
			}
			else
			{
				Interface.usr_prt.put(usrname, ip+"&"+Interface.port_no);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			              
		}
		
		return "success"+"&"+(Interface.port_no);
	}
	public void exit()throws RemoteException
	{
		new exitclass();
	}
	public HashMap<String, List<String>> req_flist(String usrname)throws RemoteException
	{
		return Interface.file_list;
	}
	public String upload(String fname,String usrname,byte[] data)throws RemoteException
	{
		String bktnm="";
		try
		{
			JOptionPane.showMessageDialog(null,"Time taken to upload is "+timeRequired);
			if(Interface.seq==0 && Interface.file_list.isEmpty())
			{
				JOptionPane.showMessageDialog(null,"If Loop is executed");
					
				 bktnm=Interface.bkt_name+(++Interface.seq);
					//Interface.jcb1.addItem(bktnm);
					
					new uploader(bktnm,fname,data,usrname);
			}
			else
			{
				JOptionPane.showMessageDialog(null,"else Loop is executed");
				List<String> l1=new Vector<String>(Interface.file_list.keySet());
				
				for(String trv : l1)
				{
					if(!trv.contains(Interface.bk_n))
					{
						int cnt=Interface.file_list.get(trv).size();
						if(cnt<3)
						{
							double timeRequired1=0;
							if(fileLength!=0)
							{
								System.out.println("before time require : "+timeRequired);
							double tempDataLength=data.length/fileLength;
							 timeRequired1=timeRequired;
							timeRequired1*=tempDataLength;
							 timeRequired1/=1000;
							}
							JOptionPane.showMessageDialog(null,"time require : "+timeRequired+"-"+Interface.uplod_bkt.get(trv));
							if(timeRequired1>50 && Interface.uplod_bkt.get(trv)>=1)
							{
							
								JOptionPane.showMessageDialog(null,"Time required more so using another bucket uploading to "+trv);
								Interface.seq++;
								bktnm=Interface.bkt_name+(Interface.seq);
								try
								{
									new uploader(bktnm,fname,data,usrname);
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								
							}
							else
							{
								bktnm=trv;
								
								try
								{
									JOptionPane.showMessageDialog(null,"Less Time required  so using same bucket uploading to "+trv);
									new uploader(bktnm,fname,data,usrname);
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								
							}
							break;
						}
					}
					
				}
				if(bktnm.equals(""))
				{
					Interface.seq++;
					bktnm=Interface.bkt_name+(Interface.seq);
					try
					{
						new uploader(bktnm,fname,data,usrname);
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
						
					}
					
				}
				
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
		float length=(data.length/(1024*1024));
		JOptionPane.showMessageDialog(null,"datalength"+data.length+"   length"+length);
		PreparedStatement ps=con.prepareStatement("insert into log_details values(?,?,?,?,?,?)");
		ps.setString(1,usrname);
		ps.setString(2,fname);
		ps.setString(3,bktnm);
		ps.setString(4,new java.util.Date()+"");
		ps.setString(5,"U");
		ps.setFloat(6,length);
		ps.executeUpdate();
		ps.clearBatch();
		ps=con.prepareStatement("select uploaded from cloud where username=?");
		ps.setString(1,usrname);
		ResultSet rs=ps.executeQuery();
		if(rs.next())
		{
			PreparedStatement ps1=con.prepareStatement("update cloud set uploaded=? where username=?");
			ps1.setFloat(1,rs.getFloat(1)+length);
			ps1.setString(2,usrname);
			ps1.executeUpdate();
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "uploding";
	}
	class uploader implements Runnable
	{
		String bkt;
		String fname;
		byte[] data;
		String usr_name;
		uploader(String bkt,String fname,byte[] data,String usr_name)throws Exception
		{
			this.bkt=bkt;
			this.fname=fname;
			this.data=data;
			this.usr_name=usr_name;
			Thread t1=new Thread(this);
			t1.start();
		}
		public void write(String fname,byte[] data,String path)
		{
			try
			{
				FileOutputStream fout= new FileOutputStream(path);		
				fout.write(data);
				fout.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

		}
		public void run()
		{
			try
			{
				String path="cloudfiles\\"+fname;
				try
				{
					
					write(fname,data,path);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				try
				{
					
					//double startTime=System.currentTimeMillis();
					Interface.counter++;
					if(Interface.uplod_bkt.containsKey(bkt))
					{
						int i=Interface.uplod_bkt.get(bkt);
						Interface.uplod_bkt.put(bkt, ++i);
					}
					else
					{
						Interface.uplod_bkt.put(bkt, 1);
					}
					try
					{
						System.out.println("uploding : bktnam : "+bkt);
						//new CloudUpload(path,bkt);
						File f=new File(path);
						new S3TransferProgressSample(bkt,f);
						System.out.println("uploded : bktnam : "+bkt);
						Interface.jcb1.addItem(bkt);
						int i=Interface.uplod_bkt.get(bkt);
						Interface.uplod_bkt.put(bkt, --i);
						Interface.counter--;
						//double endTime=System.currentTimeMillis()-startTime;
						//timeRequired=endTime;
						JOptionPane.showMessageDialog(null,"Time taken to upload is "+timeRequired);
						fileLength=data.length;
						if(Interface.file_list.containsKey(bkt))
						{
							Interface.file_list.get(bkt).add((fname+ ": :"+ data.length));
						}
						else
						{
							List<String> l1=new Vector<String>();
							l1.add((fname+ ": :"+ data.length));
							Interface.file_list.put(bkt, l1);
							
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			 File file = new File("cloudfiles\\");        
		        String[] myFiles;      
		            if(file.isDirectory()){  
		                myFiles = file.list();  
		                for (int i=0; i<myFiles.length; i++) {  
		                    File myFile = new File(file, myFiles[i]);   
		                    myFile.delete();  
		                }  
		             }  
		}
	}
	public String receiver(String bkt_nam,String fname,byte[] data)throws RemoteException
	{
		System.out.println("fname : "+fname);
		System.out.println("bkname : "+bkt_nam);
		System.out.println("data : "+data);
		Node.jta.setText(fname+bkt_nam+data);
		 try
		   {
			 JFileChooser jf=new JFileChooser();
		         int m=jf.showSaveDialog(null);
		         if(m==JFileChooser.APPROVE_OPTION) 
		         {
			      File f1=jf.getSelectedFile();
			      String path=f1.getAbsolutePath();
				  FileOutputStream fop=new FileOutputStream(path,true);
			      fop.write(data);
				  fop.close();
		         }
		    }
		   catch(Exception e)
		   {
			   
		   }
		return "received";
	}
	
	public String upload(String fname,String nodename,byte[] fdata,String bktname)throws RemoteException
	{
		try
		{
			try
			{
				String path="cloudfiles\\"+fname;
				try
				{
					FileOutputStream fout= new FileOutputStream(path);		
					fout.write(fdata);
					fout.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				new S3Download(bktname,new File("cloudfiles\\"+fname));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return "uploding";
	}
	
	public String download(String bkt_nam,String fname,String usrnam,String time)throws RemoteException
	{
		try
		{
			
			if(Interface.usr_count.containsKey(bkt_nam))
			{
				//Interface.usr_count.put(bkt_nam, Interface.usr_count.get(bkt_nam).put(us, arg1))
				if(Interface.usr_count.get(bkt_nam).containsKey(usrnam))
				{
					Interface.usr_count.get(bkt_nam).get(usrnam).add(fname);
				}
				else
				{
					List<String> n1=new Vector<String>();
					n1.add(fname);
					Interface.usr_count.get(bkt_nam).put(usrnam, n1);
				}
			}
		
			else
			{
				System.out.println("else loop");
				List<String> n1=new Vector<String>();
				n1.add(fname);
				System.out.println(n1);
				HashMap<String, List<String>> usr_ount=new HashMap<String, List<String>>();
				usr_ount.put(usrnam, n1);
				System.out.println(usr_ount);
				Interface.usr_count.put(bkt_nam,usr_ount );
				System.out.println(Interface.usr_count);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Interface." +Interface.usr_count);
		List<String> l1=new Vector<String>(Interface.usr_count.get(bkt_nam).keySet());
		System.out.println(l1);
		List<String> l2=new Vector<String>();
		int incr=0;
		for(String i : l1)
		{
			List<String> l3=Interface.usr_count.get(bkt_nam).get(i);
			for(String jk : l3)
			{
				l2.add(jk);
				if(jk.equals(fname))
				{
					++incr;
				}
			}
			
		}
		System.out.println(l2);
		System.out.println(incr);
		

		timeReqDown=timeReqDownload;
		
		//if(timeReqDown>=Double.parseDouble(time.trim()))
		{
		
			//if(l2.contains(fname)&& incr>=1)
			{
				new downloader(bkt_nam,fname,usrnam);
			}
		}
		/*else
		{
			try {
				new CloudDownload("filesrvlowquality",fname);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		return "downloading";
	}
	class downloader implements Runnable
	{
		String bkt_nam;
		String fname;
		String usrnam;
		downloader(String bkt_nam,String fname,String usrnam)
		{
			this.bkt_nam=bkt_nam;
			this.fname=fname;
			this.usrnam=usrnam;
			System.out.println("downloder");
			Thread t1=new Thread(this);
			t1.start();
		}
		public void run()
		{
			try
			{
				String bkt="";
				if(Interface.file_list.containsKey(bkt_nam))
				{
					
					if(Interface.dwn_bkt.containsKey(bkt_nam) && Interface.dwn_bkt.get(bkt_nam)<2 )
					{
						Interface.dwn_bkt.put(bkt_nam, 1+(Interface.dwn_bkt.get(bkt_nam)));
						try
						{
							new CloudDownload(bkt_nam,fname);
							Interface.dwn_bkt.put(bkt_nam, (Interface.dwn_bkt.get(bkt_nam))-1);
							byte[] fileread=null;
							try
							{
								File f=new File("download\\"+fname);
								FileInputStream fis=new FileInputStream(f);
								fileread = new byte[fis.available()];
								fis.read(fileread);
								fis.close();
								sender(bkt_nam,fname,usrnam,fileread);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
							return;
						}
						System.out.println("Interface.dwn_bkt :"+Interface.dwn_bkt);
					}
					else if(Interface.dwn_bkt.containsKey(bkt_nam) && Interface.dwn_bkt.get(bkt_nam)>=2)
					{
						//bucket copy code;
						System.out.println("Interface.dwn_bkt :"+Interface.dwn_bkt);
						System.out.println("bucket copy code");
						String bk="";
						List<String> l1=new Vector<String>(Interface.file_list.keySet());
						String bktnm="";
						for(String trv : l1)
						{
							int cnt=Interface.file_list.get(trv).size();
							if(cnt<3 )
							{
								if(Interface.dwn_bkt.containsKey(trv))
								{
									if(Interface.dwn_bkt.get(trv)<2)
									{
										bktnm=trv;
										Interface.dwn_bkt.put(trv, 1+(Interface.dwn_bkt.get(trv)));
										break;
									}
								}
								else
								{
									bktnm=trv;
									Interface.dwn_bkt.put(bktnm, 1);
								}
								
							}
							System.out.println("in side else if loop");
							System.out.println(Interface.file_list);
							System.out.println(Interface.dwn_bkt);
						}
						
						if(bktnm.equals(""))
						{
							Interface.seq++;
							bktnm=Interface.bkt_name+(Interface.seq);
							Interface.dwn_bkt.put(bktnm, 1);
						}
						System.out.println("inside else if loop for copy code ");
						
						//copy code;
						
						new CloudDownload(bkt_nam,fname,bktnm);
						
						
						//
						if(Interface.usr_count.get(bkt_nam).get(usrnam).contains(fname))
						{
							Interface.usr_count.get(bkt_nam).get(usrnam).remove(fname);
							if(Interface.usr_count.containsKey(bktnm))
							{
								//Interface.usr_count.put(bkt_nam, Interface.usr_count.get(bkt_nam).put(us, arg1))
								if(Interface.usr_count.get(bktnm).containsKey(usrnam))
								{
									Interface.usr_count.get(bktnm).get(usrnam).add(fname);
								}
								else
								{
									List<String> n1=new Vector<String>();
									n1.add(fname);
									Interface.usr_count.get(bktnm).put(usrnam, n1);
								}
							}
							else
							{
								List<String> n1=new Vector<String>();
								n1.add(fname);
								HashMap<String, List<String>> usr_ount=new HashMap<String, List<String>>();
								 usr_ount.put(usrnam, n1);
								Interface.usr_count.put(bktnm,usr_ount );
								System.out.println(Interface.usr_count);
							}
							
						}
						
						if(Interface.dwn_bkt.containsKey(bktnm) && Interface.dwn_bkt.get(bktnm)<2 )
						{
							Interface.dwn_bkt.put(bktnm, 1+(Interface.dwn_bkt.get(bktnm)));
							try
							{
								new CloudDownload(bktnm,fname);
								Interface.dwn_bkt.put(bktnm, (Interface.dwn_bkt.get(bktnm))-1);
								byte[] fileread=null;
								
								try
								{
									File f=new File("download\\"+fname);
									FileInputStream fis=new FileInputStream(f);
									fileread = new byte[fis.available()];
									fis.read(fileread);
									fis.close();
									sender(bktnm,fname,usrnam,fileread);
									if(Interface.file_list.containsKey(bktnm))
									{
										Interface.file_list.get(bktnm).add((fname+ ": :"+ fileread.length));
									}
									else
									{
										List<String> l11=new Vector<String>();
										l1.add((fname+ ": :"+ fileread.length));
										Interface.file_list.put(bktnm, l11);
										
									}
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							catch(Exception e)
							{
								e.printStackTrace();
								return;
							}
							
						}
						else
						{
							Interface.dwn_bkt.put(bktnm, 1);
							try
							{
								new CloudDownload(bktnm,fname);
								Interface.dwn_bkt.put(bktnm, (Interface.dwn_bkt.get(bktnm))-1);
								byte[] fileread=null;
								try
								{
									File f=new File("download\\"+fname);
									FileInputStream fis=new FileInputStream(f);
									fileread = new byte[fis.available()];
									fis.read(fileread);
									fis.close();
									sender(bktnm,fname,usrnam,fileread);
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							catch(Exception e)
							{
								e.printStackTrace();
								return;
							}
						}
						
					}
					else 
					{
						Interface.dwn_bkt.put(bkt_nam, 1);
						try
						{
							new CloudDownload(bkt_nam,fname);
							Interface.dwn_bkt.put(bkt_nam, (Interface.dwn_bkt.get(bkt_nam))-1);
							byte[] fileread=null;
							try
							{
								File f=new File("download\\"+fname);
								FileInputStream fis=new FileInputStream(f);
								fileread = new byte[fis.available()];
								fis.read(fileread);
								fis.close();
								sender(bkt_nam,fname,usrnam,fileread);
								//f.delete();
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
							return;
						}
					}
		
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
	public void sender(String bk_nam,String fnam,String usrnam,byte[] data)
	{
		try
		{
			List<String> k1=new Vector(Interface.usr_count.get(bk_nam).keySet());
			System.out.println("sender : "+k1);
			for(String t1 : k1)
			{
				System.out.println(Interface.usr_count);
				if(Interface.usr_count.get(bk_nam).get(t1).contains(fnam))
				{
					System.out.println(Interface.usr_count);
					Interface.usr_count.get(bk_nam).get(t1).remove(fnam);
					System.out.println(Interface.usr_count);
					System.out.println(Interface.usr_prt);
					String[] adr=Interface.usr_prt.get(t1).split("&");
					System.out.println();
					try
					{
						Registry reg=LocateRegistry.getRegistry(adr[0],Integer.parseInt(adr[1]));
						RmiInterface ri=(RmiInterface)reg.lookup(t1);
						String ack=ri.receiver(bk_nam, fnam, data);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void run()
	{				
		 	try
		 	{	System.out.println(port);	
				reg=LocateRegistry.createRegistry(port);
				reg.rebind(nodeNam,this);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}			
	}
}
