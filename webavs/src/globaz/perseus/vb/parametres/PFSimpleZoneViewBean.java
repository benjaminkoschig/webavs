package globaz.perseus.vb.parametres;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.perseus.business.models.parametres.SimpleZone;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DMA
 * @date 12 nov. 2010
 */
public class PFSimpleZoneViewBean extends BJadePersistentObjectViewBean {

    SimpleZone simpleZone = null;

    public PFSimpleZoneViewBean() {
        super();
        simpleZone = new SimpleZone();
    }

    public PFSimpleZoneViewBean(SimpleZone simpleZone) {
        super();
        this.simpleZone = simpleZone;
    }

    @Override
    public void add() throws Exception {
        PerseusServiceLocator.getSimpleZoneService().create(simpleZone);
    }

    @Override
    public void delete() throws Exception {
        PerseusServiceLocator.getSimpleZoneService().delete(simpleZone);

    }

    @Override
    public String getId() {
        return simpleZone.getIdZone();
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * @return the simpleZone
     */
    public SimpleZone getSimpleZone() {
        return simpleZone;
    }

    @Override
    public BSpy getSpy() {
        return (simpleZone != null) && !simpleZone.isNew() ? new BSpy(simpleZone.getSpy()) : new BSpy(getSession());
    }

    @Override
    public void retrieve() throws Exception {
        // Lors de l'annulation d'une modification, le rechargement de la page SET l'id zone à NULL
        if (simpleZone.getId() != null) {
            simpleZone = PerseusServiceLocator.getSimpleZoneService().read(simpleZone.getId());
        }
    }

    @Override
    public void setId(String newId) {
        simpleZone.setId(newId);

    }

    /**
     * @param simpleZone
     *            the simpleZone to set
     */
    public void setSimpleZone(SimpleZone simpleZone) {
        this.simpleZone = simpleZone;
    }

    @Override
    public void update() throws Exception {
        PerseusServiceLocator.getSimpleZoneService().update(simpleZone);

    }

}
