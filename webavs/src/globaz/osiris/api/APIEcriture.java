package globaz.osiris.api;

public interface APIEcriture extends APIOperation {
    public final static String CREDIT = "2";
    public static final String CS_LIBELLE_IDENTIFIER = "%";
    public final static String DEBIT = "1";
    public final static String EXTOURNE_CREDIT = "4";

    public final static String EXTOURNE_DEBIT = "3";

    public String getAnneeCotisation();

    public String getCodeDebitCredit();

    public String getIdCaisseProfessionnelle();

    public String getIdCompte();

    public String getIdContrepartie();

    public String getLibelle();

    public String getMasse();

    public String getMontant();

    public String getNoEcritureCollective();

    public String getPiece();

    public String getTaux();

    public Boolean getUnsynchronizeSigneMasse();

    public void setAnneeCotisation(String newAnneeCotisation);

    public void setCodeDebitCredit(String newCodeDebitCredit);

    public void setIdCaisseProfessionnelle(String idCaisseProfessionnelle);

    public void setIdCompte(String newIdCompte);

    public void setIdContrepartie(String newIdContrepartie);

    public void setLibelle(String newLibelle);

    public void setMasse(String newMasse);

    public void setMontant(String newMontant);

    public void setPiece(String newPiece);

    public void setTaux(String newTaux);

    public void setUnsynchronizeSigneMasse(Boolean newDisableConversionMasse);
}
