package ch.globaz.pegasus.business.models.creancier;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class Creancier extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleCreancier simpleCreancier = null;
    private TiersSimpleModel simpleTiers = null;

    //NOT IN DB
    private String csRole = "";
    private boolean isCreatedForOther = false;

    // private SimpleDemande simpleDemande = null;

    public Creancier() {
        super();
        simpleCreancier = new SimpleCreancier();
        simpleTiers = new TiersSimpleModel();
        // this.simpleDemande = new SimpleDemande();
    }

    @Override
    public String getId() {
        return simpleCreancier.getIdCreancier();
    }

    /**
     * @return the simpleCreancier
     */
    public SimpleCreancier getSimpleCreancier() {
        return simpleCreancier;
    }

    /**
     * @return the simpleTiers
     */
    public TiersSimpleModel getSimpleTiers() {
        return simpleTiers;
    }

    @Override
    public String getSpy() {
        return simpleCreancier.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleCreancier.setIdCreancier(id);
    }

    /**
     * @param simpleCreancier
     *            the simpleCreancier to set
     */
    public void setSimpleCreancier(SimpleCreancier simpleCreancier) {
        this.simpleCreancier = simpleCreancier;
    }

    /**
     * @param simpleTiers
     *            the simpleTiers to set
     */
    public void setSimpleTiers(TiersSimpleModel simpleTiers) {
        this.simpleTiers = simpleTiers;
    }

    @Override
    public void setSpy(String spy) {
        simpleCreancier.setSpy(spy);
    }

    public String getCsRole() {
        return csRole;
    }

    public void setCsRole(String csRole) {
        this.csRole = csRole;
    }

    public boolean isCreatedForOther() {
        return isCreatedForOther;
    }

    public void setCreatedForOther(boolean createdForOther) {
        isCreatedForOther = createdForOther;
    }
}
