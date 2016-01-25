package globaz.hercule.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.hercule.db.controleEmployeur.CECompteRenduViewBean;
import globaz.hercule.itext.controleEmployeur.CECompteRendu_Doc;

/**
 * @author SCO
 * @since 2 nov. 2010
 */
public class CECompteRenduHelper extends FWHelper {

    /**
     * Constructeur de CECompteRenduHelper
     */
    public CECompteRenduHelper() {
        super();
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CECompteRenduViewBean vb = (CECompteRenduViewBean) viewBean;

        try {

            CECompteRendu_Doc process = new CECompteRendu_Doc((BSession) session);
            process.setDateDebutControle(vb.getDateDebutControle());
            process.setDateFinControle(vb.getDateFinControle());
            process.setDateEffective(vb.getDateEffective());
            process.setVisaReviseur(vb.getVisaReviseur());
            process.setNumAffilie(vb.getNumAffilie());
            process.setIdTiers(vb.getIdTiers());
            process.setIdControle(vb.getSelectedId());

            process.setEMailAddress(vb.getEmail());
            process.setDateEnvoi(vb.getDateEnvoi());

            BProcessLauncher.start(process);

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

}
