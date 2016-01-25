package globaz.naos.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;

/**

 */
public class AFGenreAffiliation extends BAbstractEntityExternalService {

    public AFGenreAffiliation() {
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
        if ((entity != null) && (entity instanceof AFAffiliation)) {
            AFAffiliation affiliation = (AFAffiliation) entity;
            if (!affiliation.getSession().hasErrors()) {
                if (affiliation.getTypeAffiliation().equalsIgnoreCase(CodeSystem.TYPE_AFFILI_INDEP)) {
                    affiliation.setPeriodicite(CodeSystem.PERIODICITE_TRIMESTRIELLE);
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
