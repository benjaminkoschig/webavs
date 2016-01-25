package ch.globaz.common.vb;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;

/**
 * ViewBean permettant de gérer la persistence des objets du domaine
 * 
 * @since Web@BMS 0.01.02
 */
public abstract class DomainPersistentObjectViewBean<T extends DomainEntity> extends BJadePersistentObjectViewBean
        implements DomainViewBean<T> {

    @Override
    public String getId() {
        return String.valueOf(getEntity().getId());
    }

    @Override
    public void setId(String newId) {
        getEntity().setId(newId);
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(getEntity().getSpy());
    }

    public String getEntityId() {
        return getEntity().getId();
    }

}
