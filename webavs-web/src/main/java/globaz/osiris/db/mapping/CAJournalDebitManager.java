/**
 *
 */
package globaz.osiris.db.mapping;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author sel
 * 
 */
public class CAJournalDebitManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCompteCourantSrc = "";
    private String forContrePartieSrc = "";
    private String forIdMandat = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isBlank(getForCompteCourantSrc())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(CAJournalDebit.FIELD_COMPTECOURANTSRC);
            sqlWhere.append(" LIKE '");
            sqlWhere.append(getForCompteCourantSrc());
            sqlWhere.append("%' ");
        }

        if (!JadeStringUtil.isBlank(getForContrePartieSrc())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(CAJournalDebit.FIELD_CONTREPARTIESRC);
            sqlWhere.append(" LIKE '");
            sqlWhere.append(getForContrePartieSrc());
            sqlWhere.append("%' ");
        }

        if (!JadeStringUtil.isBlank(getForIdMandat())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(CAJournalDebit.FIELD_IDMANDAT);
            sqlWhere.append(" = ");
            sqlWhere.append(getForIdMandat());
            sqlWhere.append(" ");
        }

        return sqlWhere.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAJournalDebit();
    }

    /**
     * @return the forCompteCourantSrc
     */
    public String getForCompteCourantSrc() {
        return forCompteCourantSrc;
    }

    /**
     * @return the forContrePartieSrc
     */
    public String getForContrePartieSrc() {
        return forContrePartieSrc;
    }

    /**
     * @return the forIdMandat
     */
    public String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * @param forCompteCourantSrc
     *            the forCompteCourantSrc to set
     */
    public void setForCompteCourantSrc(String forCompteCourantSrc) {
        this.forCompteCourantSrc = forCompteCourantSrc;
    }

    /**
     * @param forContrePartieSrc
     *            the forContrePartieSrc to set
     */
    public void setForContrePartieSrc(String forContrePartieSrc) {
        this.forContrePartieSrc = forContrePartieSrc;
    }

    /**
     * @param forIdMandat
     *            the forIdMandat to set
     */
    public void setForIdMandat(String forIdMandat) {
        this.forIdMandat = forIdMandat;
    }

}
