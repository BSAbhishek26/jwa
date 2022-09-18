/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlParse {
  public static Document convertStringToDocument(String xmlStr) throws Exception {
    Document doc = null;
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      doc = builder.parse(new InputSource(new StringReader(xmlStr)));
    } catch (Exception e) {
      throw e;
    } 
    return doc;
  }
  
  protected static String GetXpathValue(XPath xPath, String RequestPath, Document doc) throws XPathExpressionException {
    String XpathValue = xPath.compile(RequestPath).evaluate(doc);
    xPath.reset();
    return XpathValue;
  }
  
  public static String getSignedDoc(String resXml) {
    String SignedData = null;
    String status = null;
    try {
      Document doc = convertStringToDocument(resXml);
      XPath xPath1 = XPathFactory.newInstance().newXPath();
      status = GetXpathValue(xPath1, "/EsignResp/@status", doc);
      if (status.equals("1")) {
        NodeList list = doc.getElementsByTagName("resCode");
        for (int itr = 0; itr < list.getLength(); itr++) {
          Node node = list.item(itr);
          if (node.getNodeType() == 1) {
            Element eElement = (Element)node;
            SignedData = eElement.getAttribute("status");
            System.out.println("\nstatus: " + SignedData);
          } 
        } 
      } else {
        SignedData = GetXpathValue(xPath1, "/EsignResp/@errMsg", doc);
        System.out.println("\nError: " + SignedData);
      } 
    } catch (XPathExpressionException ex) {
      ex.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return SignedData;
  }
}
