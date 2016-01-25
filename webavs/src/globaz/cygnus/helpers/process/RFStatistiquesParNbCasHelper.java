/*
 * Créé le 16 janvier 2012
 */
package globaz.cygnus.helpers.process;

import globaz.cygnus.process.RFStatistiquesParNbCasProcess;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.process.RFStatistiquesParNbCasViewBean;
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
public class RFStatistiquesParNbCasHelper extends FWHelper {

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

            RFStatistiquesParNbCasViewBean vb = (RFStatistiquesParNbCasViewBean) viewBean;

            RFStatistiquesParNbCasProcess process = new RFStatistiquesParNbCasProcess();

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
        // if (JadeStringUtil.isBlankOrZero(((RFStatistiquesParNbCasViewBean) viewBean).getGestionnaire())) {
        // RFUtils.setMsgErreurViewBean(viewBean, "JSP_ERREUR_CHAMP_GESTIONNAIRE_STATS_PAR_NB_CAS");
        // this.validation = false;
        // }
        // Adresse e-mail obligatoire
        if (JadeStringUtil.isBlankOrZero(((RFStatistiquesParNbCasViewBean) viewBean).geteMailAdr())) {
            RFUtils.setMsgErreurViewBean(viewBean, "JSP_ERREUR_CHAMP_MAIL_STATS_PAR_NB_CAS");
            validation = false;
        }
        // Date de début de statistiques obligatoire
        if (JadeStringUtil.isBlankOrZero(((RFStatistiquesParNbCasViewBean) viewBean).getDateDebutStat())) {
            RFUtils.setMsgErreurViewBean(viewBean, "JSP_ERREUR_CHAMP_DATE_DEBUT_STATS_PAR_NB_CAS");
            validation = false;
        }
        // Date de fin de statistiques obligatoire
        if (JadeStringUtil.isBlankOrZero(((RFStatistiquesParNbCasViewBean) viewBean).getDateFinStat())) {
            RFUtils.setMsgErreurViewBean(viewBean, "JSP_ERREUR_CHAMP_DATE_FIN_STATS_PAR_NB_CAS");
            validation = false;
        }

        return validation;
    }
}
