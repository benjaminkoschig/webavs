package ch.globaz.naos.ree.protocol;

import java.util.List;

public class ProcessProtocolAndMessages {

    private List<?> businessMessages;

    private ProcessProtocol processProtocol;

    /**
     * @param businessMessages
     * @param protocol
     */
    public ProcessProtocolAndMessages(List<?> businessMessages, ProcessProtocol processProtocol) {
        super();
        this.businessMessages = businessMessages;
        this.processProtocol = processProtocol;
    }

    public List<?> getBusinessMessages() {
        return businessMessages;
    }

    public ProcessProtocol getProcessProtocol() {
        return processProtocol;
    }
}
