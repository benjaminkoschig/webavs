package ch.globaz.pegasus.businessimpl.services.converter;

import globaz.jade.persistence.model.JadeComplexModel;

public interface DomaineConverterComplexJade<T, M extends JadeComplexModel> {

    T convertToDomain(M model);

}
