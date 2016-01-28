package globaz.osiris.file.paiement;

import globaz.osiris.file.paiement.exception.LabelNameException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Classe : type_conteneur Description : Date de création: 2 mars 04
 * 
 * @author scr
 */
public class CAFileParserFactory {
    private final static String ATTRIBUTE_BEGIN_POS = "beginPos";
    private final static String ATTRIBUTE_CODE_ISO_MONNAIE = "codeIsoMonnaie";
    private final static String ATTRIBUTE_END_POS = "endPos";

    private final static String ATTRIBUTE_ID = "id";
    private final static String ATTRIBUTE_NAME = "name";
    private final static String ATTRIBUTE_SOURCE = "source";
    private final static String TAG_CLASS = "class";
    private final static String TAG_FILE = "file";
    private final static String TAG_TEXT_FIELD = "text-field";

    public static ICAFileParser newInstance(String fileName, String fileId) throws LabelNameException, Exception {

        ICAFileParser instance = null;
        InputStream inp = CAFileParserFactory.class.getResourceAsStream("/" + fileName);
        InputSource is = new InputSource(inp);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(is);

        if (document == null) {
            throw (new LabelNameException("ERROR_PARSING_FILE", fileName));
        }

        NodeList nodes = document.getElementsByTagName(CAFileParserFactory.TAG_FILE);
        String className = null;
        List txtFields = new ArrayList();
        String source = null;
        String codeIsoMonnaie = null;
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String id = node.getAttributes().getNamedItem(CAFileParserFactory.ATTRIBUTE_ID).getNodeValue();
            if ((fileId != null) && (fileId.equals(id))) {

                // // récupération des attribut
                if ((node.getAttributes() != null)
                        && (node.getAttributes().getNamedItem(CAFileParserFactory.ATTRIBUTE_SOURCE) != null)) {
                    source = node.getAttributes().getNamedItem(CAFileParserFactory.ATTRIBUTE_SOURCE).getNodeValue();
                }

                if ((node.getAttributes() != null)
                        && (node.getAttributes().getNamedItem(CAFileParserFactory.ATTRIBUTE_CODE_ISO_MONNAIE) != null)) {
                    codeIsoMonnaie = node.getAttributes().getNamedItem(CAFileParserFactory.ATTRIBUTE_CODE_ISO_MONNAIE)
                            .getNodeValue();
                }

                // classes à instancier...
                NodeList childs = node.getChildNodes();
                for (int c = 0; c < childs.getLength(); c++) {
                    if (childs.item(c).getNodeName().equals(CAFileParserFactory.TAG_CLASS)) {
                        className = childs.item(c).getAttributes().getNamedItem(CAFileParserFactory.ATTRIBUTE_NAME)
                                .getNodeValue();
                    } else if (childs.item(c).getNodeName().equals(CAFileParserFactory.TAG_TEXT_FIELD)) {
                        String fieldName = childs.item(c).getAttributes()
                                .getNamedItem(CAFileParserFactory.ATTRIBUTE_ID).getNodeValue();
                        String beginPos = childs.item(c).getAttributes()
                                .getNamedItem(CAFileParserFactory.ATTRIBUTE_BEGIN_POS).getNodeValue();
                        String endPos = childs.item(c).getAttributes()
                                .getNamedItem(CAFileParserFactory.ATTRIBUTE_END_POS).getNodeValue();

                        txtFields.add(new TextField(fieldName, Integer.valueOf(beginPos).intValue(), Integer.valueOf(
                                endPos).intValue()));
                    }
                }
                break;
            }
        }
        Class c = Class.forName(className);
        instance = (ICAFileParser) c.newInstance();
        instance.setSource(source);
        instance.setCodeIsoMonnaie(codeIsoMonnaie);
        if (instance instanceof ACATextFileParser) {
            for (int i = 0; i < txtFields.size(); i++) {
                TextField txtField = (TextField) txtFields.get(i);
                ((ACATextFileParser) instance).addField(txtField);
            }
        }
        return instance;
    }

    /**
     * Constructor for CAFileParserFactory.
     */
    public CAFileParserFactory() {
        super();
    }
}
