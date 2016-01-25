package ch.globaz.vulpecula.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;

public class EnfantTracker extends BAbstractEntityExternalService {
    private RequestFactory requestFactory = new RequestFactory();

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        // Notification notification = new Notification(InfoType.NOUVEL_ENFANT, entity.getId());
        // RequestFactory.create(notification).execute();
        // requestFactory.persistFromAnciennePersistance(notification, entity.getSession());
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        // Notification notification = new Notification(InfoType.MODIFICATION_ENFANT, entity.getId());
        // RequestFactory.create(notification).execute();
        // requestFactory.persistFromAnciennePersistance(notification, entity.getSession());
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
