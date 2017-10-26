package ch.globaz.pegasus.business.models.revisionquadriennale;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;
import java.util.List;
import ch.globaz.common.sql.SQLWriter;

public class DonneeFinanciereSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebut;
    private String forDateFin;
    private String forIdDroit;
    private List<String> forIdMembreFamilleSFIn;
    private Collection<String> forIdDroitIn;
    private List<String> forCsTypeIn;
    private Collection<String> forIdsVersionDroit;

    public Collection<String> getForIdDroitIn() {
        return forIdDroitIn;
    }

    public void setForIdDroitIn(Collection<String> forIdDroitIn) {
        this.forIdDroitIn = forIdDroitIn;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @return the forIdMembreFamilleSFIn
     */
    public List<String> getForIdMembreFamilleSFIn() {
        return forIdMembreFamilleSFIn;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * @param forIdMembreFamilleSFIn
     *            the forIdMembreFamilleSFIn to set
     */
    public void setForIdMembreFamilleSFIn(List<String> forIdMembreFamilleSFIn) {
        this.forIdMembreFamilleSFIn = forIdMembreFamilleSFIn;
    }

    @Override
    public Class<DonneeFinanciereComplexModel> whichModelClass() {
        return DonneeFinanciereComplexModel.class;
    }

    public List<String> getForCsTypeIn() {
        return forCsTypeIn;
    }

    public void setForCsTypeIn(List<String> forCsTypeIn) {
        this.forCsTypeIn = forCsTypeIn;
    }

    public String getForIdsVersionDroitInString() {
        return SQLWriter.toStrForIn(forIdsVersionDroit);
    }

    public Collection<String> getForIdsVersionDroit() {
        return forIdsVersionDroit;
    }

    public void setForIdsVersionDroit(Collection<String> forIdsVersionDroit) {
        this.forIdsVersionDroit = forIdsVersionDroit;
    }

}
