package ch.globaz.vulpecula.domain.repositories.ebusiness;

import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.ebusiness.SynchronisationTravailleur;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface SynchronisationTravailleurEbuRepository extends Repository<SynchronisationTravailleur> {

    List<SynchronisationTravailleur> findAll();

    List<SynchronisationTravailleur> findByIdEmployeur(final String idEmployeur);

    Map<String, SynchronisationTravailleur> findTravailleurToSynchronize(final String idEmployeur);

    List<SynchronisationTravailleur> findRefusedTravailleurToSynchronize(String idEmployeur);

    boolean mustBeSynchronized(String idTravailleur);

    List<SynchronisationTravailleur> findTravailleurToAck(String synchronizeId, String idTravailleur);

    List<SynchronisationTravailleur> findByIdTravailleur(String idTravailleur);

    List<SynchronisationTravailleur> findByIdAnnnonce(String idAnnonce);
}
