package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;

public class PersonneDansPlanCalcul extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DroitMembreFamille droitMembreFamille = null;
    private String idTiers = "";

    private SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul = null;

    public PersonneDansPlanCalcul(String idTiers) {
        super();
        droitMembreFamille = new DroitMembreFamille();
        simplePersonneDansPlanCalcul = new SimplePersonneDansPlanCalcul();
        this.idTiers = idTiers;
    }

    /**
     * @return the droitMembreFamille
     */
    public DroitMembreFamille getDroitMembreFamille() {
        return droitMembreFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simplePersonneDansPlanCalcul.getIdPersonneDansPlanCalcul();
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return the simpleEnfantDansCalcul
     */
    public SimplePersonneDansPlanCalcul getSimplePersonneDansPlanCalcul() {
        return simplePersonneDansPlanCalcul;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simplePersonneDansPlanCalcul.getSpy();
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
        simplePersonneDansPlanCalcul.setIdPersonneDansPlanCalcul(id);

    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @param simplePersonneDansPlanCalcul
     *            the simpleEnfantDansCalcul to set
     */
    public void setSimplePersonneDansPlanCalcul(SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul) {
        this.simplePersonneDansPlanCalcul = simplePersonneDansPlanCalcul;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simplePersonneDansPlanCalcul.setSpy(spy);
    }

}
