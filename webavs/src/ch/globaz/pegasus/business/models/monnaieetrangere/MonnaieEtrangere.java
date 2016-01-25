package ch.globaz.pegasus.business.models.monnaieetrangere;

import globaz.jade.persistence.model.JadeComplexModel;

public class MonnaieEtrangere extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleMonnaieEtrangere simpleMonnaieEtrangere = null;

    /**
     * Constructeur
     */
    public MonnaieEtrangere() {
        super();
        simpleMonnaieEtrangere = new SimpleMonnaieEtrangere();
    }

    /**
     * Retourne l'id
     */
    @Override
    public String getId() {
        return simpleMonnaieEtrangere.getId();
    }

    /**
     * Retourne le Modele simple
     * 
     */
    public SimpleMonnaieEtrangere getSimpleMonnaieEtrangere() {
        return simpleMonnaieEtrangere;
    }

    /**
     * Retourne le spy
     */
    @Override
    public String getSpy() {

        return simpleMonnaieEtrangere.getSpy();
    }

    /**
     * Set l'id
     */
    @Override
    public void setId(String id) {
        simpleMonnaieEtrangere.setId(id);

    }

    /**
     * Set le modele simple
     */
    public void setSimpleMonnaieEtrangere(SimpleMonnaieEtrangere simpleMonnaieEtrangere) {
        this.simpleMonnaieEtrangere = simpleMonnaieEtrangere;
    }

    @Override
    public void setSpy(String spy) {
        simpleMonnaieEtrangere.setSpy(spy);
    }
}
