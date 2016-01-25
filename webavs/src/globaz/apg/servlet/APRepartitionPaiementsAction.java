package globaz.apg.servlet;

import globaz.apg.db.prestation.APCotisation;
import globaz.apg.db.prestation.APCotisationManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.db.prestation.APRepartitionPaiementsManager;
import globaz.apg.vb.droits.APDroitDTO;
import globaz.apg.vb.prestation.APRepartirLesMontantsViewBean;
import globaz.apg.vb.prestation.APRepartitionPaiementsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWCurrency;
import globaz.fweb.taglib.FWSelectorTag;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRCalcul;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * <p>
 * Une action qui fait beaucoup de choses a telle point que j'ai hesite a l'appeller MacGyver.
 * </p>
 * <p>
 * Le principe de l'ecran associe est d'afficher les repartitions de paiements pour une prestation d'un droit avec en
 * plus des boutons pour passer d'une prestation a l'autre de ce droit s'il en possede plusieurs.
 * </p>
 * <p>
 * La ou ca se complique c'est que c'est un ca page et que l'utilise doit pouvoir changer l'adresse de paiement de la
 * repartition (en partant dans pyxis) et revenir dans l'ecran en affichant la bonne repartion pour la bonne prestation
 * pour le bon droit en utilisant les tags (selector) qui permettent de passer d'une application a l'autre. Cette
 * situation est aggravee par le fait que nous sommes dans une ca page et que l'on ne peut donc rien stocker de commun
 * pour tous l'ecran (car il y a trois jsp et donc trois requetes)
 * </p>
 * <p>
 * Une serie de hacks permettent de simplifier le processus de la selection dans pyxis de la facon suivante:
 * </p>
 * <ol>
 * <li>on force l'action chercher comme action qui sera executee lors du retour depuis pyxis en ecrasant une des valeurs
 * mises en session par le tag de selection (voir
 * {@link #actionSelectionner(HttpSession, HttpServletRequest, HttpServletResponse, FWDispatcher) actionSelectionner})</li>
 * <li>l'utilisation dans la methode actionChoisir() des methodes de transfert des donnees entre les deux viewbean
 * (celui de pyxis et le notre) entrainent la mise d'un flag a true dans le viewbean (voir
 * {@link globaz.apg.vb.prestation.APRepartitionPaiementsViewBean#setIdAdressePaiementDepuisPyxis(String)
 * setIdAdressePaiementDepuisPyxis}).</li>
 * <li>ce flag est detecte dans
 * {@link #actionChercher(HttpSession, HttpServletRequest, HttpServletResponse, FWDispatcher) la methode chercher}. S'il
 * est a vrai, la methode recupere le viewBean dans la session.</li>
 * <li>ce flag est egalement detecte ensuite dans la methode
 * {@link #actionAfficher(HttpSession, HttpServletRequest, HttpServletResponse, FWDispatcher) actionAfficher} ou il sera
 * remis a false. La methode, (qui est appellee lors de l'affichage du cadre de detail de la ca page, je le rappelle),
 * se contente alors de rediriger vers la jsp sans rien faire, de cette facon, la repartition qui vient d'etre modifie
 * est de nouveau affichee.</li>
 * </ol>
 * 
 * @author vre
 */
public class APRepartitionPaiementsAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static FWAction ACTION_PRE_CHERCHER;
    private static final String VERS_ECRAN_DE_ADD = "_de.jsp?" + PRDefaultAction.METHOD_ADD;
    private static final String VERS_ECRAN_DE_UPD = "_de.jsp?" + PRDefaultAction.METHOD_UPD;

    private static final String VERS_ECRAN_RC = "_rc.jsp";

    static {
        APRepartitionPaiementsAction.ACTION_PRE_CHERCHER = FWAction.newInstance(IAPActions.ACTION_REPARTITION_PAIEMENTS
                + ".actionPreparerChercher");
        // ACTION_PRE_CHERCHER.setRight(FWSecureConstants.ADD);
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APRepartitionPaiementsAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public APRepartitionPaiementsAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * on donne un parametre special dans l'url de la page de qui fait que celle-ci force le rechargement de la liste.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        saveNewViewBean(session, (APRepartitionPaiementsViewBean) viewBean);

        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    /**
     * on donne un parametre special dans l'url de la page de qui fait que celle-ci force le rechargement de la liste.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        saveNewViewBean(session, (APRepartitionPaiementsViewBean) viewBean);

        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    /**
     * on donne un parametre special dans l'url de la page de qui fait que celle-ci force le rechargement de la liste.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        saveNewViewBean(session, (APRepartitionPaiementsViewBean) viewBean);

        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }

    /**
     * redefinition pour intercepter les retours depuis pyxis et faire en sorte que le cadre de detail s'affiche avec
     * les donnes de la repartition dont l'adresse de paiement vient d'etre modifiee et pas une ecran vide.
     * 
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
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = this.loadViewBean(session);

        if (isRetourDepuisPyxis(viewBean)) {
            // on revient depuis pyxis on se contente de forwarder car le bon
            // viewBean est deja en session
            ((APRepartitionPaiementsViewBean) viewBean).setRetourDepuisPyxis(false); // pour
            // la
            // prochaine
            // fois

            if (((APRepartitionPaiementsViewBean) viewBean).isNew()) {
                forward(getRelativeURL(request, session) + APRepartitionPaiementsAction.VERS_ECRAN_DE_ADD, request,
                        response);
            } else {
                forward(getRelativeURL(request, session) + APRepartitionPaiementsAction.VERS_ECRAN_DE_UPD, request,
                        response);
            }
        } else {
            // on ne revient pas depuis pyxis, comportement par defaut
            super.actionAfficher(session, request, response, mainDispatcher);

        }
    }

    /**
     * redefinition pour charger les informations qui doivent s'afficher dans l'ecran rc.
     * <p>
     * cette methode inspecte la session pour savoir si l'on revient depuis pyxis. Si c'est le cas, l'ancien viewBean
     * est conserve dans la session et il sera reutilise pour l'action afficher.
     * </p>
     * 
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
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = this.loadViewBean(session);
        APRepartitionPaiementsViewBean rpViewBean;

        boolean isIdPrestation = false;
        String idPrestation = "";

        if (viewBean instanceof APRepartitionPaiementsViewBean) {
            rpViewBean = (APRepartitionPaiementsViewBean) viewBean;

            if (rpViewBean != null) {
                idPrestation = rpViewBean.getIdPrestationApg();
                isIdPrestation = true;
            }

        }

        if (isRetourDepuisPyxis(viewBean)) {
            // on revient de pyxis, on recupere le viewBean en session pour
            // afficher les donnees dans la page rc.
            rpViewBean = (APRepartitionPaiementsViewBean) viewBean;

        } else {
            /*
             * on affiche cette page par un chemin habituel, dans ce cas tout est normal, mise a part que l'on met un
             * viewBean dans la session qui contient les donnees qui doivent s'afficher dans le cadre rc de la ca page.
             */
            rpViewBean = new APRepartitionPaiementsViewBean();

            try {
                JSPUtils.setBeanProperties(request, rpViewBean);
            } catch (Exception e) {
                rpViewBean.setMessage(e.getMessage());
                rpViewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        if (isIdPrestation) {
            rpViewBean.setIdPrestationApg(idPrestation);
        }

        // on appelle le helper qui va charger les infos sur le droit et la
        // prestation qui doivent s'afficher
        mainDispatcher.dispatch(rpViewBean, getAction());

        APDroitDTO dto = new APDroitDTO();
        dto.setDateDebutDroit(rpViewBean.getDateDebutDroit());
        dto.setGenreService(rpViewBean.getGenreService());
        dto.setIdDroit(rpViewBean.getIdDroit());
        dto.setNoAVS(rpViewBean.getNoAVSAssure());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);

        /*
         * on sauve de toutes facons le viewBean dans la requete meme s'il est deja en session car c'est la que la page
         * rc va le rechercher.
         */

        this.saveViewBean(rpViewBean, request);

        forward(getRelativeURL(request, session) + APRepartitionPaiementsAction.VERS_ECRAN_RC, request, response);
    }

    /**
     * remplace le viewBean de repartition dans la session par une nouvelle ventilation pour cette repartition et
     * redirige sur la page de detail.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String actionNouvelleVentilation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        APRepartitionPaiementsViewBean oldViewBean = (APRepartitionPaiementsViewBean) viewBean;
        APRepartitionPaiementsViewBean newViewBean = new APRepartitionPaiementsViewBean();

        newViewBean.setIdDroit(oldViewBean.getIdDroit());
        newViewBean.setGenreService(oldViewBean.getGenreService());
        newViewBean.setIdParent(oldViewBean.getIdRepartitionBeneficiairePaiement());
        newViewBean.setIdPrestationApg(oldViewBean.getIdPrestationApg());
        newViewBean.setMontantNet(oldViewBean.getMontantNet());
        newViewBean.setSession((BSession) mainDispatcher.getSession());
        this.saveViewBean(newViewBean, session);

        return getRelativeURL(request, session) + APRepartitionPaiementsAction.VERS_ECRAN_DE_ADD;
    }

    /**
     * passe a la prestation precedante pour ce droit s'il en existe une.
     * <p>
     * delegue au helper le rechargement des infos sur le droit et des informations sur la nouvelle prestation.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String actionPrestationPrecedante(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        APRepartitionPaiementsViewBean rpViewBean = new APRepartitionPaiementsViewBean();

        try {
            JSPUtils.setBeanProperties(request, rpViewBean);
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        rpViewBean.prestationPrecedante();
        mainDispatcher.dispatch(rpViewBean, APRepartitionPaiementsAction.ACTION_PRE_CHERCHER);
        this.saveViewBean(rpViewBean, request);

        return getRelativeURL(request, session) + APRepartitionPaiementsAction.VERS_ECRAN_RC;
    }

    /**
     * passe a la prestation suivante pour ce droit s'il en existe une.
     * <p>
     * delegue au helper le rechargement des infos sur le droit et des informations sur la nouvelle prestation.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String actionPrestationSuivante(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        APRepartitionPaiementsViewBean rpViewBean = new APRepartitionPaiementsViewBean();

        try {
            JSPUtils.setBeanProperties(request, rpViewBean);
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        rpViewBean.prestationSuivante();
        mainDispatcher.dispatch(rpViewBean, APRepartitionPaiementsAction.ACTION_PRE_CHERCHER);
        this.saveViewBean(rpViewBean, request);

        return getRelativeURL(request, session) + APRepartitionPaiementsAction.VERS_ECRAN_RC;
    }

    /**
     * charge les information sur droit correspondant a la prestation et redirige sur l'ecran de repartition des
     * montants
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String actionRepartirLesMontants(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        // on charge les infos du droit
        APRepartitionPaiementsViewBean rpViewBean = (APRepartitionPaiementsViewBean) viewBean;
        // getAction().setRight(FWSecureConstants.ADD);
        mainDispatcher.dispatch(rpViewBean, getAction());

        APRepartirLesMontantsViewBean rlmViewBean = new APRepartirLesMontantsViewBean(rpViewBean);
        this.saveViewBean(rlmViewBean, session);
        return getRelativeURLwithoutClassPart(request, session) + "/repartirLesMontants"
                + APRepartitionPaiementsAction.VERS_ECRAN_DE_UPD;
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
    protected void actionSelectionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // recuperer les id et genre de service du droit pour le retour depuis
        // pyxis
        APRepartitionPaiementsViewBean rpViewBean = (APRepartitionPaiementsViewBean) this.loadViewBean(session);
        StringBuffer queryString = new StringBuffer();

        queryString.append(PRDefaultAction.USER_ACTION);
        queryString.append("=");
        queryString.append(IAPActions.ACTION_REPARTITION_PAIEMENTS);
        queryString.append(".");
        queryString.append(FWAction.ACTION_CHERCHER);
        queryString.append("&idDroit=");
        queryString.append(rpViewBean.getIdDroit());
        queryString.append("genreService=");
        queryString.append(rpViewBean.getGenreService());
        queryString.append("forIdPrestation=");
        queryString.append(rpViewBean.getIdPrestationApg());

        // HACK: on remplace une des valeurs sauvee en session par FWSelectorTag
        session.setAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL, queryString.toString());

        // comportement par defaut
        super.actionSelectionner(session, request, response, mainDispatcher);
    }

    /**
     * redefini pour ne pas perdre les idDroit et genreService qui sont transmis dans la requete.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewBean;
    }

    /**
     * inspecte le viewBean et retourne vrai si celui-ci indique que l'on revient de pyxis.
     * 
     * @param viewBean
     * @return
     */
    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof APRepartitionPaiementsViewBean) && ((APRepartitionPaiementsViewBean) viewBean)
                .isRetourDepuisPyxis());
    }

    /**
     * Supprime les ventilations, affecte les nouveaux montants aux répartitions et re-calcul les cotisations. Supprime
     * les repartition avec un montant brut a zero.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String miseAJourRepartitionsCotisations(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        APRepartirLesMontantsViewBean rlmViewBean = (APRepartirLesMontantsViewBean) viewBean;

        String destination = _getDestAjouterSucces(session, request, response, rlmViewBean.getRepartitionPaiements());

        // mise à jour des repartitions et des cotisations
        int c = 0;
        boolean hasMoreRepartitions = true;
        BSession bSession = (BSession) mainDispatcher.getSession();
        BITransaction biTransaction = bSession.newTransaction();

        try {
            biTransaction.openTransaction();

            while (hasMoreRepartitions) {
                c++;
                String val = request.getParameter("val_" + c);
                String id = request.getParameter("id_" + c);

                // si on ne trouve pas d'id, il n'y a plus de repartition
                if (JadeStringUtil.isIntegerEmpty(id)) {
                    hasMoreRepartitions = false;
                    continue;
                }

                // on retrouve la repartition
                APRepartitionPaiements rep = new APRepartitionPaiements();
                rep.setSession(bSession);
                rep.setIdRepartitionBeneficiairePaiement(id);
                rep.retrieve(biTransaction);

                // on efface les ventillations de la repartition
                APRepartitionPaiementsManager repMan = new APRepartitionPaiementsManager();
                repMan.setSession(bSession);
                repMan.setForIdParent(id);
                repMan.find(biTransaction);

                Iterator iter = repMan.iterator();
                while (iter.hasNext()) {
                    APRepartitionPaiements ven = (APRepartitionPaiements) iter.next();
                    ven.delete(biTransaction);
                }

                // si le montant vaut 0, on supprime la repartition
                // sinon on met a jour le montant et on recalcul les
                // repartitions
                FWCurrency montantBrut = new FWCurrency(val);
                FWCurrency montantNet = new FWCurrency(val);
                if (montantBrut.floatValue() == 0) {
                    rep.delete(biTransaction);
                } else {

                    rep.setMontantBrut(montantBrut.toString());
                    // on a supprime les ventillations
                    rep.setMontantVentile("0");

                    // recalcul des montants des cotisations
                    APCotisationManager cotisations = new APCotisationManager();
                    cotisations.setSession(bSession);
                    cotisations.setForIdRepartitionBeneficiairePaiement(id);
                    cotisations.find(biTransaction);
                    APCotisation cotFraisAdm = null;
                    String montantBrutFraisAdm = "";
                    // BZ 5418 Modification du traitement des cotisations lors de la répartition manuelle
                    for (int idCotisation = 0; idCotisation < cotisations.size(); ++idCotisation) {
                        APCotisation cotisation = (APCotisation) cotisations.get(idCotisation);

                        // BZ 5418 Dans le cas ou une cotisation frais d'administration existe, je la mémorise

                        if ((bSession.getApplication().getProperty("assurance.fad.paritaire.id")
                                .equals(cotisation.getIdExterne()) || (bSession.getApplication().getProperty(
                                "assurance.fad.personnelle.id").equals(cotisation.getIdExterne())))) {
                            cotFraisAdm = cotisation;
                            continue;
                        }

                        String nc = JANumberFormatter.format(
                                PRCalcul.pourcentage100(montantBrut.toString(), cotisation.getTaux()), 0.05, 2,
                                JANumberFormatter.NEAR);

                        cotisation.setMontantBrut(montantBrut.toString());
                        // Il ne faut pas traiter les cotisation frais d'administration maintenant, mais il faut
                        // déterminer la cotisation avs et prendre le montant de la cotisation qui doit être mis en
                        // temps que
                        // montant brut sur celle frais d'administration
                        if (cotisation.getMontant().startsWith("-")) {
                            // si la cotisation etait soustraite, le nouvelle
                            // cotisation doit l'etre aussi
                            cotisation.setMontant("-" + nc);
                            montantNet.sub(nc);
                        } else {
                            cotisation.setMontant(nc);
                            montantNet.add(nc);
                        }

                        // BZ 5418 Je mémorise le nouveau montant de cotisation de la cotisation AVS
                        if ((bSession.getApplication().getProperty("assurance.avsai.paritaire.id")
                                .equals(cotisation.getIdExterne()) || (bSession.getApplication().getProperty(
                                "assurance.avsai.personnelle.id").equals(cotisation.getIdExterne())))) {
                            montantBrutFraisAdm = nc;
                        }

                        cotisation.wantCallMethodAfter(false);
                        cotisation.update(biTransaction);
                    }

                    // BZ 5418 Je traite les cotisations frais d'administration en dehors de la boucle ci-dessus
                    // car je dois déjà traiter cotisation AVS pour déterminer le nouveau montant de cotisation
                    if (null != cotFraisAdm) {
                        // BZ 5418 J'utilise le montant cotisation de la cotisation AVS pour déterminer
                        // le nouveau montant cotisation de la cotisation frais d'administration
                        String nc = JANumberFormatter.format(
                                PRCalcul.pourcentage100(montantBrutFraisAdm.toString(), cotFraisAdm.getTaux()), 0.05,
                                2, JANumberFormatter.NEAR);

                        cotFraisAdm.setMontantBrut(montantBrutFraisAdm.toString());

                        if (cotFraisAdm.getMontant().startsWith("-")) {
                            // si la cotisation etait soustraite, le nouvelle
                            // cotisation doit l'etre aussi
                            cotFraisAdm.setMontant("-" + nc);
                            montantNet.sub(nc);
                        } else {
                            cotFraisAdm.setMontant(nc);
                            montantNet.add(nc);
                        }

                        cotFraisAdm.wantCallMethodAfter(false);
                        cotFraisAdm.update(biTransaction);
                    }

                    // sauvegarder la nouvelle repartition
                    rep.setMontantNet(montantNet.toString());
                    rep.wantCallValidate(false);
                    rep.update(biTransaction);
                }
            }

            // on set le flag de la prestation "modifie par l'utilisateur" a
            // true
            APPrestation pr = new APPrestation();
            pr.setSession(bSession);
            pr.setIdPrestationApg(rlmViewBean.getIdPrestationCourante());
            pr.retrieve(biTransaction);
            pr.setIsModifiedByUser(Boolean.TRUE);
            pr.update(biTransaction);

            biTransaction.commit();

        } catch (Exception e) {
            destination = _getDestAjouterEchec(session, request, response, rlmViewBean.getRepartitionPaiements());

            bSession.addError("Erreur lors de la création des nouvelles répartition : " + e.toString());
            rlmViewBean.setMessage(e.toString());
            rlmViewBean.setMsgType(FWViewBeanInterface.ERROR);
            if (biTransaction != null) {
                biTransaction.rollback();
            }
        } finally {
            if (biTransaction != null) {
                biTransaction.closeTransaction();
            }
        }

        return destination;
    }

    private void saveNewViewBean(HttpSession session, APRepartitionPaiementsViewBean oldViewBean) {
        APRepartitionPaiementsViewBean newViewBean = new APRepartitionPaiementsViewBean();

        newViewBean.setIdDroit(oldViewBean.getIdDroit());
        newViewBean.setGenreService(oldViewBean.getGenreService());
        newViewBean.setIdPrestationApg(oldViewBean.getIdPrestationApg());
        newViewBean.setIdsPrestations(oldViewBean.getIdsPrestations());
        newViewBean.setIdOfIdPrestationCourante(oldViewBean.getIdOfIdPrestationCourante());

        this.saveViewBean(newViewBean, session);
    }
}
