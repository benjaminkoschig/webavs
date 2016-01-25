package ch.globaz.al.business.services.paiement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Collection;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.prestation.paiement.PaiementPrestationComplexSearchModel;
import ch.globaz.al.business.paiement.CompabilisationPrestationContainer;
import ch.globaz.al.business.paiement.PaiementBusinessModel;
import ch.globaz.al.business.paiement.PaiementContainer;

/**
 * Service permettant la gestion des paiements direct de prestations
 * 
 * @author jts
 */
public interface PaiementDirectService extends JadeApplicationService {

    /**
     * Annule la pr�paration des prestations/r�cap avant le paiement d�finitif des prestations
     * 
     * @param periodeA
     *            P�riode trait�e (format MM.AAAA)
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void annulerPreparationPaiementDirect(String periodeA) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Annule la comptabilisation des prestations/r�cap correspondant au num�ro de journal OSIRIS pass� en param�tre.
     * 
     * Note : ce traitement n'annule pas le journal en compta
     * 
     * @param idJournal
     *            id du journal pour lequel annuler la comptabilisation
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void annulerVersement(String idJournal) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue la v�rification des prestations. Elle v�rifie si le b�n�ficiaire a une adresse de paiement.
     * 
     * @param prestations
     *            Liste des prestations
     * @param date
     *            Date pour laquelle effectuer les v�rifications
     * @param logger
     *            Instance du logger dans lequel stocker les messages
     * 
     * @return logger contenant les messages d'erreurs
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public ProtocoleLogger checkPrestations(Collection<PaiementBusinessModel> prestations, String date,
            ProtocoleLogger logger) throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les prestations correspondant au num�ro de journal <code>noJournal</code>. Elles sont stock�es sous forme
     * d'instance de {@link ch.globaz.al.businessimpl.paiement.PaiementRecapitulatifBusinessModel}
     * 
     * @param idJournal
     *            num�ro de journal de la compta
     * @return Liste des r�caps r�cup�r�e (instances de <code>PaiementBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public PaiementContainer loadPrestationsComptabilisees(String idJournal) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * FIXME : mettre commentaire � jour Charge les prestations correspondant � la p�riode <code>periodeA</code> et
     * ayant l'�tat 'TR' . Elles sont stock�e sous forme d'instance de
     * {@link ch.globaz.al.businessimpl.paiement.PaiementBusinessModel}
     * 
     * @param periodeA
     *            P�riode trait�e (format MM.AAAA). Toutes les prestations ayant une fin de p�riode inf�rieur ou �gale
     *            au param�tre seront r�cup�r�es
     * @return Liste des r�caps r�cup�r�e (instances de <code>PaiementBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public PaiementPrestationComplexSearchModel loadPrestationsPreparees(String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    public PaiementPrestationComplexSearchModel loadPrestationsPrepareesByNumProcessus(String idProcessus)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les prestation correspondant � la p�riode <code>periodeA</code>. Les prestations sont regroup�es par
     * num�ro de rubrique comptable et leur montant sont additionn�s Elles sont stock�e sous forme d'instance de
     * {@link ch.globaz.al.businessimpl.paiement.PaiementRecapitulatifBusinessModel}
     * 
     * @param idJournal
     *            num�ro de journal de la compta
     * @return Liste des r�caps r�cup�r�e (instances de <code>PaiementRecapitulatifBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    // public Collection<PaiementRecapitulatifBusinessModel>
    // loadPrestationsRecapitualifComptabilisees(
    // String idJournal) throws JadeApplicationException,
    // JadePersistenceException;

    /**
     * Charge les prestation correspondant � la p�riode <code>periodeA</code>. Les prestations sont regroup�es par
     * num�ro de rubrique comptable et leur montant sont additionn�s Elles sont stock�e sous forme d'instance de
     * {@link ch.globaz.al.businessimpl.paiement.PaiementRecapitulatifBusinessModel}
     * 
     * @param periodeA
     *            P�riode trait�e (format MM.AAAA)
     * @return Liste des r�caps r�cup�r�e (instances de <code>PaiementRecapitulatifBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    // public Collection<PaiementRecapitulatifBusinessModel>
    // loadPrestationsRecapitualifSimulation(
    // String periodeA) throws JadeApplicationException,
    // JadePersistenceException;

    /**
     * Charge les prestation correspondant � la p�riode <code>periodeA</code>. Elles sont stock�e sous forme d'instance
     * de {@link ch.globaz.al.businessimpl.paiement.PaiementBusinessModel}.
     * 
     * @param periodeA
     *            P�riode trait�e (format MM.AAAA)
     * @return Liste des r�caps r�cup�r�e (instances de <code>PaiementBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public PaiementContainer loadPrestationsSimulation(String periodeA) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Charge les prestation correspondant au num�ro processus <code>idProcessus</code>. Elles sont stock�e sous forme
     * d'instance de {@link ch.globaz.al.businessimpl.paiement.PaiementBusinessModel}.
     * 
     * @param idProcessus
     *            num�ro du processus
     * @return Liste des r�caps r�cup�r�e (instances de <code>PaiementBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public PaiementContainer loadPrestationsSimulationByNumProcessus(String idProcessus)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Pr�pare les prestations/r�cap avant le paiement d�finitif des prestations
     * 
     * @param periodeA
     *            P�riode trait�e (format MM.AAAA)
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void preparerPaiementDirect(String periodeA) throws JadePersistenceException, JadeApplicationException;

    public void preparerPaiementDirectByNumProcessus(String idProcessus) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Effectue le versement des prestations concern�e par la p�riode pass�e en param�tre
     * 
     * @param periodeA
     *            P�riode trait�e (format MM.AAAA)
     * @param dateComptable
     *            Date comptable pour le journal
     * @param logger
     *            Logger dans lequel seront ajout� les �ventuelles erreurs
     * @return logger contenant les erreurs
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */

    public CompabilisationPrestationContainer verserPrestations(String periodeA, String dateComptable,
            ProtocoleLogger logger) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue le versement des prestations concern�e par le processus pass� en param�tre
     * 
     * @param periodeA
     *            id du processus li� au r�caps � verser
     * @param dateComptable
     *            Date comptable pour le journal
     * @param logger
     *            Logger dans lequel seront ajout� les �ventuelles erreurs
     * @return logger contenant les erreurs
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public CompabilisationPrestationContainer verserPrestationsByNumProcessus(String idProcessus, String dateComptable,
            ProtocoleLogger logger) throws JadeApplicationException, JadePersistenceException;

}
