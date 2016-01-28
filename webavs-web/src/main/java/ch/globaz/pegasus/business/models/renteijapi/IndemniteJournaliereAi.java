package ch.globaz.pegasus.business.models.renteijapi;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

/**
 * Modele complexe composé de indemnité journaliere ai et de donnees financieres header 6.2010
 * 
 * @author SCE
 * 
 */
public class IndemniteJournaliereAi extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Indemnite journaliere ai
    private SimpleIndemniteJournaliereAi simpleIndemniteJournaliereAi = null;

    public IndemniteJournaliereAi() {
        super();
        simpleIndemniteJournaliereAi = new SimpleIndemniteJournaliereAi();
    }

    /**
     * Return the id from the root model
     */
    @Override
    public String getId() {
        return simpleIndemniteJournaliereAi.getId();
    }

    /**
     * @return the simpleIndemniteJournaliereAi
     */
    public SimpleIndemniteJournaliereAi getSimpleIndemniteJournaliereAi() {
        return simpleIndemniteJournaliereAi;
    }

    /**
     * Return the spy from the root model
     */
    @Override
    public String getSpy() {
        return simpleIndemniteJournaliereAi.getSpy();
    }

    /**
     * Set the id of the root model
     */
    @Override
    public void setId(String id) {
        simpleIndemniteJournaliereAi.setId(id);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel #setIsNew()
     */
    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleIndemniteJournaliereAi.setId(null);
        simpleIndemniteJournaliereAi.setSpy(null);
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleIndemniteJournaliereAi.setIdDonneeFinanciereHeader(simpleDonneeFinanciereHeader.getId());
    }

    /**
     * @param simpleIndemniteJournaliereAi
     *            the simpleIndemniteJournaliereAi to set
     */
    public void setSimpleIndemniteJournaliereAi(SimpleIndemniteJournaliereAi simpleIndemniteJournaliereAi) {
        this.simpleIndemniteJournaliereAi = simpleIndemniteJournaliereAi;
    }

    /**
     * Set the spy of the root model
     */
    @Override
    public void setSpy(String spy) {
        simpleIndemniteJournaliereAi.setSpy(spy);

    }
}
