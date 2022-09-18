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
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.APIcall;
import utilities.GenerateHash;

/**
 *
 * @author abhi
 */
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
            String mob = request.getParameter("a");
            String email = request.getParameter("a");
            
            //  <---------------------------------Printing TimeStamp---------------------------------------------->
        long input = new Date().getTime();
        DateTime dateTimeUtc = new DateTime(input, DateTimeZone.UTC);
        DateTimeZone timeZoneIndia = DateTimeZone.forID("Asia/Kolkata");
        DateTime dateTimeIndia = dateTimeUtc.withZone(timeZoneIndia);
        String[] st = dateTimeIndia.toString().split("\\.");
        String ts = st[0] + st[1].substring(3);
        System.out.println("TS: " + ts + "\n");
        String date= ts.substring(0,10);



//  <---------------------------------TXN ID--------------------------------------------------------->
        String txn = UUID.randomUUID().toString().replace("-", "");
        System.out.println("TXN: " + txn + "\n");



//  <---------------------------------Payloads------------------------------------------------------->
        String access = "";
        String accesskey = access + ts + txn;
        String hash = GenerateHash.hash(accesskey);
        System.out.println("hash: " + hash);



//  <---------------------------------Sample Req------------------------------------------------------->
            String req = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
"<GetUserInfoReq ver=\"1.0\" ts=\""+ts+"\" txn=\""+txn+"\" accessKey=\""+hash+"\" aspID=\"MASP\" emailID=\""+email+"\" mobileNumber=\""+mob+"\"> </GetUserInfoReq>";
            
            String url = "https://qaserver-int.emudhra.net:18006/KYCExternal/enrolment/GetUserInfo";
            String resp = APIcall.executePostParameters(url, req);;
            
            System.out.println(resp);
            
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Test</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>"+resp+"</h1>");
            out.println("</body>");
            out.println("</html>");
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
