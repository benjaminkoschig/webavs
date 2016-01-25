package ch.globaz.vulpecula.facturation;

/**
 * Module de facturation des décomptes
 */
public class PTFacturationDecomptesImpl extends PTFacturationGenericImpl {
    public PTFacturationDecomptesImpl() {
        super();
    }

    @Override
    public PTProcessFacturation getProcessComptabilisation() {
        return new PTProcessFacturationDecomptesSalaireComptabiliser();
    }

    @Override
    public PTProcessFacturation getProcessGeneration() {
        return new PTProcessFacturationDecomptesSalaireGenerer();
    }
}