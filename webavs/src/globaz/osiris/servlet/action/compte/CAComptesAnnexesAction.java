package globaz.osiris.servlet.action.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BIApplication;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexeManagerListViewBean;
import globaz.osiris.db.comptes.CACompteAnnexeViewBean;
import globaz.osiris.db.comptes.CACompteurManagerListViewBean;
import globaz.osiris.db.comptes.CASectionManagerListViewBean;
import globaz.osiris.db.comptes.CASoldeCompteAnnexeCCManagerListViewBean;
import globaz.osiris.db.comptes.CASoldeCompteAnnexeCCViewBean;
import globaz.osiris.db.comptes.extrait.CAExtraitCompteManager;
import globaz.osiris.db.contentieux.CAExtraitCompteListViewBean;
import globaz.osiris.external.IntTiers;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import java.rmi.RemoteException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion des comptes annexes.
 * 
 * @author DDA
 */
public class CAComptesAnnexesAction extends CADefaultServletAction {
    public static final String VB_TIERS = "tiers";
    public static final String VBL_COMPTEANNEXE = "vbl_compteannexe";
    public static final String VBL_COMPTEANNEXE_MANAGER = "vbl_compteannexemanager";
    public static final String VBL_COMPTERESUME = "vbl_compteresume";
    public static final String VBL_COMPTEUR_MANAGER = "vbl_compteurmanager";
    public static final String VBL_SECTION_MANAGER = "listViewBean";

    /**
     * Constructor for CAComptesAnnexes.
     * 
     * @param servlet
     */
    public CAComptesAnnexesAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_rc.jsp";
        String actionSuite = getActionSuite(request);
        try {
            BManager manager = null;

            String[] inSectionManager = { "apercuCompteResume", "apercuParSection" };
            String[] inCompteAnnexeManager = { "apercuCompteResume", "apercuParSection", "apercuCompteur",
                    "rechercheCompteAnnexe" };
            String[] inCompteurManager = { "apercuCompteur" };
            String[] needTier = { "apercuComptesAnnexes" };

            CACompteAnnexeViewBean element = getCompteAnnexe(session, request, response, mainDispatcher);

            setSessionAttribute(session, VB_ELEMENT, element);

            if (isIn(actionSuite, inCompteAnnexeManager)) {
                manager = getCompteAnnexeManager(request);
                manager.setSession((BSession) mainDispatcher.getSession());
                JSPUtils.setBeanProperties(request, manager);
                setSessionAttribute(session, VBL_COMPTEANNEXE_MANAGER, manager);
            }
            if (isIn(actionSuite, inSectionManager)) {
                manager = getSectionManager(request);
            }
            if (isIn(actionSuite, inCompteurManager)) {
                manager = getCompteurManager(request);
            }
            if (isIn(actionSuite, needTier)) {
                getTier(session, request, mainDispatcher);
            }

            destination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_rcListe.jsp";
        String actionSuite = getActionSuite(request);
        try {
            CACompteAnnexeViewBean element = getCompteAnnexe(session, request, response, mainDispatcher);
            FWViewBeanInterface manager = null;
            String beanName = null;

            if (actionSuite.equals("apercuCompteResume")) {
                manager = getExtraitCompte(request, element);
                beanName = VBL_COMPTERESUME;
            } else if (actionSuite.equals("apercuParSection")) {
                manager = getSectionManager(request);
                if (!JadeStringUtil.isNull(super.getId(request, "forIdCompteAnnexe"))) {
                    ((CASectionManagerListViewBean) manager).setForIdCompteAnnexe(super.getId(request,
                            "forIdCompteAnnexe"));
                } else {
                    ((CASectionManagerListViewBean) manager).setForIdCompteAnnexe(element.getIdCompteAnnexe());
                }
                beanName = VBL_SECTION_MANAGER;
            } else if (actionSuite.equals("apercuComptesCourants")) {
                CASoldeCompteAnnexeCCViewBean elementCC = (CASoldeCompteAnnexeCCViewBean) JSPUtils.useBean(request,
                        VB_ELEMENT, "globaz.osiris.db.comptes.CASoldeCompteAnnexeCCViewBean", "session");
                elementCC.setSession((BSession) mainDispatcher.getSession());
                manager = getCompteAnnexeCompteCourant(request);
                element = getCompteAnnexe(session, request, response, mainDispatcher);

                ((CASoldeCompteAnnexeCCManagerListViewBean) manager).setForIdCompteAnnexe(element.getIdCompteAnnexe());
                beanName = VBL_COMPTEANNEXE;
                setSessionAttribute(session, VB_ELEMENT, elementCC);

                setSessionAttribute(session, VB_ELEMENT, element);
            } else if (actionSuite.equals("apercuCompteur")) {
                manager = getCompteurManager(request, element);
                beanName = VBL_COMPTEUR_MANAGER;
            } else if (actionSuite.equals("apercuComptesAnnexes")) {
                manager = getCompteAnnexeManager(request);
                ((CACompteAnnexeManagerListViewBean) manager).setForIdTiers(super.getId(request, "idTiers"));

                beanName = VBL_COMPTEANNEXE_MANAGER;
            } else if (actionSuite.equals("rechercheCompteAnnexe")) {
                manager = getCompteAnnexeManager(request);
                beanName = VBL_COMPTEANNEXE_MANAGER;
            }

            JSPUtils.setBeanProperties(request, manager);

            if (beanName != null && beanName.equals(VBL_COMPTERESUME)) {
                ((CAExtraitCompteListViewBean) manager).setModeScreen(true);
            }

            manager = beforeLister(session, request, response, manager);
            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            // SPECIAL (dal) : Pour la fonction de remoursement automatique
            // d'une section
            // le message d'erreur doit être affiché sur la page des sections
            // et non sur la page de destination de la fonction de remoursement
            // automatique
            if (beanName.equals(VBL_SECTION_MANAGER)) {
                if (session.getAttribute(VB_ELEMENT) != null) {
                    CACompteAnnexeViewBean ca = (CACompteAnnexeViewBean) session.getAttribute(VB_ELEMENT);

                    if (!JadeStringUtil.isBlank(ca.getMessage()) && ca.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                        ((CASectionManagerListViewBean) manager).setMessage(ca.getMessage());
                        ((CASectionManagerListViewBean) manager).setMsgType(FWViewBeanInterface.ERROR);
                    }
                }
            }

            session.removeAttribute(beanName);
            session.setAttribute(beanName, manager);
            destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            e.printStackTrace(System.out);
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    protected CACompteAnnexeViewBean getCompteAnnexe(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, Exception {
        FWViewBeanInterface element = (CACompteAnnexeViewBean) globaz.globall.http.JSPUtils.useBean(request,
                VB_ELEMENT, "globaz.osiris.db.comptes.CACompteAnnexeViewBean", "session", false);

        if ((element == null)
                || (!JadeStringUtil.isBlank(getId(request, "idCompteAnnexe")) && !getId(request, "idCompteAnnexe")
                        .equals(((CACompteAnnexeViewBean) element).getIdCompteAnnexe()))) {
            element = new CACompteAnnexeViewBean();
            ((CACompteAnnexeViewBean) element).setSession((BSession) mainDispatcher.getSession());
            ((CACompteAnnexeViewBean) element).setIdCompteAnnexe(getId(request, "idCompteAnnexe"));

            FWAction action = FWAction.newInstance("osiris.comptes.apercuComptes.afficher");

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, action);
        }

        return ((CACompteAnnexeViewBean) element);
    }

    protected CASoldeCompteAnnexeCCManagerListViewBean getCompteAnnexeCompteCourant(HttpServletRequest request)
            throws ServletException {
        CASoldeCompteAnnexeCCManagerListViewBean manager = (CASoldeCompteAnnexeCCManagerListViewBean) JSPUtils.useBean(
                request, "manager", "globaz.osiris.db.comptes.CASoldeCompteAnnexeCCManagerListViewBean", "request");

        if (!JadeStringUtil.isNull(request.getParameter("forSelectionCompte"))) {
            if (!request.getParameter("forSelectionCompte").equalsIgnoreCase("1000")) {
                manager.setForSelectionCompte(request.getParameter("forSelectionCompte"));

            }
        }
        return manager;
    }

    protected BManager getCompteAnnexeManager(HttpServletRequest request) throws ServletException {
        BManager manager = (CACompteAnnexeManagerListViewBean) globaz.globall.http.JSPUtils.useBean(request, "manager",
                "globaz.osiris.db.comptes.CACompteAnnexeManagerListViewBean", "request");
        if (!JadeStringUtil.isNull(request.getParameter("forSelectionCompte"))) {
            ((CACompteAnnexeManagerListViewBean) manager).setForSelectionCompte(request
                    .getParameter("forSelectionCompte"));
        } else {
            ((CACompteAnnexeManagerListViewBean) manager).setForSelectionCompte("1000");
        }

        if (!JadeStringUtil.isNull(request.getParameter("forSelectionRole"))) {
            ((CACompteAnnexeManagerListViewBean) manager).setForSelectionRole(request.getParameter("forSelectionRole"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("fromNumNom"))) {
            ((CACompteAnnexeManagerListViewBean) manager).setFromNumNom(request.getParameter("fromNumNom"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("forSelectionTri"))) {
            ((CACompteAnnexeManagerListViewBean) manager).setForSelectionTri(request.getParameter("forSelectionTri"));
        } else {
            ((CACompteAnnexeManagerListViewBean) manager).setForSelectionTri("2");
        }

        return manager;

    }

    protected BManager getCompteurManager(HttpServletRequest request) throws ServletException {
        return getCompteurManager(request, null);
    }

    protected BManager getCompteurManager(HttpServletRequest request, CACompteAnnexeViewBean element)
            throws ServletException {
        BManager manager = (CACompteurManagerListViewBean) globaz.globall.http.JSPUtils.useBean(request,
                "_compteurManager", "globaz.osiris.db.comptes.CACompteurManagerListViewBean", "request");
        if (!JadeStringUtil.isNull(super.getId(request, "forIdCompteAnnexe"))) {
            ((CACompteurManagerListViewBean) manager).setForIdCompteAnnexe(super.getId(request, "forIdCompteAnnexe"));
        } else if (element != null) {
            ((CACompteurManagerListViewBean) manager).setForIdCompteAnnexe(element.getIdCompteAnnexe());
        }

        if (!JadeStringUtil.isNull(request.getParameter("forSelectionTri"))) {
            ((CACompteurManagerListViewBean) manager).setForSelectionTri(request.getParameter("forSelectionTri"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("forSelectionCompteur"))) {
            ((CACompteurManagerListViewBean) manager).setForSelectionCompteur(request
                    .getParameter("forSelectionCompteur"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("forAnnee"))) {
            ((CACompteurManagerListViewBean) manager).setForAnnee(request.getParameter("forAnnee"));
        }

        return manager;
    }

    protected CAExtraitCompteListViewBean getExtraitCompte(HttpServletRequest request, CACompteAnnexeViewBean element)
            throws ServletException {
        CAExtraitCompteListViewBean manager = (CAExtraitCompteListViewBean) JSPUtils.useBean(request, "manager",
                "globaz.osiris.db.contentieux.CAExtraitCompteListViewBean", "request");
        if (!JadeStringUtil.isNull(super.getId(request, "id"))) {
            manager.setIdCompteAnnexe(super.getId(request, "id"));
        } else {
            manager.setIdCompteAnnexe(element.getIdCompteAnnexe());
        }
        if (!JadeStringUtil.isNull(request.getParameter("forSelectionSections"))) {
            if (!request.getParameter("forSelectionSections").equalsIgnoreCase(CAExtraitCompteManager.SOLDE_ALL)) {
                manager.setForSelectionSections(request.getParameter("forSelectionSections"));
            }
        }
        if (!JadeStringUtil.isNull(request.getParameter("forIdTypeOperation"))) {
            if (!request.getParameter("forIdTypeOperation").equalsIgnoreCase(
                    CAExtraitCompteManager.FOR_ALL_IDTYPEOPERATION)) {
                manager.setForIdTypeOperation(request.getParameter("forIdTypeOperation"));
            }
        }
        if (!JadeStringUtil.isNull(request.getParameter("forSelectionTri"))) {
            if (!request.getParameter("forSelectionTri").equalsIgnoreCase(CAExtraitCompteManager.SOLDE_ALL)) {
                manager.setForSelectionTri(request.getParameter("forSelectionTri"));
            }
        }
        if (!JadeStringUtil.isNull(request.getParameter("fromPositionnement"))) {
            if (!request.getParameter("fromPositionnement").equalsIgnoreCase("null")) {
                manager.setFromPositionnement(request.getParameter("fromPositionnement"));
            }
        }
        return manager;
    }

    protected BManager getSectionManager(HttpServletRequest request) throws ServletException {
        BManager manager = (CASectionManagerListViewBean) globaz.globall.http.JSPUtils.useBean(request,
                "_sectionManager", "globaz.osiris.db.comptes.CASectionManagerListViewBean", "request");
        if (!JadeStringUtil.isNull(request.getParameter("forSelectionSections"))) {
            if (!request.getParameter("forSelectionSections").equalsIgnoreCase("1000")) {
                ((CASectionManagerListViewBean) manager).setForSelectionSections(request
                        .getParameter("forSelectionSections"));
            }
        }
        if (!JadeStringUtil.isNull(request.getParameter("forSelectionTri"))) {
            ((CASectionManagerListViewBean) manager).setForSelectionTri(request.getParameter("forSelectionTri"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("fromPositionnement"))) {
            ((CASectionManagerListViewBean) manager).setFromPositionnement(request.getParameter("fromPositionnement"));
        }

        return manager;
    }

    protected void getTier(HttpSession session, HttpServletRequest request, FWDispatcher mainDispatcher)
            throws Exception, RemoteException {
        CAApplication currentApplication = (CAApplication) ((BSession) mainDispatcher.getSession()).getApplication();
        String externalApplicationName = currentApplication.getCAParametres().getApplicationExterne();
        BIApplication externalApplication = GlobazServer.getCurrentSystem().getApplication(externalApplicationName);
        IntTiers element1 = (IntTiers) externalApplication.getImplementationFor(mainDispatcher.getSession(),
                IntTiers.class);
        element1.retrieve(request.getParameter("idTiers"));
        session.setAttribute(VB_TIERS, element1);
    }

    private final boolean isIn(String userAction, String[] values) {
        int pos = -1;
        while (++pos < values.length) {
            if (values[pos].equalsIgnoreCase(userAction)) {
                return true;
            }
        }
        return false;
    }

}
