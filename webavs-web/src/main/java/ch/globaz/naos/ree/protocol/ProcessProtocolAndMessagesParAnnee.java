package ch.globaz.naos.ree.protocol;


public class ProcessProtocolAndMessagesParAnnee {

    private int annee;
    private ProcessProtocolAndMessages processProtocolAndMessages;

    /**
     * @param annee
     * @param processProtocolAndMessages
     */
    public ProcessProtocolAndMessagesParAnnee(int annee, ProcessProtocolAndMessages processProtocolAndMessages) {
        this.annee = annee;
        this.processProtocolAndMessages = processProtocolAndMessages;
    }

    public int getAnnee() {
        return annee;
    }

    public ProcessProtocolAndMessages getProcessProtocolAndMessages() {
        return processProtocolAndMessages;
    }

}
