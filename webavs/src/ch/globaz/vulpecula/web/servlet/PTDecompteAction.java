/**
 *
 */
package ch.globaz.vulpecula.web.servlet;

import static ch.globaz.vulpecula.web.servlet.PTConstants.*;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.servlets.FWServlet;
import globaz.vulpecula.vb.decompte.PTCotisationsAjaxViewBean;
import globaz.vulpecula.vb.decompte.PTDecompteViewBean;
import globaz.vulpecula.vb.decompte.PTImprimerTOAjaxViewBean;
import globaz.vulpecula.vb.decompte.PTTotalSalairesAjaxViewBean;
import globaz.vulpecula.vb.decomptedetail.PTDecomptedetailViewBean;
import globaz.vulpecula.vb.postetravail.PTPosteTravailViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Gère les actions pour un décompte
 * 
 * @since Web@BMS 0.01.01
 */
public class PTDecompteAction extends PTDefaultServletAction {

    public PTDecompteAction(final FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {

        if (viewBean instanceof globaz.vulpecula.vb.decomptedetail.PTDecomptedetailViewBean) {
            String idEmployeur = request.getParameter("idEmployeur");
            String method = request.getParameter("_method");

            if (FWAction.ACTION_ADD.equals(method) || FWAction.ACTION_REAFFICHER.equals(method)) {
                if (idEmployeur != null) {
                    ((globaz.vulpecula.vb.decomptedetail.PTDecomptedetailViewBean) viewBean)
                            .setIdEmployeur(idEmployeur);
                }
            }
        } else if (viewBean instanceof PTImprimerTOAjaxViewBean) {
            PTImprimerTOAjaxViewBean vb = (PTImprimerTOAjaxViewBean) viewBean;
            String idPassage = request.getParameter(ID_PASSAGE);
            String noAffilie = request.getParameter(NO_AFFILIE);
            String etatTaxation = request.getParameter(ETAT_TAXATION);
            vb.setIdPassage(idPassage);
            vb.setNoAffilie(noAffilie);
            vb.setEtatTaxation(etatTaxation);
        } else if (viewBean instanceof PTDecompteViewBean) {
            setBeanProperties(request, viewBean);
            PTDecompteViewBean vb = (PTDecompteViewBean) viewBean;
            String protege = request.getParameter("protected");
            vb.setProtege(protege);
        } else if (viewBean instanceof PTCotisationsAjaxViewBean) {
            PTCotisationsAjaxViewBean vb = (PTCotisationsAjaxViewBean) viewBean;
            String idDecompte = request.getParameter(ID_DECOMPTE);
            vb.setIdDecompte(idDecompte);
        } else if (viewBean instanceof PTTotalSalairesAjaxViewBean) {
            PTTotalSalairesAjaxViewBean vb = (PTTotalSalairesAjaxViewBean) viewBean;
            String idDecompte = request.getParameter(ID_DECOMPTE);
            vb.setIdDecompte(idDecompte);
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected String _getDestAjouterSucces(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTPosteTravailViewBean) {
            PTPosteTravailViewBean vb = (PTPosteTravailViewBean) viewBean;
            // Si le poste de travail a été rajouté par le travailleur, on
            // retourne sur le travailleur vue générale
            if (vb.isAddWithTravailleur()) {
                return "/" + getAction().getApplicationPart()
                        + "?userAction=vulpecula.postetravailvueglobale.travailleurvueglobale.afficher&selectedId="
                        + vb.getPosteTravail().getTravailleur().getId();
            }
            // Si le poste de travail a été rajouté par l'employeur, on retourne
            // sur l'employeur vue générale
            else if (vb.isAddWithEmployeur()) {
                return "/" + getAction().getApplicationPart()
                        + "?userAction=vulpecula.postetravailvueglobale.employeurvueglobale.afficher&selectedId="
                        + vb.getPosteTravail().getEmployeur().getId();
            }
            // Dans tous les autres cas, on retourne sur le travailleur
            else {
                return "/" + getAction().getApplicationPart()
                        + "?userAction=vulpecula.postetravailvueglobale.travailleurvueglobale.afficher&selectedId="
                        + vb.getPosteTravail().getTravailleur().getId();
            }
        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestAjouterEchec(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        String destination = super._getDestAjouterEchec(session, request, response, viewBean);
        destination += "&_method=add";
        return destination;
    }

    @Override
    protected String _getDestSupprimerSucces(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTDecomptedetailViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=vulpecula.decompte.decompte.afficher";
        }
        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTDecomptedetailViewBean) {
            PTDecomptedetailViewBean vb = (PTDecomptedetailViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.decomptedetail.decomptedetail.afficher&selectedId=" + vb.getId();
        }
        return super._getDestModifierSucces(session, request, response, viewBean);
    }
}
