package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planCaisse.AFPlanCaisse;
import globaz.naos.db.planCaisse.AFPlanCaisseListViewBean;
import globaz.naos.db.wizard.AFWizard;
import globaz.naos.db.wizard.AFWizardViewBean;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFVerrouAffiliation;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour la saisie accélérée d'Affiliation(s).
 * 
 * @author sau
 */
public class AFActionWizard extends AFIdeDefaultActionChercher {

    public final static String ACTION_AFFICHER_CREATION_PLANAFFILIATION = "afficherCreationPlanAffiliation";
    public final static String ACTION_AFFICHER_SAISIE_AFFILIATION = "afficherSaisieAffiliation";
    public final static String ACTION_AFFICHER_SELECTION_COTISATION = "afficherSelectionCotisation";
    public final static String ACTION_AFFICHER_SELECTION_PLANCAISSE = "afficherSelectionPlanCaisse";
    public final static String ACTION_AJOUTER_AFFILIATION = "ajouterAffiliation";
    public final static String ACTION_AJOUTER_PLANAFFILIATION = "ajouterPlanAffiliation";

    /**
     * Constructeur d'AFActionWizard.
     * 
     * @param servlet
     */
    public AFActionWizard(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Action pour afficher l'écran de saisie des paramètres de l'Affiliation. Page 01
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @param mainDispatcher
     * @throws ServletException
     * @throws IOException
     */
    protected void actionAfficherSaisieAffiliation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws ServletException, IOException {

        String _destination = "";
        AFWizardViewBean viewBean = new AFWizardViewBean();
        try {
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            Object vBean = session.getAttribute("viewBean");

            if ((!JadeStringUtil.isBlank((String) session.getAttribute("tiersPrincipale")))
                    && (JadeStringUtil.isBlankOrZero(viewBean.getIdTiers()))) {
                viewBean.setIdTiers((String) session.getAttribute("tiersPrincipale"));
            }

            if ((vBean != null) && (vBean instanceof AFWizardViewBean)) {
                String from = request.getParameter("wizardPage");
                if (from != null) {
                    viewBean = (AFWizardViewBean) session.getAttribute("viewBean");
                    viewBean.saveAffiliationProperties(false);
                    JSPUtils.setBeanProperties(request, viewBean);
                    viewBean.saveAffiliationProperties(true);

                    if (from.equals("02")) {
                        BSession bSession = (BSession) CodeSystem.getSession(session);
                        updatePlanCaisse(bSession, viewBean, request);
                    } else if ((from != null) && from.equals("04")) {
                        viewBean = updateCotisation(viewBean, request);
                    }
                }

            } else {
                viewBean.setPersonnaliteJuridique(CodeSystem.PERS_JURIDIQUE_NA);
                viewBean.setMotifCreation(CodeSystem.MOTIF_AFFIL_NOUVELLE_AFFILIATION);
            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        getAction().setRight(FWSecureConstants.ADD);
        viewBean = (AFWizardViewBean) dispatcher.dispatch(viewBean, getAction());

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } else {
            _destination = getRelativeURLwithoutClassPart(request, session) + "wizard01_de.jsp?_method=add";
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionChoisir(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        try {
            startUsingContext(session);
            super.actionChoisir(session, request, response, mainDispatcher);
        } catch (Exception e) {
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }

    }

    /**
     * Traitement des actions non standard.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        try {
            startUsingContext(session);
            if (AFActionWizard.ACTION_AFFICHER_SAISIE_AFFILIATION.equals(getAction().getActionPart())) {
                actionAfficherSaisieAffiliation(session, request, response, dispatcher);
            } else if (AFActionWizard.ACTION_AFFICHER_SELECTION_PLANCAISSE.equals(getAction().getActionPart())) {
                afficherSelectionPlanCaisse(session, request, response);
            } else if (AFActionWizard.ACTION_AFFICHER_CREATION_PLANAFFILIATION.equals(getAction().getActionPart())) {
                afficherCreationPlanAffiliation(session, request, response);
            } else if (AFActionWizard.ACTION_AJOUTER_PLANAFFILIATION.equals(getAction().getActionPart())) {
                ajouterPlanAffiliation(session, request, response);
            } else if (AFActionWizard.ACTION_AFFICHER_SELECTION_COTISATION.equals(getAction().getActionPart())) {
                afficherSelectionCotisation(session, request, response);
            } else if (AFActionWizard.ACTION_AJOUTER_AFFILIATION.equals(getAction().getActionPart())) {
                ajouterAffiliation(session, request, response);
            } else {
                super.actionCustom(session, request, response, dispatcher);
            }
        } catch (Exception e) {
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    /**
     * Action pour afficher l'écran de création du(des) Plan(s) d'affiliation.
     * 
     * @param session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void afficherCreationPlanAffiliation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        int cas = 1;
        String _destination = "";

        AFWizardViewBean viewBean = new AFWizardViewBean();
        try {

            Object vBean = session.getAttribute("viewBean");

            if ((vBean != null) && (vBean instanceof AFWizardViewBean)) {
                viewBean = (AFWizardViewBean) session.getAttribute("viewBean");
                viewBean.saveAffiliationProperties(false);
                JSPUtils.setBeanProperties(request, viewBean);

                BSession bSession = (BSession) CodeSystem.getSession(session);

                String from = request.getParameter("wizardPage");
                if ((from != null) && from.equals("02")) {
                    updatePlanCaisse(bSession, viewBean, request);
                }

                if (viewBean.getAdhesionCotisationList().size() == 0) {

                    // Pas de plan de caisse selectionné
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(bSession.getLabel("1540"));

                    // Preparer la Liste des plans de caisse
                    AFPlanCaisseListViewBean planCaisseList = new AFPlanCaisseListViewBean();
                    planCaisseList.setSession(bSession);
                    planCaisseList.setOrder("HBCADM");
                    planCaisseList.changeManagerSize(BManager.SIZE_NOLIMIT);
                    planCaisseList.find();
                    request.setAttribute("planCaisseListViewBean", planCaisseList);
                } else {

                    if ((from != null) && from.equals("02")) {

                        // Initialisation du plan d'affiliation
                        String defaultLibelle = (viewBean.getAffilieNumero() + "_" + viewBean.getDateDebut()).trim();
                        if (viewBean.getPlanAffiliation().size() == 0) {

                            viewBean.addPlanAffiliationLibelle(defaultLibelle);

                        } else if (viewBean.getPlanAffiliation().size() >= 1) {

                            AFPlanAffiliation planAffiliation = viewBean.getPlanAffiliation().get(0);

                            if ((planAffiliation.getLibelle().indexOf("_") != -1)
                                    && planAffiliation.getLibelle().substring(0, defaultLibelle.indexOf("_"))
                                            .equals(viewBean.getAffilieNumero())
                                    && (planAffiliation.getLibelle().length() == defaultLibelle.length())) {
                                planAffiliation.setLibelle(defaultLibelle);
                                viewBean.getPlanAffiliation().set(0, planAffiliation);
                            }
                        }
                    } else if ((from != null) && from.equals("04")) {
                        viewBean = updateCotisation(viewBean, request);
                    }
                }
            } else {
                cas = 2;
            }

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
            cas = 3;
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (cas == 1) {
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = getRelativeURLwithoutClassPart(request, session)
                        + "wizard02_de.jsp?_valid=fail&_back=sl";
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "wizard03_de.jsp?_method=add";
            }
        } else if (cas == 2) {
            _destination = getRelativeURLwithoutClassPart(request, session) + "wizard01_de.jsp?_method=add";
        } else if (cas == 3) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action pour afficher l'ecran de séléction des cotisations définie par le Plan de Caisse. Page 04
     * 
     * @param session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void afficherSelectionCotisation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        int cas = 1;
        String _destination = "";

        AFWizardViewBean viewBean = new AFWizardViewBean();
        try {
            Object vBean = session.getAttribute("viewBean");
            if ((vBean != null) && (vBean instanceof AFWizardViewBean)) {
                viewBean = (AFWizardViewBean) session.getAttribute("viewBean");
                viewBean = updatePlanAffiliation(viewBean, request);

                if (!validatePlanAffiliation(viewBean)) {
                    BSession bSession = (BSession) CodeSystem.getSession(session);

                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(bSession.getLabel("1550"));
                } else {
                    viewBean.creationCotisation();
                }
            } else {
                cas = 2;
            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
            cas = 3;
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (cas == 1) {
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = getRelativeURLwithoutClassPart(request, session)
                        + "wizard03_de.jsp?_valid=fail&_back=sl";
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "wizard04_de.jsp?_method=add";
            }
        } else if (cas == 2) {
            _destination = getRelativeURLwithoutClassPart(request, session) + "wizard01_de.jsp?_method=add";
        } else if (cas == 3) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action pour afficher l'écran de sélection des Plans de Caisse. Page 02
     * 
     * @param session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void afficherSelectionPlanCaisse(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        int cas = 1;
        String _destination = "";
        AFWizardViewBean viewBean = new AFWizardViewBean();

        try {
            Object vBean = session.getAttribute("viewBean");
            if ((vBean != null) && (vBean instanceof AFWizardViewBean)) {
                viewBean = (AFWizardViewBean) session.getAttribute("viewBean");

                String from = request.getParameter("wizardPage");
                if ((from != null) && from.equals("01")) {
                    viewBean.saveAffiliationProperties(true);
                    JSPUtils.setBeanProperties(request, viewBean);
                    String error = viewBean.validateAffiliation();
                    if (!JadeStringUtil.isEmpty(error)) {
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        viewBean.setMessage(error);
                    }
                } else if ((from != null) && from.equals("03")) {
                    viewBean.saveAffiliationProperties(false);
                    JSPUtils.setBeanProperties(request, viewBean);
                    updatePlanAffiliation(viewBean, request);
                } else if ((from != null) && from.equals("02")) {
                    // relaod
                    viewBean.saveAffiliationProperties(false);
                    JSPUtils.setBeanProperties(request, viewBean);
                    BSession bSession = (BSession) CodeSystem.getSession(session);
                    updatePlanCaisse(bSession, viewBean, request);
                }

                // tri sur no caisse
                String noCaisse = request.getParameter("fromNoCaisse");
                if (JadeStringUtil.isEmpty(noCaisse)) {
                    noCaisse = viewBean.getFromNoCaisse();
                }
                // Preparer la Liste des plans de caisse
                AFPlanCaisseListViewBean planCaisseList = new AFPlanCaisseListViewBean();
                planCaisseList.setSession((BSession) CodeSystem.getSession(session));
                planCaisseList.setFromNumeroCaisse(noCaisse);
                planCaisseList.setForTypeAffiliation(viewBean.getTypeAffiliation());
                planCaisseList.setOrder("HBCADM");
                planCaisseList.changeManagerSize(BManager.SIZE_NOLIMIT);
                planCaisseList.find();
                request.setAttribute("planCaisseListViewBean", planCaisseList);
            } else {
                cas = 2;
            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
            cas = 3;
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (cas == 1) {
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = getRelativeURLwithoutClassPart(request, session)
                        + "wizard01_de.jsp?_valid=fail&_back=sl";
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "wizard02_de.jsp?_method=add";
            }
        } else if (cas == 2) {
            _destination = getRelativeURLwithoutClassPart(request, session) + "wizard01_de.jsp?_method=add";
        } else if (cas == 3) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action pour ajouter l'affiliation, l'adhesion, le(s) Plan d'affiliation et les Cotisations dans la DB.
     * 
     * @param session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void ajouterAffiliation(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String _destination = "";
        // Met un verrou sur la création d'affiliations
        synchronized (AFVerrouAffiliation.getInstance()) {
            int cas = 1;

            AFWizardViewBean viewBean = new AFWizardViewBean();
            try {
                Object vBean = session.getAttribute("viewBean");
                if ((vBean != null) && (vBean instanceof AFWizardViewBean)) {
                    viewBean = (AFWizardViewBean) session.getAttribute("viewBean");
                    viewBean = updateCotisation(viewBean, request);
                    // if(!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getIdTiers())){
                    /*
                     * TITiers tiers = new TITiers(); tiers.setIdTiers(viewBean.getIdTiers());
                     * tiers.setSession(viewBean.getSession()); tiers.retrieve(); String tmpNom = tiers.getNom();
                     * viewBean.setRaisonSociale(tiers.getNom()); viewBean.setRaisonSocialeCourt(tiers.getNom()); if
                     * (tmpNom.length() > 100) { viewBean.setRaisonSociale(tiers.getNom().substring(0, 100));
                     * viewBean.setRaisonSocialeCourt(tiers.getNom().substring(0, 30)); } else {
                     * viewBean.setRaisonSociale(tiers.getNom()); if (tiers.getNom().length() > 30) {
                     * viewBean.setRaisonSocialeCourt(tiers.getNom().substring(0, 30)); } else {
                     * viewBean.setRaisonSocialeCourt(tiers.getNom()); } }
                     */
                    // }
                    viewBean.ajouterAffiliation();

                    if (viewBean.getSession().hasErrors()) {
                        viewBean = updateCotisation(viewBean, request);
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        viewBean.setMessage(viewBean.getSession().getErrors().toString());
                    } else {
                        viewBean.setMsgType(FWViewBeanInterface.OK);
                        viewBean.setMessage("");
                    }

                } else {
                    cas = 2;
                }
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            if (cas == 1) {
                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                    _destination = getRelativeURLwithoutClassPart(request, session) + "wizard04_de.jsp?_valid=fail";
                } else {
                    session.removeAttribute("affiliationPrincipale");
                    session.setAttribute("affiliationPrincipale", viewBean.getAffiliationId());

                    _destination = "/" + getAction().getApplicationPart()
                            + "?userAction=naos.affiliation.affiliation.afficher&_method=na";
                }
            } else if (cas == 2) {
                _destination = getRelativeURLwithoutClassPart(request, session) + "wizard01_de.jsp?_method=add";
            }
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Ajouter un Plan D'affiliation.
     * 
     * @param session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void ajouterPlanAffiliation(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int cas = 1;
        String _destination = "";

        AFWizardViewBean viewBean = viewBean = new AFWizardViewBean();
        Object vBean = session.getAttribute("viewBean");
        if ((vBean != null) && (vBean instanceof AFWizardViewBean)) {
            viewBean = (AFWizardViewBean) session.getAttribute("viewBean");
            viewBean = updatePlanAffiliation(viewBean, request);
            viewBean.addPlanAffiliationLibelle(new String());
        } else {
            cas = 2;
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (cas == 1) {
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "wizard03_de.jsp?_method=add";
            }
        } else if (cas == 2) {
            _destination = getRelativeURLwithoutClassPart(request, session) + "wizard01_de.jsp?_method=add";
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private JadeContext getContext(HttpSession session) throws Exception {
        JadeContext context = null;

        // Si la session contient déjà le context et que celui-ci est bien une
        // instance de JadeThreadContext, alors le récupère, sinon, le crée
        if ((session.getAttribute("JADE_THREAD_CONTEXT") != null)
                && (session.getAttribute("JADE_THREAD_CONTEXT") instanceof JadeThreadContext)) {
            context = (JadeContext) session.getAttribute("JADE_THREAD_CONTEXT");
        } else {
            BSession bSession = (BSession) ((globaz.framework.controller.FWController) session
                    .getAttribute(FWServlet.OBJ_CONTROLLER)).getSession();
            context = initContext(bSession);
            session.setAttribute("JADE_THREAD_CONTEXT", context);
        }

        return context;
    }

    private JadeContext initContext(BSession session) throws Exception {
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId("fx");
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        return ctxtImpl;
    }

    private void setTypeAdhesion(String caissePrinc, AFPlanCaisse planCaisse, AFWizard.AFAdhesionCotisation newAdCoti) {
        if (JadeStringUtil.isEmpty(caissePrinc) || !caissePrinc.equals(planCaisse.getPlanCaisseId())) {
            newAdCoti.adhesion.setTypeAdhesion(CodeSystem.TYPE_ADHESION_CAISSE);
            // System.out.println(planCaisse.getLibelle()+": caisse");
        } else {
            newAdCoti.adhesion.setTypeAdhesion(CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE);
            // System.out.println(planCaisse.getLibelle()+": caisse principale");
        }
    }

    private void startUsingContext(HttpSession session) throws SQLException, Exception {
        JadeThreadActivator.startUsingJdbcContext(this, getContext(session));
        JadeThread.storeTemporaryObject("bsession", ((globaz.framework.controller.FWController) session
                .getAttribute(FWServlet.OBJ_CONTROLLER)).getSession());
    }

    /**
     * Mettre à jour la liste des Cotisations et du Plan d'Affiliation séléctionnés.
     * 
     * @param viewBean
     * @param request
     * @return le ViewBean mis à jour.
     */
    private AFWizardViewBean updateCotisation(AFWizardViewBean viewBean, HttpServletRequest request) throws Exception {

        List adhesionList = viewBean.getAdhesionCotisationList();
        int m = 0;
        for (int i = 0; i < adhesionList.size(); i++) {
            AFWizard.AFAdhesionCotisation adheCoti = (AFWizard.AFAdhesionCotisation) adhesionList.get(i);

            List cotisationList = adheCoti.cotisationList;
            List cotisationToAddList = adheCoti.cotisationToAddList;
            for (int j = 0; j < cotisationList.size(); j++) {

                AFCotisation cotisation = (AFCotisation) cotisationList.get(j);

                String planAffiliationSelectedId = request.getParameter("planAffiliationSelectedId" + i + "_" + j);
                if (planAffiliationSelectedId != null) {
                    cotisation.setPlanAffiliationId(planAffiliationSelectedId);

                    String cotisationToAdd = request.getParameter("cotisationToAdd" + i + "_" + j);
                    if ((cotisationToAdd != null) && cotisationToAdd.equals("on")) {
                        cotisationToAddList.set(j, new Boolean(true));
                    } else {
                        cotisationToAddList.set(j, new Boolean(false));
                    }

                    String periodiciteNouvelleMasse = request.getParameter("periodiciteNouvelleMasse" + m);
                    String nouvelleMasse = request.getParameter("nouvelleMasse" + m);
                    cotisation.setPeriodicite(request.getParameter("nouvellePeriodicite" + m));

                    if ((periodiciteNouvelleMasse != null) && !JadeStringUtil.isEmpty(periodiciteNouvelleMasse)
                            && (nouvelleMasse != null) && !JadeStringUtil.isEmpty(nouvelleMasse)) {

                        cotisation.updateMasseAnnuelle(periodiciteNouvelleMasse, nouvelleMasse);
                    }
                }
                m = m + 1;
            } // end for
        }
        return viewBean;
    }

    /**
     * Mettre à jour la liste du (des) Plan(s) d'affiliation.
     * 
     * @param viewBean
     * @param request
     * @return le ViewBean mis à jour.
     */
    private AFWizardViewBean updatePlanAffiliation(AFWizardViewBean viewBean, HttpServletRequest request) {

        List planAffiliationList = viewBean.getPlanAffiliation();

        // Suppression ou mise à jour des Plan d'affiliation
        for (int i = planAffiliationList.size() - 1; i >= 0; i--) {

            String planAffiliationDelete = request.getParameter("planAffiliationDelete" + Integer.toString(i));
            if ((planAffiliationDelete != null) && planAffiliationDelete.equals("on")) {
                planAffiliationList.remove(i);
            } else {
                String planAffiliationLibelle = request.getParameter("planAffiliationLibelle" + Integer.toString(i));
                String planAffiliationLibelleFacture = request.getParameter("planAffiliationLibelleFacture"
                        + Integer.toString(i));
                if (planAffiliationLibelle != null) {
                    AFPlanAffiliation planAffiliation = (AFPlanAffiliation) planAffiliationList.get(i);
                    planAffiliation.setLibelle(planAffiliationLibelle);
                    planAffiliation.setLibelleFacture(planAffiliationLibelleFacture);
                }
            }
        } // end for
        return viewBean;
    }

    /**
     * Mettre à jour la liste du (des) Plan(s) de caisse
     * 
     * @param session
     * @param viewBean
     * @param request
     * @return
     * @throws Exception
     */
    private AFWizardViewBean updatePlanCaisse(BSession session, AFWizardViewBean viewBean, HttpServletRequest request)
            throws Exception {

        // Preparer la Liste des plans de caisse
        AFPlanCaisseListViewBean planCaisseList = new AFPlanCaisseListViewBean();
        planCaisseList.setSession(session);
        planCaisseList.setOrder("HBCADM");
        planCaisseList.changeManagerSize(BManager.SIZE_NOLIMIT);
        planCaisseList.find();

        // Supprimmer les plans de caisse plus sélectionné
        List adhesionCotiList = viewBean.getAdhesionCotisationList();
        for (int i = adhesionCotiList.size() - 1; i >= 0; i--) {
            AFWizard.AFAdhesionCotisation adhesionCoti = (AFWizard.AFAdhesionCotisation) adhesionCotiList.get(i);
            String selected = request.getParameter("planCaisseId" + adhesionCoti.adhesion.getPlanCaisseId());
            if (!JadeStringUtil.isEmpty(selected) && "off".equals(selected)) {
                adhesionCotiList.remove(i);
            }
        }

        String caissePrinc = request.getParameter("caissePrincipale");
        // System.out.println("caisse princ = "+caissePrinc);
        for (int i = 0; i < planCaisseList.size(); i++) {
            AFPlanCaisse planCaisse = (AFPlanCaisse) planCaisseList.get(i);
            String selected = request.getParameter("planCaisseId" + planCaisse.getPlanCaisseId());
            if (!JadeStringUtil.isEmpty(selected) && selected.equalsIgnoreCase("on")) {
                // Check in already created

                boolean alreadyInList = false;
                adhesionCotiList = viewBean.getAdhesionCotisationList();
                for (int j = 0; j < adhesionCotiList.size(); j++) {
                    AFWizard.AFAdhesionCotisation adhesionCoti = (AFWizard.AFAdhesionCotisation) adhesionCotiList
                            .get(j);
                    if (adhesionCoti.adhesion.getPlanCaisseId().equals(planCaisse.getPlanCaisseId())) {
                        setTypeAdhesion(caissePrinc, planCaisse, adhesionCoti);
                        alreadyInList = true;
                        break;
                    }
                }

                if (!alreadyInList) {
                    AFWizard.AFAdhesionCotisation newAdCoti = new AFWizard.AFAdhesionCotisation();
                    newAdCoti.adhesion.setPlanCaisseId(planCaisse.getPlanCaisseId());
                    setTypeAdhesion(caissePrinc, planCaisse, newAdCoti);
                    newAdCoti.planCaisseLabel = planCaisse.getLibelle();
                    adhesionCotiList.add(newAdCoti);
                }
            }
        }
        viewBean.setAdhesionCotisationList(adhesionCotiList);
        return viewBean;
    }

    /**
     * Valider les paramètres saisis dans l'écran de création du (des) Plan(s) d'Affiliation.
     * 
     * @param viewBean
     * @param request
     * @return TRUE - si les paramètre sont valide.
     */
    private boolean validatePlanAffiliation(AFWizardViewBean viewBean) {

        boolean validationOK = true;

        List planAffiliationList = viewBean.getPlanAffiliation();

        // Control si il n'y a pas 2 fois le meme Libelle
        for (int i = 0; i < planAffiliationList.size(); i++) {

            String libelleOne = ((AFPlanAffiliation) planAffiliationList.get(i)).getLibelle();

            if (libelleOne.trim().equals("")) {
                validationOK = false;
            }

            for (int j = i + 1; j < planAffiliationList.size(); j++) {

                String libelleTwo = ((AFPlanAffiliation) planAffiliationList.get(j)).getLibelle();

                if (libelleOne.equals(libelleTwo)) {
                    validationOK = false;
                }
            }
        }
        return validationOK;
    }
}
