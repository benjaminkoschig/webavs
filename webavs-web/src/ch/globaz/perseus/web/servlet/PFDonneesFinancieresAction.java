package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.vb.donneesfinancieres.PFAbstractDonneesFinancieresViewBean;
import globaz.perseus.vb.donneesfinancieres.PFDonneefinanciereViewBean;
import globaz.perseus.vb.donneesfinancieres.PFEnfantDFViewBean;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.Conjoint;
import ch.globaz.perseus.business.models.situationfamille.Enfant;
import ch.globaz.perseus.business.models.situationfamille.Requerant;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFDonneesFinancieresAction extends PFAbstractDefaultServletAction {

    public PFDonneesFinancieresAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PFDonneefinanciereViewBean) {

            PFDonneefinanciereViewBean vb = (PFDonneefinanciereViewBean) viewBean;
            String parametres = "&idDemande=" + vb.getDemande().getId();
            if (!JadeStringUtil.isEmpty(vb.getIdRequerant())) {
                parametres += "&idRequerant=" + vb.getIdRequerant();
            } else if (!JadeStringUtil.isEmpty(vb.getIdConjoint())) {
                parametres += "&idConjoint=" + vb.getIdConjoint();
            }
            return getActionFullURL() + ".afficher" + parametres;

        } else if (viewBean instanceof PFEnfantDFViewBean) {

            PFEnfantDFViewBean vb = (PFEnfantDFViewBean) viewBean;
            String parametres = "&idDemande=" + vb.getDemande().getId();
            if (!JadeStringUtil.isEmpty(vb.getIdEnfant())) {
                parametres += "&idEnfant=" + vb.getIdEnfant();
            }
            return getActionFullURL() + ".afficher" + parametres;

        }

        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        PFAbstractDonneesFinancieresViewBean vb = (PFAbstractDonneesFinancieresViewBean) viewBean;
        viewBean = super.beforeAfficher(session, request, response, viewBean);

        String idDemande = request.getParameter("idDemande");
        String idConjoint = request.getParameter("idConjoint");
        String idRequerant = request.getParameter("idRequerant");
        String idEnfant = request.getParameter("idEnfant");

        if (viewBean instanceof PFDonneefinanciereViewBean) {
            ((PFDonneefinanciereViewBean) vb).setIdRequerant(idRequerant);
            ((PFDonneefinanciereViewBean) vb).setIdConjoint(idConjoint);
        } else if (viewBean instanceof PFEnfantDFViewBean) {
            ((PFEnfantDFViewBean) vb).setIdEnfant(idEnfant);
        }

        try {
            // Lecture de la demande
            if (!JadeStringUtil.isEmpty(idDemande)) {
                vb.setDemande(PerseusServiceLocator.getDemandeService().read(idDemande));
            } else {
                JadeThread.logError(PFDonneefinanciereViewBean.class.getName(),
                        "perseus.pfdonneesfinancieresAction.donneesfinancieres.nodemande");
            }

            if (!JadeStringUtil.isEmpty(idConjoint)) {
                // Si il s'agit d'un conjoint
                Conjoint conjoint = PerseusServiceLocator.getConjointService().read(idConjoint);
                if (!conjoint.isNew()) {
                    vb.setMembreFamille(conjoint.getMembreFamille());
                } else {
                    JadeThread.logError(PFDonneefinanciereViewBean.class.getName(),
                            "perseus.pfdonneesfinancieresAction.donneesfinancieres.falseconjoint");
                }
            } else if (!JadeStringUtil.isEmpty(idRequerant)) {
                // Si il s'agit d'un requérant
                Requerant requerant = PerseusServiceLocator.getRequerantService().read(idRequerant);
                if (!requerant.isNew()) {
                    vb.setMembreFamille(requerant.getMembreFamille());
                } else {
                    JadeThread.logError(PFDonneefinanciereViewBean.class.getName(),
                            "perseus.pfdonneesfinancieresAction.donneesfinancieres.falserequerant");
                }
            } else if (!JadeStringUtil.isEmpty(idEnfant)) {
                // Si il s'agit d'un enfant
                Enfant enfant = PerseusServiceLocator.getEnfantService().read(idEnfant);
                if (!enfant.isNew()) {
                    vb.setMembreFamille(enfant.getMembreFamille());
                } else {
                    JadeThread.logError(PFDonneefinanciereViewBean.class.getName(),
                            "perseus.pfdonneesfinancieresAction.donneesfinancieres.falseenfant");
                }
            } else {
                // Erreur si c'est ni un conjoint ni un requerant et ni un enfant
                JadeThread.logError(PFDonneefinanciereViewBean.class.getName(),
                        "perseus.pfdonneesfinancieresAction.donneesfinancieres.noreqnoconj");
            }

        } catch (SituationFamilleException e) {
            JadeThread.logError(PFDonneefinanciereViewBean.class.getName(), e.getMessage());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeThread.logError(PFDonneefinanciereViewBean.class.getName(), e.getMessage());
        } catch (JadePersistenceException e) {
            JadeThread.logError(PFDonneefinanciereViewBean.class.getName(), e.getMessage());
        } catch (DemandeException e) {
            JadeThread.logError(PFDonneefinanciereViewBean.class.getName(), e.getMessage());
        }

        return viewBean;

    }

    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        // Si les checkbox ne sont pas checkés setté à vide les listes si non elle garde leur ancienne valeur
        if (viewBean instanceof PFDonneefinanciereViewBean) {
            PFDonneefinanciereViewBean vb = (PFDonneefinanciereViewBean) viewBean;
            if (request.getParameter("totalRentes.listCsTypeRentes") == null) {
                vb.getTotalRentes().setListCsTypeRentes(new ArrayList<String>());
            }

            return super.beforeModifier(session, request, response, vb);
        }

        return super.beforeModifier(session, request, response, viewBean);
    }

}
