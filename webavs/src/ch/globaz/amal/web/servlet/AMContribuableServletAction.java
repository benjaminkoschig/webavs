package ch.globaz.amal.web.servlet;

import globaz.amal.vb.contribuable.AMContribuableComplexAnnonceSedexViewBean;
import globaz.amal.vb.contribuable.AMContribuableFamilleViewBean;
import globaz.amal.vb.contribuable.AMContribuableHistoriqueFamilleViewBean;
import globaz.amal.vb.contribuable.AMContribuableHistoriqueViewBean;
import globaz.amal.vb.contribuable.AMContribuableRevenuViewBean;
import globaz.amal.vb.contribuable.AMContribuableTaxationsViewBean;
import globaz.amal.vb.contribuable.AMContribuableViewBean;
import globaz.amal.vb.contribuable.AMNSSDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMContribuableServletAction extends AMAbstractServletAction {

    public AMContribuableServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // AMContribuableViewBean contribuableViewBean = (AMContribuableViewBean) viewBean;
        String destination = super._getDestAjouterEchec(session, request, response, viewBean);
        destination += "&_method=add";
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces (HttpSession
     * session,HttpServletRequest request, HttpServletResponse response, FWViewBeanInterface viewBean)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = "";
        AMContribuableViewBean contribuableViewBean = (AMContribuableViewBean) viewBean;
        if (!contribuableViewBean.getContribuable().getContribuable().isNew()) {
            destination = "/amal?userAction=amal.contribuable.contribuable.afficher&selectedId=";
            destination += contribuableViewBean.getContribuable().getContribuable().getIdContribuable();

            if ("o".equals(session.getAttribute("isTransfert"))) {
                try {
                    session.removeAttribute("isTransfert");

                    // suppression du contribuable dans l'historique
                    AMContribuableHistoriqueViewBean hist = new AMContribuableHistoriqueViewBean();
                    hist.setId(contribuableViewBean.getContribuableInfos().getIdContribuable());
                    hist.setSimpleContribuableInfos(AmalServiceLocator.getContribuableService().readInfos(hist.getId()));

                    AmalServiceLocator.getContribuableService().deleteInfo(hist.getSimpleContribuableInfos());
                    // hist.delete();
                } catch (Exception e) {
                    return FWDefaultServletAction.ERROR_PAGE;
                }
            }
        } else if (!(contribuableViewBean.getContribuableInfos() == null)
                && !contribuableViewBean.getContribuableInfos().isNew()) {
            destination = "/amal?userAction=amal.contribuable.contribuable.afficher&_method=add";
        }
        return destination;
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        super.actionAfficher(session, request, response, mainDispatcher);

        FWViewBeanInterface fwViewBean = this.loadViewBean(request);

        if (fwViewBean instanceof AMContribuableViewBean) {
            try {
                AMContribuableViewBean viewBean = (AMContribuableViewBean) fwViewBean;

                request.getSession().setAttribute(
                        globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                        ((AMContribuableViewBean) fwViewBean).getContribuable().getPersonneEtendue().getTiers()
                                .getIdTiers());

                AMNSSDTO dto = new AMNSSDTO();
                dto.setNSS((viewBean).getPersonneEtendueContribuable().getPersonneEtendue().getNumAvsActuel());
                PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            } catch (Exception ex) {
                // Erreur VG
            }
        }
    }

    @Override
    protected void actionAfficherAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionAfficherAJAX(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        AMNSSDTO nssDto = new AMNSSDTO();
        if (!JadeStringUtil.isBlankOrZero(request.getParameter("noAVS"))) {
            nssDto.setNSS(request.getParameter("noAVS"));
        } else {
            nssDto.setNSS("");
        }

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, nssDto);

        super.actionChercher(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        if (!(session.getAttribute("viewBean") == null) && !(session.getAttribute("message") == null)
                && !(session.getAttribute("msgType") == null)) {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            viewBean.setMessage((String) session.getAttribute("message"));
            viewBean.setMsgType((String) session.getAttribute("msgType"));
            session.removeAttribute("viewBean");
            session.removeAttribute("message");
            session.removeAttribute("msgType");
            session.setAttribute("viewBean", viewBean);
        }
        super.actionReAfficher(session, request, response, mainDispatcher);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if ((viewBean instanceof AMContribuableTaxationsViewBean) || (viewBean instanceof AMContribuableRevenuViewBean)
                || (viewBean instanceof AMContribuableFamilleViewBean)
                || (viewBean instanceof AMContribuableHistoriqueFamilleViewBean)
                || (viewBean instanceof AMContribuableComplexAnnonceSedexViewBean)) {
            AMContribuableViewBean vbContribuable = (AMContribuableViewBean) session.getAttribute("viewBean");
            viewBean = vbContribuable;
        }

        // String selectedTabId = request.getParameter("selectedTabId");
        // if (JadeStringUtil.isBlank(selectedTabId)) {
        // selectedTabId = "0";
        // }
        // // Récupération des infos du viewBean principal pour l'utilisation dans les "sous-viewbean"
        // if ((viewBean instanceof AMContribuableHistoriqueFamilleViewBean) || !"0".equals(selectedTabId)) {
        // AMContribuableViewBean vbContribuable = (AMContribuableViewBean) session.getAttribute("viewBean");
        // viewBean = vbContribuable;
        // }
        //
        // if (viewBean instanceof AMContribuableFamilleViewBean) {
        // AMContribuableViewBean vbContribuable = (AMContribuableViewBean) session.getAttribute("viewBean");
        // ((AMContribuableFamilleViewBean) viewBean).getContribuable()
        // .setId(vbContribuable.getContribuable().getId());
        // ((AMContribuableFamilleViewBean) viewBean).setListeFamilleContribuableView(vbContribuable
        // .getListeFamilleContribuableView());
        // ((AMContribuableFamilleViewBean) viewBean).setListeFamilleContribuableViewAnnee(vbContribuable
        // .getListeFamilleContribuableViewAnnee());
        // ((AMContribuableFamilleViewBean) viewBean).setListeFamilleContribuableViewMember(vbContribuable
        // .getListeFamilleContribuableViewMember());
        // ((AMContribuableFamilleViewBean) viewBean).setListeFamille(vbContribuable.getListeFamille());
        // }
        //
        // if (viewBean instanceof AMContribuableRevenuViewBean) {
        // AMContribuableViewBean vbContribuable = (AMContribuableViewBean) session.getAttribute("viewBean");
        // ((AMContribuableRevenuViewBean) viewBean).getContribuable().setId(vbContribuable.getContribuable().getId());
        // ((AMContribuableRevenuViewBean) viewBean).setListeFamilleContribuableViewAnnee(vbContribuable
        // .getListeFamilleContribuableViewAnnee());
        // ((AMContribuableRevenuViewBean) viewBean).setRevenusContribuable(vbContribuable.getRevenusContribuable());
        // ((AMContribuableRevenuViewBean) viewBean).setRevenusHistoriquesContribuable(vbContribuable
        // .getRevenusHistoriquesContribuable());
        // }
        //
        // if (viewBean instanceof AMContribuableTaxationsViewBean) {
        // AMContribuableViewBean vbContribuable = (AMContribuableViewBean) session.getAttribute("viewBean");
        // ((AMContribuableTaxationsViewBean) viewBean).getContribuable().setId(
        // vbContribuable.getContribuable().getId());
        // ((AMContribuableTaxationsViewBean) viewBean)
        // .setRevenusContribuable(vbContribuable.getRevenusContribuable());
        // ((AMContribuableTaxationsViewBean) viewBean).setRevenusHistoriquesContribuable(vbContribuable
        // .getRevenusHistoriquesContribuable());
        // }

        // Gestion du transfert d'un contribuable
        boolean modeTransfert = (!(request.getParameter("mode") == null)
                && "transfert".equals(request.getParameter("mode")) && !(request.getParameter("selectedId") == null));

        // Si on est en mode transfert, on récupère les infos contribuables pour pré-remplir la page "_de.jsp"
        if (modeTransfert) {
            session.setAttribute("isTransfert", "o");
            AMContribuableHistoriqueViewBean hist = new AMContribuableHistoriqueViewBean();
            hist.setId(request.getParameter("selectedId"));
            try {
                hist.retrieve();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }

            AMContribuableViewBean contribuableViewBean = (AMContribuableViewBean) viewBean;
            contribuableViewBean.getContribuable().setIsContribuableHistorique(true);
            contribuableViewBean.setContribuableInfos(hist.getContribuableHistoriqueRCListe().getContribuableInfos());
        }

        if (viewBean instanceof AMContribuableViewBean) {
            String ndc = request.getParameter("ndc");
            AMContribuableViewBean cv = (AMContribuableViewBean) viewBean;
            cv.getContribuable().getContribuable().setNoContribuable(ndc);
            viewBean = cv;
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AMContribuableViewBean contribuableViewBean = (AMContribuableViewBean) viewBean;

        if (!(request.getParameter("addAdresse") == null) && "1".equals(request.getParameter("addAdresse"))) {
            contribuableViewBean.setId(contribuableViewBean.getContribuableInfos().getIdContribuable());
            contribuableViewBean.getContribuable().getAdresseComplexModel().getLocalite()
                    .setNumPostal(request.getParameter("npa"));
            contribuableViewBean.getContribuable().getAdresseComplexModel().getAdresse()
                    .setRue(request.getParameter("rue"));
            contribuableViewBean.getContribuable().getAdresseComplexModel().getAdresse()
                    .setNumeroRue(request.getParameter("noRue"));
            contribuableViewBean.getContribuable().getAdresseComplexModel().getAdresse()
                    .setCasePostale(request.getParameter("casepostal"));
            contribuableViewBean.getContribuable().getAdresseComplexModel().getAdresse()
                    .setLigneAdresse1(request.getParameter("adresse1"));
            contribuableViewBean.getContribuable().getAdresseComplexModel().getAdresse()
                    .setLigneAdresse2(request.getParameter("adresse2"));
            contribuableViewBean.getContribuable().getAdresseComplexModel().getAdresse()
                    .setLigneAdresse3(request.getParameter("adresse3"));
        }
        return super.beforeAjouter(session, request, response, viewBean);
    }

    public String fusionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        String destination = "";
        try {
            if (viewBean instanceof AMContribuableViewBean) {
                AMContribuableViewBean contribuableViewBean = (AMContribuableViewBean) viewBean;
                String idDossierAFusionner = request.getParameter("idDossierAFusionner");
                String idDossierBase = contribuableViewBean.getContribuable().getId();
                contribuableViewBean.fusionDossier(idDossierBase, idDossierAFusionner);
                destination = "/amal?userAction=amal.contribuable.contribuable.afficher&selectedId=";
                destination += idDossierBase;
            } else {
                destination = FWDefaultServletAction.ERROR_PAGE;
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        return destination;
    }

    public String transferer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        String destination = "";
        destination = "/amal?userAction=amal.contribuable.contribuable.afficher&mode=transfert&_method=add&selectedId=";
        destination += request.getParameter("selectedId");

        return destination;
    }
}
