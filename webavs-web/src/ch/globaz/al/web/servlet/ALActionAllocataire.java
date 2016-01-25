package ch.globaz.al.web.servlet;

import globaz.al.vb.allocataire.ALAllocataireViewBean;
import globaz.al.vb.allocataire.ALRevenusViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Classe gérant les actions commencant par al.allocataire
 * 
 * @author GMO
 * 
 */
public class ALActionAllocataire extends ALAbstractDefaultAction {

    /**
     * 
     * @param servlet
     */
    public ALActionAllocataire(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Retourne la destination en cas d'échec d'ajout d'un allocataire
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterEchec (javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        // Quand on a ajouté un allocataire, avec erreur, on remet direct en
        // _method=add et on reste sur la page pour corriger
        if (viewBean instanceof ALAllocataireViewBean) {
            String idAllocataire = "";
            if (((ALAllocataireViewBean) viewBean).isAgricoleContext()) {
                idAllocataire = ((ALAllocataireViewBean) viewBean).getAllocataireAgricoleComplexModel()
                        .getAllocataireModel().getIdAllocataire();
            } else {
                idAllocataire = ((ALAllocataireViewBean) viewBean).getAllocataireComplexModel().getAllocataireModel()
                        .getIdAllocataire();
            }
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossierMain.afficher&selectedId=" + request.getParameter("idDossier") + "&_method=add"
                    + "&idAllocataire=" + idAllocataire;

            // Gestion du type d'activité: on reprend le paramètre type
            // activité si défini (permet de remettre l'activité alloc du
            // dossier comme avant édition alloc.
            if (!JadeStringUtil.isEmpty(request.getParameter("typeActivite"))) {
                destination += "&typeActivite=" + request.getParameter("typeActivite");
            }

        } else if (viewBean instanceof ALRevenusViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=al.allocataire.revenus.reAfficher"
                    + "&_method=add" + "&idAllocataire="
                    + ((ALRevenusViewBean) viewBean).getRevenuModel().getIdAllocataire() + "&idDossier="
                    + request.getParameter("idDossier");

        } else {
            destination = super._getDestAjouterEchec(session, request, response, viewBean);
        }

        return destination;
    }

    /**
     * Retourne la destination en cas de succès d'ajout d'un allocataire ou d'un revenu (selon viewBean instance)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces (javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        // Quand on a ajouté un allocataire, on revient dans un nouveau
        // dossier, dans tous les cas
        // car on ne peut pas sauvegarder un dossier sans avoir d'allocataire
        // avant
        if (viewBean instanceof ALAllocataireViewBean) {
            destination = "/"
                    + getAction().getApplicationPart()
                    + "?userAction="
                    + getAction().getApplicationPart()
                    + ".dossier.dossierMain.afficher&selectedId="
                    + request.getParameter("idDossier")
                    + "&_method=add"
                    + "&idAllocataire="
                    + ((ALAllocataireViewBean) viewBean).getAllocataireComplexModel().getAllocataireModel()
                            .getIdAllocataire();

            // Gestion du type d'activité: on reprend le paramètre type
            // activité si défini (permet de remettre l'activité alloc du
            // dossier comme avant édition alloc.
            if (!JadeStringUtil.isEmpty(request.getParameter("typeActivite"))) {
                destination += "&typeActivite=" + request.getParameter("typeActivite");
            }

        } else if (viewBean instanceof ALRevenusViewBean) {
            // on peut ajouter à la chaîne des revenus, donc on reste sur
            // l'écran revenus en mode add
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".allocataire.revenus.afficher" + "&_method=add" + "&_valid=nok" + "&idAllocataire="
                    + ((ALRevenusViewBean) viewBean).getRevenuModel().getIdAllocataire();
            if (!JadeStringUtil.isEmpty(request.getParameter("idDossier"))) {
                destination += "&idDossier=" + request.getParameter("idDossier");
            }

        } else {
            destination = super._getDestAjouterSucces(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Retourne la destination en cas de succès de l'exécution des customAction ( pour le moment que supprimerRevenu),
     * sinon destination standard des customActions
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestExecuterSucces (javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ((viewBean instanceof ALRevenusViewBean) && "supprimerRevenu".equals(getAction().getActionPart())) {

            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".allocataire.revenus.afficher&_method=add&idDossier=" + request.getParameter("idDossier")
                    + "&idAllocataire=" + ((ALRevenusViewBean) viewBean).getRevenuModel().getIdAllocataire();

        } else {
            return super._getDestExecuterSucces(session, request, response, viewBean);
        }
    }

    /**
     * Retourne la destination en cas d'échec de modification d'un allocataire
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierEchec (javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = null;
        if (viewBean instanceof ALAllocataireViewBean) {
            destination = "/" + getAction().getApplicationPart()
                    + "?userAction=al.allocataire.allocataire.reAfficher&selectedId="
                    + ((ALAllocataireViewBean) viewBean).getAllocataireComplexModel().getId() + "&_method=upd";
            // on réplique le paramètre fromPage si il existe, afin de ne pas
            // avoir le même successretour
            // si on est sur la page allocataire depuis recherche personne ou
            // depuis écran dossier
            if (!JadeStringUtil.isEmpty(request.getParameter("fromPage"))) {
                destination += "&fromPage=" + request.getParameter("fromPage");
            }

            // pour le menu retour au dossier, on réplique le paramètre
            // iddossier si il existe
            if (!JadeStringUtil.isEmpty(request.getParameter("idDossier"))) {
                destination += "&idDossier=" + request.getParameter("idDossier");
            }
            // Gestion du type d'activité: on reprend le paramètre type
            // activité si défini (permet de remettre l'activité alloc du
            // dossier comme avant édition alloc.
            if (!JadeStringUtil.isEmpty(request.getParameter("typeActivite"))) {
                destination += "&typeActivite=" + request.getParameter("typeActivite");
            }

        }
        // normalement on entre jamais ici car tjrs en mode nouveau...
        else if (viewBean instanceof ALRevenusViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + request.getParameter("userAction")
                    + "&_method=add";

        } else {
            destination = super._getDestModifierEchec(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Retourne la destination en cas de succès de modification d'un allocataire. Retour différent si provenance page de
     * recherche personnes ou dossier existant.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces (javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof ALAllocataireViewBean) {
            // si on est arrivé sur cette page depuis la page de recherche
            // personne, on reste sur la page allocataire après la modif
            if (!JadeStringUtil.isEmpty(request.getParameter("fromPage"))
                    && request.getParameter("fromPage").equals("personneAF.chercher")) {

                destination = "/" + getAction().getApplicationPart() + "?userAction="
                        + getAction().getApplicationPart() + ".allocataire.allocataire.afficher&selectedId="
                        + ((ALAllocataireViewBean) viewBean).getId() + "&fromPage=" + request.getParameter("fromPage");

            } else { // sinon on retourne au dossier voulu
                destination = "/"
                        + getAction().getApplicationPart()
                        + "?userAction="
                        + getAction().getApplicationPart()
                        + ".dossier.dossierMain.afficher&selectedId="
                        + request.getParameter("idDossier")
                        + "&idAllocataire="
                        + ((ALAllocataireViewBean) viewBean).getAllocataireComplexModel().getAllocataireModel()
                                .getIdAllocataire();
                // Si on a déjà enregistré l'allocataire, et qu'on le modifie
                // après coup, sans avoir encore enregistré le dossier, on
                // retourne dans un nouveau dossier, sinon dans dossier existant
                // mais à sauvegarder
                if (!JadeStringUtil.isEmpty(request.getParameter("dossierIsNew"))
                        && request.getParameter("dossierIsNew").equals("true")) {
                    destination += "&_method=add";

                } else {
                    destination += "&_method=upd";
                }

                // Gestion du type d'activité: on reprend le paramètre type
                // activité si défini (permet de remettre l'activité alloc du
                // dossier comme avant édition alloc.
                if (!JadeStringUtil.isEmpty(request.getParameter("typeActivite"))) {
                    destination += "&typeActivite=" + request.getParameter("typeActivite");
                }

            }

        } else {
            destination = super._getDestModifierSucces(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Retourne la destination en cas de succès de suppression d'allocataire. Pas appelé , car l'allocataire ne peut
     * être supprimé manuellement pour le moment, uniquement en cascade via suppression dossier.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof ALAllocataireViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".personne.personneAF.chercher";

        } else {
            destination = super._getDestSupprimerSucces(session, request, response, viewBean);
        }
        return destination;
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        super.actionAfficher(session, request, response, mainDispatcher);

        FWViewBeanInterface viewBean = (FWViewBeanInterface) request.getAttribute(FWServlet.VIEWBEAN);

        if (viewBean instanceof ALAllocataireViewBean) {
            request.getSession().setAttribute(
                    globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                    ((ALAllocataireViewBean) viewBean).getAllocataireComplexModel().getAllocataireModel()
                            .getIdTiersAllocataire());
        }

        if (viewBean instanceof ALRevenusViewBean) {
            request.getSession().setAttribute(
                    globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                    ((ALRevenusViewBean) viewBean).getAllocataireComplexModel().getAllocataireModel()
                            .getIdTiersAllocataire());
        }
    }

    /**
     * Traitement effectué avant d'afficher le détail d'un allocataire ( Chargement liste des pays proposés) ou de la
     * liste des revenus (définition idAllocataire et date actuelle pour les revenus actifs)
     * 
     * @see ch.globaz.al.web.servlet.ALAbstractDefaultAction#beforeAfficher(javax .servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALAllocataireViewBean) {

            try {
                // si le paramètre n'est pas définie, on laisse tel quel (soit
                // depuis reAffichage, soit nouveau alloc)
                if (!JadeStringUtil.isEmpty(request.getParameter("typeActivite"))) {
                    ((ALAllocataireViewBean) viewBean).setAgricoleContext(ALServiceLocator.getDossierBusinessService()
                            .isAgricole(request.getParameter("typeActivite")));
                }
                PaysSearchSimpleModel paysSearch = new PaysSearchSimpleModel();
                paysSearch.setDefinedSearchSize(0);
                request.setAttribute("list_pays", TIBusinessServiceLocator.getAdresseService().findPays(paysSearch));

                ((ALAllocataireViewBean) viewBean).setIdDossier(request.getParameter("idDossier"));

            } catch (JadeApplicationException e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage("Unable to check if it's agricole context, reason :" + e.toString());
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage("Unable to list countries, reason : " + e.toString());
            }

        }
        if (viewBean instanceof ALRevenusViewBean) {
            // Avant de saisir un revenu, on doit l'informer, qui est son
            // proprio
            if (!JadeStringUtil.isEmpty(request.getParameter("idAllocataire"))) {

                ((ALRevenusViewBean) viewBean).getRevenuModel().setIdAllocataire(request.getParameter("idAllocataire"));

                ((ALRevenusViewBean) viewBean).getRevenuSearchModel().setForIdAllocataire(
                        request.getParameter("idAllocataire"));

            }

            String currentDate = JadeDateUtil.getGlobazFormattedDate(new Date());
            ((ALRevenusViewBean) viewBean).getRevenuModel().setDate(currentDate);

        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    /**
     * Traitement effectué avant de faire une modification sur l'allocataire.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeModifier(javax .servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALAllocataireViewBean) {
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
     * Traitement effectué avant d'entrer sur l'écran nouvel allocataire (récup et insertion du NSS dans le modèle
     * allocataireComplex si nécessaire)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax .servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof ALAllocataireViewBean) {
            // On arrive sur l'écran nouveau allocataire avec le NSS récupéré
            // depuis la
            // saisie de l'écran AL0004
            if (!JadeStringUtil.isEmpty(request.getParameter("numAvsActuel"))) {
                ((ALAllocataireViewBean) viewBean).getAllocataireComplexModel().getPersonneEtendueComplexModel()
                        .getPersonneEtendue().setNumAvsActuel(request.getParameter("numAvsActuel"));
            }
        }

        return super.beforeNouveau(session, request, response, viewBean);
    }
}
