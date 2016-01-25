package ch.globaz.al.business.services.models.periodeAF;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;

/**
 * Interface d�finissant les services m�tier impl�ment�s li�s � la p�riode AF
 * 
 * @author GMO
 */
public interface PeriodeAFBusinessService extends JadeApplicationService {

    /**
     * Ferme la p�riode correspondante � la date indiqu�e
     * 
     * @param datePeriode
     *            date voulue
     * @return PeriodeAFModel repr�sentant la p�riode ferm�e
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PeriodeAFModel closePeriode(String datePeriode) throws JadeApplicationException, JadePersistenceException;

    /**
     * R�cup�re la p�riode suivante � celle pass�e en param�tre
     * 
     * @param datePeriode
     *            la p�riode dont on veut la suivante
     * 
     * @return la periode AF pr�c�dente - elle sera cr�e (ouverte) si pas existante
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PeriodeAFModel getNextPeriode(String datePeriode) throws JadeApplicationException, JadePersistenceException;

    /**
     * 
     * @param datePeriode
     *            (MM.yyyy)
     * @return la p�riode formatt�e de d�but de trimestre
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getPeriodeDebutTrimestre(String datePeriode) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * R�cup�re la p�riode AF � utiliser pour les traitements en cours
     * 
     * @param bonification
     *            d�fini si p�riode des processus indirects ou directs
     * @param includePartiel
     *            indique si il faut tenir compte des processus partiels ouverts pour r�cup�rer la p�riode en cours
     * @return la periode AF en cours
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     *             Attention, actuellement toujours utilis�e avec <code>includePartiel=true</code>, changement impacte
     *             toutes les p�riodes par d�faut de g�n�ration unitaire (dossier, affili�, adi)
     * @deprecated utiliser getPeriodeEncours (String bonification, String typeCotisation, boolean includePartiel)
     */
    @Deprecated
    public PeriodeAFModel getPeriodeEnCours(String bonification, boolean includePartiel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * R�cup�re la p�riode AF � utiliser pour les traitements en cours
     * 
     * @param bonification
     *            d�fini si p�riode des processus indirects ou directs
     * 
     * @param includePartiel
     *            indique si il faut tenir compte des processus partiels ouverts pour r�cup�rer la p�riode en cours
     * @return la periode AF en cours
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     *             Attention, actuellement toujours utilis�e avec <code>includePartiel=true</code>, changement impacte
     *             toutes les p�riodes par d�faut de g�n�ration unitaire (dossier, affili�, adi)
     * 
     */

    public PeriodeAFModel getPeriodeEnCours(String bonification, String typeCotisation, boolean includePartiel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * 
     * @param datePeriode
     *            (MM.yyyy)
     * @return la p�riode formatt�e de fin de trimestre
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getPeriodeFinTrimestre(String datePeriode) throws JadeApplicationException, JadePersistenceException;

/**
	 * Retourne sous quelle p�riode g�n�rer pour un dossier en question selon sa p�riodicit� et les processus principaux
	 * d�j� effectu�s
	 * 
	 * @param periodicite
	 *            la p�riodicit� li�e au dossier
	 * @param bonification
	 *            d�fini si p�riode des processus indirects ou directs
	 *              @param typeCotisation
	 *            d�fini si paritaire / personnel / group� / direct {@link ALConstPrestations)
	 * @return la p�riode sous laquelle devrait �tre g�n�r�e la prestation
	 * @throws JadeApplicationException
	 *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
	 * @throws JadePersistenceException
	 *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
	 *             faire
	 */

    public String getPeriodeToGenerateForDossier(String periodicite, String bonification, String typeCotisation)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * R�cup�re la p�riode pr�c�dente � celle pass�e en param�tre
     * 
     * @param datePeriode
     *            la p�riode dont on veut la pr�c�dente
     * 
     * @return la periode AF pr�c�dente - elle sera cr�e (ferm�e) si pas existante
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     */

    public PeriodeAFModel getPreviousPeriode(String datePeriode) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Ouvre une p�riode pour la date indiqu�e, si d�j� ouverte, la p�riode est retourn�e
     * 
     * @param datePeriode
     *            date voulue
     * @return PeriodeAFModel repr�sentant la p�riode ouverte
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PeriodeAFModel openPeriode(String datePeriode) throws JadeApplicationException, JadePersistenceException;
}
