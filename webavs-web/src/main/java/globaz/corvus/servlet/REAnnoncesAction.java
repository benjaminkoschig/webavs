/*
 * Créé le 6 août 07
 */
package globaz.corvus.servlet;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
import globaz.corvus.vb.annonces.REAnnoncesAbstractLevel1AViewBean;
import globaz.corvus.vb.annonces.REAnnoncesAugmentationModification10EmeViewBean;
import globaz.corvus.vb.annonces.REAnnoncesAugmentationModification9EmeViewBean;
import globaz.corvus.vb.annonces.REAnnoncesDiminution10EmeViewBean;
import globaz.corvus.vb.annonces.REAnnoncesDiminution9EmeViewBean;
import globaz.corvus.vb.annonces.REAnnoncesRenteListeViewBean;
import globaz.corvus.vb.demandes.REDemandeParametresRCDTO;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author HPE
 * 
 */
public class REAnnoncesAction extends PRDefaultAction {

    public REAnnoncesAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        String destination = null;
        String method = request.getParameter("_method");
        String selectedId = request.getParameter("selectedId");
        String idRenteAccordee = request.getParameter("idRenteAccordee");

        try {

            FWViewBeanInterface viewBean = new REAnnoncesDiminution9Eme();

            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {

                // creation du viewBean en fonction du type d'annonce
                String typeAnnonce = request.getParameter("typeAnnonce");

                if ("annonceDiminution10Eme".equals(typeAnnonce)) {
                    action = FWAction.newInstance("corvus.annonces.annonceDiminution10Eme.afficher");
                    viewBean = new REAnnoncesDiminution10EmeViewBean();
                    ((REAnnoncesDiminution10EmeViewBean) viewBean).setIdRenteAccordee(idRenteAccordee);
                } else if ("annonceDiminution9Eme".equals(typeAnnonce)) {
                    action = FWAction.newInstance("corvus.annonces.annonceDiminution9Eme.afficher");
                    viewBean = new REAnnoncesDiminution9EmeViewBean();
                    ((REAnnoncesDiminution9EmeViewBean) viewBean).setIdRenteAccordee(idRenteAccordee);
                } else if ("annonceAugmentation10Eme".equals(typeAnnonce)) {
                    action = FWAction.newInstance("corvus.annonces.annonceAugmentation10Eme.afficher");
                    viewBean = new REAnnoncesAugmentationModification10EmeViewBean();
                    ((REAnnoncesAugmentationModification10EmeViewBean) viewBean).setIdRenteAccordee(idRenteAccordee);
                } else if ("annonceAugmentation9Eme".equals(typeAnnonce)) {
                    action = FWAction.newInstance("corvus.annonces.annonceAugmentation9Eme.afficher");
                    viewBean = new REAnnoncesAugmentationModification9EmeViewBean();
                    ((REAnnoncesAugmentationModification9EmeViewBean) viewBean).setIdRenteAccordee(idRenteAccordee);
                } else {
                    viewBean = new REAnnoncesAbstractLevel1AViewBean();
                    ((REAnnoncesAbstractLevel1AViewBean) viewBean).setIdRenteAccordee(idRenteAccordee);
                }

                action.changeActionPart(FWAction.ACTION_NOUVEAU);
                viewBean.setISession(mainDispatcher.getSession());

            } else {
                // suivant le type d'annonce, on redirigera vers telle ou telle
                // page avec tel ou tel viewBean
                String codeApplication = request.getParameter("typeAnnonce");

                if (JadeStringUtil.isEmpty(codeApplication)) {

                    selectedId = request.getParameter("selectedId");

                    REAnnonceHeader annonce = new REAnnonceHeader();
                    annonce.setSession((BSession) mainDispatcher.getSession());
                    annonce.setIdAnnonce(selectedId);
                    annonce.retrieve();

                    codeApplication = annonce.getCodeApplication();

                }

                // on fait l'action en fonction du type d'annonces qu'on veut
                // afficher
                if (codeApplication.equals("45")) {
                    action = FWAction.newInstance("corvus.annonces.annonceDiminution10Eme.afficher");
                    viewBean = new REAnnoncesDiminution10EmeViewBean();
                } else if (codeApplication.equals("42")) {
                    action = FWAction.newInstance("corvus.annonces.annonceDiminution9Eme.afficher");
                    viewBean = new REAnnoncesDiminution9EmeViewBean();
                } else if (codeApplication.equals("44") || codeApplication.equals("46")) {
                    action = FWAction.newInstance("corvus.annonces.annonceAugmentation10Eme.afficher");
                    viewBean = new REAnnoncesAugmentationModification10EmeViewBean();
                } else if (codeApplication.equals("41") || codeApplication.equals("43")) {
                    action = FWAction.newInstance("corvus.annonces.annonceAugmentation9Eme.afficher");
                    viewBean = new REAnnoncesAugmentationModification9EmeViewBean();
                }

            }

            ((REAnnonceHeader) viewBean).setIdAnnonce(selectedId);

            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.removeAttribute(FWServlet.VIEWBEAN);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                    destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
                } else {
                    if (JadeStringUtil.isIntegerEmpty(((REAnnonceHeader) viewBean).getIdAnnonce())) {
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        viewBean.setMessage(((BSession) viewBean.getISession()).getLabel("ANNONCE_INEXISTANTE"));
                    }

                    destination = getRelativeURLwithoutClassPart(request, session) + action.getClassPart() + "_de.jsp";
                }
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    public void actionAjouterAnnonce(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        String _destination = "";

        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                _destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                _destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), null);

        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        String idTierRequerant = request.getParameter("idTierRequerant");

        if ((null != idTierRequerant) && !JadeStringUtil.isEmpty(idTierRequerant)) {
            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(getNumeroAvsFormate(idTierRequerant, (BSession) mainDispatcher.getSession()));
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);
            session.setAttribute("idTierRequerant", "");
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {
            REAnnoncesRenteListeViewBean viewBean = new REAnnoncesRenteListeViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setHasAvsHistory(true);

            String rechercheFamille = request.getParameter("rechercheFamille");
            if (!JadeStringUtil.isBlank(rechercheFamille) && Boolean.getBoolean(rechercheFamille)) {
                viewBean.setIsRechercheFamille(true);
            }

            viewBean = (REAnnoncesRenteListeViewBean) beforeLister(session, request, response, viewBean);
            viewBean = (REAnnoncesRenteListeViewBean) mainDispatcher.dispatch(viewBean, getAction());
            request.setAttribute("viewBean", viewBean);

            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);

            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    public void actionModifierAnnonce(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        String _destination = "";

        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                _destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                _destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);
    }

    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        FWViewBeanInterface vb = (FWViewBeanInterface) session.getAttribute("viewBean");
        String validFail = "?_valid=fail";
        if (vb instanceof REAnnoncesAugmentationModification10EmeViewBean) {
            String url = getRelativeURL(request, session) + "Augmentation10Eme_de.jsp" + validFail;

            servlet.getServletContext().getRequestDispatcher(url).forward(request, response);
        } else if (vb instanceof REAnnoncesAugmentationModification9EmeViewBean) {
            String url = getRelativeURL(request, session) + "Augmentation9Eme_de.jsp" + validFail;

            servlet.getServletContext().getRequestDispatcher(url).forward(request, response);
        } else if (vb instanceof REAnnoncesDiminution10EmeViewBean) {
            String url = getRelativeURL(request, session) + "Diminution10Eme_de.jsp" + validFail;

            servlet.getServletContext().getRequestDispatcher(url).forward(request, response);
        } else if (vb instanceof REAnnoncesDiminution9EmeViewBean) {
            String url = getRelativeURL(request, session) + "Diminution9Eme_de.jsp" + validFail;

            servlet.getServletContext().getRequestDispatcher(url).forward(request, response);
        } else {
            servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.ERROR_PAGE + validFail)
                    .forward(request, response);
        }

    }

    public void actionSupprimerAnnonce(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        String _destination = "";

        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                _destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                _destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        RENSSDTO dtoNss = new RENSSDTO();

        dtoNss.setNSS(request.getParameter("likeNumeroAVS"));
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dtoNss);

        REAnnoncesRenteListeViewBean listViewBean = (REAnnoncesRenteListeViewBean) viewBean;

        REDemandeParametresRCDTO dto = new REDemandeParametresRCDTO();

        dto.setIdRenteAccordee(listViewBean.getForNoRenteAccordee());
        dto.setLikeNom(listViewBean.getLikeNom());
        dto.setLikePrenom(listViewBean.getLikePrenom());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO, dto);

        return super.beforeLister(session, request, response, viewBean);

    }

    private String getNumeroAvsFormate(String idTierRequerant, BSession session) {

        String result = "";

        if (!JadeStringUtil.isIntegerEmpty(idTierRequerant)) {

            PRTiersWrapper tiers;
            try {
                tiers = PRTiersHelper.getTiersParId(session, idTierRequerant);
                String nnss = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                result = NSUtil.formatWithoutPrefixe(nnss, nnss.length() > 14 ? true : false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
