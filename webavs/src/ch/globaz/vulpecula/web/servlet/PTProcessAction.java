/**
 *
 */
package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author sel
 * 
 */
public class PTProcessAction extends FWDefaultServletAction {

    public PTProcessAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return super._getDestModifierSucces(session, request, response, viewBean);
    }
}
