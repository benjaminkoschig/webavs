/*
 * Créé le 3 août 07
 */

package globaz.corvus.vb.interetsmoratoires;

import globaz.corvus.db.interetsmoratoires.RECalculInteretMoratoire;
import globaz.corvus.db.interetsmoratoires.RECalculInteretMoratoireManager;
import globaz.corvus.db.interetsmoratoires.REInteretMoratoire;
import globaz.corvus.db.interetsmoratoires.REInteretMoratoireManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * @author BSC
 *
 */

public class REPreparationInteretMoratoireViewBean extends PRAbstractViewBeanSupport implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // ----------------------------------------------------------------------

    // ~ Instance fields
    // ---------------------------------------------------------------------------------

    private String dateDebutDroit = "";
    private String dateDecision = "";
    private String dateDepotDemande = "";
    private String decisionDepuis = "";
    private String idDemandeRente = "";
    private String idTierRequerant = "";
    private String testRetenue = "";
    private String csTypePreparationDecision = "";

    // ~ Methods
    // -----------------------------------------------------------------------------------------

    public void cleanInteretsMoratoires() {

        try {
            // on supprime les interets moratoires pour cette demande de rente
            // les calculs d'interets moratoires pour cette demande de rente et
            // le champs
            // idCalculInteretMaratoire des rentes accordees de la demande de
            // rente sont gerer
            // dans les classes parentes
            REInteretMoratoireManager imManager = new REInteretMoratoireManager();
            imManager.setSession(getSession());
            imManager.setForIdDemandeRente(getIdDemandeRente());
            imManager.find();

            if (imManager.size() > 0) {
                for (int i = 0; i < imManager.size(); i++) {
                    REInteretMoratoire im = (REInteretMoratoire) imManager.getEntity(i);
                    im.retrieve();
                    im.delete();
                }
            } else {

                // on regarde si il y a des calcul d'im
                // car il peut y avoir la preparation au calcul sans les im
                RECalculInteretMoratoireManager cimManager = new RECalculInteretMoratoireManager();
                cimManager.setSession(getSession());
                cimManager.setForIdDemandeRente(getIdDemandeRente());
                cimManager.find();

                for (int i = 0; i < cimManager.size(); i++) {
                    RECalculInteretMoratoire cim = (RECalculInteretMoratoire) cimManager.getEntity(i);
                    cim.retrieve();
                    cim.delete();
                }
            }
        } catch (Exception e) {
            _addError(e.getMessage());
        }

    }

    /**
     * @return
     */
    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    /**
     * @return
     */
    public String getDateDecision() {
        return dateDecision;
    }

    /**
     * @return
     */
    public String getDateDepotDemande() {
        return dateDepotDemande;
    }

    public String getDecisionDepuis() {
        return decisionDepuis;
    }

    /**
     * @return
     */
    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * @return
     */
    public String getIdTierRequerant() {
        return idTierRequerant;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getTestRetenue() {
        return testRetenue;
    }

    /**
     * @param string
     */
    public void setDateDebutDroit(String string) {
        dateDebutDroit = string;
    }

    /**
     * @param string
     */
    public void setDateDecision(String string) {
        dateDecision = string;
    }

    /**
     * @param string
     */
    public void setDateDepotDemande(String string) {
        dateDepotDemande = string;
    }

    public void setDecisionDepuis(String decisionDepuis) {
        this.decisionDepuis = decisionDepuis;
    }

    /**
     * @param string
     */
    public void setIdDemandeRente(String string) {
        idDemandeRente = string;
    }

    /**
     * @param string
     */
    public void setIdTierRequerant(String string) {
        idTierRequerant = string;
    }

    public void setTestRetenue(String testRetenue) {
        this.testRetenue = testRetenue;
    }

    public String getCsTypePreparationDecision() {
        return csTypePreparationDecision;
    }

    public void setCsTypePreparationDecision(String csTypePreparationDecision) {
        this.csTypePreparationDecision = csTypePreparationDecision;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        return true;
    }
}