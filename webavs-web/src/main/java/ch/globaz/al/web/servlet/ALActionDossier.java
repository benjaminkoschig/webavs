package ch.globaz.al.web.servlet;

import globaz.al.vb.dossier.ALDossierAdiViewBean;
import globaz.al.vb.dossier.ALDossierMainViewBean;
import globaz.al.vb.dossier.ALDossierViewBean;
import globaz.al.vb.dossier.ALRadiationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe gérant les actions commencant par al.dossier
 * 
 * @author GMO
 * 
 */
public class ALActionDossier extends ALAbstractDefaultAction {
    /**
     * 
     * 
     * @param servlet
     */
    public ALActionDossier(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Retourne la destination en cas d'échec d'ajout d'un dossier
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterEchec(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof ALDossierViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=al.dossier.dossier.reAfficher"
                    + "&_method=add";
        } else if (viewBean instanceof ALDossierMainViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?" + "userAction=al.dossier.dossierMain.reAfficher"
                    + "&_method=add";
        } else {
            destination = super._getDestAjouterEchec(session, request, response, viewBean);
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
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof ALDossierViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossierMain.afficher&selectedId="
                    + ((ALDossierViewBean) viewBean).getDossierComplexModel().getId();

        }

        else if (viewBean instanceof ALDossierMainViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossierMain.afficher&selectedId="
                    + ((ALDossierMainViewBean) viewBean).getDossierComplexModel().getId();
        }

        else {
            destination = super._getDestAjouterSucces(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Retourne la destination en cas d'échec d'exécution de customAction (ici copie d'un dossier)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestExecuterEchec(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if ((viewBean instanceof ALDossierMainViewBean) && (request.getParameter("userAction").indexOf("copier") != -1)) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=al.dossier.dossierMain.reAfficher"
                    + "&_method=add";
        } else {
            destination = super._getDestExecuterEchec(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Retourne la destination en cas d'échec de modification d'un dossier. => On réaffiche l'écran en mode
     * modification.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierEchec (javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = null;
        if (viewBean instanceof ALDossierViewBean) {
            destination = "/" + getAction().getApplicationPart()
                    + "?userAction=al.dossier.dossier.reAfficher&selectedId="
                    + ((ALDossierViewBean) viewBean).getDossierComplexModel().getId() + "&_method=upd";
        } else if (viewBean instanceof ALDossierMainViewBean) {
            destination = "/" + getAction().getApplicationPart()
                    + "?userAction=al.dossier.dossierMain.reAfficher&selectedId="
                    + ((ALDossierMainViewBean) viewBean).getDossierComplexModel().getId() + "&_method=upd";
        } else {
            destination = super._getDestModifierEchec(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Retourne la destination en cas de succès de modification d'un dossier
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof ALDossierViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossierMain.afficher&selectedId="
                    + ((ALDossierViewBean) viewBean).getDossierComplexModel().getId();
        } else if ((viewBean instanceof ALDossierMainViewBean)) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossierMain.afficher&selectedId="
                    + ((ALDossierMainViewBean) viewBean).getDossierComplexModel().getId();

        } else if ((viewBean instanceof ALRadiationViewBean)) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossierMain.afficher&selectedId="
                    + ((ALRadiationViewBean) viewBean).getDossierModel().getId();
        } else {
            destination = super._getDestModifierSucces(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Retourne la destination en cas de succès de suppression d'un dossier => page recherche dossier
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof ALDossierMainViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossier.chercher";
        } else {
            destination = super._getDestSupprimerSucces(session, request, response, viewBean);
        }
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        super.actionAfficher(session, request, response, mainDispatcher);

        FWViewBeanInterface viewBean = (FWViewBeanInterface) request.getAttribute(FWServlet.VIEWBEAN);

        if (viewBean instanceof ALDossierMainViewBean) {
            request.getSession().setAttribute(
                    globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                    ((ALDossierMainViewBean) viewBean).getDossierComplexModel().getAllocataireComplexModel()
                            .getAllocataireModel().getIdTiersAllocataire());
        }

        if (viewBean instanceof ALDossierViewBean) {
            request.getSession().setAttribute(
                    globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                    ((ALDossierViewBean) viewBean).getDossierComplexModel().getAllocataireComplexModel()
                            .getAllocataireModel().getIdTiersAllocataire());
        }

        if (viewBean instanceof ALDossierAdiViewBean) {
            request.getSession().setAttribute(
                    globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                    ((ALDossierAdiViewBean) viewBean).getDossierComplexModel().getAllocataireComplexModel()
                            .getAllocataireModel().getIdTiersAllocataire());
        }
    }

    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionReAfficher(session, request, response, mainDispatcher);
    }

    /**
     * Traitement effectué avant d'afficher un dossier (Récupère la date de calcul si fourni dans la requête (oui si
     * arrivée depuis btn calculer))
     * 
     * @see ch.globaz.al.web.servlet.ALAbstractDefaultAction#beforeAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof ALDossierMainViewBean) {
            // pour la date de calcul
            if (!JadeStringUtil.isEmpty(request.getParameter("dateCalcul"))) {
                ((ALDossierMainViewBean) viewBean).setDateCalcul(request.getParameter("dateCalcul"));

            }

        }
        // pour le retour à l'écran liste des décomptes depuis le détail d'un
        // décompte
        if (viewBean instanceof ALDossierAdiViewBean) {
            // si le paramètre n'est pas définie,
            if (!JadeStringUtil.isEmpty(request.getParameter("idDossier"))) {
                ((ALDossierAdiViewBean) viewBean).setId(request.getParameter("idDossier"));
            }
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    /**
     * Traitement effectué avant d'entrer dans l'écran nouveau dossier (définit l'id allocataire si fourni dans la
     * requête)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof ALDossierMainViewBean) {

            if (!JadeStringUtil.isEmpty(request.getParameter("idAllocataire"))) {
                ((ALDossierMainViewBean) viewBean).getDossierComplexModel().getAllocataireComplexModel()
                        .getAllocataireModel().setIdAllocataire(request.getParameter("idAllocataire"));
            }
            // Dans le cas d'un retour au dossier depuis les revenus btn
            // annuler.
            // Ne devrait plus entrer ici puisque les revenus pas
            // accessibles si nouveau dossier
            if (!JadeStringUtil.isEmpty(request.getParameter("idDossier"))) {
                ((ALDossierMainViewBean) viewBean).getDossierComplexModel().getDossierModel()
                        .setIdDossier(request.getParameter("idDossier"));
            }
        }
        return super.beforeNouveau(session, request, response, viewBean);
    }

    /**
     * Traitement effectué avant de supprimer un dossier
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeSupprimer(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // on veut supprimer un dossier mais le viewBean n'est initialisé
        // si appel suppression depuis la page liste de dossiers.
        if ((viewBean == null) && (request.getParameter("userAction").indexOf("dossierMain.supprimer") != -1)
                && !JadeNumericUtil.isEmptyOrZero(request.getParameter("selectedId"))) {
            try {
                viewBean = new ALDossierMainViewBean();
                ((ALDossierMainViewBean) viewBean).setId(request.getParameter("selectedId"));
                ((ALDossierMainViewBean) viewBean).retrieve();
            } catch (Exception e) {
                viewBean.setMessage(e.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

        }
        return super.beforeSupprimer(session, request, response, viewBean);
    }
}
