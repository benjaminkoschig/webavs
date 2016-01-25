package ch.globaz.al.web.servlet;

import globaz.al.vb.adi.ALDecompteAdiViewBean;
import globaz.al.vb.adi.ALGenerationDecompteViewBean;
import globaz.al.vb.adi.ALImprimerDecompteViewBean;
import globaz.al.vb.adi.ALSaisieAdiViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe gérant les actions al.decompteAdi
 * 
 * @author GMO
 * 
 */
public class ALActionAdi extends ALAbstractDefaultAction {
    /**
     * 
     * @param servlet
     *            le servlet
     */
    public ALActionAdi(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterEchec (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        // Quand on a ajouté un decompte, avec erreur, on remet direct en
        // _method=add et on reste sur la page pour corriger
        if (viewBean instanceof ALDecompteAdiViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".adi.decompteAdi.reAfficher" + "&_method=add";
        }
        if (viewBean instanceof ALSaisieAdiViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".adi.saisieAdi.reAfficher&_method=add&idDecompte="
                    + ((ALSaisieAdiViewBean) viewBean).getDecompteModel().getIdDecompteAdi();
        }
        if (viewBean instanceof ALGenerationDecompteViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".adi.generationDecompte.reAfficher&_method=add";
        }
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.web.servlet.ALAbstractDefaultAction#_getDestAjouterSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = null;
        if (viewBean instanceof ALDecompteAdiViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossierAdi.afficher&selectedId="
                    + ((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel().getIdDossier();

        }

        if (viewBean instanceof ALGenerationDecompteViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossierMain.afficher&selectedId="
                    + ((ALGenerationDecompteViewBean) viewBean).getDossierModel().getIdDossier()
                    + "&ongletDisplay=prestations";

        }

        if (viewBean instanceof ALSaisieAdiViewBean) {
            destination = "/"
                    + getAction().getApplicationPart()
                    + "?userAction="
                    + getAction().getApplicationPart()
                    + ".adi.saisieAdi.afficher&_method=add&idDecompte="
                    + ((ALSaisieAdiViewBean) viewBean).getAdiSaisieComplexModel().getAdiSaisieModel()
                            .getIdDecompteAdi();

        }

        if (viewBean instanceof ALImprimerDecompteViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".adi.decompteAdi.afficher&selectedId="
                    + ((ALImprimerDecompteViewBean) viewBean).getIdDecompteAdi();

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
        String destination = null;
        if ("supprimerDecompte".equals(getAction().getActionPart()) && (viewBean instanceof ALDecompteAdiViewBean)) {
            destination = "/" + getAction().getApplicationPart()
                    + "?userAction=al.adi.decompteAdi.reAfficher&selectedId="
                    + ((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel().getId();

        }

        if ("supprimerSaisie".equals(getAction().getActionPart()) && (viewBean instanceof ALSaisieAdiViewBean)) {
            destination = "/"
                    + getAction().getApplicationPart()
                    + "?userAction=al.adi.saisieAdi.afficher&_method=add&idDecompte="
                    + ((ALSaisieAdiViewBean) viewBean).getAdiSaisieComplexModel().getAdiSaisieModel()
                            .getIdDecompteAdi();

        }

        return destination;
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
        String destination = super._getDestExecuterSucces(session, request, response, viewBean);
        if ("supprimerDecompte".equals(getAction().getActionPart()) && (viewBean instanceof ALDecompteAdiViewBean)) {
            destination = "/" + getAction().getApplicationPart()
                    + "?userAction=al.dossier.dossierAdi.afficher&selectedId="
                    + ((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel().getIdDossier();

        }
        if ("supprimerSaisie".equals(getAction().getActionPart()) && (viewBean instanceof ALSaisieAdiViewBean)) {
            destination = "/"
                    + getAction().getApplicationPart()
                    + "?userAction=al.adi.saisieAdi.afficher&_method=add&idDecompte="
                    + ((ALSaisieAdiViewBean) viewBean).getAdiSaisieComplexModel().getAdiSaisieModel()
                            .getIdDecompteAdi();
        }

        if ("calculer".equals(getAction().getActionPart()) && (viewBean instanceof ALDecompteAdiViewBean)) {
            destination = "/" + getAction().getApplicationPart()
                    + "?userAction=al.adi.decompteAdi.afficher&selectedId="
                    + ((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel().getIdDecompteAdi();

        }

        if ("generer".equals(getAction().getActionPart()) && (viewBean instanceof ALDecompteAdiViewBean)) {
            destination = "/" + getAction().getApplicationPart()
                    + "?userAction=al.dossier.dossierMain.afficher&selectedId="
                    + ((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel().getIdDossier()
                    + "&ongletDisplay=prestations";
        }
        return destination;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierEchec (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof ALDecompteAdiViewBean) {
            destination = "/" + getAction().getApplicationPart()
                    + "?userAction=al.adi.decompteAdi.reAfficher&selectedId="
                    + ((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel().getId() + "&_method=upd";
        }
        return destination;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof ALDecompteAdiViewBean) {
            destination = "/" + getAction().getApplicationPart()
                    + "?userAction=al.dossier.dossierAdi.afficher&selectedId="
                    + ((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel().getIdDossier();

        }
        return destination;

    }

    @Override
    protected String _getDestSupprimerEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof ALDecompteAdiViewBean) {
            destination = "/" + getAction().getApplicationPart()
                    + "?userAction=al.dossier.dossierAdi.afficher&selectedId="
                    + ((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel().getIdDossier();

        }
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof ALDecompteAdiViewBean) {
            destination = "/" + getAction().getApplicationPart()
                    + "?userAction=al.dossier.dossierAdi.afficher&selectedId="
                    + ((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel().getIdDossier();

        }
        return destination;
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALDecompteAdiViewBean) {
            if (!JadeStringUtil.isEmpty(request.getParameter("idDecompte"))) {
                ((ALDecompteAdiViewBean) viewBean).setId(request.getParameter("idDecompte"));
            }

        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALDecompteAdiViewBean) {
            if (!JadeStringUtil.isEmpty(request.getParameter("idDossier"))) {
                ((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel()
                        .setIdDossier(request.getParameter("idDossier"));

            }

            // Affichage organisme étranger par défaut
            if (JadeStringUtil.isBlank(((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel()
                    .getIdTiersOrganismeEtranger())) {

                String defaultIdTiersOrganisme = "";
                try {
                    defaultIdTiersOrganisme = FWFindParameter.findParameter(((ALDecompteAdiViewBean) viewBean)
                            .getSession().getCurrentThreadTransaction(), "61500001", "DEFORGETR", "0", "0", 0);
                } catch (Exception e) {
                    JadeCodingUtil.catchException(this, " beforeNouveau ", e);
                }

                ((ALDecompteAdiViewBean) viewBean).getDecompteAdiModel().setIdTiersOrganismeEtranger(
                        defaultIdTiersOrganisme);
            }

        }

        if (viewBean instanceof ALSaisieAdiViewBean) {
            if (!JadeStringUtil.isEmpty(request.getParameter("idDecompte"))) {
                ((ALSaisieAdiViewBean) viewBean).getAdiSaisieComplexModel().getAdiSaisieModel()
                        .setIdDecompteAdi(request.getParameter("idDecompte"));

            }
        }

        return super.beforeNouveau(session, request, response, viewBean);
    }

}
