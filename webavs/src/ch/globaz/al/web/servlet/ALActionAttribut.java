package ch.globaz.al.web.servlet;

import globaz.al.vb.attribut.ALAffilieViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe gérant les actions commencant par al.attribut
 * 
 * @author GMO
 * 
 */
public class ALActionAttribut extends ALAbstractDefaultAction {

    /**
     * @param servlet
     */
    public ALActionAttribut(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierEchec (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = null;
        if (viewBean instanceof ALAffilieViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".attribut.affilie.reAfficher&selectedId=" + ((ALAffilieViewBean) viewBean).getId()
                    + "&_method=upd";

        } else {
            destination = super._getDestModifierEchec(session, request, response, viewBean);
        }
        return destination;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = null;
        if (viewBean instanceof ALAffilieViewBean) {

            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".attribut.affilie.afficher&selectedId=" + ((ALAffilieViewBean) viewBean).getId();

        } else {
            destination = super._getDestModifierSucces(session, request, response, viewBean);
        }
        return destination;
    }

}
