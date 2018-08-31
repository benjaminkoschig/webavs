package ch.globaz.vulpecula.repositoriesjade.association;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.List;
import ch.globaz.vulpecula.business.models.association.AssociationEmployeurSearchSimpleModel;
import ch.globaz.vulpecula.business.models.association.AssociationEmployeurSimpleModel;
import ch.globaz.vulpecula.domain.models.association.AssociationEmployeur;
import ch.globaz.vulpecula.domain.repositories.association.AssociationEmployeurRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.association.converter.AssociationEmployeurConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class AssociationEmployeurRepositoryJade extends
        RepositoryJade<AssociationEmployeur, JadeComplexModel, AssociationEmployeurSimpleModel> implements
        AssociationEmployeurRepository {

    @Override
    public List<AssociationEmployeur> findByIdEmployeur(String idEmployeur) {
        AssociationEmployeurSearchSimpleModel searchModel = new AssociationEmployeurSearchSimpleModel();
        searchModel.setForIdEmployeur(idEmployeur);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<AssociationEmployeur> findByIdEmployeurAndIdAssociation(String idEmployeur, String idAssociation) {
        AssociationEmployeurSearchSimpleModel searchModel = new AssociationEmployeurSearchSimpleModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForIdAssociation(idAssociation);
        return searchAndFetch(searchModel);
    }

    @Override
    public DomaineConverterJade<AssociationEmployeur, JadeComplexModel, AssociationEmployeurSimpleModel> getConverter() {
        return AssociationEmployeurConverter.getInstance();
    }

}
