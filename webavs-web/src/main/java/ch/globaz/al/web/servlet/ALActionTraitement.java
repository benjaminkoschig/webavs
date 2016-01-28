package ch.globaz.al.web.servlet;

import globaz.al.vb.traitement.ALProcessusGestionViewBean;
import globaz.al.vb.traitement.ALProcessusViewBean;
import globaz.al.vb.traitement.ALRecapImpressionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ALActionTraitement extends ALAbstractDefaultAction {

    public ALActionTraitement(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.web.servlet.ALAbstractDefaultAction#_getDestAjouterSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALRecapImpressionViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossier.chercher";
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof ALProcessusGestionViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".traitement.processusGestion.reAfficher";
        } else {

            return super._getDestExecuterEchec(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALProcessusGestionViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".traitement.processusGestion.afficher";
        } else {
            return super._getDestExecuterSucces(session, request, response, viewBean);
        }

    }

    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALProcessusGestionViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".traitement.processusGestion.afficher";
        } else {

            return super._getDestModifierEchec(session, request, response, viewBean);
        }

    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALProcessusGestionViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".traitement.processusGestion.afficher";
        } else if (viewBean instanceof ALProcessusViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".traitement.processusGestion.afficher";
        }

        else {

            return super._getDestModifierSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof ALProcessusGestionViewBean) {
            // pour savoir quelle année on affiche par défaut
            if (!JadeStringUtil.isEmpty(request.getParameter("yearDisplay"))) {
                ((ALProcessusGestionViewBean) viewBean).setYearDisplay(request.getParameter("yearDisplay"));

            } else {
                ((ALProcessusGestionViewBean) viewBean).setYearDisplay(JadeDateUtil.getGlobazFormattedDate(new Date())
                        .substring(6));
            }

        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

}
