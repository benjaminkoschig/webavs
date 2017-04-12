package globaz.osiris.parser;

import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;

/**
 * Interface des besoins d'informations purement métier à obtenir d'un processus de remontée automatique des BVR
 * (fichier plat et ISO20022)
 * 
 * @author cel
 * 
 */
public interface IntBVRPojo {

    String GENRE_CREDIT = "D";
    String GENRE_DEBIT = "C";

    public String getCodeRejet();

    public String getDateDepot();

    public String getDateInscription();

    public String getDateTraitement();

    String getGenreEcriture();

    public String getGenreTransaction();

    public String getMontant();

    public String getNumeroAdherent();

    public String getNumeroReference();

    public String getReferenceInterne();

    public String getTaxeTraitement();

    public String getTaxeVersement();

    public String getBankTransactionCode();

    public String getAccountServicerReference();

    public String getDebtor();

    public FWMemoryLog getMemoryLog();

    public void setMemoryLog(FWMemoryLog newMemoryLog);

    BSession getSession();

    void setSession(BSession session);

}
