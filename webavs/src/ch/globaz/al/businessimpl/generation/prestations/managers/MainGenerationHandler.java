package ch.globaz.al.businessimpl.generation.prestations.managers;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.log.JadeLogger;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.loggers.ProtocoleLogger;

/**
 * Handler g�rant les producteurs/consommateurs de la g�n�ration globale
 * 
 * @author jts
 */
public class MainGenerationHandler {
    /**
     * Intervalle d'attente avant que le handler v�rifie si le traitement est termin�
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
     * Indique si le producteur a termin�
     */
    private boolean affilieManagerFinished = false;
    /**
     * indique si le consommateur a termin�
     */
    private boolean generationManagerFinished = false;

    /**
     * Constructeur priv�. Utiliser <code>newInstance</code> pour obtenir une instance
     * 
     * @see MainGenerationHandler#newInstance()
     */
    private MainGenerationHandler() {
        super();
    }

    /**
     * Initialise et ex�cute le gestionnaire de fichier (producteur) et le gestionnaire d'importation (consommateur)
     * 
     * @param periode
     *            P�riode � traiter (format MM.AAAA)
     * @param typeCot
     *            le type de cotisation � traiter {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @param numGeneration
     *            Num�ro de g�n�ration. G�n�ralement l'id du traitement ayant ex�cut� la g�n�ration
     * @param logger
     *            Instance du logger devant contenir les messages des erreur survenant pendant la g�n�ration
     * 
     * @throws JadeApplicationException
     *             Exception lev�e si la p�riode indiqu�e n'est pas valide
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
     * Utilis� par le consommateur ou le producteur pour notifier qu'il a termin� son traitement
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