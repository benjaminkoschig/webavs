package globaz.phenix.db.principale;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.util.JACalendar;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.phenix.translation.CodeSystem;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPDecisionNonComptabiliseeListViewBean extends CPDecisionNonComptabiliseeManager implements
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private String colonneSelection = "";

    /**
	 * 
	 */
    public CPDecisionNonComptabiliseeListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPDecisionNonComptabilisee();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    public String getColonneSelection() {
        return colonneSelection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDateFacturation(int pos) {
        return ((CPDecisionNonComptabilisee) getEntity(pos)).getDatefacturation();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDescriptionTiers(int pos) {
        return ((CPDecisionNonComptabilisee) getEntity(pos)).getDesignation1() + " "
                + ((CPDecisionNonComptabilisee) getEntity(pos)).getDesignation2();
    }

    public String getGenreDecision(int pos) {
        String genreDecision = ((CPDecisionNonComptabilisee) getEntity(pos)).getGenreDecision();
        try {
            return CodeSystem.getLibelleCourt(getSession(), genreDecision);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdAffiliation(int pos) {
        return ((CPDecisionNonComptabilisee) getEntity(pos)).getIdAffiliation();
    }

    /*
     * méthode pour récupérer l'id des comptes annexes avec un num d'aff et un role == 517002
     */
    public String getIdCompteAnnexe(int pos) throws Exception {
        // Extraction du compte annexe
        String role = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication());
        CACompteAnnexe compte = new CACompteAnnexe();
        compte.setSession(getSession());
        compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        compte.setIdRole(role);
        compte.setIdExterneRole(getNumAffilie(pos));
        compte.wantCallMethodBefore(false);
        compte.retrieve();
        if (compte != null && !compte.isNew()) {
            return compte.getIdCompteAnnexe();
        } else {
            return null;
        }
    }

    public String getIdCompteIndividuel(int pos) {
        String numAvs = ((CPDecisionNonComptabilisee) getEntity(pos)).getNumAvsActuel();
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setSession(getSession());
        ciMgr.orderByAvs(false);
        CICompteIndividuel ci = ciMgr.getCIRegistreAssures(NSUtil.unFormatAVS(numAvs), null);
        if (ci != null && !ci.isNew()) {
            return ci.getCompteIndividuelId();
        } else {
            return null;
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdDecision(int pos) {
        return ((CPDecisionNonComptabilisee) getEntity(pos)).getIdDecision();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdPassage(int pos) {
        return ((CPDecisionNonComptabilisee) getEntity(pos)).getIdPassage();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdTiers(int pos) {
        return ((CPDecisionNonComptabilisee) getEntity(pos)).getIdTiers();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getMontantCI(int pos) {
        String idDecision = ((CPDecisionNonComptabilisee) getEntity(pos)).getIdDecision();
        CPDonneesCalcul donCalcul = new CPDonneesCalcul();
        donCalcul.setSession(getSession());
        return donCalcul.getMontant(idDecision, CPDonneesCalcul.CS_REV_CI);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getNumAffilie(int pos) {
        return ((CPDecisionNonComptabilisee) getEntity(pos)).getNumAffilie();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getPeriodeDecision(int pos) {
        String dateDebut = "";
        try {
            dateDebut = JACalendar.getDay(((CPDecisionNonComptabilisee) getEntity(pos)).getDebutDecision()) + "."
                    + JACalendar.getMonth(((CPDecisionNonComptabilisee) getEntity(pos)).getDebutDecision());
        } catch (Exception e) {
            dateDebut = "";
        }
        return dateDebut + " - " + ((CPDecisionNonComptabilisee) getEntity(pos)).getFinDecision();
    }

    public String getTypeDecision(int pos) {
        String typeDecision = ((CPDecisionNonComptabilisee) getEntity(pos)).getTypeDecision();
        try {
            return CodeSystem.getLibelle(getSession(), typeDecision);
        } catch (Exception e) {
            return "";
        }
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

    public void setColonneSelection(String value) {
        colonneSelection = value;
    }

}
