package globaz.osiris.db.comptecourant;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

public class CASoldesParCompteCourant extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String description;
    private String idExterneCompteCourant;
    private String idExterneRole;
    private String idExterneSection;
    private String montant;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdExterneRole(statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE));
        setDescription(statement.dbReadString(CACompteAnnexe.FIELD_DESCRIPTION));
        setIdExterneSection(statement.dbReadString(CASection.FIELD_IDEXTERNE));
        setIdExterneCompteCourant(statement.dbReadString(CACompteCourant.FIELD_IDEXTERNE + "CC"));
        setMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));
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

    public String getDescription() {
        return description;
    }

    public String getIdExterneCompteCourant() {
        return idExterneCompteCourant;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public String getIdExterneSection() {
        return idExterneSection;
    }

    public String getMontant() {
        return montant;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdExterneCompteCourant(String idExterneCompteCourant) {
        this.idExterneCompteCourant = idExterneCompteCourant;
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    public void setIdExterneSection(String idExterneSection) {
        this.idExterneSection = idExterneSection;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
