package globaz.draco.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Classe permettant de fusionner plusieurs fichiers PUCS se trouvant dans un répertoire en créant sur la base du
 * premier fichier un nouveau fichier avec la concaténation de tous les éléments <Person> et additionne les masses ainsi
 * que le nombre de travailleurs.
 * 
 * @author dcl
 * @version 1.0
 * @since 10.02.2014
 */
public class DSFusionFichiersPUCS {

    public static final String emplacementAExporter = "C:\\Temp\\localPucsFile";
    public static final String nomDuFichierAExporter = "XML_FINAL";
    public static final String repertoireDesFichiers = "C:\\Temp\\localPucsFile";

    /**
     * Fonction à utiliser si nous désirons faire la totalité de la fusion : Fusion + Export
     * 
     * @param directory Le répertoire contenant les fichiers PUCS à fusionner.
     * @param pathWhereToExport L'emplacement où nous désirons exporter notre fichier XML final.
     * @param filenameToExport Le nom du fichier XML final.
     * @return Vrai si le fichier XML a été crée.
     */
    public static Boolean executeFusion(String directory, String pathWhereToExport, String filenameToExport) {
        File file = new File(directory);
        File[] files = file.listFiles();
        Boolean processIsOK = false;

        Document firstDocument = DSFusionFichiersPUCS.fusionElements(files);
        if (firstDocument != null) {
            processIsOK = DSFusionFichiersPUCS.export(firstDocument, pathWhereToExport, filenameToExport);
        }

        return processIsOK;
    }

    /**
     * Fonction permettant d'exporter un document XML en fichier XML.
     * 
     * @param document Document contenant la totalité des éléments <Person> et des additions de masses.
     * @param pathWhereToExport L'emplacement où déposer le nouveau fichier XML.
     * @param filenameToExport Le nom du fichier à vouloir créer.
     * @return Si le processus c'est bien déroulé.
     */
    public static Boolean export(Document document, String pathWhereToExport, String filenameToExport) {
        Writer output = null;
        Boolean processIsOk = false;

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(document);
            transformer.transform(source, result);

            output = new BufferedWriter(new FileWriter(pathWhereToExport + "\\" + filenameToExport + ".xml"));

            String xmlOutput = result.getWriter().toString();
            output.write(xmlOutput);
            processIsOk = true;
        } catch (Exception e) {
            Logger.getLogger("Exportation").log(Level.WARNING, e.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    Logger.getLogger("Exportation").log(Level.WARNING, e.getMessage());
                }
            }
        }
        Logger.getLogger("Fusion").log(Level.INFO, "Création du fichier final : " + processIsOk);
        return processIsOk;
    }

    /**
     * Fonction permettant de fusionner les différents fichiers mis en paramètres en incorporant les éléments <Person>
     * et en additionnant les masses.
     * 
     * @param files Les fichiers à devoir fusionner.
     * @return Un document contenant la totalité des éléments <Person> et les additions des masses.
     */
    public static Document fusionElements(File[] files) {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        documentFactory.setIgnoringComments(true);
        DocumentBuilder parser = null;
        Document firstDocument = null;

        // Create a XPathFactory
        XPathFactory xFactory = XPathFactory.newInstance();

        // Create a XPath object
        XPath xpath = xFactory.newXPath();

        try {
            parser = documentFactory.newDocumentBuilder();

            BigDecimal definedAVSIncomes = new BigDecimal(0);
            BigDecimal definedAVSOpen = new BigDecimal(0);
            BigDecimal definedACIncomes = new BigDecimal(0);
            BigDecimal definedACSIncomes = new BigDecimal(0);
            BigDecimal definedACOpen = new BigDecimal(0);
            Integer definedNbOfSalaries = 0;

            for (int nbFiles = 0; nbFiles < files.length; nbFiles++) {
                InputStream inputStream = new FileInputStream(files[nbFiles]);
                Reader reader = new InputStreamReader(inputStream);
                InputSource is = new InputSource(reader);
                Document currentDocument = parser.parse(is);

                definedAVSIncomes = definedAVSIncomes.add(DSFusionFichiersPUCS.getAmount(currentDocument, xpath,
                        "//AHV-AVS-Totals/Total-AHV-AVS-Incomes"));
                definedAVSOpen = definedAVSOpen.add(DSFusionFichiersPUCS.getAmount(currentDocument, xpath,
                        "//AHV-AVS-Totals/Total-AHV-AVS-Open"));
                definedACIncomes = definedACIncomes.add(DSFusionFichiersPUCS.getAmount(currentDocument, xpath,
                        "//AHV-AVS-Totals/Total-ALV-AC-Incomes"));
                definedACSIncomes = definedACSIncomes.add(DSFusionFichiersPUCS.getAmount(currentDocument, xpath,
                        "//AHV-AVS-Totals/Total-ALVZ-ACS-Incomes"));
                definedACOpen = definedACOpen.add(DSFusionFichiersPUCS.getAmount(currentDocument, xpath,
                        "//AHV-AVS-Totals/Total-ALV-AC-Open"));
                definedNbOfSalaries += DSFusionFichiersPUCS.getValue(currentDocument, xpath,
                        "//SalaryCounters/NumberOf-AHV-AVS-Salary-Tags");

                if (firstDocument == null) {
                    firstDocument = currentDocument;
                } else {
                    NodeList firstDocumentNodes = DSFusionFichiersPUCS.getNodeList(firstDocument, xpath,
                            "//Staff/Person");
                    NodeList currentDocumentNodes = DSFusionFichiersPUCS.getNodeList(currentDocument, xpath,
                            "//Staff/Person");

                    for (int i = 0; i < currentDocumentNodes.getLength(); i++) {
                        Node n = firstDocument.importNode(currentDocumentNodes.item(i), true);
                        firstDocumentNodes.item(0).getParentNode().appendChild(n);
                    }
                }
            }

            NodeList definedAVSIncomesNode = DSFusionFichiersPUCS.getNodeList(firstDocument, xpath,
                    "//AHV-AVS-Totals/Total-AHV-AVS-Incomes");
            definedAVSIncomesNode.item(0).setTextContent(definedAVSIncomes.toString());

            NodeList definedAVSOpenNode = DSFusionFichiersPUCS.getNodeList(firstDocument, xpath,
                    "//AHV-AVS-Totals/Total-AHV-AVS-Open");
            definedAVSOpenNode.item(0).setTextContent(definedAVSOpen.toString());

            NodeList definedACIncomesNode = DSFusionFichiersPUCS.getNodeList(firstDocument, xpath,
                    "//AHV-AVS-Totals/Total-ALV-AC-Incomes");
            definedACIncomesNode.item(0).setTextContent(definedACIncomes.toString());

            NodeList definedACSIncomesNode = DSFusionFichiersPUCS.getNodeList(firstDocument, xpath,
                    "//AHV-AVS-Totals/Total-ALVZ-ACS-Incomes");
            definedACSIncomesNode.item(0).setTextContent(definedACSIncomes.toString());

            NodeList definedACOpenNode = DSFusionFichiersPUCS.getNodeList(firstDocument, xpath,
                    "//AHV-AVS-Totals/Total-ALV-AC-Open");
            definedACOpenNode.item(0).setTextContent(definedACOpen.toString());

            NodeList definedNbOfSalariesNode = DSFusionFichiersPUCS.getNodeList(firstDocument, xpath,
                    "//SalaryCounters/NumberOf-AHV-AVS-Salary-Tags");
            definedNbOfSalariesNode.item(0).setTextContent(definedNbOfSalaries.toString());

            Logger.getLogger("Fusion").log(Level.INFO, "Fusion termine : " + files.length + " fichiers");
        } catch (Exception e) {
            Logger.getLogger("PUCS_FILES").log(Level.WARNING, e.getMessage());
            return null;
        }
        return firstDocument;
    }

    /**
     * 
     * @param document Le document ä devoir lire.
     * @param xpath Le xpath ä utiliser pour évaluer l'expression.
     * @param expression Expression à évaluer
     * @return L'addition des valeurs des éléments trouvés.
     * @throws XPathExpressionException
     */
    private static BigDecimal getAmount(Document document, XPath xpath, String expression)
            throws XPathExpressionException {
        // Compile the XPath expression
        XPathExpression expr = xpath.compile(expression);
        // Run the query and get a nodeset
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        double totalAmount = 0;
        for (int i = 0; i < nodes.getLength(); i++) {
            totalAmount = totalAmount + Double.valueOf(nodes.item(i).getTextContent());
        }
        BigDecimal amount = new BigDecimal(totalAmount);
        return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 
     * @param document Le document ä devoir lire.
     * @param xpath Le xpath ä utiliser pour évaluer l'expression.
     * @param expression Expression à évaluer
     * @return Une liste d'éléments matchant avec l'expression.
     * @throws XPathExpressionException
     */
    private static NodeList getNodeList(Document document, XPath xpath, String expression)
            throws XPathExpressionException {
        // Compile the XPath expression
        XPathExpression expr = xpath.compile(expression);
        // Run the query and get a nodeset
        return ((NodeList) expr.evaluate(document, XPathConstants.NODESET));
    }

    /**
     * 
     * @param document Le document ä devoir lire.
     * @param xpath Le xpath ä utiliser pour évaluer l'expression.
     * @param expression Expression à évaluer
     * @return La valeur se trouvant dans un élément.
     * @throws XPathExpressionException
     */
    private static Integer getValue(Document document, XPath xpath, String expression) throws XPathExpressionException {
        // Compile the XPath expression
        XPathExpression expr = xpath.compile(expression);
        // Run the query and get a nodeset
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        Integer value = null;
        for (int i = 0; (i < nodes.getLength()) && (value == null); i++) {
            value = Integer.valueOf(nodes.item(i).getTextContent());
        }
        return value;
    }
}