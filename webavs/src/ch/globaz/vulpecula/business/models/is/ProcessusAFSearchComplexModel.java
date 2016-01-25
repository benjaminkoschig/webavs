package ch.globaz.vulpecula.business.models.is;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

public class ProcessusAFSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -2530948099089038424L;

    private String forId;
    private String forEtat;
    private String forBusinessProcessus;
    private Boolean forIsPartiel;
    private Collection<String> forIdsNotIn;
    private String forDatePeriode;

    @Override
    public Class<ProcessusAFComplexModel> whichModelClass() {
        return ProcessusAFComplexModel.class;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForEtat() {
        return forEtat;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public String getForBusinessProcessus() {
        return forBusinessProcessus;
    }

    public void setForBusinessProcessus(String forBusinessProcessus) {
        this.forBusinessProcessus = forBusinessProcessus;
    }

    public Boolean getForIsPartiel() {
        return forIsPartiel;
    }

    public void setForIsPartiel(String value) {
        if ("true".equals(value)) {
            forIsPartiel = true;
        } else {
            forIsPartiel = false;
        }
    }

    public void setForIsPartiel(Boolean forIsPartiel) {
        this.forIsPartiel = forIsPartiel;
    }

    public Collection<String> getForIdsNotIn() {
        return forIdsNotIn;
    }

    public void setForIdsNotIn(Collection<String> forIdsNotIn) {
        this.forIdsNotIn = forIdsNotIn;
    }

    public String getForDatePeriode() {
        return forDatePeriode;
    }

    public void setForDatePeriode(String forDatePeriode) {
        this.forDatePeriode = forDatePeriode;
    }
}
