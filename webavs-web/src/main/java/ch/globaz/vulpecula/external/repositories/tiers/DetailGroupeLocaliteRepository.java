package ch.globaz.vulpecula.external.repositories.tiers;

import ch.globaz.vulpecula.domain.models.common.DetailGroupeLocalite;
import ch.globaz.vulpecula.domain.models.common.DetailGroupeLocalites;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface DetailGroupeLocaliteRepository extends Repository<DetailGroupeLocalite> {

    /**
     * Recherche la région et le disctrict associé à une localité
     */
    DetailGroupeLocalites findByIdLocalite(String id);
}
