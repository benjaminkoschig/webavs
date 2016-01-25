package globaz.draco.servlet;

import globaz.draco.db.preimpression.DSPreImpressionDeclarationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DSActionPreImpression extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur DSActionPreImpression.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public DSActionPreImpression(FWServlet servlet) {
        super(servlet);
    }

    private void _actionPreImprimer(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            // On initialise le viewBean à null
            FWViewBeanInterface viewBean = null;
            // Dans le cas ou on a pas de viewBean en session ou
            System.out.println("ViewBean en session:" + session.getAttribute("viewBean"));
            // Dans le cas ou on a un CTTexteSaisieViewBean, on instancie le
            // viewBean avec un CTTexteSaisieViewBean
            viewBean = new DSPreImpressionDeclarationViewBean();

            FWAction action = getAction();
            action.setRight(FWSecureConstants.READ);

            // appel du controlleur

            viewBean = mainDispatcher.dispatch(viewBean, action);

            // On lui passe un view Bean de ce type pour éviter les qu'il n'y
            // ait pas d'erreur Class Cast Exception
            DSPreImpressionDeclarationViewBean impression = new DSPreImpressionDeclarationViewBean();
            impression.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
            // On sette le nouveau viewBean dans la session
            session.setAttribute("viewBean", impression);
            // redirection vers destination
            // _destination= getRelativeURL(request,session) + "_de.jsp";
            _destination = "/dracoRoot/" + getIdLangueIso(session) + "/preimpression/preImpression_de.jsp";
            ;
        } catch (Exception ex) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        // Préimprimer
        _actionPreImprimer(session, request, response, dispatcher);

    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("preImprimer")) {
            // Préimprimer
            _actionPreImprimer(session, request, response, dispatcher);
        }
    }
}