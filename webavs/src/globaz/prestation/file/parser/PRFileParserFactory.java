package globaz.prestation.file.parser;

import globaz.osiris.file.paiement.exception.LabelNameException;
import globaz.prestation.file.parser.exception.PRLabelNameException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class PRFileParserFactory {

    private static final String ATTRIBUTE_BEGIN_POS = "beginPos";
    private static final String ATTRIBUTE_END_POS = "endPos";
    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_SOURCE = "source";
    private static final String TAG_CLASS = "class";
    private static final String TAG_FILE = "file";
    private static final String TAG_TEXT_FIELD = "text-field";

    /**
     * Charge la configuration d'un IPRFileParser
     * 
     * @param fileName
     *            DOCUMENT ME!
     * @param fileId
     *            DOCUMENT ME!
     * 
     * @return une liste de PRTextField
     * 
     * @throws LabelNameException
     *             DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final List<PRTextField> loadConfiguration(String fileName, String fileId)
            throws PRLabelNameException, Exception {
        return PRFileParserFactory.loadConfiguration(fileName, fileId, new String[2]);
    }

    private static final List<PRTextField> loadConfiguration(String fileName, String fileId, String[] extras)
            throws PRLabelNameException, Exception {
        InputStream inp = PRFileParserFactory.class.getResourceAsStream("/" + fileName);
        InputSource is = new InputSource(inp);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(is);

        if (document == null) {
            throw (new PRLabelNameException("ERROR_PARSING_FILE", fileName));
        }

        NodeList nodes = document.getElementsByTagName(PRFileParserFactory.TAG_FILE);
        List<PRTextField> txtFields = new ArrayList<PRTextField>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String id = node.getAttributes().getNamedItem(PRFileParserFactory.ATTRIBUTE_ID).getNodeValue();

            if ((fileId != null) && (fileId.equals(id))) {
                // récupération des attribut
                if ((node.getAttributes() != null)
                        && (node.getAttributes().getNamedItem(PRFileParserFactory.ATTRIBUTE_SOURCE) != null)) {
                    extras[0] = node.getAttributes().getNamedItem(PRFileParserFactory.ATTRIBUTE_SOURCE).getNodeValue();
                }

                // classes à instancier...
                NodeList childs = node.getChildNodes();

                for (int c = 0; c < childs.getLength(); c++) {
                    if (childs.item(c).getNodeName().equals(PRFileParserFactory.TAG_CLASS)) {
                        extras[1] = childs.item(c).getAttributes().getNamedItem(PRFileParserFactory.ATTRIBUTE_NAME)
                                .getNodeValue();
                    } else if (childs.item(c).getNodeName().equals(PRFileParserFactory.TAG_TEXT_FIELD)) {
                        String fieldName = childs.item(c).getAttributes()
                                .getNamedItem(PRFileParserFactory.ATTRIBUTE_ID).getNodeValue();
                        String beginPos = childs.item(c).getAttributes()
                                .getNamedItem(PRFileParserFactory.ATTRIBUTE_BEGIN_POS).getNodeValue();
                        String endPos = childs.item(c).getAttributes()
                                .getNamedItem(PRFileParserFactory.ATTRIBUTE_END_POS).getNodeValue();

                        txtFields.add(new PRTextField(fieldName, Integer.valueOf(beginPos).intValue(), Integer.valueOf(
                                endPos).intValue()));
                    }
                }

                break;
            }
        }

        return txtFields;
    }

    /**
     * crée une nouvelle instance de IPRFileParser
     * 
     * @param fileName
     *            DOCUMENT ME!
     * @param fileId
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws LabelNameException
     *             DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static IPRFileParser newInstance(String fileName, String fileId) throws PRLabelNameException, Exception {
        String[] extras = new String[2];
        List<PRTextField> txtFields = PRFileParserFactory.loadConfiguration(fileName, fileId, extras);

        Class<?> c = Class.forName(extras[1]);
        IPRFileParser instance = (IPRFileParser) c.newInstance();

        instance.setSource(extras[0]);

        if (instance instanceof APRTextFileParser) {
            for (int i = 0; i < txtFields.size(); i++) {
                PRTextField txtField = txtFields.get(i);
                ((APRTextFileParser) instance).addField(txtField);
            }
        }

        return instance;
    }

    /**
     * Constructor for PRFileParserFactory.
     */
    public PRFileParserFactory() {
        super();
    }
}
