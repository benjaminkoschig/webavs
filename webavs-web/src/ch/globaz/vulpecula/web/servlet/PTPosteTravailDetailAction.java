/**
 *
 */
package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.vulpecula.vb.postetravaildetail.PTEmployeurdetailViewBean;
import globaz.vulpecula.vb.postetravaildetail.PTTravailleurdetailViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author jpa
 * 
 */
public class PTPosteTravailDetailAction extends FWDefaultServletAction {
    public PTPosteTravailDetailAction(final FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestModifierSucces(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof PTTravailleurdetailViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".postetravaildetail.travailleurdetail.afficher&selectedId="
                    + ((PTTravailleurdetailViewBean) viewBean).getId();

        } else if (viewBean instanceof PTEmployeurdetailViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".postetravailvueglobale.employeurvueglobale.afficher&selectedId="
                    + ((PTEmployeurdetailViewBean) viewBean).getId();
        } else {
            destination = super._getDestModifierSucces(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Retourne la destinatin en cas de succès d'ajout d'un dossier
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof PTTravailleurdetailViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".postetravaildetail.travailleurdetail.afficher&selectedId="
                    + ((PTTravailleurdetailViewBean) viewBean).getId();

        } else {
            destination = super._getDestAjouterSucces(session, request, response, viewBean);
        }
        return destination;
    }

    @Override
    protected String _getDestAjouterEchec(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        String destination;
        if (viewBean instanceof PTTravailleurdetailViewBean) {
            // Workaround afin d'éviter de retransmettre l'id "null"
            PTTravailleurdetailViewBean vb = (PTTravailleurdetailViewBean) viewBean;
            vb.setId(null);
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".postetravaildetail.travailleurdetail.reAfficher&_method=add";
        } else {
            destination = super._getDestAjouterEchec(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Retourne la destination en cas de succès de suppression d'un dossier =>
     * page recherche dossier
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof PTTravailleurdetailViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".postetravail.travailleur.afficher";
        } else {
            destination = super._getDestSupprimerSucces(session, request, response, viewBean);
        }
        return destination;
    }

    @Override
    protected void goSendRedirect(final String url, final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        // Ne propage plus les paramètres pour cette application
        super.goSendRedirectWithoutParameters(url, request, response);
    }

}
