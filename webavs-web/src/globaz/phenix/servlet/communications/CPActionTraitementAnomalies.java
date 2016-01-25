/*
 * Cr�� le 17 ao�t 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.phenix.servlet.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.phenix.db.communications.CPTraitementAnomaliesViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author mmu Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CPActionTraitementAnomalies extends FWDefaultServletAction {

    public CPActionTraitementAnomalies(FWServlet servlet) {
        super(servlet);
    }

    /**
     * R�ceptionne le fichier selon trois diff�rent mode: - r�ceptionner et cr�er un journal - r�ceptionner dans un
     * journal existant - re-r�ceptionner dans un journal existant
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     */
    private void _actionExecuterReceptionner(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String _destination = "";

        try {
            CPTraitementAnomaliesViewBean viewBean = (CPTraitementAnomaliesViewBean) session.getAttribute("viewBean");
            // Retour de parametres
            String eMail = request.getParameter("eMailAddress");
            String csCanton = request.getParameter("csCanton");
            String receptionFileName = request.getParameter("fileName");

            viewBean.setEMailAdress(eMail);
            viewBean.setCsCanton(csCanton);
            viewBean.setReceptionFileName(receptionFileName);

            mainDispatcher.dispatch(viewBean, getAction());
            // Instanciation du process
            // CPProcessTraitementAnomalies process = new
            // CPProcessTraitementAnomalies();
            // //*****
            // process.setSession((BSession) mainDispatcher.getSession());
            // process.setEMailAddress(eMail);
            //
            // // ------------------------------------------------------------
            //
            // // -------
            // process.setInputFileName(receptionFileName);
            // process.setCsCanton(csCanton);
            // process.start();
            //
            // if (process.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(process.getMsgType());
            // viewBean.setMessage(process.getMessage());
            // }
            _destination = "/phenix?userAction=phenix.communications.traitementAnomalies.receptionner";
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _destination + "&_valid=fail";
            } else {
                _destination = _destination + "&process=launched";
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * affiche la prochaine page
         */
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        goSendRedirect(_destination, request, response);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * 
     *             HACK : Cette m�thode ne fait rien du tout si ce n'est afficher l'�cran de lancement du process
     *             concern� Tentative de modifier l'action en ".afficher" � la place de ".receptionner" dans le fichier
     *             "PhenixMenu" = ECHEC Pour contourner ce probl�me on fait appel � l'actionAfficher dans l'action
     *             custom
     * 
     */
    protected void _actionReceptionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            CPTraitementAnomaliesViewBean viewBean = new CPTraitementAnomaliesViewBean();
            viewBean.setSession((BSession) mainDispatcher.getSession());
            // Check les droits ;-)
            mainDispatcher.dispatch(viewBean, getAction());
            session.setAttribute("viewBean", viewBean);

            _destination = getRelativeURLwithoutClassPart(request, session) + "traitementAnomalies_de.jsp";

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        /*
         * affiche la prochaine page
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        // super.actionAfficher(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if ("receptionner".equals(getAction().getActionPart())) {
            _actionReceptionner(session, request, response, dispatcher);
        } else if ("executerReceptionner".equals(getAction().getActionPart())) {
            _actionExecuterReceptionner(session, request, response, dispatcher);
        } else {
            // on a demand� un page qui n'existe pas, si rien n'est renseign�,
            // on arrivera sur une page blanche
            // alors que dans ce cas nous allons sur la page under construction
            servlet.getServletContext().getRequestDispatcher(UNDER_CONSTRUCTION_PAGE).forward(request, response);
        }
    }

}
