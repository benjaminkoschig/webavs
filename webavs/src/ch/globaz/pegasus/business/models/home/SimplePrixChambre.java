package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimplePrixChambre extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String dateDebut = null;

    private String dateFin = null;
    private String idPrixChambre = null;
    private String idTypeChambre = null;
    private String prixJournalier = null;

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getId() {
        return idPrixChambre;
    }

    /**
     * @return the idPrixChambre
     */
    public String getIdPrixChambre() {
        return idPrixChambre;
    }

    /**
     * @return the idTypeChambre
     */
    public String getIdTypeChambre() {
        return idTypeChambre;
    }

    /**
     * @return the prixJournalier
     */
    public String getPrixJournalier() {
        return prixJournalier;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setId(String id) {
        idPrixChambre = id;
    }

    /**
     * @param idPrixChambre
     *            the idPrixChambre to set
     */
    public void setIdPrixChambre(String idPrixChambre) {
        this.idPrixChambre = idPrixChambre;
    }

    /**
     * @param idTypeChambre
     *            the idTypeChambre to set
     */
    public void setIdTypeChambre(String idTypeChambre) {
        this.idTypeChambre = idTypeChambre;
    }

    /**
     * @param prixJournalier
     *            the prixJournalier to set
     */
    public void setPrixJournalier(String prixJournalier) {
        this.prixJournalier = prixJournalier;
    }

}
