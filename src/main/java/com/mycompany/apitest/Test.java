/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.apitest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utilities.APIcall;
import utilities.GenerateHash;
import static utilities.UrlEncoder.urlEncoder;
import utilities.XmlParse;
import static utilities.base64.base64Encoder;
import utilities.xmlsigner;


/**
 *
 * @author abhi
 */
//@WebServlet("/Test")
@MultipartConfig
public class Test extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
//            String mob = request.getParameter("a");
            String file = request.getParameter("file");

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



//        String file = request.getParameter("file");
        
//            String filebase64 = base64Encoder(file.toString());

            String hash = GenerateHash.hash(file);
            System.out.println("hash: " + hash);

            String url = "https://authenticate.sandbox.emudhra.com/eSignExternal/v3_0/eSignRequest";

            String req = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Esign aspId=\"ABC123\" maxWaitPeriod=\"1440\" redirectUrl=\"https://abhishek.free.beeceptor.com\" responseUrl=\"https://abhishek.free.beeceptor.com\" signerid=\"\" signingAlgorithm=\"RSA\" ts=\"" + ts + "\" txn=\"" + txn + "\" ver=\"3.0\"><Docs><InputHash docInfo=\"INFO 1\" docUrl=\"http://localhost:8080/Response\" hashAlgorithm=\"SHA256\" id=\"1\" responseSigType=\"pkcs7\">" + hash + "</InputHash></Docs></Esign>";

            String password = "emudhra";
            char[] ch = new char[password.length()];

            for (int i = 0; i < password.length(); i++) {
                ch[i] = password.charAt(i);
            }
            xmlsigner xs = new xmlsigner("/Users/abhi/NetBeansProjects/xmlsigner/Test-Class3DocumentSigner2014.pfx", ch, "1");
            String signedXML = xs.signXML(req, true);

            String final_req = urlEncoder(signedXML);

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
                    resCode = eElement.getAttribute("resCode");

                }
                String authURL = "https://authenticate.sandbox.emudhra.com/index.jsp?txnref=";
                String redirecturl = txns + "|" + resCode;
                String page = base64Encoder(redirecturl);
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
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
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
