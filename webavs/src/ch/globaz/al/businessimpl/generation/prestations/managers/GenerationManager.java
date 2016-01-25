package ch.globaz.al.businessimpl.generation.prestations.managers;

import globaz.globall.api.GlobazSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextPrestation;
import ch.globaz.al.web.application.ALApplication;

/**
 * Manager de génération. Il se charge de gérer l'exécution des threads de génération
 * 
 * @author jts
 * 
 */
public class GenerationManager extends AbstractManager {

    /**
     * verrou
     */
    private static final Object token = new Object();
    /**
     * Identifiant unique de la génération
     * 
     * @see AffiliesManager
     */
    private String genAffilieUUID;
    /**
     * Instance du logger devant contenir les messages des erreur survenant pendant la génération
     */
    private ProtocoleLogger logger;
    /**
     * Liste des threads en exécution
     */
    private ArrayList<AffilieHandler> runningThreads = null;

    /**
     * Constructeur
     * 
     * @param generationHandler
     *            handler maître
     * @param logger
     *            Instance du logger devant contenir les messages des erreur survenant pendant la génération
     * @param genAffilieUUID
     *            Identifiant unique de la génération
     */
    protected GenerationManager(MainGenerationHandler generationHandler, ProtocoleLogger logger, String genAffilieUUID) {
        super(generationHandler);
        runningThreads = new ArrayList<AffilieHandler>();
        this.genAffilieUUID = genAffilieUUID;
        this.logger = logger;
    }

    /**
     * Lance l'exécution de l'importation des dossiers dès que des éléments se trouvent dans le conteneur et que le
     * producteur n'a pas terminé de traiter la totalité des fichiers
     */
    @Override
    protected void handle() {
        ContainerAffilies container = ContainerAffilies.getInstance(genAffilieUUID);

        while (!container.isAbleToConsume() && !container.isFinishedProducing()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // DO NOTHING
            }
        }

        if (container.isAbleToConsume()) {
            launch(container);
        }
    }

    /**
     * Exécute l'importation des dossiers. Le traitement se termine quand il n'y a plus de dossier à importer.
     * 
     * La méthode fait appel à <code>ImportationDossierHandler</code> et exécute plusieurs thread simultanément.
     * 
     * @param container
     *            Conteneur
     */
    private void launch(ContainerAffilies container) {

        int maxThreads = getMaxThreads();

        while (container.isAbleToConsume() || !container.isFinishedProducing() || (runningThreads.size() > 0)) {

            // s'il n'y a rien à consommer mais que le producteur n'a pas encore
            // terminé
            while (!container.isAbleToConsume() && !container.isFinishedProducing()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // DO NOTHING
                }
            }

            // exécution de thread jusqu'à atteindre le max et tant qu'il reste
            // des dossiers (affiliés) à traiter
            while (container.isAbleToConsume() && (runningThreads.size() < maxThreads)) {
                AffilieHandler handler = new AffilieHandler(container.getDossiers(), this);

                synchronized (GenerationManager.token) {
                    runningThreads.add(handler);
                    new Thread(handler).start();
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // DO NOTHING
            }
        }
    }

    /**
     * Utilisé par <code>ImportationDossierHandler</code> pour notifier la fin de traitement d'un dossier
     * 
     * @param importationDossierHandler
     *            Handler ayant envoyant la notification
     */
    public void notifyFinished(AffilieHandler importationDossierHandler) {
        synchronized (GenerationManager.token) {

            if (runningThreads.contains(importationDossierHandler)) {
                runningThreads.remove(importationDossierHandler);
            } else {
                StringBuffer message = new StringBuffer("Classe : GenerationManager#notifyFinished\n");
                message.append("Tried to remove ").append(importationDossierHandler.toString()).append(" from list");
                logger.addFatalError(new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, ContextPrestation.class
                        .getName(), message.toString()));
            }
        }
    }

    /**
     * Récupère le nombre de thread pouvant être exécutés simultanément.
     * 
     * Si le paramètre ne peux pas être récupéré, la valeur par défaut (7) est utilisée
     * 
     * @return nombre de thread max.
     */
    private int getMaxThreads() {

        String maxThreadValue;

        try {
            maxThreadValue = GlobazSystem.getApplication(ALApplication.DEFAULT_APPLICATION_WEBAF).getProperty(
                    ALConstPrestations.PREST_GEN_MAX_THREADS);
        } catch (Exception e) {
            maxThreadValue = "7";
            JadeLogger.warn(this, "Unable to retrieve the property [" + ALApplication.APPLICATION_NAME + "."
                    + ALConstPrestations.PREST_GEN_MAX_THREADS + "] : " + e.getMessage()
                    + " . Default value is used : " + maxThreadValue);
        }

        if (JadeStringUtil.isBlankOrZero(maxThreadValue)) {
            maxThreadValue = "7";
            JadeLogger.warn(this, "maxThread value is not valid : " + maxThreadValue + " . Default value is used : "
                    + maxThreadValue);
            maxThreadValue = "7";
        }

        return Integer.parseInt(maxThreadValue);
    }
}
