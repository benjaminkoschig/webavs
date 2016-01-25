package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeComplexModel;

public class NombrePersonne extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleNombrePersonne simpleNombrePersonne = null;

    public NombrePersonne() {
        super();
        simpleNombrePersonne = new SimpleNombrePersonne();
    }

    @Override
    public String getId() {
        return simpleNombrePersonne.getId();
    }

    public SimpleNombrePersonne getSimpleNombrePersonne() {
        return simpleNombrePersonne;
    }

    @Override
    public String getSpy() {
        return simpleNombrePersonne.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleNombrePersonne.setId(id);

    }

    public void setSimpleNombrePersonne(SimpleNombrePersonne simpleNombrePersonne) {
        this.simpleNombrePersonne = simpleNombrePersonne;
    }

    @Override
    public void setSpy(String spy) {
        simpleNombrePersonne.setSpy(spy);

    }

}
