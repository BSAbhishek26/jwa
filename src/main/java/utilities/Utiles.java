package utilities;


import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

// Java program to calculate SHA hash value
public class Utiles {

    public static String Generatehash(String hashkey) {
//        String base = request;
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(hashkey.getBytes("UTF-8"));
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hexString.toString();

    }
    
    public static String urlEncoder(String request) {
        String urlencodedXML = "";
        try {
            urlencodedXML = URLEncoder.encode(request, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//		System.out.println(url);
        return urlencodedXML;
    }
    
     public static String base64Encoder(String signerinfo) {
        String sample = "";
        // Encode into Base64 format
        String BasicBase64format = Base64.getEncoder().encodeToString(signerinfo.getBytes());
        
        // Decode Base64      
        byte[] actualByte = Base64.getDecoder().decode(sample);
        String actualString = new String(actualByte);
 
        return BasicBase64format;
    }
}
