package globaz.phenix.process;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class ValidationXmlParXsd2 {
    static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    public static String validXMLParXSD(String xsdPath, String xmlPath) {
        try {
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;

            dbfactory.setValidating(true);
            // --- Attribution du schéma de validation
            dbfactory.setAttribute(JAXP_SCHEMA_LANGUAGE, "http://www.w3.org/2001/XMLSchema");
            // --- Le chemin d'accès à mon fichier xsd se trouve dans un fichier
            // de config
            File f = new File(xsdPath);
            dbfactory.setAttribute(JAXP_SCHEMA_SOURCE, f);

            db = dbfactory.newDocumentBuilder();
            // --- Entity resolver pour passer outre les messages d'erreur sur
            // les entity connu de mon process

            db.setErrorHandler(new ErrorHandler() {

                @Override
                public void error(SAXParseException e) {
                    System.out.println("ligne : " + e.getLineNumber() + " : " + e.getMessage());
                }

                @Override
                public void fatalError(SAXParseException e) {

                    System.out.println("Erreur de validation XSD - Erreur fatal");
                }

                @Override
                public void warning(SAXParseException e) {
                    System.out.println("Erreur de validation XSD - Warning");
                }
            });

            // --- Parsing du fichier xml en entrée
            db.parse(new File(xmlPath));

            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
