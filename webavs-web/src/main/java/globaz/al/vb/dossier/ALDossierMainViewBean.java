package globaz.al.vb.dossier;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.external.IntRole;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import ch.globaz.al.business.adapters.prestation.PrestationHolder;
import ch.globaz.al.business.adapters.prestation.PrestationHolder.EnteteAndPrestationAdapter;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstCalcul;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierActifComplexModel;
import ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EnteteAndDetailPrestationComplexSearchModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.naos.business.model.AffiliationSearchSimpleModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModelSearch;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.osiris.exception.OsirisException;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * ViewBean gérant le modèle représentant un dossier complet ainsi qu'une liste de droits attachés à ce dossier.
 * Embarque aussi le calcul des droits.
 * 
 * @author GMO
 * 
 */
public class ALDossierMainViewBean extends BJadePersistentObjectViewBean {

    private static final int LINE_HEIGHT_MSG_POPUP = 70;
    private static final String URL_ADD_PAIEMENT_TIERS = "/pyxis?userAction=pyxis.adressepaiement.avoirPaiement.afficher&_method=add&back=_sl&idTiers=";

    private String addWarnings = "";
    /**
     * Structure contenant les informations de l'affiliation affichée à l'écran
     */
    private AssuranceInfo affiliation;

    /**
     * Map contenant les cs compléments activité
     */
    private HashMap<String, String> allCsComplActivite = null;

    /**
     * Map contenant les cs des type de lien pour la copie des dossiers
     */
    private HashMap<String, String> allCsTypeLien = null;
    /**
     * Témoin indiquant si une erreur a eu lieu dans le calcul
     */
    private boolean calculError = false;

    private String calculErrorMessage = null;

    private boolean compteAnnexeAffError = false;

    private boolean compteAnnexeAllError = false;

    /**
     * Date à laquelle les droits sont calculés, par défaut : date du jour
     */
    private String dateCalcul = JadeDateUtil.getGlobazFormattedDate(new Date());

    /**
     * Modèle conteneur du dossier agricole complet géré dans l'écran correspondant
     */
    private DossierAgricoleComplexModel dossierAgricoleComplexModel;

    /**
     * Modèle conteneur du dossier complet géré dans l'écran correspondant
     */
    private DossierComplexModel dossierComplexModel;

    /**
     * La liste des droits calculés à la date définie (dateCalcul)
     */
    private List<CalculBusinessModel> droitsList = new ArrayList<>();

    /**
     * Représente la valeur de l'état du dossier à l'entrée dans l'écran
     */
    private String etatDossierAvantModification = null;
    /**
     * Représente la valeur du bénéficiaire à l'entrée dans l'écran
     */
    private String beneficiaireAvantModification = null;

    /**
     * Id du compte annexe lié à l'affilié
     */
    private String idCompteAnnexeAffilie = null;

    /**
     * Id du compte annexe éventuel lié à l'allocataire (dossier paiement direct)
     */
    private String idCompteAnnexeAllocataire = null;

    /**
     * Contient les affiliations pour le numéro d'affilié si plusieurs sous la forme : n° : début affiliation - fin
     * affiliation
     */
    private ArrayList listAffiliation = new ArrayList();

    /**
     * Le montant total des droits calculés
     */
    private String montantTotal = null;

    /**
     * Indique le nombre de dossiers liés à celui-ci
     */
    private int nbDossiersLies = 0;

    /**
     * Modèle conteneur d'une composition {@link EntetePrestationModel} avec {@link DetailPrestationModel}
     */
    private PrestationHolder prestationHolder = new PrestationHolder();

    /**
     * Indique le type de l'affilié, champ ici car obtenu après utilisation service affiliation et pas dans
     * AssuranceInfo
     */
    private String typeAffilie = null;

    /**
     * Solde du ou des comptes annexes de l'allocataire
     */
    private BigDecimal soldeComptesAnnexes = BigDecimal.ZERO;

    /**
     * Constructeur du viewBean, initialise le dossier avec les valeurs par défaut
     * 
     * @throws Exception
     */
    public ALDossierMainViewBean() throws Exception {
        super();
        dossierComplexModel = new DossierComplexModel();
        dossierComplexModel = ALServiceLocator.getDossierComplexModelService().initModel(dossierComplexModel);
        affiliation = new AssuranceInfo();
        affiliation.setDesignation("");
        affiliation.setDateDebutCotisation("");
        affiliation.setDateFinCotisation("");
        affiliation.setCouvert(Boolean.FALSE);

        dossierAgricoleComplexModel = new DossierAgricoleComplexModel();
    }

    /**
     * Constructeur du viewBean
     * 
     * @param _dossierComplexModel
     *            modèle représentant le dossier complet
     */
    public ALDossierMainViewBean(DossierComplexModel _dossierComplexModel) {
        super();
        dossierComplexModel = _dossierComplexModel;
        affiliation = new AssuranceInfo();

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        dossierComplexModel = ALServiceLocator.getDossierBusinessService().createDossier(dossierComplexModel,
                dossierAgricoleComplexModel);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        dossierComplexModel = ALServiceLocator.getDossierBusinessService().deleteDossier(dossierComplexModel);

    }

    public String getAddWarnings() {
        return addWarnings;
    }

    /**
     * @return affiliation structure contenant les infos de l'affiliation
     */
    public AssuranceInfo getAffiliation() {
        return affiliation;
    }

    public String getAffilieWarnsError() {
        String html = "";
        if (compteAnnexeAffError || (affiliation.getWarningsContainer().size() > 0)) {
            html += "<a href='#' class='warningLink info_bulle'>";
        }
        if (compteAnnexeAffError) {
            html += "<span>" + getSession().getLabel("RETRIEVE_CA_ERROR_MSG") + "</span>";
        }

        if (affiliation.getWarningsContainer().size() > 0) {
            for (String warnAff : affiliation.getWarningsContainer()) {
                html += "<span>" + JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(), warnAff)
                        + "</span>";
            }
        }

        if (!JadeStringUtil.isBlankOrZero(html)) {
            html += "</a>";
        }
        return html;
    }

    public HashMap<String, String> getAllCsComplActivite() {
        return allCsComplActivite;
    }

    public HashMap<String, String> getAllCsTypeLien() {
        return allCsTypeLien;
    }

    public String getCalculErrorMessage() {
        return calculErrorMessage;
    }

    /**
     * @return dateCalcul la date à laquelle les droits sont calculés
     */
    public String getDateCalcul() {
        return dateCalcul;
    }

    public DossierAgricoleComplexModel getDossierAgricoleComplexModel() {
        return dossierAgricoleComplexModel;
    }

    /**
     * @return dossierComplexModel le modèle représentant le dossier complet
     */
    public DossierComplexModel getDossierComplexModel() {
        return dossierComplexModel;
    }

    /**
     * Vérifie si l'allocataire du dossier est lié à plusieurs dossiers pour le même affilié.
     * 
     * @return Code HTML permettant d'afficher une infobulle
     */
    public String getDossierListHTMLWarnings() {

        if (JadeStringUtil.isBlankOrZero(dossierComplexModel.getAllocataireComplexModel()
                .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel())
                || JadeStringUtil.isBlankOrZero(dossierComplexModel.getDossierModel().getNumeroAffilie())) {
            return "";
        } else {

            try {

                JadeAbstractModel[] idsDossier = ALServiceLocator.getDossierBusinessService().getIdDossiersActifs(
                        dossierComplexModel.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                                .getPersonneEtendue().getNumAvsActuel(),
                        dossierComplexModel.getDossierModel().getNumeroAffilie());

                if (idsDossier.length > 1) {
                    StringBuffer str = new StringBuffer("<a href='#' class='warningLink info_bulle'><span>");
                    str.append(JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                            "al.dossier.dossierModel.idAllocataire.businessIntegrity.severalDossier"));
                    str.append("<br />(");

                    for (int i = 0; i < idsDossier.length; i++) {
                        str.append(((DossierActifComplexModel) idsDossier[i]).getIdDossier());
                        if (i < (idsDossier.length - 1)) {
                            str.append(", ");
                        }
                    }

                    str.append(")</span></a>");
                    return str.toString();

                } else {
                    return "";
                }
            } catch (Exception e) {
                return "<a href='#' class='warningLink info_bulle'><span>Une erreur s'est produite pendant la vérification de la présence de dossiers pour cet allocataire : "
                        + e.getMessage() + "</span></a>";
            }
        }
    }

    /**
     * Retourne la description des différents avertissements formatés en HTML
     * 
     * @param id
     *            l'id du droit
     * @return la description formattée en HTML
     */
    public String getDroitHTMLWarnings(String id) {
        StringBuffer buffer = new StringBuffer();
        for (CalculBusinessModel line : droitsList) {
            if (id.equals(line.getDroit().getId())) {
                if (line.getMessagesWarning().size() > 0) {
                    buffer.append("<span>");

                    for (String message : line.getMessagesWarning()) {
                        buffer.append(message).append("<br />");
                    }
                    buffer.append("</span>");
                }

            }
        }
        return buffer.toString();

    }

    public List<CalculBusinessModel> getDroitsList() {
        return droitsList;
    }

    /**
     * @return droitsList liste des droits calculés à la date donnée
     * @throws ALCalculException
     *             {"ENF-12": {0:[CalculBusinessModel,CalculBusinessModel],1:[CalculBusinessModel]},"FORM-13":{0:[-],1[
     *             CalculBusinessModel]}}
     */
    public Map<String, Map<String, ArrayList<CalculBusinessModel>>> getDroitsListExtended() throws ALCalculException {

        Map<String, ArrayList<CalculBusinessModel>> droitsParAllocEnfant = new HashMap<String, ArrayList<CalculBusinessModel>>();

        Map<String, Map<String, ArrayList<CalculBusinessModel>>> listDroits = new HashMap<String, Map<String, ArrayList<CalculBusinessModel>>>();

        // parcours du résultat calcul et construction d'une map par enfant
        for (CalculBusinessModel line : droitsList) {
            String idEnfant = JadeStringUtil.isBlankOrZero(line.getDroit().getEnfantComplexModel().getId()) ? "0"
                    : line.getDroit().getEnfantComplexModel().getId();
            String cleAllocEnfant = line.getDroit().getDroitModel().getTypeDroit() + "-" + idEnfant;
            if (droitsParAllocEnfant.containsKey(cleAllocEnfant)) {
                droitsParAllocEnfant.get(cleAllocEnfant).add(line);
            } else {
                ArrayList<CalculBusinessModel> newList = new ArrayList<CalculBusinessModel>();
                newList.add(line);
                droitsParAllocEnfant.put(cleAllocEnfant, newList);
            }
        }
        // pour chaque liste des droits par enfant
        for (ArrayList<CalculBusinessModel> listeParAllocEnfant : droitsParAllocEnfant.values()) {
            String idEnfant = JadeStringUtil.isBlankOrZero(listeParAllocEnfant.get(0).getDroit()
                    .getEnfantComplexModel().getId()) ? "0" : listeParAllocEnfant.get(0).getDroit()
                    .getEnfantComplexModel().getId();

            String cleAllocEnfant = listeParAllocEnfant.get(0).getDroit().getDroitModel().getTypeDroit() + "-"
                    + idEnfant;

            ArrayList<CalculBusinessModel> droitsActifsAllocEnfant = new ArrayList<CalculBusinessModel>();
            ArrayList<CalculBusinessModel> droitsInactifsAllocEnfant = new ArrayList<CalculBusinessModel>();

            // on itére sur les droits par enfant
            for (int i = 0; i < listeParAllocEnfant.size(); i++) {

                if (!(listeParAllocEnfant.get(i).getDroit().getDroitModel().getTypeDroit() + "-" + idEnfant)
                        .equals(cleAllocEnfant)) {
                    throw new ALCalculException("Problème pour afficher le résultat du calcul:getDroitsList");
                }

                if (listeParAllocEnfant.get(i).isActif()) {
                    droitsActifsAllocEnfant.add(listeParAllocEnfant.get(i));

                } else {
                    droitsInactifsAllocEnfant.add(listeParAllocEnfant.get(i));
                }

            }

            listDroits.put(cleAllocEnfant, new HashMap<String, ArrayList<CalculBusinessModel>>());
            listDroits.get(cleAllocEnfant).put("1", droitsActifsAllocEnfant);
            listDroits.get(cleAllocEnfant).put("0", droitsInactifsAllocEnfant);

        }

        return listDroits;
    }

    /**
     * Retourne la description des différents avertissements formatés en HTML
     * 
     * @param idx
     *            l'index du droit
     * @return la description formattée en HTML
     */
    public String getDroitsListHTMLWarnings(int idx) {
        CalculBusinessModel line = droitsList.get(idx);
        StringBuffer buffer = new StringBuffer();

        if (line.getMessagesWarning().size() > 0) {
            buffer.append("<span>");

            for (String message : line.getMessagesWarning()) {
                buffer.append(message).append("<br />");
            }
            buffer.append("</span>");
        }
        return buffer.toString();
    }

    /**
     * Indique la valeur du paramètre vue droits
     * 
     * @return
     */
    public String getDroitsViewMode() {

        ParameterModel param = new ParameterModel();
        try {
            param = ParamServiceLocator.getParameterModelService().getParameterByName(ALConstParametres.APPNAME,
                    ALConstParametres.DROITS_VIEW_EXTENDED, JadeDateUtil.getGlobazFormattedDate(new Date()));
        } catch (Exception e) {
            return "0";

        }

        return param.getValeurAlphaParametre();
    }

    public List<EnteteAndPrestationAdapter> getEntetesPrestations() {
        return prestationHolder.getPrestationsAdapter();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return dossierComplexModel.getId();
    }

    public String getIdCompteAnnexeAffilie() {
        return idCompteAnnexeAffilie;
    }

    public String getIdCompteAnnexeAllocataire() {
        return idCompteAnnexeAllocataire;
    }

    public String getInfosBeneficiaire() {
        String infos = "";
        TiersSimpleModel simpleTiersSimpleModel = dossierComplexModel.getTiersBeneficiaireModel();
        if ((simpleTiersSimpleModel != null) && !JadeStringUtil.isEmpty(simpleTiersSimpleModel.getIdTiers())) {
            infos = simpleTiersSimpleModel.getDesignation1() + " " + simpleTiersSimpleModel.getDesignation2();
        }
        return infos;
    }

    public ArrayList getListAffiliation() {
        return listAffiliation;
    }

    /**
     * @return montantTotal
     */
    public String getMontantTotal() {
        return montantTotal;
    }

    public int getNbDossiersLies() {
        return nbDossiersLies;
    }

    /**
     * Méthode retournant le mode de paiement du dossier en fonction du tiers bénéficiaire défini
     * 
     * @return <ul>
     *         <li><code>ALCSDossier.PAIEMENT_DIRECT</code></li>
     *         <li><code>ALCSDossier.PAIEMENT_INDIRECT</code></li>
     *         <li><code>ALCSDossier.PAIEMENT_TIERS</code></li>
     *         </ul>
     * 
     */
    public String getPaiementMode() {

        String idTiersBeneficiaire = dossierComplexModel.getDossierModel().getIdTiersBeneficiaire();
        String idTiersAllocataire = dossierComplexModel.getAllocataireComplexModel().getAllocataireModel()
                .getIdTiersAllocataire();

        if (dossierComplexModel.isNew()) {

            try {
                String paiementdirect = (ParamServiceLocator.getParameterModelService().getParameterByName(
                        ALConstParametres.APPNAME, ALConstParametres.MODE_PAIEMENT_DIRECT, dateCalcul))
                        .getValeurAlphaParametre();

                if ("true".equals(paiementdirect)) {
                    dossierComplexModel.getDossierModel().setIdTiersBeneficiaire(idTiersAllocataire);

                    return ALCSDossier.PAIEMENT_DIRECT;

                } else {
                    return ALCSDossier.PAIEMENT_INDIRECT;
                }
            } catch (Exception e) {
                return null;
            }

        }

        if (idTiersBeneficiaire.equals(idTiersAllocataire)) {
            return ALCSDossier.PAIEMENT_DIRECT;
        } else if (JadeStringUtil.isBlankOrZero(idTiersBeneficiaire)) {
            return ALCSDossier.PAIEMENT_INDIRECT;
        } else {
            return ALCSDossier.PAIEMENT_TIERS;
        }

    }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (dossierComplexModel != null) && !dossierComplexModel.isNew() ? new BSpy(dossierComplexModel.getSpy())
                : new BSpy(getSession());

    }

    /**
     * @return le type d'affilié (maison mère, succursale ou normal)
     */
    public String getTypeAffilie() {
        return typeAffilie;
    }

    private void handleWarningsForPopup() {

        // Traitement des avertissements en session
        JadeBusinessMessage[] messages = (JadeBusinessMessage[]) getSession().getAttribute("addWarnings");
        if (messages != null) {

            StringBuffer strTextErrors = new StringBuffer();

            String typePopup = "simple";
            int height = 0;
            String urlAdrPaiement = ALDossierMainViewBean.URL_ADD_PAIEMENT_TIERS;
            String idTiersBenef = dossierComplexModel.getTiersBeneficiaireModel().getIdTiers();
            urlAdrPaiement = urlAdrPaiement.concat(idTiersBenef);

            for (int i = 0; i < messages.length; i++) {
                height += ALDossierMainViewBean.LINE_HEIGHT_MSG_POPUP;
                String messageId = messages[i].getMessageId();

                if (i == 0) {
                    strTextErrors.append("[\"");
                }

                strTextErrors.append(StringEscapeUtils.escapeJavaScript(JadeI18n.getInstance().getMessage(
                        getSession().getUserInfo().getLanguage(), messages[i].getMessageId())));

                if ((i + 1) == messages.length) {
                    strTextErrors.append("\"]");
                } else {
                    strTextErrors.append("\",\"");
                }

                if (messageId.contains("popup-redirect")) {
                    typePopup = "redirect";
                }

            }

            String strHeight = Integer.toString(height);
            String json = "{\"type\":\"";
            json = json.concat(typePopup).concat("\",\"options\":{\"title\":\"Avertissement\",\"text\":");
            json = json.concat(strTextErrors.toString());
            json = json.concat(",\"iconcss\":\"warningIcon\",\"url\":\"");
            json = json.concat(urlAdrPaiement);
            json = json.concat("\",\"height\":").concat(strHeight).concat("},\"buttons\":[\"Compléter\",\"Valider\"]}");

            setAddWarnings(json);

            getSession().removeAttribute("addWarnings");
        }

    }

    public boolean hasAnnonceRafam() throws Exception {
        if (dossierComplexModel.isNew()) {
            return false;
        } else {
            return ALServiceLocator.getDossierBusinessService().hasSentAnnonces(getId());
        }
    }

    public boolean isAffilieLocked() {
        ParameterModel param = new ParameterModel();
        try {
            param = ParamServiceLocator.getParameterModelService().getParameterByName(ALConstParametres.APPNAME,
                    ALConstParametres.LOCK_DOSSIER_AFFILIE, JadeDateUtil.getGlobazFormattedDate(new Date()));
        } catch (Exception e) {
            // si le paramètre n'est pas trouvé, le champ est verrouillé si prestations existent
            if (prestationHolder.getSize() > 0) {
                return true;
            } else {
                return false;
            }

        }

        if ("false".equals(param.getValeurAlphaParametre())) {
            return false;
        } else {
            // si le paramètre vaut true, le champ est verrouillé si prestations existent
            if (prestationHolder.getSize() > 0) {
                return true;
            } else {
                return false;
            }
        }

    }

    /**
     * @return calculError
     */
    public boolean isCalculError() {
        return calculError;
    }

    public boolean isCompteAnnexeAffError() {
        return compteAnnexeAffError;
    }

    public boolean isCompteAnnexeAllError() {
        return compteAnnexeAllError;
    }

    public void loadRequiredCs() {

        // Recherche de tous les codes système ALLIDOS
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();
        cm.setSession(currentSession);
        cm.setForIdGroupe("ALLIDOS");
        cm.setForIdLangue(currentSession.getIdLangue());
        try {
            cm.find();
        } catch (Exception ex) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage("Error listing code systems for group ALLIDOS : " + ex.toString());
        }

        allCsTypeLien = new HashMap<String, String>();
        for (int i = 0; i < cm.getSize(); i++) {

            FWParametersSystemCode cs = (FWParametersSystemCode) cm.get(i);
            allCsTypeLien.put(cs.getIdCode(), cs.getLibelle());

        }

        cm.setForIdGroupe("ALDOSCOMAC");

        try {
            cm.find();
        } catch (Exception ex) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage("Error listing code systems for group 'ALDOSCOMAC' : " + ex.toString());
        }

        allCsComplActivite = new HashMap<String, String>();
        for (int i = 0; i < cm.getSize(); i++) {

            FWParametersSystemCode cs = (FWParametersSystemCode) cm.get(i);
            allCsComplActivite.put(cs.getIdCode(), cs.getLibelle());

        }

    }

    public String renderHTMLSelectStatut() throws Exception {
        String html = "";

        String statutNselected = ALCSDossier.STATUT_N.equals(dossierComplexModel.getDossierModel().getStatut()) ? "selected"
                : "";
        String statutNPselected = ALCSDossier.STATUT_NP.equals(dossierComplexModel.getDossierModel().getStatut()) ? "selected"
                : "";
        String statutCPselected = ALCSDossier.STATUT_CP.equals(dossierComplexModel.getDossierModel().getStatut()) ? "selected"
                : "";
        String statutIPselected = ALCSDossier.STATUT_IP.equals(dossierComplexModel.getDossierModel().getStatut()) ? "selected"
                : "";
        String statutISselected = ALCSDossier.STATUT_IS.equals(dossierComplexModel.getDossierModel().getStatut()) ? "selected"
                : "";
        String statutCSelected = ALCSDossier.STATUT_CS.equals(dossierComplexModel.getDossierModel().getStatut()) ? "selected"
                : "";

        if (hasAnnonceRafam() || (prestationHolder.getSize() > 0)) {

            if (ALCSDossier.STATUT_N.equals(getDossierComplexModel().getDossierModel().getStatut())
                    || ALCSDossier.STATUT_NP.equals(getDossierComplexModel().getDossierModel().getStatut())
                    || ALCSDossier.STATUT_CP.equals(getDossierComplexModel().getDossierModel().getStatut())
                    || ALCSDossier.STATUT_IP.equals(getDossierComplexModel().getDossierModel().getStatut())) {

                html += "<select name=\"dossierComplexModel.dossierModel.statut\" data-g-select=\"\" tabindex=\"7\">";
                html += "<option " + statutNselected + " value='" + ALCSDossier.STATUT_N + "'>"
                        + getSession().getCode(ALCSDossier.STATUT_N) + "</option>";
                html += "<option " + statutNPselected + " value='" + ALCSDossier.STATUT_NP + "'>"
                        + getSession().getCode(ALCSDossier.STATUT_NP) + "</option>";
                html += "<option " + statutCPselected + " value='" + ALCSDossier.STATUT_CP + "'>"
                        + getSession().getCode(ALCSDossier.STATUT_CP) + "</option>";
                html += "<option " + statutIPselected + " value='" + ALCSDossier.STATUT_IP + "'>"
                        + getSession().getCode(ALCSDossier.STATUT_IP) + "</option>";
                html += "</select>";

            }

            else {
                html += "<input name=\"dossierComplexModel.dossierModel.statut\" class=\"small readOnly\" disabled=\"disabled\" readonly=\"readonly\"";
                html += " type=\"text\" value=\""
                        + getSession().getCode(getDossierComplexModel().getDossierModel().getStatut()) + "\" />";

            }
        } else {
            html += "<select name=\"dossierComplexModel.dossierModel.statut\" data-g-select=\"\" tabindex=\"7\">";
            html += "<option " + statutNselected + " value='" + ALCSDossier.STATUT_N + "'>"
                    + getSession().getCode(ALCSDossier.STATUT_N) + "</option>";
            html += "<option " + statutNPselected + " value='" + ALCSDossier.STATUT_NP + "'>"
                    + getSession().getCode(ALCSDossier.STATUT_NP) + "</option>";
            html += "<option " + statutCPselected + " value='" + ALCSDossier.STATUT_CP + "'>"
                    + getSession().getCode(ALCSDossier.STATUT_CP) + "</option>";
            html += "<option " + statutCSelected + " value='" + ALCSDossier.STATUT_CS + "'>"
                    + getSession().getCode(ALCSDossier.STATUT_CS) + "</option>";
            html += "<option " + statutIPselected + " value='" + ALCSDossier.STATUT_IP + "'>"
                    + getSession().getCode(ALCSDossier.STATUT_IP) + "</option>";
            html += "<option " + statutISselected + " value='" + ALCSDossier.STATUT_IS + "'>"
                    + getSession().getCode(ALCSDossier.STATUT_IS) + "</option>";
            html += "</select>";

        }
        return html;

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        loadRequiredCs();
        // Chargement des données du dossier
        dossierComplexModel = ALServiceLocator.getDossierComplexModelService().read(getId());

        etatDossierAvantModification = dossierComplexModel.getDossierModel().getEtatDossier();
        beneficiaireAvantModification = dossierComplexModel.getDossierModel().getIdTiersBeneficiaire();

        boolean isAgricoleContext = ALServiceLocator.getDossierBusinessService().isAgricole(
                dossierComplexModel.getDossierModel().getActiviteAllocataire());

        if (isAgricoleContext) {
            dossierAgricoleComplexModel = ALServiceLocator.getDossierAgricoleComplexModelService().read(getId());
        }

        nbDossiersLies = ALServiceLocator.getDossierBusinessService().getDossiersLies(dossierComplexModel.getId())
                .getSize();

        // Chargement des droits calculés
        try {
            droitsList = ALServiceLocator.getCalculBusinessService().getCalcul(dossierComplexModel, dateCalcul);
        } catch (JadeApplicationException e) {
            calculError = true;
            calculErrorMessage = e.getMessage();
        }

        // Obtention du montant total calculé
        if (droitsList.size() > 0) {
            montantTotal = (ALServiceLocator.getCalculBusinessService().getTotal(dossierComplexModel,
                    droitsList, dossierComplexModel.getDossierModel().getUniteCalcul(), "1", false, dateCalcul)).get(
                    ALConstCalcul.TOTAL_EFFECTIF).toString();
        }

        affiliation = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(
                dossierComplexModel.getDossierModel(), dateCalcul);

        // si plusieurs affiliations pour ce numéro affilié

        AffiliationSearchSimpleModel searchModel = new AffiliationSearchSimpleModel();
        searchModel.setForNumeroAffilie(dossierComplexModel.getDossierModel().getNumeroAffilie());
        searchModel = AFBusinessServiceLocator.getAffiliationService().find(searchModel);
        if (searchModel.getSize() > 1) {
            for (int i = 0; i < searchModel.getSize(); i++) {
                String dateDebut = ((AffiliationSimpleModel) searchModel.getSearchResults()[i]).getDateDebut();
                String dateFin = ((AffiliationSimpleModel) searchModel.getSearchResults()[i]).getDateFin();
                listAffiliation.add(i + 1 + ":" + dateDebut + " - " + dateFin);
            }
        }

        // Affiliation vérification le type (maison mère, succursale, normal)
        AffiliationSimpleModel maisonMere = AFBusinessServiceLocator.getAffiliationService().findMaisonMere(
                affiliation.getNumeroAffilie());
        if (maisonMere == null) {
            setTypeAffilie(getSession().getLabel("AL0004_AFFILIE_TYPE_NORMAL"));
        } else if (maisonMere.getAffiliationId().equals(affiliation.getIdAffiliation())) {
            setTypeAffilie(getSession().getLabel("AL0004_AFFILIE_TYPE_MERE"));
        } else {
            setTypeAffilie(getSession().getLabel("AL0004_AFFILIE_TYPE_SUCCU"));
        }

        CompteAnnexeSimpleModel compteAllocataire = null;
        CompteAnnexeSimpleModel compteAffilie = null;

        try {
            compteAffilie = (CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexeByIdTiers(null,
                    affiliation.getIdTiersAffiliation(), IntRole.ROLE_AFFILIE, dossierComplexModel.getDossierModel()
                            .getNumeroAffilie()));
        } catch (Exception e) {
            compteAnnexeAffError = true;
            calculErrorMessage = e.getMessage();
        } finally {
            idCompteAnnexeAffilie = compteAffilie == null ? "0" : compteAffilie.getIdCompteAnnexe();
        }

        try {
            compteAllocataire = (CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexeByIdTiers(null,
                    dossierComplexModel.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(),
                    IntRole.ROLE_AF, dossierComplexModel.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                            .getPersonneEtendue().getNumAvsActuel()));

        } catch (Exception e) {
            compteAnnexeAllError = true;
            calculErrorMessage = e.getMessage();
        } finally {
            idCompteAnnexeAllocataire = compteAllocataire == null ? "0" : compteAllocataire.getIdCompteAnnexe();
        }

        List<CompteAnnexeSimpleModel> comptesAnnexe = findComptesAnnexes();
        calculSoldeComptesAnnexes(comptesAnnexe);

        EnteteAndDetailPrestationComplexSearchModel enteteAndDetailSearchModel = new EnteteAndDetailPrestationComplexSearchModel();
        enteteAndDetailSearchModel.setForIdDossier(getId());
        enteteAndDetailSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        enteteAndDetailSearchModel.setOrderKey("default");
        enteteAndDetailSearchModel = ALImplServiceLocator.getEnteteAndDetailPrestationComplexModelService().search(
                enteteAndDetailSearchModel);
        prestationHolder = new PrestationHolder(enteteAndDetailSearchModel);

        handleWarningsForPopup();
    }

    /**
     * Additionne les soldes des comptes annexes passés en paramètre
     * 
     */
    private void calculSoldeComptesAnnexes(List<CompteAnnexeSimpleModel> comptesAnnexe) {
        for (CompteAnnexeSimpleModel compteAnnexe : comptesAnnexe) {
            // Addition de la solde uniquement si c'est le compte annexe a un rôle différent
            if (!JadeStringUtil.isBlankOrZero(compteAnnexe.getSolde())) {
                soldeComptesAnnexes = soldeComptesAnnexes.add(new BigDecimal(compteAnnexe.getSolde()));
            }
        }
    }

    /**
     * Recherche tous les comptes annexes d'un allocataire avec l'ID Tiers comme paramètre
     * 
     * @return Une liste des comptes annexes
     */
    private List<CompteAnnexeSimpleModel> findComptesAnnexes() {
        try {
            CompteAnnexeSimpleModelSearch modelSearch = new CompteAnnexeSimpleModelSearch();
            modelSearch.setForIdTiers(dossierComplexModel.getAllocataireComplexModel().getAllocataireModel()
                    .getIdTiersAllocataire());
            modelSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            return CABusinessServiceLocator.getCompteAnnexeService().search(modelSearch);
        } catch (OsirisException e) {
            JadeLogger.error(this, e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage("Erreur lors de la récupération des comptes annexes : " + e.toString());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(this, e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage("Erreur lors de la récupération des comptes annexes : " + e.toString());
        }
        return new ArrayList<CompteAnnexeSimpleModel>();
    }

    public void setAddWarnings(String addWarnings) {
        this.addWarnings = addWarnings;
    }

    /**
     * @param affiliation
     *            structure contenant les informations de l'affiliation
     */
    public void setAffiliation(AssuranceInfo affiliation) {
        this.affiliation = affiliation;
    }

    public void setAllCsComplActivite(HashMap<String, String> allCsComplActivite) {
        this.allCsComplActivite = allCsComplActivite;
    }

    public void setAllCsTypeLien(HashMap<String, String> allCsTypeLien) {
        this.allCsTypeLien = allCsTypeLien;
    }

    /**
     * @param calculError
     *            indique si il y a eu une erreur dans le calcul
     */
    public void setCalculError(boolean calculError) {
        this.calculError = calculError;
    }

    /**
     * @param dateCalcul
     *            la date à laquelle les droits sont calculés
     */
    public void setDateCalcul(String dateCalcul) {
        this.dateCalcul = dateCalcul;
    }

    public void setDossierAgricoleComplexModel(DossierAgricoleComplexModel dossierAgricoleComplexModel) {
        this.dossierAgricoleComplexModel = dossierAgricoleComplexModel;
    }

    /**
     * @param dossierComplexModel
     *            le modèle représentant le dossier complet
     */
    public void setDossierComplexModel(DossierComplexModel dossierComplexModel) {
        this.dossierComplexModel = dossierComplexModel;
    }

    /**
     * @param droitsList
     *            la liste des droits calculés
     */
    public void setDroitsList(List droitsList) {
        this.droitsList = droitsList;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        dossierComplexModel.setId(newId);
    }

    public void setListAffiliation(ArrayList listAffiliation) {
        this.listAffiliation = listAffiliation;
    }

    /**
     * @param montantTotal
     *            le montant total des droits calculés
     */
    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setNbDossiersLies(int nbDossiersLies) {
        this.nbDossiersLies = nbDossiersLies;
    }

    /**
     * @param typeAffilie
     *            le type d'affilié (maison mère, succursale ou normal)
     */
    public void setTypeAffilie(String typeAffilie) {
        this.typeAffilie = typeAffilie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {

        dossierComplexModel = ALServiceLocator.getDossierBusinessService().updateDossier(dossierComplexModel,
                etatDossierAvantModification, "", dossierAgricoleComplexModel);

    }

    public String getBeneficiaireAvantModification() {
        return beneficiaireAvantModification;
    }

    public BigDecimal getSoldeComptesAnnexes() {
        return soldeComptesAnnexes;
    }

    public String getSoldeFormate() {
        return globaz.globall.util.JANumberFormatter.formatNoRound(
                JANumberFormatter.deQuote(getSoldeComptesAnnexes().toString()), 2);
    }

    public void setSoldeComptesAnnexes(BigDecimal soldeComptesAnnexes) {
        this.soldeComptesAnnexes = soldeComptesAnnexes;
    }

}
