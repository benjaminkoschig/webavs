package ch.globaz.vulpecula.external.models.musca;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;

/**
 * Représente un passage de facturation dans le module associé.
 * 
 */
public class Passage implements DomainEntity {
    private String id;
    private String libelle;
    private String idTypeFacturation;
    private String dateFacturation;
    private String status;
    private String spy;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getIdTypeFacturation() {
        return idTypeFacturation;
    }

    public void setIdTypeFacturation(String idTypeFacturation) {
        this.idTypeFacturation = idTypeFacturation;
    }

    public String getDateFacturation() {
        return dateFacturation;
    }

    public void setDateFacturation(String dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
