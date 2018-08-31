package ch.globaz.vulpecula.domain.repositories.postetravail;

import java.util.List;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.repositories.Repository;

/**
 * D�finition des m�thodes pour le repository relatives � la classe {@link Travailleur}
 * 
 * @author sel
 */
public interface TravailleurRepository extends Repository<Travailleur> {
    /**
     * Retourne l'enesemble des travailleurs existant en base de donn�es
     * 
     * @return List<{@link Travailleur}> contenant tous les travailleurs
     */
    List<Travailleur> findAll();

    /**
     * Retourne un travailleur par rapport � son id tiers.
     * 
     * @param idTiers
     *            idTiers � rechercher
     * @return Un travailleur ou null si non pr�sent en base de donn�es.
     */
    Travailleur findByIdTiers(String idTiers);

    /**
     * Retourne un travailleur par rapport � son nss.
     * 
     * @param nss
     * @return Un travailleur ou null si non pr�sent en base de donn�es.
     */
    Travailleur findByNss(String nss);

    /**
     * Retourne un travailleur par rapport � son nom / prenom et date de naissance.
     * 
     * @param nom
     * @param prenom
     * @param dateNaissance
     * @return Un travailleur, null si non pr�sent en base de donn�es, null si exception.
     */
    Travailleur findByNomPrenomDateNaissance(String nom, String prenom, String dateNaissance);

    /**
     * Pour portail : retourne un travailleur par rapport � son correlationId
     * 
     * @param nom
     * @param prenom
     * @param dateNaissance
     * @return Un travailleur, null si non pr�sent en base de donn�es, null si exception.
     */
    Travailleur findByCorrelationId(String correlationId);

    /**
     * Retourne la liste de travailleur en fonction du nom / prenom et de la date de naissance.
     * 
     * @param nom
     * @param prenom
     * @param dateNaissance
     * @return Une liste de travailleur, null si aucun pr�sent en base de donn�es, null si exception.
     */
    List<Travailleur> findByNomPrenomDateNaissanceV2(String nom, String prenom, String dateNaissance);

    /**
     * Retourne le nombre de travailleur retourn� par la requ�te
     * 
     * @param nom
     * @param prenom
     * @param dateNaissance
     * @return le nombre de travailleur ou null si non trouv� en DB.
     */
    int countForNomPrenomDateNaissance(String nom, String prenom, String dateNaissance);
}
