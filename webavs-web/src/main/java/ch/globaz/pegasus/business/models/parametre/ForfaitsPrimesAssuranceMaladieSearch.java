package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author DMA
 * @date 12 nov. 2010
 */
public class ForfaitsPrimesAssuranceMaladieSearch extends JadeSearchComplexModel {
    // private String forAnneeValidite = null;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String WITH_DATE_VALABLE_LE = "withDateValable";

    private String forCsTypePrime = null;
    private String forDateValable = null;
    private String forIdForfaitsPrimesAssuranceMaladie = null;

    private String forIdZoneForfaits = null;
    private String forType = null;

    /**
     * @return the forCsTypePrime
     */
    public String getForCsTypePrime() {
        return forCsTypePrime;
    }

    public String getForDateValable() {
        return forDateValable;
    }

    /**
     * @return the forIdForfaitsPrimesAssuranceMaladie
     */
    public String getForIdForfaitsPrimesAssuranceMaladie() {
        return forIdForfaitsPrimesAssuranceMaladie;
    }

    /**
     * @return the forIdZoneFofaits
     */
    public String getForIdZoneForfaits() {
        return forIdZoneForfaits;
    }

    /**
     * @param forCsTypePrime
     *            the forCsTypePrime to set
     */
    public void setForCsTypePrime(String forCsTypePrime) {
        this.forCsTypePrime = forCsTypePrime;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    /**
     * @param forIdForfaitsPrimesAssuranceMaladie
     *            the forIdForfaitsPrimesAssuranceMaladie to set
     */
    public void setForIdForfaitsPrimesAssuranceMaladie(String forIdForfaitsPrimesAssuranceMaladie) {
        this.forIdForfaitsPrimesAssuranceMaladie = forIdForfaitsPrimesAssuranceMaladie;
    }

    /**
     * @param forIdZoneFofaits
     *            the forIdZoneFofaits to set
     */
    public void setForIdZoneForfaits(String forIdZoneFofaits) {
        forIdZoneForfaits = forIdZoneFofaits;
    }

    public String getForType() {
        return forType;
    }

    public void setForType(String forType) {
        this.forType = forType;
    }

    @Override
    public Class<ForfaitsPrimesAssuranceMaladie> whichModelClass() {
        return ForfaitsPrimesAssuranceMaladie.class;
    }
}
