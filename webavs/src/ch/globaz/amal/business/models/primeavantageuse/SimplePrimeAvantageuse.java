/**
 * 
 */
package ch.globaz.amal.business.models.primeavantageuse;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author CBU
 * 
 */
public class SimplePrimeAvantageuse extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeSubside = null;
    private String idPrimeAvantageuse = null;
    private String montantPrimeAdulte = null;
    private String montantPrimeEnfant = null;
    private String montantPrimeFormation = null;

    public String getAnneeSubside() {
        return anneeSubside;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idPrimeAvantageuse;
    }

    public String getIdPrimeAvantageuse() {
        return idPrimeAvantageuse;
    }

    public String getMontantPrimeAdulte() {
        return montantPrimeAdulte;
    }

    public String getMontantPrimeEnfant() {
        return montantPrimeEnfant;
    }

    public String getMontantPrimeFormation() {
        return montantPrimeFormation;
    }

    public void setAnneeSubside(String anneeSubside) {
        this.anneeSubside = anneeSubside;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idPrimeAvantageuse = id;
    }

    public void setIdPrimeAvantageuse(String idPrimeAvantageuse) {
        this.idPrimeAvantageuse = idPrimeAvantageuse;
    }

    public void setMontantPrimeAdulte(String montantPrimeAdulte) {
        this.montantPrimeAdulte = montantPrimeAdulte;
    }

    public void setMontantPrimeEnfant(String montantPrimeEnfant) {
        this.montantPrimeEnfant = montantPrimeEnfant;
    }

    public void setMontantPrimeFormation(String montantPrimeFormation) {
        this.montantPrimeFormation = montantPrimeFormation;
    }

}
