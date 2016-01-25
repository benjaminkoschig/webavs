package ch.globaz.vulpecula.facturation;

public class PTFacturationCongePayeImpl extends PTFacturationGenericImpl {

    @Override
    public PTProcessFacturation getProcessComptabilisation() {
        return new PTProcessFacturationCongePayeComptabiliser();
    }

    @Override
    public PTProcessFacturation getProcessGeneration() {
        return new PTProcessFacturationCongePayeGenerer();
    }

}
