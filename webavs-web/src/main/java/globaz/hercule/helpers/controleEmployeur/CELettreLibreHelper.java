package globaz.hercule.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.hercule.db.controleEmployeur.CELettreLibreViewBean;
import globaz.hercule.itext.controleEmployeur.CELettreLibre_Doc;

/**
 * @author SCO
 * @since 24 nov. 2010
 */
public class CELettreLibreHelper extends FWHelper {

    /**
     * Constructeur de CELettreLibreHelper
     */
    public CELettreLibreHelper() {
        super();
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CELettreLibreViewBean vb = (CELettreLibreViewBean) viewBean;

        try {

            CELettreLibre_Doc process = new CELettreLibre_Doc((BSession) session);
            process.setEMailAddress(vb.getEmail());
            process.setDateEnvoi(vb.getDateEnvoi());
            process.setTexteLibre(vb.getTexteLibre());

            process.setDateDebutControle(vb.getDateDebutControle());
            process.setDateFinControle(vb.getDateFinControle());
            process.setDateEffective(vb.getDateEffective());
            process.setVisaReviseur(vb.getVisaReviseur());
            process.setNumAffilie(vb.getNumAffilie());
            process.setIdTiers(vb.getIdTiers());
            process.setIdControle(vb.getSelectedId());

            BProcessLauncher.start(process);

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
