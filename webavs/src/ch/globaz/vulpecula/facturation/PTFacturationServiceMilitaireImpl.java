package ch.globaz.vulpecula.facturation;

public class PTFacturationServiceMilitaireImpl extends PTFacturationGenericImpl {

    @Override
    public PTProcessFacturation getProcessComptabilisation() {
        return new PTProcessFacturationServiceMilitaireComptabiliser();
    }

    @Override
    public PTProcessFacturation getProcessGeneration() {
        return new PTProcessFacturationServiceMilitaireGenerer();
    }

}
