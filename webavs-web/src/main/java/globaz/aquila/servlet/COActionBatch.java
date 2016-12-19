package globaz.aquila.servlet;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.transition.COTransitionAction;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.batch.COTransitionListViewBean;
import globaz.aquila.db.batch.COTransitionViewBean;
import globaz.aquila.db.poursuite.COContentieuxViewBean;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.taxes.ICOTaxeProducer;
import globaz.aquila.util.COActionUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.osiris.db.interets.CADetailInteretMoratoire;
import globaz.osiris.db.interets.CADetailInteretMoratoireManager;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.process.interetmanuel.CAProcessInteretMoratoireManuel;
import globaz.osiris.process.interetmanuel.visualcomponent.CAInteretManuelVisualComponent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <h1>Description</h1>
 * <p>
 * Classe de base pour les actions 'aquila.batch'.
 * </p>
 * <p>
 * Le dernier contentieux sélectionné pour une action d'édition est toujours stocké comme attribut de la session http en
 * utilisant la clé "contentieuxViewBean". Cet attribut est utilisé ensuite pour afficher l'en-tête jsp standard des
 * contentieux.
 * </p>
 * 
 * @author Arnaud Dostes, 18-oct-2004
 */
public class COActionBatch extends CODefaultServletAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Initialise l'action.
     * 
     * @param servlet
     *            Le servlet concerné par cette action
     */
    public COActionBatch(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Annule la dernière transition pour le contentieux courant.
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @param appSession
     * @return l'uri de la page vers laquelle rediriger la servlet
     * @throws ServletException
     * @throws IOException
     */
    private String actionAnnulerDerniereTransition(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher, BSession appSession) throws ServletException,
            IOException {
        /*
         * Pour annuler la dernière étape : - On appelle la méthode annuler() de l'action de transition correspondant à
         * l'avant-dernière ligne de l'historique.
         */
        String titleMail = "";
        String corpsMail = "";

        try {
            // Récupère le contentieux
            COContentieuxViewBean contentieux = (COContentieuxViewBean) chargerContentieux(request, session, appSession);
            if (JadeStringUtil.equals(request.getParameter("idExtourne"), "on", false)) {
                contentieux.setIdExtourne(true);
            } else {
                contentieux.setIdExtourne(false);
            }

            titleMail = contentieux.getSession().getLabel("AQUILA_ETAPE_ANNULEE_LIBELLE_MAIL")
                    + contentieux.getEtape().getLibEtapeLibelle();
            corpsMail = contentieux.getSession().getLabel("AQUILA_ETAPE_ANNULEE_LIBELLE_MAIL")
                    + contentieux.getEtape().getLibEtapeLibelle() + " ("
                    + contentieux.getCompteAnnexe().getRole().getDescription() + " "
                    + contentieux.getCompteAnnexe().getIdExterneRole() + " " + contentieux.getSection().getIdExterne()
                    + ")";

            try {
                // Créer un viewBean avec un paramètre boolean pour les if
                // ci-dessous ?
                dispatcher.dispatch(contentieux, getAction());

                if (FWViewBeanInterface.ERROR.equals(contentieux.getMsgType())) {
                    // l'annulation est en erreur, on affiche le détail du
                    // dossier pour afficher l'erreur
                    // on met d'abord le contentieux en session pour afficher la
                    // page
                    session.setAttribute("viewBean", contentieux);

                    // et on redirige directement sur la jsp pour éviter que le
                    // message ne se perde
                    FWAction action = COActionUtils.createAction(contentieux, dispatcher.getPrefix(),
                            FWAction.ACTION_AFFICHER);

                    // Le constructeur d'url
                    // (FWDefaultServletAction#viewResolver) est privé. on en
                    // est réduit à construire à la main
                    // HACK: on construit l'url à la main
                    String url = getRelativeURLwithoutClassPart(request, session);

                    // enlever le slash final
                    if (url.endsWith("/")) {
                        url = url.substring(0, url.length() - 2);
                    }

                    // enlever le package
                    url = url.substring(0, url.lastIndexOf('/'));

                    titleMail = contentieux.getSession().getLabel("AQUILA_ETAPE_ANNULEE_LIBELLE_MAIL_ERROR")
                            + contentieux.getEtape().getLibEtapeLibelle();
                    corpsMail = contentieux.getSession().getLabel("AQUILA_ETAPE_ANNULEE_LIBELLE_MAIL_ERROR")
                            + contentieux.getMessage();

                    return url + "/" + action.getPackagePart() + "/" + action.getClassPart() + "_de.jsp";
                }
            } finally {
                // remonter les erreurs dans le viewBean et fermer la
                // transaction
                JadeSmtpClient.getInstance().sendMail(request.getParameter("eMailAddress"), titleMail, corpsMail, null);
            }

            // Retourne a l'historique des etapes
            return getDestinationCustomSucces(contentieux, dispatcher);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return FWDefaultServletAction.ERROR_PAGE;
        }
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        chargerContentieux(request, session, (BSession) mainDispatcher.getSession());

        super.actionChercher(session, request, response, mainDispatcher);
    }

    /**
     * Il y a deux actions custom possibles, chacune a sa propre méthode:
     * {@link #actionAnnulerDerniereTransition(HttpSession, HttpServletRequest, HttpServletResponse, FWDispatcher, BSession)}
     * et
     * {@link #actionEffectuerTransition(HttpSession, HttpServletRequest, HttpServletResponse, FWDispatcher, BSession)}
     * .
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String destination = FWDefaultServletAction.ERROR_PAGE;
        BSession appSession = (BSession) dispatcher.getSession();

        if ("annulerdernieretransition".equals(getAction().getActionPart())) {
            destination = actionAnnulerDerniereTransition(session, request, response, dispatcher, appSession);
        } else if ("effectuertransition".equals(getAction().getActionPart())) {
            destination = actionEffectuerTransition(session, request, response, dispatcher, appSession);
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Effectue la transition dont l'utilisateur vient de remplir le masque. Etape manuelle.
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @param appSession
     * @return l'uri de la page vers laquelle rediriger la servlet
     * @throws ServletException
     * @throws IOException
     */
    private String actionEffectuerTransition(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher, BSession appSession) throws ServletException,
            IOException {
        String destination = FWDefaultServletAction.ERROR_PAGE;
        COTransitionViewBean transitionViewBean = getTransitionViewBean(session, request);

        try {
            COContentieux contentieux = chargerContentieux(request, session, appSession);

            // HACK sel : l'action est changée en fonction des critères de la
            // request.
            // Recherche la transition à effectuer
            COTransitionAction action = getTransitionAction(request, appSession, contentieux);

            JSPUtils.setBeanProperties(request, transitionViewBean);
            transitionViewBean.setContentieux((COContentieux) session.getAttribute("contentieuxViewBean"));
            transitionViewBean.setAction(action);
            transitionViewBean = (COTransitionViewBean) dispatcher.dispatch(transitionViewBean, getAction());

            // Met à jour le contentieux dans la session (il a peut-être été
            // remplacé plus haut)
            session.removeAttribute("contentieuxViewBean");
            session.setAttribute("contentieuxViewBean", transitionViewBean.getContentieux());

            if (transitionViewBean.getContentieux().getMsgType().equals(FWViewBeanInterface.ERROR)) {
                // En cas d'erreur on réaffiche la page
                destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
            } else {
                destination = getDestinationCustomSucces(transitionViewBean.getContentieux(), dispatcher);
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return destination;
    }

    /**
     * Charge la liste des taxes pour l'étape a exécuter et la renseigne dans le viewBean.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof COTransitionViewBean) {
            COContentieux contentieux = (COContentieux) session.getAttribute("contentieuxViewBean");

            try {
                BSession sessionb = (BSession) session.getAttribute("objSession");
                COTransitionViewBean transitionViewBean = (COTransitionViewBean) viewBean;

                // on charge la transition pour obtenir l'étape suivante, on ne
                // veut pas modifier le viewBean
                COTransition transition = new COTransition();
                transition.setIdTransition(transitionViewBean.getIdTransition());
                transition.setSession(sessionb);
                transition.retrieve();

                // POAVS-223
                boolean requisitionPoursuite = ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE.equals(transition
                        .getEtapeSuivante().getLibEtape());
                if (!requisitionPoursuite) {
                    // Recherche ou/et calcul des IM
                    transitionViewBean.setInteretCalcule(giveDecisionIM(sessionb, contentieux, transition));
                }

                // calculer les taxes
                ICOTaxeProducer producer = COServiceLocator.getTaxeService().getTaxeProducer(
                        transition.getEtapeSuivante());

                transitionViewBean
                        .setTaxes(producer.getListeTaxes(sessionb, contentieux, transition.getEtapeSuivante()));
            } catch (Exception e) {
                viewBean.setMessage(e.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    /**
     * "Nettoye" les effets de bords de la méthode {@link JSPUtils#setBeanProperties(HttpServletRequest, Object)}.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeLister(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof COTransitionListViewBean) {
            // le framework a renseigné les champs de type Boolean, on ne le
            // veut pas, on annule donc
            cleanBooleanDefaultValues(request, viewBean);
        }

        return viewBean;
    }

    /**
     * @param session
     * @param im
     * @return
     */
    private CADetailInteretMoratoireManager findDetailForIM(BSession session, CAInteretMoratoire im) {
        CADetailInteretMoratoireManager managerDIM = new CADetailInteretMoratoireManager();
        managerDIM.setSession(session);
        managerDIM.setForIdInteretMoratoire(im.getIdInteretMoratoire());
        try {
            managerDIM.find();
        } catch (Exception e) {
            // nope
        }
        return managerDIM;
    }

    /**
     * @param session
     * @param contentieux
     * @return
     */
    private CAInteretMoratoireManager findIMExistant(BSession session, COContentieux contentieux) {
        CAInteretMoratoireManager managerIM = new CAInteretMoratoireManager();
        managerIM.setSession(session);
        managerIM.setForIdCompteAnnexe(contentieux.getIdCompteAnnexe());
        managerIM.setForIdSection(contentieux.getIdSection());
        managerIM.setForIdJournalFacturation("0");
        try {
            managerIM.find();
        } catch (Exception e) {
            // nope
        }
        return managerIM;
    }

    /**
     * @param request
     * @param transitionViewBean
     * @return
     */
    private ArrayList<HashMap<String, String>> fraisEtInterets(HttpServletRequest request,
            COTransitionViewBean transitionViewBean) {
        ArrayList<HashMap<String, String>> fraisEtInterets = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i <= transitionViewBean.getMaxRows(); i++) {
            HashMap<String, String> infos = new HashMap<String, String>();
            String montant = JANumberFormatter.deQuote(request.getParameter("montantFrais" + i));
            if (!JadeStringUtil.isBlank(request.getParameter("rubriqueFrais" + i))
                    && !JadeStringUtil.isDecimalEmpty(montant)) {
                String[] s = request.getParameter("rubriqueFrais" + i).split(";");
                infos.put(COTransitionViewBean.RUBRIQUE, s[0]);
                infos.put(COTransitionViewBean.RUB_DESCRIPTION, s[1]);
                infos.put(COTransitionViewBean.LIBELLE, request.getParameter("libelleFrais" + i));
                infos.put(COTransitionViewBean.MONTANT, montant);
                fraisEtInterets.add(infos);
            }
        }
        return fraisEtInterets;
    }

    /**
     * Retourne la destination par défaut pour les action custom soit l'écran de suivi des étapes pour le contentieux
     * courant.
     * 
     * @param contentieux
     * @param dispatcher
     * @return
     * @throws Exception
     */
    private String getDestinationCustomSucces(COContentieux contentieux, FWDispatcher dispatcher) throws Exception {
        FWAction actionDetail = COActionUtils.createAction(contentieux.loadHistorique(), dispatcher.getPrefix(),
                FWAction.ACTION_CHERCHER);

        return "/" + actionDetail.getApplicationPart() + "?userAction=" + actionDetail.toString() + "&selectedId="
                + contentieux.getIdContentieux() + "&libSequence=" + contentieux.getLibSequence();
    }

    /**
     * Recherche la transition à effectuer pour le contentieux donné et l'étape suivante donnée et renseigne ses
     * propriétés avec les choix que l'utilisateur a saisis dans l'écran de transition.
     * 
     * @param request
     *            la requête depuis laquelle récupérer les choix de l'utilisateur
     * @param session
     *            la session de l'application
     * @param transaction
     *            la transaction
     * @param contentieux
     *            le contentieux courant
     * @return
     * @throws Exception
     */
    private COTransitionAction getTransitionAction(HttpServletRequest request, BSession session,
            COContentieux contentieux) throws Exception {
        COTransitionAction action = COServiceLocator.getActionService().getTransitionAction(session, null, contentieux,
                request.getParameter("idEtapeSuivante"), Boolean.TRUE, null);

        if (action != null) {
            JSPUtils.setBeanProperties(request, action);

            // Boucle qui teste si un des champs requis de l'étape est vide et
            // set la variable isError
            // en fonction du résultat. Ce test est nécessaire car certaines
            // étapes ne lèvent pas d'exception
            // via le validate en cas de d'erreur de sasie.
            List<COEtapeInfoConfig> etapeInfosCheckError = action.getTransition().getEtapeSuivante()
                    .loadEtapeInfoConfigs();
            boolean isError = false;
            for (Iterator<COEtapeInfoConfig> infoIter = etapeInfosCheckError.iterator(); infoIter.hasNext();) {
                COEtapeInfoConfig infoConfig = infoIter.next();
                String valeur = request.getParameter(infoConfig.createNomChamp());

                if (infoConfig.getRequis().booleanValue() && JadeStringUtil.isEmpty(valeur)) {
                    isError = true;
                }
            }

            // retrouve les infos par étapes
            List<COEtapeInfoConfig> etapeInfos = action.getTransition().getEtapeSuivante().loadEtapeInfoConfigs();

            for (Iterator<COEtapeInfoConfig> infoIter = etapeInfos.iterator(); infoIter.hasNext();) {

                COEtapeInfoConfig infoConfig = infoIter.next();
                String valeur = request.getParameter(infoConfig.createNomChamp());

                if (infoConfig.getRequis().booleanValue() && JadeStringUtil.isEmpty(valeur)) {
                    session.addError(session.getLabel("AQUILA_REQUIS") + " " + infoConfig.getLibelle());
                }

                action.addEtapeInfo(infoConfig, valeur);
                // remplacer la date d'exécution si nécessaire (cas ou l'étape
                // ne contient aucune erreur de saisie).
                if (infoConfig.getRemplaceDateExecution().booleanValue() && !isError) {
                    contentieux.setDateExecution(valeur);
                    action.setDateExecution(contentieux.getDateExecution());
                }
            }
        }

        return action;
    }

    /**
     * Retrouve le viewBean de transition dans la session puis récupère depuis la requête les choix de l'utilisateur
     * concernant les frais, frais variables et intérêts variables et les renseigne dans le viewBean.
     * 
     * @param session
     *            la session http
     * @param request
     *            la requête http
     * @return le viewBean de transition
     */
    private COTransitionViewBean getTransitionViewBean(HttpSession session, HttpServletRequest request) {
        COTransitionViewBean transitionViewBean = (COTransitionViewBean) session.getAttribute("viewBean");
        List<CAInteretManuelVisualComponent> listInterer = new ArrayList<CAInteretManuelVisualComponent>();
        // imputer les frais ou non
        for (int i = 0; i < transitionViewBean.getTaxes().size(); i++) {
            String imput = request.getParameter("imputer" + i);

            if ((imput != null) && imput.equalsIgnoreCase("on")) {
                (transitionViewBean.getTaxes().get(i)).setImputerTaxe(true);
            } else {
                (transitionViewBean.getTaxes().get(i)).setImputerTaxe(false);
            }
        }

        transitionViewBean.setFraisEtInterets(fraisEtInterets(request, transitionViewBean));
        if (transitionViewBean.getInteretCalcule() != null) {
            for (int i = 0; i < transitionViewBean.getInteretCalcule().size(); i++) {
                String imput = request.getParameter("imputerIM" + i);

                if (imput != null && imput.equalsIgnoreCase("on")) {
                    listInterer.add(transitionViewBean.getInteretCalcule().get(i));
                }
            }

            transitionViewBean.setInteretCalcule(listInterer);
        }
        return transitionViewBean;
    }

    /**
     * Recherche ou/et calcul des IM
     * 
     * @param session
     * @param contentieux
     * @param transition
     * @return la liste des interets manuel ou null
     * @throws Exception
     */
    public List<CAInteretManuelVisualComponent> giveDecisionIM(BSession session, COContentieux contentieux,
            COTransition transition) throws Exception {

        if (contentieux == null) {
            return null;
        }

        // POAVS-223 ajout && !isNouveauRegime(
        if (!ICOEtape.CS_FRAIS_ET_INTERETS_RECLAMES.equals(transition.getEtapeSuivante().getLibEtape())
                || !isNouveauRegime(session, contentieux.getDateExecution())) {
            return null;
        }

        boolean requisitionPoursuite = ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE.equals(transition
                .getEtapeSuivante().getLibEtape());

        ArrayList<CAInteretManuelVisualComponent> liste = new ArrayList<CAInteretManuelVisualComponent>();
        CAInteretMoratoireManager managerIM = findIMExistant(session, contentieux);

        if ((managerIM != null) && !managerIM.isEmpty()) {
            for (int i = 0; i < managerIM.size(); i++) {
                CAInteretMoratoire im = (CAInteretMoratoire) managerIM.getEntity(i);

                if (im != null) {
                    CAInteretManuelVisualComponent visualComponent = new CAInteretManuelVisualComponent(im);

                    CADetailInteretMoratoireManager managerDIM = findDetailForIM(session, im);
                    for (int j = 0; (managerDIM != null) && (j < managerDIM.size()); j++) {
                        CADetailInteretMoratoire imd = (CADetailInteretMoratoire) managerDIM.getEntity(j);
                        visualComponent.addDetailInteretMoratoire(imd);
                    }

                    liste.add(visualComponent);
                }
            }
        } else {
            // Simuler IM
            CAProcessInteretMoratoireManuel process = new CAProcessInteretMoratoireManuel();
            process.setSession(session);
            process.setDateFin(JACalendar.todayJJsMMsAAAA());
            process.setIdSection(contentieux.getIdSection());
            process.setSimulationMode(true);
            process.setIsRDPProcess(requisitionPoursuite); // POAVS-223
            process.executeProcess();

            liste = process.getVisualComponents();
        }

        return liste;
    }

    public Boolean isNouveauRegime(BSession session, String dateExecution) {
        try {
            String dateProduction = session.getApplication().getProperty("dateProductionNouveauCDP");
            if (dateProduction == null) {
                return false;
            }

            // retourne true si la date d'execution de la RP est supérieure ou égale à la date de production du nouveau
            // regime
            return BSessionUtil.compareDateFirstGreaterOrEqual(session, dateExecution, dateProduction);
        } catch (Exception e) {
            JadeLogger.error(e, "La propriété n'existe pas.");
            return false;
        }
    }
}