/*
 * Créé le 27 août 07
 */
package globaz.corvus.helpers.paiementMensuel;

import globaz.corvus.process.AREPmtMensuel;
import globaz.corvus.process.REExecuterPaiementMensuelProcess;
import globaz.corvus.process.REExecuterRentesEnErreurProcess;
import globaz.corvus.vb.paiementMensuel.REExecuterPaiementMensuelViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author BSC
 * 
 */
public class REExecuterPaiementMensuelHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        REExecuterPaiementMensuelViewBean epmViewBean = (REExecuterPaiementMensuelViewBean) viewBean;

        AREPmtMensuel process = null;

        if (epmViewBean.getIsActiverTraitementPrstErreurs().booleanValue()) {
            process = new REExecuterRentesEnErreurProcess();
        } else {
            process = new REExecuterPaiementMensuelProcess();
        }

        process.setSession((BSession) session);
        process.setEMailAddress(epmViewBean.getEMailAddress());
        process.setMoisPaiement(epmViewBean.getDatePaiement());
        process.setDescription(epmViewBean.getDescription());
        process.setDateEcheancePaiement(epmViewBean.getDateEcheancePaiement());
        process.setIdOrganeExecution(epmViewBean.getIdOrganeExecution());
        process.setNumeroOG(epmViewBean.getNumeroOG());
        process.setIsoCsTypeAvis(epmViewBean.getIsoCsTypeAvis());
        process.setIsoGestionnaire(epmViewBean.getIsoGestionnaire());
        process.setIsoHighPriority(epmViewBean.getIsoHighPriority());
        process.start();
    }

}
