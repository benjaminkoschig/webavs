package ch.globaz.pegasus.business.models.habitat;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class HabitatSearch extends JadeSearchComplexModel {
    // private String forDateDebut = null;
    // private String forDateFin = null;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String FOR_ALL_VALABLE_LE = "forAllValableHabitat";

    private String forDateValable = null;
    private String forIdDroit = null;
    private String forIdEntity = null;
    private String forIdMembre = null;
    private String forNumeroVersion = null;
    private List<String> inCsTypeDonneeFinancierer = null;

    public String getForDateValable() {
        return forDateValable;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @return the forIdEntity
     */
    public String getForIdEntity() {
        return forIdEntity;
    }

    /**
     * @return the forIdMembre
     */
    public String getForIdMembre() {
        return forIdMembre;
    }

    /**
     * @return the forNumeroVersion
     */
    public String getForNumeroVersion() {
        return forNumeroVersion;
    }

    public List<String> getInCsTypeDonneeFinancierer() {
        return inCsTypeDonneeFinancierer;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * @param forIdEntity
     *            the forIdEntity to set
     */
    public void setForIdEntity(String forIdEntity) {
        this.forIdEntity = forIdEntity;
    }

    /**
     * @param forIdMembre
     *            the forIdMembre to set
     */
    public void setForIdMembre(String forIdMembre) {
        this.forIdMembre = forIdMembre;
    }

    /**
     * @param forNumeroVersion
     *            the forNumeroVersion to set
     */
    public void setForNumeroVersion(String forNumeroVersion) {
        this.forNumeroVersion = forNumeroVersion;
    }

    public void setInCsTypeDonneeFinancierer(List<String> inCsTypeDonneeFinancierer) {
        this.inCsTypeDonneeFinancierer = inCsTypeDonneeFinancierer;
    }

    @Override
    public Class<Habitat> whichModelClass() {
        return Habitat.class;
    }

}
