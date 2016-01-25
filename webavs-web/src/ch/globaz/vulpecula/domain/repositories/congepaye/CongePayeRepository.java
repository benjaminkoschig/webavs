package ch.globaz.vulpecula.domain.repositories.congepaye;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.repositories.Repository;

/**
 * Repository des congés payés
 * 
 * @since WebBMS 0.01.04
 */
public interface CongePayeRepository extends Repository<CongePaye> {
    /**
     * Suppression d'un congé payé par sa clé primaire
     */
    @Override
    void deleteById(String id);

    /**
     * Recherche les congés payés associées à un travailleur.
     * 
     * @param id : l'id du travailleur pour lequel on souhaite connaître les congés payés
     * @return la liste des congés payés
     */
    List<CongePaye> findByIdTravailleur(String idTravailleur);

    /**
     * Recherche les congés payés associées à un travailleur.
     * Trie la liste par lot (idPassage)
     * 
     * @param id : l'id du travailleur pour lequel on souhaite connaître les congés payés
     * @return la liste des congés payés
     */
    List<CongePaye> findByIdTravailleurOrderByIdpassage(String idTravailleur);

    /**
     * Recherche des congés payés associés au passage de facturation passé en paramètre.
     * 
     * @param id Id du passage de facturation
     * @return Liste de congés pays
     */
    List<CongePaye> findForFacturation(String idPassage);

    /**
     * Recherche des congés payés associés au passage de facturation passé en paramètre.
     * 
     * @param idPassage Id du passage de facturation
     * @return Liste de congés payés associés au passage de facturation
     */
    List<CongePaye> findByIdPassage(String idPassage);

    List<CongePaye> findByIdPassage(String idPassage, String idEmployeur);

    CongePaye findByIdWithDependancies(String idCongePaye);

    List<CongePaye> findBy(String idPassage, String idEmployeur, String idTravailleur, String idConvention,
            String orderBy);

    List<CongePaye> findBy(String idPassage, String idEmployeur, String idTravailleur, String idConvention);

    List<CongePaye> findBy(String idPassageFacturation, String idEmployeur, String idTravailleur, String idConvention,
            Periode periode);
}
