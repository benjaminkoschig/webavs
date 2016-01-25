package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JAUtil;
import globaz.pavo.db.compte.CICompteIndividuelViewBean;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureEclaterViewBean;
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
public class CIActionEcriture extends FWDefaultServletAction {
    private FWAction _action = null;
    /**
     * Commentaire relatif au constructeur CIActionEcriture.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */

    private String _destination = "";

    public CIActionEcriture(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    /*
     * protected void actionAjouter(HttpSession session,HttpServletRequest request, HttpServletResponse response,
     * FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException { try { /* recuperation
     * du bean depuis la session
     */
    /*
     * FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
     * 
     * /* set automatique des proprietes
     */
    // globaz.globall.http.JSPUtils.setBeanProperties(request,viewBean);

    /*
     * beforeAdd() call du dispatcher, puis mis en session
     */
    /*
     * viewBean = beforeAjouter(session,request,response,viewBean); FWAction action = getAction();
     * action.setRight(FWSecureConstants.READ); viewBean = mainDispatcher.dispatch(viewBean, action);
     * session.setAttribute ("viewBean", viewBean);
     * 
     * /* chois de la destination _valid=fail : revient en mode edition _back=sl : sans effacer les champs deja rempli
     * par l'utilisateur
     */
    /*
     * if (viewBean.getMsgType().equals(viewBean.ERROR) == true) { _destination=
     * getRelativeURL(request,session)+"_de.jsp?_valid=fail&_back=sl"; } else {
     * 
     * _destination = FWScenarios.getInstance().getDestination((String)session.getAttribute (FWScenarios
     * .SCENARIO_ATTRIBUT),FWAction.newInstance(request.getParameter( "userAction")),viewBean); if
     * (globaz.globall.util.JAUtil.isStringEmpty(_destination)) { //request.setAttribute
     * ("selectedId",((CIEcritureViewBean)viewBean).getId()); //_destination = getActionFullURL
     * ()+".afficher&selectedId="+((CIEcritureViewBean)viewBean).getId(); _destination=
     * getRelativeURL(request,session)+"_de.jsp";
     * //_destination="pavo?userAction=pavo.compte.ecriture.afficher&selectedId"
     * +((CIEcritureViewBean)viewBean).getId(); } } } catch (Exception e) { _destination = ERROR_PAGE; }
     * 
     * /* redirection vers la destination
     */

    // }

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
            session.setAttribute("viewBeanEcriture", viewBean);

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

    protected void _actionAjouterSuccesEcriture(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        servlet.getServletContext()
                .getRequestDispatcher(getRelativeURL(request, session) + "_caDetail.jsp?_result=ADDED")
                .forward(request, response);
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
            session.setAttribute("viewBeanEcriture", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = getActionFullURL() + ".reAfficherDetail";
            } else {
                destination = getActionFullURL() + ".ajouterSucces";
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
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

        // corrections pour chercher écritures depuis le journal
        CIEcritureViewBean viewBean2 = new CIEcritureViewBean();
        session.setAttribute("viewBean", viewBean2);

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
            String method = request.getParameter("_method");
            if (journalId == null || journalId.length() == 0 || method != null) {
                try {
                    viewBean = (CIJournalViewBean) session.getAttribute("viewBean");
                } catch (Exception ex) {
                    // n'existe pas ou incorrect
                }
            }
            // } else {
            if (viewBean == null || viewBean.getIdJournal().length() == 0) {
                viewBean = new CIJournalViewBean();
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            }
            // }
            // viewBean.setFromAvsSauvegarde(request.getParameter("fromAvs"));
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

    protected void _actionComptabiliser(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, boolean nocheck) throws javax.servlet.ServletException, java.io.IOException {
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
                _destination = getRelativeURL(request, session) + "_rc.jsp";
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionCopie(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, boolean nocheck) throws javax.servlet.ServletException, java.io.IOException {
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
                if (nocheck) {
                    _destination = getRelativeURL(request, session) + "_de.jsp?_method=add&modeAjout=extourne";
                } else {
                    _destination = getRelativeURL(request, session) + "_de.jsp?_method=add";
                }
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionEclate(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, boolean nocheck) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
            CIEcritureEclaterViewBean viewBean = new CIEcritureEclaterViewBean();
            viewBean.setEcritureId(request.getParameter("selectedId"));
            viewBean.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
            viewBean.retrieve();
            if (!viewBean.canEclateIncription()) {
                CIEcritureViewBean viewBean2 = new CIEcritureViewBean();
                viewBean2.setEcritureId(viewBean.getEcritureId());
                viewBean2.setSession(viewBean.getSession());
                viewBean2.retrieve();
                // JSPUtils.setBeanProperties(request, viewBean2);

                // JSPUtils.setBeanProperties(request, viewBean2);
                FWAction action = getAction();
                // viewBean.retrieve();
                // viewBean.setPathRoot(servlet.getServletContext().getResource(action.getApplicationPart()+"Root").getFile());
                // action.setRight(FWSecureConstants.READ);
                // appel du controlleur
                viewBean2 = (CIEcritureViewBean) mainDispatcher.dispatch(viewBean2, action);
                // viewBean2.canEclateIncription();

                // sauve le bean dans la session
                session.setAttribute("viewBean", viewBean2);
                // _destination=getAction().getApplicationPart().toString()+"?userAction=pavo.compte.ecriture.afficher&selectedId="+viewBean2.getEcritureId();
                _destination = getRelativeURL(request, session) + "_de.jsp";
                // viewBean2.getSession().addError("error");
            } else {
                // viewBean.setFromIdJournal(request.getParameter("selectedId"));
                JSPUtils.setBeanProperties(request, viewBean);
                FWAction action = getAction();
                // viewBean.retrieve();
                // viewBean.setPathRoot(servlet.getServletContext().getResource(action.getApplicationPart()+"Root").getFile());
                // action.setRight(FWSecureConstants.READ);
                // appel du controlleur
                viewBean = (CIEcritureEclaterViewBean) mainDispatcher.dispatch(viewBean, action);
                // sauve le bean dans la session
                session.setAttribute("viewBean", viewBean);
                // redirection vers destination
                _destination = getRelativeURL(request, session) + "Eclater_de.jsp?_method=add";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionEclateExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, boolean nocheck) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
            CIEcritureEclaterViewBean viewBean = new CIEcritureEclaterViewBean();
            // viewBean.setFromIdJournal(request.getParameter("selectedId"));
            JSPUtils.setBeanProperties(request, viewBean);
            FWAction action = getAction();
            // viewBean.setPathRoot(servlet.getServletContext().getResource(action.getApplicationPart()+"Root").getFile());
            // action.setRight(FWSecureConstants.READ);
            // appel du controlleur
            viewBean = (CIEcritureEclaterViewBean) mainDispatcher.dispatch(viewBean, action);
            // sauve le bean dans la session
            session.setAttribute("viewBean", viewBean);
            /*
             * CICompteIndividuel ciACopier = new CICompteIndividuel(); ciACopier.setSession((BSession)
             * viewBean.getISession()); ciACopier.setId(viewBean.getCompteIndividuelId());
             * 
             * try { ciACopier.retrieve(); ciACopier.extournerCi(viewBean.getCompteIndividuelIdDestination ()); } catch
             * (Exception e) { }
             */

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                viewBean.setEcritureId(viewBean.getEcritureIdAEclater());
                viewBean.retrieve();
                _destination = getActionFullURL() + ".reAfficherEclate";
            } else {
                viewBean.setEcritureId(viewBean.getEcritureIdAEclater());
                viewBean.retrieve();
                _destination = getAction().getApplicationPart().toString()
                        + "?userAction=pavo.compte.ecriture.chercherEcriture&compteIndividuelId="
                        + viewBean.getCompteIndividuelId();
            }
            // _destination=getAction()+"ecriture.chercherEcriture&compteIndividuelId="+
            // viewBean.getCompteIndividuelId();

            // _destination="/pavoRoot/FR/compte.ecriture.chercherEcriture&compteIndividuelId=";
            goSendRedirect(_destination, request, response);

        } catch (Exception ex) {

        }

    }

    protected void _actionFindNext(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBeanEcrit");

            FWAction _action = new FWRequestActionAdapter().adapt(request, FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            ((CIEcritureListViewBean) viewBean).findNext();
            request.setAttribute("viewBeanEcrit", viewBean);

            _destination = getRelativeURL(request, session) + "_caPage.jsp";
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionFindPrevious(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBeanEcrit");

            FWAction _action = new FWRequestActionAdapter().adapt(request, FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            ((CIEcritureListViewBean) viewBean).findPrev();
            request.setAttribute("viewBeanEcrit", viewBean);

            _destination = getRelativeURL(request, session) + "_caPage.jsp";
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionListerSurPage(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";
        try {
            FWViewBeanInterface viewBean = FWListViewBeanActionFactory.newInstance(getAction(),
                    mainDispatcher.getPrefix());
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            /*
             * Check des critères On ne fait de plausi sur les critères de séléction si c'est un new journal etant
             * donnée qu'on affiche une ecriture en mode edition
             */

            boolean criteresOk = true;
            // if(!"true".equals(request.getParameter("addNewEcriture"))){

            // criteresOk = _criteresOk(request);
            // }

            viewBean = beforeListerSurPage(session, request, response, viewBean);
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);

            session.setAttribute("viewBeanEcrit", viewBean);
            //
            CIJournalViewBean viewBeanHead = (CIJournalViewBean) session.getAttribute("viewBean");
            viewBean = mainDispatcher.dispatch(viewBean, action);
            viewBeanHead.setFromAvsSauvegardeNNSS(request.getParameter("fromAvsNNSS"));
            viewBeanHead.setFromAvsSauvegarde(request.getParameter("fromAvs"));
            viewBeanHead.setForMontantSauvegarde(request.getParameter("queryRevenu"));
            viewBeanHead.setForAnneeSauvegarde(request.getParameter("forAnnee"));
            viewBeanHead.setForPremiereFoisSurPage(request.getParameter("premiereFoisSurPage"));
            //

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
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBeanEcriture");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean = beforeModifier(session, request, response, viewBean);
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBeanEcriture", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = getActionFullURL() + ".reAfficherDetail";
            } else {
                // destination = getRelativeURL(request,session) +
                // "_caDetail.jsp?_result=UPDATED";
                destination = getActionFullURL() + ".chercherSurPage";
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }
        goSendRedirect(destination, request, response);
    }

    protected void _actionReAfficherDetail(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        servlet.getServletContext()
                .getRequestDispatcher(getRelativeURL(request, session) + "_caDetail.jsp?_valid=ERROR")
                .forward(request, response);
    }

    protected void _actionReAfficherEclate(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        servlet.getServletContext()
                .getRequestDispatcher(getRelativeURL(request, session) + "Eclater_de.jsp?_valid=fail&_back=sl")
                .forward(request, response);
    }

    protected void _actionSupprimerSurPage(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = ERROR_PAGE;
        try {
            getAction().changeActionPart(FWAction.ACTION_SUPPRIMER);

            // recuperation du bean depuis la session
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBeanEcriture");

            viewBean = beforeSupprimer(session, request, response, viewBean);
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBeanEcriture", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = getActionFullURL() + ".reAfficherDetail";
                request.setAttribute("_method", "add");
            } else {
                destination = getActionFullURL() + ".chercherSurPage";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        goSendRedirect(destination, request, response);

    }

    // Met à jour une inscription en SUSPENS vers SUPPRIMER_SUSPENS
    protected void _actionSupprimerSuspens(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String destination = "";
        try {
            getAction().changeActionPart(FWAction.ACTION_MODIFIER);
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBeanEcriture");
            // Pour mettre à jour le type de compte
            viewBean = beforeSupprimerSuspens(session, request, response, viewBean);
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBeanEcriture", viewBean);

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

    /**
     * critères obligatoires (au moins 1 parmis la liste)
     */
    private boolean _criteresOk(HttpServletRequest request) {
        boolean res = false;
        String[] criteres = new String[] { "fromAvs", "forAnnee", "queryRevenu" };
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < criteres.length; i++) {
            buf.append(request.getParameter(criteres[i]));
        }
        return !JAUtil.isStringEmpty(buf.toString().trim());
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

        } else if (getAction().getActionPart().equals("comptabiliser")) {
            // comptabilise l'écriture
            _actionComptabiliser(session, request, response, dispatcher, true);
        } else if (getAction().getActionPart().equals("extourne")) {
            // extourne l'écriture
            _actionCopie(session, request, response, dispatcher, true);

        } else if (getAction().getActionPart().equals("copie")) {
            // copie l'écriture
            _actionCopie(session, request, response, dispatcher, false);
        } else if (getAction().getActionPart().equals("eclate")) {
            // copie l'écriture
            _actionEclate(session, request, response, dispatcher, false);
        } else if (getAction().getActionPart().equals("eclaterExecuter")) {
            // copie l'écriture
            _actionEclateExecuter(session, request, response, dispatcher, false);
        } else if (getAction().getActionPart().equals("suivantPerso")) {
            // suivant pour les journaux
            _actionFindNext(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("precedantPerso")) {
            // précedent pour les journaux
            _actionFindPrevious(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("ajouterSucces")) {
            _actionAjouterSuccesEcriture(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("reAfficherDetail")) {
            _actionReAfficherDetail(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("reAfficherEclate")) {
            _actionReAfficherEclate(session, request, response, dispatcher);
        }
    }

    protected FWViewBeanInterface beforeListerSurPage(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        ((BManager) viewBean).changeManagerSize(15);
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
