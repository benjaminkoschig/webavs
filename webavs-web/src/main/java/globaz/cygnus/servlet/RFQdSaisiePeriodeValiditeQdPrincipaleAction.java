/*
 * Créé le 30 novembre 2009
 */
package globaz.cygnus.servlet;

import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author jje
 */
public class RFQdSaisiePeriodeValiditeQdPrincipaleAction extends RFDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public RFQdSaisiePeriodeValiditeQdPrincipaleAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

}
