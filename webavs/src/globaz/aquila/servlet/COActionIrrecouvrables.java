package globaz.aquila.servlet;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.irrecouvrables.COSectionIrrecViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author dostes, 3 janv. 05
 * @author sel, 01.10.2009
 */
public class COActionIrrecouvrables extends CODefaultServletAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    /**
     * Crée une nouvelle instance de la classe COActionIrrecouvrables.
     * 
     * @param servlet
     */
    public COActionIrrecouvrables(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param httpSession
     * @param request
     * @param response
     * @param dispatcher
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void actionCustom(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        if ("sections".equals(getAction().getClassPart()) && "passerIrrecouvrable".equals(getAction().getActionPart())) {
            COContentieux contentieux = (COContentieux) httpSession.getAttribute("contentieuxViewBean");
            contentieux = (COContentieux) dispatcher.dispatch(contentieux, getAction());
            if (contentieux.hasErrors()) {
                httpSession.setAttribute("viewBean", contentieux);
                goSendRedirect(FWDefaultServletAction.ERROR_PAGE, request, response);
            } else {
                super.actionChercher(httpSession, request, response, dispatcher);
            }
        } else {
            super.actionCustom(httpSession, request, response, dispatcher);
        }
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        COSectionIrrecViewBean sectionIrrecViewBean = (COSectionIrrecViewBean) session.getAttribute(("viewBean"));

        // inserer les modifications dans le viewBean
        for (Enumeration names = request.getParameterNames(); names.hasMoreElements();) {
            String paramName = (String) names.nextElement();
            String paramValue = request.getParameter(paramName);

            if (paramName.startsWith("ligneMontantAffecte_")) {
                String[] ids = paramName.split("_");
                System.out.println("paramètre recu : " + paramName + " -> " + paramValue);

                sectionIrrecViewBean.updateMontantAffecteLigneDePoste(ids[1], ids[2], ids[3], ids[4], ids[5],
                        paramValue);
            }

            if (paramName.startsWith("amortissementMontant_")) {
                String[] id = paramName.split("_");
                System.out.println("paramètre recu : " + paramName + " -> " + paramValue);

                sectionIrrecViewBean.updateMontantAmortissementCi(id[1], paramValue);
            }
        }

        try {
            JSPUtils.setBeanProperties(request, sectionIrrecViewBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String destination = FWDefaultServletAction.ERROR_PAGE;
        sectionIrrecViewBean.setMessage("");
        sectionIrrecViewBean.setMsgType(FWViewBeanInterface.OK);

        // validation et redirection en fonction du résultat de la validation
        if (sectionIrrecViewBean.validerViewBean()) {
            mainDispatcher.dispatch(sectionIrrecViewBean, getAction());
            destination = _getDestExecuterSucces(session, request, response, sectionIrrecViewBean);
        } else {
            destination = _getDestExecuterEchec(session, request, response, sectionIrrecViewBean);
        }

        goSendRedirectWithoutParameters(destination, request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";

        try {
            FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
            FWViewBeanInterface viewBean = FWListViewBeanActionFactory.newInstance(_action, mainDispatcher.getPrefix());
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeLister(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);

            request.setAttribute("viewBean", viewBean);
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);
            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewBean;
    }
}
