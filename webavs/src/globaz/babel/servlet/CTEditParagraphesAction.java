/*
 * Créé le 26 janv. 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.servlet;

import globaz.babel.api.doc.ICTScalableDocument;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class CTEditParagraphesAction extends CTDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // unused private static final String VERS_ECRAN_RC = "_rc.jsp";
    /*
     * unused private static final Class[] PARAMS = new Class[] { HttpSession.class, HttpServletRequest.class,
     * HttpServletResponse.class, FWDispatcher.class, FWViewBeanInterface.class };
     */
    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CTEditParagraphesAction.
     * 
     * @param servlet
     */
    public CTEditParagraphesAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";
        try {
            ICTScalableDocument viewBean = (ICTScalableDocument) session.getAttribute(FWServlet.VIEWBEAN);

            session.removeAttribute(FWServlet.VIEWBEAN);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix destination
             */
            if (((FWViewBeanInterface) viewBean).getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp?" + "_valid=new";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public void allerVersEcranPrecedent(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        String destination = ((ICTScalableDocument) viewBean).getPreviousUrl() + "&_valid=new&_method=add";

        session.setAttribute(FWServlet.VIEWBEAN, viewBean);
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);
        goSendRedirect(destination, request, response);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public void allerVersEcranSuivant(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        String destination = ((ICTScalableDocument) viewBean).getNextUrl() + "&_valid=new&_method=add";

        session.setAttribute(FWServlet.VIEWBEAN, viewBean);
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);
        goSendRedirect(destination, request, response);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String arreterGenererDocument(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        try {
            getAction().changeActionPart(FWAction.ACTION_AFFICHER);
        } catch (Exception e) {
            return ERROR_PAGE;
        }

        mainDispatcher.dispatch(viewBean, getAction());
        session.removeAttribute(FWServlet.VIEWBEAN);

        return ((ICTScalableDocument) viewBean).getReturnUrl();
    }
}
