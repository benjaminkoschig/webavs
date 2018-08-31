package ch.globaz.vulpecula.business.services.decompte;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;

public interface CotisationDecompteService {
    /**
     * Retourne la liste des cotisations avec lesquels le décompte salaire a été créé.
     * 
     * @param idDecompteSalaire String représentant l'id d'un décompte
     * @return Liste des cotisations d'un décompte salaire
     */
    List<CotisationDecompte> getCotisationsDecompte(String idDecompteSalaire);

    /**
     * Supprime la cotisation d'un décompte salaire dont l'id est passé en paramètre et recalcule le taux de
     * contribution du décompte salaire.
     * 
     * @return Nouveau taux de cotisation du décompte pour les caisses sociales
     */
    Taux deleteCotisationDecompte(String idDecompteSalaire, String idCotisationDecompte);

    void deleteCotisationDecompteWithoutRecalcul(String idDecompteSalaire, String idCotisationDecompte);

    void deleteCotisationDecompteWithoutRecalcul(String idLigneDecopmte);
}
