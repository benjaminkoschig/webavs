package ch.globaz.vulpecula.businessimpl.services.caissemaladie;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.caissemaladie.SuiviCaisseMaladieService;
import ch.globaz.vulpecula.domain.models.caissemaladie.SuiviCaisseMaladie;
import ch.globaz.vulpecula.domain.repositories.caissemaladie.SuiviCaisseMaladieRepository;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class SuiviCaisseMaladieServiceImpl implements SuiviCaisseMaladieService {
    private SuiviCaisseMaladieRepository repository = VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository();

    public SuiviCaisseMaladieServiceImpl(SuiviCaisseMaladieRepository repository) {
        this.repository = repository;
    }

    @Override
    public Map<Administration, Collection<SuiviCaisseMaladie>> findSuivisStandardNonEnvoyeesGroupByCaisseAF() {
        List<SuiviCaisseMaladie> suivis = repository.findSuivisStandardsNonEnvoyees();
        return groupByCaisseAF(suivis);
    }

    @Override
    public Map<Administration, Collection<SuiviCaisseMaladie>> findSuivisFichesAnnoncesNonEnvoyeesGroupByCaisseAF() {
        List<SuiviCaisseMaladie> suivis = repository.findSuivisFichesAnnoncesNonEnvoyees();
        return groupByCaisseAF(suivis);
    }

    private Map<Administration, Collection<SuiviCaisseMaladie>> groupByCaisseAF(Collection<SuiviCaisseMaladie> suivis) {
        return Multimaps.index(suivis, new Function<SuiviCaisseMaladie, Administration>() {
            @Override
            public Administration apply(SuiviCaisseMaladie suivi) {
                return suivi.getCaisseMaladie();
            }
        }).asMap();
    }
}
