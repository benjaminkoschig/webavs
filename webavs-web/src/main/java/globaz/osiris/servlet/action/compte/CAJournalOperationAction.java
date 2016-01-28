package globaz.osiris.servlet.action.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeViewBean;
import globaz.osiris.db.comptes.CAEcritureManagerListViewBean;
import globaz.osiris.db.comptes.CAJournalOperationLettrerViewBean;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrementManagerListViewBean;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManagerListViewBean;
import globaz.osiris.db.comptes.CAOperationOrdreVersementViewBean;
import globaz.osiris.db.comptes.CAPaiementEtrangerViewBean;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.comptes.CASectionManagerListViewBean;
import globaz.osiris.db.comptes.CASectionViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import globaz.osiris.utils.CASessionDataContainerHelper;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour les opérations dans les journaux.
 * 
 * @author DDA
 */
public class CAJournalOperationAction extends CADefaultServletAction {
    public static final String AFFICHER_LETTRAGE = "afficherLettrage";

    private static final String DIRECTORY_COMPTES = "comptes";
    private static final String JOURNAL_OPERATION_AUXILIAIRE = "journalOperationAuxiliaire";
    public static final String JOURNAL_OPERATION_CUSTOM = "journalOperationCustom";
    private static final String JOURNAL_OPERATION_ECRITURE = "journalOperationEcriture";
    public static final String JOURNAL_OPERATION_LETTRER = "journalOperationLettrer";
    private static final String JOURNAL_OPERATION_ORDRE_RECOUVREMENT = "journalOperationOrdreRecouvrement";
    public static final String JOURNAL_OPERATION_ORDRE_VERSEMENT = "journalOperationOrdreVersement";
    private static final String JOURNAL_OPERATION_PAIEMENT = "journalOperationPaiement";
    private static final String JOURNAL_OPERATION_PAIEMENT_BVR = "journalOperationPaiementBVR";
    private static final String JOURNAL_OPERATION_PAIEMENT_ETRANGER = "journalOperationPaiementEtranger";
    private static final String JOURNAL_OPERATION_RECOUVREMENT = "journalOperationRecouvrement";

    private static final String JOURNAL_OPERATION_SUSPENS = "journalOperationSuspens";
    private static final String JOURNAL_OPERATION_VERSEMENT = "journalOperationVersement";
    private static final String NAME_AUXILIAIRE = "auxiliaire";
    private static final String NAME_ECRITURE = "ecriture";
    private static final String NAME_OPERATION = "operation";
    private static final String NAME_OPERATION_ORDRE_RECOUVREMENT = "operationOrdreRecouvrement";
    private static final String NAME_OPERATION_ORDRE_VERSEMENT = "operationOrdreVersement";
    private static final String NAME_PAIEMENT = "paiement";
    private static final String NAME_PAIEMENT_BVR = "paiementBVR";
    private static final String NAME_PAIEMENT_ETRANGER = "paiementEtranger";
    private static final String NAME_RECOUVREMENT = "recouvrement";

    private static final String NAME_VERSEMENT = "versement";

    public static final String REMBOURSER = "rembourser";

    private static final String USERACTION = "userAction";

    /**
     * Constructor for CAJournalOperationEcriture.
     * 
     * @param servlet
     */
    public CAJournalOperationAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher&selectedId=" + ((CAOperation) viewBean).getIdOperation();
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher&selectedId=" + ((CAOperation) viewBean).getIdOperation();
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

        if (!JadeStringUtil.isBlank(request.getParameter("_method")) && isRetourDepuisPyxis(viewBean)) {
            servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp")
                    .forward(request, response);
        } else {
            String myDestination = getRelativeURL(request, session) + "_de.jsp";
            String actionSuite = getActionSuite(request);

            try {
                CAOperationManager manager = null;

                if ((manager = getManager(request, actionSuite, false)) == null) {
                    throw new JAException("Action non valid : " + actionSuite);
                }

                FWViewBeanInterface element = null;

                boolean isNew = (!JadeStringUtil.isBlank(request.getParameter("_method")) && (request
                        .getParameter("_method").equals("add")));

                if ((element = getElement(request, actionSuite, (CAOperation) element, manager, isNew)) == null) {
                    throw new JAException("Action non valid : " + actionSuite);
                }

                ((CAOperation) element).setNomEcran("comptes/" + actionSuite + "_de");
                ((CAOperation) element).setSession((BSession) mainDispatcher.getSession());

                if (!JadeStringUtil.isEmpty(super.getId(request, "selectedId"))) {
                    ((CAOperation) element).setIdOperation(super.getId(request, "selectedId"));
                } else {
                    ((CAOperation) element).setIdOperation("");
                }

                if (isNew) {
                    ((CAOperation) element).setEtat(APIOperation.ETAT_OUVERT);
                }

                if (!JadeStringUtil.isEmpty(super.getId(request, "Message")) && isNew) {
                    element.setMessage(super.getId(request, "Message"));
                }

                element = beforeAfficher(session, request, response, element);
                element = mainDispatcher.dispatch(element, FWAction.newInstance(request.getParameter("userAction")));

                if (element instanceof CAPaiementEtrangerViewBean) {
                    splitNoAvsToPart1234((CAOperation) element);
                }

                // Gestion de la vue globale
                if ((((CAOperation) element).getCompteAnnexe() != null)
                        && (((CAOperation) element).getCompteAnnexe().getIdTiers() != null)) {
                    request.getSession().setAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                            ((CAOperation) element).getCompteAnnexe().getIdTiers());
                }

                setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, element);

                if (element.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                    myDestination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
                } else {
                    myDestination = getRelativeURL(request, session) + "_de.jsp";
                }

            } catch (Exception e) {
                myDestination = FWDefaultServletAction.ERROR_PAGE;
            }

            servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
        }
    }

    /**
     * Première étape de l'action de lettrage. Contrôle si la valeur de la section sélectionné est négatif puis affiche
     * page _de.
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws ServletException
     * @throws IOException
     */
    private void actionAfficherLettrage(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";
        FWViewBeanInterface ca = null;

        try {
            CASectionViewBean actualSection = (CASectionViewBean) (new CASectionAction((FWServlet) servlet))
                    .getSection(session, request, response, dispatcher);

            FWCurrency solde = new FWCurrency(actualSection.getSolde());
            BSession bSession = ((BSession) dispatcher.getSession());

            if ((solde.isPositive()) || (solde.isZero())) {
                // Message disant que l'on ne peut pas rembourser une section avec un montant positif ou à zéro
                String message = bSession.getLabel(CAOperationOrdreVersement.LABEL_MONTANT_NEGATIF);

                // Preparation du message retourné à l'utilisateur sur l'écran source et non de destination
                ca = prepareToGetErrorMessage(ca, message, request, session, dispatcher);

                setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, ca);

                destination = FWDefaultServletAction.createFullURL(request, "/osiris?userAction="
                        + CAApplication.DEFAULT_OSIRIS_NAME + ".comptes.apercuParSection.chercher&idCompteAnnexe="
                        + ((CACompteAnnexeViewBean) ca).getIdCompteAnnexe());

                goSendRedirect(destination, request, response);
                return;
            } else if (APISection.ID_CATEGORIE_SECTION_RETOUR.equals(actualSection.getCategorieSection())) {
                // Message disant que les sections de type retours doivent être gérer ailleurs.
                String message = bSession.getLabel(CASection.LABEL_RETOURS_GERER_AILLEURS);

                // Preparation du message retourné à l'utilisateur sur l'écran source et non de destination
                ca = prepareToGetErrorMessage(ca, message, request, session, dispatcher);

                setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, ca);

                destination = FWDefaultServletAction.createFullURL(request, "/osiris?userAction="
                        + CAApplication.DEFAULT_OSIRIS_NAME + ".comptes.apercuParSection.chercher&idCompteAnnexe="
                        + ((CACompteAnnexeViewBean) ca).getIdCompteAnnexe());

                goSendRedirect(destination, request, response);
                return;
            } else {
                // Possibilité de faire du lettrage si nous sommes dans cette condition.
                CAJournalOperationLettrerViewBean viewBean = (CAJournalOperationLettrerViewBean) this
                        .getViewBean(request);
                viewBean.setISession(dispatcher.getSession());
                viewBean.setIdCompteAnnexe(request.getParameter("idCompteAnnexe"));
                viewBean.setIdSourceSection(actualSection.getIdSection());

                setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);

                CASectionManagerListViewBean sectionListViewBean = (CASectionManagerListViewBean) session
                        .getAttribute(CAComptesAnnexesAction.VBL_SECTION_MANAGER);
                sectionListViewBean.setOrderBy(CASectionManager.ORDER_DATE);

                dispatcher.dispatch(sectionListViewBean, FWAction.newInstance(request.getParameter("userAction")));
                setSessionAttribute(session, CAComptesAnnexesAction.VBL_SECTION_MANAGER, sectionListViewBean);
            }
        } catch (Exception e) {
            FWViewBeanInterface theViewBeanUsedInErrorPage = null;

            if (session != null) {
                theViewBeanUsedInErrorPage = (FWViewBeanInterface) session.getAttribute("viewBean");
            }

            if (theViewBeanUsedInErrorPage == null) {
                theViewBeanUsedInErrorPage = (FWViewBeanInterface) request.getAttribute("viewBean");
            }

            if (theViewBeanUsedInErrorPage != null) {
                theViewBeanUsedInErrorPage.setMsgType(FWViewBeanInterface.ERROR);
                theViewBeanUsedInErrorPage.setMessage(theViewBeanUsedInErrorPage.getMessage() + " "
                        + JadeUtil.getStackTrace(e));
            }

            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    protected void actionAjouterPaiementEtranger(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = "";

        try {
            CAPaiementEtrangerViewBean viewBean = (CAPaiementEtrangerViewBean) session.getAttribute("viewBean");
            JSPUtils.setBeanProperties(request, viewBean);

            FWAction action = FWAction.newInstance(request.getParameter(CAJournalOperationAction.USERACTION));

            viewBean = (CAPaiementEtrangerViewBean) beforeModifier(session, request, response, viewBean);
            viewBean = (CAPaiementEtrangerViewBean) mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
            } else {

                destination = getRelativeURL(request, session) + "_de.jsp";
            }

            CASessionDataContainerHelper sessionDataContainer = new CASessionDataContainerHelper();
            sessionDataContainer.setData(session, CASessionDataContainerHelper.KEY_LAST_PAIEMENT_ETRANGER, viewBean);

        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_rc.jsp";
        String actionSuite = getActionSuite(request);
        try {
            CAOperation element = null;
            CAOperationManager manager = null;

            if ((manager = getManager(request, actionSuite, false)) == null) {
                throw new JAException("Action non valid : " + actionSuite);
            }

            if ((element = getElement(request, actionSuite, element, manager, true)) == null) {
                throw new JAException("Action non valid : " + actionSuite);
            }

            element.setSession((BSession) mainDispatcher.getSession());
            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, element);

            manager.setSession((BSession) mainDispatcher.getSession());
            JSPUtils.setBeanProperties(request, manager);

            setSessionAttribute(session, CADefaultServletAction.VBL_ELEMENT, manager);

            myDestination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            myDestination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    protected void actionChercherPaiementsEtranger(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_rc.jsp";
        try {
            CAOperation element = null;
            CAOperationManager manager = null;

            if ((manager = getManager(request, "journalOperationPmtEtranger", false)) == null) {
                throw new JAException("Action non valid : " + "journalOperationPmtEtranger");
            }

            if ((element = getElement(request, "journalOperationPaiementEtranger", element, manager, true)) == null) {
                throw new JAException("Action non valid : " + "journalOperationPaiementEtranger");
            }

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            action.changeActionPart(FWAction.ACTION_AFFICHER);

            element = (CAOperation) mainDispatcher.dispatch(element, action);

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, element);

            manager.setSession((BSession) mainDispatcher.getSession());
            JSPUtils.setBeanProperties(request, manager);
            setSessionAttribute(session, CADefaultServletAction.VBL_ELEMENT, manager);

            myDestination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            myDestination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        String actionSuite = getActionSuite(request);
        if (CAJournalOperationAction.JOURNAL_OPERATION_PAIEMENT_ETRANGER.equals(actionSuite)) {

            if ("ajouterAvantCompensation".equals(getAction().getActionPart())) {
                actionAjouterPaiementEtranger(session, request, response, dispatcher);
            } else if ("modifierAvantCompensation".equals(getAction().getActionPart())) {
                actionModifierPaiementEtranger(session, request, response, dispatcher);
            } else if ("chercherPaiements".equals(getAction().getActionPart())) {
                actionChercherPaiementsEtranger(session, request, response, dispatcher);
            } else if ("supprimerPaiement".equals(getAction().getActionPart())) {
                actionSupprimerPaiementEtranger(session, request, response, dispatcher);
            }

        } else if (CAJournalOperationAction.JOURNAL_OPERATION_ORDRE_VERSEMENT.equals(actionSuite)) {
            if (CAJournalOperationAction.REMBOURSER.equals(getAction().getActionPart())) {
                actionRembourser(session, request, response, dispatcher);
            }
        } else if (CAJournalOperationAction.JOURNAL_OPERATION_LETTRER.equals(actionSuite)) {
            if (CAJournalOperationAction.AFFICHER_LETTRAGE.equals(getAction().getActionPart())) {
                actionAfficherLettrage(session, request, response, dispatcher);
            }
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        String actionSuite = getActionSuite(request);
        try {
            FWViewBeanInterface manager = null;

            if ((manager = getManager(request, actionSuite, true)) == null) {
                throw new JAException("Action non valid : " + actionSuite);
            }

            JSPUtils.setBeanProperties(request, manager);

            manager = beforeLister(session, request, response, manager);
            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, CADefaultServletAction.VBL_ELEMENT, manager);

            myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            myDestination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getActionFullURL() + ".chercher";

        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session
                    .getAttribute(CADefaultServletAction.VB_ELEMENT);

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = getRelativeURL(request, session) + "_de.jsp";
            } else {
                if (!JadeStringUtil.isBlank(request.getParameter("returnview"))
                        && (request.getParameter("returnview").equals("suspens"))) {
                    destination = "/osiris?userAction=osiris.comptes.journalOperationSuspens." + ".chercher";
                } else {
                    destination = getActionFullURL() + ".chercher";
                }
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    protected void actionModifierPaiementEtranger(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = "";

        try {
            CAPaiementEtrangerViewBean viewBean = (CAPaiementEtrangerViewBean) session.getAttribute("viewBean");
            JSPUtils.setBeanProperties(request, viewBean);

            FWAction action = FWAction.newInstance(request.getParameter(CAJournalOperationAction.USERACTION));

            viewBean = (CAPaiementEtrangerViewBean) beforeModifier(session, request, response, viewBean);
            viewBean = (CAPaiementEtrangerViewBean) mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);

            CAOperationManager manager = null;

            if ((manager = getManager(request, "journalOperationPmtEtranger", false)) == null) {
                throw new JAException("Action non valid : " + "journalOperationPmtEtranger");
            }

            manager.setSession((BSession) mainDispatcher.getSession());
            setSessionAttribute(session, CADefaultServletAction.VBL_ELEMENT, manager);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
            } else {

                destination = getRelativeURL(request, session) + "_rc.jsp";
            }

            CASessionDataContainerHelper sessionDataContainer = new CASessionDataContainerHelper();
            sessionDataContainer.setData(session, CASessionDataContainerHelper.KEY_LAST_PAIEMENT_ETRANGER, viewBean);

        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }

    /**
     * Action du remboursement automatique d'un compte annexe.
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws ServletException
     * @throws IOException
     */
    private void actionRembourser(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String destination;

        try {
            CAOperationOrdreVersementViewBean operation = new CAOperationOrdreVersementViewBean();

            CASectionViewBean actualSection = (CASectionViewBean) (new CASectionAction((FWServlet) servlet))
                    .getSection(session, request, response, dispatcher);

            operation.setSession((BSession) dispatcher.getSession());

            if (hasSpecialRight(operation, FWSecureConstants.ADD)) {
                operation.addOrdreVersementAutomatique(null, actualSection, null);
            }

            if (operation.hasErrors()) {

                // SPECIAL (dal) : Le message d'erreur du solde non négatif
                // sera ajouté à la jsp.RC qui sera ensuite ajouté à la
                // jsp.RCLISTE
                // Nécessaire car le message s'affichera sur la page d'appelle
                // de la fonction et non sur la page de destination
                CACompteAnnexeViewBean ca = (CACompteAnnexeViewBean) session
                        .getAttribute(CADefaultServletAction.VB_ELEMENT);

                StringBuffer sessionError = ((BSession) dispatcher.getSession()).getErrors();

                if (!JadeStringUtil.isBlank(sessionError.toString())) {
                    ca.setMessage(sessionError.toString());
                    ca.setMsgType(FWViewBeanInterface.ERROR);
                }

                setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, ca);

                destination = FWDefaultServletAction.createFullURL(request,
                        "/osiris?userAction=" + CAApplication.DEFAULT_OSIRIS_NAME
                                + ".comptes.apercuParSection.chercher&idCompteAnnexe=" + ca.getIdCompteAnnexe());

            } else {
                setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, operation);
                destination = FWDefaultServletAction.createFullURL(request, "/osiris?userAction="
                        + CAApplication.DEFAULT_OSIRIS_NAME
                        + ".comptes.journalOperationOrdreVersement.afficher&selectedId=" + operation.getIdOperation());
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }

    protected void actionSupprimerPaiementEtranger(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = null;

        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));

        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

        viewBean = beforeSupprimer(session, request, response, viewBean);
        viewBean = mainDispatcher.dispatch(viewBean, _action);

        CAOperation element = null;
        CAOperationManager manager = null;

        try {
            if ((manager = getManager(request, "journalOperationPmtEtranger", false)) == null) {
                throw new JAException("Action non valid : " + "journalOperationPmtEtranger");
            }

            if ((element = getElement(request, "journalOperationPaiementEtranger", element, manager, true)) == null) {
                throw new JAException("Action non valid : " + "journalOperationPaiementEtranger");
            }

            element.setSession((BSession) mainDispatcher.getSession());
            element.retrieve();
        } catch (ServletException e) {
            // Do nothing
        } catch (JAException e) {
            // Do nothing
        } catch (Exception e) {
            // Do nothing
        }

        setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, element);

        manager.setSession((BSession) mainDispatcher.getSession());
        setSessionAttribute(session, CADefaultServletAction.VBL_ELEMENT, manager);

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
        } else {

            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        goSendRedirect(_destination, request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeModifier(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String actionSuite = getActionSuite(request);

        if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_ORDRE_VERSEMENT)) {
            CAOperationOrdreVersement oldViewBean = (CAOperationOrdreVersement) session.getAttribute("viewBean");
            if (!oldViewBean.isUpdatable()) {
                oldViewBean.setEstBloque(((CAOperationOrdreVersement) viewBean).getEstBloque());
                return oldViewBean;
            }
        }

        return super.beforeModifier(session, request, response, viewBean);
    }

    private CAOperation getElement(HttpServletRequest request, String actionSuite, CAOperation element,
            CAOperationManager manager, boolean isNew) throws ServletException {
        if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_ECRITURE)) {
            element = (CAOperation) super.getViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_ECRITURE, isNew); // getEcriture(request,
            // isNew);
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_ORDRE_RECOUVREMENT)) {
            element = (CAOperation) super.getViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_OPERATION_ORDRE_RECOUVREMENT, isNew); // getOperationOrdreRecouvrement(request);
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_ORDRE_VERSEMENT)) {
            element = (CAOperation) super.getViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_OPERATION_ORDRE_VERSEMENT, isNew); // getOperationOrdreVersement(request);
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_PAIEMENT_BVR)) {
            element = (CAOperation) super.getViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_PAIEMENT_BVR, isNew); // getPaiementBVR(request);
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_PAIEMENT_ETRANGER)) {
            element = (CAOperation) super.getViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_PAIEMENT_ETRANGER, isNew);
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_PAIEMENT)) {
            element = (CAOperation) super.getViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_PAIEMENT, isNew); // getPaiement(request);
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_RECOUVREMENT)) {
            element = (CAOperation) super.getViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_RECOUVREMENT, isNew); // getRecouvrement(request);
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_VERSEMENT)) {
            element = (CAOperation) super.getViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_VERSEMENT, isNew); // getVersement(request);
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_SUSPENS)) {
            element = (CAOperation) super.getViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_OPERATION, isNew); // getOperation(request);
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_AUXILIAIRE)) {
            element = (CAOperation) super.getViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_AUXILIAIRE, isNew);
        } else {
            element = (CAOperation) super.getViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_OPERATION, isNew);
        }

        if (!JadeStringUtil.isBlank(request.getParameter("forIdJournal"))) {
            element.setIdJournal(request.getParameter("forIdJournal"));
        } else if (!JadeStringUtil.isBlank(request.getParameter("idJournal"))) {
            element.setIdJournal(request.getParameter("idJournal"));
        } else if (!JadeStringUtil.isBlank(super.getId(request, "id"))) {
            element.setIdJournal(super.getId(request, "id"));
        } else {
            element.setIdJournal(manager.getForIdJournal());
        }

        return element;
    }

    private CAOperationManager getManager(HttpServletRequest request, String actionSuite, boolean isNew)
            throws ServletException {
        CAOperationManager manager;
        if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_ORDRE_VERSEMENT)) {
            manager = (CAOperationManager) super.getListViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_OPERATION_ORDRE_VERSEMENT, isNew); // getOperationOrdreVersementManager(request);
            if (!JadeStringUtil.isNull(request.getParameter("forMontantABS"))) {
                ((CAOperationOrdreVersementManagerListViewBean) manager).setForMontantABS(request
                        .getParameter("forMontantABS"));
            }
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_ORDRE_RECOUVREMENT)) {
            manager = (CAOperationManager) super.getListViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_OPERATION_ORDRE_RECOUVREMENT, isNew); // getOperationOrdreRecouvrementManager(request);
            if (!JadeStringUtil.isNull(request.getParameter("forIdExterneRole"))) {
                manager.setForIdExterneRole(request.getParameter("forIdExterneRole"));
                ((CAOperationOrdreRecouvrementManagerListViewBean) manager).setForMontantABS(request
                        .getParameter("forIdExterneRole"));
            }
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_SUSPENS)) {
            manager = (CAOperationManager) super.getListViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_OPERATION, isNew); // getOperationManager(request);
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_AUXILIAIRE)) {
            manager = (CAOperationManager) super.getListViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_AUXILIAIRE, isNew);
        } else if (actionSuite.equals("journalOperationPaiementEtranger")) {
            manager = (CAOperationManager) super
                    .getListViewBean(request, "comptes", "operationPaiementEtranger", isNew); // getOperationPaiementEtrangerManager(request);
            manager.setForSelectionTri(request.getParameter("forSelectionTri"));
        } else if (actionSuite.equals("journalOperation")) {
            manager = (CAOperationManager) super.getListViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_OPERATION, isNew);
        } else {
            manager = (CAOperationManager) super.getListViewBean(request, CAJournalOperationAction.DIRECTORY_COMPTES,
                    CAJournalOperationAction.NAME_ECRITURE, isNew); // getEcritureManager(request);
            if (!JadeStringUtil.isNull(request.getParameter("forMontantABS"))) {
                ((CAEcritureManagerListViewBean) manager).setForMontantABS(request.getParameter("forMontantABS"));
            }
            manager.setVueOperationCpteAnnexe("true");
            manager.setApercuJournal("true");
            manager.setForSelectionTri(request.getParameter("forSelectionTri"));
        }

        if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_ECRITURE)) {
            manager.setForIdTypeOperation(APIOperation.CAECRITURE);
            manager.setVueOperationCpteAnnexe("true");
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_ORDRE_RECOUVREMENT)) {
            manager.setForIdTypeOperation(APIOperation.CAOPERATIONORDRERECOUVREMENT);
            manager.setVueOperationCpteAnnexe("true");
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_ORDRE_VERSEMENT)) {
            manager.setForIdTypeOperation(APIOperation.CAOPERATIONORDREVERSEMENT);
            manager.setVueOperationCpteAnnexe("true");
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_PAIEMENT_BVR)) {
            manager.setForIdTypeOperation(APIOperation.CAPAIEMENTBVR);
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_PAIEMENT_ETRANGER)
                || actionSuite.equals("journalOperationPmtEtranger")) {
            manager.setForIdTypeOperation(APIOperation.CAPAIEMENTETRANGER);
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_PAIEMENT)) {
            manager.setForIdTypeOperation(APIOperation.CAPAIEMENT);
            manager.setVueOperationCpteAnnexe("true");
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_RECOUVREMENT)) {
            manager.setForIdTypeOperation(APIOperation.CARECOUVREMENT);
            manager.setVueOperationCpteAnnexe("true");
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_VERSEMENT)) {
            manager.setForIdTypeOperation(APIOperation.CAVERSEMENT);
            manager.setVueOperationCpteAnnexe("true");
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_SUSPENS)) {
            manager.setForEtat(APIOperation.ETAT_ERREUR);
            manager.setVueOperationCpteAnnexe("true");
        } else if (actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_AUXILIAIRE)) {
            manager.setForIdTypeOperation(APIOperation.CAAUXILIAIRE);
        }

        if (!JadeStringUtil.isNull(request.getParameter("forIdJournal"))) {
            manager.setForIdJournal(request.getParameter("forIdJournal"));
        }

        if (!JadeStringUtil.isNull(request.getParameter("fromDate"))) {
            manager.setFromDate(request.getParameter("fromDate"));
        }

        if (!JadeStringUtil.isNull(request.getParameter("fromDescription"))) {
            manager.setFromDescription(request.getParameter("fromDescription"));
        }

        if (!JadeStringUtil.isNull(request.getParameter("forIdCompteAnnexe"))) {
            manager.setForIdCompteAnnexe(request.getParameter("forIdCompteAnnexe"));
        }

        if (!JadeStringUtil.isNull(request.getParameter("forIdExterneRole"))) {
            manager.setForIdExterneRole(request.getParameter("forIdExterneRole"));
        }

        if (!JadeStringUtil.isNull(request.getParameter("forSelectionRole"))) {
            manager.setForSelectionRole(request.getParameter("forSelectionRole"));
        }

        manager.setForCodeMaster(CAOperationManager.SAISIE);
        manager.setApercuJournal("true");
        manager.changeManagerSize(200);

        if (!JadeStringUtil.isNull(request.getParameter("idCompte"))) {
            manager.setForIdCompte(request.getParameter("idCompte"));
        }

        if (!actionSuite.equals(CAJournalOperationAction.JOURNAL_OPERATION_VERSEMENT)) {
            manager.setApercuJournal("true");
        }

        manager.setVueOperationCpteAnnexe("true");

        return manager;
    }

    /**
     * L'action est-elle appelée en retour du module tiers ?
     * 
     * @param viewBean
     * @return
     */
    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof CAOperationOrdreVersementViewBean) && ((CAOperationOrdreVersementViewBean) viewBean)
                .isRetourDepuisPyxis());
    }

    private FWViewBeanInterface prepareToGetErrorMessage(FWViewBeanInterface ca, String message,
            HttpServletRequest request, HttpSession session, FWDispatcher dispatcher) throws Exception {

        // SPECIAL (dal) : Le message d'erreur du solde non négatif
        // sera ajouté à la jsp.RC qui sera ensuite ajouté à la
        // jsp.RCLISTE
        // Nécessaire car le message s'affichera sur la page d'appelle
        // de la fonction et non sur la page de destination

        if (session.getAttribute(CADefaultServletAction.VB_ELEMENT) instanceof CACompteAnnexeViewBean) {
            ca = (CACompteAnnexeViewBean) session.getAttribute(CADefaultServletAction.VB_ELEMENT);
        } else {
            ca = new CACompteAnnexeViewBean();

            JSPUtils.setBeanProperties(request, ca);

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            action.changeActionPart(FWAction.ACTION_AFFICHER);

            ca = dispatcher.dispatch(ca, action);
        }

        ca.setMessage(message);
        ca.setMsgType(FWViewBeanInterface.ERROR);

        return ca;
    }

    private void splitNoAvsToPart1234(CAOperation element) {
        CACompteAnnexe ca = element.getCompteAnnexe();
        String noAVS = null;

        if (ca != null) {
            noAVS = ca.getIdExterneRole();
        }

        if (noAVS == null) {
            noAVS = "";
        }

        StringTokenizer token = new StringTokenizer(noAVS, ".");
        int counter = 1;
        while (token.hasMoreTokens()) {
            switch (counter) {
                case 1:
                    ((CAPaiementEtrangerViewBean) element).setNoAvs1(token.nextToken());
                    break;
                case 2:
                    ((CAPaiementEtrangerViewBean) element).setNoAvs2(token.nextToken());
                    break;
                case 3:
                    ((CAPaiementEtrangerViewBean) element).setNoAvs3(token.nextToken());
                    break;
                case 4:
                    ((CAPaiementEtrangerViewBean) element).setNoAvs4(token.nextToken());
                    break;
            }
            counter++;
        }
    }
}
