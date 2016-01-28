package ch.globaz.lyra.business.models.historique;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class LYSimpleHistoriqueSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String depuisDate;
    private String forCsEtat;
    private String forIdEcheances;
    private String forIdHistorique;
    private String forVisaUtilisateur;

    public LYSimpleHistoriqueSearchModel() {
        super();
    }

    public String getDepuisDate() {
        return depuisDate;
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForIdEcheances() {
        return forIdEcheances;
    }

    public String getForIdHistorique() {
        return forIdHistorique;
    }

    public String getForVisaUtilisateur() {
        return forVisaUtilisateur;
    }

    public void setDepuisDate(String depuisDate) {
        this.depuisDate = depuisDate;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForIdEcheances(String forIdEcheances) {
        this.forIdEcheances = forIdEcheances;
    }

    public void setForIdHistorique(String forIdHistorique) {
        this.forIdHistorique = forIdHistorique;
    }

    public void setForVisaUtilisateur(String forVisaUtilisateur) {
        this.forVisaUtilisateur = forVisaUtilisateur;
    }

    @Override
    public Class<?> whichModelClass() {
        return LYSimpleHistorique.class;
    }
}
