package ch.globaz.vulpecula.business.models.servicemilitaire;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ServiceMilitaireSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 1L;

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

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdTravailleur() {
        return forIdTravailleur;
    }

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

    @Override
    public Class<ServiceMilitaireComplexModel> whichModelClass() {
        return ServiceMilitaireComplexModel.class;
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
