/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.apitest;

import java.io.IOException;
  

import java.io.File; 


import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utilities.APIcall;
import utilities.Conf;
import utilities.Settings;
import utilities.Utiles;
import utilities.XmlParse;
import utilities.xmlsigner;

/**
 *
 * @author 21701
 */

public class eSign extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        Utiles obj = new Utiles();
        Conf conf = new Conf();
        try (PrintWriter out = response.getWriter()) {

//            Part filePart = request.getPart("files");
            //  <---------------------------------Printing TimeStamp---------------------------------------------->
            long input = new Date().getTime();
            DateTime dateTimeUtc = new DateTime(input, DateTimeZone.UTC);
            DateTimeZone timeZoneIndia = DateTimeZone.forID("Asia/Kolkata");
            DateTime dateTimeIndia = dateTimeUtc.withZone(timeZoneIndia);
            String[] st = dateTimeIndia.toString().split("\\.");
            String ts = st[0] + st[1].substring(3);
            System.out.println("TS: " + ts + "\n");
            String date = ts.substring(0, 10);

//  <---------------------------------TXN ID--------------------------------------------------------->
            String txn = UUID.randomUUID().toString().replace("-", "");
            System.out.println("TXN: " + txn + "\n");

//  <---------------------------------Payloads------------------------------------------------------->

            String file= request.getParameter("b");
            String hash = obj.Generatehash(file);
            System.out.println("hash: " + hash);

            String req = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Esign aspId=\"ABC123\" maxWaitPeriod=\"1440\" redirectUrl=\"https://abhishek.free.beeceptor.com\" responseUrl=\"https://abhishek.free.beeceptor.com\" signerid=\"\" signingAlgorithm=\"RSA\" ts=\"" + ts + "\" txn=\"" + txn + "\" ver=\"3.0\"><Docs><InputHash docInfo=\"INFO 1\" docUrl=\"http://localhost:8080/Response\" hashAlgorithm=\"SHA256\" id=\"1\" responseSigType=\"pkcs7\">" + hash + "</InputHash></Docs></Esign>";

            String password = conf.getPassword();
            char[] ch = new char[password.length()];
            for (int i = 0; i < password.length(); i++) {
                ch[i] = password.charAt(i);
            }




File filepath = new File("resource\\Test-Class3DocumentSigner2014.pfx");

String givenPath = filepath.getPath();
String absPath = filepath.getAbsolutePath();

   

  





            xmlsigner xs = new xmlsigner(absPath, ch, "1");
            String signedXML = xs.signXML(req, true);

            String final_req = obj.urlEncoder(signedXML);
            
            String url = Settings.Posturl;
            String resp = APIcall.executePostParameters(url, final_req);;

            Document doc = XmlParse.convertStringToDocument(resp);
            NodeList nodeList = doc.getElementsByTagName("EsignResp");
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);
                String txns = null;
                String resCode = null;
                if (node.getNodeType() == 1) {
                    Element eElement = (Element) node;
                    XPath xPath1 = XPathFactory.newInstance().newXPath();
                    txns = eElement.getAttribute("txn");
                    conf.setTxns(txns);
                    resCode = eElement.getAttribute("resCode");
                    conf.setResCode(resCode);
                }
                String authURL = Settings.authURL;
                String redirecturl = txns + "|" + resCode;
                String page = obj.base64Encoder(redirecturl);
                String pageload = authURL + page;
                response.sendRedirect(pageload);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(eSign.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(eSign.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
