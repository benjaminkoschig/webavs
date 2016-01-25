package globaz.cygnus.servlet;

import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author jje
 * 
 *         Affiche la 2eme partie de l'écran de saisie d'une Qd permettant de choisir le genre de Qd à saisir
 */
public class RFSaisieQdChoixGenreAction extends RFDefaultAction {

    public RFSaisieQdChoixGenreAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            return viewBean;
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }
    }

    @Override
    protected String getRelativeURL(HttpServletRequest request, HttpSession session) {

        String _destination = "";
        String cygnusRelativeUrl = "/cygnusRoot/qds/saisieQd";

        FWViewBeanInterface sessionVb = loadViewBean(session);

        if (!FWViewBeanInterface.ERROR.equals(sessionVb.getMsgType())) {
            _destination = cygnusRelativeUrl + "ChoixGenre";
        } else {
            _destination = cygnusRelativeUrl;
        }

        return _destination;
    }
}
