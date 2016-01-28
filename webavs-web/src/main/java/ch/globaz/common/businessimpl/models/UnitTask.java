package ch.globaz.common.businessimpl.models;

import globaz.jade.context.JadeThread;

public abstract class UnitTask {
    private String blobName = null;
    private String businessErrorMessage = null;
    private String fileName = null;
    private boolean simulation = false;

    private String technicalErrorMessage = null;

    public UnitTask(boolean simulation) {
        super();
        this.simulation = simulation;
    }

    public String getBlobName() {
        return blobName;
    }

    public String getErrorMessage() {
        if (technicalErrorMessage != null) {
            return technicalErrorMessage;
        }
        if (businessErrorMessage != null) {
            return businessErrorMessage;
        }

        return null;
    }

    public String getFileName() {
        return fileName;
    }

    public UnitTaskResultState getResultState() {
        if (technicalErrorMessage != null) {
            return UnitTaskResultState.ERREUR_TECHNNIQUE;
        }
        if (businessErrorMessage != null) {
            return UnitTaskResultState.ERREUR_METIER;
        }

        return UnitTaskResultState.OK;
    }

    public String getResultStateLibelle() {

        String messageKey = "common.unit.task.statut.ok";
        UnitTaskResultState theUnitTaskResultState = getResultState();

        if (UnitTaskResultState.ERREUR_METIER.equals(theUnitTaskResultState)) {
            messageKey = "common.unit.task.statut.erreur.metier";
        }

        if (UnitTaskResultState.ERREUR_TECHNNIQUE.equals(theUnitTaskResultState)) {
            messageKey = "common.unit.task.statut.erreur.technique";
        }

        return JadeThread.getMessage(messageKey);
    }

    public boolean isSimulation() {
        return simulation;
    }

    public void setBlobName(String blobName) {
        this.blobName = blobName;
    }

    public void setBusinessErrorMessage(String businessErrorMessage) {
        this.businessErrorMessage = businessErrorMessage;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setTechnicalErrorMessage(String technicalErrorMessage) {
        this.technicalErrorMessage = technicalErrorMessage;
    }

    public abstract void work() throws Exception;
}
