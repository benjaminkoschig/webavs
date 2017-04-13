/*
 * Créé le 18 juil. 07
 */
package globaz.corvus.helpers.process;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.process.REDebloquerRenteComptablisationProcess;
import globaz.corvus.process.RETraiterLotDecisionsProcess;
import globaz.corvus.vb.process.REValiderDecisionsViewBean;
import globaz.corvus.vb.process.REValiderLotViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author SCR
 * 
 */
public class REValiderLotHelper extends PRAbstractHelper {

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        String idDecision = ((REValiderDecisionsViewBean) viewBean).getIdDecision();
        REDecisionEntity decision = new REDecisionEntity();
        decision.setSession((BSession) session);
        decision.setIdDecision(idDecision);
        decision.retrieve();

        if (decision.isNew()) {
            throw new Exception("Unable to retrieve Decision - idDecision = " + idDecision);
        }

        String idDemandeRente = decision.getIdDemandeRente();
        REDemandeRenteJointDemande dem = new REDemandeRenteJointDemande();
        dem.setSession((BSession) session);
        dem.setIdDemandeRente(idDemandeRente);
        dem.retrieve();

        if (dem.isNew()) {
            throw new Exception("Unable to retrieve Demande - idDecision/idDemande = " + idDecision + " / "
                    + idDemandeRente);
        }

        ((REValiderDecisionsViewBean) viewBean).setRequerantInfo(dem.getNoAVS() + " - " + dem.getNom() + " "
                + dem.getPrenom() + " (" + dem.getDateNaissance() + " / " + session.getCodeLibelle(dem.getCsSexe())
                + ")");

        ((REValiderDecisionsViewBean) viewBean).setIdTiersRequerant(dem.getIdTiersRequerant());
        ((REValiderDecisionsViewBean) viewBean).setIdDemandeRente(dem.getIdDemandeRente());
        ((REValiderDecisionsViewBean) viewBean).setEMailAddress("");

    }

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        REValiderLotViewBean vb = (REValiderLotViewBean) viewBean;

        if (IRELot.CS_TYP_LOT_DEBLOCAGE_RA.equals(vb.getCsTypeLot())) {
            REDebloquerRenteComptablisationProcess process = new REDebloquerRenteComptablisationProcess();
            process.setSession((BSession) session);
            process.setIdLot(vb.getIdLot());
            process.setDateComptable(vb.getDateValeurComptable());
            process.setEMailAddress(vb.getEMailAddress());
            process.setIdOrganeExecution(vb.getIdOrganeExecution());
            process.setNumeroOG(vb.getNumeroOG());
            process.setDateEcheancePaiement(vb.getDateEcheancePaiement());

            process.start();

        } else {
            RETraiterLotDecisionsProcess process = new RETraiterLotDecisionsProcess((BSession) session);

            process.setIdLot(vb.getIdLot());
            process.setDateComptable(vb.getDateValeurComptable());
            process.setEMailAddress(vb.getEMailAddress());
            process.setIdOrganeExecution(vb.getIdOrganeExecution());
            process.setNumeroOG(vb.getNumeroOG());
            process.setDateEcheancePaiement(vb.getDateEcheancePaiement());
            process.setIsoGestionnaire(vb.getIsoGestionnaire());
            process.setIsoHighPriority(vb.getIsoHighPriority());
            process.start();
        }
    }
}
