package ch.globaz.pegasus.business.constantes;

public interface IPCPCAccordee {

    // PCETAPCA: etat_pc_accordee
    public final static String CS_ETAT_PCA_CALCULE = "64029001";

    public final static String CS_ETAT_PCA_HISTORISEE = "64029004";

    public final static String CS_ETAT_PCA_VALIDE = "64029002";

    // PCGENPC: genre_pc
    public final static String CS_GENRE_PC_DOMICILE = "64026001";
    public final static String CS_GENRE_PC_HOME = "64026002";
    // PCTYPPC: type_pc
    public final static String CS_TYPE_PC_INVALIDITE = "64027003";

    public final static String CS_TYPE_PC_SURVIVANT = "64027002";
    public final static String CS_TYPE_PC_VIELLESSE = "64027001";

    public final static String ETAT_PCA_COURANT_VALIDE = "64029003";
    public final static String GROUPE_GENRE_PC = "PCGENPC";

    public final static String GROUPE_TYPE_PC = "PCTYPPC";
}
