package ch.globaz.pegasus.business.models.creancier;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleCreanceAccordee extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCreanceAccordee = null;
    private String idCreancier = null;
    private String idOrdreVersement = null;
    private String idPCAccordee = null;
    private String montant = null;

    @Override
    public String getId() {
        return idCreanceAccordee;
    }

    /**
     * @return the idCreanceAccordee
     */
    public String getIdCreanceAccordee() {
        return idCreanceAccordee;
    }

    /**
     * @return the idCreancier
     */
    public String getIdCreancier() {
        return idCreancier;
    }

    /**
     * @return the idOrdreVersement
     */
    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    /**
     * @return the idPCAccordee
     */
    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    @Override
    public void setId(String id) {
        idCreanceAccordee = id;
    }

    /**
     * @param idCreanceAccordee
     *            the idCreanceAccordee to set
     */
    public void setIdCreanceAccordee(String idCreanceAccordee) {
        this.idCreanceAccordee = idCreanceAccordee;
    }

    /**
     * @param idCreancier
     *            the idCreancier to set
     */
    public void setIdCreancier(String idCreancier) {
        this.idCreancier = idCreancier;
    }

    /**
     * @param idOrdreVersement
     *            the idOrdreVersement to set
     */
    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    /**
     * @param idPCAccordee
     *            the idPCAccordee to set
     */
    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

}
