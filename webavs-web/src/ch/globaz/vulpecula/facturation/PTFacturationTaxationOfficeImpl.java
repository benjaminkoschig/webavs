package ch.globaz.vulpecula.facturation;

public class PTFacturationTaxationOfficeImpl extends PTFacturationGenericImpl {

    @Override
    public PTProcessFacturation getProcessGeneration() {
        return new PTProcessFacturationTaxationOfficeGenerer();
    }

    @Override
    public PTProcessFacturation getProcessComptabilisation() {
        return new PTProcessFacturationTaxationOfficeComptabiliser();
    }
}
