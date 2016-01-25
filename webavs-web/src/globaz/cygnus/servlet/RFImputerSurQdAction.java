// créé le 24 mars 2010
package globaz.cygnus.servlet;

import globaz.cygnus.application.RFApplication;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author JJE
 */
public class RFImputerSurQdAction extends RFDefaultAction {

    public RFImputerSurQdAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        return RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_DEMANDE_JOINT_DOSSIER_JOINT_TIERS + ".chercher";
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Auto-generated method stub
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            return super.beforeAfficher(session, request, response, viewBean);
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }
    }
}
