package ch.globaz.pegasus.business.models.fortuneusuelle;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class BienImmobilierHabitationNonPrincipale extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /* private TiersSimpleModel tiersCommune = null; */
    private LocaliteSimpleModel localite = null;
    private SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale = null;
    private PaysSimpleModel simplePays = null;
    private TiersSimpleModel tiersCompagnie = null;

    /**
	 * 
	 */
    public BienImmobilierHabitationNonPrincipale() {
        super();
        simpleBienImmobilierHabitationNonPrincipale = new SimpleBienImmobilierHabitationNonPrincipale();
        // this.tiersCommune = new TiersSimpleModel();
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
        return simpleBienImmobilierHabitationNonPrincipale.getId();
    }

    /**
     * @return the localite
     */
    public LocaliteSimpleModel getLocalite() {
        return localite;
    }

    /**
     * @return the simpleBienImmobilierHabitationNonPrincipale
     */
    public SimpleBienImmobilierHabitationNonPrincipale getSimpleBienImmobilierHabitationNonPrincipale() {
        return simpleBienImmobilierHabitationNonPrincipale;
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
        return simpleBienImmobilierHabitationNonPrincipale.getSpy();
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
        simpleBienImmobilierHabitationNonPrincipale.setId(id);
    }

    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleBienImmobilierHabitationNonPrincipale.setId(null);
        simpleBienImmobilierHabitationNonPrincipale.setSpy(null);
    }

    /**
     * @param localite
     *            the localite to set
     */
    public void setLocalite(LocaliteSimpleModel localite) {
        this.localite = localite;
    }

    /**
     * @param simpleBienImmobilierHabitationNonPrincipale
     *            the simpleBienImmobilierHabitationNonPrincipale to set
     */
    public void setSimpleBienImmobilierHabitationNonPrincipale(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale) {
        this.simpleBienImmobilierHabitationNonPrincipale = simpleBienImmobilierHabitationNonPrincipale;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleBienImmobilierHabitationNonPrincipale.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader
                .getId());
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
        simpleBienImmobilierHabitationNonPrincipale.setSpy(spy);
    }

    public void setTiersCompagnie(TiersSimpleModel tiersCompagnie) {
        this.tiersCompagnie = tiersCompagnie;
    }
}