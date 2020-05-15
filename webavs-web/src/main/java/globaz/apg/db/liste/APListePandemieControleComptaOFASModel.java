package globaz.apg.db.liste;

public class APListePandemieControleComptaOFASModel {
    String idTiers = "";
    String idJournal = "";
    String numAffilie = "";
    String dateTransmission ="";
    String dateOp ="";
    String iban ="";
    String montant ="";
    String montantis = "";

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public String getDateTransmission() {
        return dateTransmission;
    }

    public void setDateTransmission(String dateTransmission) {
        this.dateTransmission = dateTransmission;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getDateOp() {
        return dateOp;
    }

    public void setDateOp(String dateOp) {
        this.dateOp = dateOp;
    }

    public String getLineCSV() {
        return numAffilie+";"+idTiers+";"+dateOp;
    }

    public String getMontantis() {
        return montantis;
    }

    public void setMontantis(String montantis) {
        this.montantis = montantis;
    }

}
