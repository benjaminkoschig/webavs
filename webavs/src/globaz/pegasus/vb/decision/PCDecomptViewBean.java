package globaz.pegasus.vb.decision;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCDroitHandler;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.domaine.ListTotal;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompense;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.decompte.DecompteTotalPcVO;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class PCDecomptViewBean extends BJadePersistentObjectViewBean {

    DecisionApresCalcul decisionApresCalcul = null;
    private DecompteTotalPcVO decompte = null;
    private String idDecision = null;
    private String idDecisionDac = null;
    private String idDemande = null;
    private String idDroit = null;
    private String idVersionDroit = null;
    private String noVersion = null;

    @Override
    public void add() throws Exception {
        throw new Exception("Méthode add non implémentée");
    }

    @Override
    public void delete() throws Exception {
        throw new Exception("Méthode delete non implémentée");
    }

    public String getCreancierLibelle(String idTiers) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException, DecisionException {
        if (JadeStringUtil.isBlank(idTiers)) {
            throw new DecisionException("Unable to retrieve Tiers for building libelle for OO, the id passed is null");
        }

        TiersSimpleModel tiersModel = TIBusinessServiceLocator.getTiersService().read(idTiers);

        String nom = tiersModel.getDesignation1() + " " + tiersModel.getDesignation2() + " "
                + tiersModel.getDesignation3() + " " + tiersModel.getDesignation4();

        return nom;
    }

    public DecompteTotalPcVO getDecompte() {
        return decompte;
    }

    public String getDescriptionSection(String idSection) throws JadeApplicationException {
        return CABusinessServiceLocator.getSectionService().findDescription(idSection);
    }

    public ListTotal<SimpleDetteComptatCompense> getDette() throws JadeApplicationServiceNotAvailableException,
            JadeApplicationException, JadePersistenceException {

        ListTotal<SimpleDetteComptatCompense> dettes = PegasusServiceLocator.getDetteComptatCompenseService()
                .findListTotalCompense(idVersionDroit, idDroit);
        return dettes;
    }

    @Override
    public String getId() {
        return null;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDecisionDac() {
        return idDecisionDac;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public String getRemarqueDecompte() {
        return decisionApresCalcul.getVersionDroit().getSimpleVersionDroit().getRemarqueDecompte();
    }

    public String getMontantAllocationNoel() {
        return new FWCurrency(decompte.getMontantAllocationNoelParPersonne().toString()).toStringFormat();
    }

    public String getMontantTotalAllocationNoel() {
        return new FWCurrency(decompte.getMontantTotalAllocationNoel().toString()).toStringFormat();
    }

    public String getNbPersonneAllocationNoel() {
        return String.valueOf(decompte.getNombrePersonnesAllocationDeNoel());
    }

    public String getNoVersion() {
        return noVersion;
    }

    public String getRequerantDetail() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        return PCDroitHandler.getInfoHtmlRequerant(getIdVersionDroit());
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public boolean hasAllocationNoel() {
        return decompte.getSimpleAllocationNoels().size() > 0;
    }

    public boolean hasDecompte() {
        return (!IPCDecision.CS_PREP_COURANT.equals(decisionApresCalcul.getSimpleDecisionApresCalcul()
                .getCsTypePreparation()))
                && decisionApresCalcul.getPcAccordee().getSimplePCAccordee().getIsCalculRetro();
    }

    public Boolean isDecisionReadyForValidation() {
        // Si elle est dans l'etat prévalidé
        // return true;
        return (decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getCsEtatDecision()
                .equals(IPCDecision.CS_ETAT_PRE_VALIDE));

    }

    @Override
    public void retrieve() throws Exception {
        DecisionApresCalculSearch search = new DecisionApresCalculSearch();
        // search.setForIdDecisionHeader(this.getIdDecision());
        search.setForIdVersionDroit(idVersionDroit);
        search = PegasusServiceLocator.getDecisionApresCalculService().search(search);
        decisionApresCalcul = (DecisionApresCalcul) search.getSearchResults()[0];
        idDroit = decisionApresCalcul.getVersionDroit().getSimpleVersionDroit().getIdDroit();
        decompte = PegasusServiceLocator.getDecompteService().getDecompteTotalPCA(idVersionDroit);

    }

    @Override
    public void setId(String newId) {
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDecisionDac(String idDecisionDac) {
        this.idDecisionDac = idDecisionDac;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
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

    @Override
    public void update() throws Exception {
        throw new Exception("Méthode update non implémentée");
    }

}
