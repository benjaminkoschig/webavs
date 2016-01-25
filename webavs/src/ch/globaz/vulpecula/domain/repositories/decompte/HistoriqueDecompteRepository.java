package ch.globaz.vulpecula.domain.repositories.decompte;

import java.util.List;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface HistoriqueDecompteRepository extends Repository<HistoriqueDecompte> {
    /**
     * Retourne les historiques du d�compte dont l'id est pass� en param�tre
     * 
     * @param idDecompte
     *            id � partir duquel les historiques sont recherch�s
     * @return La liste des historiques ou vide
     */
    List<HistoriqueDecompte> findByIdDecompte(String idDecompte);

    /**
     * Retourne les historiques de d�comptes relatifs aux ids pass�s en param�tres.
     * 
     * @param ids Liste de String � rechercher
     * @return Liste d'historique de d�comptes
     */
    List<HistoriqueDecompte> findByIdDecompteIn(List<String> ids);

    /**
     * Retourne les historiques du d�compte dont l'id et l'�tat sont pass� en param�tre tri�s par ordre DECROISSANT
     * 
     * @param idDecompte
     *            id � partir duquel les historiques sont recherch�s
     * @param etat
     *            etat du d�compte (ignor� si <code>null</code>)
     * @return La liste des historiques ou vide
     */
    List<HistoriqueDecompte> findLastHistoriqueDecompte(String idDecompte, String etat);

    /**
     * Retourne les historiques du d�compte correspondant aux diff�rentes validations tri�s par ordre DECROISSANT.
     * Une validation correspond aux �tats {@link EtatDecompte#VALIDE} et {@link EtatDecompte#RECTIFIE}
     * 
     * @param decompte D�compte pour lequel rechercher l'historique
     * @return La liste des historiques ou vide
     */
    List<HistoriqueDecompte> findLastHistoriqueValidationDecompte(Decompte decompte);
}
