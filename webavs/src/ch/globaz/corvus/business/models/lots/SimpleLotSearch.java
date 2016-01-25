/**
 * 
 */
package ch.globaz.corvus.business.models.lots;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

/**
 * @author BSC
 * 
 */
public class SimpleLotSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String LOT_FOR_PC = "lotForPC";
    private String forCsEtat = null;
    private String forCsProprietaire = null;
    private String forCsType = null;
    private String forCsType2 = null;
    private String forDateDebut = null;
    private String forDateFin = null;
    private String fromDateCreation = null;
    private List<String> inCsType = null;

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForCsProprietaire() {
        return forCsProprietaire;
    }

    public String getForCsType() {
        return forCsType;
    }

    public String getForCsType2() {
        return forCsType2;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getFromDateCreation() {
        return fromDateCreation;
    }

    public List<String> getInCsType() {
        return inCsType;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForCsProprietaire(String forCsProprietaire) {
        this.forCsProprietaire = forCsProprietaire;
    }

    public void setForCsType(String forCsType) {
        this.forCsType = forCsType;
    }

    public void setForCsType2(String forCsType2) {
        this.forCsType2 = forCsType2;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setFromDateCreation(String fromDateCreation) {
        this.fromDateCreation = fromDateCreation;
    }

    public void setInCsType(List<String> inCsType) {
        this.inCsType = inCsType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleLot.class;
    }

}
