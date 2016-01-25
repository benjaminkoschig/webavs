package globaz.corvus.helpers.process;

import globaz.corvus.topaz.REDemandeCompensationOO;
import globaz.corvus.vb.process.REGenererDemandeCompensationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

public class REGenererDemandeCompensationHelper extends PRAbstractHelper {

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

        REGenererDemandeCompensationViewBean gDemCompViewBean = (REGenererDemandeCompensationViewBean) viewBean;

        REDemandeCompensationOO process;

        try {
            process = new REDemandeCompensationOO();
            process.setIdCreancier(gDemCompViewBean.getIdCreancier());
            process.setMoisAnnee(gDemCompViewBean.getMoisAnnee());
            process.setIdDemandeRente(gDemCompViewBean.getIdDemandeRente());
            process.setAfficherTauxInv(gDemCompViewBean.getAfficherTauxInv());
            process.setIsImprimerTous(gDemCompViewBean.getIsImprimerTous());
            process.setSession((BSession) session);
            process.setEmailAdresse(gDemCompViewBean.getEMailAddress());
            process.setTexte1(gDemCompViewBean.getTexte1());
            process.setTexte2(gDemCompViewBean.getTexte2());
            process.setTexte3(gDemCompViewBean.getTexte3());
            process.setTexte4(gDemCompViewBean.getTexte4());
            process.setMontant1(gDemCompViewBean.getMontant1());
            process.setMontant2(gDemCompViewBean.getMontant2());
            process.setMontant3(gDemCompViewBean.getMontant3());
            process.setMontant4(gDemCompViewBean.getMontant4());
            process.setIsSendToGed(gDemCompViewBean.getIsSendToGed());
            process.setCommentaires(gDemCompViewBean.getCommentaires());
            BProcessLauncher.start(process, false);

        } catch (FWIException e) {
            ((BSession) session).addError(e.toString());
        } catch (Exception e) {
            ((BSession) session).addError(e.toString());
        }

    }

}
