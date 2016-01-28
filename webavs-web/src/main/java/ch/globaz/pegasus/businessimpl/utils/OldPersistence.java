package ch.globaz.pegasus.businessimpl.utils;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;

public abstract class OldPersistence<T> {
    public abstract T action() throws Exception;

    public T execute() throws Exception {
        // on est obliger d'enlever les log pour executer le find
        JadeBusinessMessage[] messages = JadeThread.logMessages();
        JadeThread.logClear();
        try {
            return this.action();
        } finally {
            // :) on rest les log, c'est pas beau la technologie !!!!
            if (messages != null) {
                for (JadeBusinessMessage message : messages) {
                    if (JadeBusinessMessageLevels.ERROR == message.getLevel()) {
                        JadeThread.logError(message.getSource(), message.getMessageId(), message.getParameters());
                    }
                    if (JadeBusinessMessageLevels.WARN == message.getLevel()) {
                        JadeThread.logWarn(message.getSource(), message.getMessageId(), message.getParameters());
                    }
                    if (JadeBusinessMessageLevels.INFO == message.getLevel()) {
                        JadeThread.logInfo(message.getSource(), message.getMessageId(), message.getParameters());
                    }
                }
            }
        }
    }

    public T executeWithOutException() {
        try {
            return this.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}