/*
 * Créé le 1 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.servlet;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.exceptions.APEmptyIdException;
import globaz.apg.helpers.prestation.APPrestationHelper;
import globaz.apg.utils.APGUtils;
import globaz.apg.vb.droits.APDroitAPGDTO;
import globaz.apg.vb.droits.APSituationProfessionnelleViewBean;
import globaz.apg.vb.prestation.APCalculACORViewBean;
import globaz.apg.vb.prestation.APDeterminerTypeCalculPrestationViewBean;
import globaz.apg.vb.prestation.APPrestationViewBean;
import globaz.apg.vb.prestation.APValidationPrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APPrestationAction extends PRDefaultAction {

    public static final String VERS_ECRAN_CALCUL_ACOR = "actionCalculerACOR&selectedId=";
    private static final String VERS_ECRAN_ERREUR_METIER = "actionRecapitulatif&selectedId=";

    /**
     * Crée une nouvelle instance de la classe APPrestationAction.
     * 
     * @param servlet
     */
    public APPrestationAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Action : apg.prestation.prestation.actionCalculerToutesLesPrestations
     */
    public String actionCalculerToutesLesPrestations(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws Exception {

        String idDroit = resoudreIdDroit(vb, request);
        String genreService = resoudreGenreService(vb, request);
        String typePrestation = resoudreTypePrestation(vb, request);

        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new APEmptyIdException(APDroitLAPG.class, idDroit);
        }


        // on met à jour le dto du droit pour l'affichage.
        APDroitLAPG droit = new APDroitLAPG();
        droit.setISession(mainDispatcher.getSession());
        droit.setIdDroit(idDroit);
        try {
            droit.retrieve();
        } catch (Exception e) {
            // on ne met pas à jour le dto.
        }
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, new APDroitAPGDTO(droit));

        String destination = FWDefaultServletAction.ERROR_PAGE;
        APDeterminerTypeCalculPrestationViewBean typeCalculViewBean = new APDeterminerTypeCalculPrestationViewBean();
        APPrestationViewBean viewBean = null;
        if (vb instanceof APPrestationViewBean) {
            viewBean = (APPrestationViewBean) vb;
        } else {
            viewBean = new APPrestationViewBean();
        }

        typeCalculViewBean.setIdDroit(idDroit);
        typeCalculViewBean.setTypePrestation(typePrestation);
        viewBean.setIdDroit(idDroit);
        viewBean.setGenreService(genreService);

        // 1 - en premier lieu il s'agit de savoir si on les calcul via ACOR ou via la calculateur maison
        getAction().changeActionPart(APPrestationHelper.ACTION_DETERMINER_TYPE_CALCUL_PRESTATIONS);

        typeCalculViewBean = (APDeterminerTypeCalculPrestationViewBean) mainDispatcher.dispatch(typeCalculViewBean,
                getAction());
        if (FWViewBeanInterface.ERROR.equals(typeCalculViewBean.getMsgType())) {
            this.saveViewBean(viewBean, session);
            throw new Exception("Exception thrown during action APPrestationHelper."
                    + APPrestationHelper.ACTION_DETERMINER_TYPE_CALCUL_PRESTATIONS + typeCalculViewBean.getMessage());
        }

        // 2 - calcul des prestations avec la méthode correspondante
        switch (typeCalculViewBean.getTypeCalculPrestation()) {
            case STANDARD:
                // On appel directement la méthode concernée dans la helper.
                if (typePrestation != null && typePrestation.equals(APTypeDePrestation.MATCIAB2.getNomTypePrestation())) {
                    getAction().changeActionPart(APPrestationHelper.ACTION_CALCULER_PRESTATION_MATCIAB2_AVEC_CALCULATEUR_GLOBAZ);
                } else {
                    getAction().changeActionPart(APPrestationHelper.ACTION_CALCULER_PRESTATION_AVEC_CALCULATEUR_GLOBAZ);
                }
                viewBean = (APPrestationViewBean) mainDispatcher.dispatch(viewBean, getAction());
                if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                    this.saveViewBean(viewBean, session);
                    throw new Exception("Exception thrown during action APPrestationHelper."
                            + APPrestationHelper.ACTION_DETERMINER_TYPE_CALCUL_PRESTATIONS + viewBean.getMessage());
                }
                // 3 - Contrôle des plausibilité sur les prestations qui ont étés calculées
                destination = controllerLesPrestation(session, request, response, mainDispatcher, viewBean);
                break;
            case ACOR:
                // on redirige vers la page pour lancer le calcul ACOR
                destination = this.getUserActionURL(
                        request,
                        IAPActions.ACTION_CALCUL_ACOR,
                        APPrestationAction.VERS_ECRAN_CALCUL_ACOR + viewBean.getIdDroit() + "&genreService="
                                + viewBean.getGenreService());
                break;
            default:
                throw new Exception(
                        "APPrestationAction.actionCalculerPrestations() : impossible de déterminer le type de calcul");
        }
        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            throw new Exception("Exception thrown during action APPrestationHelper."
                    + APPrestationHelper.ACTION_DETERMINER_TYPE_CALCUL_PRESTATIONS + viewBean.getMessage());
        }

        return destination;
    }

    /**
     * méthode commune au calcul et au recalcul des prestations
     * 
     * @param request
     * @param session
     * @param mainDispatcher
     * @param fwViewBean
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private String actionDeleguerHelper(HttpServletRequest request, HttpSession session, FWDispatcher mainDispatcher,
            FWViewBeanInterface fwViewBean) throws ServletException, IOException,
            JadeApplicationServiceNotAvailableException {
        APPrestationViewBean viewBean = new APPrestationViewBean();

        viewBean.setIdDroit(getSelectedId(request));
        viewBean.setGenreService(request.getParameter("genreService"));
        viewBean.setDroitAcquis(request.getParameter("droitAcquis"));
        viewBean.setEtat(request.getParameter("etat"));

        if (FWAction.newInstance(getUserAction(request)).getActionPart().equals("actionRecalculerPrestations")) {
            viewBean.setIdPrestationApg(request.getParameter("forIdPrestation"));
        }

        viewBean = (APPrestationViewBean) mainDispatcher.dispatch(viewBean, getAction());

        if (viewBean.isErreurCalcul()) {
            viewBean.setErreurCalcul(false);
            this.saveViewBean(viewBean, session);

            if (viewBean.isCalculACOR()) {
                viewBean.setCalculACOR(false);

                return this.getUserActionURL(
                        request,
                        IAPActions.ACTION_CALCUL_ACOR,
                        APPrestationAction.VERS_ECRAN_CALCUL_ACOR + viewBean.getIdDroit() + "&genreService="
                                + viewBean.getGenreService());
            } else {
                return this.getUserActionURL(request, IAPActions.ACTION_DROIT_LAPG,
                        APPrestationAction.VERS_ECRAN_ERREUR_METIER + viewBean.getIdDroit() + "&genreService="
                                + viewBean.getGenreService());
            }
        }

        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            this.saveViewBean(viewBean, session);
            return FWDefaultServletAction.ERROR_PAGE;
        } else {
            this.saveViewBean(viewBean, session);
            return this.getUserActionURL(request, IAPActions.ACTION_PRESTATIONS,
                    IAPActions.ACTION_CONTROLE_PRESTATION_CALCULEES + "&forIdDroit=" + getSelectedId(request));
        }
    }

    /**
     * action appellée depuis l'écran de calcul des prestations avec ACOR.
     * <p>
     * Cette action reçoit en paramètre un APCalculACORViewBean contenant le fichier annoncePay généré par ACOR.
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
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String actionImporterPrestationsDepuisACOR(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        ((APCalculACORViewBean) viewBean).setIdDroit(getSelectedId(request));

        // getAction().setRight(FWSecureConstants.ADD);

        FWAction action = getAction();

        // il faut toujours calculer toutes les prestations
        //
        // // Si on veut calculer toutes les prestations, on change l'actionPart
        // if(((APCalculACORViewBean)
        // viewBean).getAppelant()==APCalculACORViewBean.CALCULER_TOUTES_PREST_ACOR){
        try {
            action.changeActionPart(APPrestationHelper.IMPORTER_TOUTES_LES_PRESTATIONS_ACOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // }

        viewBean = mainDispatcher.dispatch(viewBean, action);

        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            this.saveViewBean(viewBean, session);

            return FWDefaultServletAction.ERROR_PAGE;
        } else {
            return controllerLesPrestation(session, request, response, mainDispatcher, viewBean);
            // return this.getUserActionURL(request, IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT,
            // "chercher&forIdDroit=" + this.getSelectedId(request) + "&checkRepartitionsDroit=true");
        }
    }

    // public String actionAfficherErreurValidationPrestation(HttpSession session, HttpServletRequest request,
    // HttpServletResponse response,
    // FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws Exception {
    //
    // if (!(vb instanceof APValidationPrestationViewBean)) {
    // throw new APWrongViewBeanTypeException(
    // "Wrong viewBean type for action : actionAfficherErreurValidationPrestation. It must be an instance of : "
    // + APValidationPrestationViewBean.class.getName());
    // }
    // APValidationPrestationViewBean viewBean = (APValidationPrestationViewBean) vb;
    // if (viewBean == null) {
    // throw new
    // APViewBeanNotFoundException("ViewBean not found for action : actionAfficherErreurValidationPrestation");
    // }
    // return "";
    // }

    private String controllerLesPrestation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws Exception {

        if (!(vb instanceof APPrestationViewBean)) {
            throw new Exception(
                    "actionControllerPrestation wrong viewBean received. It must be an instance of class APPrestationViewBean");
        }
        String idDroit = ((APPrestationViewBean) vb).getIdDroit();
        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new APEmptyIdException(APDroitLAPG.class, idDroit);
        }

        APValidationPrestationViewBean newViewBean = new APValidationPrestationViewBean();
        newViewBean.setIdDroit(idDroit);
        getAction().changeActionPart(APPrestationHelper.ACTION_CONTROLE_PRESTATION_CALCULEES);
        newViewBean = (APValidationPrestationViewBean) mainDispatcher.dispatch(newViewBean, getAction());

        if (FWViewBeanInterface.ERROR.equals(newViewBean.getMsgType())) {
            this.saveViewBean(newViewBean, session);
            throw new Exception("Exception thrown during action APPrestationAction.controllerLesPrestation : "
                    + newViewBean.getMessage());
        } else if (newViewBean.hasValidationError() || newViewBean.hasErreursValidationPeriodes()) {
            this.saveViewBean(newViewBean, session);
            getAction().changeActionPart("validationPrestations");
            return "/apgRoot/prestation/validationPrestations_de.jsp";
        } else {
            if(APGUtils.isTypeAllocationPandemie(newViewBean.getGenreService())) {
                getAction().changeActionPart("passerDroitValider");
                mainDispatcher.dispatch(newViewBean, getAction());
            }
            return this.getUserActionURL(request, IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT,
                    "chercher&forIdDroit=" + idDroit + "&checkRepartitionsDroit=true");
        }
    }

    private String resoudreGenreService(FWViewBeanInterface viewBean, HttpServletRequest request) {
        String genreService = null;
        // Ce cas se présente lordqu'on arrive depuis le calcul des prestations d'un droit mat
        if (viewBean instanceof APSituationProfessionnelleViewBean) {
            genreService = ((APSituationProfessionnelleViewBean) viewBean).getDroitDTO().getGenreService();
        }

        // Ce cas lorsqu'on arrive depuis un droit APG
        else if (viewBean instanceof APPrestationViewBean) {
            genreService = ((APPrestationViewBean) viewBean).getGenreService();
        }

        // Ce cas peut également se présenter lorsqu'on arrive depuis le menu d'option des droits et qu'on fait l'option
        // 'calculer prestations'
        if (JadeStringUtil.isBlankOrZero(genreService)) {
            genreService = request.getParameter("genreService");
        }
        return genreService;
    }

    private String resoudreIdDroit(FWViewBeanInterface viewBean, HttpServletRequest request) {
        String idDroit = null;

        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            idDroit = request.getParameter("selectedId");
        }

        // TODO LGA a valider...
        // Ce cas lorsqu'on arrive depuis un droit APG
        // else if (viewBean instanceof APPrestationViewBean) {
        // idDroit = ((APPrestationViewBean) viewBean).getIdDroit();
        // }

        // Ce cas peut également se présenter lorsqu'on arrive depuis le menu d'option des droits et qu'on fait l'option
        // 'calculer prestations'
        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            idDroit = request.getParameter("idDroit");
        }

        return idDroit;
    }

    private String resoudreTypePrestation(FWViewBeanInterface viewBean, HttpServletRequest request) {
        // Ce cas peut se présenter lorsqu'on arrive depuis le menu d'option des droits et qu'on fait l'option
        // 'calculer prestations'
        String typePrestation = request.getParameter("typePrestation");

        return typePrestation;
    }
};
