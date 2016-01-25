package globaz.naos.db.affiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.IDEServiceCallUtil;
import java.util.List;

public class AFAffiliationViewBean extends AFAffiliation implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private String message;
    private String msgType;
    private String ideRaisonSocialeFromWebServcice = "";

    private Boolean noAVSChange = Boolean.FALSE;
    private Boolean nomChange = Boolean.FALSE;

    private boolean isIdeReadOnly = false;
    private boolean isInMutationOnly = false;

    private boolean isMessageAnnonceIdeCreationAjouteeToDisplay = false;

    public boolean isMessageAnnonceIdeCreationAjouteeToDisplay() {
        return isMessageAnnonceIdeCreationAjouteeToDisplay;
    }

    // commence par souligné afin de ne pas être prise par jspSetBeanProperties
    public void _setMessageAnnonceIdeCreationAjouteeToDisplay(boolean isMessageAnnonceIdeCreationAjouteeToDisplay) {
        this.isMessageAnnonceIdeCreationAjouteeToDisplay = isMessageAnnonceIdeCreationAjouteeToDisplay;
    }

    public boolean isInMutationOnly() {
        return isInMutationOnly;
    }

    /**
     * commence par '_' (souligné) afin de ne pas être prise par jspSetBeanProperties
     */
    public void _setInMutationOnly(boolean isMutationOnly) {
        isInMutationOnly = isMutationOnly;
    }

    public boolean isIdeReadOnly() {
        return isIdeReadOnly;
    }

    // commence par souligné afin de ne pas être prise par jspSetBeanProperties
    public void _setIdeReadOnly(boolean isIdeReadOnly) {
        this.isIdeReadOnly = isIdeReadOnly;
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);
        int nbAnnonce = AFIDEUtil.countAffiliationAnnonceEnCours(getSession(), getAffiliationId());
        boolean hasAffiliationAnnonceLieeEnCours = AFIDEUtil.hasAffiliationAnnonceLieeEnCours(getSession(),
                getAffiliationId());

        isInMutationOnly = false;
        isIdeReadOnly = false;
        if (nbAnnonce >= 1 || hasAffiliationAnnonceLieeEnCours) {
            if (nbAnnonce == 1
                    && AFIDEUtil.hasAnnonceEnCours(getSession(), getAffiliationId(),
                            CodeSystem.TYPE_ANNONCE_IDE_MUTATION, CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI)
                    && !hasAffiliationAnnonceLieeEnCours) {
                isInMutationOnly = true;
            } else {
                isIdeReadOnly = true;
            }
        }
    }

    /**
     * Commentaire relatif au constructeur AFAffiliationViewBean.
     */
    public AFAffiliationViewBean() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    public String getDebutAffiliationReelle() {
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
            return null;
        }
        AFParticulariteAffiliationManager particulariteManager = new AFParticulariteAffiliationManager();
        particulariteManager.setForAffiliationId(getAffiliationId());
        particulariteManager.setForParticularite(CodeSystem.PARTIC_AFFILIE_PERIODE_AFFILIATION);
        particulariteManager.setSession(getSession());
        try {
            particulariteManager.find();
            if (!particulariteManager.isEmpty()) {
                // une date existe, retourner cette inforamtion
                return ((AFParticulariteAffiliation) particulariteManager.getFirstEntity()).getDateDebut();
            }
        } catch (Exception ex) {
            // exception, retourner null
            return null;
        }
        return null;
    }

    public String initInfoIDEPourRechercheTrouvee(BSession session, String numeroIDE) throws Exception {
        List<IDEDataBean> rechercheIDE = IDEServiceCallUtil.searchForNumeroIDE(numeroIDE, session);
        if (rechercheIDE.size() == 1) {
            String codeSystemStatutIde = rechercheIDE.get(0).getStatut();
            return rechercheIDE.get(0).getNumeroIDE() + "@" + rechercheIDE.get(0).getRaisonSociale() + '@'
                    + codeSystemStatutIde + '@' + CodeSystem.getLibelle(session, codeSystemStatutIde) + '@';
        }
        return null;
    }

    /**
     * @return
     */
    public Boolean getNoAVSChange() {
        return noAVSChange;
    }

    /**
     * @return
     */
    public Boolean getNomChange() {
        return nomChange;
    }

    /**
     * Retourne le libellé de la raison sociale fourni par le webService IDE
     * 
     * @return
     */
    public String getIdeRaisonSocialeFromWebServcice() {
        return ideRaisonSocialeFromWebServcice;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @param boolean1
     */
    public void setNoAVSChange(Boolean boolean1) {
        noAVSChange = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setNomChange(Boolean boolean1) {
        nomChange = boolean1;
    }

    /**
     * Permet de setter la raison sociale fourni par le webservice
     * Utilis en retour de selection deu webservice IDE
     * 
     * @param raisonSocialeFromWebServcice
     */
    public void setIdeRaisonSocialeFromWebServcice(String raisonSocialeFromWebServcice) {
        ideRaisonSocialeFromWebServcice = raisonSocialeFromWebServcice;
    }

}
