package globaz.draco.servlet;

import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.declaration.MajFADHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.util.JANumberFormatter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * à la rue
 * 
 * Date de création : (18.11.2002 18:19:41)
 * 
 * @author: Administrator
 */
public class DSActionDeclaration extends DSActionCustomFind {

    public DSActionDeclaration(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof DSDeclarationViewBean) {
            if (MajFADHelper.getTypesDeclarationDepuisProprietes().size() > 0) {
                updateProvenanceDeclaration((DSDeclarationViewBean) viewBean);
            }
        }
        return super.beforeNouveau(session, request, response, viewBean);
    }

    /**
     * Update la provenance à MANUELLE en fonction de la propriété
     * système draco.majoration.declaration.manuelle.active
     */
    private void updateProvenanceDeclaration(DSDeclarationViewBean viewBean) {
        viewBean.setProvenance(DSDeclarationViewBean.PROVENANCE_MANUELLE);
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        try {
            /*
             * recuperation du bean depuis la session
             */
            DSDeclarationViewBean viewBean = (DSDeclarationViewBean) session.getAttribute("viewBean");

            /*
             * set automatique des proprietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeAdd() call du dispatcher, puis mis en session
             */
            if (request.getParameter("masseSalTotal").equals("0")) {
                viewBean.setMasseZero(true);
            }
            viewBean = (DSDeclarationViewBean) beforeAjouter(session, request, response, viewBean);
            viewBean = (DSDeclarationViewBean) mainDispatcher.dispatch(viewBean, action);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix de la destination _valid=fail : revient en mode edition _back=sl : sans effacer les champs deja
             * rempli par l'utilisateur
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                /*
                 * destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
                 */
                destination = _getDestAjouterEchec(session, request, response, viewBean);
            } else {
                if (request.getParameter("masseSalTotal").equals("0")
                        || !JANumberFormatter.deQuote(viewBean.getMasseSalTotalEcran()).equals(
                                JANumberFormatter.deQuote(viewBean.getMasseSalTotal()))) {
                    destination = "/draco?userAction=draco.declaration.ligneDeclaration.chercher&selectedId="
                            + viewBean.getIdDeclaration();
                    // destination = _getDestAjouterSucces(session, request,
                    // response, viewBean);
                } else {
                    destination = getActionFullURL() + ".afficher&_method=upd&_back=sl&selectedId="
                            + viewBean.getIdDeclaration();
                    // destination = _getDestAjouterSucces(session, request,
                    // response, viewBean);
                }
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */

        /*
         * servlet.getServletContext().getRequestDispatcher(destination).forward( request, response);
         */
        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {

        if ("customFind".equals(getAction().getActionPart())) {
            _actionCustomFind(session, request, response, mainDispatcher);
        }
    }

    /*
     * protected void actionAfficher(HttpSession session, HttpServletRequest request,HttpServletResponse response,
     * FWDispatcher mainDispatcher) throws ServletException, IOException { FWAction _monAction =
     * FWAction.newInstance(request.getParameter("userAction")); String _destination = ""; if(
     * "imprimerDeclaration".equals(_monAction.getClassPart())){ try{ DSImprimerDeclarationViewBean viewBean = new
     * DSImprimerDeclarationViewBean(); viewBean.setSession((BSession)mainDispatcher.getSession());
     * globaz.globall.http.JSPUtils.setBeanProperties(request,viewBean); session.setAttribute("viewBean", viewBean);
     * _destination = getRelativeURL(request, session) + "_de.jsp"; servlet.getServletContext().getRequestDispatcher(
     * _destination).forward(request, response); }catch(Exception e){ e.printStackTrace(); } }else{
     * super.actionAfficher(session, request, response, mainDispatcher); } }
     */

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        try {
            /*
             * recupération du bean depuis la sesison
             */
            DSDeclarationViewBean viewBean = (DSDeclarationViewBean) session.getAttribute("viewBean");

            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */
            viewBean = (DSDeclarationViewBean) beforeModifier(session, request, response, viewBean);
            viewBean = (DSDeclarationViewBean) mainDispatcher.dispatch(viewBean, action);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix de la destination _valid=fail : revient en mode edition
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = _getDestModifierEchec(session, request, response, viewBean);
            } else {
                if (!JANumberFormatter.deQuote(viewBean.getMasseSalTotalEcran()).equals(
                        JANumberFormatter.deQuote(viewBean.getMasseSalTotal()))) {
                    destination = "/draco?userAction=draco.declaration.ligneDeclaration.chercher&selectedId="
                            + viewBean.getIdDeclaration();
                } else {
                    destination = getActionFullURL() + ".afficher&_method=upd&_back=sl&selectedId="
                            + viewBean.getIdDeclaration();
                    // destination = _getDestModifierSucces(session, request,
                    // response, viewBean);
                }
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        // servlet.getServletContext().getRequestDispatcher
        // (destination).forward (request, response);
        goSendRedirect(destination, request, response);
    }

}