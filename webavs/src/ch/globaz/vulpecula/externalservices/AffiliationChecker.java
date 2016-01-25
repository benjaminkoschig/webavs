package ch.globaz.vulpecula.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.jade.context.JadeThread;
import globaz.naos.db.affiliation.AFAffiliation;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;

public class AffiliationChecker extends BAbstractEntityExternalService {

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
        AFAffiliation affiliation = (AFAffiliation) entity;

        // On contrôler que le numéro d'affilié n'existe pas déjà en base de données.
        Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findByNumAffilie(
                affiliation.getAffilieNumero());
        if (employeur != null) {
            BTransaction transaction = BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction();
            // On enlève les anciens messages pour un mettre un nouveau
            JadeThread.logClear();
            transaction.clearErrorBuffer();

            transaction.addErrors("Un employeur avec le numéro d'affilié " + affiliation.getAffilieNumero()
                    + " est déjà existant");
        }
    }
}
