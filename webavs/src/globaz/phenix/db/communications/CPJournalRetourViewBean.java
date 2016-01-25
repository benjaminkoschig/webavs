/*
 * Créé le 17 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWController;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.ServicesFacturation;
import globaz.phenix.application.CPApplication;
import java.util.Hashtable;
import javax.servlet.http.HttpSession;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPJournalRetourViewBean extends CPJournalRetour implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String defaultMenu = "Principale-journalRetour-default";
    private static Hashtable<String, String> menuTable = new Hashtable<String, String>();

    /**
     * Renvoie le nom du menu a appelé en fonction de l'état du journal
     * 
     * @return
     */
    static public String getMenuName(String status) {
        if (CPJournalRetourViewBean.menuTable.isEmpty()) {
            CPJournalRetourViewBean.menuTable.put(globaz.phenix.db.communications.CPJournalRetour.CS_RECEPTION_PARTIEL,
                    "Principale-journalRetour-receptionPartiel");
            CPJournalRetourViewBean.menuTable.put(globaz.phenix.db.communications.CPJournalRetour.CS_RECEPTION_TOTAL,
                    "Principale-journalRetour-receptionTotal");
            CPJournalRetourViewBean.menuTable.put(globaz.phenix.db.communications.CPJournalRetour.CS_GENERE_PARTIEL,
                    "Principale-journalRetour-default");
            CPJournalRetourViewBean.menuTable.put(globaz.phenix.db.communications.CPJournalRetour.CS_GENERE_TOTAL,
                    "Principale-journalRetour-default");
            CPJournalRetourViewBean.menuTable.put(
                    globaz.phenix.db.communications.CPJournalRetour.CS_COMPTABILISE_PARTIEL,
                    "Principale-journalRetour-default");
            CPJournalRetourViewBean.menuTable.put(
                    globaz.phenix.db.communications.CPJournalRetour.CS_COMPTABILISE_TOTAL,
                    "Principale-journalRetour-default");
            CPJournalRetourViewBean.menuTable.put(globaz.phenix.db.communications.CPJournalRetour.CS_ABANDONNE,
                    "Principale-journalRetour-default");
        }
        String dynamicMenu = CPJournalRetourViewBean.menuTable.get(status);
        if (dynamicMenu == null) {
            return CPJournalRetourViewBean.defaultMenu;
        } else {
            return dynamicMenu;
        }

    }

    private Boolean afficheComptabiliseTotal = new Boolean(false);
    private String dateFichier = "";
    private String eMailAddress = "";
    private String forGenreAffilie = "";
    private String forIdPlausibilite = "";
    private String fromNumAffilie = "";
    private String impression = "";

    private String libellePassage = "";

    private String listeExcel = "";
    private String[] listIdJournalRetour = null;
    private String orderBy = "";
    private String receptionFileName = "";
    private String simulation = "";
    private String tillNumAffilie = "";
    private String typeReception = "";
    private String validationDecision = "NOK";
    private Boolean wantDetail = Boolean.FALSE;

    @Override
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

    public Boolean getAfficheComptabiliseTotal() {
        return afficheComptabiliseTotal;
    }

    public String getDateFichier() {
        return dateFichier;
    }

    public String getDescription() {
        return getIdJournalRetour() + " - " + getLibelleJournal();

    }

    /**
     * @return
     */
    public java.lang.String getEMailAddress() {
        return eMailAddress;
    }

    public String getForGenreAffilie() {
        return forGenreAffilie;
    }

    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

    /**
     * @return
     */
    public java.lang.String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String getImpression() {
        return impression;
    }

    public void getJournalRetour(String idJournal, HttpSession session) {
        FWController controller = (FWController) session.getAttribute("objController");
        BISession bSession = controller.getSession();
        setISession(bSession);
        setIdJournalRetour(idJournal);
        try {
            this.retrieve();
        } catch (Exception e) {
        }
    }

    /**
     * Returns the libellePassage.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibellePassage() {
        return libellePassage;
    }

    public String getListeExcel() {
        return listeExcel;
    }

    public String[] getListIdJournalRetour() {
        return listIdJournalRetour;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getReceptionFileName() {
        return receptionFileName;
    }

    public String getSimulation() {
        return simulation;
    }

    /**
     * @return
     */
    public java.lang.String getTillNumAffilie() {
        return tillNumAffilie;
    }

    public String getTypeReception() {
        return typeReception;
    }

    /**
     * @return
     */
    public String getValidationDecision() {
        return validationDecision;
    }

    public Boolean getWantDetail() {
        return wantDetail;
    }

    public boolean isSuccess() {
        return getSucces().booleanValue();
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
                passage = ServicesFacturation.getProchainPassageFacturation(getSession(), getSession()
                        .getCurrentThreadTransaction(), FAModuleFacturation.CS_MODULE_COT_PERS_IND + ", "
                        + FAModuleFacturation.CS_MODULE_COT_PERS_NAC);
            } else {
                passage = ServicesFacturation.getProchainPassageFacturation(getSession(), getSession()
                        .getCurrentThreadTransaction(), FAModuleFacturation.CS_MODULE_COT_PERS);
            }
            if (passage != null) {
                setIdJournalFacturation(passage.getIdPassage());
                if (passage.getLibelle().length() > 0) {
                    setIdJournalFacturation(passage.getIdPassage());
                }
                setLibellePassage(passage.getLibelle());
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
    }

    public void setAfficheComptabiliseTotal(Boolean afficheComptabiliseTotal) {
        this.afficheComptabiliseTotal = afficheComptabiliseTotal;
    }

    public void setDateFichier(String dateFichier) {
        this.dateFichier = dateFichier;
    }

    /**
     * @param string
     */
    public void setEMailAddress(java.lang.String string) {
        eMailAddress = string;
    }

    public void setForGenreAffilie(String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    public void setForIdPlausibilite(String forIdPlausibilite) {
        this.forIdPlausibilite = forIdPlausibilite;
    }

    /**
     * @param string
     */
    public void setFromNumAffilie(java.lang.String string) {
        fromNumAffilie = string;
    }

    public void setImpression(String impression) {
        this.impression = impression;
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

    public void setListeExcel(String listeExcel) {
        this.listeExcel = listeExcel;
    }

    public void setListIdJournalRetour(String[] listeIdJournalRetour) {
        listIdJournalRetour = listeIdJournalRetour;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setReceptionFileName(String receptionFileName) {
        this.receptionFileName = receptionFileName;
    }

    public void setSimulation(String simulation) {
        this.simulation = simulation;
    }

    /**
     * @param string
     */
    public void setTillNumAffilie(java.lang.String string) {
        tillNumAffilie = string;
    }

    public void setTypeReception(String typeReception) {
        this.typeReception = typeReception;
    }

    /**
     * @param b
     */
    public void setValidationDecision(String b) {
        validationDecision = b;
    }

    public void setWantDetail(Boolean wantDetail) {
        this.wantDetail = wantDetail;
    }

    /**
     * Permet de savoir si la caisse en question est la CCCVS
     * DDS : S141124_011
     * 
     * @return true si la caisse est la CCCVS, false sinon
     */
    public boolean isCaisseCCCVS() {

        try {
            CPApplication app = (CPApplication) GlobazServer.getCurrentSystem().getApplication(
                    CPApplication.DEFAULT_APPLICATION_PHENIX);

            return app.isCaisseCCCVS();
        } catch (Exception e) {
            JadeLogger.error(this, "Unabled to retrieve the propertie 'common.noCaisseFormate' " + e.getMessage());
        }

        return false;
    }

}
