/**
 * 
 */
package globaz.perseus.vb.creancier;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.exceptions.models.creancier.CreancierException;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.lot.PrestationSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author MBO
 * 
 */
public class PFCreancierViewBean extends BJadePersistentObjectViewBean {

    private Boolean areEditable = null;
    private Demande demande;
    private String idDemande = null;
    private String montantRetro = null;
    private Prestation prestation = null;

    public PFCreancierViewBean() {
        super();
        demande = new Demande();
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    /**
     * @return the areEditable
     */
    public Boolean getAreEditable() {
        return areEditable;
    }

    public Demande getDemande() {
        return demande;
    }

    public String getEtatDemande() {
        return demande.getSimpleDemande().getCsEtatDemande();
    }

    @Override
    public String getId() {
        return null;
    }

    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return the montantRetro
     */
    public String getMontantRetro() {
        return montantRetro;
    }

    /**
     * @return the prestation
     */
    public Prestation getPrestation() {
        return prestation;
    }

    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(getSession());
    }

    public void init() throws Exception {
        demande = PerseusServiceLocator.getDemandeService().read(idDemande);

        PrestationSearchModel psm = new PrestationSearchModel();
        psm.setForIdDemande(demande.getId());
        psm.getInTypeLot().add(CSTypeLot.LOT_DECISION.getCodeSystem());
        psm = PerseusServiceLocator.getPrestationService().search(psm);

        if (psm.getSize() == 1) {
            prestation = (Prestation) psm.getSearchResults()[0];
            montantRetro = prestation.getSimplePrestation().getMontantTotal();
        } else {
            montantRetro = PerseusServiceLocator.getDemandeService().calculerRetroPourCreanciers(demande).toString();
        }
        // Si il y'a des décisions validées autres que des projets pour la demande elle est pas éditable
        DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
        decisionSearchModel.setForIdDemande(getDemande().getId());
        decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
        decisionSearchModel.setForNotCsTypeDecision(CSTypeDecision.PROJET.getCodeSystem());
        areEditable = (PerseusServiceLocator.getDecisionService().count(decisionSearchModel) > 0) ? false : true;

        getISession()
                .setAttribute(
                        "likeNss",
                        demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                                .getNumAvsActuel());
    }

    @Override
    public void retrieve() throws Exception {
        init();
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    @Override
    public void setId(String newId) {

    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param montantRetro
     *            the montantRetro to set
     */
    public void setMontantRetro(String montantRetro) {
        this.montantRetro = montantRetro;
    }

    /**
     * @param prestation
     *            the prestation to set
     */
    public void setPrestation(Prestation prestation) {
        this.prestation = prestation;
    }

    @Override
    public void update() throws CreancierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
    }

}
