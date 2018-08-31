package ch.globaz.vulpecula.business.services.decompte;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;

public interface CotisationDecompteService {
    /**
     * Retourne la liste des cotisations avec lesquels le d�compte salaire a �t� cr��.
     * 
     * @param idDecompteSalaire String repr�sentant l'id d'un d�compte
     * @return Liste des cotisations d'un d�compte salaire
     */
    List<CotisationDecompte> getCotisationsDecompte(String idDecompteSalaire);

    /**
     * Supprime la cotisation d'un d�compte salaire dont l'id est pass� en param�tre et recalcule le taux de
     * contribution du d�compte salaire.
     * 
     * @return Nouveau taux de cotisation du d�compte pour les caisses sociales
     */
    Taux deleteCotisationDecompte(String idDecompteSalaire, String idCotisationDecompte);

    void deleteCotisationDecompteWithoutRecalcul(String idDecompteSalaire, String idCotisationDecompte);

    void deleteCotisationDecompteWithoutRecalcul(String idLigneDecopmte);
}
