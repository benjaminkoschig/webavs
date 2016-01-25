package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeComplexModel;

public class PeriodeImpotSource extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimplePeriodeImpotSource simplePeriodeImpotSource = null;

    public PeriodeImpotSource() {
        super();
        setSimplePeriodeImpotSource(new SimplePeriodeImpotSource());
    }

    public PeriodeImpotSource create(PeriodeImpotSource periode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getId() {
        return getSimplePeriodeImpotSource().getId();
    }

    public SimplePeriodeImpotSource getSimplePeriodeImpotSource() {
        return simplePeriodeImpotSource;
    }

    @Override
    public String getSpy() {
        return getSimplePeriodeImpotSource().getSpy();
    }

    @Override
    public void setId(String id) {
        getSimplePeriodeImpotSource().setId(id);

    }

    public void setSimplePeriodeImpotSource(SimplePeriodeImpotSource simplePeriodeImpotSource) {
        this.simplePeriodeImpotSource = simplePeriodeImpotSource;
    }

    @Override
    public void setSpy(String spy) {
        getSimplePeriodeImpotSource().setSpy(spy);

    }

}
