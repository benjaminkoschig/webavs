package globaz.pegasus.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

public class Xml {

    public static void main(String[] args) throws Exception {
        /*
         * String xmlString = new String( ("<ul class='onglets'><li>" +
         * "<a href='loyer.afficher&noVersion=1&idVersionDroit=1' title='Loyer'>Loyer</a></li><li class='selected'>Taxe journalière dans </li></ul>"
         * ) .getBytes("UTF8"), "UTF8"); String str = new String(xmlString.getBytes("UTF8"), "ISO-8859-15");
         * System.out.print(Xml.stringToXMLPrettyFormat(xmlString));
         */

        BigDecimal arr = new BigDecimal(123456.555).setScale(0, BigDecimal.ROUND_HALF_DOWN);
        System.out.println(arr);

        double value = 123.5000;
        BigDecimal bd = new BigDecimal(value).setScale(0, BigDecimal.ROUND_HALF_UP);
        System.out.println(bd);

    }

    /**
     * @param doc
     * @param out
     * @throws Exception
     */
    private static void serialize(Document doc, StringWriter out) throws Exception {

        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer;
        try {
            serializer = tfactory.newTransformer();
            // Setup indenting to "pretty print"
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

            serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            DOMSource xmlSource = new DOMSource(doc);
            // xmlSource.
            StreamResult outputTarget = new StreamResult(out);

            serializer.transform(xmlSource, outputTarget);
        } catch (TransformerException e) {
            // this is fatal, just dump the stack and throw a runtime exception
            // e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String stringToXMLPrettyFormat(String xmlString) {
        String strToReturn = "";
        StringWriter writer = new StringWriter();
        try {
            xmlString = xmlString.replaceAll("&", "&#38;");
            System.out.println(xmlString);

            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes("ISO-8859-15"));
            // documentBuilder.
            Document document = documentBuilder.parse(inputStream);

            StringWriter xmlout = new java.io.StringWriter();

            Xml.serialize(document, xmlout);
            strToReturn = xmlout.getBuffer().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strToReturn;
    }
}