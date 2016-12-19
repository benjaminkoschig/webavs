package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CAControleOperationSection extends BEntity {

    private static final long serialVersionUID = 2340332687672943550L;

    private String idExterneRole;
    private String idSection;
    private String idExterne;
    private String soldeSection;
    private String sommeOperations;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idExterneRole = statement.dbReadString("IDEXTERNEROLECOMPTE");
        idSection = statement.dbReadString("IDSECTIONSECTION");
        idExterne = statement.dbReadString("IDEXTERNESECTION");
        soldeSection = statement.dbReadString("SOLDESECTION");
        sommeOperations = statement.dbReadString("SOMME");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        return;
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        return;
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        return;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    public String getIdSection() {
        return idSection;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public String getSoldeSection() {
        return soldeSection;
    }

    public void setSoldeSection(String soldeSection) {
        this.soldeSection = soldeSection;
    }

    public String getSommeOperations() {
        return sommeOperations;
    }

    public void setSommeOperations(String sommeOperations) {
        this.sommeOperations = sommeOperations;
    }
}
