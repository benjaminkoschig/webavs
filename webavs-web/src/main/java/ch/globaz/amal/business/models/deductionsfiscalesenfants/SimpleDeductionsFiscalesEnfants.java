/**
 * 
 */
package ch.globaz.amal.business.models.deductionsfiscalesenfants;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author CBU
 * 
 */
public class SimpleDeductionsFiscalesEnfants extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeTaxation = null;
    private String idDeductionFiscaleEnfant = null;
    private String montantDeductionParEnfant = null;
    private String montantDeductionTotal = null;
    private String nbEnfant = null;

    public String getAnneeTaxation() {
        return anneeTaxation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idDeductionFiscaleEnfant;
    }

    /**
     * @return
     */
    public String getIdDeductionFiscaleEnfant() {
        return idDeductionFiscaleEnfant;
    }

    /**
     * @return
     */
    public String getMontantDeductionParEnfant() {
        return montantDeductionParEnfant;
    }

    /**
     * @return
     */
    public String getMontantDeductionTotal() {
        return montantDeductionTotal;
    }

    /**
     * @return
     */
    public String getNbEnfant() {
        return nbEnfant;
    }

    /**
     * @param anneeTaxation
     */
    public void setAnneeTaxation(String anneeTaxation) {
        this.anneeTaxation = anneeTaxation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDeductionFiscaleEnfant = id;
    }

    /**
     * @param idDeductionFiscaleEnfant
     */
    public void setIdDeductionFiscaleEnfant(String idDeductionFiscaleEnfant) {
        this.idDeductionFiscaleEnfant = idDeductionFiscaleEnfant;
    }

    /**
     * @param montantDeductionParEnfant
     */
    public void setMontantDeductionParEnfant(String montantDeductionParEnfant) {
        this.montantDeductionParEnfant = montantDeductionParEnfant;
    }

    /**
     * @param montantDeductionTotal
     */
    public void setMontantDeductionTotal(String montantDeductionTotal) {
        this.montantDeductionTotal = montantDeductionTotal;
    }

    /**
     * @param nbEnfant
     */
    public void setNbEnfant(String nbEnfant) {
        this.nbEnfant = nbEnfant;
    }
}
