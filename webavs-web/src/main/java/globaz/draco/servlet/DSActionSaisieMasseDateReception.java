package globaz.draco.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DSActionSaisieMasseDateReception extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur DSActionPreImpression.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public DSActionSaisieMasseDateReception(FWServlet servlet) {
        super(servlet);
    }

    protected void actionAfficherApresAnnulation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String _destination = "";
        try {

            FWAction _action = new FWRequestActionAdapter().adapt(request, FWSecureConstants.READ);
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String selectedId = request.getParameter("selectedId");

            /*
             * Creation dynamique de notre viewBean
             */
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(_action, mainDispatcher.getPrefix());

            /*
             * pour pouvoir faire un setId remarque : si il y a d'autre set a faire (si plusieurs id par ex) il faut le
             * faire dans le beforeAfficher(...)
             */
            Class b = Class.forName("globaz.globall.db.BIPersistentObject");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });

            /*
             * initialisation du viewBean
             */
            if (_action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            }

            /*
             * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getActionFullURL() + ".afficher&_method=add&_valid=";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        try {
            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            /*
             * recuperation du bean depuis la session
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set automatique des proprietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeAdd() call du dispatcher, puis mis en session
             */
            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix de la destination
             */
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                _destination = getActionFullURL() + ".afficher&_method=add&_valid=";

            } else {
                _destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(_destination, request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("afficherApresAnnulation")) {
            actionAfficherApresAnnulation(session, request, response, dispatcher);
        }
    }
}