package ch.globaz.pegasus.business.models.renteijapi;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class RenteMembreFamilleCalculeFieldSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ORDER_DROIT = "idDroit";
    public final static String WITH_DATE_VALABLE = "withDateValable";

    private String forCsEtatDemande;
    private String forCsEtatVersionDroit;
    private String forDate;
    private String forDateFin;
    private String forIdDemandePC;
    private boolean forIsPlanRetenu = false;
    private List<String> inCsEtatPca;
    private List<String> inCsEtatVersionDroit;
    private List<String> inCsTypeDonneeFinanciere;

    public String getForCsEtatDemande() {
        return forCsEtatDemande;
    }

    public String getForCsEtatVersionDroit() {
        return forCsEtatVersionDroit;
    }

    public String getForDate() {
        return forDate;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdDemandePC() {
        return forIdDemandePC;
    }

    public boolean getForIsPlanRetenu() {
        return forIsPlanRetenu;
    }

    public List<String> getInCsEtatPca() {
        return inCsEtatPca;
    }

    public List<String> getInCsEtatVersionDroit() {
        return inCsEtatVersionDroit;
    }

    public List getInCsTypeDonneeFinanciere() {
        return inCsTypeDonneeFinanciere;
    }

    public void setForCsEtatDemande(String forCsEtatDemande) {
        this.forCsEtatDemande = forCsEtatDemande;
    }

    public void setForCsEtatVersionDroit(String forCsEtatVersionDroit) {
        this.forCsEtatVersionDroit = forCsEtatVersionDroit;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdDemandePC(String forIdDemandePC) {
        this.forIdDemandePC = forIdDemandePC;
    }

    public void setForIsPlanRetenu(boolean forIsPlanRetenu) {
        this.forIsPlanRetenu = forIsPlanRetenu;
    }

    public void setInCsEtatPca(List<String> inCsEtatPca) {
        this.inCsEtatPca = inCsEtatPca;
    }

    public void setInCsEtatVersionDroit(List<String> inCsEtatVersionDroit) {
        this.inCsEtatVersionDroit = inCsEtatVersionDroit;
    }

    public void setInCsTypeDonneeFinanciere(List inCsTypeDonneeFinanciere) {
        this.inCsTypeDonneeFinanciere = inCsTypeDonneeFinanciere;
    }

    @Override
    public Class<RenteMembreFamilleCalculeField> whichModelClass() {
        return RenteMembreFamilleCalculeField.class;
    }

}
