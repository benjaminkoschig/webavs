package ch.globaz.vulpecula.repositoriesjade.association;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.List;
import ch.globaz.vulpecula.business.models.association.LigneFactureAssociationProfessionnelleSearchSimpleModel;
import ch.globaz.vulpecula.business.models.association.LigneFactureAssociationProfessionnelleSimpleModel;
import ch.globaz.vulpecula.domain.models.association.LigneFactureAssociation;
import ch.globaz.vulpecula.domain.repositories.association.LigneFactureAssociationProfessionnelleRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.association.converter.LigneFactureAssociationProfessionnelleConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class LigneFactureAssociationProfessionnelleRepositoryJade extends
        RepositoryJade<LigneFactureAssociation, JadeComplexModel, LigneFactureAssociationProfessionnelleSimpleModel>
        implements LigneFactureAssociationProfessionnelleRepository {

    @Override
    public DomaineConverterJade<LigneFactureAssociation, JadeComplexModel, LigneFactureAssociationProfessionnelleSimpleModel> getConverter() {
        return LigneFactureAssociationProfessionnelleConverter.getInstance();
    }

    @Override
    public List<LigneFactureAssociation> findByIdEntete(String idEntete) {
        LigneFactureAssociationProfessionnelleSearchSimpleModel searchModel = new LigneFactureAssociationProfessionnelleSearchSimpleModel();
        searchModel.setForIdEntete(idEntete);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<LigneFactureAssociation> findByIdAssociationCotisation(String idAssociationCotisation) {
        LigneFactureAssociationProfessionnelleSearchSimpleModel searchModel = new LigneFactureAssociationProfessionnelleSearchSimpleModel();
        searchModel.setForIdAssociationCotisation(idAssociationCotisation);
        return searchAndFetch(searchModel);
    }
}
