package globaz.osiris.api.ordre;

public interface APIOrdreGroupe {

    public final static String ISO_TYPE_AVIS_AUCUN = "261001";
    public final static String ISO_TYPE_AVIS_DETAIL = "261002";
    public final static String ISO_TYPE_AVIS_COLLECT_SANS = "261003";
    public final static String ISO_TYPE_AVIS_COLLECT_AVEC = "261004";

    public final static String ISO_ORDRE_STATUS_A_TRANSMETTRE = "262001";
    public final static String ISO_ORDRE_STATUS_TRANSMIS = "262002";
    public final static String ISO_ORDRE_STATUS_CONFIRME = "262003";

    public final static String ISO_TRANSAC_STATUS_AUCUNE = "260001";
    public final static String ISO_TRANSAC_STATUS_COMPLET = "260002";
    public final static String ISO_TRANSAC_STATUS_PARTIEL = "260003";
    public final static String ISO_TRANSAC_STATUS_REJETE = "260004";

    public String getDateCreation();

    public String getDateEcheance();

    public String getMotif();

    public String getNbTransactions() throws Exception;

    public String getNumeroOG();

    public APIOrganeExecution getOrganeExecution() throws Exception;

    public String getTotal() throws Exception;

    public String getTypeOrdreGroupe();

    // new iso20022/sepa
    public String getNumLivraison();

    public String getIsoHighPriority();
}
