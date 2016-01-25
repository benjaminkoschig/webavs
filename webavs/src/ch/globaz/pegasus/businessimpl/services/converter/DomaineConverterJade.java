package ch.globaz.pegasus.businessimpl.services.converter;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.common.domaine.EntiteDeDomaine;

public interface DomaineConverterJade<T extends EntiteDeDomaine, M extends JadeComplexModel, SM extends JadeSimpleModel> {

    abstract T convertToDomain(M model);

    abstract SM convertToPersistence(T entity);

    abstract T convertToDomain(SM simpleModel);

    abstract JadeAbstractSearchModel getSearchSimpleModel();
}
