package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleForfaitPrimesAssuranceMaladieSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeValidite = null;
    private String forCsTypePrime = null;
    private String forIdZoneForfaits = null;

    /**
     * @return the forAnneeValidite
     */
    public String getForAnneeValidite() {
        return forAnneeValidite;
    }

    /**
     * @return the forCsTypePrime
     */
    public String getForCsTypePrime() {
        return forCsTypePrime;
    }

    /**
     * @return the forIdZoneForfaits
     */
    public String getForIdZoneForfaits() {
        return forIdZoneForfaits;
    }

    /**
     * @param forAnneeValidite
     *            the forAnneeValidite to set
     */
    public void setForAnneeValidite(String forAnneeValidite) {
        this.forAnneeValidite = forAnneeValidite;
    }

    /**
     * @param forCsTypePrime
     *            the forCsTypePrime to set
     */
    public void setForCsTypePrime(String forCsTypePrime) {
        this.forCsTypePrime = forCsTypePrime;
    }

    /**
     * @param forIdZoneForfaits
     *            the forIdZoneForfaits to set
     */
    public void setForIdZoneForfaits(String forIdZoneForfaits) {
        this.forIdZoneForfaits = forIdZoneForfaits;
    }

    @Override
    public Class<SimpleForfaitPrimesAssuranceMaladie> whichModelClass() {
        return SimpleForfaitPrimesAssuranceMaladie.class;
    }

}
