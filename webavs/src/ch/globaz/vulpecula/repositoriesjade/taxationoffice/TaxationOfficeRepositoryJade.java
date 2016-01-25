package ch.globaz.vulpecula.repositoriesjade.taxationoffice;

import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.business.models.taxationoffice.TaxationOfficeComplexModel;
import ch.globaz.vulpecula.business.models.taxationoffice.TaxationOfficeSearchComplexModel;
import ch.globaz.vulpecula.business.models.taxationoffice.TaxationOfficeSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.domain.repositories.taxationoffice.TaxationOfficeRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.taxationoffice.converters.TaxationOfficeConverter;

public class TaxationOfficeRepositoryJade extends
        RepositoryJade<TaxationOffice, TaxationOfficeComplexModel, TaxationOfficeSimpleModel> implements
        TaxationOfficeRepository {

    @Override
    public TaxationOffice findById(String id) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForId(id);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public List<TaxationOffice> findByIdIn(Collection<String> ids) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForIds(ids);
        return searchAndFetch(searchModel);
    }

    @Override
    public TaxationOffice findByIdDecompte(String idDecompte) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForIdDecompte(idDecompte);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public DomaineConverterJade<TaxationOffice, TaxationOfficeComplexModel, TaxationOfficeSimpleModel> getConverter() {
        return new TaxationOfficeConverter();
    }

    @Override
    public List<TaxationOffice> getTaxationForFacturation(String idPassage) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForIdPassageFacturation(idPassage);
        List<TaxationOffice> taxationOffices = searchAndFetch(searchModel);
        for (TaxationOffice taxationOffice : taxationOffices) {
            taxationOffice.setLignes(VulpeculaRepositoryLocator.getLigneTaxationRepository().findByIdTaxationOffice(
                    taxationOffice.getId()));
        }
        return taxationOffices;
    }

    @Override
    public List<TaxationOffice> findBy(String idPassage, String numeroAffilie, EtatTaxation etat) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForIdPassageFacturation(idPassage);
        searchModel.setLikeNoAffilie("%" + numeroAffilie);
        if (etat != null) {
            searchModel.setForEtatIn(etat);
        }
        return searchAndFetch(searchModel);
    }

    @Override
    public int findNbTOActives(Employeur employeur, Date dateDebut) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForIdEmployeur(employeur.getId());
        searchModel.setForEtatNotIn(EtatTaxation.ANNULE);
        searchModel.setForDateContenueDansPeriode(dateDebut);
        return count(searchModel);
    }
}
