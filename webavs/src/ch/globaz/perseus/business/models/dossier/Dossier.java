package ch.globaz.perseus.business.models.dossier;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.prestation.business.models.demande.DemandePrestation;

/**
 * TODO Javadoc
 * 
 * @author vyj
 */
public class Dossier extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DemandePrestation demandePrestation = null;
    private SimpleDossier dossier = null;

    /**
	 * 
	 */
    public Dossier() {
        super();
        dossier = new SimpleDossier();
        demandePrestation = new DemandePrestation();
    }

    /**
     * @return
     */
    public DemandePrestation getDemandePrestation() {
        return demandePrestation;
    }

    /**
     * @return
     */
    public SimpleDossier getDossier() {
        return dossier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return dossier.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return dossier.getSpy();
    }

    /**
     * @param demandePrestation
     */
    public void setDemandePrestation(DemandePrestation demandePrestation) {
        this.demandePrestation = demandePrestation;
    }

    /**
     * @param dossier
     */
    public void setDossier(SimpleDossier dossier) {
        this.dossier = dossier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        dossier.setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        dossier.setSpy(spy);
    }

}
