package ch.globaz.al.business.services.models.periodeAF;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;

/**
 * Interface définissant les services métier implémentés liés à la période AF
 * 
 * @author GMO
 */
public interface PeriodeAFBusinessService extends JadeApplicationService {

    /**
     * Ferme la période correspondante à la date indiquée
     * 
     * @param datePeriode
     *            date voulue
     * @return PeriodeAFModel représentant la période fermée
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PeriodeAFModel closePeriode(String datePeriode) throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère la période suivante à celle passée en paramètre
     * 
     * @param datePeriode
     *            la période dont on veut la suivante
     * 
     * @return la periode AF précédente - elle sera crée (ouverte) si pas existante
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PeriodeAFModel getNextPeriode(String datePeriode) throws JadeApplicationException, JadePersistenceException;

    /**
     * 
     * @param datePeriode
     *            (MM.yyyy)
     * @return la période formattée de début de trimestre
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getPeriodeDebutTrimestre(String datePeriode) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Récupère la période AF à utiliser pour les traitements en cours
     * 
     * @param bonification
     *            défini si période des processus indirects ou directs
     * @param includePartiel
     *            indique si il faut tenir compte des processus partiels ouverts pour récupérer la période en cours
     * @return la periode AF en cours
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     *             Attention, actuellement toujours utilisée avec <code>includePartiel=true</code>, changement impacte
     *             toutes les périodes par défaut de génération unitaire (dossier, affilié, adi)
     * @deprecated utiliser getPeriodeEncours (String bonification, String typeCotisation, boolean includePartiel)
     */
    @Deprecated
    public PeriodeAFModel getPeriodeEnCours(String bonification, boolean includePartiel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère la période AF à utiliser pour les traitements en cours
     * 
     * @param bonification
     *            défini si période des processus indirects ou directs
     * 
     * @param includePartiel
     *            indique si il faut tenir compte des processus partiels ouverts pour récupérer la période en cours
     * @return la periode AF en cours
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     *             Attention, actuellement toujours utilisée avec <code>includePartiel=true</code>, changement impacte
     *             toutes les périodes par défaut de génération unitaire (dossier, affilié, adi)
     * 
     */

    public PeriodeAFModel getPeriodeEnCours(String bonification, String typeCotisation, boolean includePartiel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * 
     * @param datePeriode
     *            (MM.yyyy)
     * @return la période formattée de fin de trimestre
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getPeriodeFinTrimestre(String datePeriode) throws JadeApplicationException, JadePersistenceException;

/**
	 * Retourne sous quelle période générer pour un dossier en question selon sa périodicité et les processus principaux
	 * déjà effectués
	 * 
	 * @param periodicite
	 *            la périodicité liée au dossier
	 * @param bonification
	 *            défini si période des processus indirects ou directs
	 *              @param typeCotisation
	 *            défini si paritaire / personnel / groupé / direct {@link ALConstPrestations)
	 * @return la période sous laquelle devrait être générée la prestation
	 * @throws JadeApplicationException
	 *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
	 * @throws JadePersistenceException
	 *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
	 *             faire
	 */

    public String getPeriodeToGenerateForDossier(String periodicite, String bonification, String typeCotisation)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère la période précédente à celle passée en paramètre
     * 
     * @param datePeriode
     *            la période dont on veut la précédente
     * 
     * @return la periode AF précédente - elle sera crée (fermée) si pas existante
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     */

    public PeriodeAFModel getPreviousPeriode(String datePeriode) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Ouvre une période pour la date indiquée, si déjà ouverte, la période est retournée
     * 
     * @param datePeriode
     *            date voulue
     * @return PeriodeAFModel représentant la période ouverte
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PeriodeAFModel openPeriode(String datePeriode) throws JadeApplicationException, JadePersistenceException;
}
