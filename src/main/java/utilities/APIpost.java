package utilities;



import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.UUID;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import java.net.URL;
import java.io.FileWriter;
import java.io.IOException;
import utilities.*;

public class APIpost {

    public static void main(String[] args) throws IOException {
        Utiles obj = new Utiles();
//  <---------------------------------Printing TimeStamp---------------------------------------------->
        long input = new Date().getTime();
        DateTime dateTimeUtc = new DateTime(input, DateTimeZone.UTC);
        DateTimeZone timeZoneIndia = DateTimeZone.forID("Asia/Kolkata");
        DateTime dateTimeIndia = dateTimeUtc.withZone(timeZoneIndia);
        String[] st = dateTimeIndia.toString().split("\\.");
        String s = st[0] + st[1].substring(3);
        System.out.println("TS: " + s + "\n");
        String date= s.substring(0,10);

//  <---------------------------------TXN ID--------------------------------------------------------->
        String txns = UUID.randomUUID().toString().replace("-", "");
        System.out.println("TXN: " + txns + "\n");

//  <---------------------------------Payloads------------------------------------------------------->
        String access = "emudhra123";
        String accesskey = access + s + txns;
        String hashkey = obj.Generatehash(accesskey);
//        System.out.println("hash: " + hashkey);
        String signerinfo = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
                + "<SignerInfo version=\"1.0\">\n"
                + "	<kycData address=\"add1\" country=\"IN\" date=\""+date+"\" emailId=\"ankit.s@emudhra.com\" idNumber=\"\" locality=\"loc\" name=\"Ankit\" postalCode=\"560068\" stateProvince=\"karnataka\"/>\n"
                + "</SignerInfo>";
        System.out.println("signerinfo: \n"+signerinfo+"\n");

//  <--------------------------------XML Signer------------------------------------------------------->           
        String password = "1234";
        char[] ch = new char[password.length()];

        for (int i = 0; i < password.length(); i++) {
            ch[i] = password.charAt(i);
        }
        xmlsigner xs = new xmlsigner("C:\\Users\\21701\\Downloads\\nikhil.pfx", ch, "nikhil");
        String signedsignerinfo = xs.signXML(signerinfo, true);

        
//  <--------------------------------BASE64 encode------------------------------------------------------->  
        String base64encode = obj.base64Encoder(signedsignerinfo);
        

//  <--------------------------------Request XML------------------------------------------------------->  
        String request = "<SignReq version=\"1.0\" ts=\"" + s + "\" txn=\"" + txns + "\" aspId=\"A1A1B8\" kycType=\"1\" vipId=\"A1A6\" signerId=\"\" aspProductCode=\"890-81\" signAlgorithm=\"ECC\"><docs><InputHash id=\"1\" hashAlgorithm=\"SHA256\" responseSigType=\"PKCS7\">d3e4c76387fe30bdef3a74a32cd20e289fa158d1c85cc59571dc7caf72386da5</InputHash></docs><SignerInfo>"+base64encode+"</SignerInfo></SignReq>";
        String signedXML = xs.signXML(request, true);
        System.out.println("RequestXML: \n" + signedXML + "\n");
 
        
//  <--------------------------------URLEncode------------------------------------------------------->        
        String urlEncodedRequest = obj.urlEncoder(signedXML);
//        System.out.println(urlEncodedRequest);    

//  <--------------------------------POST Request------------------------------------------------------->        
        URL url = new URL("https://server1.qa.emudhra.net/emSign-eSign-API/SignReq");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set timeout as per needs
        connection.setConnectTimeout(20000);
        connection.setReadTimeout(20000);

        // Set DoOutput to true if you want to use URLConnection for output.
        // Default is false
        connection.setDoOutput(true);
        connection.setUseCaches(true);
        connection.setRequestMethod("POST");

        // Set Headers
        connection.setRequestProperty("Accept", "application/xml");
        connection.setRequestProperty("Content-Type", "application/xml");

        // Write XML
        OutputStream outputStream = connection.getOutputStream();
        byte[] b = urlEncodedRequest.getBytes("UTF-8");
        outputStream.write(b);
        outputStream.flush();
        outputStream.close();

        // Read XML
        InputStream inputStream = connection.getInputStream();
        byte[] res = new byte[2048];
        int i = 0;
        StringBuilder response = new StringBuilder();
        while ((i = inputStream.read(res)) != -1) {
            response.append(new String(res, 0, i));
        }
        inputStream.close();

        System.out.println("Response: \n" + response.toString());

//  <--------------------------------File handling response--------------------------------------------->                
        try {
            FileWriter myWriter = new FileWriter("C:\\Users\\21701\\Downloads\\Response.txt");
            FileWriter myWriter1 = new FileWriter("C:\\Users\\21701\\Downloads\\Request.txt");
            myWriter.write(response.toString());
            myWriter.close();
            myWriter1.write(signedXML);
            myWriter1.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
