package ch.globaz.vulpecula.business.services.compteur;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.congepaye.Compteur;
import ch.globaz.vulpecula.domain.models.congepaye.Compteurs;
import ch.globaz.vulpecula.domain.models.congepaye.LigneCompteur;

public interface CompteurService {
    /**
     * Cr�ation d'un nouveau compteur pour l'id du poste de travail pass� en param�tre.
     * 
     * @param idPosteTravail String repr�sentant l'id du poste de travail
     * @param annee du compteur
     * @return Nouveau compteur
     */
    public Compteur createCompteur(String idPosteTravail, Annee annee);

    /**
     * Recherche des compteurs pour l'id du poste de travail et les ann�es entre l'ann�e du d�but et l'ann�e de fin.
     * Si les compteurs n'existent pas pour une ann�e donn�e, ils sont alors cr��s.
     * 
     * @param idPosteTravail Id du poste de travail
     * @param anneeDebut Ann�e de d�but
     * @param anneeFin Ann�e de fin
     * @return Liste des compteurs
     */
    public Compteurs findCompteursByIdAndCreateIfNecessary(String idPosteTravail, Annee anneeDebut, Annee anneeFin);

    /**
     * Retourne les compteurs relatifs � une p�riode se situant entre anneeDebut et anneeFin pour un poste de travail.
     * 
     * @param idPosteTravail Id du poste sur lequel les compteurs doivent �tre recherch�s.
     * @param anneeDebut Ann�e de d�but
     * @param anneeFin Ann�e de fin
     * @return Liste de compteurs sous forme d'objets GSON
     */
    public List<CompteurGSON> findByPosteTravailAndPeriode(String idPosteTravail, String anneeDebut, String anneeFin);

    /**
     * Retourne la somme des montants restants relatif � une p�riode anneeDebut et anneeFin pour un poste de travail.
     * 
     * @param idPosteTravail Id du poste sur lequel les compteurs doivent �tre recherch�s.
     * @param anneeDebut Ann�e de d�but
     * @param anneeFin Ann�e de fin
     * @return double repr�sentant le montant total restant des compteurs
     */
    public double getMontantTotalForPoste(String idPosteTravail, String anneeDebut, String anneeFin);

    /**
     * Mise � jour d'une liste de compteurs en base de donn�es.
     * 
     * @param compteurs Compteurs � mettre � jour
     */
    public void update(Compteurs compteurs);

    /**
     * Retourne la liste des compteurs par rapport � un idCompteur.
     * 
     * @param idCompteur Id compteur sur lequel effectuer la recherche
     * @return Liste de lignes de compteur relatives au compteur pass� en param�tre.
     */
    List<LigneCompteur> findByIdCompteur(String idCompteur);

    /**
     * Retourne si le poste de travail poss�de des compteurs actifs (non plein)
     * 
     * @param idPosteTravail id du poste de travail � rechercher
     * @return boolean true si le poste de travail poss�de un compteur actif
     */
    boolean hasCompteurs(String idPosteTravail);

    /**
     * Retourne si le poste de travail poss�de d�j� une entr�e dans ses compteurs par la p�riode pass� en param�tre.
     * 
     * @param idPosteTravail id du poste de travail
     * @param anneeDebut ann�e de d�but
     * @param anneeFin ann�e de fin
     * @return true si la p�riode est contenue
     */
    boolean hasLignePourPeriodeSaisie(String idPosteTravail, Annee anneeDebut, Annee anneeFin);
}
