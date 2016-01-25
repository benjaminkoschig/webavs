package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author DMA
 * @date 16 nov. 2010
 */
public class ForfaitPrimeAssuranceMaladieLocalite extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    SimpleForfaitPrimesAssuranceMaladie simpleForfaitPrimesAssuranceMaladie = null;
    SimpleLienZoneLocalite simpleLienZoneLocalite = null;
    SimpleZoneForfaits simpleZoneForfaits = null;

    public ForfaitPrimeAssuranceMaladieLocalite() {
        super();
        simpleForfaitPrimesAssuranceMaladie = new SimpleForfaitPrimesAssuranceMaladie();
        simpleLienZoneLocalite = new SimpleLienZoneLocalite();
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
     * @return the simpleLienZoneLocalite
     */
    public SimpleLienZoneLocalite getSimpleLienZoneLocalite() {
        return simpleLienZoneLocalite;
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
     * @param simpleLienZoneLocalite
     *            the simpleLienZoneLocalite to set
     */
    public void setSimpleLienZoneLocalite(SimpleLienZoneLocalite simpleLienZoneLocalite) {
        this.simpleLienZoneLocalite = simpleLienZoneLocalite;
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
