package ch.globaz.corvus.business.models.rentesaccordees;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

/**
 * @author BSC
 */
public class SimplePrestationsAccordeesSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String GROUPE_ID_PRESTATION_WHERE_KEY = "groupe_id_prestation";
    private List<String> forCodePrestation = null;
    private String forDateDebutDroit = null;
    private String forDateFinDroit = null;
    private String forIdPrestation = null;
    private String forIdTiersBeneficiaire = null;
    private List<String> forInIdPrestation = null;

    public List<String> getForCodePrestation() {
        return forCodePrestation;
    }

    public String getForDateDebutDroit() {
        return forDateDebutDroit;
    }

    public String getForDateFinDroit() {
        return forDateFinDroit;
    }

    public String getForIdPrestation() {
        return forIdPrestation;
    }

    public String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    public List<String> getForInIdPrestation() {
        return forInIdPrestation;
    }

    public void setForCodePrestation(List<String> forCodePrestation) {
        this.forCodePrestation = forCodePrestation;
    }

    public void setForDateDebutDroit(String forDateDebutDroit) {
        this.forDateDebutDroit = forDateDebutDroit;
    }

    public void setForDateFinDroit(String forDateFinDroit) {
        this.forDateFinDroit = forDateFinDroit;
    }

    public void setForIdPrestation(String forIdPrestation) {
        this.forIdPrestation = forIdPrestation;
    }

    public void setForIdTiersBeneficiaire(String forIdTiersBeneficiaire) {
        this.forIdTiersBeneficiaire = forIdTiersBeneficiaire;
    }

    public void setForInIdPrestation(List<String> forInIdPrestation) {
        this.forInIdPrestation = forInIdPrestation;
    }

    @Override
    public Class<SimplePrestationsAccordees> whichModelClass() {
        return SimplePrestationsAccordees.class;
    }
}
