package ch.globaz.al.business.services.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.tarif.EcheanceComplexModel;

/**
 * Service permettant la gestion des dates d'�ch�ances
 * 
 * @author PTA
 * 
 */
public interface DatesEcheancePrivateService extends JadeApplicationService {

    /**
     * 
     * M�thode calculant la fin de la validit� d'une �ch�ance
     * 
     * @param droitComplex
     *            Les droits
     * @param dateDebutValidite
     *            date du d�but
     * @param EcheanceCriter
     *            liste des crit�res �ges (12, 16, etc...)
     * @return date fin de validit� �ch�ance
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String calculFinValiditeEcheance(DroitComplexModel droitComplex, String dateDebutValidite,
            ArrayList<EcheanceComplexModel> EcheanceCriter) throws JadePersistenceException, JadeApplicationException;

    /**
     * M�thode permettant de calculer la fin de la validit� d'une �ch�ance en passant en param�tre une date de d�but de
     * validit�, une �ch�ance des �ges (ageDebut et ageFin et une date de naissance
     * 
     * 
     * @param dateDebutValidite
     *            d�but de la date de validit�
     * @param EcheanceCriter
     *            crit�re d'�ge de d�but et de fin
     * @param dateNaissance
     *            date de naissance
     * @return la date de fin de validit�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String calculFinValiditeEcheance(String dateDebutValidite, EcheanceComplexModel EcheanceCriter,
            String dateNaissance) throws JadeApplicationException, JadePersistenceException;

    /**
     * m�thode de calcule de l'�ge
     * 
     * @param dateNaissance
     *            naissance de l'enfant
     * @param dateEcheance
     *            date �ch�ance du droit
     * @return �ge de l'enfant
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getAgeEnfant(String dateNaissance, String dateEcheance) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Gestion du d�but de validit� d'un droit qui d�marre � la fin de la validit� d'un autre droit (exemples d�but de
     * validit� d'un droit Formation, faisant suite � la fin de validit� d'un droit type enfant)
     * 
     * @param dateFinValiditeDroitEnfant
     *            fin de la validit� d'un droit type enfant
     * @return dateDebutValidite Date de d�but de validit� calcul�e par la m�thode
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public String getDateDebutValiditeDroit(String dateFinValiditeDroitEnfant) throws JadeApplicationException;

    /**
     * M�thode qui compare deux dates (date de naissance et date d�but d'activit�) et retourne la date de d�but de
     * validit� du droit
     * 
     * 
     * @param dateDateNaissance
     *            date naissance de la personne
     * @param dateDebutActivite
     *            date d�but activit�
     * @return la date la plus r�cente
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public String getDateDebutValiditeDroit(String dateDateNaissance, String dateDebutActivite)
            throws JadeApplicationException;

    /**
     * Gestion des �ch�ances, retourne la liste des r�sultats de la recherche des �ges (d�but et fin) d'un droit selon
     * les valeurs pass�es en param�tre
     * 
     * @param typePrestation
     *            type de prestation (enfant, formation..)
     * @param catTarif
     *            cat�gorie de tarif
     * @param catResident
     *            cat�gorie de r�sident
     * @param dateDebutValidite
     *            date du d�but de validit� du tarif
     * 
     * @param caExercer
     *            si l'enfant est capable d'exercer
     * @return un tableau retournant les �ch�ances li�es aux donn�es pass�es en param�tre
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList<EcheanceComplexModel> getDebutFinValiditeEcheance(String typePrestation, String catTarif,
            String catResident, String dateDebutValidite, Boolean caExercer) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Gestion de la date de fin, calcule de la date de fin d'une �ch�ance en passant un DroitComplexModel en param�tre
     * et une date de validit�
     * 
     * @param droitComplex
     *            Permet de r�cup�re les donn�es n�cessaire des droits et enfant
     * @param dateDebutValidite
     *            date de d�but de validit�
     * @return date fin validit� �ch�ance
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getFinValiditeEcheance(DroitComplexModel droitComplex, String dateDebutValidite)
            throws JadeApplicationException, JadePersistenceException;
}
