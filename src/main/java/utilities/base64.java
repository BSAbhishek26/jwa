package utilities;

import java.util.*;
public class base64 {
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

