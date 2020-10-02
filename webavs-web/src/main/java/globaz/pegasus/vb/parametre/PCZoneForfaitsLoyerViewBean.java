package globaz.pegasus.vb.parametre;

import ch.globaz.pegasus.business.constantes.EPCForfaitType;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;

/**
 * @author DMA
 * @date 12 nov. 2010
 */
public class PCZoneForfaitsLoyerViewBean extends BJadePersistentObjectViewBean {
    final EPCForfaitType type = EPCForfaitType.LOYER;
    SimpleZoneForfaits simpleZoneForfaits = null;

    public PCZoneForfaitsLoyerViewBean() {
        super();
        simpleZoneForfaits = new SimpleZoneForfaits();
        simpleZoneForfaits.setType(type.getCode().toString());
    }

    public PCZoneForfaitsLoyerViewBean(SimpleZoneForfaits simpleZoneForfaits) {
        super();
        this.simpleZoneForfaits = simpleZoneForfaits;
    }

    @Override
    public void add() throws Exception {
        simpleZoneForfaits.setType(type.getCode().toString());
        PegasusServiceLocator.getParametreServicesLocator().getSimpleZoneForfaitsService().create(simpleZoneForfaits);
    }

    @Override
    public void delete() throws Exception {
        PegasusServiceLocator.getParametreServicesLocator().getSimpleZoneForfaitsService().delete(simpleZoneForfaits);

    }

    @Override
    public String getId() {
        return simpleZoneForfaits.getIdZoneForfait();
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
     * @return the simpleZoneForfaits
     */
    public SimpleZoneForfaits getSimpleZoneForfaits() {
        return simpleZoneForfaits;
    }

    @Override
    public BSpy getSpy() {
        return (simpleZoneForfaits != null) && !simpleZoneForfaits.isNew() ? new BSpy(simpleZoneForfaits.getSpy())
                : new BSpy(getSession());
    }

    @Override
    public void retrieve() throws Exception {
        simpleZoneForfaits = PegasusServiceLocator.getParametreServicesLocator().getSimpleZoneForfaitsService()
                .read(simpleZoneForfaits.getId());

    }

    @Override
    public void setId(String newId) {
        simpleZoneForfaits.setId(newId);

    }

    /**
     * @param simpleZoneForfaits
     *            the simpleZoneForfaits to set
     */
    public void setSimpleZoneForfaits(SimpleZoneForfaits simpleZoneForfaits) {
        this.simpleZoneForfaits = simpleZoneForfaits;
    }

    @Override
    public void update() throws Exception {
        PegasusServiceLocator.getParametreServicesLocator().getSimpleZoneForfaitsService().update(simpleZoneForfaits);

    }

}
