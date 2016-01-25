package globaz.hercule.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.hercule.db.controleEmployeur.CEControlesEffectuesViewBean;
import globaz.hercule.process.CEListeControlesEffectuesProcess;

/**
 * @author SCO
 * @since 2 déc. 2010
 */
public class CEControlesEffectuesHelper extends FWHelper {

    /**
     * Constructeur de CEControlesEffectuesHelper
     */
    public CEControlesEffectuesHelper() {
        super();
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CEControlesEffectuesViewBean vb = (CEControlesEffectuesViewBean) viewBean;

        CEListeControlesEffectuesProcess process = new CEListeControlesEffectuesProcess();

        process.setFromDateImpression(vb.getFromDateImpression());
        process.setToDateImpression(vb.getToDateImpression());
        process.setVisaReviseur(vb.getVisaReviseur());
        process.setEMailAddress(vb.getEmail());
        process.setTypeAdresse(vb.getTypeAdresse());
        process.setAnnee(vb.getAnnee());
        process.setISession(session);

        try {
            BProcessLauncher.start(process);

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
