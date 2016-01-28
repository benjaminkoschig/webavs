package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.helios.db.comptes.CGCentreCharge;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion des centres de charges.
 * 
 * @author DDA
 * 
 */
public class CGActionCentreCharge extends FWDefaultServletAction {

    /**
     * Constructor for CGActionCentreCharge.
     * 
     * @param servlet
     */
    public CGActionCentreCharge(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        CGCentreCharge centre = (CGCentreCharge) viewBean;
        centre.setIdMandat(request.getParameter("idMandat"));

        return super.beforeAfficher(session, request, response, centre);
    }

}
