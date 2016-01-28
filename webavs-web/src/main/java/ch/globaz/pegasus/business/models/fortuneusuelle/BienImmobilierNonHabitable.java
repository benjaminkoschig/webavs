package ch.globaz.pegasus.business.models.fortuneusuelle;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class BienImmobilierNonHabitable extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private LocaliteSimpleModel localite = null;
    private SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable = null;
    private PaysSimpleModel simplePays = null;
    private TiersSimpleModel tiersCompagnie = null;

    /**
	 * 
	 */
    public BienImmobilierNonHabitable() {
        super();
        simpleBienImmobilierNonHabitable = new SimpleBienImmobilierNonHabitable();
        localite = new LocaliteSimpleModel();
        tiersCompagnie = new TiersSimpleModel();
        simplePays = new PaysSimpleModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleBienImmobilierNonHabitable.getId();
    }

    /**
     * @return the localite
     */
    public LocaliteSimpleModel getLocalite() {
        return localite;
    }

    /**
     * @return the simpleBienImmobilierNonHabitable
     */
    public SimpleBienImmobilierNonHabitable getSimpleBienImmobilierNonHabitable() {
        return simpleBienImmobilierNonHabitable;
    }

    public PaysSimpleModel getSimplePays() {
        return simplePays;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleBienImmobilierNonHabitable.getSpy();
    }

    public TiersSimpleModel getTiersCompagnie() {
        return tiersCompagnie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleBienImmobilierNonHabitable.setId(id);
    }

    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleBienImmobilierNonHabitable.setId(null);
        simpleBienImmobilierNonHabitable.setSpy(null);
    }

    /**
     * @param localite
     *            the localite to set
     */
    public void setLocalite(LocaliteSimpleModel localite) {
        this.localite = localite;
    }

    /**
     * @param simpleBienImmobilierNonHabitable
     *            the simpleBienImmobilierNonHabitable to set
     */
    public void setSimpleBienImmobilierNonHabitable(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable) {
        this.simpleBienImmobilierNonHabitable = simpleBienImmobilierNonHabitable;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleBienImmobilierNonHabitable.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    public void setSimplePays(PaysSimpleModel simplePays) {
        this.simplePays = simplePays;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleBienImmobilierNonHabitable.setSpy(spy);
    }

    public void setTiersCompagnie(TiersSimpleModel tiersCompagnie) {
        this.tiersCompagnie = tiersCompagnie;
    }

}