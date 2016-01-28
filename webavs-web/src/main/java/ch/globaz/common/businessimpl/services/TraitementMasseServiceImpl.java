package ch.globaz.common.businessimpl.services;

import globaz.jade.client.util.JadeProgressBarModel;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.util.List;
import ch.globaz.common.business.exceptions.CommonJobException;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.common.business.services.TraitementMasseService;
import ch.globaz.common.businessimpl.models.UnitTask;

public class TraitementMasseServiceImpl implements TraitementMasseService {

    @Override
    public <T extends UnitTask> void traiter(JadeProgressBarModel jadeProgressBarModel, List<T> unitTasks)
            throws Exception {

        // Initialisation de la barre de progression
        jadeProgressBarModel.setMax(unitTasks.size());
        jadeProgressBarModel.setCurrent(0);

        for (UnitTask unitTask : unitTasks) {
            jadeProgressBarModel.setCurrent(jadeProgressBarModel.getCurrent() + 1);

            // Traitement unitaire - ISOLATION DES TRANSACTION AU NIVEAU DE LA METHODE
            try {
                this.traiter(unitTask);
            } catch (CommonJobException e) {

                StringBuffer errorMessage = new StringBuffer(e.getMessage() + " ");

                if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) != null) {
                    for (JadeBusinessMessage aErrorMessage : JadeThread
                            .logMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                        errorMessage.append(aErrorMessage.getContents(JadeThread.currentLanguage()) + " ");
                    }
                }
                // errorMessage contient de toute façon un espace
                // ainsi la tâche unitaire se voit en erreur métier même si e.getMessage() et le log sont null
                unitTask.setBusinessErrorMessage(errorMessage.toString());

            } catch (Exception e) {
                JadeLogger.error("Error during unit task batch processing", e);
                // J'utilise ici le e.toString() car par exemple dans le cas d'une NullPointerException e.getMessage()
                // est null
                // et la tâche unitaire ne se voit alors pas en erreur technique
                unitTask.setTechnicalErrorMessage(e.toString());
            } finally {
                JadeThread.logClear();
            }

        }

    }

    private void traiter(UnitTask unitTask) throws Exception {

        boolean processSucceed = false;

        try {
            unitTask.work();

            // Si le log du jadeThread contient des erreurs, une exception technique est levée car ce cas de figure
            // n'est pas prévu
            if (JadeThread.logMaxLevel() > JadeBusinessMessageLevels.WARN) {
                String message = "";
                for (JadeBusinessMessage jadeMessage : JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                    message = message + jadeMessage.getContents(null);
                }
                throw new CommonTechnicalException(message);
            }

            processSucceed = true;

        } finally {
            if (processSucceed && !unitTask.isSimulation()) {
                try {
                    JadeThread.commitSession();
                } catch (Exception e) {
                    throw new CommonTechnicalException("Problem during commit", e);
                }
            } else {
                // Une exception a forcément été levée pour arriver ici
                try {
                    JadeThread.rollbackSession();
                } catch (Exception e) {
                    // L'exception n'est pas propagée pour ne pas masquer l'exception originelle
                    JadeLogger.error(this, e);
                }
            }
        }
    }

}
