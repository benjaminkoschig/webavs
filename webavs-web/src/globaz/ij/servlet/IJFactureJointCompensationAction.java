/*
 * Créé le 6 oct. 05
 */
package globaz.ij.servlet;

import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.ij.vb.lots.IJLotViewBean;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJFactureJointCompensationAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_RC = "_rc.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJFactureJointCompensationAction.
     * 
     * @param servlet
     */
    public IJFactureJointCompensationAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        IJLotViewBean lot = new IJLotViewBean();

        lot.setIdLot(getSelectedId(request));
        saveViewBean(mainDispatcher.dispatch(lot, getAction()), request);
        forward(getRelativeURL(request, session) + VERS_ECRAN_RC, request, response);
    }
}
