package ch.globaz.vulpecula.domain.repositories.congepaye;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.repositories.Repository;

/**
 * Repository des cong�s pay�s
 * 
 * @since WebBMS 0.01.04
 */
public interface CongePayeRepository extends Repository<CongePaye> {
    /**
     * Suppression d'un cong� pay� par sa cl� primaire
     */
    @Override
    void deleteById(String id);

    /**
     * Recherche les cong�s pay�s associ�es � un travailleur.
     * 
     * @param id : l'id du travailleur pour lequel on souhaite conna�tre les cong�s pay�s
     * @return la liste des cong�s pay�s
     */
    List<CongePaye> findByIdTravailleur(String idTravailleur);

    /**
     * Recherche les cong�s pay�s associ�es � un travailleur.
     * Trie la liste par lot (idPassage)
     * 
     * @param id : l'id du travailleur pour lequel on souhaite conna�tre les cong�s pay�s
     * @return la liste des cong�s pay�s
     */
    List<CongePaye> findByIdTravailleurOrderByIdpassage(String idTravailleur);

    /**
     * Recherche des cong�s pay�s associ�s au passage de facturation pass� en param�tre.
     * 
     * @param id Id du passage de facturation
     * @return Liste de cong�s pays
     */
    List<CongePaye> findForFacturation(String idPassage);

    /**
     * Recherche des cong�s pay�s associ�s au passage de facturation pass� en param�tre.
     * 
     * @param idPassage Id du passage de facturation
     * @return Liste de cong�s pay�s associ�s au passage de facturation
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
