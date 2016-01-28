package globaz.osiris.db.bulletinSolde;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

/**
 * @author dda CAOPERP table entity. Use for the "Extrait de Compte" function.
 */
public class CABulletinSolde extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String SECTIONDATE_FIELD = "SECTIONDATE";

    // CAOPERP variable
    private String date;
    private String idCompteAnnexe;
    private String idSection;

    /**
     * Return le nom de la table (CAOPERP).
     */
    @Override
    protected String _getTableName() {
        return CAOperation.TABLE_CAOPERP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdSection(statement.dbReadNumeric(CASection.FIELD_IDSECTION));
        setIdCompteAnnexe(statement.dbReadNumeric(CACompteAnnexe.FIELD_IDCOMPTEANNEXE));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        try {
            BSessionUtil.checkDateGregorian(getSession(), getDate());
        } catch (Exception ex) {
            _addError(statement.getTransaction(), "Invalid date");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Not needed here
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not needed here
    }

    /**
     * @return
     */
    public String getDate() {
        return date;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * @param string
     */
    public void setDate(String string) {
        date = string;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param string
     */
    public void setIdSection(String id) {
        idSection = id;
    }

}
