package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controller des actions du package "perseus.dossier"
 * 
 * @author vyj
 */
public class PFParametresAction extends PFAbstractDefaultServletAction {

    /**
     * @param aServlet
     */
    public PFParametresAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        return super._getDestModifierSucces(session, request, response, viewBean);
    }

}
