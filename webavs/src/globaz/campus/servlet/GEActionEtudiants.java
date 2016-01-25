package globaz.campus.servlet;

import globaz.campus.vb.etudiants.GEEtudiantsAjoutViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GEActionEtudiants extends FWDefaultServletAction {

    public GEActionEtudiants(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            // pour compatibilité :
            // si on a le parametre _method=add, c'est que l'on a une action
            // nouveau

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String selectedId = request.getParameter("selectedId");

            GEEtudiantsAjoutViewBean viewBean = new GEEtudiantsAjoutViewBean();

            // pour pouvoir faire un setId
            // remarque : si il y a d'autre set a faire (si plusieurs id par ex)
            // il faut le faire dans le beforeAfficher(...)

            Class b = Class.forName("globaz.globall.db.BIPersistentObject");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });

            // initialisation du viewBean

            if (action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = (GEEtudiantsAjoutViewBean) beforeNouveau(session, request, response, viewBean);
            }

            // appelle beforeAfficher, puis le Dispatcher, puis met le bean en
            // session

            viewBean = (GEEtudiantsAjoutViewBean) beforeAfficher(session, request, response, viewBean);
            viewBean = (GEEtudiantsAjoutViewBean) mainDispatcher.dispatch(viewBean, action);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            // choix destination

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        // redirection vers la destination

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

}
