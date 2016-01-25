package globaz.hercule.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.hercule.db.controleEmployeur.CEControlesPrevusViewBean;
import globaz.hercule.process.CEListeControlesPrevusProcess;

/**
 * @author SCO
 * @since 11 oct. 2010
 */
public class CEControlesPrevusHelper extends FWHelper {

    /**
     * Constructeur de CEControlesPrevusHelper
     */
    public CEControlesPrevusHelper() {
        super();
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CEControlesPrevusViewBean vb = (CEControlesPrevusViewBean) viewBean;

        CEListeControlesPrevusProcess process = new CEListeControlesPrevusProcess();

        process.setAnnee(vb.getAnnee());
        process.setEMailAddress(vb.getEmail());
        process.setTypeAdresse(vb.getTypeAdresse());
        process.setISession(session);

        try {
            BProcessLauncher.start(process);

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
