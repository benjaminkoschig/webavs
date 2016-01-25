package globaz.aquila.servlet;

import globaz.aquila.db.batch.COParamTaxesListViewBean;
import globaz.aquila.db.batch.COParamTaxesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class COActionParamTaxes extends FWDefaultServletAction {

    public COActionParamTaxes(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        String method = "";
        try {
            method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                super.actionAfficher(session, request, response, mainDispatcher);
                // getAction().changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String i = request.getParameter("selectedId");
            if (JadeStringUtil.isBlank(i)) {
                i = (String) session.getAttribute("selectedId");
            }
            COParamTaxesListViewBean lvb = (COParamTaxesListViewBean) session.getAttribute("listViewBeanTaxe");
            COParamTaxesViewBean vb = new COParamTaxesViewBean();
            vb = (COParamTaxesViewBean) lvb.getEntity(Integer.parseInt(i));
            // vb = (COParamTaxesViewBean) session.getAttribute("viewBean");
            // vb.setIdTaxe(request.getParameter("idTaxe"));
            // vb.setIdEtape(request.getParameter("idEtape"));
            // vb.setImputerTaxes(Boolean.valueOf(request.getParameter("imputerTaxes")));
            // vb.setIdRubrique(request.getParameter("idRubrique"));
            // vb.setMontantFixe(request.getParameter("montantFixe"));
            // vb.setBaseTaxe(request.getParameter("baseTaxe"));
            // vb.setIdTraduction(request.getParameter("idTraduction"));
            // vb.setTypeTaxe(request.getParameter("typeTaxe"));
            // vb.setTypeTaxeEtape(request.getParameter("typeTaxeEtape"));
            // vb.setEtape(request.getParameter("Etape"));
            // vb.setLibelle(request.getParameter("Libelle"));
            // vb.setIdSequence(request.getParameter("idSequence"));

            if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                vb = (COParamTaxesViewBean) beforeNouveau(session, request, response, vb);
            }

            vb = (COParamTaxesViewBean) beforeAfficher(session, request, response, vb);
            vb = (COParamTaxesViewBean) mainDispatcher.dispatch(vb, getAction());
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", vb);
            request.setAttribute(FWServlet.VIEWBEAN, vb);
            /*
             * choix destination
             */
            if (vb.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        if ((method == null) || (!method.equalsIgnoreCase("ADD"))) {
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        }
    }
}
