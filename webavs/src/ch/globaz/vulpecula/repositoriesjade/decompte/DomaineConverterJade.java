package ch.globaz.vulpecula.repositoriesjade.decompte;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;

public interface DomaineConverterJade<T extends DomainEntity, M extends JadeComplexModel, SM extends JadeSimpleModel> {

    abstract T convertToDomain(M model);

    abstract SM convertToPersistence(T entity);

    abstract T convertToDomain(SM simpleModel);

    abstract JadeAbstractSearchModel getSearchSimpleModel();
}
