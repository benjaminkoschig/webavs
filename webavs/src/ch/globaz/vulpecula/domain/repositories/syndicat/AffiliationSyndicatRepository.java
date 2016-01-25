package ch.globaz.vulpecula.domain.repositories.syndicat;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.syndicat.AffiliationSyndicat;
import ch.globaz.vulpecula.domain.repositories.Repository;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public interface AffiliationSyndicatRepository extends Repository<AffiliationSyndicat> {
    List<AffiliationSyndicat> findByIdTravailleur(String idTravailleur);

    List<AffiliationSyndicat> findBySyndicatAnnee(Administration syndicat, Annee annee);

    List<AffiliationSyndicat> findByAnnee(String idSyndicat, Annee annee);

    List<AffiliationSyndicat> findByAnnee(Annee annee);
}
