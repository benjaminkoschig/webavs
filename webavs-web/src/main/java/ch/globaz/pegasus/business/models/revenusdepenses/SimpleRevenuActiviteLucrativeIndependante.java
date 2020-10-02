package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author FHA
 * 
 */
public class SimpleRevenuActiviteLucrativeIndependante extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csDeterminationRevenu = null;
    private String csGenreRevenu = null;
    private String idAffiliation = null;
    private String idCaisseCompensation = null;
    private String idDonneeFinanciereHeader = null;
    private String idRevenuActiviteLucrativeIndependante = null;
    private String idTiersAffilie = null;
    private String fraisDeGarde = null;
    private String montantRevenu = null;

    public String getCsDeterminationRevenu() {
        return csDeterminationRevenu;
    }

    public String getCsGenreRevenu() {
        return csGenreRevenu;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idRevenuActiviteLucrativeIndependante;
    }

    /**
     * @return the idAffiliation
     */
    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdCaisseCompensation() {
        return idCaisseCompensation;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getIdRevenuActiviteLucrativeIndependante() {
        return idRevenuActiviteLucrativeIndependante;
    }

    public String getIdTiersAffilie() {
        return idTiersAffilie;
    }

    public String getMontantRevenu() {
        return montantRevenu;
    }

    public void setCsDeterminationRevenu(String csDeterminationRevenu) {
        this.csDeterminationRevenu = csDeterminationRevenu;
    }

    public void setCsGenreRevenu(String csGenreRevenu) {
        this.csGenreRevenu = csGenreRevenu;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idRevenuActiviteLucrativeIndependante = id;
    }

    /**
     * @param idAffiliation
     *            the idAffiliation to set
     */
    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdCaisseCompensation(String idCaisseCompensation) {
        this.idCaisseCompensation = idCaisseCompensation;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setIdRevenuActiviteLucrativeIndependante(String idRevenuActiviteLucrativeIndependante) {
        this.idRevenuActiviteLucrativeIndependante = idRevenuActiviteLucrativeIndependante;
    }

    public void setIdTiersAffilie(String idTiersAffilie) {
        this.idTiersAffilie = idTiersAffilie;
    }

    public void setMontantRevenu(String montantRevenu) {
        this.montantRevenu = montantRevenu;
    }
    public String getFraisDeGarde() {
        return fraisDeGarde;
    }

    public void setFraisDeGarde(String fraisDeGarde) {
        this.fraisDeGarde = fraisDeGarde;
    }
}
