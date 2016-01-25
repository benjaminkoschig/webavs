/**
 * 
 */
package ch.globaz.amal.business.models.controleurEnvoi;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.amal.business.models.annonce.SimpleAnnonce;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.famille.SimpleFamille;

/**
 * @author DHI
 * 
 */
public class ComplexControleurAnnonceDetail extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    SimpleAnnonce simpleAnnonce = null;
    SimpleControleurJob simpleControleurJob = null;
    SimpleDetailFamille simpleDetailFamille = null;
    SimpleControleurEnvoiStatus simpleEnvoiStatus = null;

    SimpleFamille simpleFamille = null;

    /**
	 * 
	 */
    public ComplexControleurAnnonceDetail() {
        super();
        simpleAnnonce = new SimpleAnnonce();
        simpleControleurJob = new SimpleControleurJob();
        simpleEnvoiStatus = new SimpleControleurEnvoiStatus();
        simpleDetailFamille = new SimpleDetailFamille();
        simpleFamille = new SimpleFamille();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleControleurJob.getId();
    }

    /**
     * @return the simpleAnnonce
     */
    public SimpleAnnonce getSimpleAnnonce() {
        return simpleAnnonce;
    }

    /**
     * @return the simpleControleurJob
     */
    public SimpleControleurJob getSimpleControleurJob() {
        return simpleControleurJob;
    }

    /**
     * @return the simpleDetailFamille
     */
    public SimpleDetailFamille getSimpleDetailFamille() {
        return simpleDetailFamille;
    }

    /**
     * @return the simpleEnvoiStatus
     */
    public SimpleControleurEnvoiStatus getSimpleEnvoiStatus() {
        return simpleEnvoiStatus;
    }

    /**
     * @return the simpleFamille
     */
    public SimpleFamille getSimpleFamille() {
        return simpleFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleControleurJob.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleControleurJob.setId(id);
    }

    /**
     * @param simpleAnnonce
     *            the simpleAnnonce to set
     */
    public void setSimpleAnnonce(SimpleAnnonce simpleAnnonce) {
        this.simpleAnnonce = simpleAnnonce;
    }

    /**
     * @param simpleControleurJob
     *            the simpleControleurJob to set
     */
    public void setSimpleControleurJob(SimpleControleurJob simpleControleurJob) {
        this.simpleControleurJob = simpleControleurJob;
    }

    /**
     * @param simpleDetailFamille
     *            the simpleDetailFamille to set
     */
    public void setSimpleDetailFamille(SimpleDetailFamille simpleDetailFamille) {
        this.simpleDetailFamille = simpleDetailFamille;
    }

    /**
     * @param simpleEnvoiStatus
     *            the simpleEnvoiStatus to set
     */
    public void setSimpleEnvoiStatus(SimpleControleurEnvoiStatus simpleEnvoiStatus) {
        this.simpleEnvoiStatus = simpleEnvoiStatus;
    }

    /**
     * @param simpleFamille
     *            the simpleFamille to set
     */
    public void setSimpleFamille(SimpleFamille simpleFamille) {
        this.simpleFamille = simpleFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleControleurJob.setSpy(spy);
    }

}
