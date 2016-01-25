package ch.globaz.vulpecula.business.services.compteur;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.congepaye.Compteur;
import ch.globaz.vulpecula.domain.models.congepaye.Compteurs;
import ch.globaz.vulpecula.domain.models.congepaye.LigneCompteur;

public interface CompteurService {
    /**
     * Création d'un nouveau compteur pour l'id du poste de travail passé en paramètre.
     * 
     * @param idPosteTravail String représentant l'id du poste de travail
     * @param annee du compteur
     * @return Nouveau compteur
     */
    public Compteur createCompteur(String idPosteTravail, Annee annee);

    /**
     * Recherche des compteurs pour l'id du poste de travail et les années entre l'année du début et l'année de fin.
     * Si les compteurs n'existent pas pour une année donnée, ils sont alors créés.
     * 
     * @param idPosteTravail Id du poste de travail
     * @param anneeDebut Année de début
     * @param anneeFin Année de fin
     * @return Liste des compteurs
     */
    public Compteurs findCompteursByIdAndCreateIfNecessary(String idPosteTravail, Annee anneeDebut, Annee anneeFin);

    /**
     * Retourne les compteurs relatifs à une période se situant entre anneeDebut et anneeFin pour un poste de travail.
     * 
     * @param idPosteTravail Id du poste sur lequel les compteurs doivent être recherchés.
     * @param anneeDebut Année de début
     * @param anneeFin Année de fin
     * @return Liste de compteurs sous forme d'objets GSON
     */
    public List<CompteurGSON> findByPosteTravailAndPeriode(String idPosteTravail, String anneeDebut, String anneeFin);

    /**
     * Retourne la somme des montants restants relatif à une période anneeDebut et anneeFin pour un poste de travail.
     * 
     * @param idPosteTravail Id du poste sur lequel les compteurs doivent être recherchés.
     * @param anneeDebut Année de début
     * @param anneeFin Année de fin
     * @return double représentant le montant total restant des compteurs
     */
    public double getMontantTotalForPoste(String idPosteTravail, String anneeDebut, String anneeFin);

    /**
     * Mise à jour d'une liste de compteurs en base de données.
     * 
     * @param compteurs Compteurs à mettre à jour
     */
    public void update(Compteurs compteurs);

    /**
     * Retourne la liste des compteurs par rapport à un idCompteur.
     * 
     * @param idCompteur Id compteur sur lequel effectuer la recherche
     * @return Liste de lignes de compteur relatives au compteur passé en paramètre.
     */
    List<LigneCompteur> findByIdCompteur(String idCompteur);

    /**
     * Retourne si le poste de travail possède des compteurs actifs (non plein)
     * 
     * @param idPosteTravail id du poste de travail à rechercher
     * @return boolean true si le poste de travail possède un compteur actif
     */
    boolean hasCompteurs(String idPosteTravail);

    /**
     * Retourne si le poste de travail possède déjà une entrée dans ses compteurs par la période passé en paramètre.
     * 
     * @param idPosteTravail id du poste de travail
     * @param anneeDebut année de début
     * @param anneeFin année de fin
     * @return true si la période est contenue
     */
    boolean hasLignePourPeriodeSaisie(String idPosteTravail, Annee anneeDebut, Annee anneeFin);
}
