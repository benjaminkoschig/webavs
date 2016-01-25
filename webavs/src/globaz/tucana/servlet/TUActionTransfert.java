package globaz.tucana.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.tucana.application.TUApplication;
import globaz.tucana.db.bouclement.access.TUBouclementManager;
import globaz.tucana.exception.fw.TUFWCountException;
import globaz.tucana.exception.fw.TUFWFindException;
import globaz.tucana.vb.transfert.TUExportViewBean;
import globaz.tucana.vb.transfert.TUImportationViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Définition de l'action pour le package transfert
 * 
 * @author fgo date de création : 25.08.2006
 * @version : version 1.0
 */
public class TUActionTransfert extends TUActionTucanaDefault {

    /**
     * @param servlet
     */
    public TUActionTransfert(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestExecuterEchec (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof TUExportViewBean) {
            // Redirection, bouclement_rc
            return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
        } else if (viewBean instanceof TUImportationViewBean) {
            // Redirection, bouclement_rc
            return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
        } else {
            return super._getDestExecuterEchec(session, request, response, viewBean);
        }
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
        if (viewBean instanceof TUExportViewBean) {
            // Redirection, bouclement_rc
            return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
        } else if (viewBean instanceof TUImportationViewBean) {
            // Redirection, bouclement_rc
            return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
        } else {
            return super._getDestExecuterSucces(session, request, response, viewBean);
        }
    }

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

        if ("export".equals(getAction().getClassPart())) {
            TUExportViewBean vBean = new TUExportViewBean();
            // FWAction action =
            // FWAction.newInstance(request.getParameter("userAction"));
            String destination = "";
            // FWAction action = new
            // FWAction(request.getParameter("userAction"),
            // FWSecureConstants.READ);
            vBean.setISession(mainDispatcher.getSession());
            // vBean = mainDispatcher.dispatch(vBean, getAction());
            session.setAttribute("viewBean", vBean);
            destination = getRelativeURL(request, session) + "_de.jsp";

            try {
                if (!rechercheBouclement(session)) {
                    vBean.setMsgType(FWViewBeanInterface.ERROR);
                    vBean.setMessage(getSession(session).getLabel("ERR_AUCUN_BOUCLEMENT_ENCOURS"));
                    destination = destination.concat("?_method=add");
                } else {
                    destination = destination.concat("?_method=add&_valid=error");
                }
            } catch (Exception e) {
                throw new IOException(e.toString());
            }
            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

        } else if ("importation".equals(getAction().getClassPart())) {
            super.actionAfficher(session, request, response, mainDispatcher);
            // String destination = "";
            // FWAction action = new
            // FWAction(request.getParameter("userAction"),
            // FWSecureConstants.READ);
            // FWViewBeanInterface vBean = (FWViewBeanInterface)
            // FWViewBeanActionFactory.newInstance(action,
            // mainDispatcher.getPrefix());
            // vBean.setISession(mainDispatcher.getSession());
            // vBean = mainDispatcher.dispatch(vBean, getAction());
            // session.setAttribute("viewBean", vBean);
            // destination = getRelativeURL(request, session) +
            // "_de.jsp?_method=add&_valid=error";
            // servlet.getServletContext().getRequestDispatcher(destination).forward(request,
            // response);
        } else {
            super.actionAfficher(session, request, response, mainDispatcher);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#goSendRedirect(java .lang.String,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goSendRedirect(String url, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (url.indexOf("tucanaRoot") > -1) {
            servlet.getServletContext().getRequestDispatcher(url).forward(request, response);
        } else {
            super.goSendRedirect(url, request, response);
        }
    }

    /**
     * Recherche le bouclement en fonction du mois et de l'année
     * 
     * @param transaction
     * @param annee
     * @param mois
     * @return le premier bouclement trouvé (le seul)
     * @throws Exception
     * @throws TUFWCountException
     * @throws TUFWFindException
     */
    private boolean rechercheBouclement(HttpSession session) throws Exception {
        TUBouclementManager manager = new TUBouclementManager();
        manager.setSession(getSession(session));
        // manager.setForCsEtat(ITUCSConstantes.CS_ETAT_BOUCLE);
        int count = 0;
        try {
            count = manager.getCount();
        } catch (Exception e) {
            throw new Exception(this.getClass().getName() + ".rechercheBouclement()" + e.toString());
        }
        return count > 0 ? true : false;
    }
}
