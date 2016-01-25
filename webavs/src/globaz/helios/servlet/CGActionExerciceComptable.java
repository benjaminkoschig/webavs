package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JAVector;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableListViewBean;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGMandatListViewBean;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la sélection d'un exercice comptable. Classe pour les actions specifique a l'object ExerciceComptable
 * Date de création : (10.10.2002 16:08:43) Particularitée : action 'choisirExerice' permettant de mettre l'object en
 * session http. certaines action auront besoin de cet objet pour leur fonctionnement, et ne seront pas accessible si
 * l'objet n'est pas en session. voir également : - CGActionNeedExerciceComptable - CGNeedExerciceComptableInterface
 * 
 */
public class CGActionExerciceComptable extends CGDefaultServletAction {

    private static final String EXERCICE_COMPTABLE_ALLOW_NUMBER = "helios.comptes.exerciceComptable.allowNumber.";
    public static final String EXERCICE_COMPTABLE_PREFIX = "exerciceComptable";

    public CGActionExerciceComptable(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Cette methode est utilisée pour mettre un objet CGExerciceComptable dans la session http. cet objet sera utilisé
     * par la suite par la plus part des actions/ecrans.
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void actionChoisirExercice(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String selectedId = "";

        // recupere la destination ou l'on veut se rendre apres avoir choisis
        // l'exercice comptable
        String destination = (String) session.getAttribute(CGNeedExerciceComptable.SESSION_DESTINATION);
        session.removeAttribute(CGNeedExerciceComptable.SESSION_DESTINATION);

        if (destination == null) {
            String path = "/" + globaz.helios.application.CGApplication.APPLICATION_HELIOS_REP + "/"
                    + FWDefaultServletAction.getIdLangueIso(session) + "/";
            destination = path + "appMain.jsp";
        }

        selectedId = request.getParameter("selectedId");
        if ((selectedId != null) && (!JadeStringUtil.isIntegerEmpty(selectedId))) {
            CGExerciceComptableViewBean viewBean = new CGExerciceComptableViewBean();
            viewBean.setIdExerciceComptable(selectedId);

            try {
                if (hasRight((BSession) mainDispatcher.getSession(), viewBean)) {
                    FWAction action = FWAction.newInstance(request.getParameter("userAction"), FWSecureConstants.READ);

                    action.changeActionPart(FWAction.ACTION_AFFICHER);
                    viewBean = (CGExerciceComptableViewBean) mainDispatcher.dispatch(viewBean, action);

                    FWMenuBlackBox bb = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
                    if (viewBean.getMandat().isMandatConsolidation()) {
                        bb.setNodeActive(true, "consolidation", "CG-MenuPrincipal");

                        bb.setNodeActive(false, "consolidation_exportation", "CG-consolidation");
                        bb.setNodeActive(true, "consolidation_importation", "CG-consolidation");
                        bb.setNodeActive(true, "consolidation_reset_importation", "CG-consolidation");
                        bb.setNodeActive(true, "consolidation_imprimer_mois", "CG-consolidation");
                        bb.setNodeActive(true, "consolidation_imprimer_agence", "CG-consolidation");

                        bb.setActionParameter("userAction", "helios.consolidation.processImportConsolidation.afficher",
                                "consolidation", "CG-MenuPrincipal");
                        bb.setActionParameter("userAction", "helios.print.compteAnnuelConsolide.afficher",
                                "imprimer_compte_annuel", "CG-MenuPrincipal");

                        if (destination.indexOf("helios.consolidation.processExportConsolidation.afficher") > -1) {
                            destination = JadeStringUtil.change(destination, "processExportConsolidation",
                                    "processImportConsolidation");
                        } else if (destination.indexOf("helios.print.compteAnnuel.afficher") > -1) {
                            destination = JadeStringUtil.change(destination, "compteAnnuel", "compteAnnuelConsolide");
                        }
                    } else if (viewBean.getMandat().isEstComptabiliteAVS().booleanValue()) {
                        bb.setNodeActive(true, "consolidation", "CG-MenuPrincipal");

                        bb.setNodeActive(true, "consolidation_exportation", "CG-consolidation");
                        bb.setNodeActive(false, "consolidation_importation", "CG-consolidation");
                        bb.setNodeActive(false, "consolidation_reset_importation", "CG-consolidation");
                        bb.setNodeActive(false, "consolidation_imprimer_mois", "CG-consolidation");
                        bb.setNodeActive(false, "consolidation_imprimer_agence", "CG-consolidation");

                        bb.setActionParameter("userAction", "helios.consolidation.processExportConsolidation.afficher",
                                "consolidation", "CG-MenuPrincipal");
                        bb.setActionParameter("userAction", "helios.print.compteAnnuel.afficher",
                                "imprimer_compte_annuel", "CG-MenuPrincipal");

                        if (destination.indexOf("helios.consolidation.processImportConsolidation.afficher") > -1) {
                            destination = JadeStringUtil.change(destination, "processImportConsolidation",
                                    "processExportConsolidation");
                        } else if (destination.indexOf("helios.print.compteAnnuelConsolide.afficher") > -1) {
                            destination = JadeStringUtil.change(destination, "compteAnnuelConsolide", "compteAnnuel");
                        }
                    } else {
                        bb.setNodeActive(false, "consolidation", "CG-MenuPrincipal");
                    }

                    if ((viewBean != null) && (viewBean.getMsgType() != FWViewBeanInterface.ERROR)
                            && (viewBean.getMandat() != null)) {
                        setSessionAttribute(session, CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE, viewBean);
                    } else {
                        destination = ERROR_PAGE;
                    }
                } else {
                    setSessionAttribute(session, VIEWBEAN, viewBean);

                    destination = ERROR_PAGE;
                }
            } catch (Exception e) {
                e.printStackTrace();
                destination = ERROR_PAGE;
            }
        } else {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if ("choisirExercice".equals(getAction().getActionPart())) {
            actionChoisirExercice(session, request, response, mainDispatcher);
        }
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            FWViewBeanInterface viewBean = FWListViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeLister(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);

            removeUnauthorizedExerciceComptable(mainDispatcher, viewBean);

            request.setAttribute(VIEWBEAN, viewBean);

            setSessionAttribute(session, "listViewBean", viewBean);

            destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWViewBeanInterface) Sette l'id du mandat si non défini, et prend le premier mandat
     *      trouvé comme mandat par défaut.
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        CGExerciceComptableViewBean vBean = (CGExerciceComptableViewBean) viewBean;
        String idMandat = request.getParameter("idMandat");
        if (idMandat == null || idMandat.trim().length() == 0) {
            CGMandatListViewBean mgr = new CGMandatListViewBean();

            try {
                mgr.setSession((BSession) CodeSystem.getSession(session));
                mgr.find();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mgr.size() > 0) {
                vBean.setIdMandat(((CGMandat) mgr.getEntity(0)).getIdMandat());
            }
        } else {
            vBean.setIdMandat(idMandat);
        }

        return viewBean;
    }

    /**
     * si on modifie l'exerice comptable (ds la db) qui est le meme que celui qui se trouve en session, en retire
     * l'exerice comptable de la session car il n'est plus valable. il faudra le remettre en session manuellement.
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        CGExerciceComptableViewBean vBean = (CGExerciceComptableViewBean) viewBean;
        CGExerciceComptableViewBean exerciceSession = (CGExerciceComptableViewBean) session
                .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
        if ((exerciceSession != null) && (viewBean != null)) {
            if (exerciceSession.getIdExerciceComptable().equals(vBean.getIdExerciceComptable())) {
                session.removeAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
            }
        }
        return viewBean;
    }

    /**
     * Has user right to select the following exercice comptable ?
     * 
     * @param session
     * @param selectedId
     * @return
     */
    private boolean hasRight(BSession session, CGExerciceComptable viewBean) throws Exception {
        if (session.hasRight(EXERCICE_COMPTABLE_ALLOW_NUMBER + viewBean.getIdExerciceComptable(),
                FWSecureConstants.READ)) {
            return true;
        } else {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(FWMessageFormat.format("ERROR User [{0}] has no right [{1}] for the action [{2}]",
                    session.getUserId(), FWSecureConstants.READ, getAction()));

            return false;
        }
    }

    /**
     * Iterate viewBean to remove unauth orized exercice comptable for the current client.
     * 
     * @param mainDispatcher
     * @param viewBean
     * @throws Exception
     */
    private void removeUnauthorizedExerciceComptable(FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws Exception {
        JAVector list = ((CGExerciceComptableListViewBean) viewBean).getContainer();

        if (list != null) {
            JAVector idToRemove = new JAVector();

            for (int i = 0; i < list.size(); i++) {
                CGExerciceComptable tmp = (CGExerciceComptable) list.get(i);

                if (!hasRight((BSession) mainDispatcher.getSession(), tmp)) {
                    idToRemove.add(tmp.getIdExerciceComptable());
                }
            }

            for (int i = 0; i < idToRemove.size(); i++) {
                boolean found = false;
                int j = 0;

                while ((j < list.size()) && (!found)) {
                    CGExerciceComptable tmp = (CGExerciceComptable) list.get(j);

                    if (tmp.getIdExerciceComptable().equals(idToRemove.get(i))) {
                        list.remove(j);
                        found = true;
                    }

                    j++;
                }
            }
        }
    }

}
