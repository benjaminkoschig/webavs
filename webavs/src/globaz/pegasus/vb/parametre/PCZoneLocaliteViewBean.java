package globaz.pegasus.vb.parametre;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCParametreHandler;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits;
import ch.globaz.pegasus.business.models.parametre.ZoneLocalite;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author DMA
 * @date 12 nov. 2010
 */
public class PCZoneLocaliteViewBean extends BJadePersistentObjectViewBean {
    ZoneLocalite zoneLocalite = null;

    public PCZoneLocaliteViewBean() {
        super();
        zoneLocalite = new ZoneLocalite();
    }

    public PCZoneLocaliteViewBean(ZoneLocalite zoneLocalite) {
        super();
        this.zoneLocalite = zoneLocalite;
    }

    @Override
    public void add() throws Exception {
        PegasusServiceLocator.getParametreServicesLocator().getZoneLocaliteService().create(zoneLocalite);
    }

    @Override
    public void delete() throws Exception {
        PegasusServiceLocator.getParametreServicesLocator().getZoneLocaliteService().delete(zoneLocalite);

    }

    public String getDescZone(BSession objSession) {
        SimpleZoneForfaits zf = getZoneLocalite().getSimpleZoneForfaits();
        return this.getDescZone(zf, objSession);
    }

    public String getDescZone(SimpleZoneForfaits simpleZoneForfaits, BSession objSession) {
        return PCParametreHandler.getDescriptionZone(simpleZoneForfaits, objSession);
    }

    @Override
    public String getId() {
        return zoneLocalite.getId();
    }

    public JadeAbstractModel[] getListZoneFofaits() throws ForfaitsPrimesAssuranceMaladieException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return PCParametreHandler.getListZoneFofaits();
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (zoneLocalite != null) && !zoneLocalite.isNew() ? new BSpy(zoneLocalite.getSpy()) : new BSpy(
                getSession());
    }

    /**
     * @return the zoneLocalite
     */
    public ZoneLocalite getZoneLocalite() {
        return zoneLocalite;
    }

    @Override
    public void retrieve() throws Exception {
        zoneLocalite = PegasusServiceLocator.getParametreServicesLocator().getZoneLocaliteService()
                .read(zoneLocalite.getId());

    }

    @Override
    public void setId(String newId) {
        zoneLocalite.setId(newId);

    }

    /**
     * @param simpleZoneForfaits
     *            the simpleZoneForfaits to set
     */
    public void setSimpleZoneForfaits(ZoneLocalite zoneLocalite) {
        this.zoneLocalite = zoneLocalite;
    }

    /**
     * @param zoneLocalite
     *            the zoneLocalite to set
     */
    public void setZoneLocalite(ZoneLocalite zoneLocalite) {
        this.zoneLocalite = zoneLocalite;
    }

    @Override
    public void update() throws Exception {
        PegasusServiceLocator.getParametreServicesLocator().getZoneLocaliteService().update(zoneLocalite);

    }

}
