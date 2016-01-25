package globaz.osiris.db.bvrftp.log;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author dda
 */
public class CABvrFtpLogManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCodeJournal;
    private String forFileName;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isBlank(getForCodeJournal())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + CABvrFtpLog.TABLE_CALOGPP + "." + CABvrFtpLog.FIELD_CODEJOURNAL + "="
                    + this._dbWriteString(statement.getTransaction(), getForCodeJournal());
        }

        if (!JadeStringUtil.isBlank(getForFileName())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + CABvrFtpLog.TABLE_CALOGPP + "." + CABvrFtpLog.FIELD_FILE + "="
                    + this._dbWriteString(statement.getTransaction(), getForFileName());
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CABvrFtpLog();
    }

    /**
     * @return
     */
    public String getForCodeJournal() {
        return forCodeJournal;
    }

    /**
     * @return
     */
    public String getForFileName() {
        return forFileName;
    }

    /**
     * @param string
     */
    public void setForCodeJournal(String string) {
        forCodeJournal = string;
    }

    /**
     * @param string
     */
    public void setForFileName(String string) {
        forFileName = string;
    }

}
