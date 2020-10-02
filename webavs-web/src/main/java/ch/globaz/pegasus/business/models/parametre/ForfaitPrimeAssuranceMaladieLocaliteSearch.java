package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.Collection;

public class ForfaitPrimeAssuranceMaladieLocaliteSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String WITH_PERIODE_CS_TYPE_PRIME = "withPeriodeCsTypePrime";

    private String forCsTypePrime;
    private String forDateDebut;
    private String forDateFin;
    private Collection<String> forIdsLocalite;
    private String forDateValable;
    private String forIdLocalite;
    private String forType = null;

    /**
     * @return the forCsTypePrime
     */
    public String getForCsTypePrime() {
        return forCsTypePrime;
    }

    /**
     * @return the forDateDebut
     */
    public String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * @return the forDateFin
     */
    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * @return the forDateValable
     */
    public String getForDateValable() {
        return forDateValable;
    }

    /**
     * @return the forIdLocalite
     */
    public String getForIdLocalite() {
        return forIdLocalite;
    }

    /**
     * @param forCsTypePrime
     *            the forCsTypePrime to set
     */
    public void setForCsTypePrime(String forCsTypePrime) {
        this.forCsTypePrime = forCsTypePrime;
    }

    /**
     * @param forDateDebut
     *            the forDateDebut to set
     */
    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    /**
     * @param forDateFin
     *            the forDateFin to set
     */
    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * @param forDateValable
     *            the forDateValable to set
     */
    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    /**
     * @param forIdLocalite
     *            the forIdLocalite to set
     */
    public void setForIdLocalite(String forIdLocalite) {
        this.forIdLocalite = forIdLocalite;
    }

    public Collection<String> getForIdsLocalite() {
        return forIdsLocalite;
    }

    public void setForIdsLocalite(Collection<String> forIdsLocalite) {
        this.forIdsLocalite = forIdsLocalite;
    }

    public String getForType() {
        return forType;
    }

    public void setForType(String forType) {
        this.forType = forType;
    }

    @Override
    public Class<ForfaitPrimeAssuranceMaladieLocalite> whichModelClass() {
        return ForfaitPrimeAssuranceMaladieLocalite.class;
    }
}
