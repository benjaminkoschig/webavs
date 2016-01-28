package ch.globaz.vulpecula.domain.repositories.decompte;

import java.util.List;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface HistoriqueDecompteRepository extends Repository<HistoriqueDecompte> {
    /**
     * Retourne les historiques du décompte dont l'id est passé en paramètre
     * 
     * @param idDecompte
     *            id à partir duquel les historiques sont recherchés
     * @return La liste des historiques ou vide
     */
    List<HistoriqueDecompte> findByIdDecompte(String idDecompte);

    /**
     * Retourne les historiques de décomptes relatifs aux ids passés en paramètres.
     * 
     * @param ids Liste de String à rechercher
     * @return Liste d'historique de décomptes
     */
    List<HistoriqueDecompte> findByIdDecompteIn(List<String> ids);

    /**
     * Retourne les historiques du décompte dont l'id et l'état sont passé en paramètre triés par ordre DECROISSANT
     * 
     * @param idDecompte
     *            id à partir duquel les historiques sont recherchés
     * @param etat
     *            etat du décompte (ignoré si <code>null</code>)
     * @return La liste des historiques ou vide
     */
    List<HistoriqueDecompte> findLastHistoriqueDecompte(String idDecompte, String etat);

    /**
     * Retourne les historiques du décompte correspondant aux différentes validations triés par ordre DECROISSANT.
     * Une validation correspond aux états {@link EtatDecompte#VALIDE} et {@link EtatDecompte#RECTIFIE}
     * 
     * @param decompte Décompte pour lequel rechercher l'historique
     * @return La liste des historiques ou vide
     */
    List<HistoriqueDecompte> findLastHistoriqueValidationDecompte(Decompte decompte);
}
