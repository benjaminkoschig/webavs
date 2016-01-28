package ch.globaz.al.businessimpl.generation.prestations.managers;

import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.io.PrintWriter;
import java.io.StringWriter;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextGlobal;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextPrestation;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.generation.prestations.GenerationServiceImpl;

/**
 * Classe se chargeant d'ex�cuter le service de g�n�ration pour le contexte pass� en param�tre au constructeur
 * 
 * @author jts
 * 
 */
public class AffilieHandler implements Runnable {
    /**
     * Manager de g�n�ration (g�re les threads en cours d'ex�cution)
     */
    private GenerationManager generationManager = null;
    /**
     * Liste des dossier pour lesquels g�n�rer des prestations
     */
    private ContextGlobal globalContext = null;

    /**
     * Constructeur
     * 
     * @param globalContext
     *            Les dossiers devant �tres trait�s par le thread
     * @param generationManager
     *            le thread parent
     */
    public AffilieHandler(ContextGlobal globalContext, GenerationManager generationManager) {
        this.globalContext = globalContext;
        this.generationManager = generationManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {

            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(),
                    ContextProvider.getNewJadeThreadContext(globalContext.getContextAffilie().getJadeContextImpl())
                            .getContext());

            ((GenerationServiceImpl) ALImplServiceLocator.getGenerationService()).generationAffilie(globalContext);

        } catch (Exception e) {
            StringBuffer message = new StringBuffer("Classe : AffilieHandler\n");
            message.append("Affili� : ").append(globalContext.getContextAffilie().getNumAffilie()).append("\n");
            message.append("Dossier : ")
                    .append(globalContext.getContextAffilie().getContextDossier().getDossier().getId()).append("\n");
            message.append("Message : ").append(e.getMessage()).append("\n");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            message.append(sw.toString());
            globalContext
                    .getContextAffilie()
                    .getLogger()
                    .addFatalError(
                            new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, ContextPrestation.class.getName(),
                                    message.toString()));
        } finally {
            generationManager.notifyFinished(this);
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }
}
