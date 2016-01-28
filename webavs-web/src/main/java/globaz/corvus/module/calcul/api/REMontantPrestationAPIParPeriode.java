package globaz.corvus.module.calcul.api;

import java.math.BigDecimal;

public class REMontantPrestationAPIParPeriode {

    private String dateDebut;
    private String dateFin;
    private String idPeriode = null;
    private BigDecimal montant = null;
    private String typePrestation = "";
    private String csGenreDroitApi = "";
    private String codeCasSpecial = "";

    public String getCodeCasSpecial() {
        return codeCasSpecial;
    }

    public void setCodeCasSpecial(String codeCasSpecial) {
        this.codeCasSpecial = codeCasSpecial;
    }

    public REMontantPrestationAPIParPeriode() {

    }

    public REMontantPrestationAPIParPeriode(REMontantPrestationAPIParPeriode elm) {
        dateDebut = elm.getDateDebut();
        dateFin = elm.getDateFin();
        montant = new BigDecimal(elm.getMontant().toString());
        idPeriode = elm.getIdPeriode();
        typePrestation = elm.getTypePrestation();
        csGenreDroitApi = elm.getCsGenreDroitApi();
        codeCasSpecial = elm.getCodeCasSpecial();
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    public String getCsGenreDroitApi() {
        return csGenreDroitApi;
    }

    public void setCsGenreDroitApi(String csGenreDroitApi) {
        this.csGenreDroitApi = csGenreDroitApi;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return the idPeriode
     */
    public String getIdPeriode() {
        return idPeriode;
    }

    /**
     * @return the montant
     */
    public BigDecimal getMontant() {
        return montant;
    }

    /**
     * @return the typePrestation
     */
    public String getTypePrestation() {
        return typePrestation;
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

    /**
     * @param idPeriode
     *            the idPeriode to set
     */
    public void setIdPeriode(String idPeriode) {
        this.idPeriode = idPeriode;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    /**
     * @param typePrestation
     *            the typePrestation to set
     */
    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }
}
