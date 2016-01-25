package globaz.osiris.servlet.action.services;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAEcritureManagerListViewBean;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrementManagerListViewBean;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManagerListViewBean;
import globaz.osiris.db.services.CARechercheMontantOperationManagerListViewBean;
import globaz.osiris.db.services.CARechercheMontantOperationViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la recherche des opérations à travers tout les journaux.
 * 
 * @author DDA
 */
public class CARechercheMontantOperationAction extends CADefaultServletAction {
    public static final String AFFICHER_LETTRAGE = "afficherLettrage";
    public static final String AFFICHER_SPECIAL = "afficherSpecial";

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
    private static final String NAME_SERVICES = "services";

    public static final String RECHERCHE_MONTANT_OPERATION = "rechercheMontantOperation";

    public static final String REMBOURSER = "rembourser";

    public CARechercheMontantOperationAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_rc.jsp";
        try {
            CARechercheMontantOperationManagerListViewBean manager = new CARechercheMontantOperationManagerListViewBean();
            manager.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, VBL_ELEMENT, manager);

        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

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

            manager.setISession(mainDispatcher.getSession());

            JSPUtils.setBeanProperties(request, manager);

            manager = beforeLister(session, request, response, manager);
            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VBL_ELEMENT, manager);

            myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    private CAOperationManager getManager(HttpServletRequest request, String actionSuite, boolean isNew)
            throws ServletException {
        CAOperationManager manager;
        if (actionSuite.equals(JOURNAL_OPERATION_ORDRE_VERSEMENT)) {
            manager = (CAOperationManager) super.getListViewBean(request, DIRECTORY_COMPTES,
                    NAME_OPERATION_ORDRE_VERSEMENT, isNew); // getOperationOrdreVersementManager(request);
            if (!JadeStringUtil.isNull(request.getParameter("forMontantABS"))) {
                ((CAOperationOrdreVersementManagerListViewBean) manager).setForMontantABS(request
                        .getParameter("forMontantABS"));
            }
        } else if (actionSuite.equals(JOURNAL_OPERATION_ORDRE_RECOUVREMENT)) {
            manager = (CAOperationManager) super.getListViewBean(request, DIRECTORY_COMPTES,
                    NAME_OPERATION_ORDRE_RECOUVREMENT, isNew); // getOperationOrdreRecouvrementManager(request);
            if (!JadeStringUtil.isNull(request.getParameter("forIdExterneRole"))) {
                manager.setForIdExterneRole(request.getParameter("forIdExterneRole"));
                ((CAOperationOrdreRecouvrementManagerListViewBean) manager).setForMontantABS(request
                        .getParameter("forIdExterneRole"));
            }
        } else if (actionSuite.equals(JOURNAL_OPERATION_SUSPENS)) {
            manager = (CAOperationManager) super.getListViewBean(request, DIRECTORY_COMPTES, NAME_OPERATION, isNew); // getOperationManager(request);
        } else if (actionSuite.equals(JOURNAL_OPERATION_AUXILIAIRE)) {
            manager = (CAOperationManager) super.getListViewBean(request, DIRECTORY_COMPTES, NAME_AUXILIAIRE, isNew);
        } else if (actionSuite.equals("journalOperationPaiementEtranger")) {
            manager = (CAOperationManager) super
                    .getListViewBean(request, "comptes", "operationPaiementEtranger", isNew); // getOperationPaiementEtrangerManager(request);
            manager.setForSelectionTri(request.getParameter("forSelectionTri"));
        } else if (actionSuite.equals("journalOperation")) {
            manager = (CAOperationManager) super.getListViewBean(request, NAME_SERVICES, NAME_OPERATION, isNew);
        } else if (actionSuite.equals(RECHERCHE_MONTANT_OPERATION)) {
            manager = (CAOperationManager) super.getListViewBean(request, DIRECTORY_COMPTES, NAME_OPERATION, isNew);
        } else {
            manager = (CAOperationManager) super.getListViewBean(request, DIRECTORY_COMPTES, NAME_ECRITURE, isNew); // getEcritureManager(request);
            if (!JadeStringUtil.isNull(request.getParameter("forMontantABS"))) {
                ((CAEcritureManagerListViewBean) manager).setForMontantABS(request.getParameter("forMontantABS"));
            }
            manager.setVueOperationCpteAnnexe("true");
            manager.setApercuJournal("true");
            manager.setForSelectionTri(request.getParameter("forSelectionTri"));
        }

        if (actionSuite.equals(JOURNAL_OPERATION_ECRITURE)) {
            manager.setForIdTypeOperation(APIOperation.CAECRITURE);
            manager.setVueOperationCpteAnnexe("true");
        } else if (actionSuite.equals(JOURNAL_OPERATION_ORDRE_RECOUVREMENT)) {
            manager.setForIdTypeOperation(APIOperation.CAOPERATIONORDRERECOUVREMENT);
            manager.setVueOperationCpteAnnexe("true");
        } else if (actionSuite.equals(JOURNAL_OPERATION_ORDRE_VERSEMENT)) {
            manager.setForIdTypeOperation(APIOperation.CAOPERATIONORDREVERSEMENT);
            manager.setVueOperationCpteAnnexe("true");
        } else if (actionSuite.equals(JOURNAL_OPERATION_PAIEMENT_BVR)) {
            manager.setForIdTypeOperation(APIOperation.CAPAIEMENTBVR);
        } else if (actionSuite.equals(JOURNAL_OPERATION_PAIEMENT_ETRANGER)
                || actionSuite.equals("journalOperationPmtEtranger")) {
            manager.setForIdTypeOperation(APIOperation.CAPAIEMENTETRANGER);
        } else if (actionSuite.equals(JOURNAL_OPERATION_PAIEMENT)) {
            manager.setForIdTypeOperation(APIOperation.CAPAIEMENT);
            manager.setVueOperationCpteAnnexe("true");
        } else if (actionSuite.equals(JOURNAL_OPERATION_RECOUVREMENT)) {
            manager.setForIdTypeOperation(APIOperation.CARECOUVREMENT);
            manager.setVueOperationCpteAnnexe("true");
        } else if (actionSuite.equals(JOURNAL_OPERATION_VERSEMENT)) {
            manager.setForIdTypeOperation(APIOperation.CAVERSEMENT);
            manager.setVueOperationCpteAnnexe("true");
        } else if (actionSuite.equals(JOURNAL_OPERATION_SUSPENS)) {
            manager.setForEtat(APIOperation.ETAT_ERREUR);
            manager.setVueOperationCpteAnnexe("true");
        } else if (actionSuite.equals(JOURNAL_OPERATION_AUXILIAIRE)) {
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

        if (!JadeStringUtil.isNull(request.getParameter("idCompte"))) {
            manager.setForIdCompte(request.getParameter("idCompte"));
        }

        if (!actionSuite.equals(JOURNAL_OPERATION_VERSEMENT)) {
            manager.setApercuJournal("true");
        }

        manager.setVueOperationCpteAnnexe("true");
        manager.setRechercheMontant("true");

        return manager;
    }

    protected CARechercheMontantOperationViewBean getRechercheMontant(HttpServletRequest request)
            throws ServletException {
        CARechercheMontantOperationViewBean element = (CARechercheMontantOperationViewBean) JSPUtils.useBean(request,
                "element", "globaz.osiris.services.CARechercheMontantViewBean", "session");
        if ((!JadeStringUtil.isBlank(getId(request, "idOperation")))
                && (!JadeStringUtil.isNull(getId(request, "idOperation")))) {
            element.setIdOperation(getId(request, "idOperation"));
        } else {
            element = (CARechercheMontantOperationViewBean) JSPUtils.useBean(request, "element",
                    "globaz.osiris.services.CARechercheMontantViewBean", "session", true);
        }

        return element;
    }

    protected CARechercheMontantOperationManagerListViewBean getRechercheMontantManagerListViewBean(
            HttpServletRequest request) throws ServletException, InvocationTargetException, IllegalAccessException {
        CARechercheMontantOperationManagerListViewBean manager = (CARechercheMontantOperationManagerListViewBean) JSPUtils
                .useBean(request, "manager", "globaz.osiris.services.CARechercheMontantManagerListViewBean", "request");
        JSPUtils.setBeanProperties(request, manager);

        return manager;
    }

}