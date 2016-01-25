/*
 * Cr�� le 28 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.servlet;

import globaz.apg.vb.lots.APLotViewBean;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APFactureJointCompensationAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_RC = "_rc.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe APFactureJointCompensationAction.
     * 
     * @param servlet
     */
    public APFactureJointCompensationAction(FWServlet servlet) {
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
        APLotViewBean lot = new APLotViewBean();

        lot.setIdLot(getSelectedId(request));
        saveViewBean(mainDispatcher.dispatch(lot, getAction()), request);
        forward(getRelativeURL(request, session) + VERS_ECRAN_RC, request, response);
    }
}
