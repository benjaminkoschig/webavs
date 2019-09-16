package globaz.corvus.servlet;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.utils.REMappingSaisieDemandeRente;
import globaz.corvus.vb.demandes.REDemandeRenteViewBean;
import globaz.corvus.vb.demandes.REInfoComplViewBean;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.corvus.vb.demandes.REPeriodeAPIListViewBean;
import globaz.corvus.vb.demandes.REPeriodeAPIViewBean;
import globaz.corvus.vb.demandes.REPeriodeInvaliditeListViewBean;
import globaz.corvus.vb.demandes.REPeriodeInvaliditeViewBean;
import globaz.corvus.vb.demandes.RESaisieDemandeRenteViewBean;
import globaz.corvus.vb.process.REGenererTransfertDossierViewBean;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAVector;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.ISFUrlEncode;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe action gérant toute la saisie de la demande de rente
 * 
 * @author HPE
 */
public class RESaisieDemandeRenteAction extends PRDefaultAction {

    protected static final String VERS_ECRAN_DE = "_de.jsp";

    /**
     * Extrait le NSS à partir d'un possible NSS-DTO contenu dans la session spécifiée en paramètre
     * 
     * @param session
     *            la session considérée
     * @return la chaîne de caractère formattée du NSS, ou null si l'opération a échouée
     */
    private static String getFormatedDtoNss(HttpSession session) {
        RENSSDTO nssDto = (RENSSDTO) PRSessionDataContainerHelper.getData(session,
                PRSessionDataContainerHelper.KEY_NSS_DTO);

        if (null != nssDto) {
            String nss = nssDto.getNSS();

            if (RESaisieDemandeRenteAction.isValidNss(nss)) {
                return NSUtil.formatAVSNewNum(nss);
            }
        }

        return null;
    }

    /**
     * Extrait l'ID de la demande de rente stocké dans la requête spécifiée en paramètre, ceci selon la priorité
     * suivante:
     * <ol>
     * <li>le paramètre "selectedId";</li>
     * <li>le paramètre "selectedidDemandeRente";</li>
     * </ol>
     * 
     * @param request
     *            la requête considérée
     * @return la chaîne de caractères contenant l'ID de la demande, ou une chaîne vide si non trouvé
     */
    private static String getIdDemandeRenteFromRequest(HttpServletRequest request) {
        String output = request.getParameter("selectedId");
        if (JadeStringUtil.isBlankOrZero(output)) {
            output = request.getParameter("idDemandeRente");
        }

        return output;
    }

    /**
     * Retourne si la requête en paramètre spécifie que le NSS-DTO doit être ignoré (typiquement dans le cas d'une
     * nouvelle demande "from scratch"), ou non.
     * 
     * @param request
     *            La requête considérée
     * @return vrai si la condition est remplie, faux sinon
     */
    private static boolean isDtoNssIgnored(HttpServletRequest request) {
        return "true".equalsIgnoreCase(request.getParameter("fromScratch"));
    }

    /**
     * Retourne si le texte donné en paramètre correspond à un NSS valide
     * 
     * @param txt
     *            La chaîne de caractères contenant le NSS à valider
     * @return vrai si la chaîne de caractère correspond à un NSS valide, faux sinon
     */
    private static boolean isValidNss(String txt) {
        return (txt.startsWith("756") && (txt.length() == 13));
    }

    /**
     * Supprime le DTO contenant un NSS de la session spécifiée en paramètre
     * 
     * @param session
     *            la session considérée
     */
    private static void removeNssDto(HttpSession session) {
        PRSessionDataContainerHelper.removeData(session, PRSessionDataContainerHelper.KEY_NSS_DTO);
    }

    private String _destination = "";

    public RESaisieDemandeRenteAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        return this.getUserActionURL(request, IREActions.ACTION_SAISIE_DEMANDE_RENTE, FWAction.ACTION_REAFFICHER);
    }

    protected String _getDestAjouterEchecInfoComp(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        return this.getUserActionURL(request, IREActions.ACTION_SAISIE_DEMANDE_RENTE,
                "afficherInformationsComplementaires");

    }

    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean, FWDispatcher mainDispatcher) {

        RESaisieDemandeRenteViewBean vb = (RESaisieDemandeRenteViewBean) viewBean;

        if (vb.isPassageHera()) {

            String idTiers = vb.getIdRequerant();

            if (JadeStringUtil.isBlankOrZero(idTiers)) {
                idTiers = vb.getIdTiers();
            }

            String urlRetour = null;

            // Pour les API redirection vers la page de calcul
            // Autrement redirection vers la page des demandes RCI
            if (!IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(vb.getCsTypeDemandeRente())) {

                urlRetour = ISFUrlEncode.encodeUrl("/corvus?userAction=" + IREActions.ACTION_DEMANDES_DE_RASSEMBLEMENT
                        + "." + FWAction.ACTION_AFFICHER + "&selectedId=" + vb.getIdDemandeRente() + "&idTiers="
                        + idTiers + "&csTypeDemande=" + vb.getCsTypeDemandeRente() + "&requerantDescription="
                        + vb.getNssRequerant() + " / " + vb.getNomRequerant() + " " + vb.getPrenomRequerant() + " / "
                        + vb.getDateNaissanceRequerant());
            } else {

                urlRetour = ISFUrlEncode.encodeUrl("/corvus?userAction=" + IREActions.ACTION_CALCUL_DEMANDE_RENTE + "."
                        + FWAction.ACTION_AFFICHER + "&selectedId=" + vb.getIdDemandeRente() + "&idTiers=" + idTiers
                        + "&csTypeDemande=" + vb.getCsTypeDemandeRente() + "&requerantDescription="
                        + vb.getNssRequerant() + " / " + vb.getNomRequerant() + " " + vb.getPrenomRequerant() + " / "
                        + vb.getDateNaissanceRequerant());

            }

            String idDemandeRente = vb.getIdDemandeRente();
            String csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
            try {
                if (!JadeStringUtil.isBlankOrZero(idDemandeRente)) {
                    REDemandeRente dem = new REDemandeRente();
                    dem.setSession((BSession) mainDispatcher.getSession());
                    dem.setIdDemandeRente(idDemandeRente);
                    dem.retrieve();
                    if (IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL.equals(dem.getCsTypeCalcul())) {
                        csDomaine = ISFSituationFamiliale.CS_DOMAINE_CALCUL_PREVISIONNEL;
                    }

                }
            } catch (Exception e) {
                csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
            }

            return "/hera?userAction=hera.famille.apercuRelationConjoint.entrerApplication&csDomaine=" + csDomaine
                    + "&idTiers=" + idTiers + "&urlFrom=" + urlRetour;

        } else {

            // Si on passe pas dans HERA, il faut quand même créer une situation
            // familiale pour le requérant

            try {

                ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(
                        (BSession) mainDispatcher.getSession(), ISFSituationFamiliale.CS_DOMAINE_STANDARD,
                        vb.getIdRequerant());
                ISFMembreFamille membre = sf.addMembreCelibataire(vb.getIdRequerant());

                // vérifier que le membre a été créé
                if (JadeStringUtil.isEmpty(membre.getIdTiers())) {
                    throw new Exception("Membre non crée dans la situation familiale");
                }

            } catch (Exception e) {
                JadeLogger.error("Erreur dans la création de la situation familiale", e);
            }

            String idTiers = vb.getIdRequerant();

            if (JadeStringUtil.isBlankOrZero(idTiers)) {
                idTiers = vb.getIdTiers();
            }

            // Pour les API redirection vers la page de calcul
            // Autrement redirection vers la page des demandes RCI
            if (!IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(vb.getCsTypeDemandeRente())) {
                return this.getUserActionURL(request, IREActions.ACTION_DEMANDES_DE_RASSEMBLEMENT,
                        FWAction.ACTION_AFFICHER)
                        + "&selectedId="
                        + vb.getIdDemandeRente()
                        + "&idTiers="
                        + idTiers
                        + "&csTypeDemande=" + vb.getCsTypeDemandeRente();
            } else {
                return this.getUserActionURL(request, IREActions.ACTION_CALCUL_DEMANDE_RENTE, FWAction.ACTION_AFFICHER)
                        + "&selectedId=" + vb.getIdDemandeRente() + "&idTiers=" + idTiers + "&csTypeDemande="
                        + vb.getCsTypeDemandeRente();
            }
        }
    }

    protected String _getDestAjouterSuccesArret(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        return this.getUserActionURL(request, IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE,
                FWAction.ACTION_CHERCHER);
    }

    protected String _getDestAjouterSuccesInfoComp(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        return this.getUserActionURL(request, IREActions.ACTION_SAISIE_DEMANDE_RENTE, "afficherInformationsComplementaires");

    }

    /**
     * Cette méthode permet d'afficher la saisie de la demande de rente. En regardant si un ID existe, soit elle charge
     * le viewBean par rapport à la rente dans la base ou alors elle crée un viewBean vide. Ensuite, on charge le
     * requérant pour insérer les données dans le viewBean et on regarde aussi s'il existe des périodes API ou
     * Invalidité pour ces demandes, si oui, on les charge directement dans la liste prévue dans le viewBean. Ensuite,
     * on set également les informations complémentaires si elles existent.
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "";

        try {

            String selectedId = request.getParameter("selectedId");
            if (JadeStringUtil.isBlankOrZero(selectedId)) {
                selectedId = request.getParameter("idDemandeRente");
            }

            RESaisieDemandeRenteViewBean viewBean = null;

            // Si aucun ID de demande rente n'est défini, il s'agit d'une nouvelle demande
            if (JadeStringUtil.isEmpty(selectedId)) {

                viewBean = new RESaisieDemandeRenteViewBean();
                viewBean.setSession((BSession) mainDispatcher.getSession());
                getAction().changeActionPart(FWAction.ACTION_NOUVEAU);

                // Si l'argument fromScratch=true est présent dans la requête,
                // le NSS-DTO est supprimé. Ce dispositif mis en oeuvre dans
                // le cadre du BZ 8251, permet de créer une nouvelle demande
                // sans NSS prédéfini (champ NSS vide dans l'écran de recherche).
                // Il est alors nécessaire d'écraser le NSS-DTO car, il
                // n'est pas possible de l'écraser en cliquant sur le bouton
                // "Effacer" de l'écran de recherche
                if (RESaisieDemandeRenteAction.isDtoNssIgnored(request)) {
                    RESaisieDemandeRenteAction.removeNssDto(session);
                }

                // Si un NSS valide est trouvé dans une DTO dédié
                String nss = RESaisieDemandeRenteAction.getFormatedDtoNss(session);

                if (nss != null) {
                    viewBean.setNssRequerant(nss);
                }

            } else {
                viewBean = doBuildDemande((BSession) mainDispatcher.getSession(), viewBean, selectedId);
            }

            this.saveViewBean(viewBean, session);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = getRelativeURL(request, session) + "_de.jsp?selectedId=" + selectedId;
            } else {
                destination = FWDefaultServletAction.ERROR_PAGE;
            }

            // On mémorise le NSS dans la session
            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(viewBean.getNssRequerant());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    /**
     * Action qui permet de supprimer un période API dans la liste du viewBean de la base si nécessaire
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     * @return la destination à atteindre
     */
    public String actionAfficherPeriodeAPI(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        RESaisieDemandeRenteViewBean vb = (RESaisieDemandeRenteViewBean) viewBean;

        vb.setIdProvisoirePeriodeAPI(new Long(getSelectedId(request)));
        vb = (RESaisieDemandeRenteViewBean) mainDispatcher.dispatch(vb, getAction());

        String destination = getRelativeURL(request, session) + RESaisieDemandeRenteAction.VERS_ECRAN_DE + "?";

        if (JadeStringUtil.isIntegerEmpty((vb).getIdDemandeRente())) {

            destination = destination + PRDefaultAction.METHOD_ADD;

        } else {

            destination = destination + PRDefaultAction.METHOD_UPD;

        }

        return destination;
    }

    /**
     * Action qui permet de supprimer un période Invalidité dans la liste du viewBean de la base si nécessaire
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     * @return la destination à atteindre
     */
    public String actionAfficherPeriodeINV(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        RESaisieDemandeRenteViewBean vb = (RESaisieDemandeRenteViewBean) viewBean;

        vb.setIdProvisoirePeriodeINV(new Long(getSelectedId(request)));
        vb = (RESaisieDemandeRenteViewBean) mainDispatcher.dispatch(vb, getAction());

        String destination = getRelativeURL(request, session) + RESaisieDemandeRenteAction.VERS_ECRAN_DE + "?";

        if (JadeStringUtil.isIntegerEmpty((vb).getIdDemandeRente())) {

            destination = destination + PRDefaultAction.METHOD_ADD;

        } else {

            destination = destination + PRDefaultAction.METHOD_UPD;

        }

        return destination;

    }

    /**
     * Ajoute une période API dans la liste du viewBean (car tout est stocké dans le viewBean avant le clic sur suivant
     * ou arrêt)
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     * @return la destination à atteindre
     */
    public String actionAjouterPeriodeAPI(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        mainDispatcher.dispatch(viewBean, getAction());
        String destination = getRelativeURL(request, session) + RESaisieDemandeRenteAction.VERS_ECRAN_DE + "?";
        ((RESaisieDemandeRenteViewBean) viewBean).setModifie(true);

        if (JadeStringUtil.isIntegerEmpty(((RESaisieDemandeRenteViewBean) viewBean).getIdDemandeRente())) {
            destination = destination + PRDefaultAction.METHOD_ADD;
        } else {
            destination = destination + PRDefaultAction.METHOD_UPD;
        }

        return destination;
    }

    /**
     * Ajoute une période Invalidité dans la liste du viewBean (car tout est stocké dans le viewBean avant le clic sur
     * suivant ou arrêt)
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     * @return la destination à atteindre
     */
    public String actionAjouterPeriodeINV(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        mainDispatcher.dispatch(viewBean, getAction());
        String destination = getRelativeURL(request, session) + RESaisieDemandeRenteAction.VERS_ECRAN_DE + "?";
        ((RESaisieDemandeRenteViewBean) viewBean).setModifie(true);

        if (JadeStringUtil.isIntegerEmpty(((RESaisieDemandeRenteViewBean) viewBean).getIdDemandeRente())) {
            destination = destination + PRDefaultAction.METHOD_ADD;
        } else {
            destination = destination + PRDefaultAction.METHOD_UPD;
        }
        return destination;
    }

    /**
     * Ajoute une période d'invalidité dans la liste du viewBean (car tout est stocké dans le viewBean avant le clic sur
     * suivant ou arrêt). On fait directement un appel sur la méthode actionAjouterPeriodeINV() qui est utilisée pour
     * l'ajout de période depuis l'écran saisieDemandeRente_de.jsp Pourquoi cette indirection ? 1 -> 2 -> Parce qu'il
     * est probable qu'il y ai du job spécifique à faire dans notre cas, si c'est avéré, il faudra juste modifié le code
     * de cette méthode sans changer l'appel dans la jsp
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String actionAjouterPeriodeINVPrestationTransitoire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        mainDispatcher.dispatch(viewBean, getAction());
        String destination = getRelativeURL(request, session) + RESaisieDemandeRenteAction.VERS_ECRAN_DE + "?";
        ((RESaisieDemandeRenteViewBean) viewBean).setModifie(true);

        if (JadeStringUtil.isIntegerEmpty(((RESaisieDemandeRenteViewBean) viewBean).getIdDemandeRente())) {
            destination = destination + PRDefaultAction.METHOD_ADD;
        } else {
            destination = destination + PRDefaultAction.METHOD_UPD;
        }
        return "/corvusRoot/demandes/saisieDemandePrestationTransitoire_de.jsp?_method=upd";
    }

    /**
     * Action qui permet de lister les périodes API sur la page de saisie de demande de rente
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            vb
     */
    public void actionListerPeriodesAPI(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws ServletException, IOException {

        String _destination = "";
        try {

            REPeriodeAPIListViewBean viewBean = new REPeriodeAPIListViewBean();
            RESaisieDemandeRenteViewBean vbRente = (RESaisieDemandeRenteViewBean) vb;

            JAVector vectorMan = new JAVector();
            vectorMan.addAll(vbRente.getPeriodesAPI());
            viewBean.setContainer(vectorMan);

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            request.setAttribute("viewBean", viewBean);
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);
            _destination = "/corvusRoot/demandes/periodeAPI_rcListe.jsp";

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action qui permet de lister les périodes Invalidités sur la page de saisie de demande de rente
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            vb
     */
    public void actionListerPeriodesINV(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws ServletException, IOException {

        String _destination = "";

        try {
            REPeriodeInvaliditeListViewBean viewBean = new REPeriodeInvaliditeListViewBean();
            RESaisieDemandeRenteViewBean vbRente = (RESaisieDemandeRenteViewBean) vb;
            JAVector vectorMan = new JAVector();
            vectorMan.addAll(vbRente.getPeriodesInvalidite());
            viewBean.setContainer(vectorMan);

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            request.setAttribute("viewBean", viewBean);
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);

            _destination = "/corvusRoot/demandes/periodeInvalidite_rcListe.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    public void actionListerPeriodesINVPrestationTransitoire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws ServletException,
            IOException {
        actionListerPeriodesINV(session, request, response, mainDispatcher, vb);
    }

    /**
     * Action qui permet de supprimer un période API dans la liste du viewBean de la base si nécessaire
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     * @return la destination à atteindre
     */
    public String actionSupprimerPeriodeAPI(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        RESaisieDemandeRenteViewBean vb = (RESaisieDemandeRenteViewBean) viewBean;
        vb.setIdProvisoirePeriodeAPI(new Long(getSelectedId(request)));
        vb.setModifie(true);

        mainDispatcher.dispatch(vb, getAction());
        return this.getUserActionURL(request, IREActions.ACTION_SAISIE_DEMANDE_RENTE,
                "actionListerPeriodesAPI&forIdDemandeRente=" + vb.getIdDemandeRente());
    }

    /**
     * Action qui permet de supprimer un période Invalidité dans la liste du viewBean de la base si nécessaire
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     * @return la destination à atteindre
     */
    public String actionSupprimerPeriodeINV(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        RESaisieDemandeRenteViewBean vb = (RESaisieDemandeRenteViewBean) viewBean;
        vb.setIdProvisoirePeriodeINV(new Long(getSelectedId(request)));
        vb.setModifie(true);

        mainDispatcher.dispatch(vb, getAction());
        return this.getUserActionURL(request, IREActions.ACTION_SAISIE_DEMANDE_RENTE,
                "actionListerPeriodesINV&forIdDemandeRente=" + vb.getIdDemandeRente());
    }

    public String actionSupprimerPeriodeINVPrestationTransitoire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {
        return actionSupprimerPeriodeINV(session, request, response, mainDispatcher, viewBean);
    }

    public void afficherCopie(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        String destination = "";

        try {

            REDemandeRenteViewBean vb = (REDemandeRenteViewBean) request.getAttribute("viewBean");
            if (vb == null) {
                actionAfficher(session, request, response, mainDispatcher);
            }

            String selectedId = vb.getIdDemandeRenteCopiee();
            RESaisieDemandeRenteViewBean vvvvBean = null;

            vvvvBean = doBuildDemande((BSession) mainDispatcher.getSession(), vvvvBean, selectedId);
            vvvvBean.setIsCalculerCopie(Boolean.TRUE);
            this.saveViewBean(vvvvBean, session);

            if (!FWViewBeanInterface.ERROR.equals(vb.getMsgType())) {
                destination = getRelativeURL(request, session) + "_de.jsp?selectedId=" + selectedId;
            } else {
                destination = FWDefaultServletAction.ERROR_PAGE;
            }

            // On mémorise le NSS dans la session
            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(vvvvBean.getNssRequerant());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Action qui permet d'afficher l'écran des informations complémentaires
     * 
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     * @throws Exception
     */
    public void afficherInformationsComplementaires(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        REInfoComplViewBean infoComplViewBean = null;

        if (viewBean instanceof RESaisieDemandeRenteViewBean) {

            // ViewBean d'arrivée
            RESaisieDemandeRenteViewBean vb = (RESaisieDemandeRenteViewBean) viewBean;

            // viewBean pour l'affichage
            infoComplViewBean = new REInfoComplViewBean();
            infoComplViewBean.setSession((BSession) mainDispatcher.getSession());
            infoComplViewBean.setIdInfoCompl(vb.getIdInfoComplementaire());
            infoComplViewBean.setIdTiers(vb.getIdTiers());
            infoComplViewBean.setIdDemandeRente(vb.getIdDemandeRente());

            infoComplViewBean = (REInfoComplViewBean) mainDispatcher.dispatch(infoComplViewBean, getAction());

        } else if (viewBean instanceof REGenererTransfertDossierViewBean) {

            // ViewBean d'arrivée
            REGenererTransfertDossierViewBean vb = (REGenererTransfertDossierViewBean) viewBean;

            // viewBean pour l'affichage
            infoComplViewBean = new REInfoComplViewBean();
            infoComplViewBean.setSession((BSession) mainDispatcher.getSession());
            infoComplViewBean.setIdInfoCompl(vb.getIdInfoCompl());
            infoComplViewBean.setIdTiers(vb.getIdTiers());
            infoComplViewBean.setIdDemandeRente(vb.getIdDemandeRente());

            infoComplViewBean = (REInfoComplViewBean) mainDispatcher.dispatch(infoComplViewBean, getAction());
        } else if (viewBean instanceof REInfoComplViewBean){
            infoComplViewBean = (REInfoComplViewBean)viewBean;
        } else{
            infoComplViewBean = (REInfoComplViewBean) mainDispatcher.dispatch(viewBean, getAction());
        }

        this.saveViewBean(infoComplViewBean, request);
        this.saveViewBean(infoComplViewBean, session);

        _destination = "/corvusRoot/demandes/informationsComplementaires_de.jsp";

        if ((infoComplViewBean != null) && !FWViewBeanInterface.ERROR.equals(infoComplViewBean.getMsgType())) {
            servlet.getServletContext().getRequestDispatcher(_destination + "?_method=").forward(request, response);
        } else {
            servlet.getServletContext().getRequestDispatcher(_destination + "?_method=add&_valid=error")
                    .forward(request, response);
        }
    }

    /**
     * Affiche la copie de la demande d'invalidité utilisé pour la création de la prestation transitoire
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @throws ServletException
     * @throws IOException
     */
    public void afficherPrestationTransitoire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws ServletException,
            IOException {

        String destination = "";
        try {

            RESaisieDemandeRenteViewBean viewBean = new RESaisieDemandeRenteViewBean();
            viewBean.setIdDemandeRente(((REDemandeRenteViewBean) request.getAttribute("viewBean"))
                    .getIdDemandeRenteCopiee());

            viewBean = (RESaisieDemandeRenteViewBean) mainDispatcher.dispatch(viewBean, getAction());

            this.saveViewBean(viewBean, request);
            this.saveViewBean(viewBean, session);
            viewBean.setSession((BSession) mainDispatcher.getSession());
            // On mémorise les NSS dans la session
            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(viewBean.getNssRequerant());

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

            if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                destination = "/corvusRoot/demandes/saisieDemandePrestationTransitoire_de.jsp";
            } else {
                destination = FWDefaultServletAction.ERROR_PAGE;
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Action appelée lors qui ajoute les informations complémentaires dans le viewBean quand on revient de l'écran
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     */
    public void ajouterInformationsComplementaires(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {
        try {
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            REInfoComplViewBean infoComplViewBean = (REInfoComplViewBean) viewBean;
            String chaineIdInfoCompl = "";
            if (!JadeStringUtil.isBlankOrZero(infoComplViewBean.getIdInfoCompl())) {
                chaineIdInfoCompl = "&idInfoComplementaire=" + infoComplViewBean.getIdInfoCompl();
            }

            String typeRetour = "";
            if (JadeStringUtil.isBlankOrZero(infoComplViewBean.getIdDemandeRente())) {
                typeRetour = "&_method=add";
            } else {
                typeRetour = "&_method=";
            }

            if (goesToSuccessDest) {
                _destination = _getDestAjouterSuccesInfoComp(session, request, response, viewBean);
                _destination += "&idDemandeRente=" + infoComplViewBean.getIdDemandeRente() + typeRetour
                        + chaineIdInfoCompl;
            } else {
                _destination = _getDestAjouterEchecInfoComp(session, request, response, viewBean);
            }

            // On mémorise le NSS dans la session
            RENSSDTO dto = new RENSSDTO();
            if(viewBean instanceof  RESaisieDemandeRenteViewBean) {
                dto.setNSS(((RESaisieDemandeRenteViewBean) viewBean).getNssRequerant());
                PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            }
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

        } catch (Exception e) {
            _destination = _getDestAjouterEchecInfoComp(session, request, response, viewBean);
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Création définitive de la prestation transitoire. Action lancée suite à l'appui sur le bouton suivant
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @throws ServletException
     * @throws IOException
     */
    public void ajouterSaisieDemandePrestationTransitoire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws ServletException,
            IOException {

        try {
            // ATTENTION : le viewBean est récupéré depuis la session
            RESaisieDemandeRenteViewBean viewBean = (RESaisieDemandeRenteViewBean) session.getAttribute("viewBean");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = (RESaisieDemandeRenteViewBean) beforeAjouter(session, request, response, viewBean);
            viewBean = (RESaisieDemandeRenteViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // On mémorise le NSS dans la session
            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(viewBean.getNssRequerant());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = "/corvusRoot/demandes/saisieDemandePrestationTransitoire_de.jsp";
            } else {
                // Création du viewBean attendu par la page page prestationAccoree_rc.jsp
                RERenteAccordeeJointDemandeRenteViewBean newViewBean = new RERenteAccordeeJointDemandeRenteViewBean();
                newViewBean.setIdTierRequerant(viewBean.getIdTiers());
                newViewBean.setNoDemandeRente(viewBean.getIdDemandeRente());
                session.setAttribute("viewBean", newViewBean);

                request.setAttribute(FWServlet.VIEWBEAN, newViewBean);
                request.setAttribute("noDemandeRente", viewBean.getIdDemandeRente());
                request.setAttribute("idTierRequerant", viewBean.getIdTiers());

                _destination = "/corvusRoot/rentesaccordees/renteAccordeeJointDemandeRente_rc.jsp";
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action appelée lors qu'on clic sur "ajouter" dans la saisie de la demande de rente
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     */
    public void ajouterSaisieDemandeRente(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {
        try {

            // si pas de droit update, on redirige directement sur la page suivante
            if (mainDispatcher.getSession().hasRight(IREActions.ACTION_SAISIE_DEMANDE_RENTE, FWSecureConstants.UPDATE)) {

                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

                viewBean = beforeAjouter(session, request, response, viewBean);
                viewBean = mainDispatcher.dispatch(viewBean, getAction());

                session.setAttribute("viewBean", viewBean);
                request.setAttribute(FWServlet.VIEWBEAN, viewBean);

                boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

                if (goesToSuccessDest) {
                    _destination = this._getDestAjouterSucces(session, request, response, viewBean, mainDispatcher);
                } else {
                    _destination = _getDestAjouterEchec(session, request, response, viewBean);
                }

            } else {
                _destination = this._getDestAjouterSucces(session, request, response, viewBean, mainDispatcher);
            }

            // On mémorise le NSS dans la session
            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(((RESaisieDemandeRenteViewBean) viewBean).getNssRequerant());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        if (_destination.startsWith("/")) {
            _destination = _destination.substring(1);
        }
        if (_destination.length() > 2000) {
            JadeLogger.warn(this, "sendRedirect: URL seems pretty long [" + _destination.length() + " caracters]: "
                    + _destination);
        }
        response.sendRedirect(_destination);
    }

    /**
     * Action appelée lors qui arrête les informations complémentaires dans le viewBean quand on revient de l'écran
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     */
    public void arreterInformationsComplementaires(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {
        try {

            JSPUtils.setBeanProperties(request, viewBean);

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            REInfoComplViewBean infoComplViewBean = (REInfoComplViewBean) viewBean;

            String chaineIdInfoCompl = "";
            if (!JadeStringUtil.isBlankOrZero(infoComplViewBean.getIdInfoCompl())) {
                chaineIdInfoCompl = "&idInfoComplementaire=" + infoComplViewBean.getIdInfoCompl();
            }

            String typeRetour = "";
            if (JadeStringUtil.isBlankOrZero(infoComplViewBean.getIdDemandeRente())) {
                typeRetour = "&_method=add";
            } else {
                typeRetour = "&_method=";
            }

            if (goesToSuccessDest) {
                _destination = _getDestAjouterSuccesInfoComp(session, request, response, viewBean);
                _destination += "&idDemandeRente=" + infoComplViewBean.getIdDemandeRente() + typeRetour
                        + chaineIdInfoCompl;
            } else {
                _destination = _getDestAjouterEchecInfoComp(session, request, response, viewBean);
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);

    }

    /**
     * Action appelée lors qu'on arrête la saisie de la demande de rente après avoir éditer uniquement le genre de
     * prestation et le nombre de page de la motivation (edition partielle, toujours possible quel que soit l'état de la
     * demande, pour API + Invalidité).
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     */
    public void arreterMajPartielleDemande(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        try {

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            session.setAttribute("viewBean", viewBean);
            this.saveViewBean(viewBean, request);
            this.saveViewBean(viewBean, (HttpServletRequest) mainDispatcher.getSession());
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                _destination = _getDestAjouterSuccesArret(session, request, response, viewBean);
            } else {
                _destination = _getDestAjouterEchec(session, request, response, viewBean);
            }

            // On mémorise le NSS dans la session
            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(((RESaisieDemandeRenteViewBean) viewBean).getNssRequerant());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);

    }

    /**
     * Appelé lors de l'arrêt de la saisie de la demande de prestation transitoire
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @throws ServletException
     * @throws IOException
     */
    public void arreterSaisieDemandePrestationTransitoire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        try {
            // Suppression des entités précédemment clonés
            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = _getDestAjouterSuccesArret(session, request, response, viewBean);
            } else {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }

            // On mémorise le NSS dans la session
            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(((RESaisieDemandeRenteViewBean) viewBean).getNssRequerant());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        goSendRedirect(_destination, request, response);
    }

    public void arreterSaisieDemandeRente(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        try {

            if (mainDispatcher.getSession().hasRight(IREActions.ACTION_SAISIE_DEMANDE_RENTE, FWSecureConstants.UPDATE)) {

                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

                viewBean = beforeAjouter(session, request, response, viewBean);
                viewBean = mainDispatcher.dispatch(viewBean, getAction());

                session.setAttribute("viewBean", viewBean);
                request.setAttribute(FWServlet.VIEWBEAN, viewBean);

                boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

                if (goesToSuccessDest) {
                    _destination = _getDestAjouterSuccesArret(session, request, response, viewBean);
                } else {
                    _destination = _getDestAjouterEchec(session, request, response, viewBean);
                }

            } else {
                _destination = _getDestAjouterSuccesArret(session, request, response, viewBean);
            }

            // On mémorise le NSS dans la session
            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(((RESaisieDemandeRenteViewBean) viewBean).getNssRequerant());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);

    }

    public void calculerCopie(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        try {

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                _destination = "/corvus?userAction=corvus.acor.calculACORDemandeRente.afficher&selectedId="
                        + ((RESaisieDemandeRenteViewBean) viewBean).getIdDemandeRente() + "&csTypeDemande="
                        + ((RESaisieDemandeRenteViewBean) viewBean).getCsTypeDemandeRente();

            } else {
                _destination = _getDestAjouterEchec(session, request, response, viewBean);
            }

            // On mémorise le NSS dans la session
            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(((RESaisieDemandeRenteViewBean) viewBean).getNssRequerant());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);

    }

    protected RESaisieDemandeRenteViewBean doBuildDemande(BSession session, RESaisieDemandeRenteViewBean viewBean,
            String selectedId) throws Exception {

        viewBean = REMappingSaisieDemandeRente.buildSaisieDemandeRenteViweBean(selectedId, session,
                session.getCurrentThreadTransaction());

        PRTiersWrapper requerant = PRTiersHelper.getTiersParId(session, viewBean.getIdRequerant());

        // Remplir le vb des données du requérant
        viewBean.setNssRequerant(requerant.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        viewBean.setNomRequerant(requerant.getProperty(PRTiersWrapper.PROPERTY_NOM));
        viewBean.setPrenomRequerant(requerant.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
        viewBean.setDateNaissanceRequerant(requerant.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
        viewBean.setCsSexeRequerant(requerant.getProperty(PRTiersWrapper.PROPERTY_SEXE));
        // la nationalité est donnée par la clé PROPERTY_ID_PAYS_DOMICILE
        viewBean.setCsNationaliteRequerant(requerant.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE));
        viewBean.setDateDecesRequerant(requerant.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES));
        viewBean.setIdTiers(requerant.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));

        PRTiersWrapper requerantAdresse = PRTiersHelper.getTiersAdresseDomicileParId(session,
                viewBean.getIdRequerant(), JACalendar.todayJJsMMsAAAA());

        if (requerantAdresse != null) {
            viewBean.setCsCantonRequerant(requerantAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON));
        }

        // Remplir le vb de la liste des périodes API
        REPeriodeAPIListViewBean apiListVB = new REPeriodeAPIListViewBean();
        apiListVB.setForIdDemandeRente(viewBean.getIdDemandeRente());
        apiListVB.setSession(session);
        apiListVB.find();

        TreeSet<REPeriodeAPIViewBean> listePeriodeAPI = viewBean.getPeriodesAPI();

        for (int i = 0; i < apiListVB.size(); i++) {
            REPeriodeAPIViewBean periodeAPIVB = (REPeriodeAPIViewBean) apiListVB.get(i);

            Integer noProv = viewBean.getNextKeyPeriodeAPI();

            if (JadeStringUtil.isEmpty(periodeAPIVB.getIdPeriodeAPI())) {
                periodeAPIVB.setIdProvisoire(noProv.intValue());
            } else {
                periodeAPIVB.setIdProvisoire(Integer.parseInt(periodeAPIVB.getIdPeriodeAPI()));
            }

            listePeriodeAPI.add(periodeAPIVB);

        }

        viewBean.setPeriodesAPI(listePeriodeAPI);

        // Remplir le vb de la liste des périodes Invalidités
        REPeriodeInvaliditeListViewBean invListVB = new REPeriodeInvaliditeListViewBean();
        invListVB.setForIdDemandeRente(viewBean.getIdDemandeRente());
        invListVB.setSession(session);
        invListVB.find();

        TreeSet<REPeriodeInvaliditeViewBean> listePeriodeINV = viewBean.getPeriodesInvalidite();

        for (int i = 0; i < invListVB.size(); i++) {
            REPeriodeInvaliditeViewBean periodeINVVB = (REPeriodeInvaliditeViewBean) invListVB.get(i);

            Integer noProv = viewBean.getNextKeyPeriodeINV();

            if (JadeStringUtil.isEmpty(periodeINVVB.getIdPeriodeInvalidite())) {
                periodeINVVB.setIdProvisoire(noProv.intValue());
            } else {
                periodeINVVB.setIdProvisoire(Integer.parseInt(periodeINVVB.getIdPeriodeInvalidite()));
            }

            listePeriodeINV.add(periodeINVVB);

        }

        viewBean.setPeriodesInvalidite(listePeriodeINV);

        viewBean.setSession(session);

        return viewBean;
    }

    /**
     * Action appelée lors qu'on valide la saisie de la demande de rente après avoir éditer uniquement le genre de
     * prestation et le nombre de page de la motivation (edition partielle, toujours possible quel que soit l'état de la
     * demande, pour API + Invalidité).
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     */
    public void majPartielleDemande(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        try {

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                _destination = _getDestAjouterSuccesArret(session, request, response, viewBean);
            } else {
                _destination = _getDestAjouterEchec(session, request, response, viewBean);
            }

            // On mémorise le NSS dans la session
            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(((RESaisieDemandeRenteViewBean) viewBean).getNssRequerant());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);

    }

    /**
     * Action appelée lorsqu'on supprime une demande de rente
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     */
    public void supprimerSaisieDemandeRente(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        try {

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                _destination = _getDestAjouterSuccesArret(session, request, response, viewBean);
            } else {
                _destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);

    }

    /**
     * BZ 5493, permet de faire passer une demande de rente vieillesse, dont la personne est décédée avant d'y avoir eu
     * droit, en état terminé (et de supprimer sa base de calcul)
     */
    public void terminerDemandeVieillesse(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {
        try {
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = _getDestAjouterEchecInfoComp(session, request, response, viewBean);
            } else {
                _destination = this.getUserActionURL(request,
                        IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE, "chercher");
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);
    }

}
