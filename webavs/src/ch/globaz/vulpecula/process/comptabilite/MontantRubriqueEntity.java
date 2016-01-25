package ch.globaz.vulpecula.process.comptabilite;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class MontantRubriqueEntity extends BEntity {
    private static final long serialVersionUID = 1573488787981166544L;

    private String idExterne;
    private String libelle;
    private String codeAdministration;
    private String masse;
    private String montant;

    public String getNumRubrique() {
        return idExterne;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCodeAdministration() {
        return codeAdministration;
    }

    public void setCodeAdministration(String codeAdministration) {
        this.codeAdministration = codeAdministration;
    }

    public String getMasse() {
        return masse;
    }

    public void setMasse(String masse) {
        this.masse = masse;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idExterne = statement.dbReadString("IDEXTERNE");
        libelle = statement.dbReadString("LIBELLE");
        codeAdministration = statement.dbReadString("HBCADM");
        montant = statement.dbReadNumeric("MONTANT");
        masse = statement.dbReadNumeric("MASSE");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }
}
