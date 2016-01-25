package ch.globaz.al.business.services.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import java.util.List;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;

/**
 * Services permettant la gestion des processus métier
 * 
 * @author GMO
 * 
 */
public interface BusinessProcessusService extends JadeApplicationService {

    /**
     * Annule un traitement périodique
     * 
     * @param idProcessusPeriodique
     *            id du processus périodique conteneur
     * @param idTraitementPeriodique
     *            id du traitement périodique à annuler
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void annulerTraitementPeriodique(String idProcessusPeriodique, String idTraitementPeriodique)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime processus partiel pour autant qu'il ne soit pas déjà fermé
     * 
     * @param idProcessusPartiel
     *            id du processus à supprimer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void deleteProcessusPartiel(String idProcessusPartiel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Exécute un traitement périodique
     * 
     * @param idProcessusPeriodique
     *            id du processus périodique conteneur
     * @param idTraitementPeriodique
     *            id du traitement périodique à exécuter
     * @return JadePrintDocumentContainer l'éventuel protocole
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public HashMap<ALConstProtocoles.TypeProtocole, Object> executeTraitementPeriodique(String idProcessusPeriodique,
            String idTraitementPeriodique) throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne le template appliqué pour la configuration des processus (selon paramètre en DB)
     * 
     * @param periode
     *            mm.YYYY - periode à laquelle on veut le template qui était appliqué
     * @return CS représentant le template
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getAppliedTemplate(String periode) throws JadeApplicationException, JadePersistenceException;

    /**
     * Détermine à quel processus il faut lier la récap pour un n° de facture données
     * 
     * @param numFacture
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getNumProcessusForNumFacture(String numFacture) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retourne une instance du processus périodique du processus spécifié pour une période donnée
     * 
     * @param CSProcessus
     *            - le processus à obtenir
     * @param periode
     *            - la période pour laquelle on veut le processusPeriodique
     * @return instance du ProcessusPeriodique (ProcessusPeriodique)
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public List<ProcessusPeriodiqueModel> getTheProcessusForPeriode(String CSProcessus, String periode)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne la liste des processus non verrouillé (c'est-à-dire dont le traitement préparation n'a pas déjà été
     * exécuté, donc disponible pour accueillir de prestations supplémentaires).
     * 
     * @param bonification
     *            - pour savoir quel type de processus chercher (direct ou compensation)
     * @param typeCoti
     *            - pour savoir les processus compatibles avec le type de coti ( en cas de processus pers. et par)
     * @return la liste des processus ouverts
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public List<ProcessusPeriodiqueModel> getUnlockProcessusPaiementForPeriode(String bonification, String typeCoti)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise tous les processus métier en fonction de la configuration lié au template de la caisse Attention,
     * aucun contrôle si les processus sont déjà créés ou pas
     * 
     * @param CSTemplate
     *            Le code système du template à appliquer
     * @param datePeriode
     *            La période pour laquelle initialiser les processus
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     */
    public void initAllBusinessProcessusForPeriode(String CSTemplate, String datePeriode)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un processus métier pour une période donnée en fonction du template et du processus métier
     * 
     * @param CSTemplate
     *            - le template appliqué
     * @param CSBusinessProcessus
     *            - le processus métier à initialiser
     * @param datePeriode
     *            - la période pour laquelle initialiser le processus
     * @return indique si les processus / traitements ont bien été créés
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean initBusinessProcessusForPeriode(String CSTemplate, String CSBusinessProcessus, String datePeriode,
            boolean isProcessusPartiel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Indique si il s'agit d'un template (configuration des processus) proposant des processus facturation séparée
     * (par/pers) ou groupée
     * 
     * @param csTemplate
     *            - code système ALCSProcessus.GROUP_NAME_TEMPLATE_CONFIG
     * @return true si facturation séparée, false sinon
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isFacturationSeparee(String csTemplate) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime tout l'historique pour un traitement périodique donné.
     * 
     * @param idTraitementPeriodique
     *            - le traitement dont on veut supprimer l'historique
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void removeHistoriqueTraitement(String idTraitementPeriodique) throws JadeApplicationException,
            JadePersistenceException;

}
