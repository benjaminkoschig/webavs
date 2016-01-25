package globaz.osiris.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BTransaction;

/**
 * Insérez la description du type ici. Date de création : (25.02.2002 16:56:29)
 * 
 * @author: Administrator
 */
public interface APISlave extends BIEntity {
    /**
     * Insérez la description de la méthode ici. Date de création : (05.12.2002 13:42:20)
     * 
     * @param transaction
     *            globaz.globall.db.BTransaction
     */
    void activer(BTransaction transaction);

    public void add(BITransaction transaction, boolean ShouldAddError) throws java.lang.Exception;

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2002 14:38:50)
     * 
     * @return java.lang.String
     */
    String getIdOperation();

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 17:43:52)
     * 
     * @param newCodeDebitCredit
     *            java.lang.String
     */
    void setCodeDebitCredit(String newCodeDebitCredit);

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 17:37:27)
     * 
     * @param newCodeMaster
     *            java.lang.String
     */
    void setCodeMaster(String newCodeMaster);

    public void setIdCompteCourant(String newIdCompteCourant);

    /**
     * Insérez la description de la méthode ici. Date de création : (05.03.2002 16:28:06)
     * 
     * @param newIdJournal
     *            java.lang.String
     */
    void setIdJournal(String newIdJournal);

    public void setIdSectionCompensation(String newIdSectionCompensation);

    public void setMontant(String newMontant);

    public void setProvenancePmt(String provenancePmt);

    public void setSectionCompensationDeSur(String newSectionCompensationDeSur);
}
