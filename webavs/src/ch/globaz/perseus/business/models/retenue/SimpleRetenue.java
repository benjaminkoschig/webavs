package ch.globaz.perseus.business.models.retenue;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author LFO
 * 
 */
public class SimpleRetenue extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String baremeIS = null;
    private String csTypeRetenue = null;
    private String dateDebutRetenue = null;
    private String dateFinRetenue = null;
    private String idCompteAnnexe = null;
    private String idDomaineApplicatif = null;
    private String idExterneSection = null;
    private String idParentRetenue = null;
    private String idPcfAccordee = null;
    private String idRetenue = null;
    private String idTauxIS = null;
    private String idTiersAdressePmt = null;
    private String idTypeSection = null;
    private String montantDejaRetenu = null;
    private String montantRetenuMensuel = null;
    private String montantTotalARetenir = null;
    private String tauxImposition = null;

    /**
     * @return the baremeIS
     */
    public String getBaremeIS() {
        return baremeIS;
    }

    /**
     * @return the csTypeRetenue
     */
    public String getCsTypeRetenue() {
        return csTypeRetenue;
    }

    /**
     * @return the dateDebutRetenue
     */
    public String getDateDebutRetenue() {
        return dateDebutRetenue;
    }

    /**
     * @return the dateFinRetenue
     */
    public String getDateFinRetenue() {
        return dateFinRetenue;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return getIdRetenue();
    }

    /**
     * @return the idCompteAnnexe
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return the idDomaineApplicatif
     */
    public String getIdDomaineApplicatif() {
        return idDomaineApplicatif;
    }

    /**
     * @return the idExterneSection
     */
    public String getIdExterneSection() {
        return idExterneSection;
    }

    /**
     * @return the idParentRetenue
     */
    public String getIdParentRetenue() {
        return idParentRetenue;
    }

    /**
     * @return the idPcfAccordee
     */
    public String getIdPcfAccordee() {
        return idPcfAccordee;
    }

    /**
     * @return the idRetenue
     */
    public String getIdRetenue() {
        return idRetenue;
    }

    /**
     * @return the idTauxIS
     */
    public String getIdTauxIS() {
        return idTauxIS;
    }

    /**
     * @return the idTiersAdressePmt
     */
    public String getIdTiersAdressePmt() {
        return idTiersAdressePmt;
    }

    /**
     * @return the typeSection
     */
    public String getIdTypeSection() {
        return idTypeSection;
    }

    /**
     * @return the montantDejaRetenu
     */
    public String getMontantDejaRetenu() {
        return montantDejaRetenu;
    }

    /**
     * @return the montantRetenuMensuel
     */
    public String getMontantRetenuMensuel() {
        return montantRetenuMensuel;
    }

    /**
     * @return the montantTotalARetenir
     */
    public String getMontantTotalARetenir() {
        return montantTotalARetenir;
    }

    /**
     * @return the tauxImposition
     */
    public String getTauxImposition() {
        return tauxImposition;
    }

    /**
     * @param baremeIS
     *            the baremeIS to set
     */
    public void setBaremeIS(String baremeIS) {
        this.baremeIS = baremeIS;
    }

    /**
     * @param csTypeRetenue
     *            the csTypeRetenue to set
     */
    public void setCsTypeRetenue(String csTypeRetenue) {
        this.csTypeRetenue = csTypeRetenue;
    }

    /**
     * @param dateDebutRetenue
     *            the dateDebutRetenue to set
     */
    public void setDateDebutRetenue(String dateDebutRetenue) {
        this.dateDebutRetenue = dateDebutRetenue;
    }

    /**
     * @param dateFinRetenue
     *            the dateFinRetenue to set
     */
    public void setDateFinRetenue(String dateFinRetenue) {
        this.dateFinRetenue = dateFinRetenue;
    }

    @Override
    public void setId(String id) {
        idRetenue = id;
    }

    /**
     * @param idCompteAnnexe
     *            the idCompteAnnexe to set
     */
    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param idDomaineApplicatif
     *            the idDomaineApplicatif to set
     */
    public void setIdDomaineApplicatif(String idDomaineApplicatif) {
        this.idDomaineApplicatif = idDomaineApplicatif;
    }

    /**
     * @param idExterneSection
     *            the idExterneSection to set
     */
    public void setIdExterneSection(String idExterneSection) {
        this.idExterneSection = idExterneSection;
    }

    /**
     * @param idParentRetenue
     *            the idParentRetenue to set
     */
    public void setIdParentRetenue(String idParentRetenue) {
        this.idParentRetenue = idParentRetenue;
    }

    /**
     * @param idPcfAccordee
     *            the idPcfAccordee to set
     */
    public void setIdPcfAccordee(String idPcfAccordee) {
        this.idPcfAccordee = idPcfAccordee;
    }

    /**
     * @param idRetenue
     *            the idRetenue to set
     */
    public void setIdRetenue(String idRetenue) {
        this.idRetenue = idRetenue;
    }

    /**
     * @param idTauxIS
     *            the idTauxIS to set
     */
    public void setIdTauxIS(String idTauxIS) {
        this.idTauxIS = idTauxIS;
    }

    /**
     * @param idTiersAdressePmt
     *            the idTiersAdressePmt to set
     */
    public void setIdTiersAdressePmt(String idTiersAdressePmt) {
        this.idTiersAdressePmt = idTiersAdressePmt;
    }

    /**
     * @param typeSection
     *            the typeSection to set
     */
    public void setIdTypeSection(String idTypeSection) {
        this.idTypeSection = idTypeSection;
    }

    /**
     * @param montantDejaRetenu
     *            the montantDejaRetenu to set
     */
    public void setMontantDejaRetenu(String montantDejaRetenu) {
        this.montantDejaRetenu = montantDejaRetenu;
    }

    /**
     * @param montantRetenuMensuel
     *            the montantRetenuMensuel to set
     */
    public void setMontantRetenuMensuel(String montantRetenuMensuel) {
        this.montantRetenuMensuel = montantRetenuMensuel;
    }

    /**
     * @param montantTotalARetenir
     *            the montantTotalARetenir to set
     */
    public void setMontantTotalARetenir(String montantTotalARetenir) {
        this.montantTotalARetenir = montantTotalARetenir;
    }

    /**
     * @param tauxImposition
     *            the tauxImposition to set
     */
    public void setTauxImposition(String tauxImposition) {
        this.tauxImposition = tauxImposition;
    }

}
