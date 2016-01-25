package ch.globaz.al.business.services.compensation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Collection;
import ch.globaz.al.business.compensation.CompensationBusinessModel;
import ch.globaz.al.business.compensation.CompensationRecapitulatifBusinessModel;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.prestation.paiement.CompensationPrestationFullComplexSearchModel;

/**
 * Service permettant la gestion des compensations sur facture
 * 
 * @author jts
 */
public interface CompensationFactureService extends JadeApplicationService {

    /**
     * Annule la préparation des prestations/récap. Elle passe l'état des prestations et des récap de TR à SA
     * 
     * @param periodeA
     *            Période traitée (format MM.AAAA)
     * @param typeCoti
     *            type de cotisation traité. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void annulerPreparation(String periodeA, String typeCoti) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Annule la préparation des prestations/récap. Elle passe l'état des prestations et des récap de TR à SA
     * 
     * @param idProcessus
     *            processus lié aux récaps
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void annulerPreparationByNumProcessus(String idProcessus) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Prépare les récaps pour l'impression des protocoles ou pour la compensation
     * 
     * @param search
     *            Modèle de recherche. la recherche doit avoir été exécutée
     * @param rubrSep
     *            Indique si les enregistrement dans la liste doivent être séparé par rubrique comptable en plus des
     *            critères standard (affilié et genre d'assurance)
     * @return Liste des récap préparées ( {@link ch.globaz.al.businessimpl.compensation.CompensationBusinessModel} )
     * @throws JadeApplicationException
     *             Exception levée si <code>search</code> est <code>null</code>
     */
    public Collection<CompensationBusinessModel> buildRecaps(CompensationPrestationFullComplexSearchModel search,
            boolean rubrSep) throws JadeApplicationException;

    /**
     * Effectue la vérification des récaps. Les affiliés sont contrôlés et un avertissement est enregistré s'il est
     * inactif pour la période concernée
     * 
     * @param recaps
     *            Liste des récaps à vérifier
     * @param logger
     *            Instance du logger dans lequel stocker les messages
     * @return logger contenant les messages d'erreurs
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public ProtocoleLogger checkRecaps(Collection<CompensationBusinessModel> recaps, ProtocoleLogger logger)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Charge les récaps correspondant aux paramètres. Elles sont stockée sous forme d'instance de
     * <code>CompensationBusinessModel</code>.
     * 
     * @param idPassage
     *            Numéro de passage de la génération
     * @param activitesAlloc
     *            types d'activités des allocataires pour lesquels les prestations doivent être traitées. Si ce
     *            paramètre est <code>null</code> ou vide, toutes les prestations seront retournées. Les valeurs
     *            contenue dans la collection doivent correspondre aux code système du groupe
     *            {@link ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC}
     * @param rubrSep
     *            Indique si les enregistrement dans la liste doivent être séparé par rubrique comptable en plus des
     *            critères standard (affilié et genre d'assurance)
     * @return Liste des récaps récupérée (instances de
     *         {@link ch.globaz.al.businessimpl.compensation.CompensationBusinessModel} )
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public Collection<CompensationBusinessModel> loadRecapsDefinitif(String idPassage,
            Collection<String> activitesAlloc, boolean rubrSep) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Charge les récaps correspondant aux paramètres et ayant l'état SA. Elles sont stockée sous forme d'instance de
     * <code>CompensationBusinessModel</code>
     * 
     * @param periodeA
     *            Période traitée (format MM.AAAA)
     * @param activitesAlloc
     *            types d'activités des allocataires pour lesquels les prestations doivent être traitées. Si ce
     *            paramètre est <code>null</code> ou vide, toutes les prestations seront retournées. Les valeurs
     *            contenue dans la collection doivent correspondre aux code système du groupe
     *            {@link ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC}
     * @param rubrSep
     *            Indique si les enregistrement dans la liste doivent être séparé par rubrique comptable en plus des
     *            critères standard (affilié et genre d'assurance)
     * @return Liste des récaps récupérée (instances de
     *         {@link ch.globaz.al.businessimpl.compensation.CompensationBusinessModel} )
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public Collection<CompensationBusinessModel> loadRecapsSimulation(String periodeA,
            Collection<String> activitesAlloc, boolean rubrSep) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Charge les récaps correspondant aux paramètres et ayant l'état SA. Elles sont stockée sous forme d'instance de
     * <code>CompensationBusinessModel</code>
     * 
     * @param idProcessus
     *            processus lié aux récaps
     * 
     * @param activitesAlloc
     *            types d'activités des allocataires pour lesquels les prestations doivent être traitées. Si ce
     *            paramètre est <code>null</code> ou vide, toutes les prestations seront retournées. Les valeurs
     *            contenue dans la collection doivent correspondre aux code système du groupe
     *            {@link ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC}
     * @param rubrSep
     *            Indique si les enregistrement dans la liste doivent être séparé par rubrique comptable en plus des
     *            critères standard (affilié et genre d'assurance)
     * @return Liste des récaps récupérée (instances de
     *         {@link ch.globaz.al.businessimpl.compensation.CompensationBusinessModel} )
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public Collection<CompensationBusinessModel> loadRecapsSimulationByNumProcessus(String idProcessus,
            Collection<String> activitesAlloc, boolean rubrSep) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Charge les rubriques correspondant aux paramètres. Elles sont stockée sous forme d'instance de
     * <code>CompensationBusinessModel</code>.
     * 
     * @param idPassage
     *            numéro de passage de la facturation
     * @return Liste des récaps récupérée (instances de
     *         {@link ch.globaz.al.businessimpl.TucanaTransfertBusinessModel.CompensationRecapitulatifBusinessModel} )
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public Collection<CompensationRecapitulatifBusinessModel> loadRubriquesDefinitif(String idPassage)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les rubriques correspondant aux paramètres. Elles sont stockée sous forme d'instance de
     * <code>CompensationRecapitulatifBusinessModel</code>.
     * 
     * @param periodeA
     *            Période traitée (format MM.AAAA)
     * @param typeCoti
     *            type de cotisation traité. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @return Liste des récaps récupérée (instances de
     *         {@link ch.globaz.al.businessimpl.TucanaTransfertBusinessModel.CompensationRecapitulatifBusinessModel} )
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public Collection<CompensationRecapitulatifBusinessModel> loadRubriquesSimulation(String periodeA, String typeCoti)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les rubriques correspondant au paramètre. Elles sont stockée sous forme d'instance de
     * <code>CompensationRecapitulatifBusinessModel</code>.
     * 
     * @param idProcessus
     *            processus lié aux récaps
     * @return Liste des récaps récupérée (instances de
     *         {@link ch.globaz.al.businessimpl.TucanaTransfertBusinessModel.CompensationRecapitulatifBusinessModel} )
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public Collection<CompensationRecapitulatifBusinessModel> loadRubriquesSimulationByNumProcessus(String idProcessus)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Prépare les prestations/récap avant la compensation définitive. Elle passe l'état des prestations et des récap de
     * SA à TR
     * 
     * @param periodeA
     *            Période traitée (format MM.AAAA)
     * @param typeCoti
     *            type de cotisation traité. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void preparerCompensation(String periodeA, String typeCoti) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Prépare les prestations/récap avant la compensation définitive. Elle passe l'état des prestations et des récap de
     * SA à TR
     * 
     * @param idProcessus
     *            processus lié aux récaps
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void preparerCompensationByNumProcessus(String idProcessus) throws JadeApplicationException,
            JadePersistenceException;
}
