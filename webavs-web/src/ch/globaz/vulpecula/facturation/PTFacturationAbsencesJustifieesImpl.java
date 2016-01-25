package ch.globaz.vulpecula.facturation;

public class PTFacturationAbsencesJustifieesImpl extends PTFacturationGenericImpl {

    @Override
    public PTProcessFacturation getProcessComptabilisation() {
        return new PTProcessFacturationAbsencesJustifieesComptabiliser();
    }

    @Override
    public PTProcessFacturation getProcessGeneration() {
        return new PTProcessFacturationAbsencesJustifieesGenerer();
    }

}
