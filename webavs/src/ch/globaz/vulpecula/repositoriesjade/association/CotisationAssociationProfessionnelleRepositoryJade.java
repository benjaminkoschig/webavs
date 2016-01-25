package ch.globaz.vulpecula.repositoriesjade.association;

import java.util.List;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleComplexModel;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleSearchComplexModel;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleSimpleModel;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.repositories.association.CotisationAssociationProfessionnelleRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.association.converter.CotisationAssociationProfessionnelleConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class CotisationAssociationProfessionnelleRepositoryJade
        extends
        RepositoryJade<CotisationAssociationProfessionnelle, CotisationAssociationProfessionnelleComplexModel, CotisationAssociationProfessionnelleSimpleModel>
        implements CotisationAssociationProfessionnelleRepository {

    @Override
    public DomaineConverterJade<CotisationAssociationProfessionnelle, CotisationAssociationProfessionnelleComplexModel, CotisationAssociationProfessionnelleSimpleModel> getConverter() {
        return CotisationAssociationProfessionnelleConverter.getInstance();
    }

    @Override
    public CotisationAssociationProfessionnelle findById(String id) {
        CotisationAssociationProfessionnelleSearchComplexModel searchModel = new CotisationAssociationProfessionnelleSearchComplexModel();
        searchModel.setForId(id);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public List<CotisationAssociationProfessionnelle> findAll() {
        CotisationAssociationProfessionnelleSearchComplexModel searchModel = new CotisationAssociationProfessionnelleSearchComplexModel();
        return searchAndFetch(searchModel);
    }

    @Override
    public List<CotisationAssociationProfessionnelle> findByAssociationGenre(String idAssociation,
            GenreCotisationAssociationProfessionnelle genre) {
        CotisationAssociationProfessionnelleSearchComplexModel searchModel = new CotisationAssociationProfessionnelleSearchComplexModel();
        searchModel.setForGenre(genre);
        searchModel.setForIdAssociation(idAssociation);
        return searchAndFetch(searchModel);
    }
}
