package utilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 21701
 */
import java.net.URLEncoder;

public class UrlEncoder {

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

}
