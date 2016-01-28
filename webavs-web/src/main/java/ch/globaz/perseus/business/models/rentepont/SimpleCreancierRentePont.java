package ch.globaz.perseus.business.models.rentepont;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author MBO
 * 
 */

public class SimpleCreancierRentePont extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypeCreance = null;
    private String idCreancier = null;
    private String idDomaineApplicatif = null;
    private String idOrdreVersement = null;
    private String idRentePont = null;
    private String idTiers = null;
    private String montantAccorde = null;
    private String montantRevendique = null;
    private String referencePaiement = null;

    public SimpleCreancierRentePont() {
        super();
    }

    public String getCsTypeCreance() {
        return csTypeCreance;
    }

    @Override
    public String getId() {
        return idCreancier;
    }

    public String getIdCreancier() {
        return idCreancier;
    }

    public String getIdDomaineApplicatif() {
        return idDomaineApplicatif;
    }

    /**
     * @return the idOrdreVersement
     */
    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    /**
     * @return the idRentePont
     */
    public String getIdRentePont() {
        return idRentePont;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return the montantAccorde
     */
    public String getMontantAccorde() {
        return montantAccorde;
    }

    public String getMontantRevendique() {
        return montantRevendique;
    }

    public String getReferencePaiement() {
        return referencePaiement;
    }

    public void setCsTypeCreance(String csTypeCreance) {
        this.csTypeCreance = csTypeCreance;
    }

    @Override
    public void setId(String id) {
        idCreancier = id;
    }

    public void setIdCreancier(String idCreancier) {
        this.idCreancier = idCreancier;
    }

    public void setIdDomaineApplicatif(String idDomaineApplicatif) {
        this.idDomaineApplicatif = idDomaineApplicatif;
    }

    /**
     * @param idOrdreVersement
     *            the idOrdreVersement to set
     */
    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    /**
     * @param idRentePont
     *            the idRentePont to set
     */
    public void setIdRentePont(String idRentePont) {
        this.idRentePont = idRentePont;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @param montantAccorde
     *            the montantAccorde to set
     */
    public void setMontantAccorde(String montantAccorde) {
        this.montantAccorde = montantAccorde;
    }

    public void setMontantRevendique(String montantRevendique) {
        this.montantRevendique = montantRevendique;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

}
