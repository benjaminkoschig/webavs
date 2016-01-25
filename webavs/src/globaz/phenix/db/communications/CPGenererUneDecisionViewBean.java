/*
 * Créé le 17 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.ServicesFacturation;
import globaz.phenix.application.CPApplication;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPGenererUneDecisionViewBean extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idJournalFacturation = "";
    private String idRetour = "";
    private Boolean impressionListe = Boolean.TRUE;
    private String libellePassage = "";
    private String validationDecision = "NOK";

    public void _afterRetrieve(BTransaction transaction) throws Exception {
        // --- Initialise le viewBean
        if (((CPApplication) getSession().getApplication()).isValidationDecision()) {
            setValidationDecision("OK");
            // Recherche libellé passage
            FAPassage passage = new FAPassage();
            passage.setSession(getSession());
            passage.setIdJournal(getIdJournalFacturation());
            passage.retrieve();
            if ((passage != null) && !passage.isNew()) {
                setLibellePassage(passage.getLibelle());
            } else {
                setLibellePassage("");
            }
        }
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdJournalFacturation() {
        return idJournalFacturation;
    }

    public String getIdRetour() {
        return idRetour;
    }

    public Boolean getImpressionListe() {
        return impressionListe;
    }

    /**
     * Returns the libellePassage.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibellePassage() {
        return libellePassage;
    }

    /**
     * @return
     */
    public String getValidationDecision() {
        return validationDecision;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void rechercheProchainJournalFacturation() {
        try {
            // Recherche du prochain id passage à facturer
            IFAPassage passage = null;
            // Recherche si séparation indépendant et non-actif - Inforom 314s
            Boolean isSeprationIndNac = false;
            try {
                isSeprationIndNac = new Boolean(GlobazSystem.getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA)
                        .getProperty(FAApplication.SEPARATION_IND_NA));
            } catch (Exception e) {
                isSeprationIndNac = Boolean.FALSE;
            }
            if (isSeprationIndNac) {
                passage = ServicesFacturation.getProchainPassageFacturation(getSession(), getTransaction(),
                        FAModuleFacturation.CS_MODULE_COT_PERS_IND + ", " + FAModuleFacturation.CS_MODULE_COT_PERS_NAC);
            } else {
                passage = ServicesFacturation.getProchainPassageFacturation(getSession(), getTransaction(),
                        globaz.musca.db.facturation.FAModuleFacturation.CS_MODULE_COT_PERS);
            }
            if (passage != null) {
                // setIdJournalFacturation(passage.getIdPassage());
                if (passage.getLibelle().length() > 0) {
                    // setIdJournalFacturation(passage.getIdPassage());
                    setLibellePassage(passage.getLibelle());
                }
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
    }

    public void setIdJournalFacturation(String idJournalFacturation) {
        this.idJournalFacturation = idJournalFacturation;
    }

    public void setIdRetour(String idRetour) {
        this.idRetour = idRetour;
    }

    public void setImpressionListe(Boolean impressionListe) {
        this.impressionListe = impressionListe;
    }

    /**
     * Sets the libellePassage.
     * 
     * @param libellePassage
     *            The libellePassage to set
     */
    public void setLibellePassage(java.lang.String libellePassage) {
        this.libellePassage = libellePassage;
    }

    /**
     * @param b
     */
    public void setValidationDecision(String b) {
        validationDecision = b;
    }

}
