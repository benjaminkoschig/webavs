package ch.globaz.pegasus.process.allocationsNoel;

import globaz.jade.context.JadeThread;

public enum PCProcessAllocationsNoelEnum {
    ANNEE_ALLOCATION_NOEL("pegasus.process.allocationsNoel.dateAllocationNoel"),
    GENRE_PC("pegasus.allocationsNoel.genrepc"),
    MONTANT_ALLOCATIONS("pegasus.allocationsNoel.montantAllocation"),
    MONTANT_ALLOCATIONS_CONJOINT("pegasus.allocationsNoel.montantAllocationConjoint"),
    NOMBRE_PERSONNES("pegasus.allocationsNoel.nombrePersonnes");

    private final String idLabel;

    PCProcessAllocationsNoelEnum(String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }
}
