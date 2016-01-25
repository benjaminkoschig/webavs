package globaz.pavo.service.ebusiness;

import globaz.globall.db.BSession;

public interface CIEbusinessAccessInterface {
    /**
     * @param transaction
     * @param idPucsFile
     * @throws Exception
     */
    public void notifyFinishedPucsFile(String idPucsFile, String type, BSession session) throws Exception;
}
