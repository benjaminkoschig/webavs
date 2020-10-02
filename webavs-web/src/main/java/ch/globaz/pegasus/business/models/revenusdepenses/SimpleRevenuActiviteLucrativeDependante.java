package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author FHA
 * 
 */
public class SimpleRevenuActiviteLucrativeDependante extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String autreFraisObtentionRevenu = null;
    private String csFraisObtentionRevenu = null;
    private String csGenreRevenu = null;
    private String deductionsLpp = null;
    private String deductionsSociales = null;
    private String idAffiliation = null;
    private String idDonneeFinanciereHeader = null;
    private String idEmployeur = null;
    private String idRevenuActiviteLucrativeDependante = null;
    private String montantActiviteLucrative = null;
    private String montantFrais = null;
    private String fraisDeGarde = null;
    private String typeRevenu = null;

    public String getAutreFraisObtentionRevenu() {
        return autreFraisObtentionRevenu;
    }

    public String getCsFraisObtentionRevenu() {
        return csFraisObtentionRevenu;
    }

    public String getCsGenreRevenu() {
        return csGenreRevenu;
    }

    public String getDeductionsLpp() {
        return deductionsLpp;
    }

    public String getDeductionsSociales() {
        return deductionsSociales;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idRevenuActiviteLucrativeDependante;
    }

    /**
     * @return the idAffiliation
     */
    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public String getIdRevenuActiviteLucrativeDependante() {
        return idRevenuActiviteLucrativeDependante;
    }

    public String getMontantActiviteLucrative() {
        return montantActiviteLucrative;
    }

    public String getMontantFrais() {
        return montantFrais;
    }

    public String getTypeRevenu() {
        return typeRevenu;
    }

    public void setAutreFraisObtentionRevenu(String autreFraisObtentionRevenu) {
        this.autreFraisObtentionRevenu = autreFraisObtentionRevenu;
    }

    public void setCsFraisObtentionRevenu(String csFraisObtentionRevenu) {
        this.csFraisObtentionRevenu = csFraisObtentionRevenu;
    }

    public void setCsGenreRevenu(String csGenreRevenu) {
        this.csGenreRevenu = csGenreRevenu;
    }

    public void setDeductionsLpp(String deductionsLpp) {
        this.deductionsLpp = deductionsLpp;
    }

    public void setDeductionsSociales(String deductionsSociales) {
        this.deductionsSociales = deductionsSociales;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idRevenuActiviteLucrativeDependante = id;
    }

    /**
     * @param idAffiliation
     *            the idAffiliation to set
     */
    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public void setIdRevenuActiviteLucrativeDependante(String idRevenuActiviteLucrativeDependante) {
        this.idRevenuActiviteLucrativeDependante = idRevenuActiviteLucrativeDependante;
    }

    public void setMontantActiviteLucrative(String montantActiviteLucrative) {
        this.montantActiviteLucrative = montantActiviteLucrative;
    }

    public void setMontantFrais(String montantFrais) {
        this.montantFrais = montantFrais;
    }

    public void setTypeRevenu(String typeRevenu) {
        this.typeRevenu = typeRevenu;
    }

    public String getFraisDeGarde() {
        return fraisDeGarde;
    }

    public void setFraisDeGarde(String fraisDeGarde) {
        this.fraisDeGarde = fraisDeGarde;
    }
}
