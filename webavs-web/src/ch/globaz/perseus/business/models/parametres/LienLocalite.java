package ch.globaz.perseus.business.models.parametres;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;

/**
 * 
 * @author MBO
 * 
 */

public class LienLocalite extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private LocaliteSimpleModel localiteSimpleModel = null;
    private SimpleLienLocalite simpleLienLocalite = null;
    private SimpleZone simpleZone = null;

    public LienLocalite() {
        super();
        simpleLienLocalite = new SimpleLienLocalite();
        simpleZone = new SimpleZone();
        localiteSimpleModel = new LocaliteSimpleModel();
    }

    @Override
    public String getId() {
        return simpleLienLocalite.getId();
    }

    public LocaliteSimpleModel getLocaliteSimpleModel() {
        return localiteSimpleModel;
    }

    public SimpleLienLocalite getSimpleLienLocalite() {
        return simpleLienLocalite;
    }

    public SimpleZone getSimpleZone() {
        return simpleZone;
    }

    @Override
    public String getSpy() {
        return simpleLienLocalite.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleLienLocalite.setId(id);
    }

    public void setLocaliteSimpleModel(LocaliteSimpleModel localiteSimpleModel) {
        this.localiteSimpleModel = localiteSimpleModel;
    }

    public void setSimpleLienLocalite(SimpleLienLocalite simpleLienLocalite) {
        this.simpleLienLocalite = simpleLienLocalite;
    }

    public void setSimpleZone(SimpleZone simpleZone) {
        this.simpleZone = simpleZone;
    }

    @Override
    public void setSpy(String spy) {
        simpleLienLocalite.setSpy(spy);
    }

}
