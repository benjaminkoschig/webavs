package ch.globaz.musca.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * Mod�le de donn�es simple d'un module facturation
 * 
 * Sert uniquement � afficher les donn�es via le mod�le complexe <code>PassageModuleComplexModel</code>
 * 
 * 
 */
public class ModuleModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idModuleFacturation = null;

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }

    private String idTypeModule = null;

    @Override
    public String getId() {
        return idModuleFacturation;
    }

    @Override
    public void setId(String id) {
        idModuleFacturation = id;

    }

    public String getIdTypeModule() {
        return idTypeModule;
    }

    public void setIdTypeModule(String idTypeModule) {
        this.idTypeModule = idTypeModule;
    }

}
