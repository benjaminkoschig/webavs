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

        // On contr�ler que le num�ro d'affili� n'existe pas d�j� en base de donn�es.
        Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findByNumAffilie(
                affiliation.getAffilieNumero());
        if (employeur != null) {
            BTransaction transaction = BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction();
            // On enl�ve les anciens messages pour un mettre un nouveau
            JadeThread.logClear();
            transaction.clearErrorBuffer();

            transaction.addErrors("Un employeur avec le num�ro d'affili� " + affiliation.getAffilieNumero()
                    + " est d�j� existant");
        }
    }
}
