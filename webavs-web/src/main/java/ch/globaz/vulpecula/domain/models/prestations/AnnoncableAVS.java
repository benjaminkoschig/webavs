package ch.globaz.vulpecula.domain.models.prestations;

/**
 * Interface d�finissant si la prestation doit �tre annonc� � l'AVS.
 * => Liste des salaires AVS
 * => Communication des salaires (Excel, CSV)
 * 
 */
public interface AnnoncableAVS {
    boolean isAnnoncableAVS();
}
