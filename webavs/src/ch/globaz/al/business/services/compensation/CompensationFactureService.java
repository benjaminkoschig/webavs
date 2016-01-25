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
     * Annule la pr�paration des prestations/r�cap. Elle passe l'�tat des prestations et des r�cap de TR � SA
     * 
     * @param periodeA
     *            P�riode trait�e (format MM.AAAA)
     * @param typeCoti
     *            type de cotisation trait�. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void annulerPreparation(String periodeA, String typeCoti) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Annule la pr�paration des prestations/r�cap. Elle passe l'�tat des prestations et des r�cap de TR � SA
     * 
     * @param idProcessus
     *            processus li� aux r�caps
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void annulerPreparationByNumProcessus(String idProcessus) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Pr�pare les r�caps pour l'impression des protocoles ou pour la compensation
     * 
     * @param search
     *            Mod�le de recherche. la recherche doit avoir �t� ex�cut�e
     * @param rubrSep
     *            Indique si les enregistrement dans la liste doivent �tre s�par� par rubrique comptable en plus des
     *            crit�res standard (affili� et genre d'assurance)
     * @return Liste des r�cap pr�par�es ( {@link ch.globaz.al.businessimpl.compensation.CompensationBusinessModel} )
     * @throws JadeApplicationException
     *             Exception lev�e si <code>search</code> est <code>null</code>
     */
    public Collection<CompensationBusinessModel> buildRecaps(CompensationPrestationFullComplexSearchModel search,
            boolean rubrSep) throws JadeApplicationException;

    /**
     * Effectue la v�rification des r�caps. Les affili�s sont contr�l�s et un avertissement est enregistr� s'il est
     * inactif pour la p�riode concern�e
     * 
     * @param recaps
     *            Liste des r�caps � v�rifier
     * @param logger
     *            Instance du logger dans lequel stocker les messages
     * @return logger contenant les messages d'erreurs
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public ProtocoleLogger checkRecaps(Collection<CompensationBusinessModel> recaps, ProtocoleLogger logger)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Charge les r�caps correspondant aux param�tres. Elles sont stock�e sous forme d'instance de
     * <code>CompensationBusinessModel</code>.
     * 
     * @param idPassage
     *            Num�ro de passage de la g�n�ration
     * @param activitesAlloc
     *            types d'activit�s des allocataires pour lesquels les prestations doivent �tre trait�es. Si ce
     *            param�tre est <code>null</code> ou vide, toutes les prestations seront retourn�es. Les valeurs
     *            contenue dans la collection doivent correspondre aux code syst�me du groupe
     *            {@link ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC}
     * @param rubrSep
     *            Indique si les enregistrement dans la liste doivent �tre s�par� par rubrique comptable en plus des
     *            crit�res standard (affili� et genre d'assurance)
     * @return Liste des r�caps r�cup�r�e (instances de
     *         {@link ch.globaz.al.businessimpl.compensation.CompensationBusinessModel} )
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public Collection<CompensationBusinessModel> loadRecapsDefinitif(String idPassage,
            Collection<String> activitesAlloc, boolean rubrSep) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Charge les r�caps correspondant aux param�tres et ayant l'�tat SA. Elles sont stock�e sous forme d'instance de
     * <code>CompensationBusinessModel</code>
     * 
     * @param periodeA
     *            P�riode trait�e (format MM.AAAA)
     * @param activitesAlloc
     *            types d'activit�s des allocataires pour lesquels les prestations doivent �tre trait�es. Si ce
     *            param�tre est <code>null</code> ou vide, toutes les prestations seront retourn�es. Les valeurs
     *            contenue dans la collection doivent correspondre aux code syst�me du groupe
     *            {@link ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC}
     * @param rubrSep
     *            Indique si les enregistrement dans la liste doivent �tre s�par� par rubrique comptable en plus des
     *            crit�res standard (affili� et genre d'assurance)
     * @return Liste des r�caps r�cup�r�e (instances de
     *         {@link ch.globaz.al.businessimpl.compensation.CompensationBusinessModel} )
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public Collection<CompensationBusinessModel> loadRecapsSimulation(String periodeA,
            Collection<String> activitesAlloc, boolean rubrSep) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Charge les r�caps correspondant aux param�tres et ayant l'�tat SA. Elles sont stock�e sous forme d'instance de
     * <code>CompensationBusinessModel</code>
     * 
     * @param idProcessus
     *            processus li� aux r�caps
     * 
     * @param activitesAlloc
     *            types d'activit�s des allocataires pour lesquels les prestations doivent �tre trait�es. Si ce
     *            param�tre est <code>null</code> ou vide, toutes les prestations seront retourn�es. Les valeurs
     *            contenue dans la collection doivent correspondre aux code syst�me du groupe
     *            {@link ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC}
     * @param rubrSep
     *            Indique si les enregistrement dans la liste doivent �tre s�par� par rubrique comptable en plus des
     *            crit�res standard (affili� et genre d'assurance)
     * @return Liste des r�caps r�cup�r�e (instances de
     *         {@link ch.globaz.al.businessimpl.compensation.CompensationBusinessModel} )
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public Collection<CompensationBusinessModel> loadRecapsSimulationByNumProcessus(String idProcessus,
            Collection<String> activitesAlloc, boolean rubrSep) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Charge les rubriques correspondant aux param�tres. Elles sont stock�e sous forme d'instance de
     * <code>CompensationBusinessModel</code>.
     * 
     * @param idPassage
     *            num�ro de passage de la facturation
     * @return Liste des r�caps r�cup�r�e (instances de
     *         {@link ch.globaz.al.businessimpl.TucanaTransfertBusinessModel.CompensationRecapitulatifBusinessModel} )
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public Collection<CompensationRecapitulatifBusinessModel> loadRubriquesDefinitif(String idPassage)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les rubriques correspondant aux param�tres. Elles sont stock�e sous forme d'instance de
     * <code>CompensationRecapitulatifBusinessModel</code>.
     * 
     * @param periodeA
     *            P�riode trait�e (format MM.AAAA)
     * @param typeCoti
     *            type de cotisation trait�. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @return Liste des r�caps r�cup�r�e (instances de
     *         {@link ch.globaz.al.businessimpl.TucanaTransfertBusinessModel.CompensationRecapitulatifBusinessModel} )
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public Collection<CompensationRecapitulatifBusinessModel> loadRubriquesSimulation(String periodeA, String typeCoti)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les rubriques correspondant au param�tre. Elles sont stock�e sous forme d'instance de
     * <code>CompensationRecapitulatifBusinessModel</code>.
     * 
     * @param idProcessus
     *            processus li� aux r�caps
     * @return Liste des r�caps r�cup�r�e (instances de
     *         {@link ch.globaz.al.businessimpl.TucanaTransfertBusinessModel.CompensationRecapitulatifBusinessModel} )
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public Collection<CompensationRecapitulatifBusinessModel> loadRubriquesSimulationByNumProcessus(String idProcessus)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Pr�pare les prestations/r�cap avant la compensation d�finitive. Elle passe l'�tat des prestations et des r�cap de
     * SA � TR
     * 
     * @param periodeA
     *            P�riode trait�e (format MM.AAAA)
     * @param typeCoti
     *            type de cotisation trait�. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void preparerCompensation(String periodeA, String typeCoti) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Pr�pare les prestations/r�cap avant la compensation d�finitive. Elle passe l'�tat des prestations et des r�cap de
     * SA � TR
     * 
     * @param idProcessus
     *            processus li� aux r�caps
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void preparerCompensationByNumProcessus(String idProcessus) throws JadeApplicationException,
            JadePersistenceException;
}
