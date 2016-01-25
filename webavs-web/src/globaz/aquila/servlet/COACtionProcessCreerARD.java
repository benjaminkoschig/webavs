/*
 * Créé le 8 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.servlet;

import globaz.aquila.vb.process.COProcessCreerARDViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COACtionProcessCreerARD extends CODefaultServletAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe COACtionProcessCreerARD.
     * 
     * @param servlet
     */
    public COACtionProcessCreerARD(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        COProcessCreerARDViewBean viewBean = (COProcessCreerARDViewBean) session.getAttribute("viewBean");

        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType()) || !viewBean.validate()) {
            servlet.getServletContext()
                    .getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl")
                    .forward(request, response);

            return;
        }

        // on reprend la liste des montants et des séquences par administrateur.
        for (Enumeration names = request.getParameterNames(); names.hasMoreElements();) {
            String name = (String) names.nextElement();
            if (name.startsWith("montant_")) {
                String id = name.substring("montant_".length());
                if (JadeStringUtil.equals(request.getParameter("estSelectionne_" + id), "on", true)) {
                    viewBean.addMontantParAdmin(id, request.getParameter("montant_" + id));
                }
            }
            if (name.startsWith("sequence_")) {
                String id = name.substring("sequence_".length());
                if (JadeStringUtil.equals(request.getParameter("estSelectionne_" + id), "on", true)) {
                    viewBean.addSequenceParAdmin(id, request.getParameter("sequence_" + id));
                }
            }
        }
        super.actionExecuter(session, request, response, mainDispatcher);
    }
}
