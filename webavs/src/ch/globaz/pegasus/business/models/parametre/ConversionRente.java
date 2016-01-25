package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeComplexModel;

public class ConversionRente extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleConversionRente simpleConversionRente = null;

    public ConversionRente() {
        super();
        simpleConversionRente = new SimpleConversionRente();
    }

    @Override
    public String getId() {
        return simpleConversionRente.getId();
    }

    /**
     * @return the simpleConversionRente
     */
    public SimpleConversionRente getSimpleConversionRente() {
        return simpleConversionRente;
    }

    @Override
    public String getSpy() {
        return simpleConversionRente.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleConversionRente.setId(id);

    }

    /**
     * @param simpleConversionRente the simpleConversionRente to set
     */
    public void setSimpleConversionRente(SimpleConversionRente simpleConversionRente) {
        this.simpleConversionRente = simpleConversionRente;
    }

    @Override
    public void setSpy(String spy) {
        simpleConversionRente.setSpy(spy);
    }

}
