/**
 *
 */
package ch.globaz.vulpecula.web.servlet;

import static ch.globaz.vulpecula.web.servlet.PTConstants.*;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSessionUtil;
import globaz.vulpecula.vb.postetravail.PTCotisationsAjaxViewBean;
import globaz.vulpecula.vb.postetravail.PTEmployeurViewBean;
import globaz.vulpecula.vb.postetravail.PTPosteTravailViewBean;
import globaz.vulpecula.vb.postetravail.PTPrestationsAjaxViewBean;
import globaz.vulpecula.vb.postetravail.PTPrestationsdetailAjaxViewBean;
import globaz.vulpecula.vb.postetravail.PTTravailleurViewBean;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

/**
 * @author sel
 * 
 */
public class PTPosteTravailAction extends PTDefaultServletAction {
    public PTPosteTravailAction(final FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {

        if (viewBean instanceof PTPosteTravailViewBean) {
            String idEmployeur = request.getParameter(ID_EMPLOYEUR);
            String idTravailleur = request.getParameter(ID_TRAVAILLEUR);
            String method = request.getParameter("_method");
            PTPosteTravailViewBean vb = (PTPosteTravailViewBean) viewBean;
            if (FWAction.ACTION_ADD.equals(method) || FWAction.ACTION_REAFFICHER.equals(method)) {
                vb.setIsNouveau(true);

                // On charge les caisses maladies

                List<Administration> listCaisseMaladiePourEcran = new ArrayList<Administration>();

                List<Administration> listCaisseMaladie = VulpeculaRepositoryLocator.getAdministrationRepository()
                        .findAllCaissesMaladies();

                String aucuneCaisseLabel = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_AUCUNE_CAISSE");
                Administration cmVide = new Administration();
                cmVide.setId("-1");
                cmVide.setDesignation1(aucuneCaisseLabel);

                listCaisseMaladiePourEcran.add(cmVide);

                for (Administration administration : listCaisseMaladie) {
                    listCaisseMaladiePourEcran.add(administration);
                }

                vb.setListCaisseMaladie(listCaisseMaladiePourEcran);

                if (idEmployeur != null) {
                    Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findByIdAffilie(
                            idEmployeur);
                    vb.setEmployeur(employeur);
                    vb.setAddWithEmployeur(true);
                } else if (idTravailleur != null) {
                    List<AffiliationCaisseMaladie> listAffiliationCM = VulpeculaRepositoryLocator
                            .getAffiliationCaisseMaladieRepository().findByIdTravailleur(idTravailleur);
                    if (listAffiliationCM.size() > 0) {
                        vb.setAffiliationCaisseMaladie(listAffiliationCM.get(0).getIdCaisseMaladie());
                    } else {
                        vb.setAffiliationCaisseMaladie("-1");
                    }

                    Travailleur travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(
                            idTravailleur);
                    vb.setTravailleur(travailleur);
                    vb.setAddWithTravailleur(true);
                }
            }
        } else if (viewBean instanceof PTCotisationsAjaxViewBean) {
            PTCotisationsAjaxViewBean vb = (PTCotisationsAjaxViewBean) viewBean;
            String idPosteTravail = request.getParameter(ID_POSTE_TRAVAIL);
            String idEmployeur = request.getParameter(ID_EMPLOYEUR);
            String dateDebut = request.getParameter(DATE_DEBUT);
            String dateFin = request.getParameter(DATE_FIN);
            String dateNaissanceTravailleur = request.getParameter(DATE_NAISSANCE_TRAVAILLEUR);
            String sexeTravailleur = request.getParameter(SEXE_TRAVAILLEUR);
            vb.setIdEmployeur(idEmployeur);
            vb.setDateDebut(dateDebut);
            vb.setDateFin(dateFin);
            vb.setIdPosteTravail(idPosteTravail);
            vb.setDateNaissanceTravailleur(dateNaissanceTravailleur);
            vb.setSexeTravailleur(sexeTravailleur);
        } else if (viewBean instanceof PTPrestationsAjaxViewBean) {
            PTPrestationsAjaxViewBean vb = (PTPrestationsAjaxViewBean) viewBean;
            String idEmployeur = request.getParameter(ID_EMPLOYEUR);
            vb.setIdEmployeur(idEmployeur);
        } else if (viewBean instanceof PTPrestationsdetailAjaxViewBean) {
            PTPrestationsdetailAjaxViewBean vb = (PTPrestationsdetailAjaxViewBean) viewBean;
            String idPassage = request.getParameter(ID_PASSAGE);
            String idEmployeur = request.getParameter(ID_EMPLOYEUR);
            vb.setIdPassage(idPassage);
            vb.setIdEmployeur(idEmployeur);
        } else if (viewBean instanceof PTEmployeurViewBean) {
            setBeanProperties(request, viewBean);
            PTEmployeurViewBean vb = (PTEmployeurViewBean) viewBean;
            String protege = request.getParameter("protected");
            vb.setProtege(protege);
        } else if (viewBean instanceof PTTravailleurViewBean) {
            setBeanProperties(request, viewBean);
            PTTravailleurViewBean vb = (PTTravailleurViewBean) viewBean;
            String protege = request.getParameter("protected");
            vb.setProtege(protege);
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
    protected String _getDestModifierSucces(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTPosteTravailViewBean) {
            PTPosteTravailViewBean vb = (PTPosteTravailViewBean) viewBean;
            if (vb.isAddWithEmployeur()) {
                return "/" + getAction().getApplicationPart()
                        + "?userAction=vulpecula.postetravailvueglobale.employeurvueglobale.afficher&selectedId="
                        + vb.getPosteTravail().getEmployeur().getId();
            } else if (vb.isAddWithTravailleur()) {
                return "/" + getAction().getApplicationPart()
                        + "?userAction=vulpecula.postetravailvueglobale.travailleurvueglobale.afficher&selectedId="
                        + vb.getPosteTravail().getTravailleur().getId();
            } else {
                return "/" + getAction().getApplicationPart()
                        + "?userAction=vulpecula.postetravailvueglobale.employeurvueglobale.afficher&selectedId="
                        + vb.getPosteTravail().getEmployeur().getId();
            }
        }
        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTPosteTravailViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=back";
        }
        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }
}
