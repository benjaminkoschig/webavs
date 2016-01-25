package globaz.tucana.db.list;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class TUAffiliationProblem extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCompteAnnexe;
    private String idExterneRole;
    private String idRole;
    private String montant;

    @Override
    protected String _getTableName() {
        // Not used here.
        return "";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setMontant(statement.dbReadNumeric("MONTANT"));
        setIdExterneRole(statement.dbReadString("IDEXTERNEROLE"));
        setIdRole(statement.dbReadString("IDROLE"));
        setIdCompteAnnexe(statement.dbReadString("IDCOMPTEANNEXE"));
    }

    @Override
    protected void _validate(BStatement arg0) throws Exception {
        // Nothing here.
    }

    @Override
    protected void _writePrimaryKey(BStatement arg0) throws Exception {
        // Nothing here.
    }

    @Override
    protected void _writeProperties(BStatement arg0) throws Exception {
        // Nothing here.
    }

    public String getHashKey() {
        return (getIdCompteAnnexe() + "-" + getMontant()).trim();
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public String getIdRole() {
        return idRole;
    }

    public String getMontant() {
        return montant;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
