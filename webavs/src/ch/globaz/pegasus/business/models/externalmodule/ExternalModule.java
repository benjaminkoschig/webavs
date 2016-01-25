package ch.globaz.pegasus.business.models.externalmodule;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Modèle complexe encapsulant le table de gestion des modules externes PC
 * 
 * @author sce
 * 
 */
public class ExternalModule<E> extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleExternalModule<E> simpleExternalModule = null;

    public ExternalModule() {
        super();
        simpleExternalModule = new SimpleExternalModule<E>();
    }

    @Override
    public String getId() {
        return simpleExternalModule.getId();
    }

    @Override
    public String getSpy() {
        return simpleExternalModule.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleExternalModule.setId(id);

    }

    @Override
    public void setSpy(String spy) {
        simpleExternalModule.setSpy(spy);

    }

    public SimpleExternalModule<E> getSimpleExternalModule() {
        return simpleExternalModule;
    }

    public void setSimpleExternalModule(SimpleExternalModule<E> simpleExternalModule) {
        this.simpleExternalModule = simpleExternalModule;
    }

}
