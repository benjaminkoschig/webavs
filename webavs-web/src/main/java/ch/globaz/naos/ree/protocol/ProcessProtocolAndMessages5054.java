package ch.globaz.naos.ree.protocol;

import java.util.List;
import java.util.Map;

public class ProcessProtocolAndMessages5054 extends ProcessProtocolAndMessages {

    private Map<Integer, ProcessProtocolAndMessages> resultatParAnnee;

    /**
     * @param businessMessages
     * @param protocol
     */
    public ProcessProtocolAndMessages5054() {
        super(null, null);
    }

    public ProcessProtocolAndMessages5054(Map<Integer, ProcessProtocolAndMessages> resultatParAnnee) {
        super(null, null);
        this.resultatParAnnee = resultatParAnnee;
    }

    public Map<Integer, ProcessProtocolAndMessages> getResultatParAnnee() {
        return resultatParAnnee;
    }

    @Override
    public ProcessProtocol getProcessProtocol() {
        throw new RuntimeException("Don't use this method");
    }

    @Override
    public List<?> getBusinessMessages() {
        throw new RuntimeException("Don't use this method");
    }

}
