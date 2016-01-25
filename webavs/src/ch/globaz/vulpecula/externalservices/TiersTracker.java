package ch.globaz.vulpecula.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import ch.globaz.mercato.mutations.myprodis.InfoType;
import ch.globaz.mercato.notification.Notification;

public class TiersTracker extends BAbstractEntityExternalService {

    private RequestFactory requestFactory = new RequestFactory();

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        // La création d'un nouveau tiers ne nous intéresse pas.
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        Notification notification = new Notification(InfoType.MODIFICATION_TIERS, entity.getId());
        // RequestFactory.executeAsync(notification);
        requestFactory.persistFromAnciennePersistance(notification, entity.getSession());
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
    }

    @Override
    public void init(BEntity entity) throws Throwable {
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
    }

}
