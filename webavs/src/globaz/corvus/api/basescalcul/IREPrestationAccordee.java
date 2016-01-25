package globaz.corvus.api.basescalcul;

public interface IREPrestationAccordee {

    public final static String CS_ETAT_AJOURNE = "52820005";
    public final static String CS_ETAT_CALCULE = "52820001";
    public final static String CS_ETAT_DIMINUE = "52820004";
    public final static String CS_ETAT_PARTIEL = "52820003";
    public final static String CS_ETAT_VALIDE = "52820002";
    public final static String CS_GENRE_PC = "52849002";

    public final static String CS_GENRE_RENTES = "52849001";
    public final static String CS_GENRE_RFM = "52849003";

    // REETATRA ://Etat de la rente accordée
    public final static String CS_GROUPE_ETAT_RENTE_ACCORDEE = "REETATRA";
    // REGENPRA : //Genre prestation accordées
    public final static String CS_GROUPE_GENRE_PREST_ACCORDEE = "REGENPRA";

    public final static String CS_MAJ_BLOCAGE_AUTOMATIQUE = "52855001";
    public final static String CS_MAJ_BLOCAGE_MANUELLE = "52855002";
    // Type de mise a jours de la prestation accordee
    public final static String CS_TYPE_MAJ_PRESTATION_ACCORDEE = "RETYPMAJPA";

}
