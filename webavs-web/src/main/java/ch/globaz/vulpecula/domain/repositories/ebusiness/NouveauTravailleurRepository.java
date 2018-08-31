package ch.globaz.vulpecula.domain.repositories.ebusiness;

import java.util.List;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;
import ch.globaz.vulpecula.domain.repositories.Repository;

/**
 * Définition des méthodes pour le repository relatives à la classe {@link Travailleur}
 * 
 * @author sel
 */
public interface NouveauTravailleurRepository extends Repository<TravailleurEbuDomain> {

    List<TravailleurEbuDomain> findAll();

    List<TravailleurEbuDomain> findAllSansQuittance();

    List<TravailleurEbuDomain> findByIdEmployeur(final String idEmployeur);

    TravailleurEbuDomain findByCorrelationId(String correlationId);

    TravailleurEbuDomain findByCorrelationIdWithQuittance(String correlationId);

    TravailleurEbuDomain findByCorrelationIdAndPosteCorrelationId(String correlationId, String posteCorrelationId);

    TravailleurEbuDomain findByIdSansQuittance(String id);

    TravailleurEbuDomain findByPosteCorrelationId(String posteCorrelationId);

    List<TravailleurEbuDomain> findByNSS(String nss);

    TravailleurEbuDomain findByCorrelationAndPosteCorrelationWithQuittance(String correlationId,
            String posteCorrelationId);

}
