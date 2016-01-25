package ch.globaz.vulpecula.domain.repositories.syndicat;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface ParametreSyndicatRepository extends Repository<ParametreSyndicat> {
    List<ParametreSyndicat> findByIdSyndicat(String idSyndicat);

    List<ParametreSyndicat> findByIdSyndicat(String idSyndicat, String idConvention);

    List<ParametreSyndicat> findForYear(Annee annee);
}
