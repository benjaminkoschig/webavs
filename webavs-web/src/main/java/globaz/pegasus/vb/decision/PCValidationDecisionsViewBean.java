package globaz.pegasus.vb.decision;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.AmalUtilsForDecisionsPC;
import globaz.pegasus.utils.PCDroitHandler;
import java.math.BigDecimal;
import java.util.ArrayList;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.IPCActions;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.creancier.CreancierSearch;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.decompte.DecompteTotalPcVO;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValiderDecisionAcData;
import com.sun.star.lang.IllegalArgumentException;

public class PCValidationDecisionsViewBean extends BJadePersistentObjectViewBean {

    private String amalWarning = null;
    private DecisionApresCalculSearch decisionApresCalculSearch = null;
    private String idDroit = null;
    private String idVersionDroit = null;
    private Boolean isAmalIncoherent = null;
    private String noVersion = null;
    private String path = null;
    private BigDecimal montant = BigDecimal.ZERO;
    private boolean isComptabilisationAuto = false;
    private ValiderDecisionAcData data = null;
    private Boolean isBaseSurCalculMoisSuivant = null;

    public PCValidationDecisionsViewBean() {
        super();
        decisionApresCalculSearch = new DecisionApresCalculSearch();
    }

    @Override
    public void add() throws Exception {
    }

    public String checkValiderTout() throws PmtMensuelException, JadeApplicationServiceNotAvailableException {
        String msg = null;
        if (!isAllDecisionPreValide()) {
            msg = getSession().getLabel("JSP_PC_VALIDATION_DECISIONS_D_DOIT_ETRE_PRE_VALIDER");
        } else if (!isValidationDecisionAuthorise()) {
            msg = getSession().getLabel("JSP_PC_VALIDATION_DECISIONS_D_LOCK_LOT");
        }
        return msg;
    }

    public String controleDecision(DecisionApresCalcul decisionApresCalcul)
            throws JadeApplicationServiceNotAvailableException, DecisionException, CreancierException,
            JadePersistenceException, PmtMensuelException {
        String str = "";
        String label = "";
        String href = "";
        String param = "";

        if (PegasusServiceLocator.getValidationDecisionService().isPaymentDoneBetweenTheValidation(decisionApresCalcul)) {

            String idDemande = decisionApresCalcul.getVersionDroit().getDemande().getSimpleDemande().getIdDemande();

            CreancierSearch search = new CreancierSearch();
            int countCreancier = PegasusServiceLocator.getCreancierService().count(search);
            search.setForIdDemande(idDemande);

            if (countCreancier > 0) {
                label = getSession().getLabel("JSP_PC_VALIDATION_DECISIONS_D_REPARTIR_CRANCE");
                param = "idDemandePc=" + idDemande;
                href = path + "?userAction=" + IPCActions.ACTION_CREANCE_ACCORDEE + ".afficher&" + param;
                str = "<a data-g-externallink='selectorForClose:¦#btnVal¦' href =\"" + href + "\">" + label + "</a>";
            }
        }

        String datePP = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();

        if (JadeDateUtil.isDateBefore("01." + datePP, decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                .getDateDecision())) {
            label = getSession().getLabel("JSP_PC_VALIDATION_DECISIONS_D_DATE_A_MODIFIER") + "(01." + datePP + ")";
            str = str + " " + label;
        }

        return str;
    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub
    }

    public String getAmalWarning() {

        if (amalWarning != null) {
            return amalWarning;
        } else {
            return "";
        }

    }

    public String getMontantDispo() {
        return montant.abs().toString();
    }

    /**
     * Retourne la a figurer sur le document !!! retourne la date de preparation DE LA 1 ERE DECISION DU LOT !!!
     * 
     * @return
     */
    public String getDateDoc() {
        return ((DecisionApresCalcul) decisionApresCalculSearch.getSearchResults()[0]).getDecisionHeader()
                .getSimpleDecisionHeader().getDatePreparation();
    }

    /**
     * Retourne les id des décisions
     * 
     * @return une ArrayList<String> des décisions (après-calcul)
     */
    public ArrayList<String> getDecisionsId() {

        ArrayList<String> idsDecision = new ArrayList<String>();

        for (JadeAbstractModel model : decisionApresCalculSearch.getSearchResults()) {
            DecisionApresCalcul dec = (DecisionApresCalcul) model;
            idsDecision.add(dec.getSimpleDecisionApresCalcul().getIdDecisionApresCalcul());
        }
        return idsDecision;
    }

    @Override
    public String getId() {
        return null;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public Boolean getIsAmalIncoherent() throws Exception {

        if ((isAmalIncoherent != null)
                && Boolean.parseBoolean(getSession().getApplication().getProperty(
                        "pegasus.amal.check.blockifincomplete"))) {
            return isAmalIncoherent;
        } else {
            return false;
        }

    }

    public JadeAbstractModel[] getListDecision() {
        return decisionApresCalculSearch.getSearchResults();
    }

    public String getNoVersion() {
        return noVersion;
    }

    public String getPath() {
        return path;
    }

    /**
     * Retourne la personne de référence, pour impresison auto !!! retourne la personne AYANT PREPARE LA 1 ERE DECISION
     * DU LOT !!!
     * 
     * @return
     */
    public String getPersonneRef() {
        return ((DecisionApresCalcul) decisionApresCalculSearch.getSearchResults()[0]).getDecisionHeader()
                .getSimpleDecisionHeader().getPreparationPar();
    }

    public String getRequerant() throws DroitException {
        return PCDroitHandler.getRequerantDetail(getSession(), idDroit);
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    private boolean isAllDecisionPreValide() {
        for (JadeAbstractModel model : decisionApresCalculSearch.getSearchResults()) {
            DecisionApresCalcul decision = (DecisionApresCalcul) model;
            if (!IPCDecision.CS_ETAT_PRE_VALIDE.equals(decision.getDecisionHeader().getSimpleDecisionHeader()
                    .getCsEtatDecision())) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidationDecisionAuthorise() throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException {
        return PegasusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise();
    }

    @Override
    public void retrieve() throws Exception {
        decisionApresCalculSearch.setForIdVersionDroit(idVersionDroit);
        decisionApresCalculSearch.setForExcludeCsEtatDecisionValide(IPCDecision.CS_ETAT_DECISION_VALIDE);
        decisionApresCalculSearch.setWhereKey("forValidationDecision");
        decisionApresCalculSearch = PegasusServiceLocator.getDecisionApresCalculService().search(
                decisionApresCalculSearch);
        if (Boolean.parseBoolean(getSession().getApplication().getProperty("pegasus.amal.check.enable"))) {
            // Amal warning
            AmalUtilsForDecisionsPC.checkAndGenerateWarningCoherenceWithAmal(idVersionDroit, getSession());
            isAmalIncoherent = AmalUtilsForDecisionsPC.getIsAmalIncoherent();
        }

        isBaseSurCalculMoisSuivant = defineIfDecisionBasesSurCalculMoisSuivant();

        if (!isBaseSurCalculMoisSuivant) {
            DecompteTotalPcVO decompte = PegasusServiceLocator.getDecompteService().getDecompteTotalPCA(idVersionDroit);
            montant = decompte.getTotal();
        }

    }

    /**
     * Défini si les pc des décisions sont basées sur des calculs mois suivants
     * 
     * @return
     */
    private Boolean defineIfDecisionBasesSurCalculMoisSuivant() {

        // itéation sur toute sles décisions
        for (JadeAbstractModel dec : decisionApresCalculSearch.getSearchResults()) {
            DecisionApresCalcul decision = (DecisionApresCalcul) dec;

            // si une seule n'est pas issu d'un calcul retro on sort
            if (decision.getPcAccordee().getSimplePCAccordee().getIsCalculRetro()) {

                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;

    }

    /**
     * Pour écran, défini si on est dansle cas d'un calcul RetroActif
     * 
     * @return
     */
    public Boolean getIsDecisionBaseSurCalculRetro() {
        return !isBaseSurCalculMoisSuivant;
    }

    /**
     * Pour écran défini si le montant disponible est positif.
     * 
     * @return
     */
    public Boolean getIsMontantDispoPositif() {
        return montant.floatValue() >= 0;
    }

    public void setAmalWarning(String amalWarning) {
        this.amalWarning = amalWarning;
    }

    @Override
    public void setId(String arg0) {
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void update() throws Exception {
        // 1. Validation des décisions
        validerTout();
        // 2. Comptabilisation du lot
        if (isComptabilisationAuto) {
            comptabiliserLeLotToDay();
        }
    }

    /**
     * Permet une comptabilisation du lot<br>
     * Si l'option <strong>isComptabilisationAuto</strong> vaut true, on comptabilise.<br>
     * Sinon on fait rien.
     * 
     * @throws PropertiesException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ComptabiliserLotException
     * @throws DecisionException
     * @throws IllegalArgumentException
     */
    private void comptabiliserLeLotToDay() throws PropertiesException, ComptabiliserLotException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException,
            IllegalArgumentException {

        if (data == null) {
            throw new IllegalArgumentException("Aucune donnée de validation de décision AC");
        }

        if (data.getSimplePrestation() == null) {
            throw new IllegalArgumentException("Aucune donnée de validation de décision AC");
        }

        if (data.getSimplePrestation().getIdLot() == null) {
            throw new IllegalArgumentException("Aucune donnée de validation de décision AC, pas d'idLot");
        }

        PegasusServiceLocator.getLotService().comptabiliserAndResolveDateComptableEcheance(
                data.getSimplePrestation().getIdLot());

    }

    private void validerTout() throws DecisionException, JadePersistenceException, JadeApplicationException {
        data = PegasusServiceLocator.getValidationDecisionService().validerToutesLesDesionsionApresCalcule(
                idVersionDroit, isComptabilisationAuto());
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public boolean isComptabilisationAuto() {
        return isComptabilisationAuto;
    }

    public boolean getIsComptabilisationAuto() {
        return isComptabilisationAuto;
    }

    public void setIsComptabilisationAuto(boolean isComptabilisationAuto) {
        this.isComptabilisationAuto = isComptabilisationAuto;
    }
}
