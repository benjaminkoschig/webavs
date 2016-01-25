package ch.globaz.pegasus.business.models.renteijapi;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleAutreApi extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String autre = null;
    private String csDegre = null;
    private String csGenre = null;
    private String csType = null;
    private String idAutreApi = null;
    private String idDonneeFinanciereHeader = null;
    private String montant = null;

    /**
     * @return the autre
     */
    public String getAutre() {
        return autre;
    }

    /**
     * @return the csDegre
     */
    public String getCsDegre() {
        return csDegre;
    }

    /**
     * @return the csGenre
     */
    public String getCsGenre() {
        return csGenre;
    }

    /**
     * @return the csType
     */
    public String getCsType() {
        return csType;
    }

    @Override
    public String getId() {
        return getIdAutreApi();
    }

    /**
     * @return the idAutreApi
     */
    public String getIdAutreApi() {
        return idAutreApi;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @param autre
     *            the autre to set
     */
    public void setAutre(String autre) {
        this.autre = autre;
    }

    /**
     * @param csDegre
     *            the csDegre to set
     */
    public void setCsDegre(String csDegre) {
        this.csDegre = csDegre;
    }

    /**
     * @param csGenre
     *            the csGenre to set
     */
    public void setCsGenre(String csGenre) {
        this.csGenre = csGenre;
    }

    /**
     * @param csType
     *            the csType to set
     */
    public void setCsType(String csType) {
        this.csType = csType;
    }

    @Override
    public void setId(String id) {
        setIdAutreApi(id);

    }

    /**
     * @param idAutreApi
     *            the idAutreApi to set
     */
    public void setIdAutreApi(String idAutreApi) {
        this.idAutreApi = idAutreApi;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

}
