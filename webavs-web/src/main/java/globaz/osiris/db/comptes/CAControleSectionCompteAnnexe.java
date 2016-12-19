package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CAControleSectionCompteAnnexe extends BEntity {

    private static final long serialVersionUID = 2340332687672943550L;

    private String idCompteAnnexe;
    private String idExterneRole;
    private String soldeCompte;
    private String sommeSections;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCompteAnnexe = statement.dbReadString("IDCOMPTEANNEXECOMPTE");
        idExterneRole = statement.dbReadString("IDEXTERNEROLECOMPTE");
        soldeCompte = statement.dbReadString("SOLDECOMPTE");
        sommeSections = statement.dbReadString("SOMME");
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

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    public String getSoldeCompte() {
        return soldeCompte;
    }

    public void setSoldeCompte(String soldeCompte) {
        this.soldeCompte = soldeCompte;
    }

    public String getSommeSections() {
        return sommeSections;
    }

    public void setSommeSections(String sommeSections) {
        this.sommeSections = sommeSections;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
