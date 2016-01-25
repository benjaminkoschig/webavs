package ch.globaz.hera.business.models.famille;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class RelationConjointSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String WITH_DATE_VALABLE = "withDateValable";

    private String forDateValable = null;
    private String forIdConjoint1 = null;
    private String forIdConjoint2 = null;
    public final static String FOR_DATA_VALABLE_WHERE_KEY = "withDateValable";

    public String getForDateValable() {
        return forDateValable;
    }

    /**
     * getter pour l'attribut forIdConjoint1 (idMembreFamille du Conjoint1)
     * 
     * @return la valeur courante de l'attribut forIdConjoint1
     */
    public String getForIdConjoint1() {
        return forIdConjoint1;
    }

    /**
     * getter pour l'attribut forIdConjoint2 (idMembreFamille du Conjoint2)
     * 
     * @return la valeur courante de l'attribut forIdConjoint2
     */
    public String getForIdConjoint2() {
        return forIdConjoint2;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    /**
     * setter pour l'attribut forIdConjoint1 (idMembreFamille du Conjoint1)
     * 
     * @param forIdConjoint1
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdConjoint1(String forIdConjoint1) {
        this.forIdConjoint1 = forIdConjoint1;
    }

    /**
     * setter pour l'attribut forIdConjoint2 (idMembreFamille du Conjoint2)
     * 
     * @param forIdConjoint2
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdConjoint2(String forIdConjoint2) {
        this.forIdConjoint2 = forIdConjoint2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return RelationConjoint.class;
    }

}
