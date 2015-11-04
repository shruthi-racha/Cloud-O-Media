
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;


public class CloudDownload extends Thread
{

	CloudDownload(String bucketName,String filename)throws Exception
	{
		String s="";
		try{

			this.sleep(10000);
			AmazonS3 s3Client= new AmazonS3Client(new PropertiesCredentials(
					S3Sample.class.getResourceAsStream("AwsCredentials.properties")));

			//S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, "Employee1.txt"));

			//S3Object objectComplete = s3Service.getObject(testBucket, "helloWorld.txt");
			
	
            
			
			S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, filename));
			InputStream reader = new BufferedInputStream(object.getObjectContent());
			File file = new File("download\\"+filename);      
			OutputStream writer = new BufferedOutputStream(new FileOutputStream(file));

			
			S3Object objectDetailsOnly = s3Client.getObject(bucketName, filename);
			System.out.println("S3Object, details only: " + objectDetailsOnly);
			System.out.println("S3Object, details only: " + object.getObjectContent());
			

			JOptionPane.showMessageDialog(null, objectDetailsOnly);
			int read = -1;
			double startTime=System.currentTimeMillis()/1000+RMI.timeReqDown;
			
			while ( ( read = reader.read() ) != -1 ) {
			    writer.write(read);
			}
			double endTime=System.currentTimeMillis()/1000-startTime;
			RMI.timeReqDownload=endTime-RMI.timeReqDown;
			writer.flush();
			writer.close();
			reader.close();


/*			
			int i=0;
			
			while (true)
			{          
				
				
				//reader.
				String line = reader.readLine();           
				if (line == null)
					break;            
				System.out.println("\n---------------------------------\n"+line+"\n---------------------------------\n");
				s+=line;
				
				//writer.write(s);
			}

			//writer.close();
			System.out.println("finished");
			//byte[] b=new byte[s.getBytes().length];
			FileOutputStream fop1=new FileOutputStream("download1\\"+filename);
			fop1.write(s.getBytes());*/


		}catch(Exception e){
			FileOutputStream fop1=new FileOutputStream("download1\\"+filename);
			fop1.write(s.getBytes());
			e.printStackTrace();
		};
	}
	CloudDownload(String bucketName,String filename,String destbkt)throws Exception
	{
		try{

			AmazonS3 s3Client= new AmazonS3Client(new PropertiesCredentials(
					S3Sample.class.getResourceAsStream("AwsCredentials.properties")));

			s3Client.copyObject(bucketName, filename,destbkt, filename);
			System.out.println("copied to bucket : "+destbkt);
			/*S3Object object = s3Client.getObject(new GetObjectRequest(destbkt, filename));
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					object.getObjectContent()));
			File file = new File("download\\"+filename);      
			Writer writer = new OutputStreamWriter(new FileOutputStream(file));

			while (true)
			{          
				String line = reader.readLine();           
				if (line == null)
					break;            

				writer.write(line + "\n");
			}
			*/
		}
		catch(Exception e){};
	}
	public static void main(String[] args) {
		try {
			new CloudDownload("rvfiles1","BellmanFordSP.java","rvfiles2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
