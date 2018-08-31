package ch.globaz.vulpecula.facturation;

/**
 * Module de facturation des associations professionnelles
 */
public class PTFacturationAssociationsProfImpl extends PTFacturationGenericImpl {
    public PTFacturationAssociationsProfImpl() {
        super();
    }

    @Override
    public PTProcessFacturation getProcessComptabilisation() {
        return new PTProcessFacturationAssociationsProfComptabiliser();
    }

    @Override
    public PTProcessFacturation getProcessGeneration() {
        return new PTProcessFacturationAssociationsProfGenerer();
    }
}