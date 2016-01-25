package ch.globaz.vulpecula.domain.repositories.servicemilitaire;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface ServiceMilitaireRepository extends Repository<ServiceMilitaire> {

    /**
     * Retourne les services militaires rattach�s au passage de facturation pass� en param�tre.
     * 
     * @param idPassageFacturation String repr�sentant l'id d'un passage de facturation.
     * @return Toutes les prestations services militaires rattach�es au passage de facturation
     */
    List<ServiceMilitaire> findForFacturation(String idPassageFacturation);

    /**
     * Retourne les services militaires rattach�s au passage de facturation pass� en param�tre.
     * 
     * @param idPassage String repr�sentant l'id d'un passage de facturation
     * @return Toutes les prestations services militaires rattach�s au passage de facturation
     */
    List<ServiceMilitaire> findByIdPassage(String idPassage);

    List<ServiceMilitaire> findByIdPassage(String idPassage, String idEmployeur);

    /**
     * Retourne les services militaires rattach�s au travailleur dont l'id est pass� en param�tre.
     * 
     * @param idTravailleur String repr�sentant l'id d'un travailleur
     * @return Toutes les prestations services militaires rattach�s au travailleur
     */
    List<ServiceMilitaire> findByIdTravailleur(String idTravailleur);

    /**
     * Retourne les services militaires rattach�s au travailleur dont l'id est pass� en param�tre.
     * Trie la liste par lot (idPassage)
     * 
     * @param idTravailleur String repr�sentant l'id d'un travailleur
     * @return Toutes les prestations services militaires rattach�s au travailleur
     */
    List<ServiceMilitaire> findByIdTravailleurOrderByIdpassage(String idTravailleur);

    /**
     * Suppression d'un service militaire par son id
     * 
     * @param idServiceMilitaire Id du service militaire � supprimer
     */
    @Override
    void deleteById(String idServiceMilitaire);

    List<ServiceMilitaire> findBy(String idPassageFacturation, String idEmployeur, String idTravailleur,
            String idConvention, String orderBy);

    List<ServiceMilitaire> findBy(String idPassageFacturation, String idEmployeur, String idTravailleur,
            String idConvention);

    List<ServiceMilitaire> findBy(String idPassageFacturation, String idEmployeur, String idTravailleur,
            String idConvention, Periode periode);
}
