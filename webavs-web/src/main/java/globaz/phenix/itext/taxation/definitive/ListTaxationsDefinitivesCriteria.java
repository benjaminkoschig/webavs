package globaz.phenix.itext.taxation.definitive;

import java.io.Serializable;

public class ListTaxationsDefinitivesCriteria implements Serializable {

    private static final long serialVersionUID = 4132497865692850822L;

    private String noPassage;

    /**
     * Partie ann?e de taxation
     */
    private String anneeTaxationCP = null;
    private String dateDebutDecisionsCP = null;
    private String dateFinDecisionsCP = null;

    /**
     * Partie ann?e de droit APG/Amat
     */
    private String anneeDroit = null;
    private String dateDebutDecompte = null;
    private String dateFinDecompte = null;
    private Boolean inclureAffilieRadie = false;

    /**
     * Partie commune
     */
    private String startWithNoAffilie = null;
    private String endWithNoAffilie = null;

    public String getNoPassage() {
        return noPassage;
    }

    public void setNoPassage(String noPassage) {
        this.noPassage = noPassage;
    }

    public String getAnneeTaxationCP() {
        return anneeTaxationCP;
    }

    public void setAnneeTaxationCP(String anneeTaxationCP) {
        this.anneeTaxationCP = anneeTaxationCP;
    }

    public String getDateDebutDecisionsCP() {
        return dateDebutDecisionsCP;
    }

    public void setDateDebutDecisionsCP(String dateDebutDecisionsCP) {
        this.dateDebutDecisionsCP = dateDebutDecisionsCP;
    }

    public String getDateFinDecisionsCP() {
        return dateFinDecisionsCP;
    }

    public void setDateFinDecisionsCP(String dateFinDecisionsCP) {
        this.dateFinDecisionsCP = dateFinDecisionsCP;
    }

    public String getAnneeDroit() {
        return anneeDroit;
    }

    public void setAnneeDroit(String anneeDroit) {
        this.anneeDroit = anneeDroit;
    }

    public String getDateDebutDecompte() {
        return dateDebutDecompte;
    }

    public void setDateDebutDecompte(String dateDebutDecompte) {
        this.dateDebutDecompte = dateDebutDecompte;
    }

    public String getDateFinDecompte() {
        return dateFinDecompte;
    }

    public void setDateFinDecompte(String dateFinDecompte) {
        this.dateFinDecompte = dateFinDecompte;
    }

    public Boolean getInclureAffilieRadie() {
        return inclureAffilieRadie;
    }

    public void setInclureAffilieRadie(Boolean inclureAffilieRadie) {
        this.inclureAffilieRadie = inclureAffilieRadie;
    }

    public String getStartWithNoAffilie() {
        return startWithNoAffilie;
    }

    public void setStartWithNoAffilie(String startWithNoAffilie) {
        this.startWithNoAffilie = startWithNoAffilie;
    }

    public String getEndWithNoAffilie() {
        return endWithNoAffilie;
    }

    public void setEndWithNoAffilie(String endWithNoAffilie) {
        this.endWithNoAffilie = endWithNoAffilie;
    }

    @Override
    public String toString() {
        return "ListTaxationsDefinitivesCriteria [noPassage=" + noPassage + "]";
    }
}
