package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.dettecomptatcompense.DetteCompenseCompteAnnexe;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class ValiderDecisionAcData {
    private List<SimpleAllocationNoel> allocationsNoel;
    private PersonneEtendueComplexModel conjoint;
    private List<CreanceAccordee> creanciers;
    private String currentUserId;
    private String dateDebut;
    private String dateDecision;
    private String dateFin;
    private String dateFinForce;
    private String dateProchainPaiement;
    private List<DecisionApresCalcul> decisionsApresCalcul;
    private List<DetteCompenseCompteAnnexe> dettes;
    private Boolean hasAllocationNoel = false;
    private boolean hasDemandeOnlyPcaRefus = false;
    private List<SimpleJoursAppoint> joursAppointNew;
    private List<SimpleJoursAppoint> joursAppointReplaced;
    private int nbMonthBetween;
    private List<SimpleOrdreVersement> ovs;
    private List<PCAccordee> pcasCopie;
    private List<PcaForDecompte> pcasNew;
    private List<PcaForDecompte> pcasReplaced;
    private List<PCAccordee> pcasSupprimee = new ArrayList<PCAccordee>();
    private List<SimpleRetenuePayement> rentuesPayements;
    private PersonneEtendueComplexModel requerant;
    private SimpleDemande simpleDemande;
    private SimplePrestation simplePrestation;
    private SimpleVersionDroit simpleVersionDroitNew;
    private SimpleVersionDroit simpleVersionDroitReplaced;
    private boolean useJourAppoints = false;
    private String dateDebutMax;

    public List<SimpleAllocationNoel> getAllocationsNoel() {
        return allocationsNoel;
    }

    public PersonneEtendueComplexModel getConjoint() {
        return conjoint;
    }

    public List<CreanceAccordee> getCreanciers() {
        return creanciers;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDateFinForce() {
        return dateFinForce;
    }

    public String getDateProchainPaiement() {
        return dateProchainPaiement;
    }

    public List<DecisionApresCalcul> getDecisionsApresCalcul() {
        return decisionsApresCalcul;
    }

    public List<DetteCompenseCompteAnnexe> getDettes() {
        return dettes;
    }

    public Boolean getHasAllocationNoel() {
        return hasAllocationNoel;
    }

    public String getIdDemande() {
        return simpleDemande.getIdDemande();
    }

    public String getIdDroit() {
        return simpleVersionDroitNew.getIdDroit();
    }

    public List<SimpleJoursAppoint> getJoursAppointNew() {
        return joursAppointNew;
    }

    public List<SimpleJoursAppoint> getJoursAppointReplaced() {
        return joursAppointReplaced;
    }

    public int getNbMonthBetween() {
        return nbMonthBetween;
    }

    public String getNoVersionDroit() {
        return simpleVersionDroitNew.getNoVersion();
    }

    public List<SimpleOrdreVersement> getOvs() {
        return ovs;
    }

    public List<PCAccordee> getPcasCopie() {
        return pcasCopie;
    }

    public List<PcaForDecompte> getPcasNew() {
        return pcasNew;
    }

    public List<PcaForDecompte> getPcasReplaced() {
        return pcasReplaced;
    }

    public List<PCAccordee> getPcasSupprimee() {
        return pcasSupprimee;
    }

    public List<SimpleRetenuePayement> getRentuesPayements() {
        return rentuesPayements;
    }

    public PersonneEtendueComplexModel getRequerant() {
        return requerant;
    }

    public SimpleDemande getSimpleDemande() {
        return simpleDemande;
    }

    public SimplePrestation getSimplePrestation() {
        return simplePrestation;
    }

    public SimpleVersionDroit getSimpleVersionDroitNew() {
        return simpleVersionDroitNew;
    }

    public SimpleVersionDroit getSimpleVersionDroitReplaced() {
        return simpleVersionDroitReplaced;
    }

    public boolean getUseJourAppoints() {
        return useJourAppoints;
    }

    public boolean hasDemandeOnlyPcaRefus() {
        return hasDemandeOnlyPcaRefus;
    }

    public void setAllocationsNoel(List<SimpleAllocationNoel> allocationsNoel) {
        this.allocationsNoel = allocationsNoel;
    }

    public void setConjoint(PersonneEtendueComplexModel conjoint) {
        this.conjoint = conjoint;
    }

    public void setCreanciers(List<CreanceAccordee> creanciers) {
        this.creanciers = creanciers;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDateFinForce(String dateFinForce) {
        this.dateFinForce = dateFinForce;
    }

    public void setDateProchainPaiement(String dateProchainPaiement) {
        this.dateProchainPaiement = dateProchainPaiement;
    }

    public void setDecisionApresCalculs(List<DecisionApresCalcul> decisionsApresCalcul) {
        this.decisionsApresCalcul = decisionsApresCalcul;
    }

    public void setDettes(List<DetteCompenseCompteAnnexe> dettes) {
        this.dettes = dettes;
    }

    public void setHasAllocationNoel(Boolean hasAllocationNoel) {
        this.hasAllocationNoel = hasAllocationNoel;
    }

    public void setHasDemandeOnlyPcaRefus(boolean hasDemandeOnlyPcaRefus) {
        this.hasDemandeOnlyPcaRefus = hasDemandeOnlyPcaRefus;
    }

    public void setJoursAppointNew(List<SimpleJoursAppoint> joursAppointNew) {
        this.joursAppointNew = joursAppointNew;
    }

    public void setJoursAppointReplaced(List<SimpleJoursAppoint> joursAppointReplaced) {
        this.joursAppointReplaced = joursAppointReplaced;
    }

    public void setNbMonthBetween(int nbMonthBetween) {
        this.nbMonthBetween = nbMonthBetween;
    }

    public void setOvs(List<SimpleOrdreVersement> ovs) {
        this.ovs = ovs;
    }

    public void setPcaCopie(List<PCAccordee> pcasCopie) {
        this.pcasCopie = pcasCopie;
    }

    public void setPcasNew(List<PcaForDecompte> pcasNew) {
        this.pcasNew = pcasNew;
    }

    public void setPcasReplaced(List<PcaForDecompte> pcasReplaced) {
        this.pcasReplaced = pcasReplaced;
    }

    public void setPcasSupprimee(List<PCAccordee> pcasSupprimee) {
        this.pcasSupprimee = pcasSupprimee;
    }

    public void setRentuesPayements(List<SimpleRetenuePayement> rentuesPayements) {
        this.rentuesPayements = rentuesPayements;
    }

    public void setRequerant(PersonneEtendueComplexModel requerant) {
        this.requerant = requerant;
    }

    public void setSimpleDemande(SimpleDemande simpleDemande) {
        this.simpleDemande = simpleDemande;
    }

    public void setSimplePrestation(SimplePrestation simplePrestation) {
        this.simplePrestation = simplePrestation;
    }

    public void setSimpleVersionDroitNew(SimpleVersionDroit simpleVersionDroitNew) {
        this.simpleVersionDroitNew = simpleVersionDroitNew;
    }

    public void setSimpleVersionDroitReplaced(SimpleVersionDroit simpleVersionDroitReplaced) {
        this.simpleVersionDroitReplaced = simpleVersionDroitReplaced;
    }

    public void setUseJourAppoints(boolean useJourAppoints) {
        this.useJourAppoints = useJourAppoints;
    }

    public String getDateDebutMax() {
        return dateDebutMax;
    }

    public void setDateDebutMax(String dateDebutMax) {
        this.dateDebutMax = dateDebutMax;
    }

}
