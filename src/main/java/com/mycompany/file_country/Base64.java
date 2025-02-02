/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.file_country;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 *
 * @author abhi
 */
public class Base64 extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            long input = new Date().getTime();
            DateTime dateTimeUtc = new DateTime(input, DateTimeZone.UTC);
            DateTimeZone timeZoneIndia = DateTimeZone.forID("Asia/Kolkata");
            DateTime dateTimeIndia = dateTimeUtc.withZone(timeZoneIndia);
            String[] st = dateTimeIndia.toString().split("\\.");
            String ts = st[0] + st[1].substring(3);
            System.out.println("TS: " + ts + "\n");

            String hide = request.getParameter("b");
            String code = request.getParameter("code");
            String Name = request.getParameter("Name");
            
//            DECODING BASE 64 FILE
            byte[] actualByte = java.util.Base64.getDecoder().decode(hide);
            String actualString = new String(actualByte,"utf-8");

//          //  CREATING PDF FILE
           // File myObj = new File("" + Name + ".txt");
           // if (myObj.createNewFile()) {
           //     System.out.println("File created: " + myObj.getName());
           //     System.out.println("Absolute path: " + myObj.getAbsolutePath());
//         //       WRITING VALUE TO FILE
           //     FileWriter myWriter = new FileWriter("" + Name + ".txt");
            //    myWriter.write(actualString);
            //    myWriter.close();
            //    System.out.println("Successfully wrote to the file.");
           // } else {
              //  System.out.println("File already exists.");
          //  }
            
            
            
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Base64</title>");
            out.println("</head>");
            out.println("<body>");
//            out.println("<h1>Servlet Base64 at " + request.getContextPath() + "</h1>");
            out.println("<h2>Time: " + ts + "</h2>");
            out.println("<h2>code: " + code + "</h2>");
            out.println("<p> Base64 value:\n" + hide + "</p>");
            //out.println("<p> file path:\n" + myObj.getAbsolutePath() + "</p>");
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
        processRequest(request, response);
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
        processRequest(request, response);
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
