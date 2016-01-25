package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeComplexModel;

public class TransfertTucanaComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String cantonAffilie = null;
    private String idAllocataire = null;
    private String idTucana = null;
    private String montant = null;
    private String montantCaisse = null;
    private String montantCanton = null;
    private String numBouclement = null;
    private String rubriqueAllocation = null;
    private String rubriqueSupplementConventionnel = null;
    private String rubriqueSupplementLegal = null;
    private String typePrestation = null;

    public String getCantonAffilie() {
        return cantonAffilie;
    }

    @Override
    public String getId() {
        return idTucana;
    }

    public String getIdAllocataire() {
        return idAllocataire;
    }

    public String getIdTucana() {
        return idTucana;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantCaisse() {
        return montantCaisse;
    }

    public String getMontantCanton() {
        return montantCanton;
    }

    public String getNumBouclement() {
        return numBouclement;
    }

    public String getRubriqueAllocation() {
        return rubriqueAllocation;
    }

    public String getRubriqueSupplementConventionnel() {
        return rubriqueSupplementConventionnel;
    }

    public String getRubriqueSupplementLegal() {
        return rubriqueSupplementLegal;
    }

    @Override
    public String getSpy() {
        return null;
    }

    public String getTypePrestation() {
        return typePrestation;
    }

    public void setCantonAffilie(String cantonAffilie) {
        this.cantonAffilie = cantonAffilie;
    }

    @Override
    public void setId(String id) {
        // DO NOTHING
    }

    public void setIdAllocataire(String idAllocataire) {
        this.idAllocataire = idAllocataire;
    }

    public void setIdTucana(String idTucana) {
        this.idTucana = idTucana;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantCaisse(String montantCaisse) {
        this.montantCaisse = montantCaisse;
    }

    public void setMontantCanton(String montantCanton) {
        this.montantCanton = montantCanton;
    }

    public void setNumBouclement(String numBouclement) {
        this.numBouclement = numBouclement;
    }

    public void setRubriqueAllocation(String rubriqueAllocation) {
        this.rubriqueAllocation = rubriqueAllocation;
    }

    public void setRubriqueSupplementConventionnel(String rubriqueSupplementConventionnel) {
        this.rubriqueSupplementConventionnel = rubriqueSupplementConventionnel;
    }

    public void setRubriqueSupplementLegal(String rubriqueSupplementLegal) {
        this.rubriqueSupplementLegal = rubriqueSupplementLegal;
    }

    @Override
    public void setSpy(String spy) {
        // DO NOTHING
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }
}