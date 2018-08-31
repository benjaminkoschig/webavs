package ch.globaz.vulpecula.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;

public class PosteTravailTracker extends BAbstractEntityExternalService {
    private RequestFactory requestFactory = new RequestFactory();

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        // La création d'un nouveau tiers ne nous intéresse pas.
        // String idTiers = entity.getId();

        // Notification pour eBusiness
        // requestFactory.persistFromAnciennePersistanceEbu(idTiers, entity.getSession());
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        // String idPosteTravail = entity.getId();
        // Notification pour eBusiness
        // TODO Créer méthode persistPT
        // requestFactory.persistPosteTravailFromAnciennePersistanceEbu(idPosteTravail, entity.getSession());
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
