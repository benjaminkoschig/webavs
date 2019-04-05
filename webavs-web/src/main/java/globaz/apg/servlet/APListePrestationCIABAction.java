/*
 * Créé le 15 juin 05
 */
package globaz.apg.servlet;

import globaz.apg.itext.APListePrestationVersee_Doc;
import globaz.apg.vb.process.APListePrestationCIABViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.prestation.utils.ged.PRGedUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APListePrestationCIABAction extends APDefaultProcessAction {

    public APListePrestationCIABAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());

            JSPUtils.setBeanProperties(request, viewBean);

            ((APListePrestationCIABViewBean) viewBean).setDisplayGedCheckbox(false);
            if (PRGedUtils.isDocumentInGed(APListePrestationVersee_Doc.NUM_INFOROM,
                    (BSession) mainDispatcher.getSession())) {
                ((APListePrestationCIABViewBean) viewBean).setDisplayGedCheckbox(true);
            }

            viewBean.setISession(mainDispatcher.getSession());
            saveViewBean(viewBean, session);
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}
