package ch.globaz.vulpecula.domain.models.association;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;

public class AssociationEmployeur implements DomainEntity {
    private String id;
    private String spy;
    private String idAssociation;
    private Montant forfaitAssociation;
    private Montant masseAssociation;
    private String idEmployeur;

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

    public Montant getForfaitAssociation() {
        return forfaitAssociation;
    }

    public void setForfaitAssociation(Montant forfaitAssociation) {
        this.forfaitAssociation = forfaitAssociation;
    }

    public Montant getMasseAssociation() {
        return masseAssociation;
    }

    public void setMasseAssociation(Montant masseAssociation) {
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

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }
}
