package ch.globaz.vulpecula.business.services.caissemaladie;

import java.util.Collection;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.caissemaladie.SuiviCaisseMaladie;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public interface SuiviCaisseMaladieService {
    Map<Administration, Collection<SuiviCaisseMaladie>> findSuivisStandardNonEnvoyeesGroupByCaisseAF();

    Map<Administration, Collection<SuiviCaisseMaladie>> findSuivisFichesAnnoncesNonEnvoyeesGroupByCaisseAF();
}
