package ch.globaz.vulpecula.repositoriesjade.registre;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.List;
import ch.globaz.vulpecula.business.models.qualification.ConventionQualificationSearchSimpleModel;
import ch.globaz.vulpecula.business.models.qualification.ConventionQualificationSimpleModel;
import ch.globaz.vulpecula.domain.models.registre.ConventionQualification;
import ch.globaz.vulpecula.domain.repositories.registre.ConventionQualificationRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.registre.converters.ConventionQualificationConverter;

/**
 * @author Arnaud Geiser (AGE) | Créé le 15 avr. 2014
 * 
 */
public class ConventionQualificationRepositoryJade extends
        RepositoryJade<ConventionQualification, JadeComplexModel, ConventionQualificationSimpleModel> implements
        ConventionQualificationRepository {

    @Override
    public List<ConventionQualification> findByIdConvention(final String idConvention) {
        ConventionQualificationSearchSimpleModel searchModel = new ConventionQualificationSearchSimpleModel();
        searchModel.setForIdConvention(idConvention);
        return searchAndFetch(searchModel);
    }

    @Override
    public DomaineConverterJade<ConventionQualification, JadeComplexModel, ConventionQualificationSimpleModel> getConverter() {
        return ConventionQualificationConverter.getInstance();
    }

    @Override
    public ConventionQualification findById(final String id) {
        ConventionQualificationSearchSimpleModel search = new ConventionQualificationSearchSimpleModel();
        search.setForId(id);
        return searchAndFetchFirst(search);
    }

}
