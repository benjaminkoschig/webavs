package ch.globaz.pegasus.business.models.renteijapi;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

public class SimpleIndemniteJournaliereAiSearch extends JadeSearchSimpleModel {

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
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    /**
     * @param forListIdDonneeFinanciere
     *            the forListIdDonneeFinanciere to set
     */
    public void setForListIdDonneeFinanciere(List<String> forListIdDonneeFinanciere) {
        this.forListIdDonneeFinanciere = forListIdDonneeFinanciere;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    @Override
    public Class whichModelClass() {
        return SimpleIndemniteJournaliereAi.class;
    }

}
