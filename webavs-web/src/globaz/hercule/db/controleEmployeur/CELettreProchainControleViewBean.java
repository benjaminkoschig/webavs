package globaz.hercule.db.controleEmployeur;

import globaz.babel.api.ICTDocument;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.hercule.process.CELettreProchainControleProcess;
import globaz.hercule.service.CEAffiliationService;
import globaz.hercule.utils.CodeSystem;
import java.util.Vector;

/**
 * Permet de controler les valeurs entrées par l'utilisateur
 * 
 * @author sda
 * @creation Créé le 25 avr. 05
 */
public class CELettreProchainControleViewBean extends CELettreProchainControleProcess implements FWViewBeanInterface {

    private static final long serialVersionUID = -2338697621672838900L;
    public String idAffiliation;
    private String langueDoc;
    public String selectedId;

    /**
     * Constructeur de CELettreProchainControleViewBean
     */
    public CELettreProchainControleViewBean() throws java.lang.Exception {
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

    public String getIdAffiliation() {
        return idAffiliation;
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

    // *******************************************************
    // Getter
    // ***************************************************

    public String getSelectedId() {
        return selectedId;
    }

    /**
     * Retourne les documents possible pour l'impression des plans.
     * 
     * @author sel, 06.07.2007
     * @return le vecteur renseignant la liste déroulante des modèles de GCA60005.
     * @throws Exception
     */
    public Vector<String[]> returnDocumentsPossible() throws Exception {
        // Vector pour FWListSelectTag data
        Vector<String[]> v = new Vector<String[]>();

        ICTDocument[] candidats = null;
        ICTDocument loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        loader.setActif(Boolean.TRUE);
        loader.setCodeIsoLangue(getLangue());
        loader.setCsDomaine(CodeSystem.DOMAINE_CONT_EMPL);
        loader.setCsTypeDocument(CodeSystem.TYPE_LETTRE_PROCHAIN_CONTROLE);
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
                setIdDocumentDefaut(candidats[i].getIdDocument());
                v.add(line);
            }
        }

        ICTDocument loader2 = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        loader2.setActif(Boolean.TRUE);
        loader2.setCodeIsoLangue(getLangue());
        loader2.setCsDomaine(CodeSystem.DOMAINE_CONT_EMPL);
        loader2.setCsTypeDocument(CodeSystem.TYPE_LETTRE_PROCHAIN_CONTROLE);
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

    // *******************************************************
    // Setter
    // ***************************************************

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }
}
