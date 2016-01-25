package globaz.osiris.db.bvrftp.log;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author dda
 */
public class CABvrFtpLog extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_CODEJOURNAL = "CODEJOURNAL";
    public static final String FIELD_FILE = "FILE";

    public static final String FIELD_IDLOGP = "IDLOGP";
    public static final String FIELD_MOREINFOS = "MOREINFOS";
    public static final String FILE_PASSWORD = "PW";

    public static final String FILE_RECEIVE = "RC";
    public static final String FILE_SEND = "SN";
    public static final String MORE_INFOS_FILE_PASSWORD = "See FWPARP";
    public static final String MORE_INFOS_FILE_RECEIVE = "Nom AS/400=CDEPSBVR";

    public static final String TABLE_CALOGPP = "CALOGPP";

    private String codeJournal;
    private String fileName;
    private String idLogPostFinance;
    private String moreInformations;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdLogPostFinance(this._incCounter(transaction, "0"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CABvrFtpLog.TABLE_CALOGPP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdLogPostFinance(statement.dbReadNumeric(CABvrFtpLog.FIELD_IDLOGP));
        setFileName(statement.dbReadString(CABvrFtpLog.FIELD_FILE));
        setCodeJournal(statement.dbReadString(CABvrFtpLog.FIELD_CODEJOURNAL));
        setMoreInformations(statement.dbReadString(CABvrFtpLog.FIELD_MOREINFOS));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CABvrFtpLog.FIELD_IDLOGP,
                this._dbWriteNumeric(statement.getTransaction(), getIdLogPostFinance(), "idLogPostFinance"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CABvrFtpLog.FIELD_IDLOGP,
                this._dbWriteNumeric(statement.getTransaction(), getIdLogPostFinance(), "idLogPostFinance"));
        statement.writeField(CABvrFtpLog.FIELD_FILE,
                this._dbWriteString(statement.getTransaction(), getFileName(), "fileName"));
        statement.writeField(CABvrFtpLog.FIELD_CODEJOURNAL,
                this._dbWriteString(statement.getTransaction(), getCodeJournal(), "codeJournal"));
        statement.writeField(CABvrFtpLog.FIELD_MOREINFOS,
                this._dbWriteString(statement.getTransaction(), getMoreInformations(), "moreInformations"));
    }

    /**
     * @return
     */
    public String getCodeJournal() {
        return codeJournal;
    }

    /**
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return
     */
    public String getIdLogPostFinance() {
        return idLogPostFinance;
    }

    /**
     * @return
     */
    public String getMoreInformations() {
        return moreInformations;
    }

    /**
     * @param string
     */
    public void setCodeJournal(String string) {
        codeJournal = string;
    }

    /**
     * @param string
     */
    public void setFileName(String string) {
        fileName = string;
    }

    /**
     * @param string
     */
    public void setIdLogPostFinance(String string) {
        idLogPostFinance = string;
    }

    /**
     * @param string
     */
    public void setMoreInformations(String string) {
        moreInformations = string;
    }

}
