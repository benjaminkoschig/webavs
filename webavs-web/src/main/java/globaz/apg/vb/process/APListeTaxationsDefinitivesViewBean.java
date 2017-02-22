/*
 * Créé le 15.09.2006
 */
package globaz.apg.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * 
 * @author mmo
 */
public class APListeTaxationsDefinitivesViewBean extends PRAbstractViewBeanSupport {

    /**
     * Partie année de taxation
     */
    private String anneeTaxationCP = "";
    private String dateDebutDecisionsCP = "";
    private String dateFinDecisionsCP = "";

    /**
     * Partie année de droit APG/Amat
     */
    private String anneeDroit = "";
    private String dateDebutDecompte = "";
    private String dateFinDecompte = "";
    private Boolean inclureAffilieRadie = false;

    /**
     * Partie commune
     */
    private String startWithNoAffilie = "";
    private String endWithNoAffilie = "";
    private String eMailAddress = "";

    @Override
    public boolean validate() {
        return true;
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

    public String geteMailAddress() {
        return eMailAddress;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
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
}
