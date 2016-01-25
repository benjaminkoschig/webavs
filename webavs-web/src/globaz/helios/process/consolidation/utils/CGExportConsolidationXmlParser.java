package globaz.helios.process.consolidation.utils;

import globaz.globall.util.JANumberFormatter;
import globaz.helios.db.comptes.CGPeriodeComptable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CGExportConsolidationXmlParser {
    private Document document;
    private Element root;

    /**
     * Constructeur.<b/>Créer le document xml pour l'exportation.
     * 
     * @throws Exception
     */
    public CGExportConsolidationXmlParser() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();

        root = document.createElement(CGConsolidationConstants.TAG_CONSOLIDATION);
        document.appendChild(root);
    }

    /**
     * Ajout de l'attribut no d'agence au parent consolidation du document xml.
     * 
     * @param noAgence
     */
    public void addAttributAgenceToRoot(String noAgence) {
        root.setAttribute(CGConsolidationConstants.ATTRIBUT_AGENCE, noAgence);
    }

    /**
     * Ajout de l'attribut no de caisse au parent consolidation du document xml.
     * 
     * @param noCaisse
     */
    public void addAttributCaisseToRoot(String noCaisse) {
        root.setAttribute(CGConsolidationConstants.ATTRIBUT_CAISSE, noCaisse);
    }

    /**
     * Ajout de l'attrribut date de début au parent consolidation du document xml.
     * 
     * @param dateDebut
     */
    public void addAttributDateDebutToRoot(String dateDebut) {
        root.setAttribute(CGConsolidationConstants.ATTRIBUT_DATEDEBUT, dateDebut);
    }

    /**
     * Ajout de l'attrribut date de fin au parent consolidation du document xml.
     * 
     * @param dateFin
     */
    public void addAttributDateFinToRoot(String dateFin) {
        root.setAttribute(CGConsolidationConstants.ATTRIBUT_DATEFIN, dateFin);
    }

    /**
     * Ajout d'un élément compte et de ces informations au document xml.
     * 
     * @param periode
     * @param idExterne
     * @param libelle
     * @param idGenre
     * @param soldeA
     * @param soldeB
     * @param idNature
     * @param idDomaine
     */
    public void addCompteInformationsToPeriode(Element periode, String idExterne, String libelle, String idGenre,
            String soldeA, String soldeB, String idNature, String idDomaine) {
        Element compte = document.createElement(CGConsolidationConstants.TAG_COMPTE);

        Element idExterneElement = document.createElement(CGConsolidationConstants.TAG_IDEXTERNE);
        idExterneElement.appendChild(document.createTextNode(idExterne));
        compte.appendChild(idExterneElement);

        Element libelleElement = document.createElement(CGConsolidationConstants.TAG_LIBELLE);
        libelleElement.appendChild(document.createTextNode(libelle));
        compte.appendChild(libelleElement);

        Element idGenreElement = document.createElement(CGConsolidationConstants.TAG_IDGENRE);
        idGenreElement.appendChild(document.createTextNode(idGenre));
        compte.appendChild(idGenreElement);

        Element soldeAElement = document.createElement(CGConsolidationConstants.TAG_SOLDEACTIF);
        soldeAElement.appendChild(document.createTextNode(JANumberFormatter.deQuote(soldeA)));
        compte.appendChild(soldeAElement);

        Element soldeBElement = document.createElement(CGConsolidationConstants.TAG_SOLDEPASSIF);
        soldeBElement.appendChild(document.createTextNode(JANumberFormatter.deQuote(soldeB)));
        compte.appendChild(soldeBElement);

        Element idNatureElement = document.createElement(CGConsolidationConstants.TAG_IDNATURE);
        idNatureElement.appendChild(document.createTextNode(idNature));
        compte.appendChild(idNatureElement);

        Element idDomaineElement = document.createElement(CGConsolidationConstants.TAG_IDDOMAINE);
        idDomaineElement.appendChild(document.createTextNode(idDomaine));
        compte.appendChild(idDomaineElement);

        periode.appendChild(compte);
    }

    /**
     * Ajout d'un élément période au document xml.
     * 
     * @param idPeriode
     * @return
     */
    public Element addPeriode(CGPeriodeComptable idPeriode) {
        Element periode = document.createElement(CGConsolidationConstants.TAG_PERIODE);
        periode.setAttribute(CGConsolidationConstants.ATTRIBUT_CODE, idPeriode.getCode());
        periode.setAttribute(CGConsolidationConstants.ATTRIBUT_DATEDEBUT, idPeriode.getDateDebut());
        periode.setAttribute(CGConsolidationConstants.ATTRIBUT_DATEFIN, idPeriode.getDateFin());
        periode.setAttribute(CGConsolidationConstants.ATTRIBUT_IDTYPE, idPeriode.getIdTypePeriode());

        root.appendChild(periode);

        return periode;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
