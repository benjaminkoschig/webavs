package ch.globaz.vulpecula.repositoriesjade.caissemaladie;

import java.util.List;
import ch.globaz.vulpecula.business.models.caissemaladie.SuiviCaisseMaladieComplexModel;
import ch.globaz.vulpecula.business.models.caissemaladie.SuiviCaisseMaladieSearchComplexModel;
import ch.globaz.vulpecula.business.models.caissemaladie.SuiviCaisseMaladieSimpleModel;
import ch.globaz.vulpecula.domain.models.caissemaladie.SuiviCaisseMaladie;
import ch.globaz.vulpecula.domain.repositories.caissemaladie.SuiviCaisseMaladieRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.caissemaladie.converters.SuiviCaisseMaladieConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class SuiviCaisseMaladieRepositoryJade extends
        RepositoryJade<SuiviCaisseMaladie, SuiviCaisseMaladieComplexModel, SuiviCaisseMaladieSimpleModel> implements
        SuiviCaisseMaladieRepository {
    @Override
    public SuiviCaisseMaladie findById(String id) {
        SuiviCaisseMaladieSearchComplexModel searchModel = new SuiviCaisseMaladieSearchComplexModel();
        searchModel.setForId(id);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public List<SuiviCaisseMaladie> findByIdTravailleurAndCaisseMaladie(String idTravailleur, String idCaisseMaladie) {
        SuiviCaisseMaladieSearchComplexModel searchModel = new SuiviCaisseMaladieSearchComplexModel();
        searchModel.setForIdTravailleur(idTravailleur);
        searchModel.setForIdCaisseMaladie(idCaisseMaladie);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<SuiviCaisseMaladie> findSuivisStandardsNonEnvoyees() {
        SuiviCaisseMaladieSearchComplexModel searchModel = new SuiviCaisseMaladieSearchComplexModel();
        searchModel.setForIsEnvoye(false);
        searchModel.setForTypeDocumentStandard();
        return searchAndFetch(searchModel);
    }

    @Override
    public List<SuiviCaisseMaladie> findSuivisFichesAnnoncesNonEnvoyees() {
        SuiviCaisseMaladieSearchComplexModel searchModel = new SuiviCaisseMaladieSearchComplexModel();
        searchModel.setForIsEnvoye(false);
        searchModel.setForTypeDocumentFichesAnnonce();
        return searchAndFetch(searchModel);
    }

    @Override
    public DomaineConverterJade<SuiviCaisseMaladie, SuiviCaisseMaladieComplexModel, SuiviCaisseMaladieSimpleModel> getConverter() {
        return new SuiviCaisseMaladieConverter();
    }
}
