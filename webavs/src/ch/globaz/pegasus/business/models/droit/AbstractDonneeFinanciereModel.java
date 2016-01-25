package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeComplexModel;

public abstract class AbstractDonneeFinanciereModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;

    public AbstractDonneeFinanciereModel() {
        super();
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
    }

    @Override
    public abstract String getId();

    /**
     * @return the simpleDonneeFinanciereHeader
     */
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    @Override
    public abstract void setId(String id);

    /**
     * configure l'entité pret envers tiers, si existante, comme une nouvelle entité
     */
    public void setIsNew() {
        simpleDonneeFinanciereHeader.setId(null);
        simpleDonneeFinanciereHeader.setSpy(null);
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        this.simpleDonneeFinanciereHeader = simpleDonneeFinanciereHeader;
    }

}