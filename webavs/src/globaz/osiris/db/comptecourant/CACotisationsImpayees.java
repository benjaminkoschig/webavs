package globaz.osiris.db.comptecourant;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteCourant;

public class CACotisationsImpayees extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_TOTAL_MONTANT = "total_montant";
    public static final String TABLE_CACPTCP = CACompteCourant.TABLE_CACPTCP;
    private static final String ZERO = "0";
    private String idexterne = "";
    private String montant = CACotisationsImpayees.ZERO;

    public CACotisationsImpayees() {
        super();
    }

    @Override
    protected String _getTableName() {
        return CACotisationsImpayees.TABLE_CACPTCP;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdexterne(statement.dbReadString(CACompteCourant.FIELD_IDEXTERNE));
        setMontant(statement.dbReadString(CACotisationsImpayees.FIELD_TOTAL_MONTANT));
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

    public String getIdexterne() {
        return idexterne;
    }

    public String getMontant() {
        if (JadeStringUtil.isBlank(montant)) {
            montant = CACotisationsImpayees.ZERO;
        }
        return montant;
    }

    public void setIdexterne(String idexterne) {
        this.idexterne = idexterne;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}
