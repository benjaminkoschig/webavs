package ch.globaz.common.vb;

import globaz.globall.db.BSpy;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;

/**
 * ViewBean permettant de gérer la recherche des objets du domaines
 * 
 * @since Web@BMS 0.01.02
 */
public abstract class DomainSearchObjectViewBean<T extends DomainEntity> extends BJadeSearchObjectELViewBean implements
        DomainViewBean<T> {

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
