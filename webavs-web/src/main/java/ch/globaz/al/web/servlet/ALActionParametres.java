package ch.globaz.al.web.servlet;

import globaz.al.vb.parametres.ALFormulesViewBean;
import globaz.al.vb.parametres.ALSignetsViewBean;
import globaz.al.vb.parametres.ALTauxMonnaieEtrangereViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.envoi.business.models.parametrageEnvoi.GenererFormule;

/**
 * Classe gérant les actions commencant par al.tauxMonnaieEtrangere
 * 
 * @author PTA
 * 
 */

public class ALActionParametres extends ALAbstractDefaultAction {

    /**
     * @param servlet
     */
    public ALActionParametres(FWServlet servlet) {

        super(servlet);

    }

    /**
     * Retourne la destination en cas d'échec d'ajout d'un taux
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterEchec (javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        // Quand on a ajouté un taux, avec erreur, on remet direct en
        // _method=add et on reste sur la page pour corriger
        if (viewBean instanceof ALTauxMonnaieEtrangereViewBean) {
            destination = "/" + getAction().getApplicationPart()
                    + "?userAction=al.parametres.tauxMonnaieEtrangere.reAfficher" + "&_method=add";
        } else if (viewBean instanceof ALFormulesViewBean) {
            destination = super._getDestAjouterEchec(session, request, response, viewBean);
            destination += "&_method=add";
        } else if (viewBean instanceof ALSignetsViewBean) {
            destination = super._getDestAjouterEchec(session, request, response, viewBean);
            destination += "&_method=add&formuleId=";
            destination += ((ALSignetsViewBean) viewBean).getSignetModel().getFormuleList().getFormule().getId();
        }
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.web.servlet.ALAbstractDefaultAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // -------------------------------------------------------------
        // Ajout d'une formule,
        // on reste sur la page de la formule en question pour permettre
        // un upload en prochaine étape
        // -------------------------------------------------------------
        if (viewBean instanceof ALFormulesViewBean) {
            String destination = "/" + getAction().getApplicationPart();
            destination += "?userAction=al.parametres.formules.afficher&selectedId=";
            destination += ((ALFormulesViewBean) viewBean).getEnvoiTemplate().getEnvoiTemplateSimpleModel()
                    .getIdFormule();
            return destination;
        } else if (viewBean instanceof ALSignetsViewBean) {
            String destination = "/" + getAction().getApplicationPart();
            destination += "?userAction=al.parametres.signets.chercher&searchModel.forIdFormule=";
            destination += ((ALSignetsViewBean) viewBean).getSignetModel().getFormuleList().getFormule().getId();
            return destination;
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierEchec(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = "";
        if (viewBean instanceof ALSignetsViewBean) {
            destination = super._getDestModifierEchec(session, request, response, viewBean);
            destination += "&idFormule=";
            destination += ((ALSignetsViewBean) viewBean).getSignetModel().getFormuleList().getFormule().getId();
        } else {
            destination = super._getDestModifierEchec(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Retourne la destination en cas de modification avec succès
     * 
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces (javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = null;
        if (viewBean instanceof ALTauxMonnaieEtrangereViewBean) {

            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".parametres.tauxMonnaieEtrangere.afficher&selectedId="
                    + ((ALTauxMonnaieEtrangereViewBean) viewBean).getId();
        } else if (viewBean instanceof ALSignetsViewBean) {
            destination = super._getDestModifierSucces(session, request, response, viewBean);
            destination += "&searchModel.forIdFormule=";
            destination += ((ALSignetsViewBean) viewBean).getSignetModel().getFormuleList().getFormule().getId();
        } else {
            destination = super._getDestModifierSucces(session, request, response, viewBean);
        }
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerEchec(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = "";
        if (viewBean instanceof ALSignetsViewBean) {
            destination = super._getDestSupprimerEchec(session, request, response, viewBean);
            destination += "&idFormule=";
            destination += ((ALSignetsViewBean) viewBean).getSignetModel().getFormuleList().getFormule().getId();
        } else {
            destination = super._getDestSupprimerEchec(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Retourne la destination en cas de succès de suppression d'un taux
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof ALTauxMonnaieEtrangereViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".parametres.tauxMonnaieEtrangere.chercher";
        } else if (viewBean instanceof ALSignetsViewBean) {
            destination = super._getDestSupprimerSucces(session, request, response, viewBean);
            destination += "&searchModel.forIdFormule=";
            destination += ((ALSignetsViewBean) viewBean).getSignetModel().getFormuleList().getFormule().getId();
        } else {
            destination = super._getDestSupprimerSucces(session, request, response, viewBean);
        }
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionExporter(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionExporter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // ----------------------------------------------------------
        // Récupération du viewBean
        // ----------------------------------------------------------
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
        if (viewBean == null) {
            viewBean = (FWViewBeanInterface) mainDispatcher.getAttribute("viewBean");
        }
        if (viewBean == null) {
            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);
            viewBean = FWListViewBeanActionFactory.newInstance(newAction, mainDispatcher.getPrefix());
            viewBean.setISession(mainDispatcher.getSession());
        }
        // ----------------------------------------------------------
        // Traitement de l'exportation pour le viewbean
        // de type ALFormulesViewBean
        // ----------------------------------------------------------
        if (viewBean instanceof ALFormulesViewBean) {
            String action = request.getParameter("action");
            String filePahtInput = request.getParameter("filePathInput");
            Boolean bError = false;
            if (action.equals("upload")) {
                // ----------------------------------------------------------
                // Upload d'une formule en blob
                // ----------------------------------------------------------
                GenererFormule genererFormule = new GenererFormule();
                genererFormule.setIdFormule(((ALFormulesViewBean) viewBean).getEnvoiTemplate()
                        .getEnvoiTemplateSimpleModel().getIdFormule());
                genererFormule.setFileName(filePahtInput);
                try {
                    // upload
                    ((ALFormulesViewBean) viewBean).uploadFormule(genererFormule);
                } catch (Exception e) {
                    bError = true;
                }
            } else {
                // ----------------------------------------------------------
                // Récupération de la formule depuis le blob
                // ----------------------------------------------------------
                GenererFormule genererFormule = new GenererFormule();
                genererFormule.setIdFormule(((ALFormulesViewBean) viewBean).getEnvoiTemplate()
                        .getEnvoiTemplateSimpleModel().getIdFormule());
                try {
                    genererFormule = ((ALFormulesViewBean) viewBean).downloadFormule(genererFormule);
                } catch (Exception e) {
                    bError = true;
                }
                if (!bError) {
                    response.setContentType("application/word");
                    String fileName = ((ALFormulesViewBean) viewBean).getEnvoiTemplate().getFormuleList()
                            .getDefinitionformule().getCsDocument();
                    fileName += ".doc";
                    response.setHeader("Content-Disposition", "attachment; filename=" + fileName + "");
                    try {
                        genererFormule.getFichier().write(response.getOutputStream());
                    } catch (Exception e) {
                        bError = true;
                    }
                }
            }
            // ----------------------------------------------------------
            // Set de l'id de la formule pour ré-afficher la page
            // ----------------------------------------------------------
            ((ALFormulesViewBean) viewBean).setId(((ALFormulesViewBean) viewBean).getEnvoiTemplate()
                    .getEnvoiTemplateSimpleModel().getIdFormule());
            if (bError) {
                // ----------------------------------------------------------
                // Redirection pour ré-afficher la page, avec erreur
                // ----------------------------------------------------------
                goSendRedirect(super._getDestAjouterEchec(session, request, response, viewBean), request, response);
            } else if (action.equals("upload")) {
                // ----------------------------------------------------------
                // Redirection pour ré-afficher la page, pas d'erreur
                // ----------------------------------------------------------
                goSendRedirect(_getDestAjouterSucces(session, request, response, viewBean), request, response);
            }

        } else {
            super.actionExporter(session, request, response, mainDispatcher);
        }
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String action = request.getParameter("userAction");

        if ("al.parametres.metier.modify".equals(action.toString())) {
            String url = request.getServletPath();
            if (url.startsWith("/")) {
                url = url.substring(1);
            }
            int servletSeparator = url.lastIndexOf('/');
            if (servletSeparator > -1) {
                url = url.substring(0, servletSeparator + 1);
            } else {
                url = "";
            }
            url += "alRoot/parametres/parametres_metier.html";
            response.sendRedirect(url);
            return;
        } else {
            super.actionModifier(session, request, response, mainDispatcher);
        }
    }

}
