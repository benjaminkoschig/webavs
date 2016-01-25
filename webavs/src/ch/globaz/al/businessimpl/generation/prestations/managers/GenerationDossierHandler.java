package ch.globaz.al.businessimpl.generation.prestations.managers;

import globaz.jade.context.JadeThreadActivator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.io.PrintWriter;
import java.io.StringWriter;
import ch.globaz.al.businessimpl.generation.prestations.GenPrestationFactory;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextPrestation;

/**
 * Classe permettant l'exécution de la génération d'un dossier. Elle fait appel à la factory de génération
 * {@link ch.globaz.al.businessimpl.generation.prestations.GenPrestationFactory} et exécute la génération appropriée.
 * 
 * @author jts
 * 
 */
public class GenerationDossierHandler extends Thread {

    /**
     * Contexte de l'affilié
     */
    private ContextAffilie contextAffilie = null;

    /**
     * Constructeur
     * 
     * @param contextAffilie
     *            Contexte de l'affilié
     */
    public GenerationDossierHandler(ContextAffilie contextAffilie) {
        this.contextAffilie = contextAffilie;
    }

    @Override
    public void run() {

        try {

            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(),
                    ContextProvider.getNewJadeThreadContext(contextAffilie.getJadeContextImpl()).getContext());

            GenPrestationFactory.getGenPrestation(contextAffilie).execute();

            // enregistre les prestations et libère les contextes de dossier et
            // de prestations
            contextAffilie.releaseDossier();
        } catch (Exception e) {

            StringBuffer message = new StringBuffer("Classe : GenerationDossierHandler\n");
            message.append("Affilié : ").append(contextAffilie.getNumAffilie()).append("\n");
            message.append("Dossier : ").append(contextAffilie.getContextDossier().getDossier().getId()).append("\n");
            message.append("Message : ").append(e.getMessage()).append("\n");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            message.append(sw.toString());

            contextAffilie.getLogger().addFatalError(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, ContextPrestation.class.getName(), message
                            .toString()));

            // suppression de l'info qui a été ajouté au moment de génération
            // pour éviter de la comptabiliser dans le nombre de prestations
            // effectivement enregistrée en persistence
            contextAffilie.getLogger().removeInfoLogger(contextAffilie.getContextDossier().getDossier().getId());
            contextAffilie.getLogger().removeWarningLogger(contextAffilie.getContextDossier().getDossier().getId());
            contextAffilie.getLogger().removeErrorLogger(contextAffilie.getContextDossier().getDossier().getId());

            try {
                if (contextAffilie.getContextDossier() != null) {
                    contextAffilie.rollbackPrestations();
                }
            } catch (JadeApplicationException e1) {
                StringBuffer message2 = new StringBuffer("FATAL ERROR : Unable to rollback ContextDossier\n");
                message2.append("Classe : GenerationDossierHandler\n");
                message2.append("Dossier : ").append(contextAffilie.getContextDossier().getDossier().getId())
                        .append("\n");
                message2.append("Message : ").append(e.getMessage()).append("\n");
                StringWriter sw2 = new StringWriter();
                PrintWriter pw2 = new PrintWriter(sw);
                e.printStackTrace(pw2);
                message2.append(sw2.toString());
                contextAffilie.getLogger().addFatalError(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, ContextPrestation.class.getName(),
                                message2.toString()));
            }
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }
}