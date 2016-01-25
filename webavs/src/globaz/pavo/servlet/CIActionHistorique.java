package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.globall.db.BManager;
import globaz.globall.util.JAUtil;
import globaz.pavo.db.compte.CICompteIndividuelViewBean;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureListViewBean;
import globaz.pavo.db.compte.CIEcritureViewBean;
import globaz.pavo.db.inscriptions.CIJournalViewBean;
import globaz.pavo.util.CIUtil;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action concernant les écritures. Date de création : (12.11.2002 17:14:51)
 * 
 * @author: ema
 */
public class CIActionHistorique extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CIActionEcriture.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CIActionHistorique(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    protected void _actionAfficherSurPage(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String destination = "";
        try {
            String selectedId = request.getParameter("selectedId");
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());

            Class b = Class.forName("globaz.globall.db.BEntity");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });

            viewBean = beforeAfficher(session, request, response, viewBean);
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBeanEcrit", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_caDetail.jsp";
            }

        } catch (Exception e) {
            e.printStackTrace();
            destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        /*
         * try { BTransaction transaction = (BTransaction)mainDispatcher.getSession().newTransaction(); // On recherche
         * d'abord l'écriture CIEcritureViewBean viewBean = new CIEcritureViewBean();
         * viewBean.setEcritureId(request.getParameter("selectedId"));
         * viewBean.setSession((BSession)mainDispatcher.getSession()); viewBean.retrieve(transaction);
         * transaction.closeTransaction(); // sauve l'ecriture dans la session session.setAttribute("viewBeanEcrit",
         * viewBean); } catch (Exception e) { throw new javax.servlet.ServletException(e.getMessage()); }
         * 
         * //redirection vers destination String _destination = getRelativeURL() + "_caDetail.jsp";
         * servlet.getServletContext().getRequestDispatcher(_destination ).forward( request, response);
         */
    }

    protected void _actionAjouterSurPage(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = new String();
        try {
            FWViewBeanInterface viewBean = new CIEcritureViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean = beforeAjouter(session, request, response, viewBean);
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBeanEcrit", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = getRelativeURL(request, session) + "_caDetail.jsp?_valid=ERROR";
            } else {
                destination = getRelativeURL(request, session) + "_caDetail.jsp?_result=ADDED";
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Exécute la fonction chercher des mandats de splitting. Date de création : (29.10.2002 13:04:13)
     * 
     * @return globaz.framework.bean.FWViewBean
     */
    private void _actionChercherEcriture(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
        CICompteIndividuelViewBean viewBean = new CICompteIndividuelViewBean();
        // enregister les paramètres de la requête dans le bean
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            // bean reste vide
            e.printStackTrace();
        }
        FWAction action = getAction();
        // action.setRight(FWSecureConstants.READ);
        // appel du controlleur
        viewBean = (CICompteIndividuelViewBean) mainDispatcher.dispatch(viewBean, action);
        // sauve le bean dans la session en tant que bean foreig key (utilisé
        // plus
        // tard également)
        session.setAttribute("viewBeanFK", viewBean);
        // redirection vers destination
        String _destination = getRelativeURL(request, session) + "_rc.jsp";
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionChercherSurPage(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        // Addresse de redirection
        String destination = getRelativeURL(request, session) + "_ca.jsp";
        try {
            // getAction().changeActionPart(FWAction.ACTION_CHERCHER);
            // Récupération/création du journal, en session ou nouveau
            CIJournalViewBean viewBean = null;
            String journalId = request.getParameter("idJournal");
            if (journalId == null || journalId.length() == 0) {
                try {
                    viewBean = (CIJournalViewBean) session.getAttribute("viewBean");
                } catch (Exception ex) {
                    // n'existe pas ou incorrect
                }
            } else {
                if (viewBean == null || viewBean.getIdJournal().length() == 0) {
                    viewBean = new CIJournalViewBean();
                    globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
                }
            }
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            viewBean = (CIJournalViewBean) mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);
        } catch (Exception e) {
            e.printStackTrace();
            destination = ERROR_PAGE;
        }

        // Redirige vers l'écran adhoc...
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    protected void _actionCopie(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            // getAction().setRight(FWSecureConstants.READ);
            String selectedId = request.getParameter("selectedId");
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
            Class b = Class.forName("globaz.globall.db.BEntity");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp?_method=add";
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionExtourne(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        _actionCopie(session, request, response, mainDispatcher);
    }

    protected void _actionListerSurPage(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";
        try {
            FWViewBeanInterface viewBean = FWListViewBeanActionFactory.newInstance(getAction(),
                    mainDispatcher.getPrefix());
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean = beforeListerSurPage(session, request, response, viewBean);
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBeanEcrit", viewBean);

            destination = getRelativeURL(request, session) + "_caPage.jsp";
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    protected void _actionModifierSurPage(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = new String();
        try {
            getAction().changeActionPart(FWAction.ACTION_MODIFIER);
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBeanEcrit");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean = beforeModifier(session, request, response, viewBean);
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBeanEcrit", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = getRelativeURL(request, session) + "_caDetail.jsp?_valid=ERROR";
            } else {
                // destination = getRelativeURL(request,session) +
                // "_caDetail.jsp?_result=UPDATED";
                destination = getActionFullURL() + ".chercherSurPage";
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    protected void _actionSupprimerSurPage(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = ERROR_PAGE;
        try {
            getAction().changeActionPart(FWAction.ACTION_SUPPRIMER);

            // recuperation du bean depuis la session
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBeanEcrit");

            viewBean = beforeSupprimer(session, request, response, viewBean);
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBeanEcrit", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = getRelativeURL(request, session) + "_caDetail.jsp?_valid=ERROR";
                request.setAttribute("_method", "add");
            } else {
                destination = getActionFullURL() + ".chercherSurPage";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    // Met à jour une inscription en SUSPENS vers SUPPRIMER_SUSPENS
    protected void _actionSupprimerSuspens(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String destination = "";
        try {
            getAction().changeActionPart(FWAction.ACTION_MODIFIER);
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBeanEcrit");
            // Pour mettre à jour le type de compte
            viewBean = beforeSupprimerSuspens(session, request, response, viewBean);
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBeanEcrit", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = getRelativeURL(request, session) + "_caDetail.jsp?_valid=ERROR";
            } else {
                destination = getActionFullURL() + ".chercherSurPage";
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("listerSurPage")) {
            // chercher le _caPage.jsp d'affichage des inscriptions
            _actionListerSurPage(session, request, response, dispatcher);
            int test = 0;
        } else if (getAction().getActionPart().equals("afficherSurPage")) {
            // chercher le _caDetail.jsp d'affichage des détails d'inscriptions
            _actionAfficherSurPage(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("chercherEcriture")) {
            // chercher avec chargement des données nécessaire
            _actionChercherEcriture(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("supprimerSurPage")) {
            // Supression sur une page ca particuliere
            _actionSupprimerSurPage(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("modifierSurPage")) {
            // Modification sur une page ca particulière
            _actionModifierSurPage(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("chercherSurPage")) {
            // Recherche sur une page ca particuliere
            _actionChercherSurPage(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("ajouterSurPage")) {
            // Recherche sur une page ca particuliere
            _actionAjouterSurPage(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("supprimerSuspens")) {
            // Modifie un SUSPENS en SUPPRIMER_SUSPENS sur une page ca
            // particuliere
            _actionSupprimerSuspens(session, request, response, dispatcher);

        } else if (getAction().getActionPart().equals("extourne")) {
            // extourne l'écriture
            _actionExtourne(session, request, response, dispatcher);

        } else if (getAction().getActionPart().equals("copie")) {
            // copie l'écriture
            _actionCopie(session, request, response, dispatcher);
        }
    }

    protected FWViewBeanInterface beforeListerSurPage(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        ((BManager) viewBean).changeManagerSize(50);
        CIEcritureListViewBean ecrList = (CIEcritureListViewBean) viewBean;
        // Déformatage du critère sur le numéro AVS
        ecrList.setFromAvs(CIUtil.unFormatAVS(ecrList.getFromAvs()));
        // Parser la requête sur le montant
        // ecrList.setMontantQuery(parseQuery(request.getParameter("queryRevenu"),ecrList));
        return ecrList;
    }

    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        // ci et no avs en session -> assignation seulement possible ici
        CICompteIndividuelViewBean _bean = (CICompteIndividuelViewBean) session.getAttribute("viewBeanFK");
        if (_bean != null) {
            ((CIEcritureViewBean) viewBean).initEcriture(_bean);
        }
        return viewBean;
    }

    protected FWViewBeanInterface beforeSupprimerSuspens(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {

        // L'écriture est désormais à SUSPENS_SUPPRIMER
        CIEcritureViewBean ecriture = (CIEcritureViewBean) viewBean;
        ecriture.setIdTypeCompte(CIEcriture.CS_CI_SUSPENS_SUPPRIMES);
        return ecriture;
    }

    /**
     ** Parse la chaine de requête pour un tri sur le montant
     **/
    private String parseQuery(String query, CIEcritureListViewBean manager) {
        if (!JAUtil.isStringEmpty(query)) {
            String valueFrom = new String();
            String valueTo = new String();
            int separatorIndex = query.indexOf(",");
            int start = query.indexOf("[");
            int startStrict = query.indexOf("]");
            if ((start >= 0) && (startStrict >= 0)) {
                if (start < startStrict) {
                    valueFrom = query.substring(start + 1, separatorIndex);
                    valueTo = query.substring(separatorIndex + 1, startStrict);
                    query = manager.getCollection() + "CIECRIP.KBMMON >= " + valueFrom + " AND "
                            + manager.getCollection() + "CIECRIP.KBMMON <= " + valueTo;
                } else {
                    valueFrom = query.substring(startStrict + 1, separatorIndex);
                    valueTo = query.substring(separatorIndex + 1, start);
                    query = manager.getCollection() + "CIECRIP.KBMMON > " + valueFrom + " AND "
                            + manager.getCollection() + "CIECRIP.KBMMON < " + valueTo;
                }
            } else if ((start < 0) && (startStrict >= 0)) {
                valueFrom = query.substring(startStrict + 1, separatorIndex);
                startStrict = query.indexOf("]", startStrict + 1);
                valueTo = query.substring(separatorIndex + 1, startStrict);
                query = manager.getCollection() + "CIECRIP.KBMMON > " + valueFrom + " AND " + manager.getCollection()
                        + "CIECRIP.KBMMON <= " + valueTo;
            } else if ((start >= 0) && (startStrict < 0)) {
                valueFrom = query.substring(start + 1, separatorIndex);
                start = query.indexOf("[", start + 1);
                valueTo = query.substring(separatorIndex + 1, start);
                query = manager.getCollection() + "CIECRIP.KBMMON >= " + valueFrom + " AND " + manager.getCollection()
                        + "CIECRIP.KBMMON < " + valueTo;
            } else if ((start < 0) && (startStrict < 0)) {
                query = manager.getCollection() + "CIECRIP.KBMMON = " + query;
            }
        }
        return query;
    }
}
