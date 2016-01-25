// créé le 24 mars 2010
package globaz.cygnus.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author fha
 */
public class RFSaisieConventionAction extends RFDefaultAction {

    public RFSaisieConventionAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        try {
            return this.getUserActionURL(request, IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION,
                    FWAction.ACTION_CHERCHER);
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }
    }

}
