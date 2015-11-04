
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public interface RmiInterface extends Remote
{
	public String Reg_usr(String usrname , String ip) throws RemoteException;
	public String upload(String fname,String nodename,byte[] fdata,String bktname)throws RemoteException;
	public HashMap<String, List<String>> req_flist(String usrname)throws RemoteException;
	public String upload(String fname,String usrname,byte[] data)throws RemoteException;
	public String download(String bkt_nam,String fname,String usrnam,String time)throws RemoteException;
	public String receiver(String bkt_nam,String fname,byte[] data)throws RemoteException;
	public void exit()throws RemoteException;
	public void usrt_exit(String usrname)throws RemoteException;
}
