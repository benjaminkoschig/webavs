/*
 * Créé le 27 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.servlet;

import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitPandemie;
import globaz.apg.util.TypePrestation;
import globaz.apg.vb.droits.*;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public abstract class APAbstractDroitDTOAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     */
    public static final String PARAM_ID_DROIT = "idDroitLAPGBackup";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APAbstractDroitDTOAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public APAbstractDroitDTOAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        if (loadDTO(session, request, mainDispatcher) == null) {
            forwardToErrorPage(loadViewBean(session), request, response);
        } else {
            deleguerActionAfficher(session, request, response, mainDispatcher);
        }
    }

    /**
     * Recherche dans la session http s'il n'y aurait pas un viewbean dérivé d'APDroitLAPG et si oui crée un APDroitDTO
     * en se basant dessus, sinon recherche un dto déjà existant et s'il n'y en a pas, ajoute une erreur dans la session
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected final void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        APDroitDTO dto = loadDTO(session, request, mainDispatcher);

        if (dto == null) {
            forwardToErrorPage(loadViewBean(session), request, response);
        } else {
            FWViewBeanInterface viewBean = loadViewBean(session);

            // cree un viewBean et le stocke dans la requete pour recuperer les
            // donnees dans la page rc.
            if ((viewBean == null) || !(getViewBeanClass().isInstance(viewBean))) {
                try {
                    FWViewBeanInterface nouveauViewBean = (FWViewBeanInterface) getViewBeanClass().newInstance();

                    // récupérer les erreurs si nécessaire
                    if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                        nouveauViewBean.setMsgType(viewBean.getMsgType());
                        nouveauViewBean.setMessage(viewBean.getMessage());
                    }

                    viewBean = nouveauViewBean;
                } catch (Exception e) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage("internal");
                    saveViewBean(viewBean, session);
                    servlet.getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);

                    return; // erreur, quitter le processus.
                }
            }

            initViewBean(viewBean, dto, session);
            deleguerActionChercher(viewBean, session, request, response, mainDispatcher);
        }
    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    protected void actionChercherSansForward(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        APDroitDTO dto = loadDTO(session);
        FWViewBeanInterface viewBean = loadViewBean(session);

        // cree un viewBean et le stocke dans la session pour recuperer les
        // donnees dans la page rc.
        if ((viewBean != null) && (getViewBeanClass().isInstance(viewBean))) {
            initViewBean(viewBean, dto, session);
        } else {
            try {
                FWViewBeanInterface nouveauViewBean = (FWViewBeanInterface) getViewBeanClass().newInstance();

                // récupérer les erreurs si nécessaire
                if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                    nouveauViewBean.setMsgType(viewBean.getMsgType());
                    nouveauViewBean.setMessage(viewBean.getMessage());
                }

                viewBean = nouveauViewBean;
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage("internal");
                saveViewBean(viewBean, session);
                servlet.getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);

                return; // erreur, quitter le processus.
            }

            initViewBean(viewBean, dto, session);
        }
    }

    /**
     * Cette méthode a le même comportement que la méthode parente sauf qu'en plus, elle initialise le viewBean au moyen
     * du DTO.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param dispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected final void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        if (loadDTO(session, request, dispatcher) == null) {
            forwardToErrorPage(loadViewBean(session), request, response);
        } else {
            initViewBean(loadViewBean(session), loadDTO(session), session);
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected final FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return deleguerBeforeAfficher(session, request, response, initViewBean(viewBean, loadDTO(session), session));
    }

    /**
     * Crée le dto à partir de l'identifiant transmis. Le helper DOIT etendre APAbstractDroitHelper pour que cette
     * méthode fonctionne, redéfinir sinon.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param idDroitDTOBackup
     *            DOCUMENT ME!
     * @param dispatcher
     *            DOCUMENT ME!
     * 
     * @return un droit dto ou null si erreur
     */
    protected APDroitDTO createDroitDTO(HttpSession session, HttpServletRequest request, String idDroitDTOBackup,
            FWDispatcher dispatcher) {
        FWViewBeanInterface droit = null;
        FWAction action = null;

        if (isTypeAPG(session)) {
            droit = new APDroitAPGPViewBean();
            action = FWAction.newInstance(IAPActions.ACTION_SAISIE_CARTE_APG + "." + FWAction.ACTION_AFFICHER);
        } else if (isTypeMAT(session)) {
            droit = new APDroitMatPViewBean();
            action = FWAction.newInstance(IAPActions.ACTION_SAISIE_CARTE_AMAT + "." + FWAction.ACTION_AFFICHER);
        } else if (isTypePan(session)) {
            droit = new APDroitPanViewBean();
            action = FWAction.newInstance(IAPActions.ACTION_SAISIE_CARTE_PAN + "." + FWAction.ACTION_AFFICHER);
        }

        try {
            dispatcher.dispatch(droit, action);

            if (isTypeAPG(session)) {
                return new APDroitAPGDTO((APDroitAPG) droit);
            } else if (isTypeMAT(session)) {
                return new APDroitDTO((APDroitLAPG) droit);
            } else if (isTypePan(session)){
                return new APDroitPanDTO((APDroitPandemie) droit);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * La méthode action afficher est necessairement finale pour empecher de rater le dto, redefinir cette méthode pour
     * implanter un comportement specifique de l'action afficher.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    protected void deleguerActionAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionAfficher(session, request, response, mainDispatcher);
    }

    /**
     * La méthode before afficher est necessairement finale pour empecher de rater le dto, redefinir cette méthode pour
     * implanter un comportement specifique de before afficher.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected FWViewBeanInterface deleguerBeforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return super.beforeAfficher(session, request, response, viewBean);
    }

    private void forwardToErrorPage(FWViewBeanInterface viewBean, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        if (viewBean == null) {
            try {
                viewBean = (FWViewBeanInterface) getViewBeanClass().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (viewBean != null) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage("dto == null");
        }

        forward(ERROR_PAGE, request, response);
    }

    /**
     * Implémenter cette méthode pour retourner la classe du viewBean devant se trouver dans la session durant le cours
     * normal de cdette action.
     * 
     * @return la valeur courante de l'attribut view bean class
     */
    protected abstract Class getViewBeanClass();

    /**
     * Redéfinir cette méthode pour effectuer les opérations d'initialisation du viewBean en fonction du droitDTO.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param droitDTO
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected FWViewBeanInterface initViewBean(FWViewBeanInterface viewBean, APDroitDTO droitDTO, HttpSession session) {
        return viewBean;
    }

    /**
     * getter pour l'attribut type APG
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut type APG
     */
    protected boolean isTypeAPG(HttpSession session) {
        return TypePrestation.TYPE_APG.equals(TypePrestation
                .typePrestationInstanceForCS((String) PRSessionDataContainerHelper.getData(session,
                        PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)));
    }

    /**
     * getter pour l'attribut type APG
     *
     * @param session
     *            DOCUMENT ME!
     *
     * @return la valeur courante de l'attribut type APG
     */
    protected boolean isTypeMAT(HttpSession session) {
        return TypePrestation.TYPE_MATERNITE.equals(TypePrestation
                .typePrestationInstanceForCS((String) PRSessionDataContainerHelper.getData(session,
                        PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)));
    }

    /**
     * getter pour l'attribut type APG
     *
     * @param session
     *            DOCUMENT ME!
     *
     * @return la valeur courante de l'attribut type APG
     */
    protected boolean isTypePan(HttpSession session) {
        return TypePrestation.TYPE_PANDEMIE.equals(TypePrestation
                .typePrestationInstanceForCS((String) PRSessionDataContainerHelper.getData(session,
                        PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)));
    }

    /**
     * Charge le dto depuis la session.
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @return une instance de DTO ou null si le dto est perdu...
     */
    protected APDroitDTO loadDTO(HttpSession session) {
        return (APDroitDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO);
    }

    // Charge le dto depuis la session ou en crée un nouveau si nécessaire.
    private APDroitDTO loadDTO(HttpSession session, HttpServletRequest request, FWDispatcher dispatcher) {
        APDroitDTO dto = loadDTO(session);
        String idDroit = request.getParameter(PARAM_ID_DROIT);

        if ((dto == null) || JadeStringUtil.isIntegerEmpty(idDroit)) {
            // TODO: lancer une exception
        }

        if (!idDroit.equals(dto.getIdDroit())) {
            dto = createDroitDTO(session, request, idDroit, dispatcher);
            saveDTO(dto, session);
        }

        return dto;
    }

    /**
     * sauve un viewBean dans la session http sous le nom standard.
     * 
     * @param dto
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     */
    protected void saveDTO(APDroitDTO dto, HttpSession session) {
        session.setAttribute(PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);
    }

    /**
     * @see globaz.apg.servlet.APAbstractDroitAction#setTiers(globaz.framework.bean.FWViewBeanInterface,
     *      java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    protected void setTiers(FWViewBeanInterface viewBean, String idTiers, String noAVS, String nom, String prenom,
            String sexe, String titre, String idCanton, String idPays) throws Exception {
    }
}
