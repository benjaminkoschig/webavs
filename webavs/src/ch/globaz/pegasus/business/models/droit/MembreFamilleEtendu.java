package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeComplexModel;

public class MembreFamilleEtendu extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DroitMembreFamille droitMembreFamille = null;
    private SimpleDonneesPersonnelles simpleDonneesPersonnelles = null;

    public MembreFamilleEtendu() {
        super();
        droitMembreFamille = new DroitMembreFamille();
        simpleDonneesPersonnelles = new SimpleDonneesPersonnelles();
    }

    /**
     * @return the droitMembreFamille
     */
    public DroitMembreFamille getDroitMembreFamille() {
        return droitMembreFamille;
    }

    @Override
    public String getId() {
        return droitMembreFamille.getId();
    }

    /**
     * @return the simpleDonneesPersonnelles
     */
    public SimpleDonneesPersonnelles getSimpleDonneesPersonnelles() {
        return simpleDonneesPersonnelles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return droitMembreFamille.getSpy();
    }

    /**
     * @param droitMembreFamille
     *            the droitMembreFamille to set
     */
    public void setDroitMembreFamille(DroitMembreFamille droitMembreFamille) {
        this.droitMembreFamille = droitMembreFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        droitMembreFamille.setId(id);
    }

    /**
     * @param simpleDonneesPersonnelles
     *            the simpleDonneesPersonnelles to set
     */
    public void setSimpleDonneesPersonnelles(SimpleDonneesPersonnelles simpleDonneesPersonnelles) {
        this.simpleDonneesPersonnelles = simpleDonneesPersonnelles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        droitMembreFamille.setSpy(spy);
    }

}
