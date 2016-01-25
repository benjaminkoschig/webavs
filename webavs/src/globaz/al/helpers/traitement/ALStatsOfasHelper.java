package globaz.al.helpers.traitement;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.process.traitement.ALStatistiquesOfasProcess;
import globaz.al.vb.traitement.ALStatsOfasViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeNumericUtil;

/**
 * Helper dédié au viewBean ALRecapImpressionViewBean
 * 
 * @author GMO
 * 
 */
public class ALStatsOfasHelper extends ALAbstractHelper {
    public static boolean isNumericIntegerPositifFixedSize(String theNumeric, int size) {

        return JadeNumericUtil.isIntegerPositif(theNumeric) && (theNumeric.length() == size);

    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            ALStatsOfasViewBean asViewBean = (ALStatsOfasViewBean) viewBean;
            if (!ALStatsOfasHelper.isNumericIntegerPositifFixedSize(asViewBean.getForAnnee(), 4)) {
                throw new IllegalArgumentException(((BSession) session).getLabel("STATISTIQUE_OFAS_YEAR_REQUIRED"));
            }

            ALStatistiquesOfasProcess process = new ALStatistiquesOfasProcess();
            process.setSession((BSession) session);
            process.setAnnee(asViewBean.getForAnnee());
            process.setEMailAddress(asViewBean.getEmail());
            process.start();

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }

}
