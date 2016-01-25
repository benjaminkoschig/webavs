package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * <H1>Attributions des points pour le contrôle employeur</H1>
 * 
 * @author jpa
 * @since Créé le 12 févr. 07
 */
public class CEAttributionPtsCumulMasse extends BEntity {

    private static final long serialVersionUID = 5377333206939704220L;

    public static final String FIELD_CUMUL_MASSE = "CUMULMASSE";

    private String cumulMasse = "";

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        cumulMasse = statement.dbReadString(FIELD_CUMUL_MASSE);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getCumulMasse() {
        return cumulMasse;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setCumulMasse(String cumulMasse) {
        this.cumulMasse = cumulMasse;
    }
}
