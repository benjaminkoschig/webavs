package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.db.comptes.CGEnteteEcritureViewBean;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.helios.db.modeles.CGLigneModeleEcritureListViewBean;
import globaz.helios.db.modeles.CGLigneModeleEcritureViewBean;
import globaz.helios.db.modeles.CGModeleEcritureViewBean;
import globaz.helios.process.modele.CGEcritureModeleViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'exploitation d'un modèle.
 * 
 * @author: dda
 */
public class CGActionEcritureModele extends CGDefaultServletAction {

    private static final String LABEL_DATE_NON_VALIDE = "DATE_NON_VALIDE";
    private static final String LABEL_MODELE_INEXISTANT = "MODELE_INEXISTANT";
    private static final String PARAM_ATTACHED_ID_CENTRE_CHARGE = "a" + CGActionEcritureModele.PARAM_CENTRE_CHARGE
            + CGActionEcritureModele.PARAM_DOUBLE;
    private static final String PARAM_ATTACHED_ID_ECRITURE_DOUBLE = "a" + CGActionEcritureModele.PARAM_ID_ECRITURE
            + CGActionEcritureModele.PARAM_DOUBLE;
    private static final String PARAM_CENTRE_CHARGE = "ch";
    private static final String PARAM_COLLECTIVE = "c";

    private static final String PARAM_COURS_MONNAIE = "cm";
    private static final String PARAM_DOUBLE = "d";

    private static final String PARAM_ID_ECRITURE = "ide";
    private static final String PARAM_LIBELLE = "l";

    private static final String PARAM_MONTANT = "m";
    private static final String PARAM_MONTANT_MONNAIE = "mm";

    private boolean showDateErrorsOnAfficher = false;
    private boolean showModeleErrorsOnAfficher = false;

    /**
     * Commentaire relatif au constructeur CGActionEcritureModele.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CGActionEcritureModele(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            FWViewBeanInterface viewBean = new CGEnteteEcritureViewBean();
            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);

            if (showDateErrorsOnAfficher) {
                viewBean.setMessage(((BSession) mainDispatcher.getSession())
                        .getLabel(CGActionEcritureModele.LABEL_DATE_NON_VALIDE));
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            if (showModeleErrorsOnAfficher) {
                viewBean.setMessage(((BSession) mainDispatcher.getSession())
                        .getLabel(CGActionEcritureModele.LABEL_MODELE_INEXISTANT));
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            setSessionAttribute(session, CGDefaultServletAction.VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if ((JadeStringUtil.isBlank(request.getParameter("date"))) || (!validateDate(request, mainDispatcher))) {
            showDateErrorsOnAfficher = true;
            actionAfficher(session, request, response, mainDispatcher);
        } else if (JadeStringUtil.isIntegerEmpty(request.getParameter("selectedIdModel"))) {
            showModeleErrorsOnAfficher = true;
            actionAfficher(session, request, response, mainDispatcher);
        } else {
            String destination = getRelativeURL(request, session) + "Process_de.jsp";

            try {
                FWAction action = FWAction.newInstance(request.getParameter("userAction"));
                action.changeActionPart(FWAction.ACTION_AFFICHER);

                String selectedId = request.getParameter("selectedIdModel");

                FWViewBeanInterface viewBean = new CGModeleEcritureViewBean();
                ((CGModeleEcritureViewBean) viewBean).setIdModeleEcriture(selectedId);

                CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean) session
                        .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
                if (exerciceComptable != null) {
                    ((CGModeleEcritureViewBean) viewBean).setIdMandat(exerciceComptable.getIdMandat());
                }

                viewBean = beforeAfficher(session, request, response, viewBean);
                viewBean = mainDispatcher.dispatch(viewBean, action);

                if (!JadeStringUtil.isBlank(request.getParameter("piece"))) {
                    ((CGModeleEcritureViewBean) viewBean).setPiece(request.getParameter("piece"));
                }

                setSessionAttribute(session, CGDefaultServletAction.VIEWBEAN, viewBean);

                session.setAttribute("idJournal", request.getParameter("idJournal"));

                // User rights OK car dispatch précédent testé

                CGLigneModeleEcritureListViewBean ligneListViewBean = new CGLigneModeleEcritureListViewBean();
                ligneListViewBean.setISession(mainDispatcher.getSession());
                ligneListViewBean.setForIdModeleEcriture(((CGModeleEcritureViewBean) viewBean).getIdModeleEcriture());
                ligneListViewBean.find();

                setSessionAttribute(session, "listViewBean", ligneListViewBean);

                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                    destination = FWDefaultServletAction.ERROR_PAGE;
                } else {
                    destination = getRelativeURL(request, session) + "Process_de.jsp";
                }

            } catch (Exception e) {
                JadeLogger.error(this, e);
                destination = FWDefaultServletAction.ERROR_PAGE;
            }

            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        }
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(HttpSession,
     *      HttpServletRequestHttpServletResponse, globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        if ((request.getParameter("paginationChange") != null)
                && (request.getParameter("paginationChange").equals("true"))) {
            actionChercher(session, request, response, mainDispatcher);
        } else {
            execute(session, request, response, mainDispatcher);
        }
    }

    private void execute(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));

        try {
            FWViewBeanInterface viewBean = new CGEcritureModeleViewBean();
            JSPUtils.setBeanProperties(request, viewBean);

            CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean) session
                    .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
            if (exerciceComptable != null) {
                ((CGEcritureModeleViewBean) viewBean)
                        .setIdExerciceComptable(exerciceComptable.getIdExerciceComptable());
                ((CGEcritureModeleViewBean) viewBean).setIdMandat(exerciceComptable.getIdMandat());
            }

            ((CGEcritureModeleViewBean) viewBean).setEcrituresDoubles(getEcrituresFromRequest(request,
                    CGActionEcritureModele.PARAM_DOUBLE));
            ((CGEcritureModeleViewBean) viewBean)
                    .setAttachedEcrituresDoubles(getAttachedEcrituresDoublesFromRequest(request));

            ((CGEcritureModeleViewBean) viewBean).setEcrituresCollectives(getEcrituresFromRequest(request,
                    CGActionEcritureModele.PARAM_COLLECTIVE));

            viewBean = mainDispatcher.dispatch(viewBean, action);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.ERROR_PAGE)
                        .forward(request, response);
            }
        } catch (Exception e) {
            servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.ERROR_PAGE)
                    .forward(request, response);
        }

        String destination = "/" + action.getApplicationPart()
                + "?userAction=helios.comptes.ecritureModele.afficher&_method=add&_valid=&selectedIdModel=&idJournal="
                + request.getParameter("idJournal") + "&libelleJournal=" + request.getParameter("libelleJournal")
                + "&date=" + request.getParameter("date");
        goSendRedirectWithoutParameters(destination, request, response);
    }

    private ArrayList getAttachedEcrituresDoublesFromRequest(HttpServletRequest request) {
        ArrayList ecritures = new ArrayList();

        String idEcriture = new String();
        int countParam = 0;
        while ((idEcriture = request
                .getParameter(CGActionEcritureModele.PARAM_ATTACHED_ID_ECRITURE_DOUBLE + countParam)) != null) {
            if (validUserMontantEntries(request, CGActionEcritureModele.PARAM_DOUBLE, countParam)) {
                CGLigneModeleEcritureViewBean ecriture = new CGLigneModeleEcritureViewBean();
                ecriture.setIdLigneModeleEcriture(idEcriture);

                ecriture.setIdCentreCharge(request.getParameter(CGActionEcritureModele.PARAM_ATTACHED_ID_CENTRE_CHARGE
                        + countParam));

                ecritures.add(ecriture);
            }

            countParam++;
        }

        return ecritures;
    }

    private ArrayList getEcrituresFromRequest(HttpServletRequest request, String type) {
        ArrayList ecritures = new ArrayList();

        String idEcriture = new String();
        int countParam = 0;
        while ((idEcriture = request.getParameter(CGActionEcritureModele.PARAM_ID_ECRITURE + type + countParam)) != null) {
            if (validUserMontantEntries(request, type, countParam)) {
                CGLigneModeleEcritureViewBean ecriture = new CGLigneModeleEcritureViewBean();
                if ("true".equalsIgnoreCase(request.getParameter("saisieEcran"))) {
                    ecriture.setSaisieEcran(request.getParameter("saisieEcran"));
                }
                ecriture.setIdLigneModeleEcriture(idEcriture);
                ecriture.setIdEnteteModeleEcriture(request.getParameter(CGActionEcritureModele.PARAM_ID_ECRITURE + type
                        + countParam));
                ecriture.setMontant(JANumberFormatter.deQuote(request.getParameter(CGActionEcritureModele.PARAM_MONTANT
                        + type + countParam)));
                ecriture.setMontantMonnaie(JANumberFormatter.deQuote(request
                        .getParameter(CGActionEcritureModele.PARAM_MONTANT_MONNAIE + type + countParam)));
                ecriture.setCoursMonnaie(JANumberFormatter.deQuote(request
                        .getParameter(CGActionEcritureModele.PARAM_COURS_MONNAIE + type + countParam)));
                ecriture.setLibelle(request.getParameter(CGActionEcritureModele.PARAM_LIBELLE + type + countParam));
                ecriture.setIdCentreCharge(request.getParameter(CGActionEcritureModele.PARAM_CENTRE_CHARGE + type
                        + countParam));

                ecritures.add(ecriture);
            }

            countParam++;
        }

        return ecritures;
    }

    /**
     * Test si la date introduite par l'utilisateur est valide.
     * 
     * @param request
     * @param mainDispatcher
     * @return
     */
    private boolean validateDate(HttpServletRequest request, FWDispatcher mainDispatcher) {
        try {
            BSessionUtil.checkDateGregorian((BSession) mainDispatcher.getSession(), request.getParameter("date"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validUserMontantEntries(HttpServletRequest request, String type, int countParam) {
        if (!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(request
                .getParameter(CGActionEcritureModele.PARAM_MONTANT + type + countParam)))) {
            return true;
        } else if (((!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(request
                .getParameter(CGActionEcritureModele.PARAM_MONTANT_MONNAIE + type + countParam)))))
                && ((!JadeStringUtil.isIntegerEmpty(JANumberFormatter.deQuote(request
                        .getParameter(CGActionEcritureModele.PARAM_COURS_MONNAIE + type + countParam)))))) {
            return true;
        } else {
            return false;
        }
    }
}
