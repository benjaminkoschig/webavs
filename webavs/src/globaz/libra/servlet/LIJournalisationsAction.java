package globaz.libra.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.libra.utils.LIRecherchesDTO;
import globaz.libra.vb.journalisations.LIDocumentsExecutionViewBean;
import globaz.libra.vb.journalisations.LIEcheancesDetailViewBean;
import globaz.libra.vb.journalisations.LIEcheancesDetailViewBean.LIEcheanceMultipleObject;
import globaz.libra.vb.journalisations.LIEcheancesJointDossiersListViewBean;
import globaz.libra.vb.journalisations.LIEcheancesJointDossiersViewBean;
import globaz.libra.vb.journalisations.LIJournalisationsDetailViewBean;
import globaz.libra.vb.journalisations.LIJournalisationsJointDossiersListViewBean;
import globaz.libra.vb.journalisations.LIJournalisationsJointDossiersViewBean;
import globaz.prestation.ged.PRGedAffichageDossier;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author HPE
 * 
 */
public class LIJournalisationsAction extends LIDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public LIJournalisationsAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof LIEcheancesDetailViewBean) {
            return "/libra?userAction=" + ILIActions.ACTION_ECHEANCES_RC + "." + FWAction.ACTION_CHERCHER;
        } else if (viewBean instanceof LIJournalisationsDetailViewBean) {
            return "/libra?userAction=" + ILIActions.ACTION_JOURNALISATIONS_RC + "." + FWAction.ACTION_CHERCHER;
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof LIEcheancesDetailViewBean) {
            return "/libra?userAction=" + ILIActions.ACTION_ECHEANCES_RC + "." + FWAction.ACTION_CHERCHER;
        } else if (viewBean instanceof LIJournalisationsDetailViewBean) {
            return "/libra?userAction=" + ILIActions.ACTION_JOURNALISATIONS_RC + "." + FWAction.ACTION_CHERCHER;
        } else {
            return super._getDestModifierSucces(session, request, response, viewBean);
        }
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws JadeClassCastException
     * @throws ClassCastException
     * @throws NullPointerException
     * @throws JadeServiceActivatorException
     * @throws JadeServiceLocatorException
     */
    public void actionAfficherDossierGed(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException,
            JadeServiceLocatorException, JadeServiceActivatorException, NullPointerException, ClassCastException,
            JadeClassCastException {

        PRGedAffichageDossier.actionAfficherDossierGed(session, request, response, mainDispatcher, viewBean);

    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        FWViewBeanInterface viewBean = new LIEcheancesJointDossiersViewBean();

        if (getAction().getClassPart().equals("journalisations")) {

            if (session.getAttribute(FWServlet.VIEWBEAN) instanceof LIJournalisationsJointDossiersViewBean) {
                viewBean = (LIJournalisationsJointDossiersViewBean) session.getAttribute(FWServlet.VIEWBEAN);
            } else {
                viewBean = new LIJournalisationsJointDossiersViewBean();
            }

            LIRecherchesDTO dto = (LIRecherchesDTO) PRSessionDataContainerHelper.getData(session,
                    PRSessionDataContainerHelper.KEY_LIBRA_DTO);
            if (null != dto) {
                String idTiers = dto.getIdTiers();
                if (null != idTiers) {
                    ((LIJournalisationsJointDossiersViewBean) viewBean).setIdTiers(idTiers);
                }

                String idDossier = dto.getIdDossier();
                if (null != idDossier) {
                    ((LIJournalisationsJointDossiersViewBean) viewBean).setIdDossier(idDossier);
                }

                String idUser = dto.getIdUser();
                if (null != idUser) {
                    ((LIJournalisationsJointDossiersViewBean) viewBean).setIdUser(idUser);
                }

            }

            // Reprise des éventuelles paramètres si on vient depuis les
            // dossiers
            if (null != request.getParameter("idTiers")
                    && !JadeStringUtil.isBlankOrZero(request.getParameter("idTiers"))) {
                ((LIJournalisationsJointDossiersViewBean) viewBean).setIdTiers(request.getParameter("idTiers"));
            } else {
                if (!JadeStringUtil.isBlankOrZero(((LIJournalisationsJointDossiersViewBean) viewBean)
                        .getIdTiersDepuisPyxis())) {
                    ((LIJournalisationsJointDossiersViewBean) viewBean)
                            .setIdTiers(((LIJournalisationsJointDossiersViewBean) viewBean).getIdTiersDepuisPyxis());
                }
            }

            if (null != request.getParameter("selectedId")
                    && !JadeStringUtil.isBlankOrZero(request.getParameter("selectedId"))) {
                ((LIJournalisationsJointDossiersViewBean) viewBean).setIdDossier(request.getParameter("selectedId"));
            } else {
                if (!JadeStringUtil.isBlankOrZero(((LIJournalisationsJointDossiersViewBean) viewBean)
                        .getIdDossierSelector())) {
                    ((LIJournalisationsJointDossiersViewBean) viewBean)
                            .setIdDossier(((LIJournalisationsJointDossiersViewBean) viewBean).getIdDossierSelector());
                }
            }

            if (!JadeStringUtil.isBlankOrZero(((LIJournalisationsJointDossiersViewBean) viewBean).getIdUserSelector())) {
                ((LIJournalisationsJointDossiersViewBean) viewBean)
                        .setIdUser(((LIJournalisationsJointDossiersViewBean) viewBean).getIdUserSelector());
            }

        } else if (getAction().getClassPart().equals("echeances")) {
            viewBean = new LIEcheancesJointDossiersViewBean();

            LIRecherchesDTO dto = (LIRecherchesDTO) PRSessionDataContainerHelper.getData(session,
                    PRSessionDataContainerHelper.KEY_LIBRA_DTO);
            if (null != dto) {
                String idTiers = dto.getIdTiers();
                if (null != idTiers) {
                    ((LIEcheancesJointDossiersViewBean) viewBean).setIdTiers(idTiers);
                }

                String idDossier = dto.getIdDossier();
                if (null != idDossier) {
                    ((LIEcheancesJointDossiersViewBean) viewBean).setIdDossier(idDossier);
                }

            }

            // Reprise des éventuelles paramètres si on vient depuis les
            // dossiers
            if (null != request.getParameter("idTiers")
                    && !JadeStringUtil.isBlankOrZero(request.getParameter("idTiers"))) {
                ((LIEcheancesJointDossiersViewBean) viewBean).setIdTiers(request.getParameter("idTiers"));
            }

            if (null != request.getParameter("selectedId")
                    && !JadeStringUtil.isBlankOrZero(request.getParameter("selectedId"))) {
                ((LIEcheancesJointDossiersViewBean) viewBean).setIdDossier(request.getParameter("selectedId"));
            }

        }

        viewBean.setISession(mainDispatcher.getSession());
        saveViewBean(viewBean, request);
        saveViewBean(viewBean, session);

        String _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), null);

        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionExporter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        if (getAction().getClassPart().equals("echeances")) {

            String _destination = "";

            try {

                getAction().changeActionPart(FWAction.ACTION_LISTER);

                FWViewBeanInterface viewBean = new LIEcheancesJointDossiersListViewBean();

                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
                if (viewBean instanceof BManager) {
                    ((BManager) viewBean).changeManagerSize(BManager.SIZE_NOLIMIT);
                }

                viewBean = beforeLister(session, request, response, viewBean);
                viewBean = mainDispatcher.dispatch(viewBean, getAction());
                request.setAttribute("viewBean", viewBean);

                session.removeAttribute("listViewBean");
                session.setAttribute("listViewBean", viewBean);

                _destination = getRelativeURL(request, session) + "_print.jsp";

            } catch (InvocationTargetException e) {
                _destination = ERROR_PAGE;
            } catch (IllegalAccessException e) {
                _destination = ERROR_PAGE;
            } catch (Exception e) {
                _destination = ERROR_PAGE;
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

        } else if (getAction().getClassPart().equals("journalisations")) {

            String _destination = "";

            try {

                getAction().changeActionPart(FWAction.ACTION_LISTER);

                FWViewBeanInterface viewBean = new LIJournalisationsJointDossiersListViewBean();

                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
                if (viewBean instanceof BManager) {
                    ((BManager) viewBean).changeManagerSize(BManager.SIZE_NOLIMIT);
                }

                viewBean = beforeLister(session, request, response, viewBean);
                viewBean = mainDispatcher.dispatch(viewBean, getAction());
                request.setAttribute("viewBean", viewBean);

                session.removeAttribute("listViewBean");
                session.setAttribute("listViewBean", viewBean);

                _destination = getRelativeURL(request, session) + "_print.jsp";

            } catch (InvocationTargetException e) {
                _destination = ERROR_PAGE;
            } catch (IllegalAccessException e) {
                _destination = ERROR_PAGE;
            } catch (Exception e) {
                _destination = ERROR_PAGE;
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

        } else {
            super.actionExporter(session, request, response, mainDispatcher);
        }
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            FWViewBeanInterface viewBean = new LIEcheancesJointDossiersListViewBean();

            if (getAction().getClassPart().equals("journalisations")) {
                viewBean = new LIJournalisationsJointDossiersListViewBean();
            }

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeLister(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            request.setAttribute("viewBean", viewBean);

            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);

            _destination = getRelativeURL(request, session) + "_rcListe.jsp";

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            LIEcheancesDetailViewBean viewBean = (LIEcheancesDetailViewBean) session.getAttribute("viewBean");

            // Setter les choses dans le viewBean pour les échéances multiples
            int nbEchMul = viewBean.getEcheanceMultipleManager().size();

            List listEchMul = new ArrayList();

            for (int i = 1; i < nbEchMul + 1; i++) {

                LIEcheanceMultipleObject echMulObj = viewBean.new LIEcheanceMultipleObject();

                if (request.getParameter("isRecu_" + String.valueOf(i)) != null) {
                    echMulObj.isRecu = true;
                } else {
                    echMulObj.isRecu = false;
                }

                echMulObj.dateRece = request.getParameter("dateRe_" + String.valueOf(i));
                echMulObj.idEchMul = request.getParameter("idEchM_" + String.valueOf(i));

                listEchMul.add(echMulObj);

            }

            viewBean.setListEcheanceMult(listEchMul);

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = (LIEcheancesDetailViewBean) beforeModifier(session, request, response, viewBean);
            viewBean = (LIEcheancesDetailViewBean) mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                _destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                _destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);

    }

    public void afficherExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        // Récupérer les échéances cochées pour l'écran d'exécution
        LIDocumentsExecutionViewBean vb = new LIDocumentsExecutionViewBean();
        vb.setListeIdRappel(Arrays.asList(request.getParameterValues("listeIdRappel")));

        String destination = getRelativeURL(request, session) + "_de.jsp";

        viewBean = beforeAfficher(session, request, response, viewBean);
        mainDispatcher.dispatch(vb, getAction());
        saveViewBean(vb, session);
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        LIRecherchesDTO dto = new LIRecherchesDTO();

        // Journalisations
        if (viewBean instanceof LIJournalisationsJointDossiersListViewBean) {

            String idDossier = request.getParameter("forIdDossier");
            if (null != idDossier) {
                dto.setIdDossier(idDossier);
                dto.setRechercheDossier(true);
            }

            String idTiers = request.getParameter("forIdTiers");
            if (null != idTiers) {
                dto.setIdTiers(idTiers);
                dto.setRechercheTiers(true);
            }

            String idUser = request.getParameter("idUser");
            if (null != idUser) {
                dto.setIdUser(idUser);
                dto.setRechercheUser(true);
            }

            LIRecherchesDTO oldDto = (LIRecherchesDTO) PRSessionDataContainerHelper.getData(session,
                    PRSessionDataContainerHelper.KEY_LIBRA_DTO);
            if (null != oldDto) {
                dto.setIdUtilisateur(oldDto.getIdUtilisateur());
                dto.setIdGroupe(oldDto.getIdGroupe());
                dto.setIdDomaine(oldDto.getIdDomaine());
            }

            // Echéances
        } else {
            LIEcheancesJointDossiersListViewBean listViewBean = (LIEcheancesJointDossiersListViewBean) viewBean;

            if (listViewBean.getForIdDomaine().startsWith("IN")) {
                dto.setIdDomaine("");
            } else {
                dto.setIdDomaine(listViewBean.getForIdDomaine());
            }

            dto.setIdGroupe(listViewBean.getForIdGroupe());
            dto.setIdUtilisateur(listViewBean.getForIdUtilisateur());

            String idDossier = request.getParameter("forIdDossier");
            if (null != idDossier) {
                dto.setIdDossier(idDossier);
                dto.setRechercheDossier(true);
            }

            LIRecherchesDTO oldDto = (LIRecherchesDTO) PRSessionDataContainerHelper.getData(session,
                    PRSessionDataContainerHelper.KEY_LIBRA_DTO);
            if (null != oldDto) {
                dto.setIdUser(oldDto.getIdUser());
                dto.setIdTiers(oldDto.getIdTiers());
            }

        }

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_LIBRA_DTO, dto);

        return super.beforeLister(session, request, response, viewBean);
    }

    public void executerAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws ServletException, IOException {

        String _destination = "";

        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            String idJournalisation = request.getParameter("selectedId");

            FWViewBeanInterface viewBean = new LIJournalisationsDetailViewBean();
            viewBean.setISession(mainDispatcher.getSession());
            ((LIJournalisationsDetailViewBean) viewBean).setId(idJournalisation);
            ((LIJournalisationsDetailViewBean) viewBean).retrieve();
            ((LIJournalisationsDetailViewBean) viewBean).setTypeAction(request.getParameter("action"));

            if (BProcess.class.isAssignableFrom(viewBean.getClass())) {
                BProcess process = (BProcess) viewBean;
                process.setControleTransaction(true);
                process.setSendCompletionMail(true);
            }

            viewBean = mainDispatcher.dispatch(viewBean, _action);
            saveViewBean(viewBean, session);
            saveViewBean(viewBean, request);

            _destination = "/libra?userAction=" + ILIActions.ACTION_ECHEANCES_RC + "." + FWAction.ACTION_CHERCHER;

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);
    }

    public void genererDocuments(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        getAction().setRight(FWSecureConstants.UPDATE);

        // Récupérer les échéances cochées pour l'écran d'exécution
        LIDocumentsExecutionViewBean vb = (LIDocumentsExecutionViewBean) viewBean;

        vb.setAdresseEmail(request.getParameter("eMailAddress"));
        vb.setDateExecution(request.getParameter("dateExecution"));

        String destination = "/libra?userAction=" + ILIActions.ACTION_ECHEANCES_RC + "." + FWAction.ACTION_CHERCHER;

        viewBean = beforeAfficher(session, request, response, viewBean);
        mainDispatcher.dispatch(vb, getAction());
        saveViewBean(vb, session);
        goSendRedirect(destination, request, response);

    }

}
