package globaz.tucana.statistiques.config;

import globaz.tucana.exception.process.TUInitStatistiquesConfigException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.LocatorImpl;

public class TUStatistiquesConfigSaxHandler implements ContentHandler {
    private TUCategorieRubriqueStatistiqueConfig currentRubriqueStatistiqueConfig = null;

    private TUStatistiqueConfig currentStatistiqueConfig = null;

    private Locator locator = null;

    private TUStatistiquesConfigProvider provider = null;

    public TUStatistiquesConfigSaxHandler(TUStatistiquesConfigProvider _provider) {
        super();
        provider = _provider;
        locator = new LocatorImpl();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // System.out.println("[INFO] characteres : " + new String(ch, start,
        // length));
    }

    @Override
    public void endDocument() throws SAXException {
        // System.out.println("[INFO] endDocument");
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (ITUStatistiquesXmlTags.CATEGORIE_RUBRIQUE_NODE.equals(localName)) {
            currentStatistiqueConfig.registerCategorieRubriqueStatistique(currentRubriqueStatistiqueConfig.getId(),
                    currentRubriqueStatistiqueConfig);
        } else if (ITUStatistiquesXmlTags.STATISTIQUE_NODE.equals(localName)) {
            provider.registerStatistique(currentStatistiqueConfig.getId(), currentStatistiqueConfig);
        }
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        // System.out.println("[INFO] endPrefixMapping : " + prefix);
    }

    public Locator getLocator() {
        return locator;
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        // System.out.println("[INFO] ignorableWhitespace : " + new String(ch,
        // start, length));
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        // System.out.println("[INFO] processingInstruction");
        // System.out.println("	-> target : " + target);
        // System.out.println("	-> data : " + data);
    }

    private void processStartCategorieRubrique(Attributes atts) {
        String id = null;
        String nom = null;
        String signe = null;
        boolean affiche = true;
        String order = null;
        String group = null;
        for (int i = 0; i < atts.getLength(); i++) {
            if (ITUStatistiquesXmlTags.ID_ATTRIBUTE.equals(atts.getLocalName(i))) {
                id = atts.getValue(i);
            } else if (ITUStatistiquesXmlTags.NOM_ATTRIBUTE.equals(atts.getLocalName(i))) {
                nom = atts.getValue(i);
            } else if (ITUStatistiquesXmlTags.SIGNE_ATTRIBUTE.equals(atts.getLocalName(i))) {
                signe = atts.getValue(i);
            } else if (ITUStatistiquesXmlTags.AFFICHE_ATTRIBUTE.equals(atts.getLocalName(i))) {
                affiche = new Boolean(atts.getValue(i)).booleanValue();
            } else if (ITUStatistiquesXmlTags.ORDER_ATTRIBUTE.equals(atts.getLocalName(i))) {
                order = atts.getValue(i);
            } else if (ITUStatistiquesXmlTags.GROUP_ATTRIBUTE.equals(atts.getLocalName(i))) {
                group = atts.getValue(i);
            }
        }
        currentRubriqueStatistiqueConfig = new TUCategorieRubriqueStatistiqueConfig(id, false, nom, signe, affiche,
                order, group);
    }

    private void processStartStatistique(Attributes atts) {
        String id = null;
        boolean csLabel = false;
        for (int i = 0; i < atts.getLength(); i++) {
            if (ITUStatistiquesXmlTags.ID_ATTRIBUTE.equals(atts.getLocalName(i))) {
                id = atts.getValue(i);
            } else if (ITUStatistiquesXmlTags.CS_LABEL_ATTRIBUTE.equals(atts.getLocalName(i))) {
                csLabel = new Boolean(atts.getValue(i)).booleanValue();
            }
        }
        currentStatistiqueConfig = new TUStatistiqueConfig(id, csLabel);
    }

    private void processStartStatistiques(Attributes atts) {
        currentRubriqueStatistiqueConfig = null;
        currentStatistiqueConfig = null;
    }

    @Override
    public void setDocumentLocator(Locator _locator) {
        locator = _locator;
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        // System.out.println("[INFO] skippedEntity : " + name);
    }

    @Override
    public void startDocument() throws SAXException {
        // System.out.println("[INFO] startDocument");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (ITUStatistiquesXmlTags.CATEGORIE_RUBRIQUE_NODE.equals(localName)) {
            processStartCategorieRubrique(atts);
        } else if (ITUStatistiquesXmlTags.STATISTIQUE_NODE.equals(localName)) {
            processStartStatistique(atts);
        } else if (ITUStatistiquesXmlTags.STATISTIQUES_NODE.equals(localName)) {
            processStartStatistiques(atts);
        } else {
            throw new SAXException(new TUInitStatistiquesConfigException("Invalid node name : " + localName));
        }
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        // System.out.println("[INFO] startPrefixMapping : ");
        // System.out.println("	-> prefix : " + prefix);
        // System.out.println("	-> uri : " + uri);
    }
}
