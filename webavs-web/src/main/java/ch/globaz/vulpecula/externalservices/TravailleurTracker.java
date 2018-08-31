package ch.globaz.vulpecula.externalservices;

import globaz.globall.db.BIJadeSimpleModelExternalService;
import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.mercato.mutations.myprodis.InfoType;
import ch.globaz.mercato.notification.Notification;

public class TravailleurTracker implements BIJadeSimpleModelExternalService {

    private RequestFactory requestFactory = new RequestFactory();

    @Override
    public void afterAdd(JadeSimpleModel model) throws Throwable {
        Notification notification = new Notification(InfoType.NOUVEAU_TRAVAILLEUR, model.getId());
        requestFactory.persistFromNouvellePersistance(notification);
    }

    @Override
    public void afterDelete(JadeSimpleModel model) throws Throwable {
    }

    @Override
    public void afterUpdate(JadeSimpleModel model) throws Throwable {
    }

    @Override
    public void beforeAdd(JadeSimpleModel model) throws Throwable {
    }

    @Override
    public void beforeDelete(JadeSimpleModel model) throws Throwable {
    }

    @Override
    public void beforeUpdate(JadeSimpleModel model) throws Throwable {

    }

}
