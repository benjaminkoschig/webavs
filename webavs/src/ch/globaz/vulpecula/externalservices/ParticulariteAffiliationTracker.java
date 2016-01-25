package ch.globaz.vulpecula.externalservices;

import globaz.globall.db.BEntity;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;

public class ParticulariteAffiliationTracker extends BAbstractEntityExternalServiceWithContext {

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        startUsingThreadContext(entity.getSession());
        AFParticulariteAffiliation particularite = (AFParticulariteAffiliation) entity;
        if (particularite.getParticularite().equals(CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL)) {
            VulpeculaServiceLocator.getDecompteService().annulerDecompteForParticularite(
                    particularite.getAffiliationId(), particularite.getDateDebut(), particularite.getDateFin());
        }
        stopUsingThreadContext();
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        try {
            startUsingThreadContext(entity.getSession());
            AFParticulariteAffiliation particularite = (AFParticulariteAffiliation) entity;
            if (particularite.getParticularite().equals(CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL)) {
                VulpeculaServiceLocator.getDecompteService().annulerDecompteForParticularite(
                        particularite.getAffiliationId(), particularite.getDateDebut(), particularite.getDateFin());
            }
        } finally {
            stopUsingThreadContext();
        }
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
