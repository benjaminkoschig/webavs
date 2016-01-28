package globaz.apg.servlet;

import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APSituationFamilialeAPG;
import globaz.apg.helpers.prestation.APPrestationHelper;
import globaz.apg.vb.droits.APDroitAPGDTO;
import globaz.apg.vb.droits.APDroitDTO;
import globaz.apg.vb.droits.APEnfantAPGViewBean;
import globaz.apg.vb.droits.APEnfantAPG_2ViewBean;
import globaz.apg.vb.droits.APSituationFamilialeAPGViewBean;
import globaz.apg.vb.prestation.APPrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1> Créé le 20 mai 05
 * 
 * @author vre
 */
public class APEnfantAPGAction extends APAbstractDroitDTOAction {

    private static final String VERS_2_DE = "_2_de.jsp";

    /**
     * Crée une nouvelle instance de la classe APEnfantAPGAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public APEnfantAPGAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * par défaut le framework redirige vers une action .chercher après un ajout. Nous préférons que la page soit
     * directement redirigée vers la page _de.jsp.
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
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + "." + FWAction.ACTION_CHERCHER + "&" + PRDefaultAction.VALID_NEW + "&"
                + APAbstractDroitDTOAction.PARAM_ID_DROIT + "="
                + request.getParameter(APAbstractDroitDTOAction.PARAM_ID_DROIT);
    }

    /**
     * Si on modifie un enfant (ecran enfant_de2.jsp) et que c'est un echec, il faut rediriger vers le de2.
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
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getRelativeURL(request, session) + APEnfantAPGAction.VERS_2_DE + "?"
                + APAbstractDroitDTOAction.PARAM_ID_DROIT + "="
                + request.getParameter(APAbstractDroitDTOAction.PARAM_ID_DROIT) + "&"
                + PRDefaultAction.DESACTIVE_VALIDATION;
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".chercher&" + APAbstractDroitDTOAction.PARAM_ID_DROIT + "="
                + request.getParameter(APAbstractDroitDTOAction.PARAM_ID_DROIT);
    }

    /**
     * On passe par cette action si on clique su un element de la liste dans l'écran de saisie carte APG numéro 3, il
     * faut appeler un écran _de particulier
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
     * @return la destination
     */
    public void actionAfficherEnfantDeListe(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        APEnfantAPGViewBean eaViewBean = (APEnfantAPGViewBean) viewBean;
        String destination = null;
        APEnfantAPG_2ViewBean enfant = new APEnfantAPG_2ViewBean();
        enfant.setIdEnfant(request.getParameter("selectedId"));
        enfant.setSession(eaViewBean.getSession());

        // on lui donne le DTO, les frais et le nb d'enfant debut droit pour ne
        // pas qu'ils soient perdus en revenant
        // sur la page de saisie APG numero 3;
        enfant.setDroitAPGDTO((APDroitAPGDTO) (session.getAttribute(PRSessionDataContainerHelper.KEY_DROIT_DTO)));
        enfant.setNbrEnfantsDebutDroit(eaViewBean.getNbrEnfantsDebutDroit());
        enfant.setFraisGarde(eaViewBean.getFraisGarde());

        try {
            enfant.retrieve();
            this.saveViewBean(enfant, request);

            destination = "apg.droits.enfantAPG_2.afficher&" + APAbstractDroitDTOAction.PARAM_ID_DROIT + "="
                    + request.getParameter(APAbstractDroitDTOAction.PARAM_ID_DROIT);
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect("/apg?userAction=" + destination, request, response);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAjouter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        try {
            // qd on ajoute un enfant dans le 3eme ecran de la saisie de carte
            // APG, il faut mettre à jour la situation
            // familiale (frais garde et nombre d'enfant) sinon, si on va sur le
            // détail d'un enfant, en revenant dans
            // l'écran
            // les infos qu'on aurait pu entrer seront perdues
            APEnfantAPGViewBean viewBean = (APEnfantAPGViewBean) this.loadViewBean(session);
            APSituationFamilialeAPG situationFamilialeAPG = viewBean.loadSituationFamilialeAPG();
            JSPUtils.setBeanProperties(request, situationFamilialeAPG);
            situationFamilialeAPG = (APSituationFamilialeAPG) mainDispatcher.dispatch(situationFamilialeAPG,
                    FWAction.newInstance("apg.droits.situationFamilialeAPG.modifier"));

            // repercution des éventuelles erreurs sur le viewBean de l'enfant
            if (situationFamilialeAPG.hasErrors()) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(situationFamilialeAPG.getErrors().toString());
            }

            this.saveViewBean(viewBean, session);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        super.actionAjouter(session, request, response, mainDispatcher);
    }

    /**
     * DOCUMENT ME!
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
     */
    public String actionCalculerPrestation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
        return actionCalculerToutesLesPrestation(session, request, response, mainDispatcher, viewBean);
        // // On doit d'abord sauver les infos de la situation familiale avant de
        // // calculer les prestations.
        //
        // // A ce moment, le viewBean en session sera un enfant APG.
        // String destination = FWDefaultServletAction.ERROR_PAGE;
        //
        // try {
        // APEnfantAPGViewBean enfantAPGViewBean = null;
        //
        // if ((viewBean instanceof APEnfantAPGViewBean)) {
        // enfantAPGViewBean = (APEnfantAPGViewBean) viewBean;
        //
        // // update de la situation familiale (qui existe deja puisque
        // // créée à la création du droit
        // FWAction actionUpdateSitFam = FWAction.newInstance("apg.droits.situationFamilialeAPG.modifier");
        // APSituationFamilialeAPGViewBean situationFamilialeAPGViewBean = (APSituationFamilialeAPGViewBean)
        // FWViewBeanActionFactory
        // .newInstance(actionUpdateSitFam, mainDispatcher.getPrefix());
        // situationFamilialeAPGViewBean.setIdSitFamAPG(enfantAPGViewBean.getIdSituationFamiliale());
        // situationFamilialeAPGViewBean.setISession(mainDispatcher.getSession());
        // situationFamilialeAPGViewBean.retrieve();
        // situationFamilialeAPGViewBean.setFraisGarde(enfantAPGViewBean.getFraisGarde());
        // situationFamilialeAPGViewBean.setNbrEnfantsDebutDroit(enfantAPGViewBean.getNbrEnfantsDebutDroitBrut());
        // mainDispatcher.dispatch(situationFamilialeAPGViewBean, actionUpdateSitFam);
        // destination = this.getUserActionURL(request, IAPActions.ACTION_PRESTATIONS,
        // "actionCalculerPrestations&genreService=" + this.loadDTO(session).getGenreService());
        // }
        // } catch (Exception e) {
        // viewBean.setMessage(e.getMessage());
        // this.saveViewBean(viewBean, session);
        //
        // return FWDefaultServletAction.ERROR_PAGE;
        // }
        //
        // return destination;
    }

    /**
     * DOCUMENT ME!
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
     */
    public String actionCalculerToutesLesPrestation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
        // On doit d'abord sauver les infos de la situation familiale avant de
        // calculer les prestations.

        // A ce moment, le viewBean en session sera un enfant APG.
        String destination = FWDefaultServletAction.ERROR_PAGE;

        try {
            APEnfantAPGViewBean enfantAPGViewBean = null;

            if ((viewBean instanceof APEnfantAPGViewBean)) {
                enfantAPGViewBean = (APEnfantAPGViewBean) viewBean;

                // update de la situation familiale (qui existe deja puisque
                // créée à la création du droit
                FWAction actionUpdateSitFam = FWAction.newInstance("apg.droits.situationFamilialeAPG.modifier");
                APSituationFamilialeAPGViewBean situationFamilialeAPGViewBean = (APSituationFamilialeAPGViewBean) FWViewBeanActionFactory
                        .newInstance(actionUpdateSitFam, mainDispatcher.getPrefix());
                situationFamilialeAPGViewBean.setIdSitFamAPG(enfantAPGViewBean.getIdSituationFamiliale());
                situationFamilialeAPGViewBean.setISession(mainDispatcher.getSession());
                situationFamilialeAPGViewBean.retrieve();
                situationFamilialeAPGViewBean.setFraisGarde(enfantAPGViewBean.getFraisGarde());
                situationFamilialeAPGViewBean.setNbrEnfantsDebutDroit(enfantAPGViewBean.getNbrEnfantsDebutDroitBrut());
                mainDispatcher.dispatch(situationFamilialeAPGViewBean, actionUpdateSitFam);

                // Ok, on a mis à jour la sit. familiale, on créer un nouveau viewBean pour l'action
                // prestation.calculDesPrestations
                APPrestationViewBean newViewBean = new APPrestationViewBean();
                newViewBean.setIdDroit(loadDTO(session).getIdDroit());
                newViewBean.setGenreService(loadDTO(session).getGenreService());
                this.saveViewBean(newViewBean, session);

                destination = this.getUserActionURL(request, IAPActions.ACTION_PRESTATIONS,
                        APPrestationHelper.CALCULER_TOUTES_LES_PRESTATIONS);// "actionCalculerToutesLesPrestations);
            }
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            this.saveViewBean(viewBean, session);

            return FWDefaultServletAction.ERROR_PAGE;
        }

        return destination;
    }

    /**
     * DOCUMENT ME!
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
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String actionEtape3(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        APEnfantAPGViewBean eaViewBean = (APEnfantAPGViewBean) viewBean;
        String retValue;
        String action;

        try {
            action = "apg.droits.situationFamilialeAPG.modifier";

            APSituationFamilialeAPG viewBeanSitFam = (APSituationFamilialeAPG) (FWViewBeanActionFactory.newInstance(
                    FWAction.newInstance(action), mainDispatcher.getPrefix()));
            viewBeanSitFam.setISession(eaViewBean.getSession());

            if (!eaViewBean.isArret()) {
                ((APSituationFamilialeAPGViewBean) viewBeanSitFam).wantCallValidate(true);
            }

            viewBeanSitFam.setIdSitFamAPG(eaViewBean.getIdSituationFamilialeAPG());
            viewBeanSitFam.retrieve();
            viewBeanSitFam.setFraisGarde(eaViewBean.getFraisGarde());
            viewBeanSitFam.setNbrEnfantsDebutDroit(eaViewBean.getNbrEnfantsDebutDroitBrut());
            viewBeanSitFam = (APSituationFamilialeAPG) beforeModifier(session, request, response, viewBeanSitFam);
            viewBeanSitFam = (APSituationFamilialeAPG) mainDispatcher.dispatch(viewBeanSitFam,
                    FWAction.newInstance(action));

            retValue = "";
        } catch (Exception e) {
            retValue = FWDefaultServletAction.ERROR_PAGE;
        }

        return retValue;
    }

    /**
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
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String actionFindIdTiersByNoAvs(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {
        // getAction().setRight(FWSecureConstants.ADD);
        mainDispatcher.dispatch(viewBean, getAction());
        this.saveViewBean(viewBean, session);

        String destination = getRelativeURL(request, session) + APEnfantAPGAction.VERS_2_DE;

        return destination;
    }

    /**
     * va chercher l'idTiers correspondant au numero avs saisis avant l'update
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        APEnfantAPG_2ViewBean viewBean = (APEnfantAPG_2ViewBean) this.loadViewBean(session);
        APEnfantAPGViewBean enfant = new APEnfantAPGViewBean();
        enfant.setSession(viewBean.getSession());
        enfant.setIdEnfant(viewBean.getIdEnfant());

        try {
            enfant.retrieve();
            JSPUtils.setBeanProperties(request, enfant);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Si le viewBean est en erreur, il faut mettre cette erreur
        // dans la session sinon le action modifier va tout
        // de même se faire et mettre le msgType du viewBean à OK
        if (enfant.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            enfant.getSession().addError(enfant.getMessage());
        }

        this.saveViewBean(enfant, session);

        super.actionModifier(session, request, response, mainDispatcher);
    }

    /**
     * DOCUMENT ME!
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
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String arreterEtape3(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        APEnfantAPGViewBean eaViewBean = (APEnfantAPGViewBean) viewBean;
        String retValue;

        // pour dire qu'on passe par arrêter
        eaViewBean.setArret(true);

        if (eaViewBean.isModifiable()) {
            eaViewBean.wantCallValidate(false);
            retValue = actionEtape3(session, request, response, mainDispatcher, viewBean);

            if (!FWDefaultServletAction.ERROR_PAGE.equals(retValue)) {
                // pas d'erreur lors de la sauvegarde, on modifie la destination
                // pour aller sur la page de recherche.
                retValue = this.getUserActionURL(request, IAPActions.ACTION_DROIT_LAPG, FWAction.ACTION_CHERCHER);
            }
        } else {
            // le droit n'est plus modifiable, on va directement sur la page de
            // recherche sans rien sauver.
            retValue = this.getUserActionURL(request, IAPActions.ACTION_DROIT_LAPG, FWAction.ACTION_CHERCHER);
        }

        return retValue;
    }

    /**
     * @see globaz.apg.servlet.APAbstractDroitDTOAction#getViewBeanClass()
     */
    @Override
    protected Class getViewBeanClass() {
        return APEnfantAPGViewBean.class;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param droitDTO
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface initViewBean(FWViewBeanInterface viewBean, APDroitDTO droitDTO, HttpSession session) {

        if (droitDTO instanceof APDroitAPGDTO) {
            ((APEnfantAPGViewBean) viewBean).setDroitAPGDTO((APDroitAPGDTO) droitDTO);
        }
        // On arrive dans ce cas, lors d'un back depuis l'écran des répartitions
        // de paiement.
        // Le dto est de type APDroitDTO; il faut donc recréer un APDroitAPGDto
        // et récupérer l'id
        // de la situation professionnelle à partir de l'id du droit qui lui est
        // toujours stocké dans
        // le DTO.
        else {
            APDroitAPG droit = new APDroitAPG();
            if (!JadeStringUtil.isIntegerEmpty(droitDTO.getIdDroit())) {
                try {
                    BISession bSession = ((globaz.framework.controller.FWController) session
                            .getAttribute("objController")).getSession();
                    droit.setSession((BSession) bSession);
                    droit.setIdDroit(droitDTO.getIdDroit());
                    droit.retrieve();
                } catch (Exception e) {
                    ;
                }

            }
            ((APEnfantAPGViewBean) viewBean).setDroitAPGDTO(new APDroitAPGDTO(droitDTO, droit.getIdDroit()));

        }

        return viewBean;
    }

    /**
     * redéfinie pour que l'action par défaut ne remplace pas le viewBean en session par un nouveau si on fait
     * method=add. On veut juste que l'écran affiche les infos concernant le droitMaternité lié à l'enfantAPG
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param idTiers
     *            DOCUMENT ME!
     * @param noAVS
     *            DOCUMENT ME!
     * @param nom
     *            DOCUMENT ME!
     * @param prenom
     *            DOCUMENT ME!
     * @param sexe
     *            DOCUMENT ME!
     * @param titre
     *            DOCUMENT ME!
     * @param idCanton
     *            DOCUMENT ME!
     * @param idPays
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    // protected void actionAfficher(HttpSession session, HttpServletRequest
    // request, HttpServletResponse response,
    // FWDispatcher mainDispatcher) throws ServletException, IOException {
    // /* pour compatibilité :
    // * si on a le parametre _method=add, c'est que l'on a une action nouveau
    // */
    // String _destination;
    //
    // try {
    // FWAction _action =
    // FWAction.newInstance(request.getParameter("userAction"));
    // String method = request.getParameter("_method");
    //
    // if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
    // _action.changeActionPart(FWAction.ACTION_NOUVEAU);
    // }
    //
    // //String selectedId = request.getParameter("selectedId");
    //
    // /*
    // * pour pouvoir faire un setId
    // * remarque : si il y a d'autre set a faire (si plusieurs id par ex)
    // * il faut le faire dans le beforeAfficher(...)
    // */
    // FWViewBeanInterface viewBean = loadViewBean(session);
    // //APEnfantAPGViewBean enfantAPG = (APEnfantAPGViewBean) viewBean;
    //
    // /*
    // * initialisation du viewBean
    // */
    // if (_action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
    // viewBean = beforeNouveau(session, request, response, viewBean);
    // }
    //
    // /*
    // * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
    // */
    // viewBean = beforeAfficher(session, request, response, viewBean);
    // viewBean = mainDispatcher.dispatch(viewBean, _action);
    // session.removeAttribute("viewBean");
    // session.setAttribute("viewBean", viewBean);
    //
    // /*
    // * choix destination
    // */
    // if (viewBean.getMsgType().equals(FWViewBean.ERROR) == true) {
    // _destination = ERROR_PAGE;
    // } else {
    // _destination = getRelativeURL(request, session) + "_de.jsp";
    // }
    // } catch (Exception e) {
    // _destination = ERROR_PAGE;
    // }
    //
    // /*
    // * redirection vers la destination
    // */
    // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
    // response);
    // }

    // /**
    // * @see
    // globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
    // * javax.servlet.http.HttpServletRequest,
    // javax.servlet.http.HttpServletResponse,
    // * globaz.framework.controller.FWDispatcher)
    // */
    // protected void actionChercher(HttpSession session,
    // HttpServletRequest request,
    // HttpServletResponse response,
    // FWDispatcher mainDispatcher) throws ServletException, IOException {
    // // la première fois que cette action est appellée, le viewBean stocké
    // dans la session est une situation
    // // professionnelle.
    // FWViewBeanInterface oldBean = loadViewBean(session);
    //
    // if ((oldBean == null) || !(oldBean instanceof
    // APSituationProfessionnelleViewBean)) {
    // // la page a été rechargée d'une manière ou d'une autre et le bean du
    // droit a ete remplacé
    // // verifions si l'id du droit est deja stocke dans la session.
    // APDroitAPGDTO droit = (APDroitAPGDTO)
    // PRSessionDataContainerHelper.getData(session,
    // PRSessionDataContainerHelper.KEY_DROIT_DTO);
    //
    // if (droit == null) {
    // // nous avons perdu le droit, problème...
    // oldBean.setMsgType(FWViewBeanInterface.ERROR);
    // oldBean.setMessage("droit == null");
    // saveViewBean(oldBean, session);
    // servlet.getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request,
    // response);
    //
    // return; // erreur, quitter le processus.
    // }
    // } else {
    // APDroitAPGDTO dto = (APDroitAPGDTO)
    // (session.getAttribute(PRSessionDataContainerHelper.KEY_DROIT_DTO));
    // PRSessionDataContainerHelper.setData(session,
    // PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);
    // }
    //
    // // cree un viewBean et le stocke dans la session pour recuperer les
    // donnees dans la page rc.
    // APEnfantAPGViewBean viewBean;
    //
    // if ((oldBean != null) && (oldBean instanceof APEnfantAPGViewBean)) {
    // viewBean = (APEnfantAPGViewBean) oldBean;
    // servlet.getServletContext().getRequestDispatcher(getRelativeURL(request,
    // session) + "_rc.jsp").forward(request,
    // response);
    // } else {
    // viewBean = new APEnfantAPGViewBean();
    // initViewBean(viewBean, session);
    // deleguerActionChercher(viewBean, session, request, response,
    // mainDispatcher);
    // }
    // }

    // /**
    // * @see
    // globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
    // * javax.servlet.http.HttpServletRequest,
    // javax.servlet.http.HttpServletResponse,
    // * globaz.framework.controller.FWDispatcher)
    // */
    // protected void actionCustom(HttpSession session, HttpServletRequest
    // request, HttpServletResponse response,
    // FWDispatcher dispatcher) throws ServletException, IOException {
    // String destination = ERROR_PAGE;
    // APEnfantAPGViewBean viewBean;
    //
    // try {
    // viewBean = (APEnfantAPGViewBean) loadViewBean(session);
    // JSPUtils.setBeanProperties(request, viewBean);
    // initViewBean(viewBean, session);
    // } catch (Exception e) {
    // // impossible de trouver le viewBean dans la session. arreter le
    // processus et rediriger vers les erreurs
    // servlet.getServletContext().getRequestDispatcher(destination).forward(request,
    // response);
    //
    // return;
    // }
    //
    // // selectionner l'action en fonction du parametre transmis
    // String action = request.getParameter(USER_ACTION);
    //
    // // les destinations
    // // if (action.endsWith(FIND_TIERS)) {
    // // destination = actionFindIdTiersByNoAvs(session, request, response,
    // dispatcher, viewBean);
    // // } else if (action.endsWith(ETAPE_2)) {
    // // destination = actionEtape2(session, request, response, dispatcher,
    // viewBean);
    // // }
    // //
    // //
    // servlet.getServletContext().getRequestDispatcher(destination).forward(request,
    // response);
    // }

    // /**
    // * @see
    // globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,
    // * javax.servlet.http.HttpServletRequest,
    // javax.servlet.http.HttpServletResponse,
    // * globaz.framework.bean.FWViewBeanInterface)
    // */
    // protected FWViewBeanInterface beforeAfficher(HttpSession session,
    // HttpServletRequest request,
    // HttpServletResponse response,
    // FWViewBeanInterface viewBean) {
    // initViewBean((APEnfantAPGViewBean) viewBean, session);
    //
    // return super.beforeAfficher(session, request, response, viewBean);
    // }

    /**
     * @see globaz.apg.servlet.PRDefaultAction#setTiers(globaz.framework.bean.FWViewBeanInterface, java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    protected void setTiers(FWViewBeanInterface viewBean, String idTiers, String noAVS, String nom, String prenom,
            String sexe, String titre, String idCanton, String idPays) throws Exception {
        APEnfantAPGViewBean enfantAPG = (APEnfantAPGViewBean) viewBean;

        enfantAPG.setNss(noAVS);
        enfantAPG.setNom(nom);
        enfantAPG.setPrenom(prenom);

        // enfantAPG.setSexe(sexe);
        enfantAPG.setIdTiers(idTiers);
    }
}
