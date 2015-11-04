
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;





import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;


public class CloudUpload extends Thread
{
	 private static AWSCredentials credentials;
	    private static TransferManager tx;
	    public static String res,bucketName;
	    
	    private JProgressBar pb;
	    private JFrame frame;
	    private Upload upload;
	    private JButton button;
	    CloudUpload()
	    {
	    	
	    }
	CloudUpload(String path,String bucket) throws Exception
	{
		
		String response = bucket;
		System.out.println(response);
		
		 frame = new JFrame("Amazon S3 Object Upload");
	      

	        pb = new JProgressBar(0, 100);
	        pb.setStringPainted(true);

	        frame.setContentPane(createContentPane());
	        frame.pack();
	        frame.setVisible(true);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try 
		{
			AmazonS3 s3 = new AmazonS3Client(new PropertiesCredentials(
					S3Sample.class.getResourceAsStream("AwsCredentials.properties")));



			System.out.println("===========================================");
			System.out.println("Getting Started with Amazon S3");
			System.out.println("===========================================\n");



		
			System.out.println("Creating bucket " + response + "\n");
			//s3.createBucket(response);
			s3.createBucket(response);
		
			System.out.println("bucket created succesfully");

			System.out.println();


		}
		catch(IOException e){}

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
		try {
			credentials = new PropertiesCredentials(CloudUpload.class
			        .getResourceAsStream("AwsCredentials.properties"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        // TransferManager manages a pool of threads, so we create a
        // single instance and share it throughout our application.
        tx = new TransferManager(credentials);
		res=response;
		System.out.println("res is "+res);
		try {
			 ProgressListener progressListener = new ProgressListener()
			 {
	                public void progressChanged(ProgressEvent progressEvent)
	                {
	                    if (upload == null) 
	                    	{
	                    	frame.dispose();
	                    		return;
	                    	}
	                    
	                    pb.setValue((int)upload.getProgress().getPercentTransfered());
	                   
	                    
	                    switch (progressEvent.getEventCode())
	                    {
	                    case ProgressEvent.COMPLETED_EVENT_CODE:
	                    	
	                        pb.setValue(100);
	                        
	                        break;
	                    case ProgressEvent.FAILED_EVENT_CODE:
	                        try {
	                            AmazonClientException e = upload.waitForException();
	                            JOptionPane.showMessageDialog(frame, 
	                                    "Unable to upload file to Amazon S3: " + e.getMessage(), 
	                                    "Error Uploading File", JOptionPane.ERROR_MESSAGE);
	                        } catch (InterruptedException e) {}
	                        break;
	                    }
	                }
	            };
	            
	         
				System.out.println("waiting");
	            this.sleep(50000);	
						System.out.println("finished");
							 File fileToUpload=new File(path);	
							
	            PutObjectRequest request = new PutObjectRequest(
	                   response, fileToUpload.getName(), fileToUpload)
	                .withProgressListener(progressListener);
	            upload = tx.upload(request);
	            
	            System.out.println("UPLOADED SUCCESFULLY");
	          
	        }


		

		catch(AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
		catch(Exception e22){}
	
	try
	{
		
	}
	catch(Exception e)
	{
		
	}
	
	}
	 
	private JPanel createContentPane() {
	        JPanel panel = new JPanel();
	        
	        panel.add(pb);

	        JPanel borderPanel = new JPanel();
	        borderPanel.setLayout(new BorderLayout());
	        borderPanel.add(panel, BorderLayout.NORTH);
	        borderPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	        return borderPanel;
	    }
	
	 public static void main(String[] args) {
		new CloudUpload();
	}
}
