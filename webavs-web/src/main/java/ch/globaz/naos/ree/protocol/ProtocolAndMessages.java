package ch.globaz.naos.ree.protocol;

import java.util.List;

public class ProtocolAndMessages {

    private TechnicalProtocol technicalProtocol;
    private ProcessProtocolAndMessages processProtocolAndMessages;

    /**
     * @param businessMessages
     * @param protocol
     */
    public ProtocolAndMessages(TechnicalProtocol technicalProtocol,
            ProcessProtocolAndMessages processProtocolAndMessages) {
        super();
        this.technicalProtocol = technicalProtocol;
        this.processProtocolAndMessages = processProtocolAndMessages;
    }

    public TechnicalProtocol getTechnicalProtocol() {
        return technicalProtocol;
    }

    public List<?> getBusinessMessages() {
        return processProtocolAndMessages.getBusinessMessages();
    }

    public ProcessProtocol getProcessProtocol() {
        return processProtocolAndMessages.getProcessProtocol();
    }

    public ProcessProtocolAndMessages getProcessProtocolAndMessages() {
        return processProtocolAndMessages;
    }

}
