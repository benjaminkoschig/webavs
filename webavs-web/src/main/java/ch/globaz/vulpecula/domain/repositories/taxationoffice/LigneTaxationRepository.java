package ch.globaz.vulpecula.domain.repositories.taxationoffice;

import java.util.List;
import ch.globaz.vulpecula.domain.models.taxationoffice.LigneTaxation;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface LigneTaxationRepository extends Repository<LigneTaxation> {
    /**
     * Retourne les lignes de taxation par rapport � l'id d'une taxation d'office.
     * 
     * @param idTaxationOffice id � rechercher
     * @return Liste de lignes appartenant � la taxation d'office pass�e en param�tre
     */
    List<LigneTaxation> findByIdTaxationOffice(String idTaxationOffice);
}
