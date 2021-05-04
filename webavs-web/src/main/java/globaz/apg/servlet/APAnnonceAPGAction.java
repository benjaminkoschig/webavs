package globaz.apg.servlet;

import ch.globaz.utils.VueGlobaleTiersUtils;
import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.droits.IAPDroitAPG;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.db.annonces.APAnnonceAPGManager;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.enums.APConstantes;
import globaz.apg.pojo.APAnnonceDTO;
import globaz.apg.vb.annonces.APAnnonceAPGListViewBean;
import globaz.apg.vb.annonces.APAnnonceAPGViewBean;
import globaz.apg.vb.annonces.APAnnonceSedexViewBean;
import globaz.apg.vb.droits.APTypePresationDemandeResolver;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author DVH
 */
public class APAnnonceAPGAction extends PRDefaultAction {

    // Création de variables de classe, suite à la revue de code avec SCE du BZ_8135
    private static final String ATTRIB_FOR_MOIS_ANNE_COMPTABLE = "forMoisAnneeComptable";
    private static final String ATTRIB_FOR_TYPE = "forType";
    private static final String ATTRIB_TOTAL_RESTITUTION = "totalRestitutuion";
    private static final String ATTRIB_TOTAL_SANS_RESTITUTION = "totalSansRestitutuion";
    private static final String ATTRIB_VIEWBEAN = "viewBean";
    private static final String FOR_ID_PRESATION = "forIdPrestation";
    private static final String PARAM_ANNONCE_DTO = "loadAPAnnonceDTO";
    private static final String PARAM_FOR_MOIS_ANNEE_COMPTABLE = "forMoisAnneeComptable";
    private static final String PARAM_FOR_NSS = "forNss";
    private static final String PARAM_FOR_TYPE = "forType";
    private static final String PARAM_ID_ANNONCE = "idAnnonce";
    private static final String PARAM_IS_ACTION_CALCULE = "isActionCalculer";
    private static final String PARAM_METHOD = "_method";
    private static final String PARAM_MOIS_ANNEE = "moisAnnee";
    private static final String PARAM_ONLY_NSS = "onlyNss";
    private static final String PARAM_SELECTED_ID = "selectedId";
    private static final String PARAM_TYPE_ANNONCE = "typeAnnonce";

    public APAnnonceAPGAction(FWServlet servlet) {
        super(servlet);
    };

    /**
     * on veut rediriger vers le rc de apannonceAPG en cas d'ajout
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ((getAction() != null) && (getAction().isWellFormed())) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + IAPActions.ACTION_ANNONCEAPG + "."
                    + FWAction.ACTION_CHERCHER;
        }

        return "";
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ((getAction() != null) && (getAction().isWellFormed())) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + IAPActions.ACTION_ANNONCEAPG + "."
                    + FWAction.ACTION_CHERCHER;
        }

        return "";
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ((getAction() != null) && (getAction().isWellFormed())) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + IAPActions.ACTION_ANNONCEAPG + "."
                    + FWAction.ACTION_CHERCHER;
        }

        return "";
    }

    /**
     * redéfinie pour permettre de rediriger vers la bonne page
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        FWAction action = getAction();

        String destination = null;
        String method = request.getParameter(APAnnonceAPGAction.PARAM_METHOD);
        String selectedId = request.getParameter(APAnnonceAPGAction.PARAM_SELECTED_ID);
        String typeAnnonce = request.getParameter(APAnnonceAPGAction.PARAM_TYPE_ANNONCE);
        String onlyNss = request.getParameter(APAnnonceAPGAction.PARAM_ONLY_NSS);
        String idAnnonce = request.getParameter(APAnnonceAPGAction.PARAM_ID_ANNONCE);

        try {
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                if (IAPAnnonce.CS_APGREVISION1999.equals(typeAnnonce)) {
                    action = FWAction
                            .newInstance(IAPActions.ACTION_ANNONCEREVIVION1999 + "." + FWAction.ACTION_NOUVEAU);
                } else if (IAPAnnonce.CS_APGREVISION2005.equals(typeAnnonce)
                        || IAPAnnonce.CS_MATERNITE.equals(typeAnnonce)) {
                    action = FWAction
                            .newInstance(IAPActions.ACTION_ANNONCEREVISION2005 + "." + FWAction.ACTION_NOUVEAU);
                } else if (IAPAnnonce.CS_APGSEDEX.equals(typeAnnonce) || IAPAnnonce.CS_PATERNITE.equals(typeAnnonce)
                        || IAPAnnonce.CS_PROCHE_AIDANT.equals(typeAnnonce)) {
                    action = FWAction.newInstance(IAPActions.ACTION_ANNONCESEDEX + "." + FWAction.ACTION_NOUVEAU);
                } else {
                    throw new Exception("type annonce absent ou non reconnu");
                }
            } else {
                // suivant le type d'annonce, on redirigera vers telle ou telle
                // page

                if (!JadeStringUtil.isIntegerEmpty(idAnnonce)) {
                    selectedId = idAnnonce;
                    APAnnonceAPG annonceAPG = new APAnnonceAPG();
                    annonceAPG.setSession((BSession) mainDispatcher.getSession());
                    annonceAPG.setIdAnnonce(idAnnonce);
                    annonceAPG.retrieve();

                    if (!annonceAPG.isNew()) {
                        typeAnnonce = annonceAPG.getTypeAnnonce();
                    }
                } else if (JadeStringUtil.isIntegerEmpty(typeAnnonce)) {
                    String forIdPrestation = request.getParameter(APAnnonceAPGAction.FOR_ID_PRESATION);

                    if (JadeStringUtil.isIntegerEmpty(forIdPrestation)) {
                        // Problème, impossible de savoir quel type d'annonce il
                        // faut afficher.
                        throw new Exception("Impossible d'afficher l'annonce (type indéfini)");
                    } else {
                        // retrieve de la prestation pour récupérer son type, et
                        // donc celui de l'annonce
                        APPrestation prestation = new APPrestation();
                        prestation.setSession((BSession) mainDispatcher.getSession()); // TODO
                        // recuperer
                        // la
                        // session
                        // autrement
                        prestation.setIdPrestationApg(forIdPrestation);
                        prestation.retrieve();
                        selectedId = prestation.getIdAnnonce();

                        APDroitLAPG droit = new APDroitAPG();
                        // TODO Récupérer la session autrement.
                        droit.setSession((BSession) mainDispatcher.getSession());
                        droit.setIdDroit(prestation.getIdDroit());
                        droit.retrieve();

                        // on met le typeAnnonce à la bonne valeur
                        if (prestation.getNoRevision().equals(IAPDroitAPG.CS_REVISION_APG_1999)) {
                            typeAnnonce = IAPAnnonce.CS_APGREVISION1999;
                        } else if (prestation.getNoRevision().equals(IAPDroitAPG.CS_REVISION_APG_2005)) {
                            typeAnnonce = IAPAnnonce.CS_APGREVISION2005;
                        } else if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())){
                            typeAnnonce = IAPAnnonce.CS_MATERNITE;
                        } else if (IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT.equals(droit.getGenreService())){
                            typeAnnonce = IAPAnnonce.CS_PROCHE_AIDANT;
                        } else {
                            typeAnnonce = IAPAnnonce.CS_PATERNITE;
                        }
                    }
                }

                // on fait l'action en fonction du type d'annonce qu'on veut
                // afficher
                if (IAPAnnonce.CS_MATERNITE.equals(typeAnnonce)) {
                    action = FWAction.newInstance(IAPActions.ACTION_ANNONCEREVISION2005 + ".afficher");
                } else if (IAPAnnonce.CS_APGREVISION1999.equals(typeAnnonce)) {
                    action = FWAction.newInstance(IAPActions.ACTION_ANNONCEREVIVION1999 + ".afficher");
                } else if (IAPAnnonce.CS_APGREVISION2005.equals(typeAnnonce)) {
                    action = FWAction.newInstance(IAPActions.ACTION_ANNONCEREVISION2005 + ".afficher");
                } else if (IAPAnnonce.CS_APGSEDEX.equals(typeAnnonce) || IAPAnnonce.CS_PATERNITE.equals(typeAnnonce)
                        || IAPAnnonce.CS_PROCHE_AIDANT.equals(typeAnnonce)) {
                    action = FWAction.newInstance(IAPActions.ACTION_ANNONCESEDEX + ".afficher");
                } else {
                    // on a un type d'annonce invalide....
                    throw new Exception("type d'annonce invalide");
                }
            }

            APAnnonceAPGViewBean viewBean = (APAnnonceAPGViewBean) FWViewBeanActionFactory.newInstance(action,
                    mainDispatcher.getPrefix());

            // le viewBean sera un annonce APG ou une de ses sous classes
            viewBean.setIdAnnonce(selectedId);
            if (viewBean instanceof APAnnonceSedexViewBean) {
                ((APAnnonceSedexViewBean) viewBean).setOnlyNss(onlyNss);
            }

            /*
             * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            viewBean = (APAnnonceAPGViewBean) beforeAfficher(session, request, response, viewBean);
            viewBean = (APAnnonceAPGViewBean) mainDispatcher.dispatch(viewBean, action);
            viewBean.setTypeAnnonce(typeAnnonce);

            viewBean.setTypeDemande(APTypePresationDemandeResolver.resolveEnumTypePrestation(session));

            if (!JadeStringUtil.isBlank(viewBean.getIdTiers())) {
                VueGlobaleTiersUtils.stockerIdTiersPourVueGlobale(session, viewBean.getIdTiers());
            }

            session.removeAttribute(APAnnonceAPGAction.ATTRIB_VIEWBEAN);
            session.setAttribute(APAnnonceAPGAction.ATTRIB_VIEWBEAN, viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                    destination = getRelativeURLwithoutClassPart(request, session) + action.getClassPart()
                            + "_de.jsp?_valid=fail&_back=sl";
                } else {
                    if (JadeStringUtil.isIntegerEmpty(viewBean.getIdAnnonce())) {
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        viewBean.setMessage(((BSession) viewBean.getISession()).getLabel("ANNONCE_INEXISTANTE"));
                    }

                    destination = getRelativeURLwithoutClassPart(request, session) + action.getClassPart() + "_de.jsp";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String moisAnnee = request.getParameter(APAnnonceAPGAction.PARAM_FOR_MOIS_ANNEE_COMPTABLE);
        String forType = request.getParameter(APAnnonceAPGAction.PARAM_FOR_TYPE);
        String forNss = request.getParameter(APAnnonceAPGAction.PARAM_FOR_NSS);

        // on doit calculer le montant des annonces non-erronées pour les mois
        // et année donnés
        if ("true".equals(request.getParameter(APAnnonceAPGAction.PARAM_IS_ACTION_CALCULE))) {

            if (!JadeStringUtil.isEmpty(APAnnonceAPGAction.PARAM_MOIS_ANNEE)) {

                try {
                    APAnnonceAPGManager annonceManager = new APAnnonceAPGManager();
                    annonceManager.setSession((BSession) mainDispatcher.getSession());
                    annonceManager.setForMoisAnneeComptable(moisAnnee);
                    annonceManager.setForType(forType);
                    annonceManager.setForEtatDifferentDe(IAPAnnonce.CS_ERRONE);
                    annonceManager.setForNss(forNss);
                    annonceManager.setForContenuAnnonce(mainDispatcher.getSession().getCode(IAPAnnonce.CS_RESTITUTION));
                    annonceManager.setIsAnnoncesWithoutIdParent(true);

                    FWCurrency totalRestitutuion = new FWCurrency(annonceManager
                            .getSum(APAnnonceAPG.FIELDNAME_TOTALAPG).toString());

                    request.setAttribute(APAnnonceAPGAction.ATTRIB_TOTAL_RESTITUTION,
                            totalRestitutuion.toStringFormat());

                    annonceManager.setForContenuAnnonce("");
                    annonceManager.setForContenuAnnonceDifferentDe(mainDispatcher.getSession().getCode(
                            IAPAnnonce.CS_RESTITUTION));
                    FWCurrency totalSansRestitutuion = new FWCurrency(annonceManager.getSum(
                            APAnnonceAPG.FIELDNAME_TOTALAPG).toString());

                    request.setAttribute(APAnnonceAPGAction.ATTRIB_TOTAL_SANS_RESTITUTION,
                            totalSansRestitutuion.toStringFormat());

                    request.setAttribute(APAnnonceAPGAction.ATTRIB_FOR_MOIS_ANNE_COMPTABLE, moisAnnee);
                    request.setAttribute(APAnnonceAPGAction.ATTRIB_FOR_TYPE, forType);

                } catch (Exception e) {
                    request.setAttribute("errorMsg", e.getMessage());

                }
            }
        } else {
            // On fait se traitement dans le else car c'est sur qu'il ne fut pas l'exécuter dans l'autre cas

            APAnnonceDTO dto = null;
            FWViewBeanInterface viewBean = this.loadViewBean(session);
            boolean doLoadAPAnnonceDTO = "true".equals(request.getParameter(APAnnonceAPGAction.PARAM_ANNONCE_DTO));
            if (doLoadAPAnnonceDTO) {
                dto = (APAnnonceDTO) session.getAttribute(APConstantes.ANNONCE_DTO.getValue());

                if (dto != null) {
                    request.setAttribute(APAnnonceDTO.PARAMETER_KEY_TYPE, dto.getForType());
                    request.setAttribute(APAnnonceDTO.PARAMETER_KEY_MOIS_ANNEE_COMPTABLE,
                            dto.getForMoisAnneeComptable());
                    request.setAttribute(APAnnonceDTO.PARAMETER_KEY_ETAT, dto.getForEtat());
                    request.setAttribute(APAnnonceDTO.PARAMETER_KEY_BPID, dto.getForBusinessProcessId());
                    request.setAttribute(APAnnonceDTO.PARAMETER_KEY_NSS, dto.getPartialforNss());
                    request.setAttribute(APAnnonceDTO.PARAMETER_KEY_ORDER_BY, dto.getOrderBy());
                    request.setAttribute(APAnnonceDTO.PARAMETER_KEY_FULLNSS, APAnnonceDTO.getForNss());
                }
            }
        }
        if(JadeStringUtil.isBlankOrZero((String) request.getAttribute(APAnnonceDTO.PARAMETER_KEY_TYPE))){
                request.setAttribute(APAnnonceDTO.PARAMETER_KEY_TYPE,forType);
        }

        // ne fait que la redirection
        super.actionChercher(session, request, response, mainDispatcher);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((APAnnonceAPGListViewBean) viewBean).setHierarchicalOrder(true);
        // Lors de l'appui sur le bouton chercher (action lister), on stocke les infos de recherche
        APAnnonceDTO dto = (APAnnonceDTO) session.getAttribute(APConstantes.ANNONCE_DTO.getValue());
        if (dto == null) {
            dto = new APAnnonceDTO();
        }
        dto.setForType(request.getParameter(APAnnonceDTO.PARAMETER_KEY_TYPE));
        dto.setForMoisAnneeComptable(request.getParameter(APAnnonceDTO.PARAMETER_KEY_MOIS_ANNEE_COMPTABLE));
        dto.setForEtat(request.getParameter(APAnnonceDTO.PARAMETER_KEY_ETAT));
        dto.setForBusinessProcessId(request.getParameter(APAnnonceDTO.PARAMETER_KEY_BPID));
        dto.setPartialforNss(request.getParameter(APAnnonceDTO.PARAMETER_KEY_NSS));
        dto.setOrderBy(request.getParameter(APAnnonceDTO.PARAMETER_KEY_ORDER_BY));
        dto.setForNss(request.getParameter(APAnnonceDTO.PARAMETER_KEY_FULLNSS));
        session.setAttribute(APConstantes.ANNONCE_DTO.getValue(), dto);
        return super.beforeLister(session, request, response, viewBean);
    }

    public String choisirType(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        try {
            getAction().changeActionPart(FWAction.ACTION_AFFICHER);

            // il faut forcément un VB en session, alors on en fait un.
            this.saveViewBean(new APAnnonceAPGViewBean(), session);

            return getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }
    }

    public String reenvoyer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        mainDispatcher.dispatch(viewBean, getAction());
        this.saveViewBean(viewBean, request);

        String r = this.getUserActionURL(request, IAPActions.ACTION_ANNONCEAPG, "chercher");
        return r;

    }

}
