package ch.globaz.vulpecula.domain.repositories.ebusiness;

import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.ebusiness.Synchronisation;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface SynchronisationRepository extends Repository<Synchronisation> {

    Synchronisation findBySynchronizeId(String id);

    List<Synchronisation> findAll();

    List<Synchronisation> findDecompteToSynchronize(String idEmployeur);

    Map<String, Synchronisation> findDecompteToSynchronize(String idEmployeur, String yearsMonthFrom,
            String yearsMonthTo, String status);

    boolean mustBeSynchronized(String idDecompte, TypeDecompte type);

    List<Synchronisation> findDecompteToAck(String synchronizeId, String idDecompte);
}
