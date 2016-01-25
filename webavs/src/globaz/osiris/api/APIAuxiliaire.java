package globaz.osiris.api;

/**
 * @author dda
 */
public interface APIAuxiliaire extends APIOperation {
    public final static String CREDIT = "2";
    public final static String DEBIT = "1";
    public final static String EXTOURNE_CREDIT = "4";
    public final static String EXTOURNE_DEBIT = "3";

    public String getCodeDebitCredit();

    public String getLibelle();

    public String getMontant();

    public void setCodeDebitCredit(String newCodeDebitCredit);

    public void setLibelle(String newLibelle);

    public void setMontant(String newMontant);
}
