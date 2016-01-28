package globaz.osiris.db.services;

import java.io.File;
import java.util.Date;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CADirectory {
    private File m_file = null;

    CADirectory(File file) {
        m_file = file;
    }

    public String getFileName() {
        return m_file.getName();
    }

    public long getFileSize() {
        return m_file.length();
    }

    public Date getLastModified() {
        return new Date(m_file.lastModified());
    }

}
