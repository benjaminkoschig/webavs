package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;

public class DemandePcaPrestation extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // private String idTier;
    private String idTierAdressePaiement = null;

    private String idTierAdressePaiementConjoint = null;
    // private String dateDebutPca = null;
    // private String dateFinPca = null;
    // private String idGestionnaire = null;
    // private String idPca = null;
    private String idTiersBeneficiaire = null;
    // private String idTiersBeneficiaireConjoint = null;

    private String nss;
    private SimpleDemande simpleDemande = null;
    private SimpleInformationsComptabilite simpleInformationsComptabilite = null;
    private SimpleInformationsComptabilite simpleInformationsComptabiliteConjoint = null;
    private SimplePCAccordee simplePCAccordee = null;
    private SimplePrestationsAccordees simplePrestationsAccordees = null;
    private SimplePrestationsAccordees simplePrestationsAccordeesConjoint = null;
    private SimpleVersionDroit simpleVersionDroit = null;

    public DemandePcaPrestation() {
        super();
        simpleDemande = new SimpleDemande();
        simplePCAccordee = new SimplePCAccordee();
        simplePrestationsAccordees = new SimplePrestationsAccordees();
        simplePrestationsAccordeesConjoint = new SimplePrestationsAccordees();
        simpleVersionDroit = new SimpleVersionDroit();
        simpleInformationsComptabilite = new SimpleInformationsComptabilite();
        simpleInformationsComptabiliteConjoint = new SimpleInformationsComptabilite();
    }

    @Override
    public String getId() {
        return simplePCAccordee.getId();
    }

    public String getIdTierAdressePaiement() {
        return idTierAdressePaiement;
    }

    public String getIdTierAdressePaiementConjoint() {
        return idTierAdressePaiementConjoint;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getNss() {
        return nss;
    }

    public SimpleDemande getSimpleDemande() {
        return simpleDemande;
    }

    public SimpleInformationsComptabilite getSimpleInformationsComptabilite() {
        return simpleInformationsComptabilite;
    }

    public SimpleInformationsComptabilite getSimpleInformationsComptabiliteConjoint() {
        return simpleInformationsComptabiliteConjoint;
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public SimplePrestationsAccordees getSimplePrestationsAccordees() {
        return simplePrestationsAccordees;
    }

    public SimplePrestationsAccordees getSimplePrestationsAccordeesConjoint() {
        return simplePrestationsAccordeesConjoint;
    }

    public SimpleVersionDroit getSimpleVersionDroit() {
        return simpleVersionDroit;
    }

    @Override
    public String getSpy() {
        return simplePCAccordee.getSpy();
    }

    @Override
    public void setId(String id) {
        simplePCAccordee.setId(id);
    }

    public void setIdTierAdressePaiement(String idTierAdressePaiement) {
        this.idTierAdressePaiement = idTierAdressePaiement;
    }

    public void setIdTierAdressePaiementConjoint(String idTierAdressePaiementConjoint) {
        this.idTierAdressePaiementConjoint = idTierAdressePaiementConjoint;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setSimpleDemande(SimpleDemande simpleDemande) {
        this.simpleDemande = simpleDemande;
    }

    public void setSimpleInformationsComptabilite(SimpleInformationsComptabilite simpleInformationsComptabilite) {
        this.simpleInformationsComptabilite = simpleInformationsComptabilite;
    }

    public void setSimpleInformationsComptabiliteConjoint(
            SimpleInformationsComptabilite simpleInformationsComptabiliteConjoint) {
        this.simpleInformationsComptabiliteConjoint = simpleInformationsComptabiliteConjoint;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public void setSimplePrestationsAccordees(SimplePrestationsAccordees simplePrestationsAccordees) {
        this.simplePrestationsAccordees = simplePrestationsAccordees;
    }

    public void setSimplePrestationsAccordeesConjoint(SimplePrestationsAccordees simplePrestationsAccordeesConjoint) {
        this.simplePrestationsAccordeesConjoint = simplePrestationsAccordeesConjoint;
    }

    public void setSimpleVersionDroit(SimpleVersionDroit simpleVersionDroit) {
        this.simpleVersionDroit = simpleVersionDroit;
    }

    @Override
    public void setSpy(String spy) {
        simplePCAccordee.setSpy(spy);
    }

}
