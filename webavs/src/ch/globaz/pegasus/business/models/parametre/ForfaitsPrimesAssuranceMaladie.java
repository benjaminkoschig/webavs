package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author DMA
 * @date 12 nov. 2010
 */
public class ForfaitsPrimesAssuranceMaladie extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    SimpleForfaitPrimesAssuranceMaladie simpleForfaitPrimesAssuranceMaladie = null;
    SimpleZoneForfaits simpleZoneForfaits = null;

    public ForfaitsPrimesAssuranceMaladie() {
        super();
        simpleForfaitPrimesAssuranceMaladie = new SimpleForfaitPrimesAssuranceMaladie();
        simpleZoneForfaits = new SimpleZoneForfaits();
    }

    @Override
    public String getId() {
        return simpleForfaitPrimesAssuranceMaladie.getId();
    }

    /**
     * @return the simpleForfaitPrimesAssuranceMaladie
     */
    public SimpleForfaitPrimesAssuranceMaladie getSimpleForfaitPrimesAssuranceMaladie() {
        return simpleForfaitPrimesAssuranceMaladie;
    }

    /**
     * @return the simpleZoneForfaits
     */
    public SimpleZoneForfaits getSimpleZoneForfaits() {
        return simpleZoneForfaits;
    }

    @Override
    public String getSpy() {
        return simpleForfaitPrimesAssuranceMaladie.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleForfaitPrimesAssuranceMaladie.setId(id);

    }

    /**
     * @param simpleForfaitPrimesAssuranceMaladie
     *            the simpleForfaitPrimesAssuranceMaladie to set
     */
    public void setSimpleForfaitPrimesAssuranceMaladie(
            SimpleForfaitPrimesAssuranceMaladie simpleForfaitPrimesAssuranceMaladie) {
        this.simpleForfaitPrimesAssuranceMaladie = simpleForfaitPrimesAssuranceMaladie;
    }

    /**
     * @param simpleZoneForfaits
     *            the simpleZoneForfaits to set
     */
    public void setSimpleZoneForfaits(SimpleZoneForfaits simpleZoneForfaits) {
        this.simpleZoneForfaits = simpleZoneForfaits;
    }

    @Override
    public void setSpy(String spy) {
        simpleForfaitPrimesAssuranceMaladie.setSpy(spy);
    }
}
