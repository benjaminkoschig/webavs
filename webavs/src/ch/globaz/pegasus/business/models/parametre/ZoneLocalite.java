package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;

public class ZoneLocalite extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private LocaliteSimpleModel localiteSimpleModel = null;
    private SimpleLienZoneLocalite simpleLienZoneLocalite = null;
    private SimpleZoneForfaits simpleZoneForfaits = null;

    public ZoneLocalite() {
        super();
        simpleLienZoneLocalite = new SimpleLienZoneLocalite();
        simpleZoneForfaits = new SimpleZoneForfaits();
        localiteSimpleModel = new LocaliteSimpleModel();
    }

    @Override
    public String getId() {
        return simpleLienZoneLocalite.getId();

    }

    /**
     * @return the localiteSimpleModel
     */
    public LocaliteSimpleModel getLocaliteSimpleModel() {
        return localiteSimpleModel;
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
        return simpleLienZoneLocalite.getCreationSpy();
    }

    @Override
    public void setId(String id) {
        simpleLienZoneLocalite.setIdLienZoneLocalite(id);
    }

    /**
     * @param localiteSimpleModel
     *            the localiteSimpleModel to set
     */
    public void setLocaliteSimpleModel(LocaliteSimpleModel localiteSimpleModel) {
        this.localiteSimpleModel = localiteSimpleModel;
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
        simpleLienZoneLocalite.setSpy(spy);
    }

}
