package ch.globaz.pegasus.business.models.habitat;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

public class SimpleLoyerSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> forListIdDonneeFinanciere = null;

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
    public Class<SimpleLoyer> whichModelClass() {
        return SimpleLoyer.class;
    }

}
