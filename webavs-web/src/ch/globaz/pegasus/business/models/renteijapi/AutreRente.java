package ch.globaz.pegasus.business.models.renteijapi;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.PaysSimpleModel;

public class AutreRente extends AbstractDonneeFinanciereModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private SimpleAutreRente simpleAutreRente = null;

    private PaysSimpleModel simplePays = null;

    public AutreRente() {
        super();
        simpleAutreRente = new SimpleAutreRente();
        simplePays = new PaysSimpleModel();
    }

    @Override
    public String getId() {
        return simpleAutreRente.getId();
    }

    /**
     * @return the simpleAutreRente
     */
    public SimpleAutreRente getSimpleAutreRente() {
        return simpleAutreRente;
    }

    /**
     * @return the simplePays
     */
    public PaysSimpleModel getSimplePays() {
        return simplePays;
    }

    @Override
    public String getSpy() {
        return simpleAutreRente.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleAutreRente.setId(id);
    }

    /**
     * @param simpleAutreRente
     *            the simpleAutreRente to set
     */
    public void setSimpleAutreRente(SimpleAutreRente simpleAutreRente) {
        this.simpleAutreRente = simpleAutreRente;
    }

    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader donneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(donneeFinanciereHeader);
        simpleAutreRente.setIdDonneeFinanciereHeader(donneeFinanciereHeader.getId());
    }

    /**
     * @param simplePays
     *            the simplePays to set
     */
    public void setSimplePays(PaysSimpleModel simplePays) {
        this.simplePays = simplePays;
    }

    @Override
    public void setSpy(String spy) {
        simpleAutreRente.setSpy(spy);
    }

}
