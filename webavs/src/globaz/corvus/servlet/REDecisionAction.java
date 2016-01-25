package globaz.corvus.servlet;

import globaz.corvus.application.REApplication;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.utils.decisions.REDecisionsUtil;
import globaz.corvus.vb.decisions.KeyPeriodeInfo;
import globaz.corvus.vb.decisions.REAnnexeDecisionViewBean;
import globaz.corvus.vb.decisions.RECopieDecisionViewBean;
import globaz.corvus.vb.decisions.REDecisionJointDemandeRenteListViewBean;
import globaz.corvus.vb.decisions.REDecisionJointDemandeRenteViewBean;
import globaz.corvus.vb.decisions.REDecisionsICViewBean;
import globaz.corvus.vb.decisions.REPreValiderDecisionViewBean;
import globaz.corvus.vb.decisions.REPreparerDecisionStandardViewBean;
import globaz.corvus.vb.decisions.REPreparerDecisionViewBean;
import globaz.corvus.vb.demandes.REDemandeParametresRCDTO;
import globaz.corvus.vb.demandes.REDemandeRenteJointDemandeViewBean;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWMessage;
import globaz.fweb.taglib.FWSelectorTag;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.prestation.ged.PRGedAffichageDossier;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRHybridAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class REDecisionAction extends PRHybridAction {

    public static final String ACTION_AJOUTER_ANNEXE = "ajouter_annexe";
    public static final String ACTION_REAFFICHER = "reAfficher";
    public static final String ACTION_SUPPRIMER_ANNEXE = "supprimer_annexe";
    public static final String ACTION_SUPPRIMER_COPIE = "supprimer_copie";

    public static final String RETOUR_PYXIS = "retour_pyxis";

    private Integer nextKeyIdProvAnnexe = new Integer(0);
    private Integer nextKeyIdProvCopie = new Integer(0);

    public REDecisionAction(final FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestSupprimerEchec(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        return FWDefaultServletAction.ERROR_PAGE;
    }

    @Override
    protected void actionAfficher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "";
        try {

            FWViewBeanInterface inputVB = new REDecisionsICViewBean();
            ((REDecisionsICViewBean) inputVB).setIdDecision(request.getParameter("selectedId"));
            ((REDecisionsICViewBean) inputVB).setSession((BSession) mainDispatcher.getSession());
            ((REDecisionsICViewBean) inputVB).retrieve();

            inputVB = mainDispatcher.dispatch(inputVB, getAction());

            REPreValiderDecisionViewBean outputVB = new REPreValiderDecisionViewBean();
            outputVB.setISession(mainDispatcher.getSession());
            outputVB.setDecisionContainer(((REDecisionsICViewBean) inputVB).getDecisionsContainer());
            outputVB.setIdTiersRequerant(((REDecisionsICViewBean) inputVB).getIdTiersRequerant());
            outputVB.setIdDemandeRente(((REDecisionsICViewBean) inputVB).getIdDemandeRente());
            outputVB.setIdTiersBeneficiairePrincipal(((REDecisionsICViewBean) inputVB)
                    .getIdTiersBeneficiairePrincipal());
            outputVB.setIdDecision(((REDecisionsICViewBean) inputVB).getIdDecision());
            outputVB.setIdTierAdresseCourrier(((REDecisionsICViewBean) inputVB).getIdTiersAdrCourrier());
            outputVB.setIsInteretMoratoire(((REDecisionsICViewBean) inputVB).getIsRemInteretMoratoires());
            outputVB.setNssTiersBeneficiaire((PRTiersHelper.getTiersParId(mainDispatcher.getSession(),
                    ((REDecisionsICViewBean) inputVB).getIdTiersBeneficiairePrincipal()))
                    .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

            PRTiersWrapper tw = PRTiersHelper
                    .getTiersParId(mainDispatcher.getSession(), outputVB.getIdTiersRequerant());
            outputVB.setRequerantInfo(tw.getDescription((BSession) mainDispatcher.getSession()));

            tw = PRTiersHelper.getTiersParId(mainDispatcher.getSession(), outputVB.getIdTiersBeneficiairePrincipal());
            outputVB.setTiersBeneficiairePrincipalInfo(tw.getDescription((BSession) mainDispatcher.getSession()));

            beforeAfficher(session, request, response, outputVB);

            this.saveViewBean(outputVB, session);
            request.setAttribute(FWServlet.VIEWBEAN, outputVB);
            /*
             * choix destination
             */
            if (outputVB.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (outputVB.isDecisionSupprimable().booleanValue()) {
                    destination = getRelativeURLwithoutClassPart(request, session)
                            + "preValiderDecision_de.jsp?_method=add";
                } else {
                    destination = getRelativeURLwithoutClassPart(request, session) + "preValiderDecision_de.jsp";
                }

            }

            REDecisionsICViewBean vb = (REDecisionsICViewBean) inputVB;

            outputVB.setIsDepuisRcListDecision(Boolean.TRUE);
            outputVB.setCopiesList(outputVB.getDecisionContainer().getDecisionIC().getCopiesListDIC());
            outputVB.setAnnexesList(outputVB.getDecisionContainer().getDecisionIC().getAnnexesListDIC());
            outputVB.setHasRemarqueIncarceration(vb.getIsRemarqueIncarceration());
            outputVB.setHasRemarqueRenteDeVeufLimitee(vb.getIsRemarqueRenteDeVeufLimitee());
            outputVB.setHasRemarqueRenteDeVeuveLimitee(vb.getIsRemarqueRenteDeVeuveLimitee());
            outputVB.setHasRemarqueRemariageRenteDeSurvivant(vb.getIsRemarqueRemariageRenteDeSurvivant());
            outputVB.setHasRemarqueRentesPourEnfants(vb.getIsRemarqueRentePourEnfant());
            outputVB.setHasRemarqueRenteAvecDebutDroit5AnsAvantDateDepot(vb
                    .getIsRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande());
            outputVB.setIsRemSuppVeuf(vb.getIsRemSuppVeuf());
            outputVB.setHasRemarqueRenteAvecMontantMinimumMajoreInvalidite(vb
                    .getIsRemarqueRenteAvecMontantMinimumMajoreInvalidite());
            outputVB.setHasRemarqueRenteReduitePourSurassurance(vb.getIsRemarqueRenteReduitePourSurassurance());

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    public void actionAfficherDecision(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, final FWViewBeanInterface viewBean)
            throws Exception {
    }

    public void actionAfficherDossierGed(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, final FWViewBeanInterface viewBean)
            throws ServletException, IOException, JadeServiceLocatorException, JadeServiceActivatorException,
            NullPointerException, ClassCastException, JadeClassCastException {
        PRGedAffichageDossier.actionAfficherDossierGed(session, request, response, mainDispatcher, viewBean);
    }

    @Override
    protected void actionChercher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        if (("true").equals(request.getParameter("isDepuisDemande"))) {
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    new REDemandeParametresRCDTO());
        }

        String _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), null);
        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        REDecisionJointDemandeRenteViewBean vb = new REDecisionJointDemandeRenteViewBean();
        vb.setSession((BSession) mainDispatcher.getSession());

        this.saveViewBean(vb, session);

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionCustom(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher dispatcher) throws ServletException, IOException {

        FWAction action = FWAction.newInstance(request.getParameter(FWServlet.USER_ACTION));

        if ("afficherPreparation".equals(action.getActionPart())) {
            afficherPreparation(session, request, response, dispatcher);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    public void actionDecisionPrecedente(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, final FWViewBeanInterface viewBean)
            throws Exception {

        REPreValiderDecisionViewBean vb = (REPreValiderDecisionViewBean) viewBean;
        if (vb.getDecisionContainer().hasDecisionPrecedente()) {
            mainDispatcher.dispatch(vb, getAction());
            this.saveViewBean(vb, session);
        } else {
            this.saveViewBean(vb, session);
            vb.setMessage(((BSession) mainDispatcher.getSession()).getLabel("ERREUR_PAS_DE_DEC_PRECEDENTE"));
            vb.setMsgType(FWViewBeanInterface.ERROR);
        }

        vb.setCopiesList(vb.getDecisionContainer().getDecisionIC().getCopiesListDIC());
        vb.setAnnexesList(vb.getDecisionContainer().getDecisionIC().getAnnexesListDIC());

        String destination = "";

        if (vb.isDecisionSupprimable().booleanValue()) {
            destination = getRelativeURL(request, session) + "_de.jsp?_method=add";
        } else {
            destination = getRelativeURL(request, session) + "_de.jsp";
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    public void actionDecisionSuivante(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, final FWViewBeanInterface viewBean)
            throws Exception {

        REPreValiderDecisionViewBean vb = (REPreValiderDecisionViewBean) viewBean;
        String destination = null;

        if (vb.getDecisionContainer().hasDecisionSuivante()) {
            mainDispatcher.dispatch(vb, getAction());
            if (vb.isDecisionSupprimable().booleanValue()) {
                destination = getRelativeURL(request, session) + "_de.jsp?_method=add";
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } else {
            destination = getRelativeURLwithoutClassPart(request, session) + "validerDecisions" + "_de.jsp";

        }

        vb.setCopiesList(vb.getDecisionContainer().getDecisionIC().getCopiesListDIC());
        vb.setAnnexesList(vb.getDecisionContainer().getDecisionIC().getAnnexesListDIC());

        this.saveViewBean(vb, session);
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    @Override
    // Pour la validation des décisions (écran spécial)
    protected void actionExecuter(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            String action = "corvus.process.validerDecisions.executer";
            FWAction _action = FWAction.newInstance(action);

            // récupérer les valeurs souhaités
            // idDecision sélectionnées

            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            if (BProcess.class.isAssignableFrom(viewBean.getClass())) {
                BProcess process = (BProcess) viewBean;
                process.setControleTransaction(true);
                process.setSendCompletionMail(true);
            }
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            request.setAttribute("viewBean", viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                _destination = _getDestExecuterSucces(session, request, response, viewBean);
            } else {
                _destination = _getDestExecuterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);
    }

    @Override
    protected void actionLister(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "";

        try {
            /*
             * creation automatique du listviewBean
             */
            FWViewBeanInterface viewBean = new REDecisionJointDemandeRenteListViewBean();
            viewBean.setISession(mainDispatcher.getSession());

            /*
             * set automatique des properietes du listViewBean depuis la requete
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            /*
             * beforeLister() , puis appelle du dispatcher, puis le bean est mis en request
             */
            request.setAttribute("viewBean", viewBean);

            // pour bt [...] et pagination
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);

            /*
             * destination : remarque : si erreur, on va quand meme sur la liste avec le bean vide en erreur
             */
            destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    public void actionPrevalider(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, final FWViewBeanInterface viewBean)
            throws Exception {

        REPreValiderDecisionViewBean vb = (REPreValiderDecisionViewBean) viewBean;

        Map<String, KeyPeriodeInfo> map = new TreeMap<String, KeyPeriodeInfo>();
        Map<String, RECopieDecisionViewBean> mapCopies = new TreeMap<String, RECopieDecisionViewBean>();

        // Reprendre les valeurs pour les keys par la requete et setter une map
        // dans le viewBean pour utilisation dans le helper
        // Création de la clé
        String key = new String();
        String keyCopie = new String();

        // Récupérer tous les paramètres passé dans la requête (par post) et
        // l'envoyer vers l'helper
        for (Enumeration<?> names = request.getParameterNames(); names.hasMoreElements();) {
            String name = (String) names.nextElement();

            // Reprise uniquement des paramètres qui nous intéressent (pour 20
            // paramètres maximum)
            if (name.startsWith("dateDeb_") || name.startsWith("dateFin_") || name.startsWith("remarqu_")) {

                // Définition de la clé de tri
                key = name.substring(8);

                // Si key déjà existant, alors on complète
                if (map.containsKey(key)) {
                    KeyPeriodeInfo keyPeriodeInfo = map.get(key);

                    if (name.startsWith("dateDeb_")) {
                        keyPeriodeInfo.dateDebut = request.getParameter(name);
                    }
                    if (name.startsWith("dateFin_")) {
                        keyPeriodeInfo.dateFin = request.getParameter(name);
                    }
                    if (name.startsWith("remarqu_")) {
                        keyPeriodeInfo.remarque = request.getParameter(name);
                    }

                    map.put(key, keyPeriodeInfo);

                }
                // Sinon on insère simplement les valeurs
                else {

                    KeyPeriodeInfo keyPeriodeInfo = new KeyPeriodeInfo();

                    if (name.startsWith("dateDeb_")) {
                        keyPeriodeInfo.dateDebut = request.getParameter(name);
                    }
                    if (name.startsWith("dateFin_")) {
                        keyPeriodeInfo.dateFin = request.getParameter(name);
                    }
                    if (name.startsWith("remarqu_")) {
                        keyPeriodeInfo.remarque = request.getParameter(name);
                    }

                    map.put(key, keyPeriodeInfo);

                }

            } else if (name.startsWith("isVersem_") || name.startsWith("isBaseCa_") || name.startsWith("isDecomp_")
                    || name.startsWith("isRemarq_") || name.startsWith("isMoyens_") || name.startsWith("isSignat_")
                    || name.startsWith("isAnnexe_") || name.startsWith("isCopies_") || name.startsWith("idCopieD_")
                    || name.startsWith("isPageGa_")) {

                keyCopie = name.substring(9);

                RECopieDecisionViewBean copieDecision;

                // Si key déjà existant, alors on complète
                if (mapCopies.containsKey(keyCopie)) {
                    copieDecision = mapCopies.get(keyCopie);
                }
                // Sinon on insère simplement les valeurs
                else {

                    copieDecision = new RECopieDecisionViewBean();
                }

                if (name.startsWith("isVersem_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsVersementA(Boolean.TRUE);
                    } else {
                        copieDecision.setIsVersementA(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isBaseCa_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsBaseCalcul(Boolean.TRUE);
                    } else {
                        copieDecision.setIsBaseCalcul(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isDecomp_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsDecompte(Boolean.TRUE);
                    } else {
                        copieDecision.setIsDecompte(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isRemarq_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsRemarques(Boolean.TRUE);
                    } else {
                        copieDecision.setIsRemarques(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isMoyens_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsMoyensDroit(Boolean.TRUE);
                    } else {
                        copieDecision.setIsMoyensDroit(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isSignat_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsSignature(Boolean.TRUE);
                    } else {
                        copieDecision.setIsSignature(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isAnnexe_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsAnnexes(Boolean.TRUE);
                    } else {
                        copieDecision.setIsAnnexes(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isCopies_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsCopies(Boolean.TRUE);
                    } else {
                        copieDecision.setIsCopies(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isPageGa_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsPageGarde(Boolean.TRUE);
                    } else {
                        copieDecision.setIsPageGarde(Boolean.FALSE);
                    }
                }

                if (name.startsWith("idCopieD_")) {
                    copieDecision.setIdDecisionCopie(request.getParameter(name));
                }

                mapCopies.put(keyCopie, copieDecision);

            }
        }

        // Mise à jour des copies
        List<RECopieDecisionViewBean> copiesList = new ArrayList<RECopieDecisionViewBean>();
        for (String keyCop : mapCopies.keySet()) {

            RECopieDecisionViewBean copieDecisionMap = mapCopies.get(keyCop);

            RECopieDecisionViewBean copieDecisionBD = new RECopieDecisionViewBean();
            copieDecisionBD.setSession((BSession) mainDispatcher.getSession());
            copieDecisionBD.setIdDecisionCopie(copieDecisionMap.getIdDecisionCopie());
            copieDecisionBD.retrieve();

            if (!copieDecisionBD.isNew()) {
                copieDecisionBD.setIsVersementA(copieDecisionMap.getIsVersementA());
                copieDecisionBD.setIsBaseCalcul(copieDecisionMap.getIsBaseCalcul());
                copieDecisionBD.setIsDecompte(copieDecisionMap.getIsDecompte());
                copieDecisionBD.setIsRemarques(copieDecisionMap.getIsRemarques());
                copieDecisionBD.setIsMoyensDroit(copieDecisionMap.getIsMoyensDroit());
                copieDecisionBD.setIsSignature(copieDecisionMap.getIsSignature());
                copieDecisionBD.setIsAnnexes(copieDecisionMap.getIsAnnexes());
                copieDecisionBD.setIsCopies(copieDecisionMap.getIsCopies());
                copieDecisionBD.setIsPageGarde(copieDecisionMap.getIsPageGarde());
                copiesList.add(copieDecisionBD);
            }
        }

        ((REPreValiderDecisionViewBean) viewBean).setCopiesList(copiesList);
        String destination = "";

        ((REPreValiderDecisionViewBean) viewBean).setMapKey(map);
        ((REPreValiderDecisionViewBean) viewBean).setRemarqueDecision(request.getParameter("remarqueDecision"));
        ((REPreValiderDecisionViewBean) viewBean).setTraiterParDecision(request.getParameter("traiterParDecision"));

        mainDispatcher.dispatch(vb, getAction());

        destination = getRelativeURLwithoutClassPart(request, session) + "decisions" + "_rc.jsp?noDemandeRente="
                + vb.getIdDemandeRente();

        vb.setCopiesList(vb.getDecisionContainer().getDecisionIC().getCopiesListDIC());
        vb.setAnnexesList(vb.getDecisionContainer().getDecisionIC().getAnnexesListDIC());

        REDecisionJointDemandeRenteViewBean vbDec = new REDecisionJointDemandeRenteViewBean();
        vbDec.setSession((BSession) mainDispatcher.getSession());

        RENSSDTO dtoNss = new RENSSDTO();

        dtoNss.setNSS("");
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dtoNss);

        this.saveViewBean(vbDec, session);
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionReAfficher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";
        REPreValiderDecisionViewBean viewBean = (REPreValiderDecisionViewBean) session.getAttribute(FWServlet.VIEWBEAN);

        try {

            // LGA : 06.06.2013 garantie que l'on ne vas pas aller modifier une décision qui de doit pas l'être.
            BSession sess = (BSession) mainDispatcher.getSession();
            String idDecision = viewBean.getIdDecision();
            /* Avant toute chose, on vas s'assurer qu'il est possible de modifier la décision */
            if (!REDecisionsUtil.isDecisionModifiable(idDecision, sess)) {
                // On peut pas modifier la décision
                String message = sess.getLabel("ERREUR_IMP_MOD_DEC_PREST_VALIDE");
                message = message.replace("{0}", idDecision);
                throw new Exception(message);
            }

            boolean isDeleteCopie = false;
            String idDeletedCopie = "";

            // traitement des actions ajouter et supprimer sur les annexes et les copies
            String action = request.getParameter("action");

            // retour des tiers
            if (viewBean.isRetourPyxis().booleanValue()) {

                if (!JadeStringUtil.isEmpty(viewBean.getIdTiersDepuisPyxis())) {

                    RECopieDecisionViewBean copie = new RECopieDecisionViewBean();

                    copie.setIdDecision(viewBean.getIdDecision());
                    copie.setIdTiersCopie(viewBean.getIdTiersDepuisPyxis());

                    IPRAffilie aff = PRAffiliationHelper.getEmployeurParNumAffilie(mainDispatcher.getSession(),
                            viewBean.getNumAffilieDepuisPyxis());
                    // FIXME LGA : r?cup?rer la copie par l'id affili? ????? Avec un manager OK mais la...
                    if (aff != null) {
                        copie.setIdAffilie(aff.getIdAffilie());
                    }

                    copie.setSession((BSession) mainDispatcher.getSession());
                    copie.retrieve();

                    if (copie.isNew()) {
                        // setter idProv
                        copie.setIdProvisoire(String.valueOf(getNextIdProvCopie()));
                        copie.setIsVersementA(Boolean.TRUE);
                        copie.setIsBaseCalcul(Boolean.TRUE);
                        copie.setIsDecompte(Boolean.TRUE);
                        copie.setIsRemarques(Boolean.TRUE);
                        copie.setIsMoyensDroit(Boolean.TRUE);
                        copie.setIsSignature(Boolean.TRUE);
                        copie.setIsAnnexes(Boolean.TRUE);
                        copie.setIsCopies(Boolean.TRUE);
                        copie.setIsPageGarde(Boolean.TRUE);
                    } else {
                        copie.setIdProvisoire(copie.getIdDecisionCopie());
                    }

                    List<RECopieDecisionViewBean> lstCopie = viewBean.getCopiesList();
                    lstCopie.add(copie);

                    viewBean.getDecisionContainer().getDecisionIC().setCopiesListDIC(lstCopie);
                    viewBean.setCopiesList(lstCopie);

                }

            } else if (!JadeStringUtil.isEmpty(action)) {

                if (action.equals(REDecisionAction.ACTION_AJOUTER_ANNEXE)) {
                    String annexe = request.getParameter("nouvelAnnexe");

                    if (!JadeStringUtil.isEmpty(annexe)) {

                        REAnnexeDecisionViewBean ann = new REAnnexeDecisionViewBean();
                        ann.setIdDecision(viewBean.getIdDecision());
                        ann.setLibelle(annexe);
                        ann.setSession((BSession) mainDispatcher.getSession());
                        ann.retrieve();

                        if (ann.isNew()) {
                            // setter idProv
                            ann.setIdProvisoire(String.valueOf(getNextIdProvAnnexe()));
                        } else {
                            ann.setIdProvisoire(ann.getIdDecisionAnnexe());
                        }

                        List<REAnnexeDecisionViewBean> lst = viewBean.getAnnexesList();
                        lst.add(ann);
                        viewBean.getDecisionContainer().getDecisionIC().setAnnexesListDIC(lst);
                        viewBean.setAnnexesList(lst);

                    }

                } else if (action.equals(REDecisionAction.ACTION_SUPPRIMER_ANNEXE)) {
                    String idProvisoire = request.getParameter("selectedIndex");

                    List<REAnnexeDecisionViewBean> lst = viewBean.getAnnexesList();

                    if (!JadeStringUtil.isEmpty(idProvisoire)) {

                        Iterator<REAnnexeDecisionViewBean> iter = viewBean.getAnnexesIterator();

                        while (iter.hasNext()) {
                            REAnnexeDecisionViewBean currentAnnexe = iter.next();

                            if (currentAnnexe.getIdProvisoire().equals(idProvisoire)) {
                                lst.remove(currentAnnexe);
                                break;
                            }
                        }
                    }

                    viewBean.getDecisionContainer().getDecisionIC().setAnnexesListDIC(lst);
                    viewBean.setAnnexesList(lst);

                } else if (action.equals(REDecisionAction.ACTION_SUPPRIMER_COPIE)) {
                    String idProvisoire = request.getParameter("selectedIndex");

                    List<RECopieDecisionViewBean> lst = viewBean.getCopiesList();

                    if (!JadeStringUtil.isEmpty(idProvisoire)) {

                        Iterator<RECopieDecisionViewBean> iter = viewBean.getCopiesIterator();

                        while (iter.hasNext()) {
                            RECopieDecisionViewBean currentCopie = iter.next();

                            if (currentCopie.getIdProvisoire().equals(idProvisoire)) {
                                lst.remove(currentCopie);
                                isDeleteCopie = true;
                                idDeletedCopie = idProvisoire;
                                break;
                            }
                        }
                    }

                    viewBean.getDecisionContainer().getDecisionIC().setCopiesListDIC(lst);
                    viewBean.setCopiesList(lst);

                }
            }

            if (!viewBean.isRetourPyxis().booleanValue()) {
                Map<String, KeyPeriodeInfo> map = new TreeMap<String, KeyPeriodeInfo>();
                Map<String, RECopieDecisionViewBean> mapCopies = new TreeMap<String, RECopieDecisionViewBean>();

                // Reprendre les valeurs pour les keys par la requete et setter
                // une map dans le viewBean pour utilisation dans le helper
                // Création de la clé
                String key = new String();
                String keyCopie = new String();

                // Récupérer tous les paramètres passé dans la requête (par
                // post) et l'envoyer vers l'helper
                for (Enumeration<?> names = request.getParameterNames(); names.hasMoreElements();) {
                    String name = (String) names.nextElement();

                    // Reprise uniquement des paramètres qui nous intéressent
                    // (pour 20 paramètres maximum)
                    if (name.startsWith("dateDeb_") || name.startsWith("dateFin_") || name.startsWith("remarqu_")) {

                        // Définition de la clé de tri
                        key = name.substring(8);

                        // Si key déjà existant, alors on complète
                        if (map.containsKey(key)) {
                            KeyPeriodeInfo keyPeriodeInfo = map.get(key);

                            if (name.startsWith("dateDeb_")) {
                                keyPeriodeInfo.dateDebut = request.getParameter(name);
                            }
                            if (name.startsWith("dateFin_")) {
                                keyPeriodeInfo.dateFin = request.getParameter(name);
                            }
                            if (name.startsWith("remarqu_")) {
                                keyPeriodeInfo.remarque = request.getParameter(name);
                            }

                            map.put(key, keyPeriodeInfo);

                        }
                        // Sinon on insère simplement les valeurs
                        else {

                            KeyPeriodeInfo keyPeriodeInfo = new KeyPeriodeInfo();

                            if (name.startsWith("dateDeb_")) {
                                keyPeriodeInfo.dateDebut = request.getParameter(name);
                            }
                            if (name.startsWith("dateFin_")) {
                                keyPeriodeInfo.dateFin = request.getParameter(name);
                            }
                            if (name.startsWith("remarqu_")) {
                                keyPeriodeInfo.remarque = request.getParameter(name);
                            }

                            map.put(key, keyPeriodeInfo);

                        }
                    } else if (name.startsWith("isVersem_") || name.startsWith("isBaseCa_")
                            || name.startsWith("isDecomp_") || name.startsWith("isRemarq_")
                            || name.startsWith("isMoyens_") || name.startsWith("isSignat_")
                            || name.startsWith("isAnnexe_") || name.startsWith("isCopies_")
                            || name.startsWith("idCopieD_") || name.startsWith("isPageGa_")) {

                        keyCopie = name.substring(9);

                        RECopieDecisionViewBean copieDecision;

                        // Si key déjà existant, alors on complète
                        if (mapCopies.containsKey(keyCopie)) {
                            copieDecision = mapCopies.get(keyCopie);
                        }
                        // Sinon on insère simplement les valeurs
                        else {

                            copieDecision = new RECopieDecisionViewBean();
                        }

                        if (name.startsWith("isVersem_")) {
                            if (request.getParameter(name).equals("on")) {
                                copieDecision.setIsVersementA(Boolean.TRUE);
                            } else {
                                copieDecision.setIsVersementA(Boolean.FALSE);
                            }
                        }

                        if (name.startsWith("isBaseCa_")) {
                            if (request.getParameter(name).equals("on")) {
                                copieDecision.setIsBaseCalcul(Boolean.TRUE);
                            } else {
                                copieDecision.setIsBaseCalcul(Boolean.FALSE);
                            }
                        }

                        if (name.startsWith("isDecomp_")) {
                            if (request.getParameter(name).equals("on")) {
                                copieDecision.setIsDecompte(Boolean.TRUE);
                            } else {
                                copieDecision.setIsDecompte(Boolean.FALSE);
                            }
                        }

                        if (name.startsWith("isRemarq_")) {
                            if (request.getParameter(name).equals("on")) {
                                copieDecision.setIsRemarques(Boolean.TRUE);
                            } else {
                                copieDecision.setIsRemarques(Boolean.FALSE);
                            }
                        }

                        if (name.startsWith("isMoyens_")) {
                            if (request.getParameter(name).equals("on")) {
                                copieDecision.setIsMoyensDroit(Boolean.TRUE);
                            } else {
                                copieDecision.setIsMoyensDroit(Boolean.FALSE);
                            }
                        }

                        if (name.startsWith("isSignat_")) {
                            if (request.getParameter(name).equals("on")) {
                                copieDecision.setIsSignature(Boolean.TRUE);
                            } else {
                                copieDecision.setIsSignature(Boolean.FALSE);
                            }
                        }

                        if (name.startsWith("isAnnexe_")) {
                            if (request.getParameter(name).equals("on")) {
                                copieDecision.setIsAnnexes(Boolean.TRUE);
                            } else {
                                copieDecision.setIsAnnexes(Boolean.FALSE);
                            }
                        }

                        if (name.startsWith("isCopies_")) {
                            if (request.getParameter(name).equals("on")) {
                                copieDecision.setIsCopies(Boolean.TRUE);
                            } else {
                                copieDecision.setIsCopies(Boolean.FALSE);
                            }
                        }

                        if (name.startsWith("isPageGa_")) {
                            if (request.getParameter(name).equals("on")) {
                                copieDecision.setIsPageGarde(Boolean.TRUE);
                            } else {
                                copieDecision.setIsPageGarde(Boolean.FALSE);
                            }
                        }

                        if (name.startsWith("idCopieD_")) {
                            copieDecision.setIdDecisionCopie(request.getParameter(name));
                        }

                        mapCopies.put(keyCopie, copieDecision);

                    }
                }

                // Mise à jour des copies
                List<RECopieDecisionViewBean> copiesList = new ArrayList<RECopieDecisionViewBean>();
                for (String keyCop : mapCopies.keySet()) {

                    RECopieDecisionViewBean copieDecisionMap = mapCopies.get(keyCop);

                    RECopieDecisionViewBean copieDecisionBD = new RECopieDecisionViewBean();
                    copieDecisionBD.setSession((BSession) mainDispatcher.getSession());
                    copieDecisionBD.setIdDecisionCopie(copieDecisionMap.getIdDecisionCopie());
                    copieDecisionBD.retrieve();

                    if (!copieDecisionBD.isNew()) {

                        if (isDeleteCopie && copieDecisionBD.getIdDecisionCopie().equals(idDeletedCopie)) {
                            copieDecisionBD.delete();
                        } else {
                            copieDecisionBD.setIsVersementA(copieDecisionMap.getIsVersementA());
                            copieDecisionBD.setIsBaseCalcul(copieDecisionMap.getIsBaseCalcul());
                            copieDecisionBD.setIsDecompte(copieDecisionMap.getIsDecompte());
                            copieDecisionBD.setIsRemarques(copieDecisionMap.getIsRemarques());
                            copieDecisionBD.setIsMoyensDroit(copieDecisionMap.getIsMoyensDroit());
                            copieDecisionBD.setIsSignature(copieDecisionMap.getIsSignature());
                            copieDecisionBD.setIsAnnexes(copieDecisionMap.getIsAnnexes());
                            copieDecisionBD.setIsCopies(copieDecisionMap.getIsCopies());
                            copieDecisionBD.setIsPageGarde(copieDecisionMap.getIsPageGarde());
                            copiesList.add(copieDecisionBD);
                        }

                    }
                }

                viewBean.setCopiesList(copiesList);

                viewBean.setMapKey(map);
                viewBean.setRemarqueDecision(request.getParameter("remarqueDecision"));
                viewBean.setTraiterParDecision(request.getParameter("traiterParDecision"));

                // on va dans le helper pour sauvegarder les éventuelles
                // remarques saisies
                // ou modifiées avant une action sur les annexes et copies.

                String isAvecBonneFoi = request.getParameter("isAvecBonneFoi");
                String isSansBonneFoi = request.getParameter("isSansBonneFoi");
                String csGenreDecision = request.getParameter("csGenreDecision");
                String isRemAnnDeci = request.getParameter("isRemAnnDeci");
                String isObligPayerCoti = request.getParameter("isObligPayerCoti");
                String isRemSuppVeuf = request.getParameter("isRemSuppVeuf");
                String isRemRedPlaf = request.getParameter("isRemRedPlaf");

                if ("on".equals(isAvecBonneFoi)) {
                    viewBean.setIsAvecBonneFoi(Boolean.TRUE);
                } else {
                    viewBean.setIsAvecBonneFoi(Boolean.FALSE);
                }

                if ("on".equals(isSansBonneFoi)) {
                    viewBean.setIsSansBonneFoi(Boolean.TRUE);
                } else {
                    viewBean.setIsSansBonneFoi(Boolean.FALSE);
                }

                viewBean.setCsGenreDecision(csGenreDecision);

                if ("on".equals(isRemAnnDeci)) {
                    viewBean.setIsRemAnnDeci(Boolean.TRUE);
                } else {
                    viewBean.setIsRemAnnDeci(Boolean.FALSE);
                }

                if ("on".equals(isObligPayerCoti)) {
                    viewBean.setIsObligPayerCoti(Boolean.TRUE);
                } else {
                    viewBean.setIsObligPayerCoti(Boolean.FALSE);
                }

                if ("on".equals(isRemSuppVeuf)) {
                    viewBean.setIsRemSuppVeuf(Boolean.TRUE);
                } else {
                    viewBean.setIsRemSuppVeuf(Boolean.FALSE);
                }

                if ("on".equals(isRemRedPlaf)) {
                    viewBean.setIsRemRedPlaf(Boolean.TRUE);
                } else {
                    viewBean.setIsRemRedPlaf(Boolean.FALSE);
                }

            }

            viewBean.setIsRetourPyxis(Boolean.FALSE);

            mainDispatcher.dispatch(viewBean, getAction());
            this.saveViewBean(viewBean, session);

            PRTiersWrapper tw = PRTiersHelper.getTiersParId(mainDispatcher.getSession(),
                    (viewBean).getIdTiersBeneficiairePrincipal());
            viewBean.setTiersBeneficiairePrincipalInfo(tw.getDescription((BSession) mainDispatcher.getSession()));

            viewBean.setISession(mainDispatcher.getSession());

            this.saveViewBean((viewBean), session);
            request.setAttribute(FWServlet.VIEWBEAN, (viewBean));

            if ((viewBean).getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if ((viewBean).isDecisionSupprimable().booleanValue()) {
                    _destination = getRelativeURLwithoutClassPart(request, session)
                            + "preValiderDecision_de.jsp?_method=add&_valid=true";
                } else {
                    _destination = getRelativeURLwithoutClassPart(request, session)
                            + "preValiderDecision_de.jsp?_valid=true";
                }

            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * ecrase une des valeurs sauvee dans la session par FWSelectorTag de telle sorte que l'on sache exactement quelle
     * action sera executee lorsque l'on revient de pyxis et avec quels parametres.
     * 
     * @see FWSelectorTag
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionSelectionner(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        StringBuffer queryString = new StringBuffer();

        queryString.append("userAction");
        queryString.append("=");
        queryString.append("corvus.decisions.preValiderDecision");
        queryString.append(".");
        queryString.append(FWAction.ACTION_REAFFICHER);

        // HACK: on remplace une des valeurs sauvee en session par FWSelectorTag
        session.setAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL, queryString.toString());

        REPreValiderDecisionViewBean viewBean = (REPreValiderDecisionViewBean) session.getAttribute(FWServlet.VIEWBEAN);

        Map<String, KeyPeriodeInfo> map = new TreeMap<String, KeyPeriodeInfo>();
        Map<String, RECopieDecisionViewBean> mapCopies = new TreeMap<String, RECopieDecisionViewBean>();

        // Reprendre les valeurs pour les keys par la requete et setter une map
        // dans le viewBean pour utilisation dans le helper
        // Création de la clé
        String key = new String();
        String keyCopie = new String();

        // Récupérer tous les paramètres passé dans la requête (par post) et
        // l'envoyer vers l'helper
        for (Enumeration<?> names = request.getParameterNames(); names.hasMoreElements();) {
            String name = (String) names.nextElement();

            // Reprise uniquement des paramètres qui nous intéressent (pour 20
            // paramètres maximum)
            if (name.startsWith("dateDeb_") || name.startsWith("dateFin_") || name.startsWith("remarqu_")) {

                // Définition de la clé de tri
                key = name.substring(8);

                // Si key déjà existant, alors on complète
                if (map.containsKey(key)) {
                    KeyPeriodeInfo keyPeriodeInfo = map.get(key);

                    if (name.startsWith("dateDeb_")) {
                        keyPeriodeInfo.dateDebut = request.getParameter(name);
                    }
                    if (name.startsWith("dateFin_")) {
                        keyPeriodeInfo.dateFin = request.getParameter(name);
                    }
                    if (name.startsWith("remarqu_")) {
                        keyPeriodeInfo.remarque = request.getParameter(name);
                    }

                    map.put(key, keyPeriodeInfo);

                }
                // Sinon on insère simplement les valeurs
                else {

                    KeyPeriodeInfo keyPeriodeInfo;
                    try {
                        keyPeriodeInfo = new KeyPeriodeInfo();

                        if (name.startsWith("dateDeb_")) {
                            keyPeriodeInfo.dateDebut = request.getParameter(name);
                        }
                        if (name.startsWith("dateFin_")) {
                            keyPeriodeInfo.dateFin = request.getParameter(name);
                        }
                        if (name.startsWith("remarqu_")) {
                            keyPeriodeInfo.remarque = request.getParameter(name);
                        }

                        map.put(key, keyPeriodeInfo);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (name.startsWith("isVersem_") || name.startsWith("isBaseCa_") || name.startsWith("isDecomp_")
                    || name.startsWith("isRemarq_") || name.startsWith("isMoyens_") || name.startsWith("isSignat_")
                    || name.startsWith("isAnnexe_") || name.startsWith("isCopies_") || name.startsWith("idCopieD_")
                    || name.startsWith("isPageGa_")) {

                keyCopie = name.substring(9);

                RECopieDecisionViewBean copieDecision;

                // Si key déjà existant, alors on complète
                if (mapCopies.containsKey(keyCopie)) {
                    copieDecision = mapCopies.get(keyCopie);
                }
                // Sinon on insère simplement les valeurs
                else {

                    copieDecision = new RECopieDecisionViewBean();
                }

                if (name.startsWith("isVersem_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsVersementA(Boolean.TRUE);
                    } else {
                        copieDecision.setIsVersementA(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isBaseCa_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsBaseCalcul(Boolean.TRUE);
                    } else {
                        copieDecision.setIsBaseCalcul(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isDecomp_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsDecompte(Boolean.TRUE);
                    } else {
                        copieDecision.setIsDecompte(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isRemarq_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsRemarques(Boolean.TRUE);
                    } else {
                        copieDecision.setIsRemarques(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isMoyens_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsMoyensDroit(Boolean.TRUE);
                    } else {
                        copieDecision.setIsMoyensDroit(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isSignat_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsSignature(Boolean.TRUE);
                    } else {
                        copieDecision.setIsSignature(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isAnnexe_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsAnnexes(Boolean.TRUE);
                    } else {
                        copieDecision.setIsAnnexes(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isCopies_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsCopies(Boolean.TRUE);
                    } else {
                        copieDecision.setIsCopies(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isPageGa_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsPageGarde(Boolean.TRUE);
                    } else {
                        copieDecision.setIsPageGarde(Boolean.FALSE);
                    }
                }

                if (name.startsWith("idCopieD_")) {
                    copieDecision.setIdDecisionCopie(request.getParameter(name));
                }

                mapCopies.put(keyCopie, copieDecision);

            }
        }
        try {

            // Mise à jour des copies
            List<RECopieDecisionViewBean> copiesList = new ArrayList<RECopieDecisionViewBean>();
            for (String keyCop : mapCopies.keySet()) {

                RECopieDecisionViewBean copieDecisionMap = mapCopies.get(keyCop);

                RECopieDecisionViewBean copieDecisionBD = new RECopieDecisionViewBean();
                copieDecisionBD.setSession((BSession) mainDispatcher.getSession());
                copieDecisionBD.setIdDecisionCopie(copieDecisionMap.getIdDecisionCopie());
                copieDecisionBD.retrieve();

                if (!copieDecisionBD.isNew()) {
                    copieDecisionBD.setIsVersementA(copieDecisionMap.getIsVersementA());
                    copieDecisionBD.setIsBaseCalcul(copieDecisionMap.getIsBaseCalcul());
                    copieDecisionBD.setIsDecompte(copieDecisionMap.getIsDecompte());
                    copieDecisionBD.setIsRemarques(copieDecisionMap.getIsRemarques());
                    copieDecisionBD.setIsMoyensDroit(copieDecisionMap.getIsMoyensDroit());
                    copieDecisionBD.setIsSignature(copieDecisionMap.getIsSignature());
                    copieDecisionBD.setIsAnnexes(copieDecisionMap.getIsAnnexes());
                    copieDecisionBD.setIsCopies(copieDecisionMap.getIsCopies());
                    copieDecisionBD.setIsPageGarde(copieDecisionMap.getIsPageGarde());
                    copiesList.add(copieDecisionBD);
                }
            }

            viewBean.setCopiesList(copiesList);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }

        viewBean.setMapKey(map);
        viewBean.setRemarqueDecision(request.getParameter("remarqueDecision"));
        viewBean.setTraiterParDecision(request.getParameter("traiterParDecision"));

        // on va dans le helper pour sauvegarder les éventuelles remarques
        // saisies
        // ou modifiées avant une action sur les annexes et copies.
        session.setAttribute(FWServlet.VIEWBEAN, viewBean);

        // Reprise de l'action par défaut

        String _destination = "";

        try {
            // set les properties
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIsRetourPyxis(Boolean.TRUE);
            session.setAttribute("viewBean", viewBean);
            String selectorName = request.getParameter("selectorName");

            Object[] ProviderActionParams = (Object[]) session.getAttribute(selectorName + "ProviderActionParams");
            String providerApplication = (String) session.getAttribute(selectorName + "ProviderApplication");
            String providerPrefix = (String) session.getAttribute(selectorName + "ProviderPrefix");
            String redirectUrl = (String) session.getAttribute(selectorName + "RedirectUrl");

            session.setAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_METHODS,
                    session.getAttribute(selectorName + "Methods"));
            session.setAttribute("redirectUrl", redirectUrl);

            // destination
            _destination = "/" + session.getAttribute(selectorName + "ProviderApplication") + "?userAction="
                    + session.getAttribute(selectorName + "ProviderAction") + "&colonneSelection=yes";
            // eventuel parametres a transamettre pour l'url du provider
            if (ProviderActionParams != null) {
                for (int nbParams = 0; nbParams < ProviderActionParams.length; nbParams++) {
                    String fieldName = ((String[]) ProviderActionParams[nbParams])[0];
                    String paramName = ((String[]) ProviderActionParams[nbParams])[1];
                    String paramValue = request.getParameter(fieldName);
                    _destination += "&" + paramName + "=" + paramValue;
                }
            }

            FWController controller = (FWController) session.getAttribute("objController");

            Hashtable<String, Object> table = new Hashtable<String, Object>();
            HttpSession httpSession = request.getSession();
            for (Enumeration<?> enumeration = httpSession.getAttributeNames(); enumeration.hasMoreElements();) {
                String name = (String) enumeration.nextElement();
                table.put(name, httpSession.getAttribute(name));
            }

            Hashtable<String, Object> context = table;

            request.getSession().setAttribute("globazContext", context);

            controller = new FWDispatcher(controller.getSession(), providerApplication, providerPrefix);

            session.setAttribute("objController", controller);

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    public void activerValidationDecision(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, final FWViewBeanInterface viewBean)
            throws Exception {

        FWMenuBlackBox menuBB = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        menuBB.setCurrentMenu("corvus-menuprincipal", "menu");
        menuBB.setCurrentMenu("corvus-optionsempty", "options");
        menuBB.setNodeActive(false, "RE-activerValidationDecision", "corvus-menuprincipal");
        menuBB.setNodeActive(true, "RE-desactiverValidationDecision", "corvus-menuprincipal");

        REDemandeRenteJointDemandeViewBean vb = new REDemandeRenteJointDemandeViewBean();
        mainDispatcher.dispatch(vb, getAction());

        FWAction action = FWAction
                .newInstance(IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE + ".chercher");
        String destination = this.getUserActionURL(request, action.toString());
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    public void afficherModifier(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, final FWViewBeanInterface viewBean)
            throws Exception {

        servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp?_method=add")
                .forward(request, response);

    }

    public void afficherPreparation(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        String idDemandeRente = request.getParameter("noDemandeRente");

        // viewBean par défaut
        REPreparerDecisionViewBean preparerDecisionViewBean = new REPreparerDecisionStandardViewBean();
        preparerDecisionViewBean.setIdDemandeRente(idDemandeRente);

        preparerDecisionViewBean = (REPreparerDecisionViewBean) beforeAfficher(session, request, response,
                preparerDecisionViewBean);
        preparerDecisionViewBean = (REPreparerDecisionViewBean) mainDispatcher.dispatch(preparerDecisionViewBean,
                getAction());

        String destination;

        if (FWViewBeanInterface.ERROR.equals(preparerDecisionViewBean.getMsgType())) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        } else {
            // destination en fonction du viewBean qui a été retourné par le helper
            // le helper peut retourner un type de viewBean différent selon le type de préparation de décision
            destination = preparerDecisionViewBean.getDestination();
        }
        this.saveViewBean(preparerDecisionViewBean, request);
        this.saveViewBean(preparerDecisionViewBean, session);

        forward(destination, request, response);
    }

    public void annuler(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, final FWViewBeanInterface viewBean)
            throws Exception {

        servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp?_method=")
                .forward(request, response);

    }

    @Override
    protected FWViewBeanInterface beforeAfficher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {

        String idTierRequerant = request.getParameter("idTierRequerant");

        if (!JadeStringUtil.isIntegerEmpty(idTierRequerant)) {
            try {
                PRTiersWrapper benefPrincipal = PRTiersHelper.getTiersParId(new BSession(
                        REApplication.DEFAULT_APPLICATION_CORVUS), idTierRequerant);

                // On mémorise ls NSS dans la session
                RENSSDTO dto = new RENSSDTO();
                dto.setNSS(benefPrincipal.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

                PRSessionDataContainerHelper.setData(session,
                        PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return viewBean;
    }

    public void desactiverValidationDecision(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, final FWViewBeanInterface viewBean)
            throws Exception {

        FWMenuBlackBox menuBB = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
        menuBB.setCurrentMenu("corvus-menuprincipal", "menu");
        menuBB.setCurrentMenu("corvus-optionsempty", "options");
        menuBB.setNodeActive(true, "RE-activerValidationDecision", "corvus-menuprincipal");
        menuBB.setNodeActive(false, "RE-desactiverValidationDecision", "corvus-menuprincipal");

        REDemandeRenteJointDemandeViewBean vb = new REDemandeRenteJointDemandeViewBean();
        mainDispatcher.dispatch(vb, getAction());

        FWAction action = FWAction
                .newInstance(IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE + ".chercher");
        String destination = this.getUserActionURL(request, action.toString());
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    public void enregistrerModifications(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, final FWViewBeanInterface viewBean)
            throws Exception {

        String destination = "";
        try {
            /* Avant toute chose, on vas s'assurer qu'il est possible de modifier la décision */
            BSession sess = (BSession) mainDispatcher.getSession();
            String idDecision = ((REPreValiderDecisionViewBean) viewBean).getIdDecision();
            if (!REDecisionsUtil.isDecisionModifiable(idDecision, sess)) {
                String message = sess.getLabel("ERREUR_IMP_MOD_DEC_PREST_VALIDE");
                message = message.replace("{0}", idDecision);
                throw new Exception(message);
            }

            Map<String, KeyPeriodeInfo> map = new TreeMap<String, KeyPeriodeInfo>();
            Map<String, RECopieDecisionViewBean> mapCopies = new TreeMap<String, RECopieDecisionViewBean>();

            // Reprendre les valeurs pour les keys par la requete et setter une map
            // dans le viewBean pour utilisation dans le helper
            // Création de la clé
            String key = new String();
            String keyCopie = new String();

            // Récupérer tous les paramètres passé dans la requête (par post) et
            // l'envoyer vers l'helper
            for (Enumeration<?> names = request.getParameterNames(); names.hasMoreElements();) {
                String name = (String) names.nextElement();

                // Reprise uniquement des paramètres qui nous intéressent (pour 20
                // paramètres maximum)
                if (name.startsWith("dateDeb_") || name.startsWith("dateFin_") || name.startsWith("remarqu_")) {

                    // Définition de la clé de tri
                    key = name.substring(8);

                    // Si key déjà existant, alors on complète
                    if (map.containsKey(key)) {
                        KeyPeriodeInfo keyPeriodeInfo = map.get(key);

                        if (name.startsWith("dateDeb_")) {
                            keyPeriodeInfo.dateDebut = request.getParameter(name);
                        }
                        if (name.startsWith("dateFin_")) {
                            keyPeriodeInfo.dateFin = request.getParameter(name);
                        }
                        if (name.startsWith("remarqu_")) {
                            keyPeriodeInfo.remarque = request.getParameter(name);
                        }

                        map.put(key, keyPeriodeInfo);

                    }
                    // Sinon on insère simplement les valeurs
                    else {

                        KeyPeriodeInfo keyPeriodeInfo = new KeyPeriodeInfo();

                        if (name.startsWith("dateDeb_")) {
                            keyPeriodeInfo.dateDebut = request.getParameter(name);
                        }
                        if (name.startsWith("dateFin_")) {
                            keyPeriodeInfo.dateFin = request.getParameter(name);
                        }
                        if (name.startsWith("remarqu_")) {
                            keyPeriodeInfo.remarque = request.getParameter(name);
                        }

                        map.put(key, keyPeriodeInfo);

                    }

                } else if (name.startsWith("isVersem_") || name.startsWith("isBaseCa_") || name.startsWith("isDecomp_")
                        || name.startsWith("isRemarq_") || name.startsWith("isMoyens_") || name.startsWith("isSignat_")
                        || name.startsWith("isAnnexe_") || name.startsWith("isCopies_") || name.startsWith("idCopieD_")
                        || name.startsWith("isPageGa_")) {

                    keyCopie = name.substring(9);

                    RECopieDecisionViewBean copieDecision;

                    // Si key déjà existant, alors on complète
                    if (mapCopies.containsKey(keyCopie)) {
                        copieDecision = mapCopies.get(keyCopie);
                    }
                    // Sinon on insère simplement les valeurs
                    else {

                        copieDecision = new RECopieDecisionViewBean();
                    }

                    if (name.startsWith("isVersem_")) {
                        if (request.getParameter(name).equals("on")) {
                            copieDecision.setIsVersementA(Boolean.TRUE);
                        } else {
                            copieDecision.setIsVersementA(Boolean.FALSE);
                        }
                    }

                    if (name.startsWith("isBaseCa_")) {
                        if (request.getParameter(name).equals("on")) {
                            copieDecision.setIsBaseCalcul(Boolean.TRUE);
                        } else {
                            copieDecision.setIsBaseCalcul(Boolean.FALSE);
                        }
                    }

                    if (name.startsWith("isDecomp_")) {
                        if (request.getParameter(name).equals("on")) {
                            copieDecision.setIsDecompte(Boolean.TRUE);
                        } else {
                            copieDecision.setIsDecompte(Boolean.FALSE);
                        }
                    }

                    if (name.startsWith("isRemarq_")) {
                        if (request.getParameter(name).equals("on")) {
                            copieDecision.setIsRemarques(Boolean.TRUE);
                        } else {
                            copieDecision.setIsRemarques(Boolean.FALSE);
                        }
                    }

                    if (name.startsWith("isMoyens_")) {
                        if (request.getParameter(name).equals("on")) {
                            copieDecision.setIsMoyensDroit(Boolean.TRUE);
                        } else {
                            copieDecision.setIsMoyensDroit(Boolean.FALSE);
                        }
                    }

                    if (name.startsWith("isSignat_")) {
                        if (request.getParameter(name).equals("on")) {
                            copieDecision.setIsSignature(Boolean.TRUE);
                        } else {
                            copieDecision.setIsSignature(Boolean.FALSE);
                        }
                    }

                    if (name.startsWith("isAnnexe_")) {
                        if (request.getParameter(name).equals("on")) {
                            copieDecision.setIsAnnexes(Boolean.TRUE);
                        } else {
                            copieDecision.setIsAnnexes(Boolean.FALSE);
                        }
                    }

                    if (name.startsWith("isCopies_")) {
                        if (request.getParameter(name).equals("on")) {
                            copieDecision.setIsCopies(Boolean.TRUE);
                        } else {
                            copieDecision.setIsCopies(Boolean.FALSE);
                        }
                    }

                    if (name.startsWith("isPageGa_")) {
                        if (request.getParameter(name).equals("on")) {
                            copieDecision.setIsPageGarde(Boolean.TRUE);
                        } else {
                            copieDecision.setIsPageGarde(Boolean.FALSE);
                        }
                    }

                    if (name.startsWith("idCopieD_")) {
                        copieDecision.setIdDecisionCopie(request.getParameter(name));
                    }

                    mapCopies.put(keyCopie, copieDecision);

                }
            }

            // Mise à jour des copies
            List<RECopieDecisionViewBean> copiesList = new ArrayList<RECopieDecisionViewBean>();
            for (String keyCop : mapCopies.keySet()) {

                RECopieDecisionViewBean copieDecisionMap = mapCopies.get(keyCop);

                RECopieDecisionViewBean copieDecisionBD = new RECopieDecisionViewBean();
                copieDecisionBD.setSession((BSession) mainDispatcher.getSession());
                copieDecisionBD.setIdDecisionCopie(copieDecisionMap.getIdDecisionCopie());
                copieDecisionBD.retrieve();

                if (!copieDecisionBD.isNew()) {
                    copieDecisionBD.setIsVersementA(copieDecisionMap.getIsVersementA());
                    copieDecisionBD.setIsBaseCalcul(copieDecisionMap.getIsBaseCalcul());
                    copieDecisionBD.setIsDecompte(copieDecisionMap.getIsDecompte());
                    copieDecisionBD.setIsRemarques(copieDecisionMap.getIsRemarques());
                    copieDecisionBD.setIsMoyensDroit(copieDecisionMap.getIsMoyensDroit());
                    copieDecisionBD.setIsSignature(copieDecisionMap.getIsSignature());
                    copieDecisionBD.setIsAnnexes(copieDecisionMap.getIsAnnexes());
                    copieDecisionBD.setIsCopies(copieDecisionMap.getIsCopies());
                    copieDecisionBD.setIsPageGarde(copieDecisionMap.getIsPageGarde());
                    copieDecisionBD.setIsPageGarde(copieDecisionMap.getIsPageGarde());
                    copiesList.add(copieDecisionBD);
                }
            }

            ((REPreValiderDecisionViewBean) viewBean).setCopiesList(copiesList);

            ((REPreValiderDecisionViewBean) viewBean).setMapKey(map);
            mainDispatcher.dispatch(viewBean, getAction());
            this.saveViewBean(viewBean, session);

            PRTiersWrapper tw = PRTiersHelper.getTiersParId(mainDispatcher.getSession(),
                    ((REPreValiderDecisionViewBean) viewBean).getIdTiersBeneficiairePrincipal());
            ((REPreValiderDecisionViewBean) viewBean).setTiersBeneficiairePrincipalInfo(tw
                    .getDescription((BSession) mainDispatcher.getSession()));

            ((REPreValiderDecisionViewBean) viewBean).setISession(mainDispatcher.getSession());

            this.saveViewBean((viewBean), session);
            request.setAttribute(FWServlet.VIEWBEAN, (viewBean));

            if (((REPreValiderDecisionViewBean) viewBean).getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (((REPreValiderDecisionViewBean) viewBean).isDecisionSupprimable().booleanValue()) {
                    destination = getRelativeURLwithoutClassPart(request, session)
                            + "preValiderDecision_de.jsp?_method=add&_valid=true";
                } else {
                    destination = getRelativeURLwithoutClassPart(request, session)
                            + "preValiderDecision_de.jsp?_valid=true";
                }

            }
        } catch (Exception e) {
            viewBean.setMsgType(FWMessage.ERREUR);
            viewBean.setMessage(e.getMessage());
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    public void genererDecision(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, final FWViewBeanInterface viewBean)
            throws Exception {

        REPreValiderDecisionViewBean vb = new REPreValiderDecisionViewBean();

        /*
         * set automatique des properietes du listViewBean depuis la requete
         */
        globaz.globall.http.JSPUtils.setBeanProperties(request, vb);
        String destination = "";

        FWAction action = FWAction.newInstance(IREActions.ACTION_PREVALIDER_DECISION + ".genererDecision");
        destination = this.getUserActionURL(request, action.toString());

        mainDispatcher.dispatch(vb, action);

        /*
         * choix de la destination
         */

        try {
            boolean goesToSuccessDest = true;

            if (vb.getMsgType().equals(FWViewBeanInterface.ERROR)
                    || vb.getMsgType().equals(FWViewBeanInterface.WARNING)) {
                goesToSuccessDest = false;
            }

            if (goesToSuccessDest) {
                this.saveViewBean(vb, session);
                if (vb.isDecisionSupprimable().booleanValue()) {
                    destination = getRelativeURLwithoutClassPart(request, session)
                            + "preValiderDecision_de.jsp?_method=add";

                } else {
                    destination = getRelativeURLwithoutClassPart(request, session) + "preValiderDecision_de.jsp";
                }

                vb.setIsDepuisRcListDecision(Boolean.FALSE);
                vb.setCopiesList(vb.getDecisionContainer().getDecisionIC().getCopiesListDIC());
                vb.setAnnexesList(vb.getDecisionContainer().getDecisionIC().getAnnexesListDIC());

                // mise a jour du beneficiaire principal
                String idDecision = vb.getDecisionContainer().getDecisionIC().getIdDecision();
                REDecisionEntity dec = new REDecisionEntity();
                dec.setSession((BSession) mainDispatcher.getSession());
                dec.setIdDecision(idDecision);
                dec.retrieve();

                vb.setIdDecision(idDecision);
                vb.setIdTiersBeneficiairePrincipal(dec.getIdTiersBeneficiairePrincipal());
                PRTiersWrapper tw = PRTiersHelper.getTiersParId(mainDispatcher.getSession(),
                        dec.getIdTiersBeneficiairePrincipal());
                if (tw != null) {
                    vb.setTiersBeneficiairePrincipalInfo(tw.getDescription((BSession) mainDispatcher.getSession()));
                }

                this.saveViewBean(vb, request);
            } else {
                REPreparerDecisionStandardViewBean prepDecisionStandardViewBean = new REPreparerDecisionStandardViewBean();
                JSPUtils.setBeanProperties(request, prepDecisionStandardViewBean);

                prepDecisionStandardViewBean.setIdTiersRequerant(vb.getIdTiersRequerant());

                // Restore de l'ancien viewBean dans la session
                prepDecisionStandardViewBean.setMessage(vb.getMessage());
                prepDecisionStandardViewBean.setMsgType(vb.getMsgType());

                this.saveViewBean(prepDecisionStandardViewBean, session);
                this.saveViewBean(prepDecisionStandardViewBean, request);

                destination = getRelativeURLwithoutClassPart(request, session) + "preparerDecisions" + "_de.jsp";
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    /**
     * @return
     */
    public synchronized Integer getNextIdProvAnnexe() {

        Integer key = nextKeyIdProvAnnexe;

        if (nextKeyIdProvAnnexe.intValue() == Integer.MAX_VALUE) {
            nextKeyIdProvAnnexe = new Integer(0);
        } else {
            nextKeyIdProvAnnexe = new Integer(nextKeyIdProvAnnexe.intValue() + 1);
        }

        return key;
    }

    /**
     * @return
     */
    public synchronized Integer getNextIdProvCopie() {

        Integer key = nextKeyIdProvCopie;

        if (nextKeyIdProvCopie.intValue() == Integer.MAX_VALUE) {
            nextKeyIdProvCopie = new Integer(0);
        } else {
            nextKeyIdProvCopie = new Integer(nextKeyIdProvCopie.intValue() + 1);
        }

        return key;
    }

    /**
     * Cree une chaine de caractere du type '/nomServlet?userAction=action'
     * 
     * @param request
     *            DOCUMENT ME!
     * @param action
     *            une action COMPLETE (avec actionPart)
     * @return DOCUMENT ME!
     */
    @Override
    protected String getUserActionURL(final HttpServletRequest request, final String action) {
        return request.getServletPath() + "?userAction=" + action;
    }

    public void supprimerDecision(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, final FWViewBeanInterface viewBean)
            throws Exception {

        REPreValiderDecisionViewBean vb = (REPreValiderDecisionViewBean) viewBean;

        mainDispatcher.dispatch(vb, getAction());

        String destination = getRelativeURLwithoutClassPart(request, session) + "decisions" + "_rc.jsp?noDemandeRente="
                + vb.getIdDemandeRente();

        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            destination = _getDestSupprimerEchec(session, request, response, viewBean);
        }

        REDecisionJointDemandeRenteViewBean vb2 = new REDecisionJointDemandeRenteViewBean();
        vb2.setIdDemandeRente(vb.getIdDemandeRente());
        vb2.setSession(vb.getSession());

        this.saveViewBean(vb2, session);

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

}
