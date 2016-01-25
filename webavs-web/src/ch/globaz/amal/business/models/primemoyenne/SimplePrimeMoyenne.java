/**
 * 
 */
package ch.globaz.amal.business.models.primemoyenne;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author CBU
 * 
 */
public class SimplePrimeMoyenne extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeSubside = null;
    private String idPrimeMoyenne = null;
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
        return idPrimeMoyenne;
    }

    public String getIdPrimeMoyenne() {
        return idPrimeMoyenne;
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
        idPrimeMoyenne = id;
    }

    public void setIdPrimeMoyenne(String idPrimeMoyenne) {
        this.idPrimeMoyenne = idPrimeMoyenne;
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
