package globaz.ij.servlet;

import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.ij.vb.prestations.IJIJCalculeeJointIndemniteListViewBean;
import globaz.ij.vb.prononces.IJNSSDTO;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
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
public class IJIJCalculeeJointIndemniteAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_RC = "_rc.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJIJCalculeeJointIndemniteAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJIJCalculeeJointIndemniteAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Redefinition pour creer un list view bean, le renseigner (dans le helper) avec les informations sur le prononce
     * choisi et le sauver dans la requete pour l'affichage des infos dans la page de recherche
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        IJIJCalculeeJointIndemniteListViewBean viewBean = new IJIJCalculeeJointIndemniteListViewBean();

        viewBean.setForIdPrononce(request.getParameter("idPrononce"));
        viewBean.setCsTypeIJ(request.getParameter("csTypeIJ"));
        mainDispatcher.dispatch(viewBean, getAction());
        saveViewBean(viewBean, request);

        IJNSSDTO dto = new IJNSSDTO();
        dto.setNSS(viewBean.getNoAVSAssure());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        forward(getRelativeURL(request, session) + VERS_ECRAN_RC, request, response);
    }
}
