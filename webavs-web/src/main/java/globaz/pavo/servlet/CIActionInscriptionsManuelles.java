/*
 * Créé le 29 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.util.JAUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CIActionInscriptionsManuelles extends FWDefaultServletAction {
    /**
     * @param servlet
     */
    public CIActionInscriptionsManuelles(FWServlet servlet) {
        super(servlet);
        // TODO Raccord de constructeur auto-généré
    }

    private boolean _criteresOk(HttpServletRequest request) {
        String[] criteres = new String[] { "fromAffilie", };

        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < criteres.length; i++) {
            buf.append(request.getParameter(criteres[i]));
        }
        return !JAUtil.isStringEmpty(buf.toString());
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        try {
            FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
            FWViewBeanInterface viewBean = FWListViewBeanActionFactory.newInstance(_action, mainDispatcher.getPrefix());
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /**
             * check des critères
             */
            boolean criteresOk = _criteresOk(request);

            if (criteresOk) {
                viewBean = beforeLister(session, request, response, viewBean);
                viewBean = mainDispatcher.dispatch(viewBean, _action);
            } else {
                BSession ses = (BSession) mainDispatcher.getSession();

                viewBean.setMessage(ses.getLabel("CRITERES_INSUFFISANTS"));
                viewBean.setMsgType(FWViewBeanInterface.ERROR);

            }
            request.setAttribute("viewBean", viewBean);
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);
            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }
}