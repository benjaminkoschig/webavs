package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeComplexModel;

public class RevenuSourcierComplex extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleRevenu simpleRevenu = null;
    private SimpleRevenuSourcier simpleRevenuSourcier = null;

    public RevenuSourcierComplex() {
        simpleRevenu = new SimpleRevenu();
        simpleRevenuSourcier = new SimpleRevenuSourcier();
    }

    @Override
    public String getId() {
        return simpleRevenu.getId();
    }

    public SimpleRevenu getSimpleRevenu() {
        return simpleRevenu;
    }

    public SimpleRevenuSourcier getSimpleRevenuSourcier() {
        return simpleRevenuSourcier;
    }

    @Override
    public String getSpy() {
        return simpleRevenu.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleRevenu.setId(id);
    }

    public void setSimpleRevenu(SimpleRevenu simpleRevenu) {
        this.simpleRevenu = simpleRevenu;
    }

    public void setSimpleRevenuSourcier(SimpleRevenuSourcier simpleRevenuSourcier) {
        this.simpleRevenuSourcier = simpleRevenuSourcier;
    }

    @Override
    public void setSpy(String spy) {
        simpleRevenu.setSpy(spy);
    }

}
