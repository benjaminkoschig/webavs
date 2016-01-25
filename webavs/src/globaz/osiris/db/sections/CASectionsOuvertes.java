package globaz.osiris.db.sections;

import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;

public class CASectionsOuvertes extends CASection {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idExterneCompteAnnexe;

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        setIdExterneCompteAnnexe(statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE));
    }

    public String getIdExterneCompteAnnexe() {
        return idExterneCompteAnnexe;
    }

    public void setIdExterneCompteAnnexe(String idExterneCompteAnnexe) {
        this.idExterneCompteAnnexe = idExterneCompteAnnexe;
    }
}
