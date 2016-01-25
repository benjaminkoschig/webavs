package globaz.naos.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;

/**

 */
public class AFGenreCotisation extends BAbstractEntityExternalService {

    public AFGenreCotisation() {
        super();
    }

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {

    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
        if ((entity != null) && (entity instanceof AFCotisation)) {
            AFCotisation coti = (AFCotisation) entity;
            if (!coti.getSession().hasErrors()) {
                if (coti.getAffiliation().getTypeAffiliation().equalsIgnoreCase(CodeSystem.TYPE_AFFILI_INDEP)) {
                    coti.setPeriodicite(CodeSystem.PERIODICITE_TRIMESTRIELLE);
                }
            }
        }
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
