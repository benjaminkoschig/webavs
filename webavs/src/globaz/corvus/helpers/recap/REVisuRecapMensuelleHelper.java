package globaz.corvus.helpers.recap;

import globaz.corvus.db.recap.access.RERecapMensuelle;
import globaz.corvus.vb.recap.REVisuRecapMensuelleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.prestation.helpers.PRAbstractHelper;

public class REVisuRecapMensuelleHelper extends PRAbstractHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        REVisuRecapMensuelleViewBean vb = (REVisuRecapMensuelleViewBean) viewBean;
        if (action.getActionPart().equals("charger")) {
            String vdate = vb.getDateRapport();
            String idRecap = loadRecapMensuelle(vdate, (BSession) session);
            if (JadeNumericUtil.isEmptyOrZero(idRecap)) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(((BSession) session).getLabel("ERREUR_AUCUN_ENREG_RECAP_MOIS_CHOISI"));
            } else {
                vb.setIdRecapMensuelle(idRecap);
            }
        } else {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage("ERROR ! NO ACTION");
        }
        return vb;
    }

    /**
     * Retourne l'id de la récap et vide si pas trouvé
     * 
     * @param vdate
     * @param session
     * @return
     */
    private String loadRecapMensuelle(String vdate, BSession session) {
        RERecapMensuelle recap = new RERecapMensuelle();
        recap.setSession(session);
        recap.setAlternateKey(RERecapMensuelle.DATE_RAPPORT_KEY);
        recap.setDateRapportMensuel(vdate);
        try {
            recap.retrieve();
            if (recap.isNew()) {
                return "";
            } else {
                return recap.getIdRecapMensuelle();
            }
        } catch (Exception e) {
            return "";
        }
    }
}
