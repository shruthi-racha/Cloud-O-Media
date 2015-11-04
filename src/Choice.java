import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;


public class Choice extends Thread 
{

		JFrame jf;
		JPanel jp;
		JButton jb1,jb2;
		static String nodename;
		Choice()
		
		{
			this.start();
		}
		
		public void run()
		{
			try
			{
				java.lang.Process process1 = Runtime.getRuntime().exec("C:\\Documents and Settings\\PURUSHOTHAM\\My Documents\\NetBeansProjects\\BYOD\\src\\exe\\IntDis.exe");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	public static void main(String[] args) {
		
		new Choice();

	}

}
