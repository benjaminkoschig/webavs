/*
 * Créé le 16 janvier 2012
 */
package globaz.cygnus.helpers.process;

import globaz.cygnus.process.RFListeRecapitulativePaiementsProcess;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.process.RFListeRecapitulativePaiementsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * 
 * @author MBO
 */
public class RFListeRecapitulativePaiementsHelper extends FWHelper {

    boolean validation = true;

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // rien, évite un appel à retrive sur le viewBean
    }

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        // Si tous les champs obligatoire ont été renseigés, le process s'execute
        if (validate(viewBean) == true) {

            RFListeRecapitulativePaiementsViewBean vb = (RFListeRecapitulativePaiementsViewBean) viewBean;

            RFListeRecapitulativePaiementsProcess process = new RFListeRecapitulativePaiementsProcess();
            process.setSession((BSession) session);
            process.setAdresseMail(vb.geteMailAdr());
            process.setDatePeriode(vb.getDatePeriode());

            process.start();
        }

    }

    // Methode qui permet de controler que les champs obligatoires soient renseignés
    private boolean validate(FWViewBeanInterface viewBean) {
        // Adresse e-mail obligatoire
        if (JadeStringUtil.isBlankOrZero(((RFListeRecapitulativePaiementsViewBean) viewBean).geteMailAdr())) {
            RFUtils.setMsgErreurViewBean(viewBean, "JSP_ERREUR_CHAMP_ADRESSE_MAIL_LISTE_RECAP_PAIEMENTS");
            validation = false;
        }
        // Date période obligatoire
        if (JadeStringUtil.isBlankOrZero(((RFListeRecapitulativePaiementsViewBean) viewBean).geteMailAdr())) {
            RFUtils.setMsgErreurViewBean(viewBean, "JSP_ERREUR_CHAMP_DATE_PERIODE_LISTE_RECAP_PAIEMENTS");
            validation = false;
        }

        return validation;
    }
}
