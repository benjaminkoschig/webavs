package ch.globaz.vulpecula.external.services.musca;

import globaz.globall.db.BSession;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.prestations.TypePrestation;
import ch.globaz.vulpecula.external.models.musca.Passage;

public interface PassageService {
    /**
     * Retourne un passage de facturation correspond au type de module passé en paramètre.
     * Cette méthode doit être utilisé EXCLUSIVEMENT dans le cas ou un seul passage de ce type de module peut être
     * ouvert en même temps.
     * Dans le cas où il en existe plusieurs, une exception de type {@link IllegalStateException} est retournée.
     * 
     * @param typeModule Code système représentant le type de module
     * @return Unique passage de facturation pour ce module.
     * @throws IllegalStateException dans le cas où il n'existe pas ou plusieurs passages de facturation
     */
    Passage findPassageActif(String typeModule) throws PassageSearchException;

    /**
     * Retourne un passage de facturation correspond au type de prestation passé en paramètre.
     * Cette méthode doit être utilisé EXCLUSIVEMENT dans le cas ou un seul passage de ce type de module peut être
     * ouvert en même temps.
     * Dans le cas où il en existe plusieurs, une exception de type {@link IllegalStateException} est retournée.
     * 
     * @param typePrestation Code système représentant le type de prestation
     * @return Unique passage de facturation pour ce module.
     * @throws IllegalStateException dans le cas où il n'existe pas ou plusieurs passages de facturation
     */
    Passage findPassageActif(TypePrestation typePrestation) throws PassageSearchException;

    /**
     * Retourne un passage de facturation correspondant à l'id du passage passé en paramètre.
     * 
     * @param idPassage Id du passage à rechercher
     * @return Passage de facturation ou null si inexistant
     */
    Passage findById(String idPassage);

    /**
     * Création d'un nouveau passage de facturation pour le type de module et le libelle passé en paramètres.
     * 
     * @param idTypeModule Code système représentant un type de module {@link FAModuleFacturation}
     * @param libelle Nom du module à créer
     * @return
     */
    FAPassage createPassageFacturation(String idTypeModule, String libelle, Date dateOuverture, BSession session);

    /**
     * Création d'un nouveau passage de facturation pour le type de module et le libelle passé en paramètres.
     * Le passage n'est créé que si il n'en existe pas actuellement un d'actif (ouvert).
     * Celui-ci sera ouvert pour la semaine suivant la date du jour.
     * 
     * @param session courante
     * @param idTypeModule Code système représentant un type de module {@link FAModuleFacturation}
     * @param libelle Nom du module à créer
     * @param idPassage courant
     * @return true si le passage a bien été créé, false dans le cas où il y en avait déjà un existant
     */
    boolean createPassageForNextWeekIfNotExist(BSession session, String idTypeModule, String libelle, String idPassage);

    /**
     * Création d'un nouveau passage de facturation pour le type de module et le libelle passé en paramètres.
     * Le passage n'est créé que si il n'en existe pas actuellement un d'actif (ouvert).
     * 
     * @param session courante
     * @param idTypeModule Code système représentant un type de module {@link FAModuleFacturation}
     * @param libelle Nom du module à créer
     * @param idPassage courant
     * @return true si le passage a bien été créé, false dans le cas où il y en avait déjà un existant
     */
    boolean createPassageFacturationIfNotExist(BSession session, String idTypeModule, String libelle, String idPassage,
            Date dateOuverture);

    /**
     * Recherche des passages de facturation qui a concerné l'employeur dont l'id est passé en paramètre.
     * La recherche sera effectué sur les absences justifiées, les congés payés ainsi que les services militaire.
     */
    List<Passage> findByIdEmployeur(String idEmployeur, int offset, int size);

    int countByIdEmployeur(String idEmployeur);

    Passage findOrCreatePassageTO();
}
