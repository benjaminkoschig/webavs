package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeSearchComplexModel;

import java.util.List;

public class CalculDonneesFraisGardeSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forDateDebut = null;

    private String forDateFin = null;

    private String forIdDroit = null;


    private List forIdDroitMembreFamille = null;
    private List<String> inCsTypeDonneeFinanciere = null;

    private String forNotCsTypeDonneeFinanciere= null;

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }



    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }


    public List<String> getInCsTypeDonneeFinanciere() {
        return inCsTypeDonneeFinanciere;
    }

    public void setInCsTypeDonneeFinanciere(List<String> inCsTypeDonneeFinanciere) {
        this.inCsTypeDonneeFinanciere = inCsTypeDonneeFinanciere;
    }

    @Override
    public Class whichModelClass() {
        return CalculDonneesFraisGarde.class;
    }

    public List getForIdDroitMembreFamille() {
        return forIdDroitMembreFamille;
    }

    public void setForIdDroitMembreFamille(List forIdDroitMembreFamille) {
        this.forIdDroitMembreFamille = forIdDroitMembreFamille;
    }
    public String getForNotCsTypeDonneeFinanciere() {
        return forNotCsTypeDonneeFinanciere;
    }

    public void setForNotCsTypeDonneeFinanciere(String forNotCsTypeDonneeFinanciere) {
        this.forNotCsTypeDonneeFinanciere = forNotCsTypeDonneeFinanciere;
    }
}
