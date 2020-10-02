package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleForfaitPrimesAssuranceMaladie extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypePrime = null;
    private String dateDebut = null;
    private String dateFin = null;
    private String idForfaitPrimesAssuranceMaladie = null;
    private String idZoneForfait = null;
    private String montantPrimeMoy = null;
    private String montantPrimeReductionMaxCanton = null;

    /**
     * @return the csTypePrime
     */
    public String getCsTypePrime() {
        return csTypePrime;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getId() {
        return idForfaitPrimesAssuranceMaladie;
    }

    /**
     * @return the idForfaitPrimesAssuranceMaladie
     */
    public String getIdForfaitPrimesAssuranceMaladie() {
        return idForfaitPrimesAssuranceMaladie;
    }

    /**
     * @return the idZoneForfait
     */
    public String getIdZoneForfait() {
        return idZoneForfait;
    }

    /**
     * @return the montant
     */
    public String getMontantPrimeReductionMaxCanton() {
        return montantPrimeReductionMaxCanton;
    }

    /**
     * @return the montant
     */
    public String getMontantPrimeMoy() {
        return montantPrimeMoy;
    }

    /**
     * @param csTypePrime
     *            the csTypePrime to set
     */
    public void setCsTypePrime(String csTypePrime) {
        this.csTypePrime = csTypePrime;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setId(String id) {
        idForfaitPrimesAssuranceMaladie = id;
    }

    /**
     * @param idForfaitPrimesAssuranceMaladie
     *            the idForfaitPrimesAssuranceMaladie to set
     */
    public void setIdForfaitPrimesAssuranceMaladie(String idForfaitPrimesAssuranceMaladie) {
        this.idForfaitPrimesAssuranceMaladie = idForfaitPrimesAssuranceMaladie;
    }

    /**
     * @param idZoneForfait
     *            the idZoneForfait to set
     */
    public void setIdZoneForfait(String idZoneForfait) {
        this.idZoneForfait = idZoneForfait;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontantPrimeMoy(String montant) {
        this.montantPrimeMoy = montant;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontantPrimeReductionMaxCanton(String montant) {
        this.montantPrimeReductionMaxCanton = montant;
    }

}
