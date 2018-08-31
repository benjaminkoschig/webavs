package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeSimpleModel;

public class AssociationEmployeurSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -4476704137892710557L;

    private String id;
    private String idEmployeur;
    private String idAssociation;
    private String forfaitAssociation;
    private String masseAssociation;

    public AssociationEmployeurSimpleModel() {
        super();
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public String getIdAssociation() {
        return idAssociation;
    }

    public void setIdAssociation(String idAssociation) {
        this.idAssociation = idAssociation;
    }

    public String getForfaitAssociation() {
        return forfaitAssociation;
    }

    public void setForfaitAssociation(String forfaitAssociation) {
        this.forfaitAssociation = forfaitAssociation;
    }

    public String getMasseAssociation() {
        return masseAssociation;
    }

    public void setMasseAssociation(String masseAssociation) {
        this.masseAssociation = masseAssociation;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
