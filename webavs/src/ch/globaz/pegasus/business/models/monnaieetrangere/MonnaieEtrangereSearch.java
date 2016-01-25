package ch.globaz.pegasus.business.models.monnaieetrangere;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class MonnaieEtrangereSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsTypeMonnaie = null; // Code CS de la monnaie
    private String forDateValable = null; // date de validité

    /**
     * @return the forCsTypeMonnaie
     */
    public String getForCsTypeMonnaie() {
        return forCsTypeMonnaie;
    }

    /**
     * @return the forDateValable
     */
    public String getForDateValable() {
        return forDateValable;
    }

    /**
     * @param forCsTypeMonnaie
     *            the forCsTypeMonnaie to set
     */
    public void setForCsTypeMonnaie(String forCsTypeMonnaie) {
        this.forCsTypeMonnaie = forCsTypeMonnaie;
    }

    /**
     * @param forDateValable
     *            the forDateValable to set
     */
    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    @Override
    public Class whichModelClass() {
        return MonnaieEtrangere.class;
    }

}
