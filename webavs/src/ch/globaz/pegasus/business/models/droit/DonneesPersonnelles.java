/**
 * 
 */
package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;

/**
 * @author BSC
 */
public class DonneesPersonnelles extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDroitMembreFamille droitMbrFam = null;
    private LocaliteSimpleModel localite = null;
    private SimpleDonneesPersonnelles simpleDonneesPersonnelles = null;

    /**
	 * 
	 */
    public DonneesPersonnelles() {
        super();
        simpleDonneesPersonnelles = new SimpleDonneesPersonnelles();
        localite = new LocaliteSimpleModel();
        droitMbrFam = new SimpleDroitMembreFamille();
    }

    /**
     * @return the droitMbrFam
     */
    public SimpleDroitMembreFamille getDroitMbrFam() {
        return droitMbrFam;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleDonneesPersonnelles.getId();
    }

    /**
     * @return the localite
     */
    public LocaliteSimpleModel getLocalite() {
        return localite;
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
        return simpleDonneesPersonnelles.getSpy();
    }

    /**
     * @param droitMbrFam
     *            the droitMbrFam to set
     */
    public void setDroitMbrFam(SimpleDroitMembreFamille droitMbrFam) {
        this.droitMbrFam = droitMbrFam;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleDonneesPersonnelles.setId(id);
    }

    /**
     * @param localite
     *            the localite to set
     */
    public void setLocalite(LocaliteSimpleModel localite) {
        this.localite = localite;
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
        simpleDonneesPersonnelles.setSpy(spy);
    }

}
