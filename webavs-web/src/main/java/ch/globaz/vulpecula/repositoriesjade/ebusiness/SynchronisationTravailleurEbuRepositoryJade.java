package ch.globaz.vulpecula.repositoriesjade.ebusiness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationTravailleurEbuComplexModel;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationTravailleurEbuSearchComplexModel;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationTravailleurEbuSearchSimpleModel;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationTravailleurEbuSimpleModel;
import ch.globaz.vulpecula.domain.models.ebusiness.SynchronisationTravailleur;
import ch.globaz.vulpecula.domain.repositories.ebusiness.SynchronisationTravailleurEbuRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.ebusiness.converters.SynchronisationTravailleurEbuConverter;

public class SynchronisationTravailleurEbuRepositoryJade
        extends
        RepositoryJade<SynchronisationTravailleur, SynchronisationTravailleurEbuComplexModel, SynchronisationTravailleurEbuSimpleModel>
        implements SynchronisationTravailleurEbuRepository {

    @Override
    public List<SynchronisationTravailleur> findAll() {
        SynchronisationTravailleurEbuSearchComplexModel searchModel = new SynchronisationTravailleurEbuSearchComplexModel();
        return searchAndFetch(searchModel);
    }

    @Override
    public List<SynchronisationTravailleur> findByIdEmployeur(String idEmployeur) {
        SynchronisationTravailleurEbuSearchComplexModel searchModel = new SynchronisationTravailleurEbuSearchComplexModel();
        searchModel.setForDateSynchronisationIsEmpty(true);
        searchModel.setForIdEmployeur(idEmployeur);
        return searchAndFetch(searchModel);
    }

    @Override
    public Map<String, SynchronisationTravailleur> findTravailleurToSynchronize(String forIdEmployeur) {

        SynchronisationTravailleurEbuSearchComplexModel searchModel = new SynchronisationTravailleurEbuSearchComplexModel();
        searchModel.setForDateSynchronisationIsEmpty(true);
        searchModel.setForIdEmployeur(forIdEmployeur);
        List<SynchronisationTravailleur> liste = searchAndFetch(searchModel);

        HashMap<String, SynchronisationTravailleur> mapToSynchronize = new HashMap<String, SynchronisationTravailleur>();
        for (SynchronisationTravailleur synchronisation : liste) {
            if (!mapToSynchronize.containsKey(synchronisation.getTravailleur().getId())) {
                mapToSynchronize.put(synchronisation.getTravailleur().getId(), synchronisation);
            } else {
                String idDansLaMap = mapToSynchronize.get(synchronisation.getTravailleur().getId()).getId();
                if (Integer.valueOf(idDansLaMap) < Integer.valueOf(synchronisation.getId())) {
                    mapToSynchronize.remove(synchronisation.getTravailleur().getId());
                    mapToSynchronize.put(synchronisation.getTravailleur().getId(), synchronisation);
                }
            }
        }
        return mapToSynchronize;
    }

    @Override
    public boolean mustBeSynchronized(String idTravailleur) {
        return false;
    }

    @Override
    public List<SynchronisationTravailleur> findTravailleurToAck(String synchronizeId, String idTravailleur) {
        List<SynchronisationTravailleur> liste = new ArrayList<SynchronisationTravailleur>();
        SynchronisationTravailleurEbuSearchSimpleModel search = new SynchronisationTravailleurEbuSearchSimpleModel();
        search.setForIdTravailleur(idTravailleur);
        List<SynchronisationTravailleur> listeSynchro = searchAndFetch(search);
        for (SynchronisationTravailleur synchronisation : listeSynchro) {
            if (Integer.valueOf(synchronisation.getId()) < Integer.valueOf(synchronizeId)) {
                liste.add(synchronisation);
            }
        }
        return liste;
    }

    @Override
    public DomaineConverterJade<SynchronisationTravailleur, SynchronisationTravailleurEbuComplexModel, SynchronisationTravailleurEbuSimpleModel> getConverter() {
        return new SynchronisationTravailleurEbuConverter();
    }

    @Override
    public List<SynchronisationTravailleur> findByIdTravailleur(String idTravailleur) {
        SynchronisationTravailleurEbuSearchSimpleModel search = new SynchronisationTravailleurEbuSearchSimpleModel();
        search.setForIdTravailleur(idTravailleur);
        return searchAndFetch(search);
    }

    @Override
    public List<SynchronisationTravailleur> findRefusedTravailleurToSynchronize(String idEmployeur) {
        SynchronisationTravailleurEbuSearchComplexModel searchModel = new SynchronisationTravailleurEbuSearchComplexModel();
        searchModel.setWhereKey("inAnnonce");
        searchModel.setForDateSynchronisationIsEmpty(true);
        searchModel.setForIdEmployeur(idEmployeur);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<SynchronisationTravailleur> findByIdAnnnonce(String idAnnonce) {
        SynchronisationTravailleurEbuSearchSimpleModel search = new SynchronisationTravailleurEbuSearchSimpleModel();
        search.setForIdAnnonce(idAnnonce);
        return searchAndFetch(search);
    }
}
