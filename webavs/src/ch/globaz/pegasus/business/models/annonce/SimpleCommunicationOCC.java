/**
 * 
 */
package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author BSC Modele simple pour les ordres de versement
 */
public class SimpleCommunicationOCC extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateEffet = null;
    private String dateFinEffet = null;
    private String dateRapport = null;
    private String etatPC = null;
    private String idCommunicationOCC = null;
    private String idDonneesPersonnelles = null;
    private String idDonneesPersonnellesRequerant = null;
    private String idTiers = null;
    private String idTiersRequerant = null;
    private String motif = null;
    private String noSerie = null;
    private String idVersionDroit = null;
    private String idLocalite = null;

    public String getDateEffet() {
        return dateEffet;
    }

    public String getDateFinEffet() {
        return dateFinEffet;
    }

    public String getDateRapport() {
        return dateRapport;
    }

    public String getEtatPC() {
        return etatPC;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idCommunicationOCC;
    }

    public String getIdCommunicationOCC() {
        return idCommunicationOCC;
    }

    public String getIdDonneesPersonnelles() {
        return idDonneesPersonnelles;
    }

    public String getIdDonneesPersonnellesRequerant() {
        return idDonneesPersonnellesRequerant;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public String getMotif() {
        return motif;
    }

    public String getNoSerie() {
        return noSerie;
    }

    public void setDateEffet(String dateEffet) {
        this.dateEffet = dateEffet;
    }

    public void setDateFinEffet(String dateFinEffet) {
        this.dateFinEffet = dateFinEffet;
    }

    public void setDateRapport(String dateRapport) {
        this.dateRapport = dateRapport;
    }

    public void setEtatPC(String etatPC) {
        this.etatPC = etatPC;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idCommunicationOCC = id;

    }

    public void setIdCommunicationOCC(String idCommunicationOCC) {
        this.idCommunicationOCC = idCommunicationOCC;
    }

    public void setIdDonneesPersonnelles(String idDonneesPersonnelles) {
        this.idDonneesPersonnelles = idDonneesPersonnelles;
    }

    public void setIdDonneesPersonnellesRequerant(String idDonneesPersonnellesRequerant) {
        this.idDonneesPersonnellesRequerant = idDonneesPersonnellesRequerant;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNoSerie(String noSerie) {
        this.noSerie = noSerie;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public String getIdLocalite() {
        return idLocalite;
    }

    public void setIdLocalite(String idLocalite) {
        this.idLocalite = idLocalite;
    }

}
