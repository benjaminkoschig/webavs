package ch.globaz.al.businessimpl.generation.prestations.managers;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.log.JadeLogger;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.loggers.ProtocoleLogger;

/**
 * Handler gérant les producteurs/consommateurs de la génération globale
 * 
 * @author jts
 */
public class MainGenerationHandler {
    /**
     * Intervalle d'attente avant que le handler vérifie si le traitement est terminé
     */
    private static final long WAITING_TIME = 2000;

    /**
     * Retourne une nouvelle instance de <code>ImportationHandler</code>
     * 
     * @return Une nouvelle instance
     */
    public static MainGenerationHandler newInstance() {
        return new MainGenerationHandler();
    }

    /**
     * Indique si le producteur a terminé
     */
    private boolean affilieManagerFinished = false;
    /**
     * indique si le consommateur a terminé
     */
    private boolean generationManagerFinished = false;

    /**
     * Constructeur privé. Utiliser <code>newInstance</code> pour obtenir une instance
     * 
     * @see MainGenerationHandler#newInstance()
     */
    private MainGenerationHandler() {
        super();
    }

    /**
     * Initialise et exécute le gestionnaire de fichier (producteur) et le gestionnaire d'importation (consommateur)
     * 
     * @param periode
     *            Période à traiter (format MM.AAAA)
     * @param typeCot
     *            le type de cotisation à traiter {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @param numGeneration
     *            Numéro de génération. Généralement l'id du traitement ayant exécuté la génération
     * @param logger
     *            Instance du logger devant contenir les messages des erreur survenant pendant la génération
     * 
     * @throws JadeApplicationException
     *             Exception levée si la période indiquée n'est pas valide
     */
    public void handle(String periode, String typeCot, String numGeneration, ProtocoleLogger logger)
            throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALGenerationException("MainGenerationHandler#handle : " + periode + "is not a valid period");
        }

        String genAffilieUUID = JadeUUIDGenerator.createStringUUID();

        new Thread(new AffiliesManager(periode, typeCot, this, numGeneration, logger, JadeThread.currentContext()
                .getContext(), genAffilieUUID)).start();
        new Thread(new GenerationManager(this, logger, genAffilieUUID)).start();
        while (!affilieManagerFinished || !generationManagerFinished) {
            try {
                Thread.sleep(MainGenerationHandler.WAITING_TIME);
            } catch (InterruptedException e) {
                // DO NOTHING
            }
        }
    }

    /**
     * Utilisé par le consommateur ou le producteur pour notifier qu'il a terminé son traitement
     * 
     * @param abstractManager
     *            manager (AffilieManager ou GenerationManager)
     * @see AffiliesManager
     * @see GenerationManager
     */
    public void notifyFinished(AbstractManager abstractManager) {

        JadeLogger.info(this, "Notify finished : "
                + (abstractManager != null ? abstractManager.getClass().getName() : "Null value passed!"));

        if (abstractManager instanceof AffiliesManager) {
            affilieManagerFinished = true;
        } else if (abstractManager instanceof GenerationManager) {
            generationManagerFinished = true;
        }
    }
}