package ch.globaz.al.web.servlet;

import globaz.al.vb.prestation.ALAttestationVersementViewBean;
import globaz.al.vb.prestation.ALDeclarationVersementViewBean;
import globaz.al.vb.prestation.ALEntetePrestationViewBean;
import globaz.al.vb.prestation.ALGenerationAffilieViewBean;
import globaz.al.vb.prestation.ALGenerationDossierViewBean;
import globaz.al.vb.prestation.ALRecapViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Classe gérant les actions commencant par al.prestation
 * 
 * @author GMO
 * 
 */
public class ALActionPrestation extends ALAbstractDefaultAction {

    /**
     * Constructeur du gestionnaire des actions prestation
     * 
     * @param servlet
     */
    public ALActionPrestation(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALGenerationDossierViewBean) {

            // si on est en génération depuis un récap en saisie des h/j
            if (!JadeNumericUtil.isEmptyOrZero(((ALGenerationDossierViewBean) viewBean).getEntetePrestationModel()
                    .getIdRecap())
                    && !ALCSDossier.UNITE_CALCUL_MOIS.equals(((ALGenerationDossierViewBean) viewBean)
                            .getEntetePrestationModel().getUnite())) {

                return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                        + ".prestation.recap.reAfficher&selectedId="
                        + ((ALGenerationDossierViewBean) viewBean).getEntetePrestationModel().getIdRecap();

            }
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".prestation.generationDossier.reAfficher&_method=add&idRecap="
                    + ((ALGenerationDossierViewBean) viewBean).getEntetePrestationModel().getIdRecap();

        } else {
            return super._getDestAjouterEchec(session, request, response, viewBean);
        }

    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALGenerationDossierViewBean) {

            // si on est en génération depuis un récap en saisie des h/j
            if (!JadeNumericUtil.isEmptyOrZero(((ALGenerationDossierViewBean) viewBean).getEntetePrestationModel()
                    .getIdRecap())
                    && !ALCSDossier.UNITE_CALCUL_MOIS.equals(((ALGenerationDossierViewBean) viewBean)
                            .getEntetePrestationModel().getUnite())) {

                // si la prestation ajoutée est la dernière qui était à 0.- on
                // retourne dans la récap
                // et pas dans la génération suivante
                if (((ALGenerationDossierViewBean) viewBean).getNbPrestationsASaisir() == 1) {

                    EntetePrestationSearchModel searchPrestDossier = new EntetePrestationSearchModel();
                    searchPrestDossier.setForIdDossier(((ALGenerationDossierViewBean) viewBean)
                            .getDossierComplexModel().getId());
                    searchPrestDossier.setOrderKey("prestDernGen");

                    try {
                        try {
                            searchPrestDossier = ALServiceLocator.getEntetePrestationModelService().search(
                                    searchPrestDossier);
                        } catch (JadePersistenceException e) {
                            ServletException error = new ServletException("ALActionPrestation call failed. ", e);

                        }
                        EntetePrestationModel entetePres = (EntetePrestationModel) searchPrestDossier
                                .getSearchResults()[0];

                        return "/"
                                + getAction().getApplicationPart()
                                + "?userAction="
                                + getAction().getApplicationPart()
                                + ".prestation.recap.afficher&selectedId="
                                + /* ((ALGenerationDossierViewBean) viewBean).getEntetePrestationModel() */entetePres
                                        .getIdRecap();
                    } catch (JadeApplicationException e) {
                        ServletException error = new ServletException("ALActionPrestation call failed. ", e);

                    }

                    return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                            + ".prestation.recap.afficher&selectedId="
                            + ((ALGenerationDossierViewBean) viewBean).getEntetePrestationModel().getIdRecap();
                } else {
                    return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                            + ".prestation.generationDossier.afficher&_method=add&idRecap="
                            + ((ALGenerationDossierViewBean) viewBean).getEntetePrestationModel().getIdRecap();
                }

            }

            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossierMain.afficher&selectedId="
                    + ((ALGenerationDossierViewBean) viewBean).getDossierComplexModel().getId()
                    + "&ongletDisplay=prestations";

        }

        if (viewBean instanceof ALDeclarationVersementViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossier.chercher";
        }

        if (viewBean instanceof ALAttestationVersementViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossier.chercher";
        }

        if (viewBean instanceof ALGenerationAffilieViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossier.chercher";
        }

        else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        // pour actions : - suppressionPrestation
        if (viewBean instanceof ALEntetePrestationViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossierMain.afficher&selectedId="
                    + ((ALEntetePrestationViewBean) viewBean).getDossierComplexModel().getId()
                    + "&ongletDisplay=prestations";
        }
        if (viewBean instanceof ALDeclarationVersementViewBean) {

            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossier.chercher";

        }
        if (viewBean instanceof ALAttestationVersementViewBean) {

            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossier.chercher";

        } else {
            return super._getDestExecuterSucces(session, request, response, viewBean);

        }

    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // pour actions : - suppressionPrestation
        if (viewBean instanceof ALRecapViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".prestation.recap.afficher&selectedId=" + ((ALRecapViewBean) viewBean).getId();
        } else {
            return super._getDestModifierSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALGenerationDossierViewBean) {
            if (!JadeStringUtil.isEmpty(request.getParameter("idDossier"))) {
                ((ALGenerationDossierViewBean) viewBean).getDossierComplexModel().setId(
                        request.getParameter("idDossier"));

            } else if (!JadeStringUtil.isEmpty(request.getParameter("idRecap"))) {
                ((ALGenerationDossierViewBean) viewBean).getEntetePrestationModel().setIdRecap(
                        request.getParameter("idRecap"));
            }

            if (!JadeStringUtil.isEmpty(request.getParameter("periodicite"))) {
                ((ALGenerationDossierViewBean) viewBean).setPeriodicite(request.getParameter("periodicite"));

            }

            if (!JadeStringUtil.isEmpty(request.getParameter("bonification"))) {
                ((ALGenerationDossierViewBean) viewBean).setBonification(request.getParameter("bonification"));

            }

            if (!JadeStringUtil.isEmpty(request.getParameter("idDroit"))) {
                ((ALGenerationDossierViewBean) viewBean).getDroitComplexModel().setId(request.getParameter("idDroit"));
            }

        }

        return super.beforeNouveau(session, request, response, viewBean);
    }

}
