package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.List;

public class CalculDonneesHomeSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String SEARCH_HOME_FOR_LAPRAMS = "searchHomeForLAPRAMS";
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forIdDroit = null;
    private String forIdHome = null;
    private String forIdTypeChambre = null;

    private String forIdVersionDroit = null;

    private List<String> inIdTypeChambre = new ArrayList<String>();

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdHome() {
        return forIdHome;
    }

    public String getForIdTypeChambre() {
        return forIdTypeChambre;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public List<String> getInIdTypeChambre() {
        return inIdTypeChambre;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdHome(String forIdHome) {
        this.forIdHome = forIdHome;
    }

    public void setForIdTypeChambre(String forIdTypeChambre) {
        this.forIdTypeChambre = forIdTypeChambre;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setInIdTypeChambre(List<String> inIdTypeChambre) {
        this.inIdTypeChambre = inIdTypeChambre;
    }

    @Override
    public Class whichModelClass() {
        return CalculDonneesHome.class;
    }

}
