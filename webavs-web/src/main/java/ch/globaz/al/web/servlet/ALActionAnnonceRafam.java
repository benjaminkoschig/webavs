package ch.globaz.al.web.servlet;

import globaz.al.helpers.rafam.ALAnnonceRafamHelper;
import globaz.al.vb.rafam.ALAnnonceRafamEDViewBean;
import globaz.al.vb.rafam.ALAnnonceRafamViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe gérant les actions commencant par al.AnnonceRafam
 *
 * @author SIG
 */
public class ALActionAnnonceRafam extends ALAbstractDefaultAction {

    private static final String creer68c = "creer68c";
    private static final String imprimerProtocole = "imprimerProtocole";

    private static final String suspendreAnnonce = "suspendreAnnonce";

    private static final String synchroniserEnfantAvecUPI = "synchroniserEnfantAvecUPI";

    private static final String validerAnnonce = "validerAnnonce";

    public ALActionAnnonceRafam(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
                                          HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        // après modif d'un droit, on retourne à son dossier
        if (viewBean instanceof ALAnnonceRafamViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".rafam.annonceRafam.chercher&searchModel.likeNssEnfant="
                    + ((ALAnnonceRafamViewBean) viewBean).getAnnonce().getAnnonceRafamModel().getNssEnfant()
                    + "&searchModel.forIdDroit="
                    + ((ALAnnonceRafamViewBean) viewBean).getAnnonce().getAnnonceRafamModel().getIdDroit()
                    + "&searchModel.forIdDossier="
                    + ((ALAnnonceRafamViewBean) viewBean).getAnnonce().getDossierModel().getIdDossier();
        } else {
            destination = super._getDestModifierSucces(session, request, response, viewBean);
        }
        return destination;
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
                                           HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = null;
        // après modif d'un droit, on retourne à son dossier
        if (viewBean instanceof ALAnnonceRafamViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".rafam.annonceRafam.chercher&searchModel.likeNssEnfant="
                    + ((ALAnnonceRafamViewBean) viewBean).getAnnonce().getAnnonceRafamModel().getNssEnfant()
                    + "&searchModel.forIdDroit="
                    + ((ALAnnonceRafamViewBean) viewBean).getAnnonce().getAnnonceRafamModel().getIdDroit()
                    + "&searchModel.forIdDossier="
                    + ((ALAnnonceRafamViewBean) viewBean).getAnnonce().getDossierModel().getIdDossier();
        } else {
            destination = super._getDestModifierSucces(session, request, response, viewBean);
        }
        return destination;

    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestExecuterEchec (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
                                           HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ((viewBean instanceof ALAnnonceRafamViewBean)
                && (ALActionAnnonceRafam.validerAnnonce.equals(getAction().getActionPart()) || ALActionAnnonceRafam.suspendreAnnonce
                .equals(getAction().getActionPart()))) {
            StringBuffer url = new StringBuffer();
            url.append("/").append(getAction().getApplicationPart()).append("?userAction=")
                    .append(getAction().getApplicationPart());
            url.append("rafam.annonceRafam.afficher&selectedId=").append(((ALAnnonceRafamViewBean) viewBean).getId());
            return url.toString();
        } else if ((viewBean instanceof ALAnnonceRafamViewBean)
                && ALActionAnnonceRafam.synchroniserEnfantAvecUPI.equals(getAction().getActionPart())) {
            StringBuffer url = new StringBuffer();
            url.append("/").append(getAction().getApplicationPart()).append("?userAction=")
                    .append(getAction().getApplicationPart());
            url.append("rafam.annonceRafam.afficher&selectedId=").append(((ALAnnonceRafamViewBean) viewBean).getId());
            return url.toString();
        } else if ((viewBean instanceof ALAnnonceRafamViewBean)
                && ALActionAnnonceRafam.creer68c.equals(getAction().getActionPart())) {
            return "/al?userAction=back";
        }
        return super._getDestExecuterEchec(session, request, response, viewBean);
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestExecuterSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
                                            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ((viewBean instanceof ALAnnonceRafamEDViewBean)
                && ALActionAnnonceRafam.imprimerProtocole.equals(getAction().getActionPart())) {
            StringBuffer url = new StringBuffer();
            url.append("/").append(getAction().getApplicationPart()).append("?userAction=")
                    .append(getAction().getApplicationPart());
            url.append(".rafam.annonceRafamED.chercher");
            return url.toString();
        }
        if ((viewBean instanceof ALAnnonceRafamViewBean)
                && ALActionAnnonceRafam.validerAnnonce.equals(getAction().getActionPart())) {
            StringBuffer url = new StringBuffer();
            url.append("/").append(getAction().getApplicationPart()).append("?userAction=")
                    .append(getAction().getApplicationPart());
            url.append("rafam.annonceRafam.chercher");
            return url.toString();
        } else if ((viewBean instanceof ALAnnonceRafamViewBean)
                && ALActionAnnonceRafam.synchroniserEnfantAvecUPI.equals(getAction().getActionPart())) {
            StringBuffer url = new StringBuffer();
            url.append("/").append(getAction().getApplicationPart()).append("?userAction=")
                    .append(getAction().getApplicationPart());
            url.append("rafam.annonceRafam.chercher");
            return url.toString();
        } else if ((viewBean instanceof ALAnnonceRafamViewBean) && "creer68c".equals(getAction().getActionPart())) {
            return "/al?userAction=back";
        } else if (viewBean instanceof ALAnnonceRafamViewBean && ALAnnonceRafamHelper.archiver.equals(getAction().getActionPart())) {
            return "al.rafam.annonceRafam.lister";
        } else {
            return super._getDestExecuterSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
                                           HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        // après modif d'une annonce, on retourne à son dossier
        if (viewBean instanceof ALAnnonceRafamViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".rafam.annonceRafam.reAfficher&selectedId="
                    + ((ALAnnonceRafamViewBean) viewBean).getAnnonce().getAnnonceRafamModel().getIdAnnonce()
                    + "&_method=upd";

        } else {
            destination = super._getDestModifierEchec(session, request, response, viewBean);
        }
        return destination;

    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
                                            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        // après modif d'une annonce, on retourne à la recherche avec les param du droit
        if (viewBean instanceof ALAnnonceRafamViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".rafam.annonceRafam.chercher&searchModel.likeNssEnfant="
                    + ((ALAnnonceRafamViewBean) viewBean).getAnnonce().getAnnonceRafamModel().getNssEnfant()
                    + "&searchModel.forIdDroit="
                    + ((ALAnnonceRafamViewBean) viewBean).getAnnonce().getAnnonceRafamModel().getIdDroit()
                    + "&searchModel.forIdDossier="
                    + ((ALAnnonceRafamViewBean) viewBean).getAnnonce().getDossierModel().getIdDossier();
        } else {
            destination = super._getDestModifierSucces(session, request, response, viewBean);
        }
        return destination;
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                  FWDispatcher mainDispatcher) throws ServletException, IOException {

        super.actionAfficher(session, request, response, mainDispatcher);
        if (!getAction().getClassPart().equals("annonceRafamED")) {
            FWViewBeanInterface viewBean = (ALAnnonceRafamViewBean) request.getAttribute(FWServlet.VIEWBEAN);

            if (viewBean instanceof ALAnnonceRafamViewBean) {
                request.getSession().setAttribute(
                        globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                        ((ALAnnonceRafamViewBean) viewBean).getAnnonce().getAllocataireComplexModel()
                                .getAllocataireModel().getIdTiersAllocataire());
            }
        }
    }

}
