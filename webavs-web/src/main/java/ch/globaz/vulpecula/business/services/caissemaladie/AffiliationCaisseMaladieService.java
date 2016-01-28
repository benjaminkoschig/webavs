package ch.globaz.vulpecula.business.services.caissemaladie;

import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public interface AffiliationCaisseMaladieService {
    void create(AffiliationCaisseMaladie affiliationCaisseMaladie) throws UnsatisfiedSpecificationException;

    void update(AffiliationCaisseMaladie affiliationCaisseMaladie) throws UnsatisfiedSpecificationException;

    void delete(AffiliationCaisseMaladie affiliationCaisseMaladie) throws UnsatisfiedSpecificationException;

    /**
     * Méthode qui permet de créer une entrée dans les caisses maladies lors de l'ajout d'un nouveau poste de travail
     * 
     * @param dateDebut
     * @param dateFin
     * @param idTravailleur
     * @param idTiersAdministration
     */
    void createForPosteTravail(PosteTravail poste);
}
