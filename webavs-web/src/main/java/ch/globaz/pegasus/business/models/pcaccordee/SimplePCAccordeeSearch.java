package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimplePCAccordeeSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String WITH_IS_SUPPRIME = "WITH_IS_SUPPRIME";
    private String forIdPCAccordee = null;

    private String forIdPrestationsAccordee = null;
    private String forIdVersionDroit = null;
    private Boolean forIsCalculRetro = null;
    private Boolean forIsSupprime = false;
    private Boolean forDateFinPca = null;

    /**
     * @return the forIdPCAccordee
     */
    public String getForIdPCAccordee() {
        return forIdPCAccordee;
    }

    public String getForIdPrestationsAccordee() {
        return forIdPrestationsAccordee;
    }

    /**
     * @return the forIdVersionDroit
     */
    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public Boolean getForIsCalculRetro() {
        return forIsCalculRetro;
    }

    /**
     * @param forIdPCAccordee
     *            the forIdPCAccordee to set
     */
    public void setForIdPCAccordee(String forIdPCAccordee) {
        this.forIdPCAccordee = forIdPCAccordee;
    }

    public void setForIdPrestationsAccordee(String forIdPrestationsAccordee) {
        this.forIdPrestationsAccordee = forIdPrestationsAccordee;
    }

    /**
     * @param forIdVersionDroit
     *            the forIdVersionDroit to set
     */
    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setForIsCalculRetro(Boolean forIsCalculRetro) {
        this.forIsCalculRetro = forIsCalculRetro;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<SimplePCAccordee> whichModelClass() {
        return SimplePCAccordee.class;
    }

    public Boolean getForIsSupprime() {
        return forIsSupprime;
    }

    public void setForIsSupprime(Boolean forIsSupprime) {
        this.forIsSupprime = forIsSupprime;
    }

    public Boolean getForDateFinPca() {
        return forDateFinPca;
    }

    public void setForDateFinPca(Boolean forDateFinPca) {
        this.forDateFinPca = forDateFinPca;
    }

}
