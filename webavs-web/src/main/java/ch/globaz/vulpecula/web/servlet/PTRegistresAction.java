package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.vulpecula.vb.registre.PTAbstractAdministrationAjaxViewBean;
import globaz.vulpecula.vb.registre.PTCotisationsAPViewBean;
import globaz.vulpecula.vb.registre.PTDetailParamCotiAPViewBean;
import globaz.vulpecula.vb.registre.PTParametresCotisationsAssociationsViewBean;
import globaz.vulpecula.vb.registre.PTParametresyndicatAjaxViewBean;
import globaz.vulpecula.vb.registre.PTParametresyndicatViewBean;
import globaz.vulpecula.vb.registre.PTQualificationAjaxViewBean;
import globaz.vulpecula.vb.registre.PTSyndicatViewBean;
import java.lang.reflect.Type;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.web.gson.ConventionQualificationGSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Controller des actions du package "perseus.dossier"
 * 
 * @author jpa
 */
public class PTRegistresAction extends PTDefaultServletAction {
    /**
     * @param aServlet
     */
    public PTRegistresAction(final FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTParametresyndicatViewBean) {
            PTParametresyndicatViewBean vb = (PTParametresyndicatViewBean) viewBean;
            String idSyndicat = request.getParameter(PTConstants.ID_SYNDICAT);

            String method = request.getParameter(FWServlet.METHOD);
            if (FWAction.ACTION_ADD.equals(method)) {
                Administration syndicat = VulpeculaRepositoryLocator.getAdministrationRepository().findById(idSyndicat);
                syndicat.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                        .findAdressePrioriteCourrierByIdTiers(idSyndicat));
                vb.setSyndicat(syndicat);
            }
            vb.setConventions(VulpeculaRepositoryLocator.getAdministrationRepository().findAllConventions());
            vb.setCaissesMetiers(VulpeculaRepositoryLocator.getAdministrationRepository().findAllCaissesMetiers());
        } else if (viewBean instanceof PTSyndicatViewBean) {
            PTSyndicatViewBean vb = (PTSyndicatViewBean) viewBean;
            String idSyndicat = request.getParameter(PTConstants.ID_SYNDICAT);
            vb.setIdSyndicat(idSyndicat);
        } else if (viewBean instanceof PTParametresCotisationsAssociationsViewBean) {
            PTParametresCotisationsAssociationsViewBean vb = (PTParametresCotisationsAssociationsViewBean) viewBean;
            if (request.getParameter("forLibelle") != null) {
                String libelle = request.getParameter("forLibelle");
                if (!JadeStringUtil.isEmpty(libelle)) {
                    vb.setForLibelle(libelle);
                }
            }

        } else if (viewBean instanceof PTDetailParamCotiAPViewBean) {

            PTDetailParamCotiAPViewBean vb = (PTDetailParamCotiAPViewBean) viewBean;
            CotisationAssociationProfessionnelle cotiAP = new CotisationAssociationProfessionnelle();
            if (request.getParameter("idCotisationAP") != null) {
                String idCotisationAP = request.getParameter("idCotisationAP");
                if (!JadeStringUtil.isEmpty(idCotisationAP)) {
                    cotiAP = VulpeculaRepositoryLocator.getCotisationAssociationProfessionnelleRepository().findById(
                            idCotisationAP);
                    vb.setIdCotisationAP(idCotisationAP);
                }
            }
            vb.setCotiAP(cotiAP);
            List<Administration> associationsProfessionnelles = VulpeculaRepositoryLocator
                    .getAdministrationRepository().findAllAssociationsProfessionnelles();
            vb.setAssociationsProfessionnelles(associationsProfessionnelles);
            // workaround, ne pas appeler le super qui fait un setBeanProperties dont le comportement met le boolean à
            // false à l'affichage (car non déclaré dans la request) [selon moi -@cel-, défaut de conception]
            return viewBean;
        }
        if (viewBean instanceof PTCotisationsAPViewBean) {
            setBeanProperties(request, viewBean);
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeAjouter(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        retrieveParameters(request, viewBean);
        return super.beforeAjouter(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeModifier(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        retrieveParameters(request, viewBean);
        return super.beforeModifier(session, request, response, viewBean);
    }

    @Override
    protected String getAJAXListerSuccessDestination(final HttpSession session, final HttpServletRequest request,
            final FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTAbstractAdministrationAjaxViewBean) {
            PTAbstractAdministrationAjaxViewBean vb = (PTAbstractAdministrationAjaxViewBean) viewBean;
            if (vb.getId() != null) {
                return vb.getDestination(vb.getId());
            }
        }
        return super.getAJAXListerSuccessDestination(session, request, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTParametresyndicatAjaxViewBean) {
            PTParametresyndicatAjaxViewBean vb = (PTParametresyndicatAjaxViewBean) viewBean;
            vb.setIdSyndicat(request.getParameter(PTConstants.ID_SYNDICAT));
            vb.setIdCaisseMetier(request.getParameter(PTConstants.ID_CAISSE_METIER));
        }
        return super.beforeLister(session, request, response, viewBean);
    }

    private void recuperationQualifications(final HttpServletRequest request, final FWViewBeanInterface viewBean) {
        Gson gson = new Gson();

        // Permet de définir le type de collection
        Type collectionType = new TypeToken<List<ConventionQualificationGSON>>() {
        }.getType();

        // Déserialise la string en une collection typé
        String stringJson = request.getParameter("parametres");
        List<ConventionQualificationGSON> qualifications = gson.fromJson(stringJson, collectionType);
        ((PTQualificationAjaxViewBean) viewBean).setQualificationsGSON(qualifications);
    }

    private void retrieveParameters(final HttpServletRequest request, final FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTQualificationAjaxViewBean) {
            recuperationQualifications(request, viewBean);
        }
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTParametresyndicatViewBean) {
            PTParametresyndicatViewBean vb = (PTParametresyndicatViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.registre.syndicat.afficher&idSyndicat=" + vb.getIdSyndicat();
        }
        if (viewBean instanceof PTDetailParamCotiAPViewBean) {
            PTDetailParamCotiAPViewBean vb = (PTDetailParamCotiAPViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.registre.detailParamCotiAP.afficher&idCotisationAP="
                    + vb.getCotiAP().getId();
        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTParametresyndicatViewBean) {
            PTParametresyndicatViewBean vb = (PTParametresyndicatViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.registre.syndicat.afficher&idSyndicat=" + vb.getIdSyndicat();
        }
        if (viewBean instanceof PTDetailParamCotiAPViewBean) {
            PTDetailParamCotiAPViewBean vb = (PTDetailParamCotiAPViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.registre.detailParamCotiAP.afficher&idCotisationAP="
                    + vb.getCotiAP().getId();
        }
        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTParametresyndicatViewBean) {
            PTParametresyndicatViewBean vb = (PTParametresyndicatViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.registre.syndicat.afficher&idSyndicat=" + vb.getIdSyndicat();
        }
        if (viewBean instanceof PTDetailParamCotiAPViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=vulpecula.registre.cotisationAP.afficher";
        }
        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }
}
