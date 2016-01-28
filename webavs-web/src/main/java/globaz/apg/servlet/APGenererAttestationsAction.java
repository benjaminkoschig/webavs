/*
 * Crée le 6 septembre 2006
 */
package globaz.apg.servlet;

import globaz.apg.vb.process.APGenererAttestationsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * Classe action pour l'affichage de la jsp pour la génération des attestations fiscales
 * 
 * @author hpe
 */
public class APGenererAttestationsAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APGenererAttestationsAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public APGenererAttestationsAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
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
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        APGenererAttestationsViewBean viewBean = new APGenererAttestationsViewBean();

        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        saveViewBean(viewBean, session);
        mainDispatcher.dispatch(viewBean, getAction());
        forward(getRelativeURL(request, session) + "_de.jsp", request, response);
    }
}
