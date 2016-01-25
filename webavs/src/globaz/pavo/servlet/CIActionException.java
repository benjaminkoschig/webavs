package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CIExceptionsViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author: MMO 31.08.2011
 */
public class CIActionException extends FWDefaultServletAction {

    public CIActionException(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        return getActionFullURL() + ".afficher&_method=add&_valid=_new";
    }

    @Override
    protected void actionAfficher(javax.servlet.http.HttpSession session, HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        // Inforom 573 si c'est une exception on passe un attribut warning à la requête
        if (session.getAttribute("viewBean").getClass().equals(CIExceptionsViewBean.class)) {
            CIExceptionsViewBean vBean = (CIExceptionsViewBean) session.getAttribute("viewBean");
            request.setAttribute("warningEmployeurSansPerso", vBean.getWarningEmployeurSansPersoOrAccountZero());
        }
        super.actionAfficher(session, request, response, mainDispatcher);

    };

    @Override
    protected FWViewBeanInterface beforeAfficher(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        // inforom 573 ajout du message pour les personnelles
        if (session.getAttribute("viewBean").getClass().equals(CIExceptionsViewBean.class)
                && !JadeStringUtil.isBlankOrZero((String) request.getAttribute("warningEmployeurSansPerso"))) {
            CIExceptionsViewBean vBean = (CIExceptionsViewBean) viewBean;
            vBean.setWarningEmployeurSansPersoOrAccountZero((String) request.getAttribute("warningEmployeurSansPerso"));
            return vBean;
        } else {
            return viewBean;
        }
    };

}
