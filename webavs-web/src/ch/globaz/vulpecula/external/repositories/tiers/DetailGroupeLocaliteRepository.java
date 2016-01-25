package ch.globaz.vulpecula.external.repositories.tiers;

import ch.globaz.vulpecula.domain.models.common.DetailGroupeLocalite;
import ch.globaz.vulpecula.domain.models.common.DetailGroupeLocalites;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface DetailGroupeLocaliteRepository extends Repository<DetailGroupeLocalite> {

    /**
     * Recherche la r�gion et le disctrict associ� � une localit�
     */
    DetailGroupeLocalites findByIdLocalite(String id);
}
