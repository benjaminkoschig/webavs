package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class SimpleLibelleContratEntretienViagerSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdContratEntretienViager = null;
    private String forIdLibelleContratEntretienViager = null;
    private List<String> inIdContratEntretienViager = null;

    public String getForIdContratEntretienViager() {
        return forIdContratEntretienViager;
    }

    public String getForIdLibelleContratEntretienViager() {
        return forIdLibelleContratEntretienViager;
    }

    public void setForIdContratEntretienViager(String forIdContratEntretienViager) {
        this.forIdContratEntretienViager = forIdContratEntretienViager;
    }

    public void setForIdLibelleContratEntretienViager(String forIdLibelleContratEntretienViager) {
        this.forIdLibelleContratEntretienViager = forIdLibelleContratEntretienViager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<SimpleLibelleContratEntretienViager> whichModelClass() {
        return SimpleLibelleContratEntretienViager.class;
    }

    public List<String> getInIdContratEntretienViager() {
        return inIdContratEntretienViager;
    }

    public void setInIdContratEntretienViager(List<String> inIdContratEntretienViager) {
        this.inIdContratEntretienViager = inIdContratEntretienViager;
    }

}
