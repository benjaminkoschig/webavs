/*
 * Cr�� le 15 ao�t 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.phenix.servlet.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import globaz.phenix.db.communications.CPReglePlausibiliteListViewBean;
import globaz.phenix.db.communications.CPReglePlausibiliteViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author mmu
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CPActionReglePlausibilite extends FWDefaultServletAction {

    public CPActionReglePlausibilite(FWServlet servlet) {
        super(servlet);
    }

    protected void _actionConfigurerPlausibilite(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        CPReglePlausibiliteViewBean viewBean = new CPReglePlausibiliteViewBean();
        String _destination;
        try {
            // Retrouve la regle de plausibilite et la place dans la session
            BISession bSession = dispatcher.getSession();
            viewBean.setISession(bSession);
            String idPlausibilite = request.getParameter("selectedId");
            viewBean.setIdPlausibilite(idPlausibilite);
            // retrieve d�placer dans le helper
            // viewBean.retrieve();
            session.setAttribute("viewBean", viewBean);
            dispatcher.dispatch(viewBean, getAction());

            _destination = getRelativeURLwithoutClassPart(request, session) + "parametrePlausibilite_rc.jsp";

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("configurer")) {
            _actionConfigurerPlausibilite(session, request, response, dispatcher);
        } else {
            // on a demand� un page qui n'existe pas
            servlet.getServletContext().getRequestDispatcher(UNDER_CONSTRUCTION_PAGE).forward(request, response);
        }
    }

    /*
     * Remet les parametres boolean � null s'ils ne sont pas dans la requete
     */
    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        CPReglePlausibiliteListViewBean listViewBean = (CPReglePlausibiliteListViewBean) viewBean;
        if (request.getParameter("actif") == null) {
            listViewBean.setForActif(null);
        }

        return viewBean;
    }

}
