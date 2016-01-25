package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class LigneDecompteSearchSimpleModel extends JadeSearchSimpleModel {
    private String forId;
    private String forSequence;
    private String forIdDecompte;
    private String fromSequence;
    private String toSequence;

    public String getForId() {
        return forId;
    }

    /**
     * @return the forIdDecompte
     */
    public String getForIdDecompte() {
        return forIdDecompte;
    }

    /**
     * @return the forSequence
     */
    public String getForSequence() {
        return forSequence;
    }

    /**
     * @return the fromSequence
     */
    public String getFromSequence() {
        return fromSequence;
    }

    /**
     * @return the toSequence
     */
    public String getToSequence() {
        return toSequence;
    }

    public void setForId(final String forId) {
        this.forId = forId;
    }

    /**
     * @param forIdDecompte
     *            the forIdDecompte to set
     */
    public void setForIdDecompte(final String forIdDecompte) {
        this.forIdDecompte = forIdDecompte;
    }

    /**
     * @param forSequence
     *            the forSequence to set
     */
    public void setForSequence(final String forSequence) {
        this.forSequence = forSequence;
    }

    /**
     * @param fromSequence the fromSequence to set
     */
    public void setFromSequence(final String fromSequence) {
        this.fromSequence = fromSequence;
    }

    /**
     * @param toSequence the toSequence to set
     */
    public void setToSequence(final String toSequence) {
        this.toSequence = toSequence;
    }

    @Override
    public Class<LigneDecompteSimpleModel> whichModelClass() {
        return LigneDecompteSimpleModel.class;
    }

}
