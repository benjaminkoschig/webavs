package ch.globaz.vulpecula.repositoriesjade.ebusiness;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationEbuComplexModel;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationEbuSearchComplexModel;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationEbuSearchSimpleModel;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationEbuSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.ebusiness.Synchronisation;
import ch.globaz.vulpecula.domain.repositories.ebusiness.SynchronisationRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DecompteRepositoryJade.LoadOptions;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.ebusiness.converters.SynchronisationEbuConverter;

public class SynchronisationRepositoryJade extends
        RepositoryJade<Synchronisation, SynchronisationEbuComplexModel, SynchronisationEbuSimpleModel> implements
        SynchronisationRepository {

    @Override
    public List<Synchronisation> findAll() {
        SynchronisationEbuSearchComplexModel searchModel = new SynchronisationEbuSearchComplexModel();
        return searchAndFetchDependencies(searchModel);
    }

    @Override
    public DomaineConverterJade<Synchronisation, SynchronisationEbuComplexModel, SynchronisationEbuSimpleModel> getConverter() {
        return new SynchronisationEbuConverter();
    }

    private List<Synchronisation> searchAndFetchDependencies(final JadeSearchComplexModel searchModel,
            LoadOptions... options) {
        List<Synchronisation> decomptes = searchAndFetch(searchModel);
        return decomptes;
    }

    @Override
    public List<Synchronisation> findDecompteToSynchronize(String idEmployeur) {
        SynchronisationEbuSearchComplexModel searchModel = new SynchronisationEbuSearchComplexModel();
        searchModel.setForDateSynchronisationIsEmpty(true);
        searchModel.setForIdEmployeur(idEmployeur);
        return searchAndFetchDependencies(searchModel);
    }

    @Override
    public boolean mustBeSynchronized(String idDecompte, TypeDecompte type) {
        Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(idDecompte);
        if (!decompte.isEmployeurEBusiness()) {
            return false;
        }
        if (type.equals(TypeDecompte.PERIODIQUE) || type.equals(TypeDecompte.COMPLEMENTAIRE)
                || type.isTraiterAsSpecial()) {
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Synchronisation> findDecompteToSynchronize(String idEmployeur, String yearsMonthFrom,
            String yearsMonthTo, String status) {
        SynchronisationEbuSearchComplexModel searchModel = new SynchronisationEbuSearchComplexModel();
        searchModel.setForDateSynchronisationIsEmpty(true);
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setYearsMonthFrom(yearsMonthFrom);
        searchModel.setYearsMonthTo(yearsMonthTo);
        searchModel.setStatus(status);
        List<Synchronisation> liste = searchAndFetchDependencies(searchModel);
        HashMap<String, Synchronisation> mapToSynchronize = new HashMap<String, Synchronisation>();
        for (Synchronisation synchronisation : liste) {
            if (!mapToSynchronize.containsKey(synchronisation.getDecompte().getId())) {
                mapToSynchronize.put(synchronisation.getDecompte().getId(), synchronisation);
            } else {
                String idDansLaMap = mapToSynchronize.get(synchronisation.getDecompte().getId()).getId();
                if (Integer.valueOf(idDansLaMap) < Integer.valueOf(synchronisation.getId())) {
                    mapToSynchronize.remove(synchronisation.getDecompte().getId());
                    mapToSynchronize.put(synchronisation.getDecompte().getId(), synchronisation);
                }
            }
        }
        return mapToSynchronize;
    }

    @Override
    public List<Synchronisation> findDecompteToAck(String synchronizeId, String idDecompte) {
        List<Synchronisation> liste = new ArrayList<Synchronisation>();
        SynchronisationEbuSearchSimpleModel search = new SynchronisationEbuSearchSimpleModel();
        search.setForIdDecompte(idDecompte);
        List<Synchronisation> listeSynchro = searchAndFetch(search);
        for (Synchronisation synchronisation : listeSynchro) {
            if (Integer.valueOf(synchronisation.getId()) < Integer.valueOf(synchronizeId)) {
                liste.add(synchronisation);
            }
        }
        return liste;
    }

    @Override
    public Synchronisation findBySynchronizeId(String id) {
        SynchronisationEbuSearchComplexModel searchModel = new SynchronisationEbuSearchComplexModel();
        searchModel.setForId(id);
        searchModel.setForDateSynchronisationIsEmpty(true);
        List<Synchronisation> liste = searchAndFetch(searchModel);
        if (liste.size() > 0) {
            return liste.get(0);
        } else {
            return null;
        }
    }
}
