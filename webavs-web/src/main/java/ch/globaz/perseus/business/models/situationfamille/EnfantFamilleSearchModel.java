package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;

public class EnfantFamilleSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String betweenDateNaissanceDebut = null;
    private String betweenDateNaissanceFin = null;
    private ArrayList<String> forCsFormationIn = null;
    private String forIdSituationFamiliale = null;

    public String getBetweenDateNaissanceDebut() {
        return betweenDateNaissanceDebut;
    }

    public String getBetweenDateNaissanceFin() {
        return betweenDateNaissanceFin;
    }

    public ArrayList<String> getForCsFormationIn() {
        return forCsFormationIn;
    }

    /**
     * @return the forIdSituationFamiliale
     */
    public String getForIdSituationFamiliale() {
        return forIdSituationFamiliale;
    }

    public void setBetweenDateNaissanceDebut(String betweenDateNaissanceDebut) {
        this.betweenDateNaissanceDebut = betweenDateNaissanceDebut;
    }

    public void setBetweenDateNaissanceFin(String betweenDateNaissanceFin) {
        this.betweenDateNaissanceFin = betweenDateNaissanceFin;
    }

    public void setForCsFormationIn(ArrayList<String> forCsFormationIn) {
        this.forCsFormationIn = forCsFormationIn;
    }

    /**
     * @param forIdSituationFamiliale
     *            the forIdSituationFamiliale to set
     */
    public void setForIdSituationFamiliale(String forIdSituationFamiliale) {
        this.forIdSituationFamiliale = forIdSituationFamiliale;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return EnfantFamille.class;
    }

}
