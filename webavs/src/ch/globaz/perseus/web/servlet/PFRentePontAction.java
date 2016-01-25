package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.context.JadeThread;
import globaz.perseus.vb.rentepont.PFCreancierRentePontViewBean;
import globaz.perseus.vb.rentepont.PFFactureRentePontViewBean;
import globaz.perseus.vb.rentepont.PFRentePontViewBean;
import globaz.perseus.vb.rentepont.PFSituationfamilialeViewBean;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFRentePontAction extends PFAbstractDefaultServletAction {

    /**
     * @param aServlet
     */
    public PFRentePontAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String urlPlus = "";
        if (viewBean instanceof PFRentePontViewBean) {
            PFRentePontViewBean vb = (PFRentePontViewBean) viewBean;
            urlPlus = "&idDossier=" + vb.getRentePont().getDossier().getDossier().getIdDossier();
        }

        if (viewBean instanceof PFFactureRentePontViewBean) {
            PFFactureRentePontViewBean vb = (PFFactureRentePontViewBean) viewBean;
            // urlPlus = "&idDossier="
            // + vb.getFactureRentePont().getQdRentePont().getDossier().getDossier().getIdDossier();
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart() + "."
                    + getAction().getPackagePart() + ".rentePont.chercher&idDossier="
                    + vb.getFactureRentePont().getQdRentePont().getDossier().getDossier().getIdDossier();
        }

        return super._getDestAjouterSucces(session, request, response, viewBean) + urlPlus;
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFFactureRentePontViewBean) {
            PFFactureRentePontViewBean vb = (PFFactureRentePontViewBean) viewBean;
            // urlPlus = "&idDossier="
            // + vb.getFactureRentePont().getQdRentePont().getDossier().getDossier().getIdDossier();
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart() + "."
                    + getAction().getPackagePart() + ".rentePont.chercher&idDossier="
                    + vb.getFactureRentePont().getQdRentePont().getDossier().getDossier().getIdDossier();
        }

        return getActionFullURL() + ".chercher";
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String urlPlus = "";
        if (viewBean instanceof PFRentePontViewBean) {
            PFRentePontViewBean vb = (PFRentePontViewBean) viewBean;
            urlPlus = "&idDossier=" + vb.getRentePont().getDossier().getDossier().getIdDossier();
        }

        if (viewBean instanceof PFFactureRentePontViewBean) {
            PFFactureRentePontViewBean vb = (PFFactureRentePontViewBean) viewBean;
            urlPlus = "&idDossier="
                    + vb.getFactureRentePont().getQdRentePont().getDossier().getDossier().getIdDossier();
        }

        return super._getDestModifierSucces(session, request, response, viewBean) + urlPlus;
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionAfficher(session, request, response, mainDispatcher);

        FWViewBeanInterface fwViewBean = this.loadViewBean(request);

        if (fwViewBean instanceof PFRentePontViewBean) {
            try {
                PFRentePontViewBean viewBean = (PFRentePontViewBean) fwViewBean;

                request.getSession().setAttribute(
                        globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                        viewBean.getRentePont().getSituationFamiliale().getRequerant().getMembreFamille()
                                .getPersonneEtendue().getTiers().getIdTiers());

                PFNSSDTO dto = new PFNSSDTO();
                dto.setNSS(viewBean.getRentePont().getSituationFamiliale().getRequerant().getMembreFamille()
                        .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());
                PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            } catch (Exception ex) {
                // Erreur VG
            }
        }
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFCreancierRentePontViewBean) {
            PFCreancierRentePontViewBean vb = (PFCreancierRentePontViewBean) viewBean;
            vb.setIdRentePont(request.getParameter("idRentePont"));
        }

        if (viewBean instanceof PFRentePontViewBean) {
            PFRentePontViewBean vb = (PFRentePontViewBean) viewBean;
            String idDossier = request.getParameter("idDossier");

            if ("add".equals(request.getParameter("_method"))) {
                try {
                    vb.getRentePont().setDossier(PerseusServiceLocator.getDossierService().read(idDossier));
                    vb.getRentePont().getSimpleRentePont().setIdDossier(idDossier);
                } catch (Exception e) {
                    JadeThread.logError(PFRentePontAction.class.getName(),
                            "Erreur technique, merci d'envoyer un PrintScreen à Globaz (PFRentePontAction.beforeAfficher) : "
                                    + e.getMessage());
                }
            }
        }
        if (viewBean instanceof PFFactureRentePontViewBean) {
            PFFactureRentePontViewBean vb = (PFFactureRentePontViewBean) viewBean;
            String idDossier = request.getParameter("idDossier");
            String idRentePont = request.getParameter("idRentePont");
            vb.getDossier().setId(idDossier);
            vb.setIdRentePont(idRentePont);
        }

        if (viewBean instanceof PFSituationfamilialeViewBean) {
            if (viewBean instanceof PFSituationfamilialeViewBean) {
                PFSituationfamilialeViewBean vb = (PFSituationfamilialeViewBean) viewBean;
                vb.setIdRentePont(request.getParameter("idRentePont"));
            }
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

}
