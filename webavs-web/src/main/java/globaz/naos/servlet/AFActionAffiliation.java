package globaz.naos.servlet;

import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWActionGedResult;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.ged.message.JadeGedCallDefinition;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationViewBean;
import globaz.naos.db.wizard.AFWizardViewBean;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFVerrouAffiliation;
import globaz.osiris.db.comptes.CASection;
import globaz.pyxis.summary.TIActionSummary;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Affiliation.
 * 
 * @author sau
 */
public class AFActionAffiliation extends AFIdeDefaultActionChercher {

    public final static String ACTION_AFFICHER_ADRESSES_TIERS = "rechercheAdressesTiers";
    public final static String ACTION_AFFICHER_DOSSIER_GED = "gedafficherdossier";
    public final static String ACTION_AFFICHER_GESTION_ENVOI = "gestionEnvois";
    public final static String ACTION_RECHERCHE_DECISION_CP = "rechercheDecisionCP";
    public final static String ACTION_RECHERCHE_ID_COMPTE_ANNEXE = "rechercheIdCompteAnnexe";

    private static final String BLANK = "";

    /**
     * Constructeur d'AFActionAffiliation.
     * 
     * @param servlet
     */
    public AFActionAffiliation(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Destination après un ajout réussi dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        session.removeAttribute("affiliationPrincipale");
        session.setAttribute("affiliationPrincipale", ((AFAffiliationViewBean) viewBean).getAffiliationId());

        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart() + "."
                + getAction().getPackagePart() + "." + getAction().getClassPart() + ".afficher";
    }

    /**
     * Effectue les traitements pour en afficher les détails.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        try {
            String _destination = "";
            String _codeDestination = AFActionAutreDossier.CODE_ECRAN_DETAIL_AFFILIATION;
            try {
                FWAction action = FWAction.newInstance(request.getParameter("userAction"));
                startUsingContext(session);

                String method = request.getParameter("_method");
                if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                    action.changeActionPart(FWAction.ACTION_NOUVEAU);
                }

                String affiliationId = request.getParameter("selectedId");

                AFAffiliationViewBean viewBean = new AFAffiliationViewBean();

                FWViewBeanInterface vbSession = (FWViewBeanInterface) session.getAttribute("viewBean");
                if (vbSession != null && AFAffiliationViewBean.class.isAssignableFrom(vbSession.getClass())) {
                    viewBean.setWarningMessageAnnonceIdeCreationNotAdded(((AFAffiliationViewBean) vbSession)
                            .getWarningMessageAnnonceIdeCreationNotAdded());
                } else if (vbSession != null && AFWizardViewBean.class.isAssignableFrom(vbSession.getClass())) {
                    viewBean.setWarningMessageAnnonceIdeCreationNotAdded(((AFWizardViewBean) vbSession)
                            .getWarningMessageAnnonceIdeCreationNotAdded());
                }

                String idTiers = "";
                if (!JadeStringUtil.isBlank(request.getParameter("idTiers"))) {
                    idTiers = request.getParameter("idTiers");
                    session.removeAttribute("tiersPrincipale");
                    session.setAttribute("tiersPrincipale", idTiers);
                }

                if (!((method != null) && method.equals("add"))) {
                    if (JadeStringUtil.isEmpty(affiliationId)) {
                        if (!JadeStringUtil.isBlank((String) session.getAttribute("affiliationPrincipale"))) {
                            affiliationId = (String) session.getAttribute("affiliationPrincipale");
                            viewBean.setAffiliationId(affiliationId);
                        } else {
                            _codeDestination = AFActionAutreDossier.CODE_ECRAN_LIST_AFFILIATION;
                        }
                    } else {
                        session.removeAttribute("affiliationPrincipale");
                        session.setAttribute("affiliationPrincipale", affiliationId);
                        viewBean.setAffiliationId(affiliationId);
                    }
                }

                if (action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                    viewBean = (AFAffiliationViewBean) beforeNouveau(session, request, response, viewBean);
                }

                viewBean = (AFAffiliationViewBean) beforeAfficher(session, request, response, viewBean);
                viewBean = (AFAffiliationViewBean) mainDispatcher.dispatch(viewBean, action);
                if (viewBean.isNew() && ((method == null) || ((method != null) && !method.equalsIgnoreCase("ADD")))) {
                    _codeDestination = AFActionAutreDossier.CODE_ECRAN_LIST_AFFILIATION;
                }
                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", viewBean);
                request.getSession().setAttribute(TIActionSummary.PYXIS_VG_IDTIERS_CTX, viewBean.getIdTiers());

                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                    _destination = FWDefaultServletAction.ERROR_PAGE;
                } else {
                    if (_codeDestination.equals(AFActionAutreDossier.CODE_ECRAN_LIST_AFFILIATION)) {
                        _destination = getActionFullURL() + ".chercher";
                    } else {
                        _destination = getRelativeURL(request, session) + "_de.jsp";
                    }
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        } catch (Exception ex) {
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    /**
     * @param session
     *            la session HTTP
     * @param request
     *            la requête HTTP
     * @param response
     *            la response HTTP
     */
    private void actionAfficherDossierGed(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // System.out.println("*-*-*-*-*-*-*-*-*-*-*-*");
        // System.out.println("* début afficher GED *");
        // System.out.println("*-*-*-*-*-*-*-*-*-*-*-*");

        // récup du noAffilie depuis la request
        String noAffilie = request.getParameter("noAffiliationId");
        String idRole = request.getParameter("idRole");
        String idContentieux = request.getParameter("idContentieux");
        String idTiers = request.getParameter("gedIdTiers");
        // création des props à passer à la GED
        Properties gedCallProps = new Properties();

        if ("null".equals(idContentieux)) {
            idContentieux = null;
        }

        String idSection = request.getParameter("idSection");

        if (JadeStringUtil.isIntegerEmpty(idSection)) {
            idSection = request.getParameter(CADocumentInfoHelper.SECTION_ID);
            if (JadeStringUtil.isIntegerEmpty(idSection)) {
                idSection = "";
            }
        }

        if (JadeStringUtil.isIntegerEmpty(idContentieux)) {
            idContentieux = request.getParameter("aquila.contentieux.idContentieux");
            if ("null".equals(idContentieux)) {
                idContentieux = null;
            }

            if (JadeStringUtil.isEmpty(idContentieux)) {

                if (!JadeStringUtil.isIntegerEmpty(idSection)) {
                    // essaie de trouver un idContentieux depuis un idsection
                    try {
                        idContentieux = CASection.findIdContentieuxAquilaForIdSection(idSection,
                                (BISession) session.getAttribute(FWServlet.OBJ_SESSION));
                    } catch (Exception e) {
                        throw new ServletException(e);
                    }
                    if (JadeStringUtil.isIntegerEmpty(idContentieux)) {
                        idContentieux = "";
                    }
                } else {
                    idContentieux = "";
                }
            }
        }

        idRole = checkRole(session, request, idRole, idContentieux, idSection);

        if (JadeStringUtil.isIntegerEmpty(idRole)) {
            idRole = request.getParameter(TIDocumentInfoHelper.ROLE_TIERS_DOCUMENT);
            if (JadeStringUtil.isIntegerEmpty(idRole)) {
                idRole = "";
            }
        }

        if (JadeStringUtil.isBlank(noAffilie)) {

            try {
                AFAffiliationViewBean affilVB = (AFAffiliationViewBean) session.getAttribute("viewBean");
                noAffilie = affilVB.getAffilieNumero();
                // System.out.println(" ***** on a récupéré un numéro d'affilié ("
                // + noAffilie + ") depuis le viewbean en session");
            } catch (RuntimeException rte) {/* DO NOTHING */
            }

            if (JadeStringUtil.isBlank(noAffilie)) {
                noAffilie = request.getParameter(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE);
                if (noAffilie == null) {
                    noAffilie = AFActionAffiliation.BLANK;
                    // System.out.println(" ***** pas de numéro d'affilié (" +
                    // noAffilie + ") !!");
                } else {
                    // System.out.println(" ***** on a récupéré un numéro d'affilié ("
                    // + noAffilie + ") depuis la requête (num role formatté)");
                }
            }
        } else {
            // System.out.println(" ***** on a récupéré un numéro d'affilié (" +
            // noAffilie + ") depuis la requête.");
        }
        // récup du noAVS depuis la request
        String noAVS = request.getParameter("noAVSId");
        if (noAVS == null) {
            noAVS = request.getParameter(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE);
            if (noAVS == null) {
                noAVS = AFActionAffiliation.BLANK;
            }
        }
        // récup de l'année depuis la request
        String annee = request.getParameter("annee");
        if (annee == null) {
            // annee =
            // request.getParameter(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE);
            if (annee == null) {
                annee = AFActionAffiliation.BLANK;
            }
        }
        // récup du nomService depuis la request
        String nomService = request.getParameter("serviceNameId");
        if (JadeStringUtil.isBlank(nomService)) {
            try {
                nomService = GlobazServer.getCurrentSystem().getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                        .getProperty(AFApplication.PROPERTY_GED_SERVICE_NAME);
            } catch (Exception e) {
                nomService = null;
            }
        }
        if (nomService == null) {
            nomService = AFActionAffiliation.BLANK;
        }
        // specifique FER
        String nomRole = "";
        if ("517039".equals(idRole)) {
            nomRole = "PAR";
        } else if ("517040".equals(idRole)) {
            nomRole = "IND";
        } else if ("517041".equals(idRole)) {
            nomRole = "ADM";
        }

        // récup du osiris.section.IdExterne
        String sectionIdExterne = request.getParameter("osiris.section.idExterne");
        if (sectionIdExterne == null) {
            sectionIdExterne = AFActionAffiliation.BLANK;
        }

        // Données pour la comptabilité fournisseur
        String idExterneFournisseur = request.getParameter("lynx.fournisseur.idExterne");
        if (idExterneFournisseur == null) {
            idExterneFournisseur = AFActionAffiliation.BLANK;
        }

        String idExterneSectionFournisseur = request.getParameter("lynx.section.idExterne");
        if (idExterneSectionFournisseur == null) {
            idExterneSectionFournisseur = AFActionAffiliation.BLANK;
        }

        if ((request != null) && (request.getSession() != null)) {
            try {
                BSession bSession = (BSession) CodeSystem.getSession(session);
                gedCallProps.setProperty(JadeGedTargetProperties.USER, bSession.getUserId());
            } catch (Exception e) {
                new ServletException("GED call failed. ", e);
            }
        }

        // on commence par copier toutes les propriétés reçues dans la request
        for (Enumeration<?> e = request.getParameterNames(); e.hasMoreElements();) {
            String name = e.nextElement().toString();
            gedCallProps.setProperty(name, request.getParameter(name));
        }
        // on veut montrer un dossier: ACTION pour les deux GED
        gedCallProps.setProperty(JadeGedTargetProperties.ACTION, JadeGedTargetProperties.ACTION_OPEN_FOLDER);
        // propriété TI:groupdoc: nom correspondant à un folder
        gedCallProps.setProperty(JadeGedTargetProperties.FOLDER_TYPE, "affilie");
        // ajout du nom de service, pour au cas où
        gedCallProps.setProperty(JadeGedTargetProperties.SERVICE, nomService);
        // propriétés business, correspondant à des indexes
        gedCallProps.setProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE, noAffilie);
        // spécifique FER: PAR, IND ou ADM
        gedCallProps.setProperty(TIDocumentInfoHelper.ROLE_TIERS_DOCUMENT, nomRole);
        // id d'un dossier contentieux (rien a voir avec affiliation, à voir si on ne devrait pas deplacer cette
        // classe...)
        gedCallProps.setProperty("aquila.contentieux.idContentieux", idContentieux);
        gedCallProps.setProperty(CADocumentInfoHelper.SECTION_ID, idSection);

        try {
            AFApplication app = (AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS);
            gedCallProps.setProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE,
                    app.getAffileFormater().unformat(noAffilie));
        } catch (Exception ex) {
            // formateur non trouvé, essai sans les points
            gedCallProps.setProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE,
                    JadeStringUtil.removeChar(noAffilie, '.'));
        }
        gedCallProps.setProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE, noAVS);
        gedCallProps.setProperty("annee", annee);

        gedCallProps.setProperty("osiris.section.idExterne", sectionIdExterne);

        gedCallProps.setProperty("lynx.fournisseur.idExterne", idExterneFournisseur);
        gedCallProps.setProperty("lynx.section.idExterne", idExterneSectionFournisseur);

        // technique: besoin de l'adresse ip du client
        gedCallProps.setProperty(JadeGedTargetProperties.IP_ADDRESS, request.getRemoteAddr());

        if (!JadeStringUtil.isEmpty(idTiers)) {
            gedCallProps.setProperty(TIDocumentInfoHelper.TIERS_ID, idTiers);
        }

        try {
            // System.out.println(" ***** on va appeler la JadeGedFacade avec les properties");
            JadeGedCallDefinition result = JadeGedFacade.call(gedCallProps);
            // System.out.println(" ***** on va en mettre le résultat en requête");
            request.setAttribute(FWActionGedResult.OBJ_JADE_CALL_RESULT, result);
            // JadeGedCallType rType = result.getType();
            // System.out.println(" ***** type de résultat GED: " +
            // rType.toString());
            // System.out.println(" ***** commande à exécuter: " +
            // result.getCommand());
            // System.out.println(" ***** on prépare le servletCall");
            String servletCall = request.getServletPath() + "?userAction=" + FWActionGedResult.FRAMEWORK_GED;
            // System.out.println(" ***** on choppe la page " + servletCall +
            // " pour y faire un forward.");
            RequestDispatcher rd = request.getRequestDispatcher(servletCall);
            // System.out.println(" ***** alors on a le RequestDispatcher, qui vaut "
            // + rd + ".");
            rd.forward(request, response);
        } catch (Exception e) {
            new ServletException("GED call failed. ", e);
        }
        // System.out.println("*-*-*-*-*-*-*-*-*-*-*");
        // System.out.println("* fini afficher GED *");
        // System.out.println("*-*-*-*-*-*-*-*-*-*-*");
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        if ("affiliation".equals(getAction().getClassPart())) {
            // Met un verrou sur la création d'affiliations
            synchronized (AFVerrouAffiliation.getInstance()) {
                super.actionAjouter(session, request, response, mainDispatcher);
            }
        } else {
            super.actionAjouter(session, request, response, mainDispatcher);
        }
    }

    /**
     * 
     * Action utilisée pour la recherche.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        int cas = 1;
        String _destination = "";
        String idTiers = "";
        String affiliationId = "";

        AFAffiliationViewBean viewBean = new AFAffiliationViewBean();

        if (!JadeStringUtil.isBlank(request.getParameter("idTiers"))) {
            idTiers = request.getParameter("idTiers");

            session.removeAttribute("tiersPrincipale");
            session.setAttribute("tiersPrincipale", idTiers);
        } else {
            if (!JadeStringUtil.isBlank((String) session.getAttribute("tiersPrincipale"))) {
                idTiers = (String) session.getAttribute("tiersPrincipale");

                session.removeAttribute("tiersPrincipale");
                session.setAttribute("tiersPrincipale", idTiers);
            } else {
                cas = 2;
            }

            if (!JadeStringUtil.isBlank((String) session.getAttribute("affiliationPrincipale"))) {
                affiliationId = (String) session.getAttribute("affiliationPrincipale");
            } else if (!JadeStringUtil.isBlank(request.getParameter("affiliationId"))) {
                affiliationId = request.getParameter("affiliationId");
            } else {
                cas = 2;
            }
        }

        if (cas == 1) {
            viewBean.setAffiliationId(affiliationId);
            viewBean.setIdTiers(idTiers);

            try {
                FWAction action = getAction();
                action.changeActionPart(FWAction.ACTION_AFFICHER);
                viewBean = (AFAffiliationViewBean) mainDispatcher.dispatch(viewBean, action);
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (cas == 1) {
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_rc.jsp";
            }
        } else if (cas == 2) {
            _destination = "/" + getAction().getApplicationPart()
                    + "?userAction=naos.affiliation.autreDossier.afficher&_method=upd";
            // _destination = getRelativeURLwithoutClassPart(request, session) +
            // "affilieSelect_rc.jsp?";
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action utilisée pour le recherche des adresses de Tiesr.
     * 
     * @param session
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void actionChercherAdressesTiers(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        AFAffiliationViewBean viewBean = new AFAffiliationViewBean();
        try {
            BSession bSession = (BSession) CodeSystem.getSession(session);
            viewBean.setSession(bSession);
            viewBean.setAffiliationId(request.getParameter("affiliationId"));
            viewBean.retrieve();

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = "/naos?userAction=naos.affiliation.affiliation.afficher";
        } else {
            // &_method=add&back=_sl
            _destination = "/pyxis?userAction=pyxis.adressecourrier.avoirAdresse.afficher&_method=add&back=_sl&idTiers="
                    + viewBean.getIdTiers() + "&idExterne=" + viewBean.getAffilieNumero();
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action utilisée pour le recherche une descision dans Cot. Pers.
     * 
     * @param session
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void actionChercherDecisionCP(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        AFAffiliationViewBean viewBean = new AFAffiliationViewBean();
        try {
            BSession bSession = (BSession) CodeSystem.getSession(session);
            viewBean.setSession(bSession);
            viewBean.setAffiliationId(request.getParameter("affiliationId"));
            viewBean.retrieve();

            boolean hasRight = bSession.hasRight("phenix.principale.decision", FWSecureConstants.READ);
            if (!hasRight) {
                String msg = FWMessageFormat.format("ERROR User [{0}] has no right [{1}] for the action [{2}]",
                        bSession.getUserId(), FWSecureConstants.READ, "phenix.principale.decision");
                viewBean.setMessage(msg);
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = "/naos?userAction=naos.affiliation.affiliation.afficher";
        } else {
            request.getSession().setAttribute(TIActionSummary.PYXIS_VG_IDTIERS_CTX, viewBean.getIdTiers());
            _destination = "/phenix?userAction=phenix.principale.decision.chercher&idTiers= " + viewBean.getIdTiers()
                    + "&selectedId2=" + viewBean.getAffiliationId();
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * @param session
     *            la session HTTP
     * @param request
     *            la requête HTTP
     * @param response
     *            la response HTTP
     */
    private void actionChercherGestionsEnvois(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String _destination = "";
        String urlGestionEnvoi = "";
        AFAffiliationViewBean viewBean = new AFAffiliationViewBean();
        try {
            BSession bSession = (BSession) CodeSystem.getSession(session);
            viewBean.setSession(bSession);
            viewBean.setAffiliationId(request.getParameter("affiliationId"));
            viewBean.retrieve();
            urlGestionEnvoi = globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeProv1
                    + "="
                    + globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_NUMERO
                    + "&"
                    + globaz.leo.db.envoi.LEEnvoiViewBean.SRC_valProv1
                    + "="
                    + viewBean.getAffilieNumero()
                    + "&"
                    + globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeProv2
                    + "="
                    + globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_ID_TIERS
                    + "&"
                    + globaz.leo.db.envoi.LEEnvoiViewBean.SRC_valProv2
                    + "="
                    + viewBean.getIdTiers()
                    + "&"
                    + globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeProv3
                    + "="
                    + globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_ROLE
                    + "&"
                    + globaz.leo.db.envoi.LEEnvoiViewBean.SRC_valProv3
                    + "="
                    + globaz.pyxis.db.tiers.TIRole.CS_AFFILIE
                    + "&"
                    + globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeProv4
                    + "="
                    + globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE
                    + "&"
                    + globaz.leo.db.envoi.LEEnvoiViewBean.SRC_valProv4
                    + "="
                    + globaz.naos.application.AFApplication.DEFAULT_APPLICATION_NAOS
                    + "&"
                    + globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeInterProv1
                    + "="
                    + globaz.naos.translation.CodeSystem.getLibelle(session,
                            globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_NUMERO)
                    + "&"
                    + globaz.leo.db.envoi.LEEnvoiViewBean.SRC_valInterProv2
                    + "="
                    + viewBean.getTiers().getNomPrenom()
                    + "&"
                    + globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeInterProv2
                    + "="
                    + globaz.naos.translation.CodeSystem.getLibelle(session,
                            globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_ID_TIERS)
                    + "&"
                    + globaz.leo.db.envoi.LEEnvoiViewBean.SRC_valInterProv3
                    + "="
                    + globaz.naos.translation.CodeSystem.getLibelle(session, globaz.pyxis.db.tiers.TIRole.CS_AFFILIE)
                    + "&"
                    + globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeInterProv3
                    + "="
                    + globaz.naos.translation.CodeSystem.getLibelle(session,
                            globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_ROLE)
                    + "&"
                    + globaz.leo.db.envoi.LEEnvoiViewBean.SRC_typeInterProv4
                    + "="
                    + globaz.naos.translation.CodeSystem.getLibelle(session,
                            globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE) + "&"
                    + globaz.leo.db.envoi.LEEnvoiViewBean.SRC_GO_BACK
                    + "=\\naos?userAction=naos.affiliation.affiliation.afficher";
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = "/naos?userAction=naos.affiliation.affiliation.afficher";
        } else {
            _destination = "/leo?userAction=leo.envoi.envoi.chercher&" + urlGestionEnvoi;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action utilisée pour le recherche d'un compte annexe.
     * 
     * @param session
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void actionChercherIdCompteAnnexe(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        AFAffiliationViewBean viewBean = new AFAffiliationViewBean();

        try {
            BSession bSession = (BSession) CodeSystem.getSession(session);
            viewBean.setSession(bSession);
            viewBean.setAffiliationId(request.getParameter("affiliationId"));
            viewBean.retrieve();
            /*
             * CACompteAnnexe compte = new CACompteAnnexe(); compte.setSession(bSession); compte.setAlternateKey(1);
             * compte.setIdRole("517002"); compte.setIdExterneRole(viewBean.getAffilieNumero());
             * compte.wantCallMethodBefore(false); compte.retrieve(); if
             * (compte.getIdCompteAnnexe().equalsIgnoreCase("")) { viewBean.setMsgType(FWViewBeanInterface.ERROR);
             * viewBean.setMessage("Il n'y a pas de compte annexe pour ce cas"); if
             * (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) { _destination
             * ="/"+getAction().getApplicationPart()+ "?userAction=naos.affiliation.affiliation.afficher"; } }
             * idCompteAnnexe = compte.getIdCompteAnnexe(); } catch (Exception e) {
             * viewBean.setMsgType(FWViewBeanInterface.ERROR); viewBean.setMessage(e.getMessage()); } if
             * (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) { session.removeAttribute("viewBean");
             * session.setAttribute("viewBean", viewBean); _destination = getRelativeURL(request, session) + "_de.jsp";
             * } else { if(_destination.equalsIgnoreCase("")){ _destination =
             * "/osiris?userAction=osiris.comptes.apercuParSection.chercher&idCompteAnnexe=" + idCompteAnnexe; } }
             */
            session.setAttribute(globaz.osiris.servlet.action.compte.CAApercuComptes.SEARCH_CPT_ANNEXE_SAVED_MASK,
                    viewBean.getAffilieNumero());
            _destination = "/osiris?userAction=osiris.comptes.apercuComptes.chercher";
        } catch (Exception e) {
            _destination = "/" + getAction().getApplicationPart() + "?userAction=naos.affiliation.affiliation.afficher";
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
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

        if (AFActionAffiliation.ACTION_RECHERCHE_ID_COMPTE_ANNEXE.equals(getAction().getActionPart())) {
            actionChercherIdCompteAnnexe(session, request, response);
        } else if (AFActionAffiliation.ACTION_RECHERCHE_DECISION_CP.equals(getAction().getActionPart())) {
            actionChercherDecisionCP(session, request, response);
        } else if (AFActionAffiliation.ACTION_AFFICHER_DOSSIER_GED.equals(getAction().getActionPart())) {
            actionAfficherDossierGed(session, request, response);
        } else if (AFActionAffiliation.ACTION_AFFICHER_ADRESSES_TIERS.equals(getAction().getActionPart())) {
            actionChercherAdressesTiers(session, request, response);
        } else if (AFActionAffiliation.ACTION_AFFICHER_GESTION_ENVOI.equals(getAction().getActionPart())) {
            actionChercherGestionsEnvois(session, request, response);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /**
     * Effectue des traitements avant l'ajout d'une nouvelle entité dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAjouter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFAffiliationViewBean vBean = (AFAffiliationViewBean) viewBean;

        try {
            vBean.setDateCreation(JACalendar.todayJJsMMsAAAA());

            if (vBean.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)) {

                AFAffiliationManager aff = new AFAffiliationManager();
                aff.setForAffilieNumero(vBean.getAffilieNumero());
                aff.setSession(vBean.getSession());
                aff.find();

                if (aff.size() == 0) {
                    vBean.setDateTent(JACalendar.todayJJsMMsAAAA());
                }
                // vBean.add();
                // vBean.setSelection("2");
            }
        } catch (Exception e) {
            vBean.setMsgType(FWViewBeanInterface.ERROR);
            vBean.setMessage("ERROR Unable to update data (" + e.toString() + ")");
        }
        return vBean;
    }

    /**
     * Effectue des traitements avant la saisie d'une nouvelle entité.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFAffiliationViewBean vBean = (AFAffiliationViewBean) viewBean;
        vBean.setIdTiers(request.getParameter("idTiers"));
        vBean.setTypeAffiliation(CodeSystem.TYPE_AFFILI_FICHIER_CENT);

        return vBean;
    }

    /**
     * @param session
     * @param noRole
     * @param idContentieux
     * @param idSection
     * @return le noRole correspondant au compte annexe.
     * @throws ServletException
     */
    private String checkRole(HttpSession session, HttpServletRequest request, String noRole, String idContentieux,
            String idSection) throws ServletException {
        if (!JadeStringUtil.isBlankOrZero(idContentieux)) {
            if (!JadeStringUtil.isIntegerEmpty(idSection)) {
                if (JadeStringUtil.isBlank(request.getParameter(TIDocumentInfoHelper.ROLE_TIERS_DOCUMENT))) {
                    CASection section = new CASection();
                    section.setSession((BSession) session.getAttribute(FWServlet.OBJ_SESSION));
                    section.setIdSection(idSection);
                    try {
                        section.retrieve();
                    } catch (Exception e1) {
                        throw new ServletException(e1);
                    }

                    noRole = section.getCompteAnnexe().getIdRole();
                }
            }
        }
        return noRole;
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

    private void startUsingContext(HttpSession session) throws SQLException, Exception {
        JadeThreadActivator.startUsingJdbcContext(this, getContext(session));
        JadeThread.storeTemporaryObject("bsession", ((globaz.framework.controller.FWController) session
                .getAttribute(FWServlet.OBJ_CONTROLLER)).getSession());
    }
}
