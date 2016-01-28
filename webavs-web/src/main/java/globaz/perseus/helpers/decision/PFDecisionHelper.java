package globaz.perseus.helpers.decision;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.perseus.vb.decision.PFDecisionViewBean;
import ch.globaz.perseus.business.constantes.IPFConstantes;

public class PFDecisionHelper extends FWHelper {

    public static String getNumeroDemandeCalculee(String annee) throws JadePersistenceException {
        String numeroDemandeCalculee = "";
        String cleDeCalcul = IPFConstantes.DECISION_CLE_INCREMENTATION;
        // Recuperation de l'increment
        String increment = JadePersistenceManager.incIndentifiant(cleDeCalcul);
        increment = JadeStringUtil.fillWithZeroes(increment, 6);
        numeroDemandeCalculee = annee + "-" + increment;

        return numeroDemandeCalculee;
    }

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);
        if (viewBean instanceof PFDecisionViewBean) {
            ((PFDecisionViewBean) viewBean).init();
        }
    }

}
