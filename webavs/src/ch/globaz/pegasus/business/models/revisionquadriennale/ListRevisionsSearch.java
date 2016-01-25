package ch.globaz.pegasus.business.models.revisionquadriennale;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ListRevisionsSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forMoisDateFin = null;
    private String forDateFin;
    private String forMoisAnnee = null;
    private String forMoisAnneeGreaterOrEquals = null;
    private String forMoisAnneeLessOrEquals = null;
    private String forDateDebut;

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForMoisAnnee() {
        return forMoisAnnee;
    }

    public String getForMoisAnneeGreaterOrEquals() {
        return forMoisAnneeGreaterOrEquals;
    }

    public String getForMoisAnneeLessOrEquals() {
        return forMoisAnneeLessOrEquals;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForMoisAnnee(String forMoisAnnee) {
        this.forMoisAnnee = forMoisAnnee;
    }

    public void setForMoisAnneeGreaterOrEquals(String forMoisAnneeGreaterOrEquals) {
        this.forMoisAnneeGreaterOrEquals = forMoisAnneeGreaterOrEquals;
    }

    public void setForMoisAnneeLessOrEquals(String forMoisAnneeLessOrEquals) {
        this.forMoisAnneeLessOrEquals = forMoisAnneeLessOrEquals;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    @Override
    public Class<ListRevisions> whichModelClass() {
        return ListRevisions.class;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public String getForMoisDateFin() {
        return forMoisDateFin;
    }

    public void setForMoisDateFin(String forMoisDateFin) {
        this.forMoisDateFin = forMoisDateFin;
    }

}
