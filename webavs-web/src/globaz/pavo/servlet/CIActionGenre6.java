package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CIActionGenre6 extends FWDefaultServletAction {
    /**
     * Constructor for CIActionGenre6.
     * 
     * @param servlet
     */
    public CIActionGenre6(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestExecuterSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String dst = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart() + "."
                + "compte.ecrituresSuspens.chercher";

        return dst;
    }

}
