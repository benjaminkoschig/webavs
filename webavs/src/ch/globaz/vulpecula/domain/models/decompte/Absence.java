package ch.globaz.vulpecula.domain.models.decompte;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;

/**
 * Code système représentant une absence pour un décompte salaire.
 * 
 */
public class Absence implements DomainEntity {
    private String idAbsence;
    private TypeAbsence type;
    private String spy;

    public TypeAbsence getType() {
        return type;
    }

    public String getTypeAsValue() {
        if (type != null) {
            return type.getValue();
        }
        return null;
    }

    public void setType(TypeAbsence type) {
        this.type = type;
    }

    @Override
    public String getId() {
        return idAbsence;
    }

    @Override
    public void setId(String id) {
        idAbsence = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }
}
