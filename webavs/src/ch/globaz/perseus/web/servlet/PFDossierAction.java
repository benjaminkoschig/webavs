package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.perseus.vb.dossier.PFDossierViewBean;
import globaz.perseus.vb.dossier.PFTableauViewBean;
import globaz.prestation.ged.PRGedAffichageDossier;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Controller des actions du package "perseus.dossier"
 * 
 * @author vyj
 */
public class PFDossierAction extends PFAbstractDefaultServletAction {

    /**
     * @param aServlet
     */
    public PFDossierAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        super.actionAfficher(session, request, response, mainDispatcher);

        FWViewBeanInterface fwViewBean = this.loadViewBean(request);

        if (fwViewBean instanceof PFDossierViewBean) {
            try {
                PFDossierViewBean viewBean = (PFDossierViewBean) fwViewBean;

                request.getSession().setAttribute(
                        globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                        ((PFDossierViewBean) fwViewBean).getDossier().getDemandePrestation().getPersonneEtendue()
                                .getTiers().getIdTiers());

                PFNSSDTO dto = new PFNSSDTO();
                dto.setNSS((viewBean).getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                        .getNumAvsActuel());
                PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            } catch (Exception ex) {
                // Erreur VG
            }
        }
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        try {
            String idDossier = request.getParameter("idDossier");
            PFNSSDTO nssDto = new PFNSSDTO();
            if (!JadeStringUtil.isBlankOrZero(idDossier)) {

                Dossier dossier = PerseusServiceLocator.getDossierService().read(idDossier);

                if (!dossier.isNew()) {

                    nssDto.setNSS(dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                            .getNumAvsActuel());
                    PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, nssDto);
                }
            } else {
                String idTiers = request.getParameter("idTiers");
                String nss = "";
                if (!JadeStringUtil.isBlankOrZero(idTiers)) {
                    PersonneEtendueComplexModel personneEtendueComplexModel = new PersonneEtendueComplexModel();
                    personneEtendueComplexModel = TIBusinessServiceLocator.getPersonneEtendueService().read(idTiers);

                    if (!personneEtendueComplexModel.isNew()) {
                        nss = personneEtendueComplexModel.getPersonneEtendue().getNumAvsActuel();
                    }
                }
                nssDto.setNSS(nss);
                PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, nssDto);
            }
        } catch (Exception e) {
            // On ne fait rien
        }

        super.actionChercher(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        // if ("informationFacture".equals(this.getAction().getActionPart())) {
        // String action = request.getParameter("userAction");
        // FWAction _action = FWAction.newInstance(action);
        // FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(_action, dispatcher.getPrefix());

        // if (viewBean != null) {
        // JSPUtils.setBeanProperties(request, viewBean);
        // }
        // // request.setAttribute(FWServlet.VIEWBEAN, viewBean);
        // session.setAttribute(FWServlet.VIEWBEAN, viewBean);
        //
        // PRGedAffichageDossier.actionAfficherDossierGed(session, request, response, dispatcher, viewBean);

        // }

        if ("actionAfficherDossierGed".equals(getAction().getActionPart())) {
            try {
                // FWViewBeanInterface viewBean = this.loadViewBean(session);
                String action = request.getParameter("userAction");
                FWAction _action = FWAction.newInstance(action);
                FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(_action, dispatcher.getPrefix());

                if (viewBean != null) {
                    JSPUtils.setBeanProperties(request, viewBean);
                }
                // request.setAttribute(FWServlet.VIEWBEAN, viewBean);
                session.setAttribute(FWServlet.VIEWBEAN, viewBean);

                PRGedAffichageDossier.actionAfficherDossierGed(session, request, response, dispatcher, viewBean);

            } catch (Exception e) {
                // A manager le message dans le viewbean
                e.printStackTrace();
                goSendRedirect(FWDefaultServletAction.ERROR_PAGE, request, response);
            }
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }

    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFDossierViewBean) {
            if (!JadeStringUtil.isEmpty(request.getParameter("idDossier"))) {
                ((PFDossierViewBean) viewBean).setId(request.getParameter("idDossier"));
            }
        }

        if (viewBean instanceof PFTableauViewBean) {
            if (!JadeStringUtil.isEmpty(request.getParameter("idDossier"))) {
                ((PFTableauViewBean) viewBean).setId(request.getParameter("idDossier"));
            }
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

}
