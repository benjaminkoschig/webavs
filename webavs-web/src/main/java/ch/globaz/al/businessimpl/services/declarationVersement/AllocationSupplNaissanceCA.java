package ch.globaz.al.businessimpl.services.declarationVersement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class AllocationSupplNaissanceCA extends BEntity {
    private String montant = "";
    private String idExterne = "";
    private String dateValeur = "";

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        montant = statement.dbReadNumeric("MONTANT");
        idExterne = statement.dbReadString("IDEXTERNE");
        dateValeur = statement.dbReadDateAMJ("DATE");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

}
