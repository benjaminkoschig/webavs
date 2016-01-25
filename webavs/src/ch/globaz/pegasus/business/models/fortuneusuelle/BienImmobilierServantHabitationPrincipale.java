package ch.globaz.pegasus.business.models.fortuneusuelle;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class BienImmobilierServantHabitationPrincipale extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private LocaliteSimpleModel localite = null;
    private SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale = null;
    /* private TiersSimpleModel tiersCommune = null; */
    private TiersSimpleModel tiersCompagnie = null;

    /**
	 * 
	 */
    public BienImmobilierServantHabitationPrincipale() {
        super();
        simpleBienImmobilierServantHabitationPrincipale = new SimpleBienImmobilierServantHabitationPrincipale();
        // this.tiersCommune = new TiersSimpleModel();
        localite = new LocaliteSimpleModel();
        tiersCompagnie = new TiersSimpleModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleBienImmobilierServantHabitationPrincipale.getId();
    }

    /**
     * @return the localite
     */
    public LocaliteSimpleModel getLocalite() {
        return localite;
    }

    /**
     * @return the simpleBienImmobilierServantHabitationPrincipale
     */
    public SimpleBienImmobilierServantHabitationPrincipale getSimpleBienImmobilierServantHabitationPrincipale() {
        return simpleBienImmobilierServantHabitationPrincipale;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleBienImmobilierServantHabitationPrincipale.getSpy();
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
        simpleBienImmobilierServantHabitationPrincipale.setId(id);
    }

    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleBienImmobilierServantHabitationPrincipale.setId(null);
        simpleBienImmobilierServantHabitationPrincipale.setSpy(null);
    }

    /**
     * @param localite
     *            the localite to set
     */
    public void setLocalite(LocaliteSimpleModel localite) {
        this.localite = localite;
    }

    /**
     * @param simpleBienImmobilierServantHabitationPrincipale
     *            the simpleBienImmobilierServantHabitationPrincipale to set
     */
    public void setSimpleBienImmobilierServantHabitationPrincipale(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale) {
        this.simpleBienImmobilierServantHabitationPrincipale = simpleBienImmobilierServantHabitationPrincipale;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleBienImmobilierServantHabitationPrincipale.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader
                .getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleBienImmobilierServantHabitationPrincipale.setSpy(spy);
    }

    public void setTiersCompagnie(TiersSimpleModel tiersCompagnie) {
        this.tiersCompagnie = tiersCompagnie;
    }

}