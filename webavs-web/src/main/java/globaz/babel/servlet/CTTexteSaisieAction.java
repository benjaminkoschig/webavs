package globaz.babel.servlet;

import globaz.babel.vb.cat.CTTexteSaisieViewBean;
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
 * @author vre
 */
public class CTTexteSaisieAction extends CTDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CTTexteSaisieAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public CTTexteSaisieAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
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
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart() + "."
                + getAction().getPackagePart() + ".texte.chercher";
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
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart() + "."
                + getAction().getPackagePart() + ".texte.chercher";
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
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart() + "."
                + getAction().getPackagePart() + ".texte.chercher";
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                _destination = _getDestAjouterSucces(session, request, response, viewBean);
            } else {
                _destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        goSendRedirectWithoutParameters(_destination, request, response);

    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                _destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                _destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        goSendRedirectWithoutParameters(_destination, request, response);

    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((CTTexteSaisieViewBean) viewBean).setBorneInferieure(request.getParameter("borneInferieure"));

        ((CTTexteSaisieViewBean) viewBean).setNouveauNiveau(request.getParameter("niveau"));

        return viewBean;
    }

    /**
     * Recupere l'id du document qui traine en requete et initialise le viewBean de maniere a ce que tous les textes
     * crees soient pour ce document.
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
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((CTTexteSaisieViewBean) viewBean).setIdDocument(request.getParameter("idDocument"));
        ((CTTexteSaisieViewBean) viewBean).setBorneInferieure(request.getParameter("borneInferieure"));

        return viewBean;
    }

}
