package ch.globaz.pegasus.business.models.process.allocationsNoel;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.List;

public class CoupleSepareParLaMaladieSearch extends JadeSearchComplexModel {

    private List<String> inIdVersionDroit = new ArrayList<String>();
    private String forDateFin;

    @Override
    public Class<CoupleSepareParLaMaladie> whichModelClass() {
        return CoupleSepareParLaMaladie.class;
    }

    public List<String> getInIdVersionDroit() {
        return inIdVersionDroit;
    }

    public void setInIdVersionDroit(List<String> inIdVersionDroit) {
        this.inIdVersionDroit = inIdVersionDroit;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

}
