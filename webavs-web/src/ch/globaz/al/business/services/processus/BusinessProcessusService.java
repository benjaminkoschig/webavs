package ch.globaz.al.business.services.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import java.util.List;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;

/**
 * Services permettant la gestion des processus m�tier
 * 
 * @author GMO
 * 
 */
public interface BusinessProcessusService extends JadeApplicationService {

    /**
     * Annule un traitement p�riodique
     * 
     * @param idProcessusPeriodique
     *            id du processus p�riodique conteneur
     * @param idTraitementPeriodique
     *            id du traitement p�riodique � annuler
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void annulerTraitementPeriodique(String idProcessusPeriodique, String idTraitementPeriodique)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime processus partiel pour autant qu'il ne soit pas d�j� ferm�
     * 
     * @param idProcessusPartiel
     *            id du processus � supprimer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void deleteProcessusPartiel(String idProcessusPartiel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Ex�cute un traitement p�riodique
     * 
     * @param idProcessusPeriodique
     *            id du processus p�riodique conteneur
     * @param idTraitementPeriodique
     *            id du traitement p�riodique � ex�cuter
     * @return JadePrintDocumentContainer l'�ventuel protocole
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public HashMap<ALConstProtocoles.TypeProtocole, Object> executeTraitementPeriodique(String idProcessusPeriodique,
            String idTraitementPeriodique) throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne le template appliqu� pour la configuration des processus (selon param�tre en DB)
     * 
     * @param periode
     *            mm.YYYY - periode � laquelle on veut le template qui �tait appliqu�
     * @return CS repr�sentant le template
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getAppliedTemplate(String periode) throws JadeApplicationException, JadePersistenceException;

    /**
     * D�termine � quel processus il faut lier la r�cap pour un n� de facture donn�es
     * 
     * @param numFacture
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getNumProcessusForNumFacture(String numFacture) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retourne une instance du processus p�riodique du processus sp�cifi� pour une p�riode donn�e
     * 
     * @param CSProcessus
     *            - le processus � obtenir
     * @param periode
     *            - la p�riode pour laquelle on veut le processusPeriodique
     * @return instance du ProcessusPeriodique (ProcessusPeriodique)
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public List<ProcessusPeriodiqueModel> getTheProcessusForPeriode(String CSProcessus, String periode)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne la liste des processus non verrouill� (c'est-�-dire dont le traitement pr�paration n'a pas d�j� �t�
     * ex�cut�, donc disponible pour accueillir de prestations suppl�mentaires).
     * 
     * @param bonification
     *            - pour savoir quel type de processus chercher (direct ou compensation)
     * @param typeCoti
     *            - pour savoir les processus compatibles avec le type de coti ( en cas de processus pers. et par)
     * @return la liste des processus ouverts
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public List<ProcessusPeriodiqueModel> getUnlockProcessusPaiementForPeriode(String bonification, String typeCoti)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise tous les processus m�tier en fonction de la configuration li� au template de la caisse Attention,
     * aucun contr�le si les processus sont d�j� cr��s ou pas
     * 
     * @param CSTemplate
     *            Le code syst�me du template � appliquer
     * @param datePeriode
     *            La p�riode pour laquelle initialiser les processus
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     */
    public void initAllBusinessProcessusForPeriode(String CSTemplate, String datePeriode)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un processus m�tier pour une p�riode donn�e en fonction du template et du processus m�tier
     * 
     * @param CSTemplate
     *            - le template appliqu�
     * @param CSBusinessProcessus
     *            - le processus m�tier � initialiser
     * @param datePeriode
     *            - la p�riode pour laquelle initialiser le processus
     * @return indique si les processus / traitements ont bien �t� cr��s
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean initBusinessProcessusForPeriode(String CSTemplate, String CSBusinessProcessus, String datePeriode,
            boolean isProcessusPartiel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Indique si il s'agit d'un template (configuration des processus) proposant des processus facturation s�par�e
     * (par/pers) ou group�e
     * 
     * @param csTemplate
     *            - code syst�me ALCSProcessus.GROUP_NAME_TEMPLATE_CONFIG
     * @return true si facturation s�par�e, false sinon
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isFacturationSeparee(String csTemplate) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime tout l'historique pour un traitement p�riodique donn�.
     * 
     * @param idTraitementPeriodique
     *            - le traitement dont on veut supprimer l'historique
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void removeHistoriqueTraitement(String idTraitementPeriodique) throws JadeApplicationException,
            JadePersistenceException;

}
