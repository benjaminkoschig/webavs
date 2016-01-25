package globaz.hercule.db.controleEmployeur;

import globaz.babel.api.ICTDocument;
import globaz.hercule.db.CEAbstractViewBean;
import globaz.hercule.itext.controleEmployeur.CEDSLettre;
import globaz.hercule.service.CEAffiliationService;
import globaz.naos.translation.CodeSystem;
import java.util.Vector;

/**
 * @author SCO
 * @since 13 oct. 2010
 */
public class CELettreLibreCatalogueViewBean extends CEAbstractViewBean {

    public String dateDebutControle;
    public String dateEffective;
    public String dateEnvoi;
    public String dateFinControle;
    public String idAffiliation;
    public String idDocument;
    public String idDocumentDefault;
    public String idTiers;
    private String langueDoc;
    public String numAffilie;
    public String selectedId;

    public String visaReviseur;

    /**
     * Constructeur de CELettreLibreCatalogueViewBean
     */
    public CELettreLibreCatalogueViewBean() {
        super();
    }

    /**
     * Permet la récupération de l'information d'un affilie (Nom et date d'affiliation)
     * 
     * @param idAffiliation
     * @return
     */
    public String _getInfoTiers(String idAffiliation) {
        return CEAffiliationService.getNomEtDateAffiliation(getSession(), idAffiliation);
    }

    public String getDateDebutControle() {
        return dateDebutControle;
    }

    public String getDateEffective() {
        return dateEffective;
    }

    // *******************************************************
    // Getter
    // ***************************************************

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getDateFinControle() {
        return dateFinControle;
    }

    /**
     * Création de la liste des documents possible pour la lettre libre par catalogue
     * 
     * @return
     * @throws Exception
     */
    public Vector<String[]> getDocumentsPossible() throws Exception {
        // Vector pour FWListSelectTag data
        Vector<String[]> v = new Vector<String[]>();

        ICTDocument[] candidats = null;
        ICTDocument loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        loader.setActif(Boolean.TRUE);
        loader.setCodeIsoLangue(getLangue());
        loader.setCsDomaine(CodeSystem.DOMAINE_CONT_EMPL);
        loader.setCsTypeDocument(CEDSLettre.CE_DOCUMENT_LETTRE_LIBRE_CATALOGUE);
        loader.setCsDestinataire(ICTDocument.CS_EMPLOYEUR);
        loader.setDefault(new Boolean(true));
        // Charge les documents correspondant aux critères définis
        candidats = loader.load();
        if (candidats != null) {
            String line[];
            for (int i = 0; i < candidats.length; i++) {
                line = new String[2];
                line[0] = candidats[i].getIdDocument();
                line[1] = candidats[i].getNom();
                setIdDocumentDefault(candidats[i].getIdDocument());
                v.add(line);
            }
        }

        ICTDocument loader2 = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        loader2.setActif(Boolean.TRUE);
        loader2.setCodeIsoLangue(getLangue());
        loader2.setCsDomaine(CodeSystem.DOMAINE_CONT_EMPL);
        loader2.setCsTypeDocument(CEDSLettre.CE_DOCUMENT_LETTRE_LIBRE);
        loader2.setCsDestinataire(ICTDocument.CS_EMPLOYEUR);
        loader2.setDefault(new Boolean(false));
        // Charge les documents correspondant aux critères définis
        candidats = loader2.load();

        if (candidats != null) {
            String line2[];
            for (int i = 0; i < candidats.length; i++) {
                line2 = new String[2];
                line2[0] = candidats[i].getIdDocument();
                line2[1] = candidats[i].getNom();
                v.add(line2);
            }
        }
        return v;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public String getIdDocumentDefault() {
        return idDocumentDefault;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Retourne le code iso de la langue pour ce document.
     * 
     * @return la valeur courante de l'attribut langue
     */
    protected String getLangue() {
        if (langueDoc == null) {
            langueDoc = getSession().getIdLangueISO();
        }

        return langueDoc;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public String getVisaReviseur() {
        return visaReviseur;
    }

    // *******************************************************
    // Setter
    // ***************************************************

    public void setDateDebutControle(String dateDebutControle) {
        this.dateDebutControle = dateDebutControle;
    }

    public void setDateEffective(String dateEffective) {
        this.dateEffective = dateEffective;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public void setDateFinControle(String dateFinControle) {
        this.dateFinControle = dateFinControle;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setIdDocumentDefault(String idDocumentDefault) {
        this.idDocumentDefault = idDocumentDefault;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public void setVisaReviseur(String visaReviseur) {
        this.visaReviseur = visaReviseur;
    }

}
