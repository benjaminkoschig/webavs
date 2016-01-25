package globaz.naos.process.fichierCentral;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Classe permettant de gérer une structure XML des avis de mutations du fichier central en les différentes caisses
 * cantonales
 * 
 * @author DGI
 * 
 */
public class AFExportFichierCentralXmlParser {
    public static final String ATTRIBUT_CAISSE = "caisse";
    public static final String TAG_ADRESSEDOM_AVIS = "snc";
    public static final String TAG_AVIS = "avisMutation";
    public static final String TAG_COLLABORATEUR_AVIS = "collaborateur";
    public static final String TAG_DATE_AVIS = "dateAvis";
    // constantes
    public static final String TAG_FICHIER_CENTRAL = "fichierCentral";
    public static final String TAG_OBSERVATIONS_AVIS = "observations";
    public static final String TAG_SNC_AVIS = "snc";
    // public static final String ATTRIBUT_CODE_AVIS = "code";
    // variables
    private Document document;
    private Element root;

    /**
     * Constructeur. Créer le document xml pour l'exportation.
     * 
     * @param caisse
     *            la caisse émettrice
     * @throws Exception
     */
    public AFExportFichierCentralXmlParser(String caisse) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();

        root = document.createElement(AFExportFichierCentralXmlParser.TAG_FICHIER_CENTRAL);
        root.setAttribute(AFExportFichierCentralXmlParser.ATTRIBUT_CAISSE, caisse);
        document.appendChild(root);
    }

    /**
     * Ajout de l'adresse de domicile (sans x4, dans observation dans l'avis imprimé)
     * 
     * @param adresse
     *            l'adresse de domicile
     */
    public void addAdresseDomicile(Element avis, String adresse) {
        addTextNodeAvis(avis, AFExportFichierCentralXmlParser.TAG_ADRESSEDOM_AVIS, adresse);
    }

    /**
     * Ajout d'un avis de mutation.
     * 
     * @param caisse
     *            la caisse à laquelle l'avis est adressé
     * @param date
     *            la date de l'avis
     * @param collaborateur
     *            le nom du collaborateur
     * @return
     */
    public Element addAvis(String caisse, String date, String collaborateur) {
        Element avis = document.createElement(AFExportFichierCentralXmlParser.TAG_AVIS);
        avis.setAttribute(AFExportFichierCentralXmlParser.ATTRIBUT_CAISSE, caisse);
        // date de l'avis
        Element dateAvis = document.createElement(AFExportFichierCentralXmlParser.TAG_DATE_AVIS);
        dateAvis.appendChild(document.createTextNode(date));
        avis.appendChild(dateAvis);
        // collaborateur
        Element collabo = document.createElement(AFExportFichierCentralXmlParser.TAG_COLLABORATEUR_AVIS);
        collabo.appendChild(document.createTextNode(collaborateur));
        avis.appendChild(collabo);
        // liaision
        root.appendChild(avis);
        return avis;
    }

    /**
     * Ajout d'observations de l'avis
     * 
     * @param observations
     *            les observations
     */
    public void addObservations(Element avis, String observations) {
        addTextNodeAvis(avis, AFExportFichierCentralXmlParser.TAG_OBSERVATIONS_AVIS, observations);
    }

    /**
     * Ajout d'observations de l'avis
     * 
     * @param infoSNC
     *            le nom de la SNC
     */
    public void addSNC(Element avis, String infoSNC) {
        addTextNodeAvis(avis, AFExportFichierCentralXmlParser.TAG_SNC_AVIS, infoSNC);
    }

    /**
     * Ajout d'un noeud content que du texte
     * 
     * @param avis
     *            l'élément avis
     * @param tag
     *            le nom du noeud
     * @param texte
     *            le texte concerné
     */
    private void addTextNodeAvis(Element avis, String tag, String texte) {
        Element elem = document.createElement(tag);
        elem.appendChild(document.createTextNode(texte));
        avis.appendChild(elem);
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
