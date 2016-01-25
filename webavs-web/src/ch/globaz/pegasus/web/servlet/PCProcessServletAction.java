package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PCProcessServletAction extends FWDefaultServletAction {

    public PCProcessServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Auto-generated method stub
        return super._getDestModifierSucces(session, request, response, viewBean);
    }

}
