package globaz.osiris.servlet.action.historique;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.osiris.db.historique.CAHistoriqueBulletinSoldeViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CAHistoriqueBulletinSoldeAction extends FWDefaultServletAction {

    /**
     * @param servlet
     */
    public CAHistoriqueBulletinSoldeAction(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = getActionFullURL() + ".chercher";
        if (viewBean instanceof CAHistoriqueBulletinSoldeViewBean) {
            destination += ".chercher" + "&forIdSection="
                    + ((CAHistoriqueBulletinSoldeViewBean) viewBean).getIdSection();

        }
        return destination;
    }
}
