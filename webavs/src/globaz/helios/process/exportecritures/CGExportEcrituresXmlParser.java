package globaz.helios.process.exportecritures;

import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class permettant la création d'un xml.</br> Chaque element est une écriture et il se trouve sous la forme suivante :
 * < importEcritures ></br> <double id="1"></br> <date value="28.09.2009"/></br> <libelle
 * value="Exemple de libellé"/></br> <idexternecomptedebite value="9100.5010.1000"/></br> <idexternecomptecredite
 * value="1000.1011.1000"/></br> <montant value="50.00"/></br> </double></br> </importEcritures></br> Un flag
 * "verificationIntegriteDonnes" permet de s'assurer de la validité d'une écriture,</br> une information est ajoutée
 * dans les logs passés en parametre mais l'ecriture est ajouté quand meme.</br> Le test se porte sur </br> - une date
 * valide, </br> - la présence et la validité des numeros de compte
 * 
 * @author SCO 30 mars 2010
 */
public class CGExportEcrituresXmlParser {

    private int compteurEcritures;

    private Document document;
    private Element root;
    private BSession session;

    private boolean verificationIntegriteDonnees;

    /**
     * Constructeur.<b/> Créer le document xml pour l'exportation.
     * 
     * @throws Exception
     */
    public CGExportEcrituresXmlParser() throws Exception {
        setCompteurEcritures(0);
        setVerificationIntegriteDonnees(false);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();

        root = document.createElement(CGExportEcrituresConstants.TAG_EXPORT_ECRITURE);
        document.appendChild(root);
    }

    /**
     * Ajoute une éciture double dans le fichier XML
     * 
     * @param idOperation
     *            Id de l'operation qui va etre ajoutée
     * @param date
     *            Format de date = jj.mm.aaaa
     * @param libelle
     * @param idExterneCompteDebite
     * @param idExterneCompteCredite
     * @param montant
     * @param log
     */
    public void addEcritureDoubleToXml(String idOperation, String date, String libelle, String idExterneCompteDebite,
            String idExterneCompteCredite, String montant, String piece, FWMemoryLog log) {

        // Vérification des données
        valideDonnees(idOperation, date, idExterneCompteDebite, idExterneCompteCredite, log);

        // Incrémentation du compteur
        incrementeCompteur();

        // Création de l'ecriture
        Element ecriture = document.createElement(CGExportEcrituresConstants.TAG_DOUBLE);
        ecriture.setAttribute(CGExportEcrituresConstants.ATTRIBUT_ID, Integer.toString(getCompteurEcritures()));

        Element dateElement = document.createElement(CGExportEcrituresConstants.TAG_DATE);
        dateElement.setAttribute(CGExportEcrituresConstants.ATTRIBUT_VALUE, date);
        ecriture.appendChild(dateElement);

        Element libelleElement = document.createElement(CGExportEcrituresConstants.TAG_LIBELLE);
        libelleElement.setAttribute(CGExportEcrituresConstants.ATTRIBUT_VALUE, libelle);
        ecriture.appendChild(libelleElement);

        Element compteDebiteElement = document.createElement(CGExportEcrituresConstants.TAG_IDEXTERNECOMPTEDEBITE);
        compteDebiteElement.setAttribute(CGExportEcrituresConstants.ATTRIBUT_VALUE, idExterneCompteDebite);
        ecriture.appendChild(compteDebiteElement);

        Element compteCrediteElement = document.createElement(CGExportEcrituresConstants.TAG_IDEXTERNECOMPTECREDITE);
        compteCrediteElement.setAttribute(CGExportEcrituresConstants.ATTRIBUT_VALUE, idExterneCompteCredite);
        ecriture.appendChild(compteCrediteElement);

        Element montantElement = document.createElement(CGExportEcrituresConstants.TAG_MONTANT);
        montantElement.setAttribute(CGExportEcrituresConstants.ATTRIBUT_VALUE, montant);
        ecriture.appendChild(montantElement);

        Element pieceComptableElement = document.createElement(CGExportEcrituresConstants.TAG_PIECE);
        pieceComptableElement.setAttribute(CGExportEcrituresConstants.ATTRIBUT_VALUE, piece);
        ecriture.appendChild(pieceComptableElement);

        // Ajout de l'écriture dans l'xml
        getRoot().appendChild(ecriture);
    }

    private int getCompteurEcritures() {
        return compteurEcritures;
    }

    public Document getDocument() {
        return document;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    private Element getRoot() {
        return root;
    }

    public BSession getSession() {
        return session;
    }

    /**
     * Incremente le compteur
     */
    private void incrementeCompteur() {
        compteurEcritures++;
    }

    boolean isVerificationIntegriteDonnees() {
        return verificationIntegriteDonnees;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    private void setCompteurEcritures(int compteurEcritures) {
        this.compteurEcritures = compteurEcritures;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setVerificationIntegriteDonnees(boolean verificationIntegriteDonnees) {
        this.verificationIntegriteDonnees = verificationIntegriteDonnees;
    }

    /**
     * Valide les informations d'une écriture et ajoute une information dans les log suivant les tests.</br> - test de
     * la date </br> - test des numeros de compte</br>
     * 
     * @param idOperation
     * @param date
     * @param idExterneCompteDebite
     * @param idExterneCompteCredite
     */
    private void valideDonnees(String idOperation, String date, String idExterneCompteDebite,
            String idExterneCompteCredite, FWMemoryLog log) {
        // TODO SCO
        // - id des comtpes présents
        if (isVerificationIntegriteDonnees() && (log != null)) {

            // Vérification de la date
            if (!JadeDateUtil.isGlobazDate(date)) {
                log.logMessage(getSession().getLabel("EXPORTATION_VAL_DATE") + " => " + date + " [id in Xml = "
                        + getCompteurEcritures() + ", idOperation = " + idOperation + "]", FWMessage.AVERTISSEMENT,
                        this.getClass().getName());
            }
        }
    }

}
