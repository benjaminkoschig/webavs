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
     * Annule la préparation des prestations/récap avant le paiement définitif des prestations
     * 
     * @param periodeA
     *            Période traitée (format MM.AAAA)
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void annulerPreparationPaiementDirect(String periodeA) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Annule la comptabilisation des prestations/récap correspondant au numéro de journal OSIRIS passé en paramètre.
     * 
     * Note : ce traitement n'annule pas le journal en compta
     * 
     * @param idJournal
     *            id du journal pour lequel annuler la comptabilisation
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void annulerVersement(String idJournal) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue la vérification des prestations. Elle vérifie si le bénéficiaire a une adresse de paiement.
     * 
     * @param prestations
     *            Liste des prestations
     * @param date
     *            Date pour laquelle effectuer les vérifications
     * @param logger
     *            Instance du logger dans lequel stocker les messages
     * 
     * @return logger contenant les messages d'erreurs
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public ProtocoleLogger checkPrestations(Collection<PaiementBusinessModel> prestations, String date,
            ProtocoleLogger logger) throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les prestations correspondant au numéro de journal <code>noJournal</code>. Elles sont stockées sous forme
     * d'instance de {@link ch.globaz.al.businessimpl.paiement.PaiementRecapitulatifBusinessModel}
     * 
     * @param idJournal
     *            numéro de journal de la compta
     * @return Liste des récaps récupérée (instances de <code>PaiementBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public PaiementContainer loadPrestationsComptabilisees(String idJournal) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * FIXME : mettre commentaire à jour Charge les prestations correspondant à la période <code>periodeA</code> et
     * ayant l'état 'TR' . Elles sont stockée sous forme d'instance de
     * {@link ch.globaz.al.businessimpl.paiement.PaiementBusinessModel}
     * 
     * @param periodeA
     *            Période traitée (format MM.AAAA). Toutes les prestations ayant une fin de période inférieur ou égale
     *            au paramètre seront récupérées
     * @return Liste des récaps récupérée (instances de <code>PaiementBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public PaiementPrestationComplexSearchModel loadPrestationsPreparees(String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    public PaiementPrestationComplexSearchModel loadPrestationsPrepareesByNumProcessus(String idProcessus)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les prestation correspondant à la période <code>periodeA</code>. Les prestations sont regroupées par
     * numéro de rubrique comptable et leur montant sont additionnés Elles sont stockée sous forme d'instance de
     * {@link ch.globaz.al.businessimpl.paiement.PaiementRecapitulatifBusinessModel}
     * 
     * @param idJournal
     *            numéro de journal de la compta
     * @return Liste des récaps récupérée (instances de <code>PaiementRecapitulatifBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    // public Collection<PaiementRecapitulatifBusinessModel>
    // loadPrestationsRecapitualifComptabilisees(
    // String idJournal) throws JadeApplicationException,
    // JadePersistenceException;

    /**
     * Charge les prestation correspondant à la période <code>periodeA</code>. Les prestations sont regroupées par
     * numéro de rubrique comptable et leur montant sont additionnés Elles sont stockée sous forme d'instance de
     * {@link ch.globaz.al.businessimpl.paiement.PaiementRecapitulatifBusinessModel}
     * 
     * @param periodeA
     *            Période traitée (format MM.AAAA)
     * @return Liste des récaps récupérée (instances de <code>PaiementRecapitulatifBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    // public Collection<PaiementRecapitulatifBusinessModel>
    // loadPrestationsRecapitualifSimulation(
    // String periodeA) throws JadeApplicationException,
    // JadePersistenceException;

    /**
     * Charge les prestation correspondant à la période <code>periodeA</code>. Elles sont stockée sous forme d'instance
     * de {@link ch.globaz.al.businessimpl.paiement.PaiementBusinessModel}.
     * 
     * @param periodeA
     *            Période traitée (format MM.AAAA)
     * @return Liste des récaps récupérée (instances de <code>PaiementBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public PaiementContainer loadPrestationsSimulation(String periodeA) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Charge les prestation correspondant au numéro processus <code>idProcessus</code>. Elles sont stockée sous forme
     * d'instance de {@link ch.globaz.al.businessimpl.paiement.PaiementBusinessModel}.
     * 
     * @param idProcessus
     *            numéro du processus
     * @return Liste des récaps récupérée (instances de <code>PaiementBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public PaiementContainer loadPrestationsSimulationByNumProcessus(String idProcessus)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Prépare les prestations/récap avant le paiement définitif des prestations
     * 
     * @param periodeA
     *            Période traitée (format MM.AAAA)
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void preparerPaiementDirect(String periodeA) throws JadePersistenceException, JadeApplicationException;

    public void preparerPaiementDirectByNumProcessus(String idProcessus) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Effectue le versement des prestations concernée par la période passée en paramètre
     * 
     * @param periodeA
     *            Période traitée (format MM.AAAA)
     * @param dateComptable
     *            Date comptable pour le journal
     * @param logger
     *            Logger dans lequel seront ajouté les éventuelles erreurs
     * @return logger contenant les erreurs
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */

    public CompabilisationPrestationContainer verserPrestations(String periodeA, String dateComptable,
            ProtocoleLogger logger) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue le versement des prestations concernée par le processus passé en paramètre
     * 
     * @param periodeA
     *            id du processus lié au récaps à verser
     * @param dateComptable
     *            Date comptable pour le journal
     * @param logger
     *            Logger dans lequel seront ajouté les éventuelles erreurs
     * @return logger contenant les erreurs
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public CompabilisationPrestationContainer verserPrestationsByNumProcessus(String idProcessus, String dateComptable,
            ProtocoleLogger logger) throws JadeApplicationException, JadePersistenceException;

}
