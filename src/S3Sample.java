
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.*;

public class S3Sample {
	private static String bucketName; 
private static String key   ;
long st=0,end;  
static AmazonS3 s3Client;
	
	public S3Sample(String bname,long sz,String file){
	bucketName=bname;
	key=file;
	end=sz;
	try {
       s3Client = new AmazonS3Client(new PropertiesCredentials(
                S3Sample.class.getResourceAsStream(
                		"AwsCredentials.properties")));
        
            System.out.println("Downloading an object");
            /*(S3Object s3object = s3Client.getObject(new GetObjectRequest(
            		bucketName, key));
            System.out.println("Content-Type: "  + 
            		s3object.getObjectMetadata().getContentType());
            displayTextInputStream(s3object.getObjectContent());*/
            
           // Get a range of bytes from an object.
            
            GetObjectRequest rangeObjectRequest = new GetObjectRequest(
            		bucketName, key);
            rangeObjectRequest.setRange(0, end);
            S3Object objectPortion = s3Client.getObject(rangeObjectRequest);
            
            System.out.println("Printing bytes retrieved.");
            displayTextInputStream(objectPortion.getObjectContent());
            
        }
        catch(IOException e){} catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which" +
            		" means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means"+
            		" the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    private static void displayTextInputStream(InputStream input)
     {
     		PrintWriter pw=null;
     		String key="";
     	try{
     	
    	// Read one text line at a time and display.
    
    	
        BufferedReader reader = new BufferedReader(new 
        		InputStreamReader(input));
        		 String line = reader.readLine();
        while (line!=null) {
        	pw=new PrintWriter(new FileWriter("b.txt",true));
               pw.println(line);
            System.out.println("written successfully"+line);
          //if (line == null) break;
             line = reader.readLine();

            System.out.println("    " + line);
        }
       
        }catch(Exception e){}
        finally{
        if (pw != null)
        pw.close();

        }
    }
}