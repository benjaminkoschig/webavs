package globaz.hermes.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEImpressionciViewBean;
import globaz.hermes.db.gestion.HEInputAnnonceViewBean;
import globaz.hermes.db.gestion.HELotListViewBean;
import globaz.hermes.db.gestion.HELotViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.db.gestion.HERassemblementViewBean;
import globaz.hermes.db.parametrage.HEAttenteEnvoiViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourListViewBean;
import globaz.hermes.print.itext.HEAvisDeces_Doc;
import globaz.hermes.print.itext.HEDocumentRemiseAttestCA;
import globaz.hermes.print.itext.HEDocumentRemiseCertifCA;
import globaz.hermes.print.itext.HEDocumentZas;
import globaz.hermes.print.itext.HELettreAffilieCA_Doc;
import globaz.hermes.print.itext.HEListIrrecouvrable;
import globaz.hermes.process.HEExtraitAnnonceProcess;
import globaz.hermes.process.HEExtraitCIAdditionnel;
import globaz.hermes.process.HEExtraitTermine;
import globaz.hermes.process.HEGenererCIPapier;
import globaz.hermes.utils.AVSUtils;
import globaz.hermes.utils.HENNSSUtils;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//
/**
 * Correction de la méthode actionExecuter
 * 
 * @author Alexandre Cuva
 * @since 26.11.2003 *
 */
public class HEActionGestions extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur HEActionGestions.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public HEActionGestions(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (getAction().toString().equals("hermes.gestion.inputAnnonce.ajouter")) {
            // saisie de masse
            HEInputAnnonceViewBean vBean = (HEInputAnnonceViewBean) viewBean;
            String numAVS = "";
            if (JadeStringUtil.isEmpty(vBean.getNumeroAVS())) {
                // calcul à 8
                // try{
                // AVSUtils avs = new
                // AVSUtils(vBean.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF),
                // vBean.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_JJMMAAAA),
                // Integer.parseInt(vBean.getField(IHEAnnoncesViewBean.SEXE)));
                // numAVS = avs.getNumeroAvs();
                // }catch(Exception e){}
            } else {
                numAVS = vBean.getNumeroAVS();
            }
            return getActionFullURL() + ".afficher&actionMessage=" + HEInputAnnonceViewBean.MSG_INSERT_OK + "&motif="
                    + vBean.getInputMotif() + "&critere=" + vBean.getInputCritere()
                    + "&_method=add&_valid=error&numAVS=" + numAVS + "&idDernierAjout=" + vBean.getIdAnnonce();
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    // l'action afficher est ici surchargée et teste si il s'agit d'un update ou
    // d'un delete et redirige en fonction sur la page d'ajout d'un arc.
    // La vérification des droits est fait lors de l'action modifier et
    // supprimer appelé avant (par défaut),c'est pourquoi on ne passe
    // volontairement pas dans le dispatcher ici.
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        if (request.getParameter("update") != null) {
            // on vient de l'écran de détail....
            String _destination;
            try {
                HEAttenteEnvoiViewBean vBean = (HEAttenteEnvoiViewBean) session.getAttribute("viewBean");
                FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
                _destination = getRelativeURL(request, session) + "_de.jsp?_method=add&_valid=error&numAVS="
                        + request.getParameter("numAVS");
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
                HEInputAnnonceViewBean vb = (HEInputAnnonceViewBean) FWViewBeanActionFactory.newInstance(_action,
                        mainDispatcher.getPrefix());
                vb.setActionMessage(HEInputAnnonceViewBean.MSG_UPDATE_OK);
                vb.setIdDernierAjout(request.getParameter("idDernierAjout"));
                vb.setRefUnique(vBean.getRefUnique());
                vb.setISession(vBean.getISession());
                vb.setInputMotif(vBean.getMotif());
                vb.setInputCritere(vBean.getCritere());
                vb.setParamNumeroAvs(request.getParameter("numeroAvs"));
                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", vb);
            } catch (Exception e) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        } else if (request.getParameter("delete") != null) {
            // on vient de l'écran de détail....
            String _destination;
            try {
                HEAttenteEnvoiViewBean vBean = (HEAttenteEnvoiViewBean) session.getAttribute("viewBean");
                FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
                _destination = getRelativeURL(request, session) + "_de.jsp?_method=add&_valid=error&numAVS="
                        + request.getParameter("numAVS");
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
                HEInputAnnonceViewBean vb = (HEInputAnnonceViewBean) FWViewBeanActionFactory.newInstance(_action,
                        mainDispatcher.getPrefix());
                vb.setActionMessage(HEInputAnnonceViewBean.MSG_DELETE_OK);
                vb.setISession(vBean.getISession());
                vb.setInputMotif(request.getParameter("motif"));
                vb.setInputCritere(request.getParameter("critere"));
                vb.setParamNumeroAvs(request.getParameter("numeroAvs"));
                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", vb);
            } catch (Exception e) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        } else {
            if (session.getAttribute("viewBean") != null
                    && HEInputAnnonceViewBean.class.equals(session.getAttribute("viewBean").getClass())) {
                HEInputAnnonceViewBean vBean = (HEInputAnnonceViewBean) session.getAttribute("viewBean");
                request.setAttribute("warningEmployeurSansPerso", vBean.getWarningEmployeurSansPersoOrAccountZero());
            }
            super.actionAfficher(session, request, response, mainDispatcher);
        }
    }

    /**
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    // Attention : le dispatcher ne doit pas être appelé dans une action
    // "chercher". La gestion des droits se fait sur l'action "afficher" par la
    // suite
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        FWAction _monAction = FWAction.newInstance(request.getParameter("userAction"));
        // String _myDestination = getRelativeURL() + "_rc.jsp";
        String _myDestination = getRelativeURL(request, session) + "_rc.jsp";
        String actionSuite = getActionSuite(request);
        if (actionSuite.equals("inputAnnonce")) {
            try {
                FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(_monAction,
                        mainDispatcher.getPrefix());
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
                HEInputAnnonceViewBean vBean = (HEInputAnnonceViewBean) viewBean;
                vBean.setSession((BSession) mainDispatcher.getSession());
                request.setAttribute("viewBean", vBean);
                _myDestination = getRelativeURL(request, session) + "_rc.jsp";
            } catch (Exception e) {
                e.printStackTrace();
                _myDestination = FWDefaultServletAction.ERROR_PAGE;
            }
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        FWAction _monAction = FWAction.newInstance(request.getParameter("userAction"));
        // String _myDestination = getRelativeURL();
        String _myDestination = getRelativeURL(request, session);
        try {
            if (_monAction.getActionPart().endsWith("rc2")) {
                // pas besoin de passer par le dispatcher dans cette action car
                // l'action "nouveau" appelé dans la page
                // de destination "rc_liste.jsp" passe par l'action "nouveau"
                // par défaut et gère donc les droits à ce moment
                FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(_monAction, dispatcher.getPrefix());
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
                HEInputAnnonceViewBean vBean = (HEInputAnnonceViewBean) viewBean;
                vBean.setSession((BSession) dispatcher.getSession());
                vBean.computeNeededFields(request.getParameter("motif"), true);
                session.setAttribute("viewBean", vBean);
                _myDestination += "_rcListe.jsp";
            }
            // else if (_monAction.getActionPart().endsWith("listall")) {
            // FWViewBeanInterface viewBean =
            // FWViewBeanActionFactory.newInstance(_monAction,
            // dispatcher.getPrefix());
            // globaz.globall.http.JSPUtils.setBeanProperties(request,
            // viewBean);
            // HEOutputAnnonceViewBean vBean = (HEOutputAnnonceViewBean)
            // viewBean;
            // vBean.setSession((BSession) dispatcher.getSession());
            // session.setAttribute("viewBean", vBean);
            // _myDestination += "_rcListe.jsp";
            // }
            else if (_monAction.getActionPart().endsWith("chooseType")) {
                FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(_monAction, dispatcher.getPrefix());
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
                HELotViewBean vBean = (HELotViewBean) viewBean;
                vBean.setSession((BSession) dispatcher.getSession());
                vBean.setIdLot(request.getParameter("selectedId"));
                vBean.setArchivage(!JadeStringUtil.isEmpty(request.getParameter("isArchivage")) ? Boolean.valueOf(
                        request.getParameter("isArchivage")).booleanValue() : false);
                vBean = (HELotViewBean) dispatcher.dispatch(vBean, _monAction);
                String type = vBean.getType();
                if (Arrays.asList(HELotViewBean.getLotEnvoi()).contains(type)) {
                    _myDestination = "/" + _monAction.getApplicationPart()
                            + "?userAction=hermes.parametrage.attenteEnvoi.chercher&forIdLot=" + vBean.getIdLot()
                            + "&isArchivage=" + vBean.isArchivage();
                } else if (Arrays.asList(HELotViewBean.getLotReception()).contains(type)) {
                    _myDestination = "/" + _monAction.getApplicationPart()
                            + "?userAction=hermes.parametrage.attenteReception.chercher&forIdLot=" + vBean.getIdLot()
                            + "&isArchivage=" + vBean.isArchivage();
                }
            } else if (_monAction.getActionPart().endsWith("saisirCI")) {
                session.removeAttribute("EcrituresCISaisies");
                // récupérer l'annonce attendue de base
                HERassemblementViewBean annonceDepart = new HERassemblementViewBean();
                annonceDepart.setSession((BSession) dispatcher.getSession());
                annonceDepart.setIdAttenteRetour(request.getParameter("selectedId"));
                // passe dans le dispatcher pour vérifier les droits avant de
                // faire un retrieve qui ne devrait théoriquement pas être fait
                // dans l'action
                annonceDepart = (HERassemblementViewBean) dispatcher.dispatch(annonceDepart, _monAction);
                annonceDepart.retrieve();
                session.setAttribute("viewBean", annonceDepart);
                // rechercher l'annnonce 2% de départ en relation avec l'annonce
                // à générer
                String etatNominatif = "";
                HEOutputAnnonceListViewBean listAnnonces = new HEOutputAnnonceListViewBean(
                        (BSession) dispatcher.getSession());
                listAnnonces.setForVingtOnly(true);
                listAnnonces.setCodeApplication3839(false);
                listAnnonces.setForCodeEnregistrement("01");
                listAnnonces.setForRefUnique(annonceDepart.getReferenceUnique());
                listAnnonces.setForNumeroAVS(annonceDepart.getNumAVS());
                listAnnonces.wantCallMethodAfterFind(false);
                listAnnonces.wantCallMethodBefore(false);
                listAnnonces.wantCallMethodBeforeFind(false);
                listAnnonces.find();
                if (listAnnonces.getSize() != 0) {
                    // le 2% est trouvé
                    HEOutputAnnonceViewBean annonce = (HEOutputAnnonceViewBean) listAnnonces.getEntity(0);
                    etatNominatif = annonce.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF);
                }
                _myDestination += "_saisirCI.jsp?etatNominatif=" + etatNominatif;
            } else if (_monAction.getActionPart().endsWith("ajouterEcriture")) {
                // générer un nouveau 38 et l'ajouter à la liste en cours sauvée
                // dans la session
                // récupérer l'annonce attendue de base
                HERassemblementViewBean annonceDepart = (HERassemblementViewBean) session.getAttribute("viewBean");
                dispatcher.dispatch(annonceDepart, _monAction);
                // récupérer la liste en cours d'ajout (si il y en a une
                ArrayList listAnnonce38Saisie = new ArrayList();
                // java.util.ArrayList list = new java.util.ArrayList();
                Object ecritureCourantes = session.getAttribute("EcrituresCISaisies");
                if (ecritureCourantes != null) {
                    listAnnonce38Saisie = (ArrayList) ecritureCourantes;
                }
                // récupération des valeurs saisies
                String numAffilie = request.getParameter("partnerNum");
                String numPartenaire = request.getParameter("mitgliedNum");
                String detailPartouAff = request.getParameter("numeroDetailInv");
                String lieuEmployeur = request.getParameter("lieuEmployeur");
                String genre = request.getParameter("gre");
                String periodeDebut = request.getParameter("moisDebut");
                String periodeFin = request.getParameter("moisFin");
                String annee = request.getParameter("annee");
                String montant = JANumberFormatter.deQuote(request.getParameter("montant"));
                String code = request.getParameter("code");
                String codeSpecial = request.getParameter("codeSpecial");
                String brancheEco = request.getParameter("brancheEco");
                String etatNominatif = request.getParameter("nomPrenom");
                // création de l'annonce 38
                HEInputAnnonceViewBean nouveau38 = new HEInputAnnonceViewBean();
                // initialisation du champ enregistrement
                nouveau38.put(IHEAnnoncesViewBean.CODE_APPLICATION, "38");
                nouveau38.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT,
                        StringUtils.formatCodeEnregistrement((listAnnonce38Saisie.size() % 999) + 1));
                nouveau38.put(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE, annonceDepart.getSession()
                        .getApplication().getProperty("noCaisse"));
                nouveau38.put(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE, annonceDepart.getSession()
                        .getApplication().getProperty("noAgence"));
                nouveau38.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, annonceDepart.getMotif());
                nouveau38.put(IHEAnnoncesViewBean.NUMERO_CAISSE__CI,
                        StringUtils.getCaisse(annonceDepart.getNumCaisse()));
                nouveau38
                        .put(IHEAnnoncesViewBean.NUMERO_AGENCE_CI, StringUtils.getAgence(annonceDepart.getNumCaisse()));
                nouveau38.put(IHEAnnoncesViewBean.NUMERO_ASSURE,
                        JadeStringUtil.removeChar(annonceDepart.getNumAVS(), '.'));
                nouveau38.put(IHEAnnoncesViewBean.CODE_1_OU_2, "1");
                if (StringUtils.isStringEmpty(numPartenaire)) {
                    nouveau38.put(IHEAnnoncesViewBean.NUMERO_AFILLIE, StringUtils.removeDots(numAffilie));
                } else {
                    nouveau38.put(IHEAnnoncesViewBean.NUMERO_AFILLIE, JadeStringUtil.removeChar(numPartenaire, '.'));
                }
                nouveau38.put(IHEAnnoncesViewBean.CHIFFRE_CLE_EXTOURNES, genre.substring(0, 1));
                nouveau38.put(IHEAnnoncesViewBean.CHIFFRE_CLE_GENRE_COTISATIONS, genre.substring(1, 2));
                nouveau38.put(IHEAnnoncesViewBean.CODE_SPECIAL, annonceDepart.getSession().getCode(codeSpecial));
                nouveau38.put(IHEAnnoncesViewBean.DUREE_COTISATIONS_DEBUT, periodeDebut);
                nouveau38.put(IHEAnnoncesViewBean.DUREE_COTISATIONS_FIN, periodeFin);
                nouveau38.put(IHEAnnoncesViewBean.ANNEE_COTISATIONS, annee.length() == 4 ? annee.substring(2) : "");
                nouveau38.put(IHEAnnoncesViewBean.REVENU, montant);
                nouveau38.put(IHEAnnoncesViewBean.CODE_A_D_S, annonceDepart.getSession().getCode(code));
                nouveau38.put(IHEAnnoncesViewBean.BRANCHE_ECONOMIQUE, brancheEco);
                nouveau38.put(IHEAnnoncesViewBean.ANNEES_COTISATIONS_AAAA, annee);
                nouveau38.setMotif(annonceDepart.getMotif());
                // nouveau38.setNumeroCaisse(annonceDepart.getNumCaisse());
                nouveau38.setNumeroAVS(annonceDepart.getNumAVS());
                nouveau38.setSession(annonceDepart.getSession());
                nouveau38.setIdProgramme("HERMES");
                nouveau38.setRefUnique(annonceDepart.getReferenceUnique());
                nouveau38.setTypeLot(HELotViewBean.CS_TYPE_RECEPTION);
                // ajout de l'annonce créée à la liste en session déjà saisie
                // par l'utilisateur
                listAnnonce38Saisie.add(nouveau38);
                if (!JadeStringUtil.isEmpty(numAffilie)) {
                    // générer un 38 - 2
                    nouveau38 = new HEInputAnnonceViewBean();
                    nouveau38.put(IHEAnnoncesViewBean.CODE_APPLICATION, "38");
                    nouveau38.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT,
                            StringUtils.formatCodeEnregistrement((listAnnonce38Saisie.size() % 999) + 1));
                    nouveau38.put(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE, annonceDepart.getSession()
                            .getApplication().getProperty("noCaisse"));
                    nouveau38.put(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE, annonceDepart.getSession()
                            .getApplication().getProperty("noAgence"));
                    nouveau38.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, annonceDepart.getMotif());
                    nouveau38.put(IHEAnnoncesViewBean.NUMERO_CAISSE__CI,
                            StringUtils.getCaisse(annonceDepart.getNumCaisse()));
                    nouveau38.put(IHEAnnoncesViewBean.NUMERO_AGENCE_CI,
                            StringUtils.getAgence(annonceDepart.getNumCaisse()));
                    nouveau38.put(IHEAnnoncesViewBean.NUMERO_ASSURE,
                            AVSUtils.formatAVS8Or9(annonceDepart.getNumAVS(), false, false));
                    nouveau38.put(IHEAnnoncesViewBean.CODE_1_OU_2, "2");
                    if (JadeStringUtil.isEmpty(numPartenaire)) {
                        nouveau38.put(IHEAnnoncesViewBean.NUMERO_AFILLIE, StringUtils.removeDots(numAffilie));
                    } else {
                        nouveau38.put(IHEAnnoncesViewBean.NUMERO_AFILLIE,
                                AVSUtils.formatAVS8Or9(numPartenaire, false, false));
                    }
                    // attention le champ "PARTIE_INFORMATION" ne doit pas
                    // excéder 42 caractères
                    StringBuffer infoAff = new StringBuffer(42);
                    infoAff.insert(0, JAUtil.padString(detailPartouAff, 42));
                    if (!JadeStringUtil.isEmpty(lieuEmployeur)) {
                        if (detailPartouAff.length() > 25) {
                            infoAff.replace(24, 42, JAUtil.padString("," + lieuEmployeur, 42));
                        } else {
                            infoAff.replace(25, 42, JAUtil.padString(lieuEmployeur, 42));
                        }
                    }
                    if (infoAff.length() > 42) {
                        nouveau38.put(IHEAnnoncesViewBean.PARTIE_INFORMATION, infoAff.substring(0, 42).toUpperCase());
                    } else {
                        nouveau38.put(IHEAnnoncesViewBean.PARTIE_INFORMATION, infoAff.toString().toUpperCase());
                    }
                    nouveau38.setSession(annonceDepart.getSession());
                    nouveau38.setMotif(annonceDepart.getMotif());
                    // nouveau38.setNumeroCaisse(annonceDepart.getNumCaisse());
                    nouveau38.setNumeroAVS(annonceDepart.getNumAVS());
                    nouveau38.setIdProgramme("HERMES");
                    nouveau38.setTypeLot(HELotViewBean.CS_TYPE_RECEPTION);
                    listAnnonce38Saisie.add(nouveau38);
                }
                // mémoriser la nouvelle liste de 38 saisie par l'utlisateur
                session.setAttribute("EcrituresCISaisies", listAnnonce38Saisie);
                // *****************************************************************************
                _myDestination += "_saisirCI.jsp?etatNominatif=" + etatNominatif;
            } else if (_monAction.getActionPart().endsWith("getzas")) {
                _myDestination = getRelativeURLwithoutClassPart(request, session) + "getzas_de.jsp";
            }
        } catch (Exception e) {
            e.printStackTrace();
            _myDestination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    /**
	 *
	 *
	 *
	 */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
        if (_action.getClassPart().equals("extraitAnnonce")) {
            try {
                FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
                // viewBean = mainDispatcher.dispatch(viewBean, _action);
                HEImpressionciViewBean vBean = (HEImpressionciViewBean) viewBean;
                HEExtraitAnnonceProcess process = new HEExtraitAnnonceProcess();
                process.setSession(vBean.getSession());
                process.setReferenceUnique(vBean);
                process.setEMailAddress(request.getParameter("email"));
                if (!JadeStringUtil.isEmpty(request.getParameter("caisse"))) {
                    process.setForNumCaisse(request.getParameter("caisse"));
                }
                process.setManuelPrint(true);
                process.setIsArchivage(JadeStringUtil.isEmpty(request.getParameter("isArchivage")) ? false : Boolean
                        .valueOf(request.getParameter("isArchivage")).booleanValue());
                BSession userSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
                process.setUserId(userSession.getUserId());
                process = (HEExtraitAnnonceProcess) mainDispatcher.dispatch(process,
                        FWAction.newInstance(request.getParameter("userAction")));
                if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                    viewBean.setMsgType(process.getMsgType());
                    viewBean.setMessage(process.getMessage());
                }
                request.setAttribute("viewBean", viewBean);
                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                    _destination = getRelativeURLwithoutClassPart(request, session) + "impressionci_de.jsp?_valid=fail";
                } else {
                    // _destination = getActionBack();
                    _destination = getActionBack();
                }
            } catch (Exception e) {
                e.printStackTrace();
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }
        } else if (_action.getClassPart().equals("rassemblement")) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
            if ((!JadeStringUtil.isEmpty(request.getParameter("forReferenceUnique")))
                    && !request.getParameter("forReferenceUnique").equals("-1")) {
                BSession userSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
                try {
                    HEExtraitAnnonceProcess process = new HEExtraitAnnonceProcess();
                    process.setSession(userSession);
                    process.addReferenceUnique(request.getParameter("forReferenceUnique"));
                    process.setEMailAddress(request.getParameter("email"));
                    process.setLangue(request.getParameter("langue"));
                    process.setLangueFromEcran(request.getParameter("langue"));
                    process.setFromDate(request.getParameter("fromDate"));
                    process.setUntilDate(request.getParameter("untilDate"));
                    process.setIsArchivage(JadeStringUtil.isEmpty(request.getParameter("isArchivage")) ? false
                            : Boolean.valueOf(request.getParameter("isArchivage")).booleanValue());
                    process.setUserId(userSession.getUserId());
                    process.setManuelPrint(true);
                    process = (HEExtraitAnnonceProcess) mainDispatcher.dispatch(process,
                            FWAction.newInstance(request.getParameter("userAction")));
                } catch (Exception e) {
                    JadeLogger.error(
                            ((BSession) mainDispatcher.getSession()).getLabel("HERMES_HEACTIONSGESTIONS_ERROR"), e);
                }
            } else {
                request.setAttribute("afficheMsg", "true");
            }
        } else if (_action.getClassPart().equals("genererCIPapier")) {
            try {
                HERassemblementViewBean rcBean = (HERassemblementViewBean) session.getAttribute("viewBean");
                ArrayList listAnnonce38Saisie = new ArrayList();
                Object ecritureCourantes = session.getAttribute("EcrituresCISaisies");
                if (ecritureCourantes != null) {
                    listAnnonce38Saisie = (ArrayList) ecritureCourantes;
                }
                HEGenererCIPapier genererCi = new HEGenererCIPapier();
                genererCi.setSession(rcBean.getSession());
                genererCi.setIdAttenteRetour(rcBean.getIdAttenteRetour());
                genererCi.setListAnnonce38Saisie(listAnnonce38Saisie);
                genererCi.setEtatNominatif(request.getParameter("nomPrenom"));
                genererCi.setSendCompletionMail(false);
                genererCi = (HEGenererCIPapier) mainDispatcher.dispatch(genererCi,
                        FWAction.newInstance(request.getParameter("userAction")));
                if (rcBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                    _destination = getRelativeURLwithoutClassPart(request, session)
                            + "rassemblement_saisirCI.jsp?_valid=fail";
                } else {
                    // _destination = getRelativeURLwithoutClassPart(request,
                    // session) +
                    // "rassemblement_rc.jsp";//"hermes?userAction=showProcess";
                    _destination = "/" + _action.getApplicationPart()
                            + "?userAction=hermes.gestion.rassemblement.chercher";
                }
            } catch (Exception e) {
                e.printStackTrace();
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }
        } else if (_action.getClassPart().equals("lot")) {
            try {
                HELotViewBean viewBean = (HELotViewBean) FWViewBeanActionFactory.newInstance(_action,
                        mainDispatcher.getPrefix());
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
                if (HELotViewBean.CS_TYPE_RECEPTION.equals(viewBean.getType())) {
                    if ("true".equals(request.getParameter("ImprimerExtraitCiTermine"))) {
                        HEExtraitTermine extrait = new HEExtraitTermine();
                        extrait.setSession((BSession) mainDispatcher.getSession());
                        extrait.setIdLot(viewBean.getIdLot());
                        extrait.setEmail(request.getParameter("email"));
                        extrait.setArchivage(viewBean.isArchivage());
                        extrait.setSendCompletionMail(true);
                        if (!JadeStringUtil.isBlank(request.getParameter("referenceName"))) {
                            extrait.setReferenceInterne(request.getParameter("referenceName"));
                        }
                        mainDispatcher.dispatch(extrait, _action);
                    }
                    if ("true".equals(request.getParameter("ImprimerCIAdditionnels"))) {
                        HEExtraitCIAdditionnel additionnel = new HEExtraitCIAdditionnel();
                        additionnel.setSession((BSession) mainDispatcher.getSession());
                        additionnel.setIdLot(viewBean.getIdLot());
                        additionnel.setArchivage(viewBean.isArchivage());
                        additionnel.setEmail(request.getParameter("email"));
                        additionnel.setSendCompletionMail(true);
                        mainDispatcher.dispatch(additionnel, _action);

                    }
                    if ("true".equals(request.getParameter("ImprimerLettreAffilies"))) {
                        /* Génération lettres pour les CA et les CI */
                        HELettreAffilieCA_Doc impressionCA = new HELettreAffilieCA_Doc();
                        impressionCA.setSession((BSession) mainDispatcher.getSession());
                        impressionCA.setIdLot(viewBean.getIdLot());
                        impressionCA.setSendCompletionMail(true);
                        impressionCA.setEMailAddress(request.getParameter("email"));
                        impressionCA.setArchivage(viewBean.isArchivage());
                        mainDispatcher.dispatch(impressionCA, _action);
                    }
                    if ("true".equals(request.getParameter("ImprimerListeAnnonceRecue"))) {
                        // generer le document
                        HEDocumentZas doc = new HEDocumentZas();
                        doc.setSession((BSession) mainDispatcher.getSession());
                        doc.setEMailAddress(request.getParameter("email"));
                        doc.setIdLot(viewBean.getIdLot());
                        doc.setArchivage(viewBean.isArchivage());
                        doc.setCaOnly("false");
                        if (!JadeStringUtil.isBlank(request.getParameter("referenceName"))) {
                            doc.setForService(request.getParameter("referenceName"));
                        }
                        mainDispatcher.dispatch(doc, _action);
                    }
                    if ("true".equals(request.getParameter("caonly"))) {
                        HEDocumentZas docWithCA = new HEDocumentZas();
                        docWithCA.setSession((BSession) mainDispatcher.getSession());
                        docWithCA.setEMailAddress(request.getParameter("email"));
                        docWithCA.setIdLot(viewBean.getIdLot());
                        docWithCA.setArchivage(viewBean.isArchivage());
                        docWithCA.setCaOnly("true");
                        if (!JadeStringUtil.isBlank(request.getParameter("referenceName"))) {
                            docWithCA.setForService(request.getParameter("referenceName"));
                        }
                        mainDispatcher.dispatch(docWithCA, _action);
                    }
                }
                if ("true".equals(request.getParameter("ImprimerRemiseCertifCA"))) {
                    HEDocumentRemiseCertifCA docRemiseCa = new HEDocumentRemiseCertifCA();
                    docRemiseCa.setSession((BSession) mainDispatcher.getSession());
                    docRemiseCa.setEMailAddress(request.getParameter("email"));
                    docRemiseCa.setDocumentTitle("Certificat");
                    docRemiseCa.setIdLot(viewBean.getIdLot());
                    if (!JadeStringUtil.isBlank(request.getParameter("referenceName"))) {
                        docRemiseCa.setService(request.getParameter("referenceName"));
                    }
                    // docRemiseCa.setArchivage(viewBean.isArchivage());
                    // docRemiseCa.setCaOnly("true");
                    mainDispatcher.dispatch(docRemiseCa, _action);
                }

                if ("true".equals(request.getParameter("ImprimerRemiseAttestCA"))) {
                    HEDocumentRemiseAttestCA docRemiseCa = new HEDocumentRemiseAttestCA();
                    docRemiseCa.setSession((BSession) mainDispatcher.getSession());
                    docRemiseCa.setEMailAddress(request.getParameter("email"));
                    docRemiseCa.setDocumentTitle("Attestation");
                    docRemiseCa.setIdLot(viewBean.getIdLot());
                    if (!JadeStringUtil.isBlank(request.getParameter("referenceName"))) {
                        docRemiseCa.setService(request.getParameter("referenceName"));
                    }
                    // docRemiseCa.setArchivage(viewBean.isArchivage());
                    // docRemiseCa.setCaOnly("true");
                    mainDispatcher.dispatch(docRemiseCa, _action);
                }

                if ("true".equals(request.getParameter("ImprimerIrrecouvrable"))) {
                    HEListIrrecouvrable doc = new HEListIrrecouvrable((BSession) mainDispatcher.getSession(),
                            viewBean.getIdLot());
                    doc.setEMailAddress(request.getParameter("email"));
                    doc.setDocumentTitle(((BSession) mainDispatcher.getSession()).getLabel("TITRE_LISTE"));
                    doc.setFilenameRoot("Liste_Irrecouvrable");
                    mainDispatcher.dispatch(doc, _action);
                }

                if (HELotViewBean.CS_TYPE_AVIS_DECES.equals(viewBean.getType())) {
                    HEAvisDeces_Doc doc = new HEAvisDeces_Doc();
                    doc.setSession((BSession) mainDispatcher.getSession());
                    doc.setEMailAddress(request.getParameter("email"));
                    doc.setIdLot(viewBean.getIdLot());
                    doc.setArchivage(viewBean.isArchivage());
                    mainDispatcher.dispatch(doc, _action);
                }
                request.removeAttribute("userAction");
                request.removeAttribute("idLot");
                _destination = "/" + _action.getApplicationPart() + "?userAction=hermes.gestion.lot.chercher";
            } catch (Exception e) {
                e.printStackTrace();
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }
        } else if (_action.getClassPart().equals("zas")) {
            _destination = getRelativeURLwithoutClassPart(request, session) + "getzas_de.jsp";
        } else {
            _destination = getActionBack();
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    // ALD : surcharge cette méthode car dans Hermes, il y a 2 paramètres pour
    // la référence interne
    @Override
    protected String addParametersFrom(HttpServletRequest request, String url) {
        String result = url;
        Enumeration enum1 = request.getParameterNames();
        char separator = result.indexOf('?') == -1 ? '?' : '&';
        while (enum1.hasMoreElements()) {
            String paramName = (String) enum1.nextElement();
            boolean add = !"userAction".equals(paramName);
            if ("partialTOSTR118007".equals(paramName) && "INSERTOK".equals(request.getParameter("actionMessage"))) {
                add = false;
            }
            if (add) {
                String[] p = request.getParameterValues(paramName);
                for (int i = 0; i < p.length; i++) {
                    result += separator + paramName + "=" + URLEncoder.encode(p[i]);
                    separator = '&';
                }
            }
        }
        return result;
    }

    /**
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    // Attention : le dispatcher ne doit pas être appelé dans cette méthode. La
    // gestion des droits se fait sur l'action "afficher" par la suite
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String actionSuite = getActionSuite(request);
        if (actionSuite.equals("inputAnnonce")) {
            HEInputAnnonceViewBean vBean = (HEInputAnnonceViewBean) viewBean;
            vBean.setInputMotif(request.getParameter("motif"));
            vBean.setInputCritere(request.getParameter("critere"));
            vBean.setParamNumeroAvs(request.getParameter("numeroAvs"));
            vBean.setParamReferenceExterne(request.getParameter("referenceExterne"));
            vBean.setActionMessage(request.getParameter("actionMessage"));
            vBean.setIdDernierAjout(request.getParameter("idDernierAjout"));
            vBean.setWarningEmployeurSansPersoOrAccountZero((String) request.getAttribute("warningEmployeurSansPerso"));
            return vBean;
        }
        if (actionSuite.equals("impressionci")) {
            HEImpressionciViewBean vBean = (HEImpressionciViewBean) viewBean;
            vBean.setIdAnnonce(request.getParameter("selectedId"));
            if (!JadeStringUtil.isEmpty(request.getParameter("caisse"))) {
                vBean.setForNumeroCaisse(request.getParameter("caisse"));
            }
            vBean.setArchivage(request.getParameter("isArchivage") != null ? Boolean.valueOf(
                    request.getParameter("isArchivage")).booleanValue() : false);
            return vBean;
        }
        if (actionSuite.equals("lot")) {
            ((HELotViewBean) viewBean)
                    .setArchivage(!JadeStringUtil.isEmpty(request.getParameter("isArchivage")) ? Boolean.valueOf(
                            request.getParameter("isArchivage")).booleanValue() : false);
        }
        return viewBean;
    }

    /**
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    // Attention : le dispatcher ne doit pas être appelé dans cette méthode. La
    // gestion des droits se fait sur l'action "ajouter" par la suite
    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String userAction = getActionSuite(request);
        if (userAction.equals("inputAnnonce")) {
            String s[] = request.getParameterValues("paramannonce");
            HEInputAnnonceViewBean inputAnnonce = (HEInputAnnonceViewBean) viewBean;
            inputAnnonce.setToutParams(s);
            if (request.getParameter("numeroAffilie") != null) {
                inputAnnonce.setNumeroAffilie(request.getParameter("numeroAffilie"));
            }
            if (request.getParameter("numeroSuccursale") != null) {
                inputAnnonce.setNumeroSuccursale(request.getParameter("numeroSuccursale"));
            }
            if (request.getParameter("numeroEmploye") != null) {
                inputAnnonce.setNumeroEmploye(request.getParameter("numeroEmploye"));
            }
            for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
                String param = (String) e.nextElement();
                // pour corriger bug lié au CT calendar qui gère
                // pas les attributs name que en numérique
                if (param.startsWith("TOSTR")) {
                    inputAnnonce.put(param.substring(5, param.length()), request.getParameter(param));
                    // modification NNSS
                    inputAnnonce.put(param.substring(5, param.length()) + HENNSSUtils.PARAM_NNSS,
                            request.getParameter(param + HENNSSUtils.PARAM_NNSS));
                } else if (param.startsWith("118")) {
                    if (HEAnnoncesViewBean.isNumeroAVS(param)) {
                        inputAnnonce.put(param, StringUtils.removeDots(request.getParameter(param)));
                    } else if (HEAnnoncesViewBean.isCustomField(param)) {
                        String[] pValues = request.getParameterValues(param);
                        String value = "";
                        for (int i = 0; i < pValues.length; i++) {
                            value += pValues[i];
                        }
                        if (value.trim().length() > 2) {
                            inputAnnonce.put(param, value);
                        }
                    } else if (HEAnnoncesViewBean.isReferenceInterne(param)) {
                        String[] pValues = request.getParameterValues(param);
                        String value = "";
                        for (int i = 0; i < pValues.length; i++) {
                            value += pValues[i] + "/";
                        }
                        if (value.replace('/', ' ').trim().length() == 0) { // champ
                            // vide
                            inputAnnonce.put(param, "");
                        } else {
                            inputAnnonce.put(param, value.substring(0, value.length() - 1));
                        }
                    } else if (HEAnnoncesViewBean.isCountryField(param)) {
                        String value = request.getParameter(param);
                        // on contrôle que si le code pays est vide, on charge
                        // le code sélectionné dans la liste
                        if (JadeStringUtil.isEmpty(value)) {
                            value = request.getParameter("TOLST" + param);
                        }
                        inputAnnonce.put(param, value);
                    } else {
                        inputAnnonce.put(param, request.getParameter(param));
                    }
                }
            }
            return inputAnnonce;
        }
        return viewBean;
    }

    /**
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    // Attention : le dispatcher ne doit pas être appelé dans cette méthode. La
    // gestion des droits se fait sur l'action "lister" par la suite
    @Override
    public final FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String actionSuite = getActionSuite(request);
        if (actionSuite.equals("retourAnnonce")) {
            HEAttenteRetourListViewBean vBean = (HEAttenteRetourListViewBean) viewBean;
            return vBean;
        } else if (actionSuite.equals("lot")) {
            HELotListViewBean vBean = (HELotListViewBean) viewBean;
            String triLot = request.getParameter("triLot");
            String typeLot = request.getParameter("typeLot");
            String from = request.getParameter("from");
            if (!JadeStringUtil.isEmpty(typeLot)) {
                vBean.setForType(typeLot);
            }
            if (triLot.equals("date")) { // tri par date
                vBean.setOrder("RMDDEN DESC, RMDHEU");
                vBean.setForDateEnvoi(request.getParameter("von"));
            } else if (triLot.equals("lot")) { // tri par type de lot
                vBean.setOrder("RMTTYP");
            } else if (triLot.equals("id")) { // tri par ID
                vBean.setOrder("RMILOT");
                vBean.setFromIdLot(from);
            }
            return vBean;
        }
        return viewBean;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.10.2002 15:27:52)
     * 
     * @return java.lang.String
     * @param param
     *            javax.servlet.http.HttpServletRequest
     */
    public String getActionSuite(HttpServletRequest request) {
        return split(request.getParameter("userAction"), 2);
    }

    /* Découpe et retourne une chaîne de caractère compris entre un élément */
    public String split(String str, int pos) {
        Vector tmp = new Vector();
        try {
            StringTokenizer st = new StringTokenizer(str, ".");
            while (st.hasMoreTokens()) {
                tmp.add(st.nextToken());
            }
            return (String) tmp.elementAt(pos);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
