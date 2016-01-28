package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

public class LienAffiliationSimpleModel extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String aff_AffiliationId;
    private String affiliationId;
    private String dateDebut;
    private String dateFin;
    private String lienAffiliationId;
    private String typeLien;

    public String getAff_AffiliationId() {
        return aff_AffiliationId;
    }

    public String getAffiliationId() {
        return affiliationId;
    }

    /*
     * Getter et Setter
     */

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    /*
     * ID
     */
    @Override
    public String getId() {
        return getLienAffiliationId();
    }

    public String getLienAffiliationId() {
        return lienAffiliationId;
    }

    public String getTypeLien() {
        return typeLien;
    }

    public void setAff_AffiliationId(String aff_AffiliationId) {
        this.aff_AffiliationId = aff_AffiliationId;
    }

    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setId(String id) {
        setLienAffiliationId(id);
    }

    public void setLienAffiliationId(String lienAffiliationId) {
        this.lienAffiliationId = lienAffiliationId;
    }

    public void setTypeLien(String typeLien) {
        this.typeLien = typeLien;
    }

}
