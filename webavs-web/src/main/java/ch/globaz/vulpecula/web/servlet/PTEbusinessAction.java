/**
 *
 */
package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.vulpecula.vb.ebusiness.PTNouveauTravailleurViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author sel
 * 
 */
public class PTEbusinessAction extends PTDefaultServletAction {
    public PTEbusinessAction(final FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTNouveauTravailleurViewBean) {
            setBeanProperties(request, viewBean);
            PTNouveauTravailleurViewBean vb = (PTNouveauTravailleurViewBean) viewBean;
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }
}
