package ch.globaz.vulpecula.external.repositories.affiliation;

import java.util.List;
import ch.globaz.vulpecula.external.models.affiliation.Adhesion;

/**
 * @author Arnaud Geiser (AGE) | Cr�� le 7 f�vr. 2014
 * 
 */
public interface AdhesionRepository {
    List<Adhesion> findByIdAffilie(String idAffilie);

    List<Adhesion> findAdhesionsActivesByIdAffilie(String idAffilie);

    /**
     * Retourne la caisse m�tier pour une affiliation
     * 
     * @param idAffilie
     * @return
     */
    Adhesion findCaisseMetier(String idAffilie);
}
