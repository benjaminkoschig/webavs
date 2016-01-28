package globaz.aquila.ts.opge;

import globaz.aquila.ts.COTSFileUtil;
import globaz.globall.util.JADate;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class COTSOPGEFileUtil implements COTSFileUtil {

    private static final String RDP_FILE_NAME = "CIA";

    /**
     * Nom du fichier pour FER RDP. RDP_FILE_NAME + date jour (070101).
     * 
     * @see RDP_FILE_NAME
     */
    @Override
    public String getOutputFileName(JADate dateCreation) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");

        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.DATE, dateCreation.getDay());
        myCal.set(Calendar.MONTH, dateCreation.getMonth());
        myCal.set(Calendar.YEAR, dateCreation.getYear());

        return COTSOPGEFileUtil.RDP_FILE_NAME + formatter.format(myCal.getTime());
    }

}
