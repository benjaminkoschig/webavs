package globaz.osiris.db.bvrftp;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author dda
 */
public class CABvrFtpViewBean extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean alreadyDownloaded = false;
    private String distantDirectory;
    private String fileDate;
    private String fileName;

    public CABvrFtpViewBean() {
    }

    public CABvrFtpViewBean(String date, String fileName, String distantDirectory) {
        setFileDate(date);
        setFileName(fileName);
        setDistantDirectory(distantDirectory);
    }

    public CABvrFtpViewBean(String date, String fileName, String distantDirectory, boolean downloaded) {
        setFileDate(date);
        setFileName(fileName);
        setAlreadyDownloaded(downloaded);
        setDistantDirectory(distantDirectory);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

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

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

    }

    /**
     * @return
     */
    public String getDistantDirectory() {
        return distantDirectory;
    }

    /**
     * @return
     */
    public String getFileDate() {
        return fileDate;
    }

    /**
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    public String getFormatedFileDate() {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(inputFormat.parse(getFileDate()));

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

            return outputFormat.format(cal.getTime());
        } catch (ParseException e) {
            // Do nothing.
        }

        return "";
    }

    /**
     * @return
     */
    public boolean isAlreadyDownloaded() {
        return alreadyDownloaded;
    }

    /**
     * @param b
     */
    public void setAlreadyDownloaded(boolean b) {
        alreadyDownloaded = b;
    }

    /**
     * @param string
     */
    public void setDistantDirectory(String string) {
        distantDirectory = string;
    }

    /**
     * @param string
     */
    public void setFileDate(String s) {
        fileDate = s.trim();
    }

    /**
     * @param string
     */
    public void setFileName(String s) {
        fileName = s.trim();
    }

}
