package ch.globaz.al.business.services.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.tarif.EcheanceComplexModel;

/**
 * Service permettant la gestion des dates d'échéances
 * 
 * @author PTA
 * 
 */
public interface DatesEcheancePrivateService extends JadeApplicationService {

    /**
     * 
     * Méthode calculant la fin de la validité d'une échéance
     * 
     * @param droitComplex
     *            Les droits
     * @param dateDebutValidite
     *            date du début
     * @param EcheanceCriter
     *            liste des critères âges (12, 16, etc...)
     * @return date fin de validité échéance
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String calculFinValiditeEcheance(DroitComplexModel droitComplex, String dateDebutValidite,
            ArrayList<EcheanceComplexModel> EcheanceCriter) throws JadePersistenceException, JadeApplicationException;

    /**
     * Méthode permettant de calculer la fin de la validité d'une échéance en passant en paramètre une date de début de
     * validité, une échéance des âges (ageDebut et ageFin et une date de naissance
     * 
     * 
     * @param dateDebutValidite
     *            début de la date de validité
     * @param EcheanceCriter
     *            critère d'âge de début et de fin
     * @param dateNaissance
     *            date de naissance
     * @return la date de fin de validité
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String calculFinValiditeEcheance(String dateDebutValidite, EcheanceComplexModel EcheanceCriter,
            String dateNaissance) throws JadeApplicationException, JadePersistenceException;

    /**
     * méthode de calcule de l'âge
     * 
     * @param dateNaissance
     *            naissance de l'enfant
     * @param dateEcheance
     *            date échéance du droit
     * @return âge de l'enfant
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getAgeEnfant(String dateNaissance, String dateEcheance) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Gestion du début de validité d'un droit qui démarre à la fin de la validité d'un autre droit (exemples début de
     * validité d'un droit Formation, faisant suite à la fin de validité d'un droit type enfant)
     * 
     * @param dateFinValiditeDroitEnfant
     *            fin de la validité d'un droit type enfant
     * @return dateDebutValidite Date de début de validité calculée par la méthode
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public String getDateDebutValiditeDroit(String dateFinValiditeDroitEnfant) throws JadeApplicationException;

    /**
     * Méthode qui compare deux dates (date de naissance et date début d'activité) et retourne la date de début de
     * validité du droit
     * 
     * 
     * @param dateDateNaissance
     *            date naissance de la personne
     * @param dateDebutActivite
     *            date début activité
     * @return la date la plus récente
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public String getDateDebutValiditeDroit(String dateDateNaissance, String dateDebutActivite)
            throws JadeApplicationException;

    /**
     * Gestion des échéances, retourne la liste des résultats de la recherche des âges (début et fin) d'un droit selon
     * les valeurs passées en paramètre
     * 
     * @param typePrestation
     *            type de prestation (enfant, formation..)
     * @param catTarif
     *            catégorie de tarif
     * @param catResident
     *            catégorie de résident
     * @param dateDebutValidite
     *            date du début de validité du tarif
     * 
     * @param caExercer
     *            si l'enfant est capable d'exercer
     * @return un tableau retournant les échéances liées aux données passées en paramètre
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList<EcheanceComplexModel> getDebutFinValiditeEcheance(String typePrestation, String catTarif,
            String catResident, String dateDebutValidite, Boolean caExercer) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Gestion de la date de fin, calcule de la date de fin d'une échéance en passant un DroitComplexModel en paramètre
     * et une date de validité
     * 
     * @param droitComplex
     *            Permet de récupère les données nécessaire des droits et enfant
     * @param dateDebutValidite
     *            date de début de validité
     * @return date fin validité échéance
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getFinValiditeEcheance(DroitComplexModel droitComplex, String dateDebutValidite)
            throws JadeApplicationException, JadePersistenceException;
}
