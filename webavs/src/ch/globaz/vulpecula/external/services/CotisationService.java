package ch.globaz.vulpecula.external.services;

import globaz.naos.db.cotisation.AFCotisation;
import java.util.List;
import java.util.Locale;
import ch.globaz.naos.business.model.TauxAssuranceSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.models.AssuranceTauxComplexModel;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

/**
 * @author Arnaud Geiser (AGE) | Créé le 2 avr. 2014
 * 
 */
public interface CotisationService {
    /**
     * Retourne le taux total d'une cotisation (part employeur et employé) pour une date donnée.
     * 
     * @param idCotisation String représentant l'id d'une cotisation
     * @param date Date à laquelle effectuer la recherche
     * @return double représentant le taux d'une cotisation
     */
    Taux findTaux(String idCotisation, Date date);

    AFCotisation findAFCotisation(String idCotisation, Date date);

    /**
     * Retourne le dernier taux de type Cotisation AVS/AI et de genre paritaire
     * 
     * @return Taux représentant le taux paritaire de la cotisation AVS/AI
     */
    TauxAssuranceSimpleModel findTauxParitaireAVS();

    /**
     * Retourne le dernier taux de type Cotisation AVS&AI et de genre paritaire
     * 
     * @return Taux représentant le taux paritaire de la cotisation AVS/AI
     */
    TauxAssuranceSimpleModel findTauxParitaireAVS(Date date);

    /**
     * Retourne le dernier taux de type Assurance-Chomage et de genre paritaire
     * 
     * @return Taux représentant le taux paritaire de l'assurance chomage
     */
    TauxAssuranceSimpleModel findTauxParitaireAC();

    /**
     * Retourne le dernier taux de type Assurance-Chomage et de genre paritaire
     * 
     * @return Taux représentant le taux paritaire de l'assurance chomage
     */
    TauxAssuranceSimpleModel findTauxParitaireAC(Date date);

    /**
     * Retourne le dernier taux de type AF et de genre paritaire
     * 
     * @return Taux représentant le taux paritaire de l'assurance AF
     */
    TauxAssuranceSimpleModel findTauxParitaireAF(Date date);

    /**
     * Retourne la somme des taux pour un type d'assurance pour un affilié
     * 
     * @param idEmployeur
     *            id de l'affilié
     * @param type
     *            Type représentant un élément du groupe système VETYPEASSU.
     * @return La somme des taux pour un type de cotisation
     */
    Taux findTauxForEmployeurAndType(String idAffilie, TypeAssurance typeAssurance, Date date);

    /**
     * @param idAffilie
     * @param date valeur
     * @return la liste des cotisations propre à l'affiliation dont l'id est passé et la date
     */
    List<Cotisation> findByIdAffilieForDate(String id, Date date);

    List<Cotisation> findByIdAffilieForDate(String id, Date dateDebut, Date dateFin);

    List<Cotisation> findByIdAffilie(String id);

    /**
     * Retourne le libellé de la cotisation, selon le langue de l'utilisateur
     * 
     * @param String idCotisation, Locale langueUtilisateur
     * 
     * @return Libellé de la cotisation
     * 
     */
    String findLibelleCotisation(String idCotisation, Locale userLocale);

    /**
     * Détermine si le travailleur est en âge de cotiser (min et max)
     * Selon les paramètres de l'assurance
     * 
     * @param anneeDebut
     * 
     * @param Travailleur le travailleur, String idCotisation id de la cotisation
     * @return boolean true si le travailleur est en age de cotiser
     * 
     */
    boolean isEnAgeDeCotiser(Travailleur travailleur, String idCotisation, String anneeDebut);

    boolean isEnAgeDeCotiser(Travailleur travailleur, String idCotisation, Date date);

    /**
     * Détermine le taux d'une assurance paritaire AF en fonction de la date.
     * ATTENTION : Cette méthode ne gère pas le cas où il existe plusieurs assurances paritaires AF.
     * 
     * @param date Date à laquelle déterminer les taux
     * @return Informations concernant le taux et l'assurance
     */
    AssuranceTauxComplexModel findAssuranceTauxParitaireAF(Date date);

    /**
     * Détermine le taux d'une assurance paritaire AC en fonction de la date.
     * ATTENTION : Cette méthode ne gère pas le cas où il existe plusieurs assurances paritaires AC.
     * 
     * @param date Date à laquelle déterminer les taux
     * @return Informations concernant le taux et l'assurance
     */
    AssuranceTauxComplexModel findAssuranceTauxParitaireAC(Date date);

    /**
     * Détermine le taux d'une assurance paritaire AVS en fonction de la date.
     * ATTENTION : Cette méthode ne gère pas le cas où il existe plusieurs assurances paritaires AVS.
     * 
     * @param date Date à laquelle déterminer les taux
     * @return Informations concernant le taux et l'assurance
     */
    AssuranceTauxComplexModel findAssuranceTauxParitaireAVS(Date date);

    TauxAssuranceSimpleModel findTauxForAssurance(String idAssurance, Date date);

    /**
     * @param idCotisation
     * @return la cotisation correspondant à l'id
     */
    CotisationView findByIdCotisation(String idCotisation);

}
