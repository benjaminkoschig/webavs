package ch.globaz.vulpecula.business.models.congepaye;

import globaz.jade.persistence.model.JadeSimpleModel;

public class LigneCompteurSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 1L;

    private String id;
    private String idCompteur;
    private String idCongePaye;
    private String montant;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdCompteur() {
        return idCompteur;
    }

    public void setIdCompteur(String idCompteur) {
        this.idCompteur = idCompteur;
    }

    public String getIdCongePaye() {
        return idCongePaye;
    }

    public void setIdCongePaye(String idCongePaye) {
        this.idCongePaye = idCongePaye;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}
