package ch.globaz.corvus.business.models.ordresversements;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleRenteVerseeATort;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class OrdreVersementComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleCompensationInterDecision compensationInterDecisionNegative;
    private SimpleCompensationInterDecision compensationInterDecisionPositive;
    private String descriptionSaisieManuelleRenteVerseeATort;
    private SimpleOrdreVersement ordreVersement;
    private SimplePrestationsAccordees renteAccordeeAncienDroit;
    private SimplePrestationsAccordees renteAccordeeNouveauDroit;
    private SimpleRenteVerseeATort renteVerseeATort;
    private SimpleSoldePourRestitution soldePourRestitution;
    private TiersSimpleModel tiersCIDNegative;
    private TiersSimpleModel tiersCIDPositive;
    private TiersSimpleModel tiersOrdreVersement;
    private SimplePrestationsAccordees renteAccordeeAncienDroitRenteVerseeATort;

    public OrdreVersementComplexModel() {
        super();

        compensationInterDecisionNegative = new SimpleCompensationInterDecision();
        compensationInterDecisionPositive = new SimpleCompensationInterDecision();
        ordreVersement = new SimpleOrdreVersement();
        renteAccordeeAncienDroit = new SimplePrestationsAccordees();
        renteAccordeeNouveauDroit = new SimplePrestationsAccordees();
        renteVerseeATort = new SimpleRenteVerseeATort();
        soldePourRestitution = new SimpleSoldePourRestitution();
        tiersCIDNegative = new TiersSimpleModel();
        tiersCIDPositive = new TiersSimpleModel();
        tiersOrdreVersement = new TiersSimpleModel();
        renteAccordeeAncienDroitRenteVerseeATort = new SimplePrestationsAccordees();
    }

    public String get_isCompensationInterDecision() {
        return ordreVersement.get_isCompensationInterDecision();
    }

    public String get_isCompense() {
        return ordreVersement.get_isCompense();
    }

    public String get_isValide() {
        return ordreVersement.get_isValide();
    }

    public String getCodePrestationCompensee() {
        return renteAccordeeNouveauDroit.getCodePrestation();
    }

    public String getCodePrestationDiminuee() {
        return renteAccordeeAncienDroit.getCodePrestation();
    }

    public String getCsType() {
        return ordreVersement.getCsType();
    }

    public String getCsTypeRenteVerseeATort() {
        return renteVerseeATort.getCsTypeRenteVerseeATort();
    }

    public String getCsTypeSoldeRestitution() {
        return soldePourRestitution.getCsTypeRestitution();
    }

    public String getDateDebutDroitCompensee() {
        return renteAccordeeNouveauDroit.getDateDebutDroit();
    }

    public String getDateDebutDroitDiminuee() {
        return renteAccordeeAncienDroit.getDateDebutDroit();
    }

    public String getDateDebutRenteVerseeATort() {
        return renteVerseeATort.getDateDebut();
    }

    public String getDateFinDroitCompensee() {
        return renteAccordeeNouveauDroit.getDateFinDroit();
    }

    public String getDateFinDroitDiminuee() {
        return renteAccordeeAncienDroit.getDateFinDroit();
    }

    public String getDateFinRenteVerseeATort() {
        return renteVerseeATort.getDateFin();
    }

    public String getDescriptionSaisieManuelleRenteVerseeATort() {
        return descriptionSaisieManuelleRenteVerseeATort;
    }

    @Override
    public String getId() {
        return ordreVersement.getId();
    }

    public String getIdCompensationInterDecisionNegative() {
        return compensationInterDecisionNegative.getIdCompensationInterDecision();
    }

    public String getIdCompensationInterDecisionPositive() {
        return compensationInterDecisionPositive.getIdCompensationInterDecision();
    }

    public String getIdOrdreVersement() {
        return ordreVersement.getIdOrdreVersement();
    }

    public String getIdPrestationAccordeeCompensee() {
        return renteAccordeeNouveauDroit.getIdPrestationAccordee();
    }

    public String getIdPrestationAccordeeDiminuee() {
        return renteAccordeeAncienDroit.getIdPrestationAccordee();
    }

    public String getIdRenteVerseeATort() {
        return renteVerseeATort.getIdRenteVerseeATort();
    }

    public String getIdSoldePourRestitution() {
        return soldePourRestitution.getIdSoldePourRestitution();
    }

    public String getIdTiersCIDNegative() {
        return tiersCIDNegative.getIdTiers();
    }

    public String getIdTiersCIDPositive() {
        return tiersCIDPositive.getIdTiers();
    }

    public String getIdTiersOrdreVersement() {
        return tiersOrdreVersement.getIdTiers();
    }

    public Boolean getIsCompensationInterDecision() {
        return ordreVersement.getIsCompensationInterDecision();
    }

    public Boolean getIsCompense() {
        return ordreVersement.getIsCompense();
    }

    public Boolean getIsValide() {
        return ordreVersement.getIsValide();
    }

    public String getMontant() {
        return ordreVersement.getMontant();
    }

    public String getMontantCompensationInterDecisionNegative() {
        return compensationInterDecisionNegative.getMontant();
    }

    public String getMontantCompensationInterDecisionPositive() {
        return compensationInterDecisionPositive.getMontant();
    }

    public String getMontantDette() {
        return ordreVersement.getMontantDette();
    }

    public String getMontantRetenueMensuelleSoldePourRestitution() {
        return soldePourRestitution.getMontantMensuelARetenir();
    }

    public String getMontantSoldePourRestitution() {
        return soldePourRestitution.getMontant();
    }

    public String getNoFacture() {
        return ordreVersement.getNoFacture();
    }

    public String getNomTiersCIDNegative() {
        return tiersCIDNegative.getDesignation1();
    }

    public String getNomTiersCIDPositive() {
        return tiersCIDPositive.getDesignation1();
    }

    public String getNomTiersOrdreVersement() {
        return tiersOrdreVersement.getDesignation1();
    }

    public String getPrenomTiersCIDNegative() {
        return tiersCIDNegative.getDesignation2();
    }

    public String getPrenomTiersCIDPositive() {
        return tiersCIDPositive.getDesignation2();
    }

    public String getPrenomTiersOrdreVersement() {
        return tiersOrdreVersement.getDesignation2();
    }

    @Override
    public String getSpy() {
        return ordreVersement.getSpy();
    }

    public void set_isCompensationInterDecision(String isCompensationInterDecision) {
        ordreVersement.set_isCompensationInterDecision(isCompensationInterDecision);
    }

    public void set_isCompense(String isCompense) {
        ordreVersement.set_isCompense(isCompense);
    }

    public void set_isValide(String isValide) {
        ordreVersement.set_isValide(isValide);
    }

    public void setCodePrestationCompensee(String codePrestation) {
        renteAccordeeNouveauDroit.setCodePrestation(codePrestation);
    }

    public void setCodePrestationDiminuee(String codePrestation) {
        renteAccordeeAncienDroit.setCodePrestation(codePrestation);
    }

    public void setCsType(String csType) {
        ordreVersement.setCsType(csType);
    }

    public void setCsTypeRenteVerseeATort(String csTypeRenteVerseeATort) {
        renteVerseeATort.setCsTypeRenteVerseeATort(csTypeRenteVerseeATort);
    }

    public void setCsTypeSoldeRestitution(String csType) {
        soldePourRestitution.setCsTypeRestitution(csType);
    }

    public void setDateDebutDroitCompensee(String dateDebutDroit) {
        renteAccordeeNouveauDroit.setDateDebutDroit(dateDebutDroit);
    }

    public void setDateDebutDroitDiminuee(String dateDebutDroit) {
        renteAccordeeAncienDroit.setDateDebutDroit(dateDebutDroit);
    }

    public void setDateDebutRenteVerseeATort(String dateDebut) {
        renteVerseeATort.setDateDebut(dateDebut);
    }

    public void setDateFinDroitCompensee(String dateFinDroit) {
        renteAccordeeNouveauDroit.setDateFinDroit(dateFinDroit);
    }

    public void setDateFinDroitDiminuee(String dateFinDroit) {
        renteAccordeeAncienDroit.setDateFinDroit(dateFinDroit);
    }

    public void setDateFinRenteVerseeATort(String dateFin) {
        renteVerseeATort.setDateFin(dateFin);
    }

    public void setDescriptionSaisieManuelleRenteVerseeATort(String descriptionSaisieManuelleRenteVerseeATort) {
        this.descriptionSaisieManuelleRenteVerseeATort = descriptionSaisieManuelleRenteVerseeATort;
    }

    @Override
    public void setId(String id) {
        ordreVersement.setId(id);
    }

    public void setIdCompensationInterDecisionNegative(String idCompensationInterDecisionNegative) {
        compensationInterDecisionNegative.setIdCompensationInterDecision(idCompensationInterDecisionNegative);
    }

    public void setIdCompensationInterDecisionPositive(String idCompensationInterDecisionPositive) {
        compensationInterDecisionPositive.setIdCompensationInterDecision(idCompensationInterDecisionPositive);
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        ordreVersement.setIdOrdreVersement(idOrdreVersement);
    }

    public void setIdPrestationAccordeeCompensee(String idPrestationAccordee) {
        renteAccordeeNouveauDroit.setIdPrestationAccordee(idPrestationAccordee);
    }

    public void setIdPrestationAccordeeDiminuee(String idPrestationAccordee) {
        renteAccordeeAncienDroit.setIdPrestationAccordee(idPrestationAccordee);
    }

    public void setIdRenteVerseeATort(String idRenteVerseeATort) {
        renteVerseeATort.setIdRenteVerseeATort(idRenteVerseeATort);
    }

    public void setIdSoldePourRestitution(String idSoldePourRestitution) {
        soldePourRestitution.setIdSoldePourRestitution(idSoldePourRestitution);
    }

    public void setIdTiersCIDNegative(String idTiers) {
        tiersCIDNegative.setIdTiers(idTiers);
    }

    public void setIdTiersCIDPositive(String idTiers) {
        tiersCIDPositive.setIdTiers(idTiers);
    }

    public void setIdTiersOrdreVersement(String idTiers) {
        tiersOrdreVersement.setIdTiers(idTiers);
    }

    public void setIsCompensationInterDecision(Boolean isCompensationInterDecision) {
        ordreVersement.setIsCompensationInterDecision(isCompensationInterDecision);
    }

    public void setIsCompense(Boolean isCompense) {
        ordreVersement.setIsCompense(isCompense);
    }

    public void setIsValide(Boolean isValide) {
        ordreVersement.setIsValide(isValide);
    }

    public void setMontant(String montant) {
        ordreVersement.setMontant(montant);
    }

    public void setMontantCompensationInterDecisionNegative(String montant) {
        compensationInterDecisionNegative.setMontant(montant);
    }

    public void setMontantCompensationInterDecisionPositive(String montant) {
        compensationInterDecisionPositive.setMontant(montant);
    }

    public void setMontantDette(String montantDette) {
        ordreVersement.setMontantDette(montantDette);
    }

    public void setMontantRetenueMensuelleSoldePourRestitution(String montant) {
        soldePourRestitution.setMontantMensuelARetenir(montant);
    }

    public void setMontantSoldePourRestitution(String montant) {
        soldePourRestitution.setMontant(montant);
    }

    public void setNoFacture(String noFacture) {
        ordreVersement.setNoFacture(noFacture);
    }

    public void setNomTiersCIDNegative(String nomTiers) {
        tiersCIDNegative.setDesignation1(nomTiers);
    }

    public void setNomTiersCIDPositive(String nomTiers) {
        tiersCIDPositive.setDesignation1(nomTiers);
    }

    public void setNomTiersOrdreVersement(String nomTiers) {
        tiersOrdreVersement.setDesignation1(nomTiers);
    }

    public void setPrenomTiersCIDNegative(String prenomTiers) {
        tiersCIDNegative.setDesignation2(prenomTiers);
    }

    public void setPrenomTiersCIDPositive(String prenomTiers) {
        tiersCIDPositive.setDesignation2(prenomTiers);
    }

    public void setPrenomTiersOrdreVersement(String prenomTiers) {
        tiersOrdreVersement.setDesignation2(prenomTiers);
    }

    @Override
    public void setSpy(String spy) {
        ordreVersement.setSpy(spy);
    }

    public String getIdRenteAccordeeAncienDroitRenteVerseeATort() {
        return renteVerseeATort.getIdRenteAccordeeAncienDroit();
    }

    public void setIdRenteAccordeeAncienDroitRenteVerseeATort(String idRenteAccordee) {
        renteVerseeATort.setIdRenteAccordeeAncienDroit(idRenteAccordee);
    }

    public String getCodePrestationRenteAccordeeAncienDroitRenteVerseeATort() {
        return renteAccordeeAncienDroitRenteVerseeATort.getCodePrestation();
    }

    public void setCodePrestationRenteAccordeeAncienDroitRenteVerseeATort(String codePrestation) {
        renteAccordeeAncienDroitRenteVerseeATort.setCodePrestation(codePrestation);
    }

    public String getDateDebutDroitRenteAccordeeAncienDroitRenteVerseeATort() {
        return renteAccordeeAncienDroitRenteVerseeATort.getDateDebutDroit();
    }

    public void setDateDebutDroitRenteAccordeeAncienDroitRenteVerseeATort(String dateDebutDroit) {
        renteAccordeeAncienDroitRenteVerseeATort.setDateDebutDroit(dateDebutDroit);
    }

    public String getDateFinDroitRenteAccordeeAncienDroitRenteVerseeATort() {
        return renteAccordeeAncienDroitRenteVerseeATort.getDateFinDroit();
    }

    public void setDateFinDroitRenteAccordeeAncienDroitRenteVerseeATort(String dateFinDroit) {
        renteAccordeeAncienDroitRenteVerseeATort.setDateFinDroit(dateFinDroit);
    }

    public boolean isRenteVerseeATortSaisieManuelle() {
        return renteVerseeATort.isSaisieManuelle();
    }

    public void setRenteVerseeATort_saisieManuelle(String _saisieManuelle) {
        renteVerseeATort.set_saisieManuelle(_saisieManuelle);
    }
}
