package globaz.tucana.helpers.list;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendarGregorianStandard;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.tucana.print.list.TUAffiliationProblemList;
import globaz.tucana.vb.list.TUAffiliationProblemListViewBean;

/**
 * Definition du helper pour le domaine liste.
 * 
 * @author fgo date de création : 3 juil. 06
 * @version : version 1.0
 * 
 */
public class TUAffiliationProblemListHelper extends FWHelper {

    /**
     * Constructeur
     */
    public TUAffiliationProblemListHelper() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#execute(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        TUAffiliationProblemListViewBean eViewBean = (TUAffiliationProblemListViewBean) viewBean;
        // -------------------------------------------------------------------------------------------------------------------------------------------------
        // ACTION GENERER
        // -------------------------------------------------------------------------------------------------------------------------------------------------
        if ("lancer".equals(action.getActionPart())) {
            try {
                TUAffiliationProblemList process = new TUAffiliationProblemList();
                process.setSession((BSession) session);
                process.setEMailAddress(eViewBean.getEMail());
                process.setForDatePeriodeBegin(new JADate("01".concat(eViewBean.getMois().concat(eViewBean.getAnnee())))
                        .toStrAMJ());
                process.setForDatePeriodeEnd(new JADate(recupereDateFin(eViewBean)).toStrAMJ());
                process.setForIdExterneRubrique(eViewBean.getIdRubrique());
                process.setForGenreAssu(eViewBean.getGenreAssu());
                BProcessLauncher.start(process);
                if (process.isOnError()) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(process.getMessage());
                }
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }
            return viewBean;
        } else {
            return super.execute(viewBean, action, session);
        }
    }

    /**
     * Récupère le dernier jour du mois
     * 
     * @param viewBean
     * @return
     * @throws JAException
     */
    private String recupereDateFin(TUAffiliationProblemListViewBean viewBean) throws JAException {
        JACalendarGregorianStandard calendar = new JACalendarGregorianStandard();
        return calendar.lastInMonth("01".concat(viewBean.getMois().concat(viewBean.getAnnee())));
    }

}
