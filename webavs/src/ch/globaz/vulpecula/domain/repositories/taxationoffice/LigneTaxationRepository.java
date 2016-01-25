package ch.globaz.vulpecula.domain.repositories.taxationoffice;

import java.util.List;
import ch.globaz.vulpecula.domain.models.taxationoffice.LigneTaxation;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface LigneTaxationRepository extends Repository<LigneTaxation> {
    /**
     * Retourne les lignes de taxation par rapport à l'id d'une taxation d'office.
     * 
     * @param idTaxationOffice id à rechercher
     * @return Liste de lignes appartenant à la taxation d'office passée en paramètre
     */
    List<LigneTaxation> findByIdTaxationOffice(String idTaxationOffice);
}
