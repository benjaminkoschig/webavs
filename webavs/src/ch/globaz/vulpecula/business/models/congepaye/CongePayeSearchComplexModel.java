package ch.globaz.vulpecula.business.models.congepaye;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class CongePayeSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -5018018874828180527L;

    public static final String ORDERBY_ID_PASSAGE_FACTURATION_DESC = "idPassageFacturationDesc";
    public static final String ORDER_BY_CONVENTION_ASC = "conventionAsc";

    public static final String WHERE_WITHDATE = "withPeriode";

    private String forId;
    private String forIdTravailleur;
    private String forIdPassage;
    private String forIdEmployeur;
    private String forIdConvention;
    private String forDateDebut;
    private String forDateFin;

    /**
     * @return the forId
     */
    public String getForId() {
        return forId;
    }

    /**
     * @param forId the forId to set
     */
    public void setForId(String forId) {
        this.forId = forId;
    }

    @Override
    public Class<CongePayeComplexModel> whichModelClass() {
        return CongePayeComplexModel.class;
    }

    /**
     * @return the forIdTravailleur
     */
    public String getForIdTravailleur() {
        return forIdTravailleur;
    }

    /**
     * @param forIdTravailleur the forIdTravailleur to set
     */
    public void setForIdTravailleur(String forIdTravailleur) {
        this.forIdTravailleur = forIdTravailleur;
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public void setForIdPassage(String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    public String getForIdConvention() {
        return forIdConvention;
    }

    public void setForIdConvention(String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }
}
