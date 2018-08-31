package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class LigneDecompteSearchSimpleModel extends JadeSearchSimpleModel {
	private static final long serialVersionUID = 8613372217797915171L;
	
	private String forId;
    private String forSequence;
    private String forIdDecompte;
    private String fromSequence;
    private String toSequence;
    private Boolean forToTreat;
    private String forCorrelationId;

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

    /**
	 * @return the forToTreat
	 */
	public Boolean getForToTreat() {
		return forToTreat;
	}

	/**
	 * @param forToTreat the forToTreat to set
	 */
	public void setForToTreat(Boolean forToTreat) {
		this.forToTreat = forToTreat;
	}

	/**
	 * @return the forCorrelationId
	 */
	public String getForCorrelationId() {
		return forCorrelationId;
	}

	/**
	 * @param forCorrelationId the forCorrelationId to set
	 */
	public void setForCorrelationId(String forCorrelationId) {
		this.forCorrelationId = forCorrelationId;
	}

	@Override
    public Class<LigneDecompteSimpleModel> whichModelClass() {
        return LigneDecompteSimpleModel.class;
    }

}
