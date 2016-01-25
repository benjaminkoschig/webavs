package ch.globaz.vulpecula.repositoriesjade.caissemaladie;

import java.util.List;
import ch.globaz.vulpecula.business.models.caissemaladie.AffiliationCaisseMaladieComplexModel;
import ch.globaz.vulpecula.business.models.caissemaladie.AffiliationCaisseMaladieSearchComplexModel;
import ch.globaz.vulpecula.business.models.caissemaladie.AffiliationCaisseMaladieSimpleModel;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.repositories.caissemaladie.AffiliationCaisseMaladieRepository;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.caissemaladie.converters.AffiliationCaisseMaladieConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class AffiliationCaisseMaladieRepositoryJade
        extends
        RepositoryJade<AffiliationCaisseMaladie, AffiliationCaisseMaladieComplexModel, AffiliationCaisseMaladieSimpleModel>
        implements AffiliationCaisseMaladieRepository {
    private static final String ORDER_BY_MOIS_DEBUT_DESC = "moisDebutDesc";

    @Override
    public AffiliationCaisseMaladie findById(String id) {
        AffiliationCaisseMaladieSearchComplexModel searchModel = new AffiliationCaisseMaladieSearchComplexModel();
        searchModel.setForId(id);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public List<AffiliationCaisseMaladie> findByIdTravailleur(String idTravailleur) {
        AffiliationCaisseMaladieSearchComplexModel searchModel = new AffiliationCaisseMaladieSearchComplexModel();
        searchModel.setForIdTravailleur(idTravailleur);
        searchModel.setOrderKey(ORDER_BY_MOIS_DEBUT_DESC);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<AffiliationCaisseMaladie> findByIdPosteTravail(String idPosteTravail) {
        AffiliationCaisseMaladieSearchComplexModel searchModel = new AffiliationCaisseMaladieSearchComplexModel();
        searchModel.setForIdPosteTravail(idPosteTravail);
        searchModel.setOrderKey(ORDER_BY_MOIS_DEBUT_DESC);
        return searchAndFetch(searchModel);
    }

    @Override
    public AffiliationCaisseMaladie findActifByIdPosteTravail(String idPosteTravail) {
        List<AffiliationCaisseMaladie> affiliations = findByIdPosteTravail(idPosteTravail);
        if (affiliations.size() > 0) {
            AffiliationCaisseMaladie affiliation = affiliations.get(0);
            if (affiliation.isActif()) {
                return affiliation;
            }
        }
        return null;
    }

    @Override
    public DomaineConverterJade<AffiliationCaisseMaladie, AffiliationCaisseMaladieComplexModel, AffiliationCaisseMaladieSimpleModel> getConverter() {
        return new AffiliationCaisseMaladieConverter();
    }

    @Override
    public List<AffiliationCaisseMaladie> findByMoisDebutBeforeDate(Date date) {
        AffiliationCaisseMaladieSearchComplexModel searchModel = new AffiliationCaisseMaladieSearchComplexModel();
        searchModel.setForMoisDebutBefore(date);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<AffiliationCaisseMaladie> findByMoisDebutBeforeDateForCaisseMaladieWhenDateDebutAnnonceIsZero(
            Administration caisseMaladie, Date date) {
        AffiliationCaisseMaladieSearchComplexModel searchModel = new AffiliationCaisseMaladieSearchComplexModel();
        searchModel.setForMoisDebutBefore(date);
        searchModel.setForCaisseMaladie(caisseMaladie);
        searchModel.setForDateDebutAnnonceIsZero();
        return searchAndFetch(searchModel);
    }

    @Override
    public List<AffiliationCaisseMaladie> findByMoisFinBeforeDateForCaisseMaladieWhenDateFinAnnonceIsZero(
            Administration caisseMaladie, Date date) {
        AffiliationCaisseMaladieSearchComplexModel searchModel = new AffiliationCaisseMaladieSearchComplexModel();
        searchModel.setForMoisFinBefore(date);
        searchModel.setForCaisseMaladie(caisseMaladie);
        searchModel.setForDateFinAnnonceIsZero();
        searchModel.setForMoisFinIsNotZero();
        return searchAndFetch(searchModel);
    }
}
