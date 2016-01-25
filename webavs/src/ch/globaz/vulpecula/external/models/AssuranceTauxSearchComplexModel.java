package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Arrays;
import java.util.Collection;
import ch.globaz.vulpecula.domain.models.common.Date;

/**
 * @since WebBMS 0.4.1
 */
public class AssuranceTauxSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 5936809251986622678L;

    private Collection<String> forTypeAssurance = null;
    private Collection<String> forGenreAssurance = null;
    private String forDate = null;
    private String forIdAssurance = null;
    private String forTypeTaux = null;

    @Override
    public Class<AssuranceTauxComplexModel> whichModelClass() {
        return AssuranceTauxComplexModel.class;
    }

    /**
     * @return the forTypeAssurance
     */
    public Collection<String> getForTypeAssurance() {
        return forTypeAssurance;
    }

    /**
     * @return the forGenreAssurance
     */
    public Collection<String> getForGenreAssurance() {
        return forGenreAssurance;
    }

    /**
     * @param forTypeAssurance the forTypeAssurance to set
     */
    public void setForTypeAssurance(String forTypeAssurance) {
        this.forTypeAssurance = Arrays.asList(forTypeAssurance);
    }

    public void setForTypeAssurance(String... forTypeAssurance) {
        this.forTypeAssurance = Arrays.asList(forTypeAssurance);
    }

    /**
     * @param forGenreAssurance the forGenreAssurance to set
     */
    public void setForGenreAssurance(String forGenreAssurance) {
        this.forGenreAssurance = Arrays.asList(forGenreAssurance);
    }

    public void setForGenreAssurance(String... forGenreAssurance) {
        this.forGenreAssurance = Arrays.asList(forGenreAssurance);
    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setForDate(Date forDate) {
        if (forDate != null) {
            this.forDate = forDate.getSwissValue();
        }
    }

    public String getForIdAssurance() {
        return forIdAssurance;
    }

    public void setForIdAssurance(String forIdAssurance) {
        this.forIdAssurance = forIdAssurance;
    }

    public String getForTypeTaux() {
        return forTypeTaux;
    }

    public void setForTypeTaux(String forTypeTaux) {
        this.forTypeTaux = forTypeTaux;
    }
}
