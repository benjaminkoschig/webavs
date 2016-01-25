package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.GlobazSystem;
import globaz.globall.util.JACalendar;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.external.ServicesFacturation;
import globaz.phenix.process.documentsItext.CPProcessImprimerDecisionAgence;

/**
 * Insérez la description du type ici. Date de création : (21.01.2003 08:24:32)
 * 
 * @author: David Girardin
 */
public class CPDecisionImprimerLotViewBean extends CPProcessImprimerDecisionAgence implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idPassage = "";
    private String isShortProcess = "true";
    private java.lang.String libellePassage = "";

    public void _init() {
        try {
            globaz.musca.api.IFAPassage passage = null;
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
                setIdPassage(passage.getIdPassage());
                setLibellePassage(passage.getLibelle());
                setDateImpression(passage.getDateFacturation());
            } else {
                setIdPassage("");
                setLibellePassage("");
                setDateImpression(JACalendar.todayJJsMMsAAAA());
            }
        } catch (Exception e) {
            setIdPassage("");
            setLibellePassage("");
        }
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        // init le job queue du process
        super.setIsShortProcess(Boolean.valueOf(getIsShortProcess()).booleanValue());
        super._validate();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.05.2003 13:53:53)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdPassage() {
        return idPassage;
    }

    /**
     * Returns the isShortProcess.
     * 
     * @return String
     */
    public String getIsShortProcess() {
        return isShortProcess;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.05.2003 14:50:01)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibellePassage() {
        return libellePassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.05.2003 13:53:53)
     * 
     * @param newIdPassage
     *            java.lang.String
     */
    @Override
    public void setIdPassage(java.lang.String newIdPassage) {
        idPassage = newIdPassage;
    }

    /**
     * Sets the isShortProcess.
     * 
     * @param isShortProcess
     *            The isShortProcess to set
     */
    public void setIsShortProcess(String isShortProcess) {
        this.isShortProcess = isShortProcess;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.05.2003 14:50:01)
     * 
     * @param newLibellePassage
     *            java.lang.String
     */
    public void setLibellePassage(java.lang.String newLibellePassage) {
        libellePassage = newLibellePassage;
    }

}
