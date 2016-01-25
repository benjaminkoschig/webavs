package ch.globaz.musca.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * Modèle de données simple d'un module facturation
 * 
 * Sert uniquement à afficher les données via le modèle complexe <code>PassageModuleComplexModel</code>
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
