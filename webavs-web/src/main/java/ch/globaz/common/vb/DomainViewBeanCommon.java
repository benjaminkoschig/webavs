package ch.globaz.common.vb;

import ch.globaz.common.domaine.repository.DomainEntity;

public interface DomainViewBeanCommon<T extends DomainEntity> {

    /**
     * @return l'entité courante du domaine
     */
    public T getEntity();
}
