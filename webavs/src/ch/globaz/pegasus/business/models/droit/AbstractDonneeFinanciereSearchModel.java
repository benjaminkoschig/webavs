package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public abstract class AbstractDonneeFinanciereSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDonneeFinanciereHeader = null;
    private List<String> inIdDonneeFinanciereHeader = null;

    public AbstractDonneeFinanciereSearchModel() {
        super();
    }

    public String getForIdDonneeFinanciereHeader() {
        return forIdDonneeFinanciereHeader;
    }

    public List<String> getInIdDonneeFinanciereHeader() {
        return inIdDonneeFinanciereHeader;
    }

    public void setForIdDonneeFinanciereHeader(String forIdDonneeFinanciereHeader) {
        this.forIdDonneeFinanciereHeader = forIdDonneeFinanciereHeader;
    }

    public void setInIdDonneeFinanciereHeader(List<String> inIdDonneeFinanciereHeader) {
        this.inIdDonneeFinanciereHeader = inIdDonneeFinanciereHeader;
    }

}