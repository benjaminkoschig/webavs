package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeComplexModel;

public class ComplexAnnonceSedexCODebiteursAssures extends JadeComplexModel {
    private SimpleAnnonceSedexCO simpleAnnonceSedexCO = null;
    private SimpleAnnonceSedexCODebiteur simpleAnnonceSedexCODebiteur = null;
    private SimpleAnnonceSedexCOAssure simpleAnnonceSedexCOAssure = null;

    public ComplexAnnonceSedexCODebiteursAssures() {
        super();
        simpleAnnonceSedexCO = new SimpleAnnonceSedexCO();
        simpleAnnonceSedexCODebiteur = new SimpleAnnonceSedexCODebiteur();
        simpleAnnonceSedexCOAssure = new SimpleAnnonceSedexCOAssure();
    }

    public SimpleAnnonceSedexCO getSimpleAnnonceSedexCO() {
        return simpleAnnonceSedexCO;
    }

    public void setSimpleAnnonceSedexCO(SimpleAnnonceSedexCO simpleAnnonceSedexCO) {
        this.simpleAnnonceSedexCO = simpleAnnonceSedexCO;
    }

    public SimpleAnnonceSedexCODebiteur getSimpleAnnonceSedexCODebiteur() {
        return simpleAnnonceSedexCODebiteur;
    }

    public void setSimpleAnnonceSedexCODebiteur(SimpleAnnonceSedexCODebiteur simpleAnnonceSedexCODebiteur) {
        this.simpleAnnonceSedexCODebiteur = simpleAnnonceSedexCODebiteur;
    }

    public SimpleAnnonceSedexCOAssure getSimpleAnnonceSedexCOAssure() {
        return simpleAnnonceSedexCOAssure;
    }

    public void setSimpleAnnonceSedexCOAssure(SimpleAnnonceSedexCOAssure simpleAnnonceSedexCOAssure) {
        this.simpleAnnonceSedexCOAssure = simpleAnnonceSedexCOAssure;
    }

    @Override
    public String getId() {
        return simpleAnnonceSedexCO.getIdAnnonceSedexCO();
    }

    @Override
    public String getSpy() {
        return simpleAnnonceSedexCO.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleAnnonceSedexCO.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        simpleAnnonceSedexCO.setSpy(spy);
    }

}
