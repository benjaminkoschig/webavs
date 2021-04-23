package globaz.pegasus.vb.decision;

import ch.globaz.pegasus.business.constantes.EPCProperties;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCDroitHandler;
import java.util.ArrayList;
import java.util.Date;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PCGedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PCImprimerDecisionsViewBean extends BJadePersistentObjectViewBean {

    private static final Logger LOG = LoggerFactory.getLogger(PCImprimerDecisionsViewBean.class);
    private String dateDoc = null;

    private DecisionApresCalculSearch decisionApresCalculSearch = null;

    private ArrayList<String> decisionsId = null;
    private String idDecision = null;
    private String idDemandePc = null;
    private String idDroit = null;
    private String idVersionDroit = null;
    // on s'assure que le lien documnet --> ged est effecivement ok, configuration
    private Boolean isDecisionForGed = false;
    private String mailGest = null;
    private String noVersion = null;
    private String persref = null;
    /* définis si la publication doit également être mise en GED */
    private Boolean toGed = false;

    private String typeDecision = null;

    public PCImprimerDecisionsViewBean() {
        super();
        decisionApresCalculSearch = new DecisionApresCalculSearch();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getDateDoc() {
        return dateDoc;
    }

    /**
     * Retourne la date courant au format Globaz
     * 
     * @return
     */
    public String getDateNow() {
        Date dateNow = new Date();
        return JadeDateUtil.getGlobazFormattedDate(dateNow);
    }

    public DecisionApresCalculSearch getDecisionApresCalculSearch() {
        return decisionApresCalculSearch;
    }

    public ArrayList<String> getDecisionsId() {
        return decisionsId;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDemandePc() {
        return idDemandePc;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public JadeAbstractModel[] getListDecision() {
        return decisionApresCalculSearch.getSearchResults();
    }

    public String getMailGest() {
        return mailGest;
    }

    public String getMailGestionnaire(BSession session) {

        return session.getUserEMail();
    }

    public String getNoVersion() {
        return noVersion;
    }

    public String getPersonnePrep() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        if (null == decisionApresCalculSearch.getSearchResults()) {
            decisionApresCalculSearch.setForIdVersionDroit(idVersionDroit);
            decisionApresCalculSearch = PegasusServiceLocator.getDecisionApresCalculService().search(
                    decisionApresCalculSearch);
        }

        if ((decisionApresCalculSearch.getSearchResults().length == 0)
                || (null == decisionApresCalculSearch.getSearchResults())) {
            String errorMsg = "Unable to find decision for idVersionDroit: [" + idVersionDroit + "], idDemande:["
                    + idDemandePc + "]";

            JadeLogger.error(this, errorMsg);
            throw new DecisionException(errorMsg);
        }

        return ((DecisionApresCalcul) decisionApresCalculSearch.getSearchResults()[0]).getDecisionHeader()
                .getSimpleDecisionHeader().getPreparationPar();
    }

    public String getPersref() {
        return persref;
    }

    public String getRequerant() throws DroitException {
        return PCDroitHandler.getRequerantDetail(getSession(), idDroit);
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public Boolean getToGed() {
        return toGed;
    }

    public String getTypeDecision() {
        return typeDecision;
    }

    public boolean isDecisionsForGed() throws PropertiesException, JadeServiceLocatorException,
            JadeServiceActivatorException, NullPointerException, ClassCastException, JadeClassCastException {
        return isDecisionForGed;
    }

    @Override
    public void retrieve() throws Exception {

        decisionApresCalculSearch.setForIdVersionDroit(idVersionDroit);
        decisionApresCalculSearch = PegasusServiceLocator.getDecisionApresCalculService().search(
                decisionApresCalculSearch);

        // lien GED
        isDecisionForGed = PCGedUtils.isDocumentInGed(IPRConstantesExternes.PC_REF_INFOROM_DECISION_APRES_CALCUL,
                getSession());
        // On force la mise en GED si propriété existante et activée
        try {
            toGed = EPCProperties.DECISION_FORCER_MISE_EN_GED.getBooleanValue();
        } catch (PropertiesException e) {
            // Si la propriété n'exite pas, on catch l'erreur, et on met à false le boolean
            LOG.error("La properties : " + EPCProperties.DECISION_FORCER_MISE_EN_GED.getProperty() + " n'existe pas.", e);
            toGed = false;
        }
    }

    public void setDateDoc(String dateDoc) {
        this.dateDoc = dateDoc;
    }

    public void setDecisionApresCalculSearch(DecisionApresCalculSearch decisionApresCalculSearch) {
        this.decisionApresCalculSearch = decisionApresCalculSearch;
    }

    public void setDecisionsId(ArrayList<String> decisionsId) {
        this.decisionsId = decisionsId;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDemandePc(String idDemandePc) {
        this.idDemandePc = idDemandePc;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setMailGest(String mailGest) {
        this.mailGest = mailGest;
    }

    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    public void setPersref(String persref) {
        this.persref = persref;
    }

    public void setToGed(Boolean toGed) {
        this.toGed = toGed;
    }

    public void setTypeDecision(String typeDecision) {
        this.typeDecision = typeDecision;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
