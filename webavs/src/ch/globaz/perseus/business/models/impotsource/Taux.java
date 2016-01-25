package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeComplexModel;

public class Taux extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleBareme simpleBareme = null;
    private SimpleTaux simpleTaux = null;
    private TrancheSalaire trancheSalaire = null;

    /**
	 * 
	 */
    public Taux() {
        super();
        simpleTaux = new SimpleTaux();
        trancheSalaire = new TrancheSalaire();
        simpleBareme = new SimpleBareme();
    }

    @Override
    public String getId() {
        return simpleTaux.getId();
    }

    /**
     * @return the simpleBareme
     */
    public SimpleBareme getSimpleBareme() {
        return simpleBareme;
    }

    /**
     * @return the simpleTaux
     */
    public SimpleTaux getSimpleTaux() {
        return simpleTaux;
    }

    @Override
    public String getSpy() {
        return simpleTaux.getSpy();
    }

    /**
     * @return the trancheSalaire
     */
    public TrancheSalaire getTrancheSalaire() {
        return trancheSalaire;
    }

    @Override
    public void setId(String id) {
        simpleTaux.setId(id);

    }

    /**
     * @param SimpleBareme
     *            the simpleBareme to set
     */
    public void setSimpleBareme(SimpleBareme simpleBareme) {
        this.simpleBareme = simpleBareme;
    }

    /**
     * @param SimpleTaux
     *            the simpleTaux to set
     */
    public void setSimpleTaux(SimpleTaux simpleTaux) {
        this.simpleTaux = simpleTaux;
    }

    @Override
    public void setSpy(String spy) {
        simpleTaux.setSpy(spy);

    }

    /**
     * @param TrancheSalaire
     *            the trancheSalaire to set
     */
    public void setTrancheSalaire(TrancheSalaire trancheSalaire) {
        this.trancheSalaire = trancheSalaire;
    }

}
