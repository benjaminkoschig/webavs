package ch.globaz.vulpecula.domain.models.prestations;

/**
 * Interface définissant si la prestation doit être annoncé à l'AVS.
 * => Liste des salaires AVS
 * => Communication des salaires (Excel, CSV)
 * 
 */
public interface AnnoncableAVS {
    boolean isAnnoncableAVS();
}
