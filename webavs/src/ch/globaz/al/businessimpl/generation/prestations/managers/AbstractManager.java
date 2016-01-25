package ch.globaz.al.businessimpl.generation.prestations.managers;

import globaz.jade.log.JadeLogger;

/**
 * Classe parente des managers. Toutes les classes manager de la g�n�ration doivent l'�tendre
 * 
 * @author jts
 */
public abstract class AbstractManager implements Runnable {

    /**
     * Handler ma�tre
     */
    private MainGenerationHandler generationHandler = null;

    /**
     * @param generationHandler
     *            Handler ma�tre
     */
    protected AbstractManager(MainGenerationHandler generationHandler) {
        super();
        this.generationHandler = generationHandler;
    }

    /**
     * Ex�cute le traitement
     */
    protected abstract void handle();

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        JadeLogger.info(this, "Start handling : " + this.getClass().getName());
        try {
            handle();
        } finally {
            generationHandler.notifyFinished(this);
            JadeLogger.info(this, "Finished handling : " + this.getClass().getName());
        }
    }

}
