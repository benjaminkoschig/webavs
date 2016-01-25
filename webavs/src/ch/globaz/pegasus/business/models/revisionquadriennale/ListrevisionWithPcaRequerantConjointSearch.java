package ch.globaz.pegasus.business.models.revisionquadriennale;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.List;

public class ListrevisionWithPcaRequerantConjointSearch extends JadeSearchComplexModel {

    private static final long serialVersionUID = 1L;
    private String forDateFin = null;
    private String forDateDebut;
    private String forCsEtatDroit;
    private List<String> foInIdDroit = new ArrayList<String>();
    private String forMoisDateFin;
    private String forMoisAnneeGreaterOrEquals;
    private String forMoisAnneeLessOrEquals;

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

    public String getForMoisAnneeGreaterOrEquals() {
        return forMoisAnneeGreaterOrEquals;
    }

    public void setForMoisAnneeGreaterOrEquals(String forMoisAnneeGreaterOrEquals) {
        this.forMoisAnneeGreaterOrEquals = forMoisAnneeGreaterOrEquals;
    }

    public String getForMoisAnneeLessOrEquals() {
        return forMoisAnneeLessOrEquals;
    }

    public void setForMoisAnneeLessOrEquals(String forMoisAnneeLessOrEquals) {
        this.forMoisAnneeLessOrEquals = forMoisAnneeLessOrEquals;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    @Override
    public Class<ListrevisionWithPcaRequerantConjoint> whichModelClass() {
        return ListrevisionWithPcaRequerantConjoint.class;
    }

    public String getForCsEtatDroit() {
        return forCsEtatDroit;
    }

    public void setForCsEtatDroit(String forCsEtatDroit) {
        this.forCsEtatDroit = forCsEtatDroit;
    }

    public List<String> getFoInIdDroit() {
        return foInIdDroit;
    }

    public void setFoInIdDroit(List<String> foInIdDroit) {
        this.foInIdDroit = foInIdDroit;
    }

}
