package ch.globaz.al.web.servlet;

import globaz.al.vb.droit.ALDroitViewBean;
import globaz.al.vb.droit.ALEnfantViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Classe gérant les actions commencant par al.droit
 * 
 * @author GMO
 * 
 */
public class ALActionDroit extends ALAbstractDefaultAction {
    /**
     * @param servlet
     */

    public ALActionDroit(FWServlet servlet) {
        super(servlet);

    }

    /**
     * Retourne la destination en cas d'échec d'ajout d'un droit
     * 
     * @param session
     *            session http en cours
     * @param request
     *            requête http
     * @param response
     *            réponse http
     * @param viewBean
     *            contient le modèle du droit en cours de traitement
     * @return destination
     * 
     */
    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        // Quand on a ajouté un allocataire, avec erreur, on remet direct en
        // _method=add et on reste sur la page pour corriger
        if (viewBean instanceof ALDroitViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=al.droit.droit.reAfficher"
                    + "&_method=add";
            // pour remettre le type de droit
            request.setAttribute("type_droit", ((ALDroitViewBean) viewBean).getDroitComplexModel().getDroitModel()
                    .getTypeDroit());

        } else {
            destination = super._getDestAjouterEchec(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Retourne la destination en cas de succès d'ajout de droit
     * 
     * @param session
     *            Session http
     * @param request
     *            Requête http
     * @param response
     *            Réponse http
     * @param viewBean
     *            contient le modèle du droit ajouté
     * @return destination
     * 
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;

        if (viewBean instanceof ALDroitViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".droit.droit.afficher&selectedId=" + ((ALDroitViewBean) viewBean).getDroitComplexModel().getId();

            if (((ALDroitViewBean) viewBean).getIdMessageDroitPC() != null) {
                request.setAttribute("idMessageDroitPC", ((ALDroitViewBean) viewBean).getIdMessageDroitPC());
                destination += "&idMessageDroitPC=" + ((ALDroitViewBean) viewBean).getIdMessageDroitPC();
            }
        } else {
            destination = super._getDestAjouterSucces(session, request, response, viewBean);
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
        if ((viewBean instanceof ALDroitViewBean) && "supprimerDroit".equals(getAction().getActionPart())) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".droit.droit.reAfficher&selectedId="
                    + ((ALDroitViewBean) viewBean).getDroitComplexModel().getDroitModel().getId();
        } else if ((viewBean instanceof ALDroitViewBean) && "toformation".equals(getAction().getActionPart())) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".droit.droit.reAfficher&selectedId="
                    + ((ALDroitViewBean) viewBean).getDroitComplexModel().getId();
        } else {
            return super._getDestExecuterEchec(session, request, response, viewBean);
        }
    }

    /**
     * Retourne la destination en cas de succès d'exécution d'une customAction
     * 
     * @param session
     *            Session http
     * @param request
     *            Requête http
     * @param response
     *            Réponse http
     * @param viewBean
     *            Contient le modèle du droit sur lequel la customAction est effectuée
     * @return destination
     * 
     */
    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if ((viewBean instanceof ALDroitViewBean) && "supprimerDroit".equals(getAction().getActionPart())) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossierMain.afficher&selectedId="
                    + ((ALDroitViewBean) viewBean).getDossierComplexModel().getId();
        } else if ((viewBean instanceof ALDroitViewBean) && "toformation".equals(getAction().getActionPart())) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".droit.droit.reAfficher&_method=add&idDossier="
                    + ((ALDroitViewBean) viewBean).getDroitComplexModel().getDroitModel().getIdDossier();

        } else {
            return super._getDestExecuterSucces(session, request, response, viewBean);
        }
    }

    /**
     * Retourne la destination en cas d'échec de modification d'un droit
     * 
     * @param session
     *            Session http
     * @param request
     *            Requête http
     * @param response
     *            Réponse http
     * @param viewBean
     *            Contient le modèle du droit en cours de traitement
     * @return destination
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierEchec (javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     * 
     */
    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        // Quand on a modifié un droit, avec erreur, on reste sur la page pour
        // corriger
        if (viewBean instanceof ALDroitViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=al.droit.droit.reAfficher&selectedId="
                    + ((ALDroitViewBean) viewBean).getDroitComplexModel().getId() + "&_method=upd";

        }
        // Quand on a modifié un enfant, avec erreur, on reste sur la page pour
        // corriger
        else if (viewBean instanceof ALEnfantViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=al.droit.enfant.reAfficher&selectedId="
                    + ((ALEnfantViewBean) viewBean).getId() + "&_method=upd";

        }

        else {
            destination = super._getDestModifierEchec(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Retourne la destination en cas de succès de modification d'un droit
     * 
     * @param session
     *            Session http
     * @param request
     *            Requête http
     * @param response
     *            Réponse http
     * @param viewBean
     *            Contient le modèle du droit en cours de traitement
     * @return destination
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;

        // après modif d'un droit, on retourne à son dossier
        if (viewBean instanceof ALDroitViewBean) {
            // si des avertissement ont été remontés durant la màj du droit, on reste sur l'écran droit pour les
            // afficher via une popup
            JadeBusinessMessage[] messages = (JadeBusinessMessage[]) ((ALDroitViewBean) viewBean).getSession()
                    .getAttribute("addWarnings");
            if (messages != null && messages.length > 0) {
                destination = "/" + getAction().getApplicationPart() + "?userAction="
                        + getAction().getApplicationPart() + ".droit.droit.afficher&selectedId="
                        + ((ALDroitViewBean) viewBean).getDroitComplexModel().getId();
            }

            else {

                destination = "/" + getAction().getApplicationPart() + "?userAction="
                        + getAction().getApplicationPart() + ".dossier.dossierMain.afficher&selectedId="
                        + ((ALDroitViewBean) viewBean).getDroitComplexModel().getDroitModel().getIdDossier();
            }
        }
        // dans le cas d'une modif depuis l'écran on reste sur cet écran
        else if (viewBean instanceof ALEnfantViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".droit.enfant.afficher&selectedId=" + ((ALEnfantViewBean) viewBean).getId();
        }

        else {
            destination = super._getDestModifierSucces(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Retourne la destination en cas de succès de suppression d'un droit
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof ALDroitViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossierMain.afficher" + "&selectedId="
                    + ((ALDroitViewBean) viewBean).getDroitComplexModel().getDroitModel().getIdDossier();
        } else {
            destination = super._getDestSupprimerSucces(session, request, response, viewBean);
        }
        return destination;

    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        super.actionAfficher(session, request, response, mainDispatcher);

        FWViewBeanInterface viewBean = (ALDroitViewBean) request.getAttribute(FWServlet.VIEWBEAN);

        if (viewBean instanceof ALDroitViewBean) {
            request.getSession().setAttribute(
                    globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                    ((ALDroitViewBean) viewBean).getDossierComplexModel().getAllocataireComplexModel()
                            .getAllocataireModel().getIdTiersAllocataire());
        }
    }

    /**
     * Traitement effectué avant d'afficher un droit
     * 
     * @see ch.globaz.al.web.servlet.ALAbstractDefaultAction#beforeAfficher(javax .servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ((viewBean instanceof ALDroitViewBean) || (viewBean instanceof ALEnfantViewBean)) {
            try {
                PaysSearchSimpleModel paysSearch = new PaysSearchSimpleModel();
                paysSearch.setDefinedSearchSize(0);
                request.setAttribute("list_pays", TIBusinessServiceLocator.getAdresseService().findPays(paysSearch));

            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage("Unable to list countries, reason : " + e.toString());
            }
            ParameterModel param = new ParameterModel();
            try {
                param = ParamServiceLocator.getParameterModelService().getParameterByName(ALConstParametres.APPNAME,
                        ALConstParametres.AFFICHAGE_ATTESTATION_ALLOC, JadeDateUtil.getGlobazFormattedDate(new Date()));
            } catch (JadeApplicationServiceNotAvailableException e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage("Unable to find param: " + e.toString());
            } catch (JadeApplicationException e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage("Unable to find param: " + e.toString());
            } catch (JadePersistenceException e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage("Unable to find param: " + e.toString());
            }

            ((ALDroitViewBean) viewBean).setAfficheAttesAlloc(JadeStringUtil.parseBoolean(
                    param.getValeurAlphaParametre(), false));
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    /**
     * Traitement effectué avant de modifier un droit
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeModifier(javax .servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ((viewBean instanceof ALDroitViewBean) || (viewBean instanceof ALEnfantViewBean)) {
            try {
                PaysSearchSimpleModel paysSearch = new PaysSearchSimpleModel();
                paysSearch.setDefinedSearchSize(0);
                request.setAttribute("list_pays", TIBusinessServiceLocator.getAdresseService().findPays(paysSearch));
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage("Unable to list countries, reason : " + e.toString());
            }
        }
        return super.beforeModifier(session, request, response, viewBean);
    }

    /**
     * Traitement effectué avant l'affichage d'un nouveau droit
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALDroitViewBean) {
            if (!JadeStringUtil.isEmpty(request.getParameter("idDossier"))) {
                ((ALDroitViewBean) viewBean).getDroitComplexModel().getDroitModel()
                        .setIdDossier(request.getParameter("idDossier"));

            }
            // setter l'attestation à l'allocataire avec valeur du paramètre Attesation allocataire valeur par défaut
            ParameterModel param = new ParameterModel();
            try {
                param = ParamServiceLocator.getParameterModelService().getParameterByName(ALConstParametres.APPNAME,
                        ALConstParametres.ATTEST_ALLOC_VAL, JadeDateUtil.getGlobazFormattedDate(new Date()));
            } catch (JadeApplicationServiceNotAvailableException e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage("Unable to find param: " + e.toString());
            } catch (JadeApplicationException e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage("Unable to find param: " + e.toString());
            } catch (JadePersistenceException e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage("Unable to find param: " + e.toString());
            }

            ((ALDroitViewBean) viewBean).getDroitComplexModel().getDroitModel()
                    .setAttestationAlloc(JadeStringUtil.parseBoolean(param.getValeurAlphaParametre(), false));
        }
        return super.beforeNouveau(session, request, response, viewBean);

    }
}
