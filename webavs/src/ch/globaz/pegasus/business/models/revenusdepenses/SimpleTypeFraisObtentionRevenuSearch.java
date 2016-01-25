package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

public class SimpleTypeFraisObtentionRevenuSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdRevenuActiviteLucrativeDependante = null;
    private String forListIdDonneeFinanciere = null;
    private List<String> inIdRevenuActiviteLucrativeDependante = null;

    public String getForIdRevenuActiviteLucrativeDependante() {
        return forIdRevenuActiviteLucrativeDependante;
    }

    /**
     * @return the forListIdDonneeFinanciere
     */
    public String getForListIdDonneeFinanciere() {
        return forListIdDonneeFinanciere;
    }

    public void setForIdRevenuActiviteLucrativeDependante(String forIdRevenuActiviteLucrativeDependante) {
        this.forIdRevenuActiviteLucrativeDependante = forIdRevenuActiviteLucrativeDependante;
    }

    /**
     * @param forListIdDonneeFinanciere
     *            the forListIdDonneeFinanciere to set
     */
    public void setForListIdDonneeFinanciere(String forListIdDonneeFinanciere) {
        this.forListIdDonneeFinanciere = forListIdDonneeFinanciere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<SimpleTypeFraisObtentionRevenu> whichModelClass() {
        return SimpleTypeFraisObtentionRevenu.class;
    }

    public List<String> getInIdRevenuActiviteLucrativeDependante() {
        return inIdRevenuActiviteLucrativeDependante;
    }

    public void setInIdRevenuActiviteLucrativeDependante(List<String> inIdRevenuActiviteLucrativeDependante) {
        this.inIdRevenuActiviteLucrativeDependante = inIdRevenuActiviteLucrativeDependante;
    }

}
