package ch.globaz.pegasus.business.models.dettecomptatcompense;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDetteComptatCompense extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDetteComptatCompense = null;
    private String idSectionDetteEnCompta = null;
    private String idVersionDroit = null;
    private String montant = null;
    private String montantModifie = null;

    @Override
    public String getId() {
        return idDetteComptatCompense;
    }

    public String getIdDetteComptatCompense() {
        return idDetteComptatCompense;
    }

    public String getIdSectionDetteEnCompta() {
        return idSectionDetteEnCompta;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantModifie() {
        return montantModifie;
    }

    @Override
    public void setId(String id) {
        idDetteComptatCompense = id;
    }

    public void setIdDetteComptatCompense(String idDetteComptatCompense) {
        this.idDetteComptatCompense = idDetteComptatCompense;
    }

    public void setIdSectionDetteEnCompta(String idSectionDetteEnCompta) {
        this.idSectionDetteEnCompta = idSectionDetteEnCompta;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantModifie(String montantModifie) {
        this.montantModifie = montantModifie;
    }
}
