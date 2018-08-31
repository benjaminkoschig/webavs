package ch.globaz.vulpecula.business.services.postetravail;

import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import ch.globaz.jade.noteIt.NoteException;
import ch.globaz.jade.noteIt.SimpleNote;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.domain.models.prestations.TypePrestation;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.util.CodeSystem;

/**
 * @author Arnaud Geiser (AGE) | Cr�� le 18 f�vr. 2014
 * 
 */
public interface PosteTravailService {
    /**
     * Retourne le num�ro de caisse m�tier (caisse principale) pour un poste de travail particulier
     * 
     * @param idPosteTravail
     * @return le num�ro de caisse m�tier (caisse principale) pour un poste de travail particulier
     */
    int getNumeroCaissePrincipale(String idPosteTravail);

    /**
     * @param idPosteTravail
     * @return l'id tiers de l'administration de la caisse m�tier (caisse prof)
     */
    int getIdTiersCaissePrincipale(String idPosteTravail);

    PosteTravail create(PosteTravail posteTravail) throws UnsatisfiedSpecificationException, JadePersistenceException;

    PosteTravail update(PosteTravail posteTravail) throws UnsatisfiedSpecificationException, JadePersistenceException;

    void delete(PosteTravail posteTravail) throws UnsatisfiedSpecificationException;

    List<PosteTravail> findPostesActifsByIdAffilie(String idAffilie);

    List<PosteTravail> findPostesActifsByIdAffilie(String idAffilie, Date date);

    /**
     * Retourne tous les postes de travails CHEVAUCHEES (qui touche) la p�riode (date d�but, date fin) pass�e param�tre
     * 
     * @param idAffilie String repr�sentant l'id d'un affili�
     * @param dateDebut Date de d�but de chevauchement
     * @param dateFin Date de fin de chevauchement
     * @return Liste de postes de travail
     */
    List<PosteTravail> findPostesActifsByIdAffilie(String idAffilie, Date dateDebut, Date dateFin);

    PosteTravail findPlusAncienPosteActif(String idTravailleur, Date date);

    List<PosteTravail> findPostesActifs(Annee annee);

    /**
     * Retourne si le poste de travail est r�f�renc� dans un d�compte.
     * 
     * @param idDecompte String repr�sentant l'id du poste de travail
     * @return true si dispose de d�comptes
     */
    boolean hasDecomptesSalaires(String idPosteTravail);

    /**
     * Retourne si le poste de travail est r�f�renc� dans un d�compte.
     * 
     * @param posteTravail Poste de travail � v�rifier
     * @return true si dispose de d�comptes
     */
    boolean hasDecomptesSalaires(PosteTravail posteTravail);

    /**
     * Retourne les cotisations utilis�es pour calculer les AJ.
     * Il s'agit des cotisations dont l'assurance est de type AVS ou AC.
     * 
     * @param idPosteTravail String repr�sentant l'id du poste de travail
     * @return Liste de cotsiations pour calculer les AJ
     */
    List<Cotisation> getCotisationsForAJ(String idPosteTravail, Date date);

    /**
     * Retourne les cotisations utilis�es pour calculer les AJ.
     * Il s'agit des cotisations dont l'assurance est de type AVS ou AC.
     * 
     * @param posteTravail sur lequel la recherche s'effectuera
     * @return Liste de cotisations pour calculer les AJ
     */
    List<Cotisation> getCotisationsForAJ(PosteTravail posteTravail, Date date);

    List<Cotisation> getCotisationsElectrciensForCP(PosteTravail posteTravail, Annee annee, Beneficiaire beneficiaire);

    /**
     * Retourne les postes de travail du travailleur qui ont le droit aux AJ, ordr�s par p�riode d'activit�.
     * 
     * @param idTravailleur String repr�sentant l'id d'un travailleur.
     * @return Liste de postes de travails
     */
    List<PosteTravail> getPostesTravailsWithDroitsAJ(String idTravailleur);

    /**
     * Retourne les postes de travail du travailleur qui ont le droit aux CP, ordr�s par p�riode d'activit�.
     * 
     * @param idTravailleur String repr�sentant l'id d'un travailleur.
     * @return Liste de postes de travails
     */
    List<PosteTravail> getPostesTravailsWithDroitsCP(String idTravailleur);

    /**
     * Retourne les postes de travail du travailleur qui ont le droit aux SM, ordr�s par p�riode d'activit�.
     * 
     * @param idTravailleur String repr�sentant l'id d'un travailleur.
     * @return Liste de postes de travails
     */
    List<PosteTravail> getPostesTravailsWithDroitsSM(String idTravailleur);

    /**
     * Retourne si le poste de travail adh�re � une caisse m�tier
     * 
     * @param idPosteTravail String repr�sentant l'id d'un poste de travail
     * @return true si il est affili� � une caisse m�tier ind�pendamment de la date
     */
    boolean hasCaisseMetier(String idPosteTravail);

    /**
     * Retourne le nombre d'heures par mois accord� � un poste de travail dont l'id est pass� en param�tre.
     * Le temps d'occupation est pris en compte pour l'�tablissement de cette valeur.
     * La valeur retourn�e depend de la table de param�trage des prestations en fonction du taux d'occupation.
     * 
     * @param idPosteTravail Id du poste de travail
     * @param date Date de validit�
     * @return Nombre repr�sentant le salaire horaire
     */
    double getNombreHeuresParMois(String idPosteTravail, Date date);

    /**
     * Retourne le nombre d'heures par mois accord� � un poste de travail dont l'id est pass� en param�tre.
     * Le temps d'occupation est pris en compte pour l'�tablissement de cette valeur.
     * La valeur retourn�e depend de la table de param�trage des prestations en fonction du taux d'occupation.
     * 
     * @param idPosteTravail Id du poste de travail
     * @param date Date de validit�
     * @return Nombre repr�sentant le salaire horaire
     */
    double getNombreHeuresParMois(String idPosteTravail, Date dateDebut, Date dateFin);

    /**
     * Retourne le nombre d'heures par mois accord� � un poste de travail dont l'id est pass� en param�tre.
     * Le temps d'occupation est pris en compte pour l'�tablissement de cette valeur.
     * La valeur retourn�e d�pend de la table de param�trage des prestations en fonction du taux d'occupation.
     * 
     * @param idPosteTravail Id du poste de travail
     * @param date Date de validit�
     * @return Nombre repr�sentant le salaire hoaire
     */
    double getNombreHeuresParJour(String idPosteTravail, Date date);

    /**
     * Recherche de tous les travailleurs (postes de travail) devant �tre annonc�s � la MEROBA. On ne retrouvera qu'un
     * seul poste par travailleur, seul le plus r�cent sera annonc� � la MEROBA.
     * Un travailleur devant �tre annonc� doit avoir plus de 18 ans et cotiser � l'AVS.
     * 
     * @return Liste de postes de travail
     */

    List<PosteTravail> findAAnnoncer(Date date);

    List<PosteTravail> findAAnnoncer2(Date date);

    /**
     * Retourne si le poste de travail dispose de la bonne cotisation active afin de disposer du droit pour le type de
     * prestation pass� en param�tre.
     * 
     * @param posteTravail Poste de travail sur lequel l'on souhaite contr�ler qu'il dispose bien du droit � un type de
     *            prestation
     * @param dateDebut Date de d�but
     * @param dateFin Date de fin
     * @param typePrestation Type de prestation
     * @return true si droit � la prestation de type typePrestation
     */
    boolean hasAssuranceActiveForX(PosteTravail posteTravail, Date dateDebut, Date dateFin,
            TypePrestation typePrestation);

    /**
     * Retourne une liste de codes syst�mes correspondant � des qualifications pour la convention dont l'id est pass� en
     * param�tres.
     * 
     * @param idConvention Id de la convention
     * @return Liste de codes syst�mes
     */
    List<CodeSystem> getQualificationForConvention(String idConvention);

    /**
     * Retourne le nombre le plus grand de postes de travails actifs dans l'ann�e pr�c�dente du dernierControle
     */
    int findPostesActifsByIdAffiliePourControle(String idEmployeur, Date dateDernierControle);

    boolean travailleurHasPostIt(String idTravailleur) throws NoteException, JadePersistenceException;

    List<SimpleNote> getPostIt(String idTravailleur) throws NoteException, JadePersistenceException;

    void sendEmailAF(String sujet, String corps) throws Exception;

    List<PosteTravail> findPostesActifDurantAnnee(String idTravailleur, Annee annee);

    void checkForMailAF(PosteTravail posteBeforeUpdate, PosteTravail posteAfterUpdate) throws Exception;}
