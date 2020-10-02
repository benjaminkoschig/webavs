package ch.globaz.pegasus.business.models.habitat;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

import java.util.List;

public class SimpleSejourMoisPartielHomeSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> forListIdDonneeFinanciere = null;
    private String forIdDonneeFinanciere = null;


    /**
     * @return the forListIdDonneeFinanciere
     */
    public List<String> getForListIdDonneeFinanciere() {
        return forListIdDonneeFinanciere;
    }

    /**
     * @param forListIdDonneeFinanciere
     *            the forListIdDonneeFinanciere to set
     */
    public void setForListIdDonneeFinanciere(List<String> forListIdDonneeFinanciere) {
        this.forListIdDonneeFinanciere = forListIdDonneeFinanciere;
    }

    @Override
    public Class<SimpleSejourMoisPartielHome> whichModelClass() {
        return SimpleSejourMoisPartielHome.class;
    }

    public String getForIdDonneeFinanciere() {
        return forIdDonneeFinanciere;
    }

    public void setForIdDonneeFinanciere(String forIdDonneeFinanciere) {
        this.forIdDonneeFinanciere = forIdDonneeFinanciere;
    }

}
