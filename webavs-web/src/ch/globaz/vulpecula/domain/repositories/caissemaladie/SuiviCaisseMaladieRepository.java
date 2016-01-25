package ch.globaz.vulpecula.domain.repositories.caissemaladie;

import java.util.List;
import ch.globaz.vulpecula.domain.models.caissemaladie.SuiviCaisseMaladie;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface SuiviCaisseMaladieRepository extends Repository<SuiviCaisseMaladie> {
    List<SuiviCaisseMaladie> findByIdTravailleurAndCaisseMaladie(String idTravailleur, String idCaisseMaladie);

    List<SuiviCaisseMaladie> findSuivisStandardsNonEnvoyees();

    List<SuiviCaisseMaladie> findSuivisFichesAnnoncesNonEnvoyees();
}
