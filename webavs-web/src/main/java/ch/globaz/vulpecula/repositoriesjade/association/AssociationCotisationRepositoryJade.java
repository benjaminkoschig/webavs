package ch.globaz.vulpecula.repositoriesjade.association;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.models.association.AssociationCotisationComplexModel;
import ch.globaz.vulpecula.business.models.association.AssociationCotisationSearchComplexModel;
import ch.globaz.vulpecula.business.models.association.AssociationCotisationSimpleModel;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.repositories.association.AssociationCotisationRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.association.converter.AssociationCotisationConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class AssociationCotisationRepositoryJade extends
        RepositoryJade<AssociationCotisation, AssociationCotisationComplexModel, AssociationCotisationSimpleModel>
        implements AssociationCotisationRepository {

    @Override
    public DomaineConverterJade<AssociationCotisation, AssociationCotisationComplexModel, AssociationCotisationSimpleModel> getConverter() {
        return AssociationCotisationConverter.getInstance();
    }

    @Override
    public List<AssociationCotisation> create(List<AssociationCotisation> associationsCotisations) {
        List<AssociationCotisation> acs = new ArrayList<AssociationCotisation>();
        for (AssociationCotisation associationCotisation : associationsCotisations) {
            acs.add(create(associationCotisation));
        }
        return acs;
    }

    @Override
    public List<AssociationCotisation> findByIdEmployeur(String idEmployeur) {
        AssociationCotisationSearchComplexModel searchModel = new AssociationCotisationSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        return searchAndFetch(searchModel);
    }

}
