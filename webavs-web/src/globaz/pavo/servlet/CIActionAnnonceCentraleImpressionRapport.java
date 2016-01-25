package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.pavo.db.process.CIAnnonceCentraleImpressionRapportViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author: mmo
 * 
 */
public class CIActionAnnonceCentraleImpressionRapport extends FWDefaultServletAction {

    public CIActionAnnonceCentraleImpressionRapport(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean.getClass().isAssignableFrom(CIAnnonceCentraleImpressionRapportViewBean.class)) {
            CIAnnonceCentraleImpressionRapportViewBean annonceCentraleImpressionRapportViewBean = (CIAnnonceCentraleImpressionRapportViewBean) viewBean;
            annonceCentraleImpressionRapportViewBean.setModeFonctionnement(request.getParameter("modeFonctionnement"));
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }
}
