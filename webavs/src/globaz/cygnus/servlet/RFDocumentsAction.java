package globaz.cygnus.servlet;

import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.documents.RFDocumentsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author fha
 */
public class RFDocumentsAction extends RFDefaultAction {

    public RFDocumentsAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        return getActionFullURL() + ".reAfficher" + "&dateDocument="
                + ((RFDocumentsViewBean) viewBean).getDateDocument() + "&dateDebut="
                + ((RFDocumentsViewBean) viewBean).getDateDebut() + "&dateFin="
                + ((RFDocumentsViewBean) viewBean).getDateFin() + "&email="
                + ((RFDocumentsViewBean) viewBean).getEmail() + "&idGestionnaire="
                + ((RFDocumentsViewBean) viewBean).getIdGestionnaire() + "&detailRequerant="
                + ((RFDocumentsViewBean) viewBean).getDetailRequerant() + "&idDossier="
                + ((RFDocumentsViewBean) viewBean).getIdDossier() + "&idDocument="
                + ((RFDocumentsViewBean) viewBean).getIdDocument() + "&typeDocument="
                + ((RFDocumentsViewBean) viewBean).getTypeDocument();

    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionExecuter(session, request, response, mainDispatcher);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // on récupére les prarmètres de la request
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            RFUtils.setMsgErreurInattendueViewBean(viewBean, "beforeAfficher", "RFDocumentsAction");
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return viewBean;
    }
}
