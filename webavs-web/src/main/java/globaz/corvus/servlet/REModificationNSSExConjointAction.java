package globaz.corvus.servlet;

import globaz.corvus.vb.ci.REModificationNSSExConjointViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class REModificationNSSExConjointAction extends REDefaultProcessAction {

    public REModificationNSSExConjointAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        REModificationNSSExConjointViewBean vb = (REModificationNSSExConjointViewBean) viewBean;

        String actionParameters = "&selectedId=" + vb.getForIdRCI() + "&idTiers=" + vb.getIdTiers();

        return "/corvus?userAction=" + IREActions.ACTION_INSCRIPTION_CI + ".chercher" + actionParameters;

    }

}
