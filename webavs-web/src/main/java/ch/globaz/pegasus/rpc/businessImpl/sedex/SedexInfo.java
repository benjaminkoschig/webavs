package ch.globaz.pegasus.rpc.businessImpl.sedex;

import java.io.Serializable;

public class SedexInfo implements Serializable {

    private final String sedexSenderId;
    private final boolean testDeliveryFlag;
    private final String persistenceDir;

    public SedexInfo(String sedexSenderId, boolean testDeliveryFlag, String persistenceDir) {
        this.sedexSenderId = sedexSenderId;
        this.testDeliveryFlag = testDeliveryFlag;
        this.persistenceDir = persistenceDir;
    }

    public String getSedexSenderId() {
        return sedexSenderId;
    }

    public boolean getTestDeliveryFlag() {
        return testDeliveryFlag;
    }

    public String getPersistenceDir() {
        return persistenceDir;
    }

}
