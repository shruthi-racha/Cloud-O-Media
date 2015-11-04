import javax.swing.JOptionPane;


public class exitclass implements Runnable
{
	Thread t1;
	exitclass()
	{
		JOptionPane.showMessageDialog(null, "Admin has terminated subscription");
		
		t1=new Thread(this);
		t1.start();
	}
	public void run()
	{
		try
		{
			t1.sleep(5000);
			System.exit(0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
