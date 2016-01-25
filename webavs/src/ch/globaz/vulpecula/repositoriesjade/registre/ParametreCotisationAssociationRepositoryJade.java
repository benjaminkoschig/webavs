package ch.globaz.vulpecula.repositoriesjade.registre;

import java.util.List;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationComplexModel;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationSearchComplexModel;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationSimpleModel;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;
import ch.globaz.vulpecula.domain.repositories.registre.ParametreCotisationAssociationRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.registre.converters.ParametreCotisationAssociationConverter;

public class ParametreCotisationAssociationRepositoryJade
        extends
        RepositoryJade<ParametreCotisationAssociation, ParametreCotisationAssociationComplexModel, ParametreCotisationAssociationSimpleModel>
        implements ParametreCotisationAssociationRepository {

    @Override
    public DomaineConverterJade<ParametreCotisationAssociation, ParametreCotisationAssociationComplexModel, ParametreCotisationAssociationSimpleModel> getConverter() {
        return ParametreCotisationAssociationConverter.getInstance();
    }

    @Override
    public List<ParametreCotisationAssociation> findAll() {
        ParametreCotisationAssociationSearchComplexModel searchModel = new ParametreCotisationAssociationSearchComplexModel();
        return searchAndFetch(searchModel);
    }

    @Override
    public List<ParametreCotisationAssociation> findCotisationsForFourchette(ParametreCotisationAssociation cotisationCM) {
        ParametreCotisationAssociationSearchComplexModel searchModel = new ParametreCotisationAssociationSearchComplexModel();
        searchModel.setForIdCotisationAssociationProfessionnelle(cotisationCM
                .getIdCotisationAssociationProfessionnelle());
        return searchAndFetch(searchModel);
    }
}
