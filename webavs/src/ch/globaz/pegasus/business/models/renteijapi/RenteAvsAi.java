package ch.globaz.pegasus.business.models.renteijapi;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

/**
 * Modèle complexe des RentesAvAI, renteavsai et donnefinanciereheader 6.2010 6.2010
 * 
 * @author SCE
 * 
 */
public class RenteAvsAi extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Rente avs ai
    private SimpleRenteAvsAi simpleRenteAvsAi = null;

    /**
     * Constructeur
     */
    public RenteAvsAi() {
        super();
        simpleRenteAvsAi = new SimpleRenteAvsAi();
    }

    /**
     * Return the id from the root model
     */
    @Override
    public String getId() {
        return simpleRenteAvsAi.getId();
    }

    /**
     * @return the simpleRenteAvsAi
     */
    public SimpleRenteAvsAi getSimpleRenteAvsAi() {
        return simpleRenteAvsAi;
    }

    /**
     * Set the spy
     */
    @Override
    public String getSpy() {
        return simpleRenteAvsAi.getSpy();
    }

    /**
     * Set the id from the root model
     */
    @Override
    public void setId(String id) {
        simpleRenteAvsAi.setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel #setIsNew()
     */
    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleRenteAvsAi.setId(null);
        simpleRenteAvsAi.setSpy(null);
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleRenteAvsAi.setIdDonneeFinanciereHeader(simpleDonneeFinanciereHeader.getId());
    }

    /**
     * @param simpleRenteAvsAi
     *            the simpleRenteAvsAi to set
     */
    public void setSimpleRenteAvsAi(SimpleRenteAvsAi simpleRenteAvsAi) {
        this.simpleRenteAvsAi = simpleRenteAvsAi;
    }

    /**
     * Set the spy from the root model
     */
    @Override
    public void setSpy(String spy) {
        simpleDonneeFinanciereHeader.setSpy(spy);

    }

}
