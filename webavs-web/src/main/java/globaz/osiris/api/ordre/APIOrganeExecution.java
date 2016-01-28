package globaz.osiris.api.ordre;

import globaz.osiris.external.IntAdressePaiement;

public interface APIOrganeExecution {

    public final static String BANQUE = "206002";
    public static final String BVR_AUCUN = "211001";

    public static final String BVR_TYPE3 = "211002";
    public final static String CS_BY_FTPPOST = "246002"; // Mode de transfert
    public final static String CS_BY_MAIL = "246001"; // Mode de transfert
    public static final String LSV_AUCUN = "212001";
    public final static String LSV_BANQUE = "212003";
    public final static String OG_AUCUN = "258001";
    public final static String OG_OPAE_DTA = "258002";

    public final static String LSV_POSTE = "212002";
    public final static String POSTE = "206001";

    public IntAdressePaiement getAdresseDebitTaxes() throws Exception;

    public IntAdressePaiement getAdressePaiement() throws Exception;

    public String getGenre();

    public String getIdentifiantDTA();

    public String getIdRubrique();

    public String getModeTransfert();

    public String getNoAdherent();

    public String getNoAdherentBVR();

    public String getNom();

    public String getNumInterneLsv();
}
