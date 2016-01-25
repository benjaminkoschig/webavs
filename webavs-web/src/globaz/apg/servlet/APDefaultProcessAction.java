/*
 * Créé le 6 juin 05
 */
package globaz.apg.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
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
public class APDefaultProcessAction extends FWDefaultServletAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APDefaultProcessAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public APDefaultProcessAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * On redirige vers la page des process
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    // protected String _getDestExecuterSucces(HttpSession session,
    // HttpServletRequest request,
    // HttpServletResponse response,
    // FWViewBeanInterface viewBean) {
    // //return getRelativeURL(request, session) + "_de.jsp";
    // return "/"+getAction().getApplicationPart()+"?userAction=showProcess";
    // }

    /**
     * Redefinie car l'action afficher par défaut cherche à faire instancier un viewbean et a faire un setId dessus,
     * chose que n'ont pas les process
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
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());

            // on lui donne les parametres en requete au cas ou.
            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setISession(mainDispatcher.getSession());
            saveViewBean(viewBean, session);
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = loadViewBean(session);

        if (viewBean instanceof PRAbstractViewBeanSupport) {
            try {
                JSPUtils.setBeanProperties(request, viewBean);

                if (!((PRAbstractViewBeanSupport) viewBean).validate()) {

                    servlet.getServletContext()
                            .getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp?_method=add")
                            .forward(request, response);

                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                viewBean.setMessage(e.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                servlet.getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
            }
        }

        super.actionExecuter(session, request, response, mainDispatcher);
    }

    /**
     * charge le viewBean sauve dans le session sous le nom standard.
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected FWViewBeanInterface loadViewBean(HttpSession session) {
        return (FWViewBeanInterface) session.getAttribute(FWServlet.VIEWBEAN);
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     */
    protected void saveViewBean(FWViewBeanInterface viewBean, HttpSession session) {
        session.setAttribute(FWServlet.VIEWBEAN, viewBean);
    }
}
