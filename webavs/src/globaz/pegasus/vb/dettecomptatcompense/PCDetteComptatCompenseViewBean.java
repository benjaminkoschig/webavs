package globaz.pegasus.vb.dettecomptatcompense;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCDroitHandler;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.decompte.DecompteTotalPcVO;

public class PCDetteComptatCompenseViewBean extends BJadePersistentObjectViewBean {

    DecisionApresCalcul decisionApresCalcul = null;
    private DecompteTotalPcVO decompte;
    // Id decision header passé en param
    private String idDecision = null;
    private String idDemande = null;
    private String idDroit = null;
    private String idVersionDroit = null;
    private String noVersion = null;

    public PCDetteComptatCompenseViewBean() {
        decisionApresCalcul = new DecisionApresCalcul();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public DecisionApresCalcul getDecisionApresCalcul() {
        return decisionApresCalcul;
    }

    public DecompteTotalPcVO getDecompte() {
        return decompte;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdDecision() {
        return idDecision;
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

    public String getNoVersion() {
        return noVersion;
    }

    public String getRequerantDetail() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        return PCDroitHandler.getInfoHtmlRequerant(getIdVersionDroit());
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public Boolean isDecisionReadyForValidation() {
        // Si elle est dans l'etat prévalidé
        return (decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getCsEtatDecision()
                .equals(IPCDecision.CS_ETAT_PRE_VALIDE));

    }

    public boolean isDetteEditable() {
        return (decisionApresCalcul.getVersionDroit().getSimpleVersionDroit().getCsEtatDroit()
                .equals(IPCDroits.CS_CALCULE) || IPCDroits.CS_COURANT_VALIDE.equals(decisionApresCalcul
                .getVersionDroit().getSimpleVersionDroit().getCsEtatDroit()));
    }

    @Override
    public void retrieve() throws Exception {
        DecisionApresCalculSearch search = new DecisionApresCalculSearch();
        search.setForIdDecisionHeader(getIdDecision());
        search = PegasusServiceLocator.getDecisionApresCalculService().search(search);
        decisionApresCalcul = (DecisionApresCalcul) search.getSearchResults()[0];
        idVersionDroit = decisionApresCalcul.getVersionDroit().getSimpleVersionDroit().getIdVersionDroit();
        idDroit = decisionApresCalcul.getVersionDroit().getSimpleVersionDroit().getIdDroit();

        decompte = PegasusServiceLocator.getDecompteService().getDecompteTotalPCA(idVersionDroit);
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
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
        // TODO Auto-generated method stub

    }

}
