package ch.globaz.vulpecula.domain.repositories.caissemaladie;

import java.util.List;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.repositories.Repository;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public interface AffiliationCaisseMaladieRepository extends Repository<AffiliationCaisseMaladie> {
    /**
     * Recherche tous les affiliations à une caisse maladie pour un travailleur dont l'id est passé en paramètre.
     * 
     * @param idTravailleur String représentant l'id d'un travailleur
     * @return Liste d'affiliations à des caisses maladies
     */
    List<AffiliationCaisseMaladie> findByIdTravailleur(String idTravailleur);

    List<AffiliationCaisseMaladie> findByIdPosteTravail(String idPosteTravail);

    AffiliationCaisseMaladie findActifByIdPosteTravail(String idPosteTravail);

    List<AffiliationCaisseMaladie> findByMoisDebutBeforeDate(Date date);

    List<AffiliationCaisseMaladie> findByMoisDebutBeforeDateForCaisseMaladieWhenDateDebutAnnonceIsZero(
            Administration caisseMaladie, Date date);

    List<AffiliationCaisseMaladie> findByMoisFinBeforeDateForCaisseMaladieWhenDateFinAnnonceIsZero(
            Administration caisseMaladie, Date dateAnnonce);
}
