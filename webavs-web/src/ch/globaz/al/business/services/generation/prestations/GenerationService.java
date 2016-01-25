package ch.globaz.al.business.services.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashSet;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.AffilieListComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextGlobal;

/**
 * Service de génération de prestations
 * 
 * @author jts
 * 
 */
public interface GenerationService extends JadeApplicationService {

    /**
     * Génère les prestations selon les données contenues dans le contexte passé en paramètre
     * 
     * @param context
     *            Contexte global contenant le contexte d'affilié et la liste des dossiers à traiter
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void generationAffilie(ContextGlobal context) throws JadeApplicationException;

    /**
     * Génère les prestations des dossiers liés à l'affilié <code>numAffilie</code> pour une période d'un mois
     * 
     * @param numAffilie
     *            Numéro de l'affilié pour lequel les prestations doivent être générée
     * @param periode
     *            Période (mois) à générer sous la forme MM.AAAA
     * @param numGeneration
     *            Numéro de génération. Généralement l'id du traitement ayant exécuté la génération
     * @param logger
     *            Instance du logger devant contenir les messages des erreur survenant pendant la génération
     * @return logger contenant les messages d'erreur
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ProtocoleLogger generationAffilie(String numAffilie, String periode, String numGeneration,
            ProtocoleLogger logger) throws JadeApplicationException, JadePersistenceException;

    /**
     * Génère les prestations pour la totalité des dossiers (type cot. pers.) pour une période d'un mois.
     * 
     * @param periode
     *            Période (mois) à générer sous la forme MM.AAAA
     * @param type
     *            Type de cotisation/génération
     * @param numGeneration
     *            Numéro de génération. Généralement l'id du traitement ayant exécuté la génération
     * @param logger
     *            Instance du logger devant contenir les messages des erreur survenant pendant la génération
     * @return logger contenant les messages d'erreur
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ProtocoleLogger generationGlobale(String periode, String type, String numGeneration, ProtocoleLogger logger)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Récupère la liste des périodicité a prendre en compte lors d'une génération globale. Les périodicités mensuelles
     * sont traitées tous les mois. Les périodicités trimestrielles sont traités les mois 3, 6, 9 et 12. Les
     * périodicités annuelles sont traitées le mois 12
     * 
     * @param periode
     *            période à générer
     * @return liste des codes système correspondant aux périodicités à traiter
     * 
     * @throws JadeApplicationException
     *             Exception levée si la période indiquée n'est pas valide
     */
    public HashSet<String> getPeriodicites(String periode) throws JadeApplicationException;

    /**
     * Initialise le modèle de recherche des affiliés à traiter
     * 
     * @param debutPeriode
     *            début de la période à générer (MM.AAAA).
     * @param typeCoti
     *            Type(s) de cotisations à traiter ({@link ch.globaz.al.business.constantes.ALConstPrestations})
     * 
     * @return le modèle initialisé (non exécuté)
     * @throws JadeApplicationException
     *             Exception levée si la période passée au manager par le constructeur n'est pas valide ou si le type de
     *             cotisation n'est pas valide
     * @throws JadePersistenceException
     */
    public AffilieListComplexSearchModel initSearchAffilies(String periode, String typeCot)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise le modèle de recherche des dossiers à traité
     * 
     * @param debutPeriode
     *            début de la période à générer (MM.AAAA). Elle permet de gérer les cas de dossier radié
     * @param typeCoti
     *            Type(s) de cotisations à traiter ({@link ch.globaz.al.business.constantes.ALConstPrestations}). Si le
     *            paramètre est <code>null</code>, tous les types de dossiers seront traités
     * 
     * @return le modèle initialisé (non exécuté)
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètres n'est pas valide
     * @throws JadePersistenceException
     */
    public DossierComplexSearchModel initSearchDossiers(String debutPeriode, String typeCoti)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * 
     * @param idDossier
     *            identifiant du dossier
     * @param idRecap
     *            identifaint de la récap
     * @param periodeValidite
     * @return le montant total des prestation
     * @throws JadeApplicationException
     *             Exception levée si la période passée au manager par le constructeur n'est pas valide ou si le type de
     *             cotisation n'est pas valide
     * @throws JadePersistenceException
     */
    public String totalMontantPrestaGenereDossierPeriode(String idDossier, String idRecap, String periodeValidite)
            throws JadeApplicationException, JadePersistenceException;
}
