package ch.globaz.musca.business.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * 
 * Modèle de recherche sur <code>PassageModuleComplexModel</code>
 * 
 */
public class PassageModuleComplexSearchModel extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Critère de recherche type de facturation
     */
    private String forTypeFacturation = null;
    /**
     * Critère de recherche type de module inclus dans passage
     */
    private Collection<String> inPassageTypeModule = null;

    private String forStatus = null;

    private String forNotTypeFacturation = null;

    @Override
    public Class whichModelClass() {
        return PassageModuleComplexModel.class;
    }

    public String getForTypeFacturation() {
        return forTypeFacturation;
    }

    public void setForTypeFacturation(String forTypeFacturation) {
        this.forTypeFacturation = forTypeFacturation;
    }

    public Collection<String> getInPassageTypeModule() {
        return inPassageTypeModule;
    }

    public void setInPassageTypeModule(Collection<String> inPassageTypeModule) {
        this.inPassageTypeModule = inPassageTypeModule;
    }

    public String getForStatus() {
        return forStatus;
    }

    public void setForStatus(String forStatus) {
        this.forStatus = forStatus;
    }

    public String getForNotTypeFacturation() {
        return forNotTypeFacturation;
    }

    public void setForNotTypeFacturation(String forNotTypeFacturation) {
        this.forNotTypeFacturation = forNotTypeFacturation;
    }

}
