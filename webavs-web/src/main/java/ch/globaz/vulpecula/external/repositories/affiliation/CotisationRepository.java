package ch.globaz.vulpecula.external.repositories.affiliation;

import java.util.List;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

/**
 * @author Luis Benimeli (LBE) | Créé le 18.05.2017
 * 
 */
public interface CotisationRepository {
    List<Cotisation> findByIdAffilie(String idAffilie);
}
