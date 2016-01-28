package ch.globaz.common.vb;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;

/**
 * Interface commune des viewbeans du domaine
 * 
 * @since Web@BMS 0.01.02
 */
public interface DomainViewBean<T extends DomainEntity> {

    /**
     * @return l'entité courante du domaine
     */
    public T getEntity();
}
