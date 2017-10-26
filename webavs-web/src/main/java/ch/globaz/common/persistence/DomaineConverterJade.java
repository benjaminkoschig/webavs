package ch.globaz.common.persistence;

import globaz.jade.persistence.model.JadeAbstractModel;
import ch.globaz.common.domaine.repository.DomainEntity;

public interface DomaineConverterJade<T extends DomainEntity, SM extends JadeAbstractModel> {

    SM convertToPersistence(T entity);

    T convertToDomain(SM model);

    DomaineJadeAbstractSearchModel getSearchModel();

}
