package ch.globaz.vulpecula.facturation;

/**
 * Module de facturation des d�comptes
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