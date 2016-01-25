package ch.globaz.vulpecula.business.models.travailleur;

import globaz.jade.persistence.model.JadeSimpleModel;

public class TravailleurSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 4973405341749918097L;

    private Boolean annonceMeroba;
    private String dateAnnonceMeroba;
    private String genrePermisTravail;
    private String idTiers;
    private String id;
    private String referencePermis;

    public TravailleurSimpleModel() {
        super();
    }

    public Boolean getAnnonceMeroba() {
        return annonceMeroba;
    }

    public String getDateAnnonceMeroba() {
        return dateAnnonceMeroba;
    }

    public String getGenrePermisTravail() {
        return genrePermisTravail;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getReferencePermis() {
        return referencePermis;
    }

    public void setAnnonceMeroba(Boolean annonceMeroba) {
        this.annonceMeroba = annonceMeroba;
    }

    public void setDateAnnonceMeroba(String dateAnnonceMeroba) {
        this.dateAnnonceMeroba = dateAnnonceMeroba;
    }

    public void setGenrePermisTravail(String genrePermisTravail) {
        this.genrePermisTravail = genrePermisTravail;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setReferencePermis(String referencePermis) {
        this.referencePermis = referencePermis;
    }
}
