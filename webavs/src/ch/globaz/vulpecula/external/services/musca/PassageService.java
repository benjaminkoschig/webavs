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
     * Retourne un passage de facturation correspond au type de module pass� en param�tre.
     * Cette m�thode doit �tre utilis� EXCLUSIVEMENT dans le cas ou un seul passage de ce type de module peut �tre
     * ouvert en m�me temps.
     * Dans le cas o� il en existe plusieurs, une exception de type {@link IllegalStateException} est retourn�e.
     * 
     * @param typeModule Code syst�me repr�sentant le type de module
     * @return Unique passage de facturation pour ce module.
     * @throws IllegalStateException dans le cas o� il n'existe pas ou plusieurs passages de facturation
     */
    Passage findPassageActif(String typeModule) throws PassageSearchException;

    /**
     * Retourne un passage de facturation correspond au type de prestation pass� en param�tre.
     * Cette m�thode doit �tre utilis� EXCLUSIVEMENT dans le cas ou un seul passage de ce type de module peut �tre
     * ouvert en m�me temps.
     * Dans le cas o� il en existe plusieurs, une exception de type {@link IllegalStateException} est retourn�e.
     * 
     * @param typePrestation Code syst�me repr�sentant le type de prestation
     * @return Unique passage de facturation pour ce module.
     * @throws IllegalStateException dans le cas o� il n'existe pas ou plusieurs passages de facturation
     */
    Passage findPassageActif(TypePrestation typePrestation) throws PassageSearchException;

    /**
     * Retourne un passage de facturation correspondant � l'id du passage pass� en param�tre.
     * 
     * @param idPassage Id du passage � rechercher
     * @return Passage de facturation ou null si inexistant
     */
    Passage findById(String idPassage);

    /**
     * Cr�ation d'un nouveau passage de facturation pour le type de module et le libelle pass� en param�tres.
     * 
     * @param idTypeModule Code syst�me repr�sentant un type de module {@link FAModuleFacturation}
     * @param libelle Nom du module � cr�er
     * @return
     */
    FAPassage createPassageFacturation(String idTypeModule, String libelle, Date dateOuverture, BSession session);

    /**
     * Cr�ation d'un nouveau passage de facturation pour le type de module et le libelle pass� en param�tres.
     * Le passage n'est cr�� que si il n'en existe pas actuellement un d'actif (ouvert).
     * Celui-ci sera ouvert pour la semaine suivant la date du jour.
     * 
     * @param session courante
     * @param idTypeModule Code syst�me repr�sentant un type de module {@link FAModuleFacturation}
     * @param libelle Nom du module � cr�er
     * @param idPassage courant
     * @return true si le passage a bien �t� cr��, false dans le cas o� il y en avait d�j� un existant
     */
    boolean createPassageForNextWeekIfNotExist(BSession session, String idTypeModule, String libelle, String idPassage);

    /**
     * Cr�ation d'un nouveau passage de facturation pour le type de module et le libelle pass� en param�tres.
     * Le passage n'est cr�� que si il n'en existe pas actuellement un d'actif (ouvert).
     * 
     * @param session courante
     * @param idTypeModule Code syst�me repr�sentant un type de module {@link FAModuleFacturation}
     * @param libelle Nom du module � cr�er
     * @param idPassage courant
     * @return true si le passage a bien �t� cr��, false dans le cas o� il y en avait d�j� un existant
     */
    boolean createPassageFacturationIfNotExist(BSession session, String idTypeModule, String libelle, String idPassage,
            Date dateOuverture);

    /**
     * Recherche des passages de facturation qui a concern� l'employeur dont l'id est pass� en param�tre.
     * La recherche sera effectu� sur les absences justifi�es, les cong�s pay�s ainsi que les services militaire.
     */
    List<Passage> findByIdEmployeur(String idEmployeur, int offset, int size);

    int countByIdEmployeur(String idEmployeur);

    Passage findOrCreatePassageTO();
}
