/**
 * 
 */
package ch.globaz.perseus.business.models.creancier;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.perseus.business.models.demande.SimpleDemande;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * @author MBO
 * 
 */
public class Creancier extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleCreancier simpleCreancier = null;
    private SimpleDemande simpleDemande = null;
    private TiersSimpleModel simpleTiers = null;

    public Creancier() {
        super();
        simpleCreancier = new SimpleCreancier();
        simpleDemande = new SimpleDemande();
        simpleTiers = new TiersSimpleModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleCreancier.getId();
    }

    public SimpleCreancier getSimpleCreancier() {
        return simpleCreancier;
    }

    public SimpleDemande getSimpleDemande() {
        return simpleDemande;
    }

    public TiersSimpleModel getSimpleTiers() {
        return simpleTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleCreancier.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleCreancier.setId(id);

    }

    public void setSimpleCreancier(SimpleCreancier simpleCreancier) {
        this.simpleCreancier = simpleCreancier;
    }

    public void setSimpleDemande(SimpleDemande simpleDemande) {
        this.simpleDemande = simpleDemande;
    }

    public void setSimpleTiers(TiersSimpleModel simpleTiers) {
        this.simpleTiers = simpleTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleCreancier.setSpy(spy);

    }

}
