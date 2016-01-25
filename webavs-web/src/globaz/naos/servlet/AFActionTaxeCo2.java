package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.taxeCo2.AFLettreTaxeCo2ViewBean;
import globaz.naos.db.taxeCo2.AFMasseTaxeCo2ViewBean;
import globaz.naos.db.taxeCo2.AFTaxeCo2ViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AFActionTaxeCo2 extends FWDefaultServletAction {
    public final static String VB_ELEMENT = "viewBean";

    public AFActionTaxeCo2(FWServlet servlet) {
        super(servlet);
    }

    private void _actionImprimeLettreTaxeCo2(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        BTransaction transaction = null;
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            AFLettreTaxeCo2ViewBean viewBean = new AFLettreTaxeCo2ViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            // Mettre la session
            viewBean.setSession((BSession) mainDispatcher.getSession());
            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdTaxeCo2(selectedId);
            // Créer une transaction
            transaction = new BTransaction(viewBean.getSession());
            transaction.openTransaction();

            // GESTION DES DROITS
            viewBean = (AFLettreTaxeCo2ViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "LettreTaxeCO2_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e1) {
                    JadeLogger.error(this, e1);
                }
            }
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    protected void _actionReloadAnneeMasse(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String _destination = "";
        AFTaxeCo2ViewBean viewBean = (AFTaxeCo2ViewBean) session.getAttribute("viewBean");
        String anneeRedistri = request.getParameter("anneeRedistri");
        String method = request.getParameter("_method");
        String pageName = request.getParameter("pageName");
        try {
            viewBean.setISession(dispatcher.getSession());
            JSPUtils.setBeanProperties(request, viewBean);

            if (!JadeStringUtil.isEmpty(anneeRedistri)) {
                viewBean.setAnneeMasse("" + (JadeStringUtil.toInt(anneeRedistri) - 2));
            }

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = getRelativeURLwithoutClassPart(request, session) + pageName + "_de.jsp?_valid=fail";
        } else {
            _destination = getRelativeURLwithoutClassPart(request, session) + pageName + "_de.jsp?_method=" + method;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionReloadAnneeRedistri(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String _destination = "";
        AFTaxeCo2ViewBean viewBean = (AFTaxeCo2ViewBean) session.getAttribute("viewBean");
        String anneeMasse = request.getParameter("anneeMasse");
        String method = request.getParameter("_method");
        String pageName = request.getParameter("pageName");
        try {
            viewBean.setISession(dispatcher.getSession());
            JSPUtils.setBeanProperties(request, viewBean);

            if (!JadeStringUtil.isEmpty(anneeMasse)) {
                viewBean.setAnneeRedistri("" + (JadeStringUtil.toInt(anneeMasse) + 2));
            }

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = getRelativeURLwithoutClassPart(request, session) + pageName + "_de.jsp?_valid=fail";
        } else {
            _destination = getRelativeURLwithoutClassPart(request, session) + pageName + "_de.jsp?_method=" + method;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return "/naos?userAction=back";
    }

    private void actionCalculerMasse(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            AFMasseTaxeCo2ViewBean viewBean = new AFMasseTaxeCo2ViewBean();

            viewBean.setISession(dispatcher.getSession());
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setMasseTotal(request.getParameter("anneeMasse"));

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = destination.substring(0, destination.indexOf("_de.jsp")) + "Resultat"
                        + destination.substring(destination.indexOf("_de.jsp"));
            }

            setSessionAttribute(session, AFActionTaxeCo2.VB_ELEMENT, viewBean);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String action = request.getParameter("userAction");

        if (getAction().getActionPart().equals("calculerMasse")) {
            // chercher avec chargement des données nécessaire
            actionCalculerMasse(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("imprimerLettreCo2")) {
            // chercher avec chargement des données nécessaire
            _actionImprimeLettreTaxeCo2(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("reloadAnneeMasse")) {
            _actionReloadAnneeMasse(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("reloadAnneeRedistri")) {
            _actionReloadAnneeRedistri(session, request, response, dispatcher);
        }
    }
}
