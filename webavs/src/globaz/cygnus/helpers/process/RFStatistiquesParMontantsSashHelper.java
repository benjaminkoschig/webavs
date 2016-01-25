/*
 * Créé le 16 janvier 2012
 */
package globaz.cygnus.helpers.process;

import globaz.cygnus.process.RFStatistiquesParMontantsSashProcess;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.process.RFStatistiquesParMontantsSashViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author MBO
 */
public class RFStatistiquesParMontantsSashHelper extends FWHelper {

    boolean validation = true;

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

            RFStatistiquesParMontantsSashViewBean vb = (RFStatistiquesParMontantsSashViewBean) viewBean;

            RFStatistiquesParMontantsSashProcess process = new RFStatistiquesParMontantsSashProcess();

            process.setSession((BSession) session);
            process.setAdresseMail(vb.geteMailAdr());
            process.setDateDebutStat(vb.getDateDebutStat());
            process.setDateFinStat(vb.getDateFinStat());
            process.setGestionnaire(vb.getGestionnaire());

            process.start();
        }
    }

    // Methode qui permet de controler que les champs obligatoires soient renseignés
    private boolean validate(FWViewBeanInterface viewBean) {

        // Gestionnaire obligatoire
        // if (JadeStringUtil.isBlankOrZero(((RFStatistiquesParMontantsSashViewBean) viewBean).getGestionnaire())) {
        // RFUtils.setMsgErreurViewBean(viewBean, "JSP_ERREUR_CHAMP_GESTIONNAIRE_STATS_PAR_MONTANTS");
        // this.validation = false;
        // }
        // Adresse e-mail obligatoire
        if (JadeStringUtil.isBlankOrZero(((RFStatistiquesParMontantsSashViewBean) viewBean).geteMailAdr())) {
            RFUtils.setMsgErreurViewBean(viewBean, "JSP_ERREUR_CHAMP_MAIL_STATS_PAR_MONTANTS");
            validation = false;
        }
        // Date de début de statistiques obligatoire
        if (JadeStringUtil.isBlankOrZero(((RFStatistiquesParMontantsSashViewBean) viewBean).getDateDebutStat())) {
            RFUtils.setMsgErreurViewBean(viewBean, "JSP_ERREUR_CHAMP_DATE_DEBUT_STATS_PAR_MONTANTS");
            validation = false;
        }
        // Date de fin de statistiques obligatoire
        if (JadeStringUtil.isBlankOrZero(((RFStatistiquesParMontantsSashViewBean) viewBean).getDateFinStat())) {
            RFUtils.setMsgErreurViewBean(viewBean, "JSP_ERREUR_CHAMP_DATE_FIN_STATS_PAR_MONTANTS");
            validation = false;
        }

        return validation;
    }

}
