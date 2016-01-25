package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

public class SimpleContratEntretienViagerSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdContratEntretienViager = null;
    private List<String> forListIdDonneeFinanciere = null;

    public String getForIdContratEntretienViager() {
        return forIdContratEntretienViager;
    }

    /**
     * @return the forListIdDonneeFinanciere
     */
    public List<String> getForListIdDonneeFinanciere() {
        return forListIdDonneeFinanciere;
    }

    public void setForIdContratEntretienViager(String forIdContratEntretienViager) {
        this.forIdContratEntretienViager = forIdContratEntretienViager;
    }

    /**
     * @param forListIdDonneeFinanciere
     *            the forListIdDonneeFinanciere to set
     */
    public void setForListIdDonneeFinanciere(List<String> forListIdDonneeFinanciere) {
        this.forListIdDonneeFinanciere = forListIdDonneeFinanciere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleContratEntretienViager.class;
    }

}
