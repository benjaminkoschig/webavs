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
 * Manager de g�n�ration. Il se charge de g�rer l'ex�cution des threads de g�n�ration
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
     * Identifiant unique de la g�n�ration
     * 
     * @see AffiliesManager
     */
    private String genAffilieUUID;
    /**
     * Instance du logger devant contenir les messages des erreur survenant pendant la g�n�ration
     */
    private ProtocoleLogger logger;
    /**
     * Liste des threads en ex�cution
     */
    private ArrayList<AffilieHandler> runningThreads = null;

    /**
     * Constructeur
     * 
     * @param generationHandler
     *            handler ma�tre
     * @param logger
     *            Instance du logger devant contenir les messages des erreur survenant pendant la g�n�ration
     * @param genAffilieUUID
     *            Identifiant unique de la g�n�ration
     */
    protected GenerationManager(MainGenerationHandler generationHandler, ProtocoleLogger logger, String genAffilieUUID) {
        super(generationHandler);
        runningThreads = new ArrayList<AffilieHandler>();
        this.genAffilieUUID = genAffilieUUID;
        this.logger = logger;
    }

    /**
     * Lance l'ex�cution de l'importation des dossiers d�s que des �l�ments se trouvent dans le conteneur et que le
     * producteur n'a pas termin� de traiter la totalit� des fichiers
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
     * Ex�cute l'importation des dossiers. Le traitement se termine quand il n'y a plus de dossier � importer.
     * 
     * La m�thode fait appel � <code>ImportationDossierHandler</code> et ex�cute plusieurs thread simultan�ment.
     * 
     * @param container
     *            Conteneur
     */
    private void launch(ContainerAffilies container) {

        int maxThreads = getMaxThreads();

        while (container.isAbleToConsume() || !container.isFinishedProducing() || (runningThreads.size() > 0)) {

            // s'il n'y a rien � consommer mais que le producteur n'a pas encore
            // termin�
            while (!container.isAbleToConsume() && !container.isFinishedProducing()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // DO NOTHING
                }
            }

            // ex�cution de thread jusqu'� atteindre le max et tant qu'il reste
            // des dossiers (affili�s) � traiter
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
     * Utilis� par <code>ImportationDossierHandler</code> pour notifier la fin de traitement d'un dossier
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
     * R�cup�re le nombre de thread pouvant �tre ex�cut�s simultan�ment.
     * 
     * Si le param�tre ne peux pas �tre r�cup�r�, la valeur par d�faut (7) est utilis�e
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
