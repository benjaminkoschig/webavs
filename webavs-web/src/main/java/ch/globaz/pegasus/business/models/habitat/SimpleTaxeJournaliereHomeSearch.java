package ch.globaz.pegasus.business.models.habitat;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

public class SimpleTaxeJournaliereHomeSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdTypeChambre = null;
    private List<String> forListIdDonneeFinanciere = null;
    private String forIdDonneeFinanciere = null;

    public String getForIdTypeChambre() {
        return forIdTypeChambre;
    }

    /**
     * @return the forListIdDonneeFinanciere
     */
    public List<String> getForListIdDonneeFinanciere() {
        return forListIdDonneeFinanciere;
    }

    public void setForIdTypeChambre(String forIdTypeChambre) {
        this.forIdTypeChambre = forIdTypeChambre;
    }

    /**
     * @param forListIdDonneeFinanciere
     *            the forListIdDonneeFinanciere to set
     */
    public void setForListIdDonneeFinanciere(List<String> forListIdDonneeFinanciere) {
        this.forListIdDonneeFinanciere = forListIdDonneeFinanciere;
    }

    @Override
    public Class<SimpleTaxeJournaliereHome> whichModelClass() {
        return SimpleTaxeJournaliereHome.class;
    }

    public String getForIdDonneeFinanciere() {
        return forIdDonneeFinanciere;
    }

    public void setForIdDonneeFinanciere(String forIdDonneeFinanciere) {
        this.forIdDonneeFinanciere = forIdDonneeFinanciere;
    }

}
